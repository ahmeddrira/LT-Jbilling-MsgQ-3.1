package com.sapienter.jbilling.client.mediation;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.CrudAction;
import com.sapienter.jbilling.common.InvalidArgumentException;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.mediation.MediationSession;
import com.sapienter.jbilling.server.mediation.MediationSessionHome;
import com.sapienter.jbilling.server.mediation.db.MediationConfiguration;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;

public class ConfigurationAction extends CrudAction {

    //private static final Logger LOG = Logger.getLogger(TaskAction.class);
    private MediationSession configurationSession = null;
    
    public ConfigurationAction() {
        setFormName("configuration");
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            MediationSessionHome configurationHome = (MediationSessionHome) EJBFactory.lookUpHome(
                    MediationSessionHome.class,
                    MediationSessionHome.JNDI_NAME);
            configurationSession = configurationHome.create();
        } catch (Exception e) {
            throw new SessionInternalError("Initializing configuration action" + 
                    e.getMessage());
        }
    }
    
    public void setup() {
        try {
            List<MediationConfiguration> configs = configurationSession.getAllConfigurations(entityId);
            myForm.set("configurations", configs);
        } catch (RemoteException e) {
            throw new SessionInternalError("setup configuration action", ConfigurationAction.class, e);
        }

    }

    public Object editFormToDTO() {
        return myForm.get("configurations");
    }

    public void create(Object dtoHolder) {
        // nothing to de here
    }

    public String update(Object dtoHolder) {
        try {
            myForm.set("configurations",configurationSession.updateAllConfiguration(
                    executorId, (List) dtoHolder));
        } catch (Exception e) {
            if (e.getCause().getClass().equals(OptimisticLockException.class)) {
                setup();
                throw new OptimisticLockException();
            } else if (e.getCause().getClass().equals(InvalidArgumentException.class)) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("mediation.configuration.error"));
                return null;
            } else {
                throw new SessionInternalError("update configuration action", ConfigurationAction.class, e);
            }
        }
        return "mediation.configuration.updated";
    }

    public String delete() {
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            if (id != null) {
                configurationSession.delete(executorId, id);
                return "mediation.configuration.deleted";
            }
            return null;
        } catch (Exception e) {
            throw new SessionInternalError("delete configuration action" + 
                    e.getMessage());
        } 
    }

    public void reset() {
        // nothing here
    }

    public boolean otherAction(String action) {
        if (action.equals("add")) {
            // create a dummy configuration with default data
            MediationConfiguration config = new MediationConfiguration();
            config.setEntityId(entityId);
            config.setName("Configuration Name");
            config.setOrderValue(1);
            config.setCreateDatetime(Calendar.getInstance().getTime());
            PluggableTaskDTO task = new PluggableTaskDTO();
            task.setId(1);
            config.setPluggableTask(task);
            
            ((List<MediationConfiguration>) myForm.get("configurations")).add(config);
            
        } else {
            return false;
        }
        // forward this to a custom mapping, for refresh
        forward = "edit";
        return true;
    }

}
