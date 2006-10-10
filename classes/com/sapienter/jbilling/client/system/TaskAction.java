package com.sapienter.jbilling.client.system;

import java.rmi.RemoteException;

import com.sapienter.jbilling.client.util.CrudAction;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskParameterDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskSession;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskSessionHome;

public class TaskAction extends CrudAction {

    private PluggableTaskSession taskSession = null;
    
    public TaskAction() {
        setFormName("task");
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            PluggableTaskSessionHome taskHome = (PluggableTaskSessionHome) EJBFactory.lookUpHome(
                    PluggableTaskSessionHome.class,
                    PluggableTaskSessionHome.JNDI_NAME);
            taskSession = taskHome.create();
        } catch (Exception e) {
            throw new SessionInternalError("Initializing task action" + 
                    e.getMessage());
        }
    }
    
    public void setup() {
        try {
            PluggableTaskDTOEx[] dtos = taskSession.getAllDTOs(entityId);
            myForm.set("tasks", dtos);
        } catch (RemoteException e) {
            throw new SessionInternalError("setup task action" + 
                    e.getMessage());
        }

    }

    public Object editFormToDTO() {
        return myForm.get("tasks");
    }

    public void create(Object dtoHolder) {
        // TODO Auto-generated method stub

    }

    public String update(Object dtoHolder) {
        try {
            taskSession.updateAll(executorId, (PluggableTaskDTOEx[]) dtoHolder);
        } catch (RemoteException e) {
            throw new SessionInternalError("update task action" + 
                    e.getMessage());
        }
        return "system.task.updated";
    }

    public String delete() {
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            taskSession.delete(executorId, id);
        } catch (Exception e) {
            throw new SessionInternalError("delete task action" + 
                    e.getMessage());
        } 
        return "system.task.deleted";
    }

    public void reset() {
        // TODO Auto-generated method stub

    }

    public boolean otherAction(String action) {
        if (action.equals("add")) {
            // create a dummy task with default data
            PluggableTaskDTOEx dto = new PluggableTaskDTOEx();
            dto.setEntityId(entityId);
            dto.setProcessingOrder(new Integer(1));
            dto.setTypeId(new Integer(1));
            // call the server tier to get it into the data base
            try {
                taskSession.create(executorId, dto);
            } catch (RemoteException e) {
                throw new SessionInternalError(action + " task action", 
                        this.getClass(), e);
            }
        } else if (action.equals("addParameter")) {
            PluggableTaskParameterDTOEx dto = new PluggableTaskParameterDTOEx();
            dto.setStrValue("default");
            dto.setName("parameter_name");
            
            try {
                Integer id = Integer.valueOf(request.getParameter("id"));
                taskSession.createParameter(executorId, id, dto);
            } catch (RemoteException e) {
                throw new SessionInternalError(action + " task action" + 
                        e.getMessage());
            }
        } else if (action.equals("deleteParameter")) {
            try {
                Integer id = Integer.valueOf(request.getParameter("id"));
                taskSession.deleteParameter(executorId, id);
            } catch (RemoteException e) {
                throw new SessionInternalError(action + " task action" + 
                        e.getMessage());
            }
        } else {
            return false;
        }
        // remove the old from from the session
        session.removeAttribute(getFormName());
        // forward this to a custom mapping, for refresh
        forward = "added";
        return true;
    }

}
