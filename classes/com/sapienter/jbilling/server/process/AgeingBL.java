/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

/*
 * Created on Mar 26, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.process;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.AgeingEntityStepEntityLocal;
import com.sapienter.jbilling.interfaces.AgeingEntityStepEntityLocalHome;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.NotificationSessionLocal;
import com.sapienter.jbilling.interfaces.NotificationSessionLocalHome;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.interfaces.UserStatusEntityLocal;
import com.sapienter.jbilling.interfaces.UserStatusEntityLocalHome;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.notification.NotificationNotFoundException;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.EventLogger;
import com.sapienter.jbilling.server.util.PreferenceBL;

/**
 * @author Emil
 */
public class AgeingBL {

    private JNDILookup EJBFactory = null;
    private AgeingEntityStepEntityLocalHome ageingHome = null;
    private AgeingEntityStepEntityLocal ageing = null;
    private UserStatusEntityLocalHome userStatusHome = null;
    private Logger log = null;
    private EventLogger eLogger = null;

    public AgeingBL(Integer ageingId) 
            throws NamingException, FinderException {
        init();
        set(ageingId);
    }

    public AgeingBL() throws NamingException {
        init();
    }

    private void init() throws NamingException {
        log = Logger.getLogger(AgeingBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        ageingHome = (AgeingEntityStepEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                AgeingEntityStepEntityLocalHome.class,
                AgeingEntityStepEntityLocalHome.JNDI_NAME);

        userStatusHome = (UserStatusEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                UserStatusEntityLocalHome.class,
                UserStatusEntityLocalHome.JNDI_NAME);
    
    }

    public AgeingEntityStepEntityLocal getEntity() {
        return ageing;
    }
    
    public void set(Integer id) throws FinderException {
        ageing = ageingHome.findByPrimaryKey(id);
    }

    public void setUserStatus(Integer executorId, Integer userId, 
            Integer statusId, Date today) 
            throws FinderException, NamingException, SessionInternalError,
                CreateException, RemoveException {
        // find out if this user is not already in the required status
        UserBL user = new UserBL(userId);
        Integer originalStatusId = user.getEntity().getStatus().getId();
        if (originalStatusId.equals(statusId)) {
            return;
        }
         
        log.debug("Setting user " + userId + " status to " + statusId);
        // see if this guy could login in her present status
        boolean couldLogin = user.getEntity().getStatus().getCanLogin().
                intValue() == 1;
        
        // log an event
        if (executorId != null) {
            // this came from the gui
            eLogger.audit(executorId, Constants.TABLE_BASE_USER, 
                    user.getEntity().getUserId(), 
                    EventLogger.MODULE_USER_MAINTENANCE, 
                    EventLogger.STATUS_CHANGE, 
                    user.getEntity().getStatus().getId(), null, null);
        } else {
            // this is from a process, no executor involved
            eLogger.auditBySystem(user.getEntity().getEntity().getId(), 
                    Constants.TABLE_BASE_USER, 
                    user.getEntity().getUserId(), 
                    EventLogger.MODULE_USER_MAINTENANCE, 
                    EventLogger.STATUS_CHANGE, 
                    user.getEntity().getStatus().getId(), null, null);
        }
        
        // make the notification
        NotificationBL notification = new NotificationBL();
        try {
            MessageDTO message = notification.getAgeingMessage(
                    user.getEntity().getEntity().getId(), 
                    user.getEntity().getLanguageIdField(), statusId, userId);
     
            NotificationSessionLocalHome notificationHome =
                    (NotificationSessionLocalHome) EJBFactory.lookUpLocalHome(
                    NotificationSessionLocalHome.class,
                    NotificationSessionLocalHome.JNDI_NAME);
    
            NotificationSessionLocal notificationSess = 
                    notificationHome.create();
            notificationSess.notify(user.getEntity(), message);
        } catch (NotificationNotFoundException e) {
            log.warn("Changeing the satus of a user. An ageing notification " +
                    "should be " +
                    "sent to the user, but the entity doesn't have it. " +
                    "entity " + user.getEntity().getEntity().getId());
        }
 

        // make the change
        UserStatusEntityLocal status = userStatusHome.findByPrimaryKey(
                statusId);
        user.getEntity().setStatus(status);
        user.getEntity().setLastStatusChange(today);
        if (status.getId().equals(UserDTOEx.STATUS_DELETED)) {
            // yikes, it's out
            user.delete(executorId);
            return; // her orders were deleted, no need for any change in status
        }
        
        // see if this new status is suspended
        if (couldLogin && status.getCanLogin().intValue() == 0) {
            // all the current orders have to be suspended
            OrderBL order = new OrderBL();
            for (Iterator it = order.getHome().findByUser_Status(userId, 
                    Constants.ORDER_STATUS_ACTIVE).iterator(); 
                    it.hasNext();) {
                OrderEntityLocal orderRow = (OrderEntityLocal) it.next();
                order.set(orderRow);
                order.setStatus(executorId, 
                        Constants.ORDER_STATUS_SUSPENDED_AGEING);
            }               
        } else if (!couldLogin && status.getCanLogin().intValue() == 1) {
            // the oposite, it is getting out of the ageing process
            // all the suspended orders have to be reactivated
            OrderBL order = new OrderBL();
            for (Iterator it = order.getHome().findByUser_Status(userId, 
                    Constants.ORDER_STATUS_SUSPENDED_AGEING).iterator(); 
                    it.hasNext();) {
                OrderEntityLocal orderRow = (OrderEntityLocal) it.next();
                order.set(orderRow);
                order.setStatus(executorId, Constants.ORDER_STATUS_ACTIVE);
            }               
        }
        
        // make the http call back
        String url = null;
        try {
            PreferenceBL pref = new PreferenceBL();
            pref.set(user.getEntity().getEntity().getId(), 
                    Constants.PREFERENCE_URL_CALLBACK);
            url = pref.getString();
        } catch (FinderException e2) {
            // no call then
        }
        
        if (url != null && url.length() > 0) {
            // get the url connection
            try {
                log.debug("Making callback to " + url);
                
                // cook the parameters to be sent
                NameValuePair[] data = new NameValuePair[6];
                data[0] = new NameValuePair("cmd", "ageing_update");
                data[1] = new NameValuePair("user_id", userId.toString());
                data[2] = new NameValuePair("login_name", user.getEntity().getUserName());
                data[3] = new NameValuePair("from_status", originalStatusId.toString());
                data[4] = new NameValuePair("to_status", statusId.toString());
                data[5] = new NameValuePair("can_login", status.getCanLogin().toString());
                
                // make the call
                HttpClient client = new HttpClient();
                client.setConnectionTimeout(30000);
                PostMethod post = new PostMethod(url);
                post.setRequestBody(data);
                client.executeMethod(post);
            } catch (Exception e1) {
                log.info("Could not make call back. url = " + url + 
                        " Message:" + e1.getMessage());
            }

        }
    }
    
    /**
     * Takes a user out of the ageing system -> back to status active
     * but only if she doesn't have any outstanding invoices.
     * @param user
     */
    public void out(UserEntityLocal user, Integer excludigInvoiceId) 
            throws NamingException, CreateException, SessionInternalError,
                    FinderException, RemoveException, SQLException {
        // if the user is in the ageing process
        log.debug("Taking user " + user.getUserId() + " out of ageing");
        if (!user.getStatus().getId().equals(UserDTOEx.STATUS_ACTIVE)) {
            InvoiceBL invoices = new InvoiceBL();
            // only if the user doesn't have any more invoices that are overdue
            if (!invoices.isUserWithOverdueInvoices(user.getUserId(),
                    Calendar.getInstance().getTime(),
                    excludigInvoiceId).booleanValue()) {
                // good, no processable invoices for this guy
                setUserStatus(null, user.getUserId(),  UserDTOEx.STATUS_ACTIVE,
                        Calendar.getInstance().getTime());
            } else {
                log.debug("User with overdue invoices");
            }
        } else {
            log.debug("User already active");
        }

    }
    
    /**
     * Will move the user one step forward in the ageing proces ONLY IF
     * the user has been long enough in the present status. (for a user
     * in active status, it always moves it to the first ageing step).
     * @param userId
     * @throws NamingException
     * @throws FinderException
     * @throws SessionInternalError
     * @throws CreateException
     */
    public void age(UserEntityLocal user, Date today) 
            throws NamingException, FinderException, SessionInternalError,
                CreateException, RemoveException {
        log.debug("Ageing user:" + user.getUserId());
        Integer status = user.getStatus().getId();
        Integer nextStatus = null;
        if (status.equals(UserDTOEx.STATUS_ACTIVE)) {
            // welcome to the ageing process
            nextStatus = getNextStep(user.getEntity(),
                    UserDTOEx.STATUS_ACTIVE);
        } else {
            log.debug("she's already in the ageing");
            // this guy is already in the ageing
            try {
	            ageing = ageingHome.findStep(user.getEntity().getId(), 
	                    status);
            
	            // verify if it is time for another notch
	            GregorianCalendar cal = new GregorianCalendar();
	            Date lastChange = user.getLastStatusChange();
	            if (lastChange == null) {
	            	lastChange = user.getCreateDateTime();
	            }
	            cal.setTime(lastChange);
	            cal.add(Calendar.DATE, ageing.getDays().intValue());
	            log.debug("last time + days=" + cal.getTime() + " today " + today
	                    + "compare=" + cal.getTime().compareTo(today));
	            if (cal.getTime().compareTo(today) <= 0) {
	                nextStatus =  getNextStep(user.
	                        getEntity(),user.getStatus().getId());
	            } else {
	                return;
	            }
            } catch (FinderException e) {
            	// this user is an ageing status that has been removed.
            	// may be this is a bug, and a currently-in-use status
            	// should not be removable.
            	// Now it will simple jump to the next status.
            	nextStatus =  getNextStep(user.
                        getEntity(),user.getStatus().getId());
            }         
        }
        if (nextStatus != null) {
            setUserStatus(null, user.getUserId(), nextStatus, today);
        } else {
            eLogger.warning(user.getEntity().getId(), user.getUserId(), 
                    EventLogger.MODULE_USER_MAINTENANCE, 
                    EventLogger.NO_FURTHER_STEP,
                    Constants.TABLE_BASE_USER);
        }
    }
    
    /**
     * Give a current step, finds the next one. If there's none next, then
     * it returns null. The current step can be missing (for the case that
     * it has been deleted by the admin).
     * @param entity
     * @param statusId
     * @return
     * @throws SessionInternalError
     */
    private Integer getNextStep(EntityEntityLocal entity, Integer statusId) 
            throws SessionInternalError {
        // now how good would it be to have cmr with order by ?
        Vector steps = new Vector();
        for (Iterator it = entity.getSteps().iterator(); it.hasNext(); ) {
            ageing = (AgeingEntityStepEntityLocal) it.next();
            steps.add(ageing.getStatus().getId());
        }

        Collections.sort(steps);
        // this will return the next step, even if statusId doesn
        // exists in the current set of steps
    	for (Iterator it = steps.iterator(); it.hasNext(); ) {
    		Integer step = (Integer) it.next();
    		if (step.compareTo(statusId) > 0) {
    			return step;
    		}
    	}
    	
    	return null;
    }
    
    public static boolean isAgeingInUse(EntityEntityLocal entity) {
        return entity.getSteps().size() > 1;
    }
    
    /**
     * Goes over all the users that are not active, and calls age on them.
     * This doesn't discriminate over entities.
     */
    public void reviewAll(Date today) 
            throws NamingException, CreateException, FinderException,
                SessionInternalError, RemoveException, SQLException {
        UserBL user = new UserBL();
        Collection users;
        
        // go over all the users already in the ageing system
        try {
            users = user.getHome().findAgeing();
        } catch (FinderException e) {
            // if there's no users againg, it's ok
            return;
        }
        for (Iterator it = users.iterator(); it.hasNext(); ) {
            UserEntityLocal userRow = (UserEntityLocal) it.next();
            age(userRow, today);
        }
        
        // now go over the active users with payable invoices
        CachedRowSet usersSql;
        try {
            
            usersSql = user.findActiveWithOpenInvoices();
        } catch (Exception e) {
            // avoid further problems
            log.error("Exception finding users to age", e);
            return;
        }
        
        InvoiceBL invoiceBL = new InvoiceBL();
        
        while (usersSql.next()) {
            Integer userId = new Integer(usersSql.getInt(1));
            user.set(userId);
            UserEntityLocal userRow = user.getEntity();
            // get the grace period for the entity of this user
            PreferenceBL prefs = new PreferenceBL();
            prefs.set(userRow.getEntity().getId(), 
                    Constants.PREFERENCE_GRACE_PERIOD);
            int gracePeriod = prefs.getInt();
            log.debug("Reviewing invoices of user:" + userRow.getUserId() +
                    " grace: " + gracePeriod);
            // now go over this user's pending invoices
            for (Iterator it2 = invoiceBL.getHome().findProccesableByUser(
                    userRow.getUserId()).iterator(); it2.hasNext(); ) {
                InvoiceEntityLocal invoice = (InvoiceEntityLocal) it2.next();
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(invoice.getDueDate());
                if (gracePeriod > 0) {
                    cal.add(Calendar.DATE, gracePeriod);
                }
                
                if (userRow.getUserId().intValue() == 17) {
                    log.debug("invoice " + invoice.getId() + " due+grace=" + cal.getTime() +
                        " today=" + today + " compare=" + (cal.getTime().compareTo(today)));
                }
                    
                if (cal.getTime().compareTo(today) < 0) {
                    // ok, this user has an overdue invoice
                    age(userRow, today);        
                    break;
                }
            }  
        }
    }
    
    public String getWelcome(Integer entityId, Integer languageId, 
            Integer statusId) 
            throws NamingException, FinderException {
        ageing = ageingHome.findStep(entityId, statusId);
        return ageing.getWelcomeMessage(languageId);
    }
    
    public AgeingDTOEx[] getSteps(Integer entityId, 
            Integer executorLanguageId, Integer languageId) 
            throws NamingException, FinderException {
        AgeingDTOEx[] result  = new AgeingDTOEx[
                UserDTOEx.STATUS_DELETED.intValue()];
        
        // go over all the steps
        for (int step = UserDTOEx.STATUS_ACTIVE.intValue(); 
                step <= UserDTOEx.STATUS_DELETED.intValue(); step++) {
            AgeingDTOEx newStep = new AgeingDTOEx();
            newStep.setStatusId(new Integer(step));
            UserStatusEntityLocal statusRow = userStatusHome.findByPrimaryKey(
                    new Integer(step));
            newStep.setStatusStr(statusRow.getDescription(
                        executorLanguageId));
            newStep.setCanLogin(statusRow.getCanLogin());
            try {
                ageing = ageingHome.findStep(entityId, new Integer(step));
                newStep.setDays(ageing.getDays());
                newStep.setFailedLoginMessage(ageing.getFailedLoginMessage(
                        languageId));
                newStep.setInUse(new Boolean(true));
                newStep.setWelcomeMessage(ageing.getWelcomeMessage(
                        languageId));
            } catch (FinderException e) {
                newStep.setInUse(new Boolean(false));
            }
            result[step-1] = newStep;
        }
        
        return result;
    }
    
    public void setSteps(Integer entityId, Integer languageId, 
            AgeingDTOEx[] steps) 
            throws RemoveException, CreateException, NamingException {
        log.debug("Setting a total of " + steps.length + " steps");
        for (int f = 0; f < steps.length; f++) {
            // get the existing data for this step
            log.debug("Processing step[" + f + "]:" + steps[f].getStatusId());
            try {
                ageing = ageingHome.findStep(entityId, steps[f].getStatusId());
                log.debug("step present");
            } catch (FinderException e) {
                log.debug("step not present");
                ageing = null;
            }
            if (!steps[f].getInUse().booleanValue()) {
                // delete if now is not wanted
                if (ageing != null) {
                    log.debug("Removig step.");
                    ageing.remove();
                } 
            } else {
                // it is wanted
                log.debug("welcome = " + steps[f].getWelcomeMessage());
                if (ageing == null) {
                    // create
                    log.debug("Creating step.");
                    ageingHome.create(entityId, steps[f].getStatusId(), 
                            steps[f].getWelcomeMessage(), 
                            steps[f].getFailedLoginMessage(), 
                            languageId, steps[f].getDays());
                } else {
                    // update
                    log.debug("Updating step.");
                    ageing.setDays(steps[f].getDays());
                    ageing.setFailedLoginMessage(languageId,
                            steps[f].getFailedLoginMessage());
                    ageing.setWelcomeMessage(languageId, 
                            steps[f].getWelcomeMessage());
                }
            }
        }
    }

}
