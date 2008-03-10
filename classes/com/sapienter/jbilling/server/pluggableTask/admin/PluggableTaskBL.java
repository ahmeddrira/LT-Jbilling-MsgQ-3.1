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

package com.sapienter.jbilling.server.pluggableTask.admin;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;

public class PluggableTaskBL<T> {
    private static final Logger LOG = Logger.getLogger(PluggableTaskBL.class);     
    private EventLogger eLogger = null;

    private PluggableTaskDAS das = null;
    private PluggableTaskParameterDAS dasParameter = null;
    private PluggableTaskDTO pluggableTask = null;
    
    public PluggableTaskBL(Integer pluggableTaskId) 
            throws FinderException {
        init();
        set(pluggableTaskId);
    }
    
    public PluggableTaskBL() {
        init();
    }
    
    private void init() {
        eLogger = EventLogger.getInstance();        
        das = new PluggableTaskDAS();
        dasParameter = new PluggableTaskParameterDAS();
    }

    public void set(Integer id) throws FinderException {
        pluggableTask = das.find(id);
    }
    
    public void set(Integer entityId, Integer typeId) 
            throws FinderException {
        pluggableTask = das.findByEntityType(entityId, typeId);
    }

    public void set(PluggableTaskDTO task) throws FinderException {
        pluggableTask = task;
    }

    public PluggableTaskDTO getDTO() {
        return pluggableTask;
    }
    
    public void create(PluggableTaskDTO dto) {
        LOG.debug("Creating a new pluggable task row " + dto);
        pluggableTask = das.save(dto);
    }
    
    public void createParameter(Integer taskId, 
            PluggableTaskParameterDTO dto) {
        PluggableTaskDTO task = das.find(taskId);
        dto.setTask(task);
        task.getParameters().add(dasParameter.save(dto));

        // clear the rules cache (just in case this plug-in was ruled based)
        PluggableTask.invalidateRuleCache(taskId);
    }
    
    public void update(Integer executorId, PluggableTaskDTO dto) {
        if (dto == null || dto.getId() == null) {
            throw new SessionInternalError("task to update can't be null");
        }
        for (PluggableTaskParameterDTO param: dto.getParameters()) {
            param.expandValue();
        }
        pluggableTask = das.save(dto);
        
        eLogger.audit(executorId, Constants.TABLE_PLUGGABLE_TASK, 
                dto.getId(), EventLogger.MODULE_TASK_MAINTENANCE,
                EventLogger.ROW_UPDATED, null, null, null);
        // clear the rules cache (just in case this plug-in was ruled based)
        PluggableTask.invalidateRuleCache(dto.getId());
    }
    
    public void delete(Integer executor) {
        eLogger.audit(executor, Constants.TABLE_PLUGGABLE_TASK, 
                pluggableTask.getId(), EventLogger.MODULE_TASK_MAINTENANCE,
                EventLogger.ROW_DELETED, null, null, null);
        das.delete(pluggableTask);
        // clear the rules cache (just in case this plug-in was ruled based)
        PluggableTask.invalidateRuleCache(pluggableTask.getId());
    }

    public void deleteParameter(Integer executor, Integer id) {
        eLogger.audit(executor, Constants.TABLE_PLUGGABLE_TASK_PARAMETER, 
                id, EventLogger.MODULE_TASK_MAINTENANCE,
                EventLogger.ROW_DELETED, null, null, null);
        PluggableTaskParameterDTO toDelete = dasParameter.find(id);
        toDelete.getTask().getParameters().remove(toDelete);
        // clear the rules cache (just in case this plug-in was ruled based)
        PluggableTask.invalidateRuleCache(toDelete.getTask().getId());
        dasParameter.delete(toDelete);
    }


    public void updateParameters(PluggableTaskDTO dto) 
            throws FinderException {

        // update the parameters from the dto
        for (PluggableTaskParameterDTO parameter: dto.getParameters()) {
            updateParameter(parameter); 
        }
    }
    
    private void updateParameter(PluggableTaskParameterDTO dto) 
            throws FinderException {
        dto.expandValue();
        dasParameter.save(dto);
        // clear the rules cache (just in case this plug-in was ruled based)
        PluggableTask.invalidateRuleCache(dto.getTask().getId());
    }
    
    public T instantiateTask()
            throws PluggableTaskException {

        PluggableTaskDTO localTask = getDTO();
        String fqn = localTask.getType().getClassName();
        T result;
        try {
            Class taskClazz = Class.forName(fqn);
                    //.asSubclass(result.getClass());
            result = (T) taskClazz.newInstance();
        } catch (ClassCastException e) {
            throw new PluggableTaskException("Task id: " + pluggableTask.getId()
                    + ": implementation class does not implements PaymentTask:"
                    + fqn, e);
        } catch (InstantiationException e) {
            throw new PluggableTaskException("Task id: " + pluggableTask.getId()
                    + ": Can not instantiate : " + fqn, e);
        } catch (IllegalAccessException e) {
            throw new PluggableTaskException("Task id: " + pluggableTask.getId()
                    + ": Can not find public constructor for : " + fqn, e);
        } catch (ClassNotFoundException e) {
            throw new PluggableTaskException("Task id: " + pluggableTask.getId()
                    + ": Unknown class: " + fqn, e);
        }

        if (result instanceof PluggableTask) {
            PluggableTask pluggable = (PluggableTask) result;
            pluggable.initializeParamters(localTask);
        } else {
            throw new PluggableTaskException("Plug-in has to extend PluggableTask " + 
                    pluggableTask.getId());
        }
        return result;
    }
 
}
