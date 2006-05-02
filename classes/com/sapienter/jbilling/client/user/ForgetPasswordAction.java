package com.sapienter.jbilling.client.user;

import java.io.IOException;

import javax.ejb.FinderException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.notification.NotificationNotFoundException;

public class ForgetPasswordAction extends Action {
	// Actionmethod for processing "forget password" operation
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    	Logger log = Logger.getLogger(ForgetPasswordAction.class);
    	ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
    	
    	UserLoginForm info = (UserLoginForm) form;
        String username = info.getUserName().trim();
        String entityId = info.getEntityId().trim();
    	
        log.debug("Received ForgetPasswordAction Request with userName " + 
        		username + " entityId " + entityId);
        
        // now do the call to the business object
        // get the value from a Session EJB 
        JNDILookup EJBFactory = null;
        try {
            EJBFactory = JNDILookup.getFactory(false);            
            UserSessionHome UserHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);

            UserSession myRemoteSession = UserHome.create();
            myRemoteSession.sendLostPassword(entityId, username);
        } catch (FinderException e) {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "forgetPassword.errors.nosuchuser"));
    	} catch( NotificationNotFoundException e) {
    		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                    "forgetPassword.errors.notificationnotactivated"));
    	} catch (Exception e) {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("all.internal"));
        }
    	
    	if (!errors.isEmpty()) {
    		saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
    	} else {
            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                    new ActionMessage("forgetPassword.ok"));
            saveMessages(request, messages);
        }
    	
    	return mapping.findForward("success");
    }
}
