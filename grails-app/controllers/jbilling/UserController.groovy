package jbilling

import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserWS;


class UserController {
	
	def webServicesSession
	def languageId= "1"

	
    def list = {
		// now find the list of users
		def users = webServicesSession.getUsersInStatus(UserDTOEx.STATUS_ACTIVE);
		// now redirect to the gsp view
		[ users: users] 
	}
	
	
	
	
	def index = {
		languageId= "1"
		render( view:"user")
	}
	
	def create = {
        UserWS newUser = new UserWS();
		bindData (newUser, params);
		
		//TODO We need to: Use the internationalization bundle to display those 
		//messages error/success messages
		//TODO Add Currency and User Type as drop down 
        newUser.setLanguageId(new Integer(1));
		newUser.setCurrencyId(1);
		newUser.setStatusId(1); //
        //set email
		try {			
			int id = webServicesSession.createUser(newUser);
			flash.message = message(code: 'user.create.success')
		} catch (Exception e) {
			e.printStackTrace();
			flash.message = message(code: 'user.create.failed')			
		}
		flash.args= [params.userName]
		flash.user = newUser;
		render( view:"user")
	}
}
