package jbilling

import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.client.ViewUtils
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.user.db.SubscriberStatusDTO;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.contact.db.ContactTypeDTO;
import com.sapienter.jbilling.server.user.contact.db.ContactTypeDAS;

class CustomerInspectorController {
	
	IWebServicesSessionBean webServicesSession
	ViewUtils viewUtils
	
	def index = { 
		//render "nothing to show here."
		redirect (action: 'editNote', params:[id:webServicesSession.getCallerId()])
	}
	
	def show = {
		UserWS user= null
		OrderWS[] orders=new OrderWS[0]
		int userid
		def languageId= webServicesSession.getCallerLanguageId()?.toString()
		if (params["id"] && params["id"].matches("^[0-9]+")) {			
			userid= Integer.parseInt(params["id"])
			log.info "Method - show, inspecting user id=" + userid
			
			try {
				user = webServicesSession.getUserWS(userid)
				log.info "User found=" + user?.getContact()?.getFirstName()
				orders= webServicesSession.getUserSubscriptions(userid)
				log.info "found " + orders?.length + " orders"
			} catch (Exception e) {
				flash.message = message(code: 'user.not.found')
				flash.args= [params["id"]]
				redirect (controller:'user')
			}
		}
		boolean isAutoCC
		if ( Constants.AUTO_PAYMENT_TYPE_CC == user?.getAutomaticPaymentType())
		{
			log.info "Auto CC true"
			isAutoCC=true;
		}
		boolean isAutoAch
		if ( Constants.AUTO_PAYMENT_TYPE_ACH == user?.getAutomaticPaymentType() )
		{
			log.info "Auto Ach True"
			isAutoAch= true;
		}
		String expDate;
		if (null != user?.getCreditCard() && null != user?.getCreditCard()?.getNumber()) {
			Calendar cal= Calendar.getInstance();
			cal.setTime(user.getCreditCard().getExpiry())
			expDate="${1 + cal.get(Calendar.MONTH)}-"
			expDate+= cal.get(Calendar.YEAR)
		}
		log.info "Subscriber status id: " + user.getSubscriberStatusId()
		String subscribStatus="";
		for (SubscriberStatusDTO status: SubscriberStatusDTO.list() ){
			if (user.getSubscriberStatusId() == status.getId())
			subscribStatus= status.getDescription(Integer.parseInt(languageId))
		}
		//find all user contacts
		ContactWS[] contacts= webServicesSession.getUserContactsWS(user.getUserId())
		List<ContactWS> contactList= new ArrayList<ContactWS>(contacts.length);
		for (ContactWS contact:contacts) {
			if (user?.contact?.getId() != contact?.getId() )
			{
				ContactTypeDTO typeDto= new ContactTypeDAS().find(contact.getContactTypeId())
				log.info "Contact ID= " + contact?.getId() + " Contact Type ID="+ contact.getContactTypeId()
				log.info "TypeDto=" + typeDto + " LanguageId="+ languageId
				log.info "getting type descr=" + typeDto?.getDescription(languageId)
				if (typeDto)
				{
					contact.setContactTypeDescr(typeDto.getDescription(languageId))
				}
				contactList.add(contact)
			}
		}
		contacts= (ContactWS[]) contactList.toArray(new ContactWS[contactList.size()])
		
		log.info "Custom Contact fields=${user?.contact?.fieldNames}"
		[user:user, _id:userid, contacts:contacts, subscribStatus:subscribStatus, orders:orders, languageId:languageId, expDate:expDate, isAutoCC:isAutoCC,isAutoAch:isAutoAch]
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

	def invoices= {
		int _id= params._id?.toInteger();
		log.info "Redirecting to get invoices for id " + _id
		redirect (controller:"invoice", action: "lists", id:_id)
	}
	def payments= {
		int _id= params._id?.toInteger();
		log.info "Redirecting to get payments for id " + _id
		render "Show payments here."
	}
	def orders= {
		OrderWS[] orders=webServicesSession.getUserSubscriptions(params._id?.toInteger())
		log.info "found " + orders?.length + " orders"
		[orders:orders]
	}
	def editCustomer= {
		Integer userId= params._id?.toInteger()
		redirect(controller:"user", action:"edit",id:userId)
	}
	def makePayment= {
		render "make payment screen "
	}
	def createOrder= {
		render "create Order screen " 
	}
	
}
