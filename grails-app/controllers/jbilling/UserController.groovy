package jbilling

import java.util.Calendar;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.user.IUserSessionBean;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.server.user.db.SubscriberStatusDTO;
import com.sapienter.jbilling.client.ViewUtils 
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.util.IWebServicesSessionBean;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.user.UserBL
import grails.plugins.springsecurity.Secured;

@Secured(['isAuthenticated()'])
class UserController {
	
	IWebServicesSessionBean webServicesSession
	ViewUtils viewUtils
	def languageId= "1"
	def isAutoCC= false
	def isAutoAch= false
	
	def list = {
		// now find the list of users
		def users = webServicesSession.getUsersInStatus(UserDTOEx.STATUS_ACTIVE);
		// now redirect to the gsp view
		[ users: users] 
	}
	
	def index = {
		//UserBL userbl = new UserBL(webServicesSession.getCallerId());
		//languageId = String.valueOf(userbl.getEntity().getLanguageIdField());
		languageId= webServicesSession.getCallerLanguageId().toString()
		render( view:"user")
	}
	
	def create = {
		UserWS newUser = new UserWS();
		bindData (newUser, params);
		ContactWS contact = new ContactWS()		
		bindData(contact, params)
		log.info "Email: " + contact.getEmail()
		newUser.setContact(contact);
		//TODO Set languageId of the logged-in user instead?
		newUser.setLanguageId(new Integer(1));
		//TODO Set the currency Id of the logged-in user?
		newUser.setCurrencyId(1);
		newUser.setStatusId(1);//active
		//set email
		try {			
			int id = webServicesSession.createUser(newUser);
			flash.message = message(code: 'user.create.success')
		} catch (SessionInternalError e) {
            boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);
		} catch (Exception e) {
			flash.message = message(code: 'user.create.failed')
		}
		flash.args= [params.userName]
		render( view:"user")
	}
	
	def edit = {
		//UserBL userbl = new UserBL(webServicesSession.getCallerId());
		//languageId = String.valueOf(userbl.getEntity().getLanguageIdField());
		languageId= webServicesSession.getCallerLanguageId().toString()

		UserWS user = null;
		def notes= null;
		def expMnth, expYr;
		
		if (params["id"] && params["id"].matches("^[0-9]+")) {
			
			int id= Integer.parseInt(params["id"])
			
			try {
				user = webServicesSession.getUserWS(id)
				session["editUser"]= user
			} catch (Exception e) {
				flash.message = message(code: 'user.not.found')
				flash.args= [params["id"]]
				redirect ( action:index)
			}
			if (user) {
				//CustomerDTO dto= CustomerDTO.findByBaseUser(new UserDTO(user.getUserId()));
				notes= user?.getNotes();
				if ( Constants.AUTO_PAYMENT_TYPE_CC == user.getAutomaticPaymentType())
				{
					log.info "Edit. Auto CC true"
					isAutoCC=true;
				} 
				if ( Constants.AUTO_PAYMENT_TYPE_ACH == user.getAutomaticPaymentType() )
				{
					log.info "Edit. Auto Ach True"
					isAutoAch= true;
				}
				log.info  "retrieved notes "  + user?.getNotes()
				if (null != user.getCreditCard() && null != user.getCreditCard().getNumber()) {
					Calendar cal= Calendar.getInstance();
					cal.setTime(user.getCreditCard().getExpiry())
					expMnth= 1 + cal.get(Calendar.MONTH)
					expYr= cal.get(Calendar.YEAR)
				}
				log.info  "Displaying user " + user.getUserId()				
			}
		}
		
		if (session["editUser"]) log.info  "User exists...."
				
		return [user:user, isAutoCC:isAutoCC, isAutoAch:isAutoAch, languageId:languageId, notes:notes, expiryMonth:expMnth, expiryYear:expYr ]
	}
	
	def cancel ={ render ('Cancelled action') }
	
	def postEdit = { 
		
		UserWS user= session["editUser"]
		def userExists= true;
		if ( user == null ) {
			userExists=false;
			user= new UserWS()
		}
		log.info  "No errors. User exists=" + userExists
			
		//set type Customer - Create/Edit Customer
		user.setMainRoleId(5);		
		
		log.info  "processing contact info..."
		ContactWS contact= new ContactWS();
		user.setContact(contact);
		
		if (params.ach?.abaRouting)
		{
			log.info  "processing ach info..."
			AchDTO ach=new AchDTO();
			user.setAch(ach);
		}
		
		if (params.creditCard?.number) {
			log.info  "processing credit card info..."
			CreditCardDTO dto= new CreditCardDTO();
			user.setCreditCard(dto);
			user.setPassword(params.newPassword);
			Calendar cal= Calendar.getInstance();
			cal.set(Calendar.MONTH, Integer.parseInt(params.creditCard.month));
			cal.set(Calendar.YEAR, Integer.parseInt(params.creditCard.year));
			cal.roll(Calendar.MONTH, false);
			int lastDate = cal.getActualMaximum(Calendar.DATE);
			cal.set(Calendar.DATE, lastDate);
			user.creditCard.setExpiry(cal.getTime());
		}
		
		bindData(user, params)
		
		user.setNotes(params.notes)
		if (params.isAutomaticPaymentCC == "on") {
			user.setAutomaticPaymentType(Constants.AUTO_PAYMENT_TYPE_CC)
			log.info "setting CC for auto"
		} else if (params.isAutomaticPaymentAch == "on") {
			user.setAutomaticPaymentType(Constants.AUTO_PAYMENT_TYPE_ACH)
			log.info "setting ACH for auto"
		}
		log.info  "Saving ach accountType as " + user?.getAch()?.getAccountType()
//		log.info  "or " + params.ach.accountType
		
		try {
			if (userExists) {
				webServicesSession.updateUser(user)
				log.info  "Updating ach info separately..."
				if ( ach || ach.getAbaRouting() || ach.getBankAccount() )
				{
					webServicesSession.updateAch(user.getUserId(), user.getAch());				
				}
				flash.message = message(code: 'user.update.success')
			} else {
				int id = webServicesSession.createUser(user);
				flash.message = message(code: 'user.create.success')
			}
			log.info  params.isAutomaticPaymentCC
			log.info  params.isAutomaticPaymentAch

		} catch (SessionInternalError e) {
		    // TODO: the locale like this is not working, and it is messy. Once we have
		    // the one resolved by jBilling in the session, add that here.
			boolean retValue = viewUtils.resolveExceptionForValidation(flash, session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE', e);
		} catch (Exception e) {
			e.printStackTrace();
			flash.message = message(code: 'user.create.failed')
		}
		session["editUser"]= null;
		flash.args= [params.userName]
		//flash.user = newUser;
		render( view:"user")
	}
}
