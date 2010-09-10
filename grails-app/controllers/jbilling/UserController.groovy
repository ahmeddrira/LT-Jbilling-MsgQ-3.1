package jbilling

import com.sapienter.jbilling.server.user.UserDTOEx;


class UserController {
	
	def webServicesSession

    def list = {
		// now find the list of users
		def users = webServicesSession.getUsersInStatus(UserDTOEx.STATUS_ACTIVE);
		// now redirect to the gsp view
		[ users: users] 
	}
}
