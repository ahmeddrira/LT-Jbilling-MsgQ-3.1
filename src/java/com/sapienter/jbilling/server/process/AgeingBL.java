/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * Created on Mar 26, 2004
 */
package com.sapienter.jbilling.server.process;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.naming.NamingException;

import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.process.task.IAgeingTask;
import com.sapienter.jbilling.server.user.EntityBL;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO;
import com.sapienter.jbilling.server.notification.INotificationSessionBean;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.notification.NotificationNotFoundException;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.process.db.AgeingEntityStepDAS;
import com.sapienter.jbilling.server.process.db.AgeingEntityStepDTO;
import com.sapienter.jbilling.server.process.event.NewUserStatusEvent;
import com.sapienter.jbilling.server.system.event.EventManager;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.user.db.UserStatusDAS;
import com.sapienter.jbilling.server.user.db.UserStatusDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.util.PreferenceBL;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * @author Emil
 */
public class AgeingBL {
    private static final Logger LOG = Logger.getLogger(AgeingBL.class);

    private AgeingEntityStepDAS ageingDas = null;
    private AgeingEntityStepDTO ageing = null;
    private EventLogger eLogger = null;

    private static final ConcurrentMap<Integer, Boolean> running = new ConcurrentHashMap<Integer, Boolean>();

    public AgeingBL(Integer ageingId) {
        init();
        set(ageingId);
    }

    public AgeingBL() {
        init();
    }

    private void init() {
        eLogger = EventLogger.getInstance();        
        ageingDas = new AgeingEntityStepDAS();
    }

    public AgeingEntityStepDTO getEntity() {
        return ageing;
    }
    
    public void set(Integer id) {
        ageing = ageingDas.find(id);
    }

    public void reviewAll(Integer entityId, Date today) throws NamingException, SessionInternalError, SQLException {
        running.putIfAbsent(entityId, Boolean.FALSE);

        if (running.get(entityId)) {
            LOG.warn("Failed to trigger ageing review process at " + today + ", another process is already running.");
            return;

        } else {
            running.put(entityId, Boolean.TRUE);
        }

        CompanyDTO company = new EntityBL(entityId).getEntity();

        try {
            PluggableTaskManager<IAgeingTask> taskManager
                    = new PluggableTaskManager<IAgeingTask>(entityId, Constants.PLUGGABLE_TASK_AGEING);

            IAgeingTask task = taskManager.getNextClass();
            while (task != null) {
                task.reviewAllUsers(company.getId(), company.getAgeingEntitySteps(), today, null);
                task = taskManager.getNextClass();
            }

        } catch (PluggableTaskException e) {
            throw new SessionInternalError("Ageing task exception while running ageing review.", e);
        } finally {
            running.put(entityId, Boolean.FALSE);
        }
    }

    public void out(UserDTO user, Integer excludedInvoiceId) {
        try {
            PluggableTaskManager<IAgeingTask> taskManager
                    = new PluggableTaskManager<IAgeingTask>(user.getCompany().getId(), Constants.PLUGGABLE_TASK_AGEING);

            IAgeingTask task = taskManager.getNextClass();
            while (task != null) {
                task.removeUser(user, excludedInvoiceId, null);
                task = taskManager.getNextClass();
            }

        } catch (PluggableTaskException e) {
            throw new SessionInternalError("Ageing task exception when removing user from ageing.", e);
        }
    }

    public void setUserStatus(Integer executorId, Integer userId, Integer statusId, Date today) {
        UserDTO user = new UserDAS().find(userId);
        UserStatusDTO userStatus = new UserStatusDAS().find(statusId);

        try {
            PluggableTaskManager<IAgeingTask> taskManager
                    = new PluggableTaskManager<IAgeingTask>(user.getCompany().getId(), Constants.PLUGGABLE_TASK_AGEING);

            IAgeingTask task = taskManager.getNextClass();
            while (task != null) {
                task.setUserStatus(user, userStatus, today, executorId);
                task = taskManager.getNextClass();
            }

        } catch (PluggableTaskException e) {
            throw new SessionInternalError("Ageing task exception when setting user status.", e);
        }
    }

    public String getWelcome(Integer entityId, Integer languageId, Integer statusId) throws NamingException {
        AgeingEntityStepDTO step = new AgeingEntityStepDAS().findStep(entityId, statusId);
        ageing = ageingDas.find(step.getId());
        return ageing.getWelcomeMessage(languageId);
    }
    
    public AgeingDTOEx[] getSteps(Integer entityId, Integer executorLanguageId, Integer languageId) throws NamingException {
        AgeingDTOEx[] result  = new AgeingDTOEx[UserDTOEx.STATUS_DELETED.intValue()];
        
        // go over all the steps
        for (int step = UserDTOEx.STATUS_ACTIVE.intValue(); step <= UserDTOEx.STATUS_DELETED.intValue(); step++) {
            AgeingDTOEx newStep = new AgeingDTOEx();
            newStep.setStatusId(new Integer(step));
            UserStatusDTO statusRow = new UserStatusDAS().find(step);
            newStep.setStatusStr(statusRow.getDescription(executorLanguageId));
            newStep.setCanLogin(statusRow.getCanLogin());
            AgeingEntityStepDTO myStep = new AgeingEntityStepDAS().findStep(entityId, new Integer(step));

            if (myStep != null) { // it doesn't have to be there
                ageing = ageingDas.find(myStep.getId());

                newStep.setDays(ageing.getDays());
                newStep.setFailedLoginMessage(ageing.getFailedLoginMessage(languageId));
                newStep.setInUse(true);
                newStep.setWelcomeMessage(ageing.getWelcomeMessage(languageId));
            } else {
                newStep.setInUse(false);
            }
            result[step-1] = newStep;
        }
        
        return result;
    }
    
    public void setSteps(Integer entityId, Integer languageId, AgeingDTOEx[] steps) throws NamingException {
        for (AgeingDTOEx step : steps) {
            // get the existing data for this step
            AgeingEntityStepDTO myStep = new AgeingEntityStepDAS().findStep(entityId, step.getStatusId());
            if (myStep != null) {
                ageing = ageingDas.find(myStep.getId());
            } else {
                ageing = null;
            }

            if (!step.getInUse()) {
                // delete if not in use
                if (ageing != null) {
                    ageingDas.delete(ageing);
                }

            } else {
                // in use, create or update
                if (ageing == null) {
                    // create
                    ageingDas.create(entityId,
                                     step.getStatusId(),
                                     step.getWelcomeMessage(),
                                     step.getFailedLoginMessage(),
                                     languageId,
                                     step.getDays());

                } else {
                    // update
                    ageing.setDays(step.getDays());
                    ageing.setFailedLoginMessage(languageId, step.getFailedLoginMessage());
                    ageing.setWelcomeMessage(languageId, step.getWelcomeMessage());
                }
            }
        }
    }

}
