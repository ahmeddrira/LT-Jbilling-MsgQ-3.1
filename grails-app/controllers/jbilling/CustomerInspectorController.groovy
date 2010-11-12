package jbilling

import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.client.ViewUtils

class CustomerInspectorController {
	
	IWebServicesSessionBean webServicesSession
	ViewUtils viewUtils

    def index = { 
		//render "nothing to show here."
		redirect (action: 'editNote', params:[id:webServicesSession.getCallerId()])
	}
	
	def inspector = {
		def user
		int userid
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			
			userid= Integer.parseInt(params["id"])
			
			try {
				user = webServicesSession.getUserWS(userid)
				session["editUser"]= user
			} catch (Exception e) {
				flash.message = message(code: 'user.not.found')
				flash.args= [params["id"]]
				redirect ( action:index)
			}
			if (user) {
				//CustomerDTO dto= CustomerDTO.findByBaseUser(new UserDTO(user.getUserId()));
				log.info "Editing = " + user?.getNotes()
			}
		}
		[id:userid,user:user]
	}
	
	def editNote ={
		def user
		int userid
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			
			userid= Integer.parseInt(params["id"])
			
			try {
				user = webServicesSession.getUserWS(userid)
				session["editUser"]= user
			} catch (Exception e) {
				flash.message = message(code: 'user.not.found')
				flash.args= [params["id"]]
				redirect ( action:index)
			}
			if (user) {
				//CustomerDTO dto= CustomerDTO.findByBaseUser(new UserDTO(user.getUserId()));				
				log.info "Editing = " + user?.getNotes()
			}
		}
		[id:userid,notes:user?.getNotes()]
	}
	
	def saveNote={
		log.info 'Notest= ' + params.notes + " for userid=" + params._id 
		webServicesSession.saveCustomerNotes(params._id?.toInteger(), params.notes)
		render "save d"
	}
}
