/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

package com.sapienter.jbilling.client.user;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.CrudActionBase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.PaymentSession;
import com.sapienter.jbilling.interfaces.PaymentSessionHome;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.user.UserDTOEx;

public class CreditCardMaintainAction extends
		CrudActionBase<CreditCardCrudContext> {
	private static final String FORM_CREDIT_CARD = "creditCard";

	private static final String FIELD_NUMBER = "number";
	private static final String FIELD_GROUP_EXPIRY = "expiry";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_USE_CARD = "chbx_use_this";

	private static final String MESSAGE_UPDATE_SUCCESS = "creditcard.update.done";
	private static final String FORWARD_EDIT = "creditCard_edit";
	private static final String FORWARD_DONE = "creditCard_done";
	private static final String FORWARD_DELETED = "creditCard_deleted";

	private final UserSession myUserSession;
	private final PaymentSession myPaymentSession;

	public CreditCardMaintainAction() {
		super(FORM_CREDIT_CARD, "credit card");
		try {
			JNDILookup EJBFactory = JNDILookup.getFactory(false);
			UserSessionHome userHome = (UserSessionHome) EJBFactory.lookUpHome(
					UserSessionHome.class, UserSessionHome.JNDI_NAME);

			PaymentSessionHome paymentHome = (PaymentSessionHome) EJBFactory
					.lookUpHome(PaymentSessionHome.class,
							PaymentSessionHome.JNDI_NAME);

			myUserSession = userHome.create();
			myPaymentSession = paymentHome.create();

		} catch (Exception e) {
			throw new SessionInternalError(
					"Initializing credit card CRUD action: " + e.getMessage());
		}
	}

	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
		Integer userId = customGetUserId();
		// now only one credit card is supported per user
		CreditCardDTO dto = myUserSession.getCreditCard(userId);
		Integer type = myUserSession.getAuthPaymentType(userId);
		boolean use = Constants.AUTO_PAYMENT_TYPE_CC.equals(type);
		if (dto != null) { // it could be that the user has no cc yet
			String ccNumber = maskCreditCardNumberIfNeeded(dto);
			myForm.set(FIELD_NUMBER, ccNumber);
			setFormDate(FIELD_GROUP_EXPIRY, dto.getExpiry());
			myForm.set(FIELD_NAME, dto.getName());
			myForm.set(FIELD_USE_CARD, use);
		} else {
			// we will use empty field number as a flag for postSetup();
			// @see postSetup
			myForm.set(FIELD_NUMBER, "");
		}
		return new ForwardAndMessage(FORWARD_EDIT);
	}

	@Override
	protected void postSetup() {
		super.postSetup();
		// we are using an empty FIELD_NUMBER as a flag indicating
		// that DTO were not found
		// in any case, empty FIELD_NUMBER means that something is wrong
		// so it makes sence to remove the cached form anyway
		// @see setup()

		if ("".equals(myForm.get(FIELD_NUMBER))) {
			removeFormFromSession();
		}
	}

	@Override
	protected void preEdit() {
		super.preEdit();
		// this default forward used, e.g, in case of validation problems
		String forwardOnValidationProblem = FORWARD_EDIT;
		setForward(forwardOnValidationProblem);
	}

	@Override
	protected CreditCardCrudContext doEditFormToDTO() throws RemoteException {
		CreditCardDTO dto = new CreditCardDTO();
		dto.setName((String) myForm.get(FIELD_NAME));
		dto.setNumber((String) myForm.get(FIELD_NUMBER));
		myForm.set(FIELD_GROUP_EXPIRY + "_day", "01"); // to complete the date
		dto.setExpiry(parseDate(FIELD_GROUP_EXPIRY, "payment.cc.date"));

		// validate the expiry date
		if (dto.getExpiry() != null) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(dto.getExpiry());
			cal.add(GregorianCalendar.MONTH, 1); // add 1 month
			if (Calendar.getInstance().getTime().after(cal.getTime())) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"creditcard.error.expired", "payment.cc.date"));
			}
		}

		// the credit card number is required
		required(dto.getNumber(), "payment.cc.number");
		if (errors.isEmpty()) {
			// verify that this entity actually accepts this kind of
			// payment method
			Integer paymentMethod = Util.getPaymentMethod(dto.getNumber());
			if (!myPaymentSession.isMethodAccepted(entityId, paymentMethod)) {
				errors.add( //
						ActionErrors.GLOBAL_ERROR, //
						new ActionError("payment.error.notAccepted",
								"payment.method"));
			}
		}

		CreditCardCrudContext result = new CreditCardCrudContext(dto);
		// update the autimatic payment type for this customer
		Boolean shouldUse = (Boolean) myForm.get(FIELD_USE_CARD);
		if (shouldUse != null) {
			result.setIsAutomaticPayment(shouldUse);
		}

		return result;
	}

	@Override
	protected ForwardAndMessage doUpdate(CreditCardCrudContext dto)
			throws RemoteException {
		Integer userId = commonGetUserId();
		myUserSession.updateCreditCard(executorId, userId, dto.getDto());
		myUserSession.setAuthPaymentType(userId,
				Constants.AUTO_PAYMENT_TYPE_CC, dto.isAutomaticPayment());
		return new ForwardAndMessage(FORWARD_DONE, MESSAGE_UPDATE_SUCCESS);
	}

	@Override
	protected ForwardAndMessage doDelete() throws RemoteException {
		myUserSession.deleteCreditCard(executorId, commonGetUserId());
		// no need to modify the auto payment type. If it is cc and
		// there's no cc the payment will be bypassed
		return new ForwardAndMessage(FORWARD_DELETED);
	}

	@Override
	protected ForwardAndMessage doCreate(CreditCardCrudContext dto)
			throws RemoteException {
		throw new UnsupportedOperationException(
				"Can't create credit card. Create mode is not directly supported");
	}

	@Override
	protected void resetCachedList() {
		// single credit card per customer, no lists
	}

	private Integer customGetUserId() {
		String fromRequest = request.getParameter("userId");
		if (fromRequest != null) {
			return Integer.valueOf(fromRequest);
		}
		return commonGetUserId();
	}

	private Integer commonGetUserId() {
		return (Integer) session.getAttribute(Constants.SESSION_USER_ID);
	}

	private UserDTOEx getUserDto() {
		return (UserDTOEx) session.getAttribute(Constants.SESSION_USER_DTO);
	}

	private String maskCreditCardNumberIfNeeded(CreditCardDTO dto)
			throws RemoteException {
		// if the user is not allowed to see cc info
		// or the entity does not want anybody to see cc numbers

		boolean maskNeeded = getUserDto().isGranted(
				Constants.P_USER_EDIT_VIEW_CC);
		if (!maskNeeded) {
			final Integer HIDE_CC_NUMBERS = com.sapienter.jbilling.server.util.Constants.PREFERENCE_HIDE_CC_NUMBERS;
			String maskAll = myUserSession.getEntityPreference(//
					entityId, HIDE_CC_NUMBERS);
			maskNeeded = "1".equals(maskAll);
		}

		String result = dto.getNumber();
		if (maskNeeded) {
			result = "************" + result.substring(result.length() - 4);
		}
		return result;
	}
}
