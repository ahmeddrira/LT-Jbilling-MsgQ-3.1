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

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.InvalidArgumentException;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.mediation.db.MediationConfiguration;
import com.sapienter.jbilling.server.mediation.db.MediationConfigurationDAS;
import com.sapienter.jbilling.server.mediation.db.MediationMapDAS;
import com.sapienter.jbilling.server.mediation.db.MediationOrderMap;
import com.sapienter.jbilling.server.mediation.db.MediationProcess;
import com.sapienter.jbilling.server.mediation.db.MediationProcessDAS;
import com.sapienter.jbilling.server.mediation.db.MediationRecordDAS;
import com.sapienter.jbilling.server.mediation.db.MediationRecordDTO;
import com.sapienter.jbilling.server.mediation.task.IMediationProcess;
import com.sapienter.jbilling.server.mediation.task.IMediationReader;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskBL;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDAS;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;

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
    
    private MediationSessionLocalHome myLocalHome = null;

    /**
     * @ejb:interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public void trigger() {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
        MediationProcessDAS processDAS = new MediationProcessDAS();
        Vector<String> errorMessages = new Vector<String>();
        MediationSessionLocal local = null;
        try {
			local = myLocalHome.create();
		} catch (CreateException e1) {
			throw new SessionInternalError("Getting local view", MediationSessionBean.class, e1);
		}

        LOG.debug("Running mediation trigger.");
        
        try {
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
                        // create a new process row. This happends in its own transactions
                    	// so it needs to be brought to the persistant context here again
                        MediationProcess process = local.createProcessRecord(cfg);
                        
                        int lastPosition = 0;
                        Vector<Record> thisGroup = new Vector<Record>();
                        for (Record record: reader) {
                            if (lastPosition >= record.getPosition()) {
                                // end of this group
                                // call the rules to get the records normalized
                                // plus the user id, item id and quantity
                                local.normalizeRecordGroup(processTask, executorId, process, 
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
                            local.normalizeRecordGroup(processTask, executorId, process, thisGroup, entityId, cfg);
                        }
                        
                        // save the information about this just ran mediation process in
                        // the mediation_process table
                        processDAS.reattach(process);
                        process.setEndDatetime(Calendar.getInstance().getTime());
                    } else {
                        LOG.error("skipping invalid reader " + cfg.getPluggableTask() + 
                                " error " + errorMessages);
                    }
                }
            }
        } catch (Exception e) {
            throw new SessionInternalError("Exception in mediation trigger", 
                    MediationSessionBean.class, e);
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
     * Needs to be in its own transaction, so it gets created right away
     * @ejb:interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public MediationProcess createProcessRecord(MediationConfiguration cfg) {
    	MediationProcessDAS processDAS = new MediationProcessDAS();
        MediationProcess process = new MediationProcess();
        process.setConfiguration(cfg);
        process.setStartDatetime(Calendar.getInstance().getTime());
        process.setOrdersAffected(0);
        process = processDAS.save(process);
        return process;
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public List<MediationProcess> getAll(Integer entityId) {
        MediationProcessDAS processDAS = new MediationProcessDAS();
        List<MediationProcess> result = processDAS.findAllByEntity(entityId);
        processDAS.touch(result);    
        return result;
        
    }

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public List<MediationConfiguration> getAllConfigurations(Integer entityId) {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
        List<MediationConfiguration> result = cfgDAS.findAllByEntity(entityId);
            
        return result;
    }

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void createConfiguration(MediationConfiguration cfg) {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();

        cfg.setCreateDatetime(Calendar.getInstance().getTime());
        cfgDAS.save(cfg);
            
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public List updateAllConfiguration(Integer executorId, List<MediationConfiguration> configurations) 
            throws InvalidArgumentException {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
        Vector<MediationConfiguration> retValue = new Vector<MediationConfiguration>();
        try {

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
            return retValue;
        } catch (EntityNotFoundException e1) {
            throw new InvalidArgumentException("Wrong data saving mediation configuration", 1, e1);
        } catch (InvalidArgumentException e2) {    
            throw new InvalidArgumentException(e2);
        } catch (Exception e) {
            throw new SessionInternalError("Exception updating mediation configurations ",
                    MediationSessionBean.class, e);
        } 
        /*
        eLogger.audit(executorId, Constants.TABLE_MEDIATION_CFG, 
                cfg.getId(), EventLogger.MODULE_MEDIATION,
                EventLogger.ROW_, null, null, null);
*/

    }

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void delete(Integer executorId, Integer cfgId) {
        MediationConfigurationDAS cfgDAS = new MediationConfigurationDAS();
            
        cfgDAS.delete(cfgDAS.find(cfgId));
        eLogger.audit(executorId, Constants.TABLE_MEDIATION_CFG, 
                cfgId, EventLogger.MODULE_MEDIATION,
                EventLogger.ROW_DELETED, null, null, null);
    }

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public boolean isBeenProcessed(
            MediationProcess process, Vector<Record> thisGroup) {
    	// validate that this group has not been already processed
    	MediationRecordDAS recordDas = new MediationRecordDAS();
    	String key = thisGroup.get(0).getKey();
    	if (recordDas.findNow(key) != null) {
    		LOG.debug("Detected duplicated of record: " + key);
    		return true;
    	}
    	MediationRecordDTO dbRecord = new MediationRecordDTO(
    			thisGroup.get(0).getKey(), // the keys are all the same withing a group
    			Calendar.getInstance().getTime(), process);
    	recordDas.save(dbRecord);
    	return false;
    }
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public void normalizeRecordGroup(IMediationProcess processTask, Integer executorId,
            MediationProcess process, Vector<Record> thisGroup, Integer entityId,
            MediationConfiguration cfg) 
            throws TaskException, FinderException, NamingException {
    	// validate that this group has not been already processed
        MediationSessionLocal local = null;
        try {
			local = myLocalHome.create();
		} catch (CreateException e1) {
			throw new SessionInternalError("Getting local view", MediationSessionBean.class, e1);
		}
		// this runs in its own transaction
		if (local.isBeenProcessed(process, thisGroup)) return;
		
        LOG.debug("Normalizing record ...");
        Vector<OrderLineDTO> lines = processTask.process(thisGroup, cfg.getName());
        Integer userId = processTask.getUserId();
        
        if (userId == null || lines == null || lines.size() == 0) {
            LOG.debug("No results from mediation process task " + thisGroup);
        } else {
        	// this process came from a different transaction (persistent context)
        	MediationProcessDAS  processDAS = new MediationProcessDAS();
        	processDAS.reattach(process);
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
                    thisGroup, processTask.getEventDate());
            process.setOrdersAffected(process.getOrdersAffected() + 1);
            
            // relate this order with this process
            MediationOrderMap map = new MediationOrderMap();
            map.setMediationProcessId(process.getId());
            map.setOrderId(order.getEntity().getId());
            MediationMapDAS mapDas = new MediationMapDAS();
            mapDas.save(map);
        }

        // mark the record as done
        MediationRecordDAS recordDAS = new MediationRecordDAS();
        MediationRecordDTO record = recordDAS.find(thisGroup.get(0).getKey());
        record.setFinished(Calendar.getInstance().getTime());
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

    public void setSessionContext(SessionContext ctx) throws EJBException,
            RemoteException {
    	// needed to make calls with new transactional boundries
    	myLocalHome = (MediationSessionLocalHome) ctx.getEJBLocalHome();
    }

    /**
     * @ejb:create-method view-type="remote"
     */
     public void ejbCreate() throws CreateException {
     }

}