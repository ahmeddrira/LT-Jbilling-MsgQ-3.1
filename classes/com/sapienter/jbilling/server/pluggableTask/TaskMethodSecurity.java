package com.sapienter.jbilling.server.pluggableTask;

import java.lang.reflect.Method;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.PermissionConstants;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.MethodBaseSecurityProxy;
import com.sapienter.jbilling.server.util.WSMethodSecurityProxy;

public class TaskMethodSecurity extends MethodBaseSecurityProxy {

    public void init(Class beanHome, Class beanRemote, Class beanLocalHome,
            Class beanLocal, Object securityMgr) throws InstantiationException {
        log = Logger.getLogger(WSMethodSecurityProxy.class);
        String methodName = null;
        try {
            Method methods[] = new Method[7];
            Method aMethod;
            int i = 0;

            // update
            Class params[] = new Class[2];
            params[0] = Integer.class;
            params[1] = PluggableTaskDTOEx.class;
            methodName = "update";
            aMethod = beanRemote.getDeclaredMethod(methodName, params);
            methods[i++] = aMethod;
            
            // updateAll
            params = new Class[2];
            params[0] = Integer.class;
            params[1] = PluggableTaskDTOEx[].class;
            methodName = "updateAll";
            aMethod = beanRemote.getDeclaredMethod(methodName, params);
            methods[i++] = aMethod;
            
            // create
            params = new Class[2];
            params[0] = Integer.class;
            params[1] = PluggableTaskDTOEx.class;
            methodName = "create";
            aMethod = beanRemote.getDeclaredMethod(methodName, params);
            methods[i++] = aMethod;

            // createParameter
            params = new Class[3];
            params[0] = Integer.class;
            params[1] = Integer.class;
            params[2] = PluggableTaskParameterDTOEx.class;
            methodName = "createParameter";
            aMethod = beanRemote.getDeclaredMethod(methodName, params);
            methods[i++] = aMethod;

            // delete
            params = new Class[2];
            params[0] = Integer.class;
            params[1] = Integer.class;
            methodName = "delete";
            aMethod = beanRemote.getDeclaredMethod(methodName, params);
            methods[i++] = aMethod;

            // deleteParameter
            params = new Class[2];
            params[0] = Integer.class;
            params[1] = Integer.class;
            methodName = "deleteParameter";
            aMethod = beanRemote.getDeclaredMethod(methodName, params);
            methods[i++] = aMethod;

            // updateParameters
            params = new Class[2];
            params[0] = Integer.class;
            params[1] = PluggableTaskDTOEx.class;
            methodName = "updateParameters";
            aMethod = beanRemote.getDeclaredMethod(methodName, params);
            methods[i++] = aMethod;

            // set the parent methods
            setMethods(methods);          

        } catch(NoSuchMethodException e) {
           String msg = "Failed to find method " + methodName;
           log.error(msg, e);
           throw new InstantiationException(msg);
        }
    }

    public void invoke(Method m, Object[] args, Object bean)
            throws SecurityException {
        if (!isMethodPresent(m)) {
            return;
        }
        // all methods for tasks need to have the executor id as the 
        // first parameter
        Integer userId = (Integer) args[0];
        // make sure this user has permisison first
        validatePermission(userId, PermissionConstants.P_TASK_MODIFY);
        if(m.getName().equals("update") ||
                m.getName().equals("updateParameters")) {
            PluggableTaskDTOEx dto = (PluggableTaskDTOEx) args[1];
            validate(userId, dto.getId());
        } else if(m.getName().equals("updateAll")) {
            PluggableTaskDTOEx dto[] = (PluggableTaskDTOEx[]) args[1];
            for (int f = 0; f < dto.length; f++) {
                validate(userId, dto[f].getId());
            }
        } else if(m.getName().equals("createParameter") || 
                m.getName().equals("delete")) {
            Integer taskId = (Integer) args[1];
            validate(userId, taskId);
        } else if(m.getName().equals("deleteParameter")) {
            Integer parameterId = (Integer) args[1];
            validateParameter(userId, parameterId);
        }
     }

    
    /**
     * Validates that the given user can modify the task
     * @param userId
     * @param taskId
     */
    private void validate(Integer userId, Integer taskId) {
        try {
            UserBL user = new UserBL(userId);
            PluggableTaskBL task = new PluggableTaskBL(taskId);
            if (!user.getEntity().getEntity().getId().equals(
                    task.getEntity().getEntityId())) {
                throw new SecurityException("Unauthorize access to user " + 
                        userId);
            }
        } catch (FinderException e) {
            throw new SecurityException("Row not present validating " + 
                    e.getMessage());
        }
    }
    
    private void validateParameter(Integer userId, Integer parameterId) {
        PluggableTaskBL task = new PluggableTaskBL();
        try {
            validate(userId, task.getParameterHome().findByPrimaryKey(
                    parameterId).getTask().getId());
        } catch (FinderException e) {
            throw new SecurityException("Row not present validating " + 
                    e.getMessage());
        }
    }
}
