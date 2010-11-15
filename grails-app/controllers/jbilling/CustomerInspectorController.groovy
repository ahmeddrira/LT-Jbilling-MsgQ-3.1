package jbilling

import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.client.ViewUtils
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.order.OrderWS;

class CustomerInspectorController {
	
	IWebServicesSessionBean webServicesSession
	ViewUtils viewUtils
	
	def index = { 
		//render "nothing to show here."
		redirect (action: 'editNote', params:[id:webServicesSession.getCallerId()])
	}
	
	def open = {
		UserWS user= null
		OrderWS[] orders=new OrderWS[0]
		int userid
		def languageId= webServicesSession.getCallerLanguageId()?.toString()
		if (params["id"] && params["id"].matches("^[0-9]+")) {			
			userid= Integer.parseInt(params["id"])
			log.info "Method - open, inspecting user id=" + userid
			
			try {
				user = webServicesSession.getUserWS(userid)
				log.info "User found=" + user?.getContact()?.getFirstName()
				orders= webServicesSession.getUserOrders(userid)
				log.info "found " + orders?.length + " orders"
			} catch (Exception e) {
				flash.message = message(code: 'user.not.found')
				flash.args= [params["id"]]
			}
		}
		boolean isAutoCC
		if ( Constants.AUTO_PAYMENT_TYPE_CC == user.getAutomaticPaymentType())
		{
			log.info "Auto CC true"
			isAutoCC=true;
		}
		boolean isAutoAch
		if ( Constants.AUTO_PAYMENT_TYPE_ACH == user.getAutomaticPaymentType() )
		{
			log.info "Auto Ach True"
			isAutoAch= true;
		}
		String expDate;
		if (null != user.getCreditCard() && null != user.getCreditCard().getNumber()) {
			Calendar cal= Calendar.getInstance();
			cal.setTime(user.getCreditCard().getExpiry())
			expDate="${1 + cal.get(Calendar.MONTH)}-"
			expDate+= cal.get(Calendar.YEAR)
		}
		log.info "Subscriber status id: " + user.getSubscriberStatusId()		
		[user:user, _id:userid, orders:orders, languageId:languageId, expDate:expDate, isAutoCC:isAutoCC,isAutoAch:isAutoAch]
	}
	
	def editNote ={
		def user
		int userid
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			
			userid= Integer.parseInt(params["id"])
			
			try {
				user = webServicesSession.getUserWS(userid)
			} catch (Exception e) {
				flash.message = message(code: 'user.not.found')
				flash.args= [params["id"]]
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
		try {
			webServicesSession.saveCustomerNotes(params._id?.toInteger(), params.notes)
		} catch (SessionInternalError e) { 
			log.error e.getMessage()			
			render "[User Id " + params._id + "] " + e.getMessage()
		}
		render "save d"
	}
}
