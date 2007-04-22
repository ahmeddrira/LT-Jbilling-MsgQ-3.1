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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.user.PaymentMethodCrudContext.AchContext;
import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.server.entity.AchDTO;

public class AchMaintainAction extends
		AbstractPaymentMethodMaintainAction<AchContext> {
	private static final String FORM_ACH = "ach";

	private static final String FIELD_ABA_CODE = "aba_code";
	private static final String FIELD_ACCOUNT_NUMBER = "account_number";
	private static final String FIELD_ACCOUNT_TYPE = "account_type";
	private static final String FIELD_BANK_NAME = "bank_name";
	private static final String FIELD_ACCOUNT_NAME = "account_name";
	private static final String FIELD_USE_ACH = "chbx_use_this";

	private static final String FORWARD_DONE = "ach_done";
	private static final String FORWARD_EDIT = "ach_edit";
	private static final String FORWARD_DELETED = "ach_deleted";

	private static final String MESSAGE_UPDATE_OK = "ach.update.done";

	public AchMaintainAction() {
		super(FORM_ACH, "ACH", FIELD_ACCOUNT_NUMBER, FORWARD_EDIT);
	}

	@Override
	protected AchContext doEditFormToDTO() throws RemoteException {
		AchDTO dto = new AchDTO();
		dto.setAbaRouting((String) myForm.get(FIELD_ABA_CODE));
		dto.setBankAccount((String) myForm.get(FIELD_ACCOUNT_NUMBER));
		dto.setAccountType((Integer) myForm.get(FIELD_ACCOUNT_TYPE));
		dto.setAccountName((String) myForm.get(FIELD_ACCOUNT_NAME));
		dto.setBankName((String) myForm.get(FIELD_BANK_NAME));

		// verify that this entity actually accepts this kind of
		// payment method
		if (!getPaymentSession().isMethodAccepted(entityId,
				Constants.PAYMENT_METHOD_ACH)) {
			ActionError notAccepted = new ActionError(//
					"payment.error.notAccepted", //
					"payment.method" //
			);
			errors.add(ActionErrors.GLOBAL_ERROR, notAccepted);
		}

		AchContext result = new AchContext(dto);
		result.setIsAutomaticPayment((Boolean) myForm.get(FIELD_USE_ACH));

		return result;
	}

	@Override
	protected ForwardAndMessage doUpdate(AchContext dto) throws RemoteException {
		Integer userId = getSessionUserId();
		getUserSession().updateACH(userId, executorId, dto.getDto());
		getUserSession().setAuthPaymentType(userId,
				Constants.AUTO_PAYMENT_TYPE_ACH, dto.isAutomaticPayment());

		return new ForwardAndMessage(FORWARD_DONE, MESSAGE_UPDATE_OK);
	}

	@Override
	protected ForwardAndMessage doSetup() throws RemoteException {
		Integer userId = getSessionUserId();

		// now only one ach data per user
		AchDTO dto = getUserSession().getACH(userId);
		Integer type = getUserSession().getAuthPaymentType(userId);

		boolean use = Constants.AUTO_PAYMENT_TYPE_ACH.equals(type);
		if (dto != null) { // it could be that the user has no ACH setup yet
			myForm.set(FIELD_ABA_CODE, dto.getAbaRouting());
			myForm.set(FIELD_ACCOUNT_NUMBER, dto.getBankAccount());
			myForm.set(FIELD_ACCOUNT_TYPE, dto.getAccountType());
			myForm.set(FIELD_BANK_NAME, dto.getBankName());
			myForm.set(FIELD_ACCOUNT_NAME, dto.getAccountName());
			myForm.set(FIELD_USE_ACH, use);
		} else {
			setupNotFound();
		}
		return new ForwardAndMessage(FORWARD_EDIT);
	}

	@Override
	protected ForwardAndMessage doDelete() throws RemoteException {
		Integer userId = getSessionUserId();
		getUserSession().removeACH(userId, executorId);
		return new ForwardAndMessage(FORWARD_DELETED);
	}

	private Integer getSessionUserId() {
		return (Integer) session.getAttribute(Constants.SESSION_USER_ID);
	}

	// public ActionForward execute(ActionMapping mapping, ActionForm form,
	// HttpServletRequest request, HttpServletResponse response)
	// throws IOException, ServletException {
	//        
	// Logger log = Logger.getLogger(CreditCardMaintainAction.class);
	// try {
	// JNDILookup EJBFactory = JNDILookup.getFactory(false);
	// UserSessionHome userHome =
	// (UserSessionHome) EJBFactory.lookUpHome(
	// UserSessionHome.class,
	// UserSessionHome.JNDI_NAME);
	//        
	// UserSession userSession = userHome.create();
	// GenericMaintainAction gma = new GenericMaintainAction(mapping,
	// form, request, response, servlet, userSession,
	// "ach");
	//                    
	// return gma.process();
	// } catch (Exception e) {
	// log.error("Exception ", e);
	// }
	//        
	// return mapping.findForward("error");
	// }

}
