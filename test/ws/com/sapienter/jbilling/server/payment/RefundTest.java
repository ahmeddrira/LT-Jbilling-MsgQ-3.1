/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.payment;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.String;
import java.lang.System;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.sapienter.jbilling.server.payment.PaymentWS;
import junit.framework.TestCase;

import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

/**
 * @author Vikas Bodani
 * @since 04/01/12
 */
public class RefundTest extends TestCase {

    private JbillingAPI api;

    public RefundTest() {
    }

    public RefundTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        api = JbillingAPIFactory.getAPI();
    }

    //a refund affect linked payments balance
    public void testRefundPayment() {

        try {
            //create user
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());
            
            //make payment
            Integer paymentId= createPayment("100.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            //check payment balance = payment amount
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(payment.getAmountAsDecimal(), payment.getBalanceAsDecimal());
            
            assertTrue(payment.getInvoiceIds().length == 0 );
            
            //create refund for above payment, refund amount = payment amount
            Integer refundId= createPayment("100.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            assertNotNull("Didn't get the payment id", refundId);

            //check payment balance = 0
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    //A refund should have no impact on the User's balance
    public void testRefundUserBalanceUnchanged() {

        try {
            //create user
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());

            //create refund with no payment set
            Integer refundId= createPayment("100.00", true, user.getUserId(), null);
            System.out.println("Returned refund payment id." + refundId);
            assertNull("Refund payment got created without linked payment id.", refundId);

            //check for validation error

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }


    //A refund is only issued against a surplus
    public void testRefundFailWhenNoPayment() {

        try {
            //create user
            //create refund with no payment set
            //check for validation error
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    //test payment balance unchanged when linked payment has linked invoices, but invoice balance increased from previous balance



    private UserWS createUser(boolean goodCC, Integer parentId, Integer currencyId) throws JbillingAPIException, IOException {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        /*
        * Create - This passes the password validation routine.
        */
        UserWS newUser = new UserWS();
        newUser.setUserId(0); // it is validated
        newUser.setUserName("refund-test-" + Calendar.getInstance().getTimeInMillis());
        newUser.setPassword("asdfasdf1");
        newUser.setLanguageId(new Integer(1));
        newUser.setMainRoleId(new Integer(5));
        newUser.setParentId(parentId); // this parent exists
        newUser.setStatusId(UserDTOEx.STATUS_ACTIVE);
        newUser.setCurrencyId(currencyId);
        newUser.setBalanceType(Constants.BALANCE_NO_DYNAMIC);
        newUser.setInvoiceChild(new Boolean(false));

        // add a contact
        ContactWS contact = new ContactWS();
        contact.setEmail("frodo@shire.com");
        contact.setFirstName("Frodo");
        contact.setLastName("Baggins");
        Integer fields[] = new Integer[2];
        fields[0] = 1;
        fields[1] = 2; // the ID of the CCF for the processor
        String fieldValues[] = new String[2];
        fieldValues[0] = "serial-from-ws";
        fieldValues[1] = "FAKE_2"; // the plug-in parameter of the processor
        contact.setFieldIDs(fields);
        contact.setFieldValues(fieldValues);
        newUser.setContact(contact);

        // add a credit card
        CreditCardDTO cc = new CreditCardDTO();
        cc.setName("Frodo Baggins");
        cc.setNumber(goodCC ? "4111111111111152" : "4111111111111111");

        // valid credit card must have a future expiry date to be valid for payment processing
        Calendar expiry = Calendar.getInstance();
        expiry.set(Calendar.YEAR, expiry.get(Calendar.YEAR) + 1);
        cc.setExpiry(expiry.getTime());

        newUser.setCreditCard(cc);

        System.out.println("Creating user ...");
        newUser.setUserId(api.createUser(newUser));

        return newUser;
    }
    
    public Integer createPayment(String amount, boolean isRefund, Integer userId, Integer linkedPaymentId) {
        PaymentWS payment = new PaymentWS();
        payment.setAmount(new BigDecimal(amount));
        payment.setIsRefund(isRefund? new Integer(1): new Integer(0));
        payment.setMethodId(Constants.PAYMENT_METHOD_CHEQUE);
        payment.setPaymentDate(Calendar.getInstance().getTime());
        payment.setResultId(Constants.RESULT_ENTERED);
        payment.setCurrencyId(new Integer(1));
        payment.setUserId(userId);
        payment.setPaymentNotes("Notes");
        payment.setPaymentPeriod(new Integer(1));
        payment.setPaymentId(linkedPaymentId);

        PaymentInfoChequeDTO cheque = new PaymentInfoChequeDTO();
        cheque.setBank("ws bank");
        cheque.setDate(Calendar.getInstance().getTime());
        cheque.setNumber("2232-2323-2323");
        payment.setCheque(cheque);

        System.out.println("Creating " + (isRefund? " refund." : " payment.") );
        Integer ret = api.createPayment(payment);
        return ret;
    }

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, BigDecimal expected, BigDecimal actual) {
        assertEquals(message,
                (Object) (expected == null ? null : expected.setScale(2, RoundingMode.HALF_UP)),
                (Object) (actual == null ? null : actual.setScale(2, RoundingMode.HALF_UP)));
    }
    
}
