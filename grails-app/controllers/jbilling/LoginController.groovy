package jbilling


import com.sapienter.jbilling.server.user.UserDTOEx
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.server.util.Constants
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.client.util.Constants

class LoginController {

    def userSession
	def webCaller

    def index = {
        def staticTitle =  "Welcome to jBilling 3!"
        [title: staticTitle]
        redirect(uri:"/login/login.gsp");
    }
    

	/*
	 * Goal: to have the login redirect to a welcome screen. This screen will list all the 
	 * active users (getUsersByStatus). For that, the web services bean will need to know about the caller.
	 */
    def login = {
        UserDTOEx user = new UserDTOEx();
        user.setUserName(params.userName);
        user.setPassword(params.password);
        user.setCompany(new CompanyDTO(Integer.valueOf(params.entityId)));

        Integer result = userSession.authenticate(user);
        if (result.equals(Constants.AUTH_OK)) {
			// save the user in the session
			// This is need also for the Filter that keeps the entity id available to the 
			// web service bean
			user = userSession.getGUIDTO(user.getUserName(), user.getEntityId());
			session[Constants.SESSION_USER_DTO] = user;
			
			// this user is logged. Populate the caller's IDs in the service bean
			webCaller.setCallerUserName(params.userName);
			webCaller.setCallerCompanyId(params.entityId as int);
			webCaller.setCallerId(user.getId());
			
			redirect(controller:"user", action: "list")
            
        } else {
			webCaller.reset();
            render "Login failed"
        }
    }
}
