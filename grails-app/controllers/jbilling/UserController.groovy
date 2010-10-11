package jbilling

import com.sapienter.jbilling.server.user.IUserSessionBean;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.server.user.db.SubscriberStatusDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.user.ContactWS;

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
		newUser.setLanguageId(new Integer(1));
		newUser.setCurrencyId(1);
		newUser.setStatusId(1);//
		//set email
		try {			
			int id = webServicesSession.createUser(newUser);
			flash.message = message(code: 'user.create.success')
		} catch (Exception e) {
			e.printStackTrace();
			flash.message = message(code: 'user.create.failed')
		}
		flash.args= [params.userName]
		render( view:"user")
	}
	
	def edit = {
		//TODO get language id from the current logged in users id.
		//((UserDTOEx)session[Constants.SESSION_USER_DTO])?.getLanguageId()
		languageId = "1"
		
		// get the order session bean
		IUserSessionBean remoteUser = (IUserSessionBean) Context.getBean(
		Context.Name.USER_SESSION);
		UserDTOEx dto  = session.getAttribute(Constants.SESSION_CUSTOMER_DTO);
		//remoteUser.getUserDTOEx((Integer) session.getAttribute(Constants.SESSION_USER_ID));
		if (dto != null)
		{
			[user: dto]
		}
		render (view: "edit")
	}
	
	def cancel ={
		render ('Cancelled action')
	}
	
	def postEdit = { 
		def user = new UserWS();
		//		if (ccc.hasErrors()) {
		//			println "Errors found."
		//			[ user : ccc ]
		//			//render  (view: "edit")	
		//		} else {
		println "No errors."
		
		//set type Customer - Create/Edit Customer
		user.setMainRoleId(5);		
		
		if (params.ach?.abaRouting)
		{
			println "processing ach info..."
			AchDTO ach=new AchDTO();
			user.setAch(ach);
		}

		if (params.contact?.firstName) 
		{
			println "processing contact info..."
			ContactWS contact= new ContactWS();
			user.setContact(contact);
		}		
		
		if (params.creditCard?.number)
		{
			println "processing credit card info..."
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
		
		try {
			int id = webServicesSession.createUser(user);
			flash.message = message(code: 'user.create.success')
		} catch (Exception e) {
			e.printStackTrace();
			flash.message = message(code: 'user.create.failed')
		}
		flash.args= [params.userName]
		flash.user = newUser;
		render( view:"user")
		//		}	
		//		render "form posted successfully." 
		//		
		//		CreditCardDTO dto= new CreditCardDTO();
		//		AchDTO ach=new AchDTO();
		//		ContactWS contact= new ContactWS();
		//		user.setContact(contact);
		//		user.setCreditCard(dto);
		//		user.setAch(ach);
		//		
		//		bindData (user, params);
		//		println user.password
		//		println user.creditCard.number
		//		println user.ach.abaRouting
		//		println user.contact.address1
		
	}
}
