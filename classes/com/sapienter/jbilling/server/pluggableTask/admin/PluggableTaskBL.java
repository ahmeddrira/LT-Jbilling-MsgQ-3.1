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

package com.sapienter.jbilling.server.pluggableTask.admin;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.EventLogger;

public class PluggableTaskBL {
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
        try {
            eLogger = EventLogger.getInstance();        
            das = new PluggableTaskDAS();
            dasParameter = new PluggableTaskParameterDAS();
        } catch (NamingException e) {
            throw new SessionInternalError("Constructing " + e.getMessage());
        }
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
    }
    
    public void update(Integer executorId, PluggableTaskDTO dto) {
        if (dto == null || dto.getId() == null) {
            throw new SessionInternalError("task to update can't be null");
        }
        LOG.debug("Updating task " + dto.getId());
        for (PluggableTaskParameterDTO param: dto.getParameters()) {
            param.expandValue();
        }
        pluggableTask = das.save(dto);
        
        eLogger.audit(executorId, Constants.TABLE_PLUGGABLE_TASK, 
                dto.getId(), EventLogger.MODULE_TASK_MAINTENANCE,
                EventLogger.ROW_UPDATED, null, null, null);
    }
    
    public void delete(Integer executor) {
        eLogger.audit(executor, Constants.TABLE_PLUGGABLE_TASK, 
                pluggableTask.getId(), EventLogger.MODULE_TASK_MAINTENANCE,
                EventLogger.ROW_DELETED, null, null, null);
        das.delete(pluggableTask);
    }

    public void deleteParameter(Integer executor, Integer id) {
        eLogger.audit(executor, Constants.TABLE_PLUGGABLE_TASK_PARAMETER, 
                id, EventLogger.MODULE_TASK_MAINTENANCE,
                EventLogger.ROW_DELETED, null, null, null);
        PluggableTaskParameterDTO toDelete = dasParameter.find(id);
        toDelete.getTask().getParameters().remove(toDelete);
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
    }
}
