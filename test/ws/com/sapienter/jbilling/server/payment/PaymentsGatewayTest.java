package com.sapienter.jbilling.server.payment;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

public class PaymentsGatewayTest extends TestCase {

	public void testPassACH() throws Exception {
		
		JbillingAPI api = JbillingAPIFactory.getAPI();
		UserWS newUser = createUser();
		newUser.setCreditCard(null);
		
		Integer userId = api.createUser(newUser);
        
        newUser = api.getUserWS(userId);
		AchDTO ach = newUser.getAch();
		// CreditCardDTO cc = newUser.getCreditCard();
		
		System.out.println("Testing ACH payment (should pass)");
		PaymentWS payment = createPayment(userId);
		payment.setAch(ach);
		
		PaymentAuthorizationDTOEx result = api.processPayment(payment);
		assertEquals("ACH payment should pass",
				Constants.RESULT_OK, api.getPayment(result.getPaymentId()).getResultId());
		
		assertEquals("Response code should be \"A\"", "A", 
				api.getPayment(result.getPaymentId()).getAuthorizationId().getCode1());
	}
	
	public void testFailACH() throws Exception {
		
		JbillingAPI api = JbillingAPIFactory.getAPI();
		UserWS newUser = createUser();
		newUser.setCreditCard(null);
		
		Integer userId = api.createUser(newUser);
        
        newUser = api.getUserWS(userId);
		AchDTO ach = newUser.getAch();
		
		ach.setBankAccount("987654321");
		api.updateAch(userId, ach);
		System.out.println("Testing ACH payment (should fail)");
		PaymentWS payment = createPayment(userId);
		payment.setAch(ach);
		
		PaymentAuthorizationDTOEx result = api.processPayment(payment);
		assertEquals("ACH payment to ABA 021000021 / acc 987654321 should fail",
				Constants.RESULT_FAIL, api.getPayment(result.getPaymentId()).getResultId());
		
		assertEquals("Response code should be \"D\"", "D", 
				api.getPayment(result.getPaymentId()).getAuthorizationId().getCode1());
	}
	
	public void testRefundACH() throws Exception {
		
		JbillingAPI api = JbillingAPIFactory.getAPI();
		UserWS newUser = createUser();
		newUser.setCreditCard(null);
		
		Integer userId = api.createUser(newUser);
        
        newUser = api.getUserWS(userId);
		AchDTO ach = newUser.getAch();
		
		System.out.println("Testing refund with negative number (should pass)");
		PaymentWS payment = createPayment(userId);
		payment.setAmount(new BigDecimal("-3.00"));
		payment.setAch(ach);
		
		PaymentAuthorizationDTOEx result = api.processPayment(payment);
		assertEquals("Refund should pass",
				Constants.RESULT_OK, api.getPayment(result.getPaymentId()).getResultId());
		
		assertEquals("Response code should be \"A\"", "A", 
				api.getPayment(result.getPaymentId()).getAuthorizationId().getCode1());
	}
	
	public void testCreditCardSalePass() throws Exception {
		
		JbillingAPI api = JbillingAPIFactory.getAPI();
		UserWS newUser = createUser();
		newUser.setAch(null);
		Integer userId = api.createUser(newUser);
        
        newUser = api.getUserWS(userId);
		CreditCardDTO cc = newUser.getCreditCard();
		
		System.out.println("Testing C/C payment (should pass)");
		PaymentWS payment = createPayment(userId);
		payment.setCreditCard(cc);
		
		PaymentAuthorizationDTOEx result = api.processPayment(payment);
		assertEquals("C/C payment should pass",
				Constants.RESULT_OK, api.getPayment(result.getPaymentId()).getResultId());
		
		assertEquals("Response code should be \"A\"", "A", 
				api.getPayment(result.getPaymentId()).getAuthorizationId().getCode1());
	}
	
	public void testCreditCardPreAuthAndCapture() throws Exception {
		
		JbillingAPI api = JbillingAPIFactory.getAPI();
		UserWS newUser = createUser();
		newUser.getCreditCard().setNumber("4200000000000000");
		newUser.setAch(null);
		Integer userId = api.createUser(newUser);
        
        newUser = api.getUserWS(userId);
		CreditCardDTO cc = newUser.getCreditCard();
		
		System.out.println("Testing C/C Pre-Auth (should pass)");
		PaymentWS payment = createPayment(userId);
		payment.setCreditCard(cc);
		payment.setIsPreauth(1);
		
		PaymentAuthorizationDTOEx result = api.processPayment(payment);
		assertEquals("C/C Pre-Auth should pass",
				Constants.RESULT_OK, api.getPayment(result.getPaymentId()).getResultId());
		
		assertEquals("Response code should be \"A\"", "A", 
				api.getPayment(result.getPaymentId()).getAuthorizationId().getCode1());
		
//		PaymentWS capt = createPayment(userId);
//		capt.setCreditCard(cc);
//		capt.setAuthorizationId(api.getPayment(result.getPaymentId()).getAuthorizationId());
//		
//		result = api.processPayment(capt);
//		assertEquals("C/C payment should pass",
//				Constants.RESULT_OK, api.getPayment(result.getPaymentId()).getResultId());
//		
//		assertEquals("Response should be \"TEST APPROVAL\"", "TEST APPROVAL", 
//				api.getPayment(result.getPaymentId()).getAuthorizationId().getResponseMessage());
	}
	
	public void testCreditCardRefund() throws Exception {
		JbillingAPI api = JbillingAPIFactory.getAPI();
		UserWS newUser = createUser();
		newUser.setAch(null);
		Integer userId = api.createUser(newUser);
        
        newUser = api.getUserWS(userId);
		CreditCardDTO cc = newUser.getCreditCard();
		
		System.out.println("Testing C/C payment (should pass)");
		PaymentWS payment = createPayment(userId);
		payment.setCreditCard(cc);
		payment.setAmount(new BigDecimal("-5.00"));
		
		PaymentAuthorizationDTOEx result = api.processPayment(payment);
		assertEquals("C/C payment should pass",
				Constants.RESULT_OK, api.getPayment(result.getPaymentId()).getResultId());
		
		assertEquals("Response code should be \"A\"", "A", 
				api.getPayment(result.getPaymentId()).getAuthorizationId().getCode1());
	}
	
	public void testATMVerify() throws Exception {
		
		JbillingAPI api = JbillingAPIFactory.getAPI();
		UserWS newUser = createUser();
		newUser.setCreditCard(null);
		
		Integer userId = api.createUser(newUser);
        
        newUser = api.getUserWS(userId);
		AchDTO ach = newUser.getAch();
		
		System.out.println("Testing ACH payment (should fail)");
		PaymentWS payment = createPayment(userId);
		payment.setAmount(new BigDecimal("1.33"));
		payment.setIsPreauth(1);
		PaymentInfoChequeDTO cheque = new PaymentInfoChequeDTO();
		cheque.setBank("210002012");
		cheque.setNumber("234567890");
		cheque.setDate(new Date());
		payment.setAch(ach);
		payment.setCheque(cheque);
		
		PaymentAuthorizationDTOEx result = api.processPayment(payment);
		assertEquals("Auth should be declined",
				Constants.RESULT_FAIL, api.getPayment(result.getPaymentId()).getResultId());
		
		assertEquals("Response code should be \"D\"", "D", 
				api.getPayment(result.getPaymentId()).getAuthorizationId().getCode1());
	}
	
	/*
	 * ***********************************************************************************
	 *       Utility methods
	 * ***********************************************************************************
	 */
	
	private PaymentWS createPayment(int userId) {
		PaymentWS payment = new PaymentWS();
        payment.setAmount(new BigDecimal("4.00"));
        payment.setIsRefund(new Integer(0));
        payment.setMethodId(Constants.PAYMENT_METHOD_ACH);
        payment.setPaymentDate(Calendar.getInstance().getTime());
        payment.setResultId(Constants.RESULT_ENTERED);
        payment.setCurrencyId(new Integer(1));
        payment.setUserId(userId);
		payment.setPaymentNotes("Notes");
		payment.setPaymentPeriod(new Integer(1));
		return payment;
	}
	
	private String getRandomNumber(int times) {
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < times; i++) {
			sb.append(r.nextInt(10));
		}
		return sb.toString();
	}
	
	private UserWS createUser() {
		
        UserWS newUser = new UserWS();
        newUser.setUserName("testUserName-" + Calendar.getInstance().getTimeInMillis());
        newUser.setPassword("asdfasdf1");
        newUser.setLanguageId(new Integer(1));
        newUser.setMainRoleId(new Integer(5));
        newUser.setParentId(null);
        newUser.setStatusId(UserDTOEx.STATUS_ACTIVE);
        newUser.setCurrencyId(null);
        newUser.setBalanceType(Constants.BALANCE_NO_DYNAMIC);
        
        // add a contact
        ContactWS contact = new ContactWS();
        contact.setEmail("frodo@shire.com");
        contact.setFirstName("Frodo");
        contact.setLastName("Baggins");
        String fields[] = new String[2];
        fields[0] = "1";
        fields[1] = "2"; // the ID of the CCF for the processor
        String fieldValues[] = new String[2];
        fieldValues[0] = "serial-from-ws";
        fieldValues[1] = "FAKE_2"; // the plug-in parameter of the processor
        contact.setFieldNames(fields);
        contact.setFieldValues(fieldValues);
        newUser.setContact(contact);
        
        // add a credit card
        CreditCardDTO cc = new CreditCardDTO();
        cc.setName("Frodo Baggins");
        cc.setNumber("4111111111111111");
        Calendar expiry = Calendar.getInstance();
        expiry.set(Calendar.YEAR, expiry.get(Calendar.YEAR) + 1);
        cc.setExpiry(expiry.getTime());        

        newUser.setCreditCard(cc);
        
        AchDTO ach = new AchDTO();
        ach.setAbaRouting("021000021");
        ach.setAccountName("Frodo Baggins");
        ach.setAccountType(Integer.valueOf(1));
        ach.setBankAccount("11" + getRandomNumber(6));
        ach.setBankName("Shire Financial Bank");
        
        newUser.setAch(ach);
        
        return newUser;
	}
}
