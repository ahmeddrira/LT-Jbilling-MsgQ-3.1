/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.sapienter.jbilling.server.mediation;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.InvalidArgumentException;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.mediation.db.MediationConfiguration;
import com.sapienter.jbilling.server.mediation.db.MediationConfigurationDAS;
import com.sapienter.jbilling.server.mediation.db.MediationMapDAS;
import com.sapienter.jbilling.server.mediation.db.MediationOrderMap;
import com.sapienter.jbilling.server.mediation.db.MediationProcess;
import com.sapienter.jbilling.server.mediation.db.MediationProcessDAS;
import com.sapienter.jbilling.server.mediation.task.IMediationProcess;
import com.sapienter.jbilling.server.mediation.task.IMediationReader;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderLineDTOEx;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskBL;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDAS;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.EventLogger;
import com.sapienter.jbilling.server.util.db.DBUtil;

/**
*
* @author emilc
* @ejb:bean name="MediationSession"
*           display-name="A stateless bean for mediation"
*           type="Stateless"
*           transaction-type="Container"
*           view-type="both"
*           jndi-name="com/sapienter/jbilling/server/mediation/MediationSession"
* 
* 
**/
public class MediationSessionBean implements SessionBean {
    
    private static final Logger LOG = Logger.getLogger(MediationSessionBean.class);
    private static final EventLogger eLogger = EventLogger.getInstance();

    /**
     * @ejb:interface-method view-type="both"
     */
    public void trigger() {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
        MediationProcessDAS processDAS = new MediationProcessDAS();
        Vector<String> errorMessages = new Vector<String>();
        EntityTransaction tx = DBUtil.getTransaction();

        LOG.debug("Running mediation trigger.");
        
        try {
            tx.begin();
            EntityBL entityBL = new EntityBL();
            
            // loop over all the entities
            for (Integer entityId: entityBL.getAllIDs()) {
                LOG.debug("Processing entity " + entityId);
                // get only once the task that will be needed to normalize records
                PluggableTaskManager<IMediationProcess> tm = 
                        new PluggableTaskManager<IMediationProcess>(entityId, 
                                Constants.PLUGGABLE_TASK_MEDIATION_PROCESS);
                IMediationProcess processTask = tm.getNextClass();
                if (processTask == null) {
                    LOG.debug("Entity " + entityId + " does not have a mediation process plug-in");
                    continue;
                }
                // find the root user of this entity. It will be the executor for the order updates
                Integer executorId = entityBL.getRootUser(entityId);
                // now process this entity
                // go over each mediation configuration. An entity can opt out from 
                // mediation buy simply not having any configuration present
                for (MediationConfiguration cfg: cfgDAS.findAllByEntity(entityId)) {
                    LOG.debug("Now using configuration " + cfg);
                    PluggableTaskBL<IMediationReader> taskManager = 
                        new PluggableTaskBL<IMediationReader>();
                    taskManager.set(cfg.getPluggableTask());
                    IMediationReader reader = taskManager.instantiateTask();

                    if (reader.validate(errorMessages)) {
                        // there is going to be records processed from this configuration
                        // create a new process row
                        MediationProcess process = new MediationProcess();
                        process.setConfiguration(cfg);
                        process.setStartDatetime(Calendar.getInstance().getTime());
                        process.setOrdersAffected(0);
                        process = processDAS.save(process);
                        
                        int lastPosition = 0;
                        Vector<Record> thisGroup = new Vector<Record>();
                        for (Record record: reader) {
                            if (lastPosition >= record.getPosition()) {
                                // end of this group
                                // call the rules to get the records normalized
                                // plus the user id, item id and quantity
                                normalizeRecordGroup(processTask, executorId, process, 
                                        thisGroup, entityId, cfg);
                                // start again
                                thisGroup.clear();
                            }
                            LOG.debug("Now processing record " + record);
                            thisGroup.add(record);
                            lastPosition = record.getPosition();
                        }
                        
                        // send the last record/s as well
                        if (thisGroup.size() > 0) {
                            normalizeRecordGroup(processTask, executorId, process, thisGroup, entityId, cfg);
                        }
                        
                        // save the information about this just ran mediation process in
                        // the mediation_process table
                        process.setEndDatetime(Calendar.getInstance().getTime());
                    } else {
                        LOG.error("skipping invalid reader " + cfg.getPluggableTask() + 
                                " error " + errorMessages);
                    }
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception in mediation trigger", 
                    MediationSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
        
        if (!errorMessages.isEmpty()) {
            StringBuffer buf = new StringBuffer("Wrong configuration of reader plugin\n");
            for (String message: errorMessages) {
                buf.append("ERROR: " + message + "\n");
            }
            throw new SessionInternalError(buf.toString());
        }
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     */
    public List<MediationProcess> getAll(Integer entityId) {
        MediationProcessDAS processDAS = new MediationProcessDAS();
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();
            List<MediationProcess> result = processDAS.findAllByEntity(entityId);
            
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception in mediation trigger", 
                    MediationSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
        
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public List<MediationConfiguration> getAllConfigurations(Integer entityId) {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();
            List<MediationConfiguration> result = cfgDAS.findAllByEntity(entityId);
            
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception in getting mediation configuration " +
                    "for entity " + entityId, MediationSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
        
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void createConfiguration(MediationConfiguration cfg) {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();

            cfg.setCreateDatetime(Calendar.getInstance().getTime());
            cfgDAS.save(cfg);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception in creating mediation configuration " +
                     cfg, MediationSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     */
    public List updateAllConfiguration(Integer executorId, List<MediationConfiguration> configurations) 
            throws InvalidArgumentException {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
        EntityTransaction tx = DBUtil.getTransaction();
        Vector<MediationConfiguration> retValue = new Vector<MediationConfiguration>();
        try {
            tx.begin();

            for (MediationConfiguration cfg: configurations) {
                // if the configuration is new, the task needs to be loaded
                if (cfg.getPluggableTask().getEntityId() == null) {
                    PluggableTaskDAS pt = new PluggableTaskDAS();
                    PluggableTaskDTO task = pt.find(cfg.getPluggableTask().getId());
                    if (task != null && task.getEntityId().equals(cfg.getEntityId())) {
                        cfg.setPluggableTask(task);
                    } else {
                        throw new InvalidArgumentException("Task not found or " +
                                "entity of pluggable task is not the same when " +
                                "creating a new mediation configuration", 1);
                    }
                }
                retValue.add(cfgDAS.save(cfg));
            }
            tx.commit();
            return retValue;
        } catch (EntityNotFoundException e1) {
            if (tx.isActive()) tx.rollback();
            throw new InvalidArgumentException("Wrong data saving mediation configuration", 1, e1);
        } catch (InvalidArgumentException e2) {    
            if (tx.isActive()) tx.rollback();
            throw new InvalidArgumentException(e2);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception updating mediation configurations ",
                    MediationSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
        /*
        eLogger.audit(executorId, Constants.TABLE_MEDIATION_CFG, 
                cfg.getId(), EventLogger.MODULE_MEDIATION,
                EventLogger.ROW_, null, null, null);
*/

    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void delete(Integer executorId, Integer cfgId) {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
        EntityTransaction tx = DBUtil.getTransaction();
        try {
            tx.begin();
            
            cfgDAS.delete(cfgDAS.find(cfgId));
            tx.commit();
            eLogger.audit(executorId, Constants.TABLE_MEDIATION_CFG, 
                    cfgId, EventLogger.MODULE_MEDIATION,
                    EventLogger.ROW_DELETED, null, null, null);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new SessionInternalError("Exception updating mediation configurations ",
                    MediationSessionBean.class, e);
        } finally {
            DBUtil.finishSession();
        }
    }
    
    private void normalizeRecordGroup(IMediationProcess processTask, Integer executorId,
            MediationProcess process, Vector<Record> thisGroup, Integer entityId,
            MediationConfiguration cfg) 
            throws TaskException, FinderException, NamingException {
        LOG.debug("Normalizing record ...");
        Vector<OrderLineDTOEx> lines = processTask.process(thisGroup, cfg.getName());
        Integer userId = processTask.getUserId();
        
        if (userId == null || lines == null || lines.size() == 0) {
            LOG.debug("No results from mediation process task " + thisGroup);
        } else {
        
            // determine the currency for this event
            Integer currencyId = processTask.getCurrencyId();
            if (currencyId == null) {
                UserBL user = new UserBL(userId);
                currencyId = user.getCurrencyId(); 
            }
            
            // add the lines to the customer's current order
            OrderBL order = new OrderBL();
            order.setUserCurrent(userId);
            order.updateCurrent(entityId, executorId, userId, currencyId, lines,
                    thisGroup);
            process.setOrdersAffected(process.getOrdersAffected() + 1);
            
            // relate this order with this process
            MediationOrderMap map = new MediationOrderMap();
            map.setMediationProcessId(process.getId());
            map.setOrderId(order.getEntity().getId());
            MediationMapDAS mapDas = new MediationMapDAS();
            mapDas.save(map);
        }

    }
    /*
     * EJB 2.1 required methods ...
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void setSessionContext(SessionContext arg0) throws EJBException,
            RemoteException {
    }

    /**
     * @ejb:create-method view-type="remote"
     */
     public void ejbCreate() throws CreateException {
     }

}
