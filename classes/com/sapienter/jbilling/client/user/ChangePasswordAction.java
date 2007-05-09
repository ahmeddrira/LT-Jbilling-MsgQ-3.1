package com.sapienter.jbilling.client.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.user.UserDTOEx;

public class ChangePasswordAction extends Action {
    private static Logger LOG = Logger.getLogger(ChangePasswordAction.class);
    
    public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        ActionErrors errors = new ActionErrors();
        DynaValidatorForm info = (DynaValidatorForm) form;
        String password = (String) info.get("password");
        String verifyPassword = (String) info.get("verifyPassword");
        
        if (!password.equals(verifyPassword)) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("user.create.error.password_match"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute(Constants.SESSION_USER_ID);

        JNDILookup EJBFactory = null;
        UserSession myRemoteSession = null;
        UserDTOEx user = null;

        try {
            EJBFactory = JNDILookup.getFactory(false);            
            UserSessionHome UserHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);

            myRemoteSession = UserHome.create();
            user = myRemoteSession.getUserDTOEx(userId);
            
            // validate that the new password is different than the current one
            user.setPassword(password);
            user = myRemoteSession.authenticate(user);
            if (user != null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("errors.repeated", "New password"));
            }
        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("all.internal"));
        }
        
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        try {
            // do the actual password change
            user = myRemoteSession.getUserDTOEx(userId);
            user.setPassword(password);
            myRemoteSession.update(userId, user);

            // I still need to call this method, because it populates the dto
            // with the menu and other fields needed for the login
            user = myRemoteSession.authenticate(user);
        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("all.internal"));
        }

        LOG.debug("Password changed for user " + userId);
        // now get the session completed. The user is actually logged only now.
        UserLoginAction.logUser(session, user);
        return (mapping.findForward("success"));

    }
}
