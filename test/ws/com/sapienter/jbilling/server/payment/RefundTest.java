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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import junit.framework.TestCase;

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
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

/**
 * jUnit Test cases for jBilling's refund funcationality
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

    /**
     * 1. Simplest test scenario - A refund affects linked payments balance.
     */
    public void testRefundPayment() {

        try {
            //create user
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());
            
            //make payment
            Integer paymentId= createPayment(api, "100.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            //check payment balance = payment amount
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(payment.getAmountAsDecimal(), payment.getBalanceAsDecimal());
            
            assertTrue(payment.getInvoiceIds().length == 0 );
            
            //create refund for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "100.00", true, user.getUserId(), paymentId);
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

    /**
     * 2. A refund should bring the User's balance to its Original value before payment
     */
    public void testRefundUserBalanceUnchanged() {
        try {
            //create user
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());

            user= api.getUserWS(user.getUserId());
            assertEquals(user.getDynamicBalanceAsDecimal(), BigDecimal.ZERO);
    
            //make payment
            Integer paymentId= createPayment(api, "100.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);
    
            //check payment balance = payment amount
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(payment.getAmountAsDecimal(), payment.getBalanceAsDecimal());
    
            //check user's balance
            user= api.getUserWS(user.getUserId());
            BigDecimal userBalance= user.getDynamicBalanceAsDecimal();
            assertNotNull(userBalance);
            assertTrue("User Balance should have been negetive", BigDecimal.ZERO.compareTo(userBalance) > 0);
                       
            assertTrue(payment.getInvoiceIds().length == 0 );
    
            //create refund for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "100.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            assertNotNull("Didn't get the payment id", refundId);
    
            //check user's balance = 0
            user = api.getUserWS(user.getUserId());
            assertNotNull(user);
            assertEquals(BigDecimal.ZERO, user.getDynamicBalanceAsDecimal());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * 3. A refund must link to a Payment ID (negetive)
     * because a refund is only issued against a surplus
     */
    public void testRefundFailWhenNoPaymentLinked() {

        try {
            //create user
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());

            //create refund with no payment set
            try {
                Integer refundId= createPayment(api, "100.00", true, user.getUserId(), null);
                System.out.println("Returned refund payment id." + refundId);
                assertNull("Refund payment got created without linked payment id.", refundId);
            } catch (Exception e) {
                //check for validation error
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * 4. Test payment balance unchanged when linked payment has linked invoices,
     * but invoice balance increased from previous balance
     */
    public void testRefundPaymentWithInvoiceLinked() {

        try {
            //CREATE USER
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());

            //CREATE ORDER & INVOICE
            Integer invoiceId= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId);
            
            //check invoice balance greater then zero
            InvoiceWS invoice= api.getLatestInvoice(user.getUserId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0 );
            
            //MAKE PAYMENT
            Integer paymentId= createPayment(api, "100.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            //check invoice balance is zero
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertEquals(invoice.getBalanceAsDecimal(), BigDecimal.ZERO );

            //check payment balance = zero since invoice paid
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());

            //payment has linked invoices
            assertTrue(payment.getInvoiceIds().length > 0 );

            //CREATE REFUND for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "100.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            assertNotNull("Didn't get the payment id", refundId);

            //check payment balance = 0
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());
            
            //check invoice balance is greater than zero
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0);

            //invoice balance is equal to its total
            assertEquals(invoice.getBalanceAsDecimal(), invoice.getTotalAsDecimal());
            
            System.out.println("Invoice balance is " + invoice.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Refund a payment that is linked to one invoice, paying it in full, but
     * having some balance left. Result: payment balance is zero. Invoice
     * balance is equal to its total (used to be zero).
     */
    public void testRefundWithPaymentBalance() {


        try {
            //CREATE USER
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());

            //CREATE ORDER & INVOICE
            Integer invoiceId= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId);
            
            //check invoice balance greater then zero
            InvoiceWS invoice= api.getLatestInvoice(user.getUserId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0 );
            
            //MAKE PAYMENT
            Integer paymentId= createPayment(api, "200.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            //check invoice balance is zero
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertEquals(invoice.getBalanceAsDecimal(), BigDecimal.ZERO );

            //check payment balance > zero since balance left after invoice paid
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertTrue(payment.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0 );
            assertEquals(new BigDecimal("100.00"), payment.getBalanceAsDecimal());

            //payment has linked invoices
            assertTrue(payment.getInvoiceIds().length > 0 );

            //CREATE REFUND for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "100.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            assertNotNull("Didn't get the payment id", refundId);

            //check payment balance = 0
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());
            
            //check invoice balance is greater than zero
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0);

            //invoice balance is equal to its total
            assertEquals(invoice.getBalanceAsDecimal(), invoice.getTotalAsDecimal());
            
            System.out.println("Invoice balance is " + invoice.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Refund a payment that is linked to many invoices, paying some partially,
     * some in full (uses the whole balance of the payment). Result: payment
     * balance remains zero. Invoice balance for each invoice = balance + amount
     * paid by the payment.
     */
    public void testRefundPaymentLinkedManyInvoices() {


        try {
            //CREATE USER
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());

            //CREATE ORDER & INVOICE 1
            Integer invoiceId= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId);
            
            //2
            invoiceId= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId);
            
            //3
            invoiceId= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId);
            
            //check invoice balance greater then zero
            InvoiceWS invoice= api.getLatestInvoice(user.getUserId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0 );
            
            //MAKE PAYMENT
            Integer paymentId= createPayment(api, "100.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            //check invoice balance is zero
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertEquals(invoice.getBalanceAsDecimal(), BigDecimal.ZERO );

            //check payment balance = zero since invoice paid
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());

            //payment has linked invoices
            assertTrue(payment.getInvoiceIds().length > 0 );

            //CREATE REFUND for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "100.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            assertNotNull("Didn't get the payment id", refundId);

            //check payment balance = 0
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());
            
            //check invoice balance is greater than zero
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0);

            //invoice balance is equal to its total
            assertEquals(invoice.getBalanceAsDecimal(), invoice.getTotalAsDecimal());
            
            System.out.println("Invoice balance is " + invoice.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /*
     * Deleting a Payment that has been refunded must fail.
     */
    
    /**
     * Cannot delete payment that has been refunded (negetive)
     */
    public void testDeletePaymentThatHasRefund() {

        try {
            //create user
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());
            
            //make payment
            Integer paymentId= createPayment(api, "100.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            //check payment balance = payment amount
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(payment.getAmountAsDecimal(), payment.getBalanceAsDecimal());
            
            assertTrue(payment.getInvoiceIds().length == 0 );
            
            //create refund for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "100.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            assertNotNull("Didn't get the payment id", refundId);

            //check payment balance = 0
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());
            
            try {
                api.deletePayment(paymentId);
                fail();
            } catch (Exception e) {
                //expected
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /*
     * Delete Refund Scenarios
     */
    
    /**
     * Simple: Delete a simple refund of a Payment that has no linked invoices.
     * Should increase linked payment's balance.
     */
    public void testDeleteRefund() {

        try {
            //create user
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());
            
            //make payment
            Integer paymentId= createPayment(api, "100.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            //check payment balance = payment amount
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(payment.getAmountAsDecimal(), payment.getBalanceAsDecimal());
            
            assertTrue(payment.getInvoiceIds().length == 0 );
            
            //create refund for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "100.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            assertNotNull("Didn't get the payment id", refundId);

            //check payment balance = 0
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());
            
            //delete refund
            api.deletePayment(refundId);
            
            //payment balance should not be zero
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(payment.getAmountAsDecimal(), payment.getBalanceAsDecimal());
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * Delete a Refund for a Payment that pays one or more Invoice in full and 
     * has zero balance left.
     */
    public void testRefundForPaymentWithPaidInvoice() {
        
        try {
            //CREATE USER
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());

            //CREATE ORDER & INVOICE
            Integer invoiceId1= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId1);
            
            //second order and invoice for $100
            Integer invoiceId2= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId2);
            
            //check invoice balance greater then zero
            InvoiceWS invoice= api.getLatestInvoice(user.getUserId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0 );
            
            //MAKE PAYMENT
            Integer paymentId= createPayment(api, "200.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            //check invoice balance is zero
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertEquals(invoice.getBalanceAsDecimal(), BigDecimal.ZERO );

            //check payment balance = zero since invoice paid
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());

            //payment has linked invoices
            assertTrue(payment.getInvoiceIds().length == 2 );

            //CREATE REFUND for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "200.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            assertNotNull("Didn't get the payment id.", refundId);

            //check payment balance = 0
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());
            
            //check invoice balance is greater than zero, increased due to Refund
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0);
            assertEquals(invoice.getBalanceAsDecimal(), new BigDecimal("100.00") );
            
            //invoice balance is equal to its total
            assertEquals(invoice.getBalanceAsDecimal(), invoice.getTotalAsDecimal());
            
            System.out.println("Invoice balance is " + invoice.getBalance());
            
            //DELETE THIS REFUND
            api.deletePayment(refundId);
            System.out.println("Deleted refund " + refundId);
            
            //invoice balance should be reduced by the refund amount less balance from payment 
            InvoiceWS inv= api.getInvoiceWS(invoiceId1);
            assertNotNull(inv);
            assertTrue(inv.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) == 0 );
            
            //other invoice
            inv= api.getInvoiceWS(invoiceId2);
            assertNotNull(inv);
            assertTrue(inv.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) == 0 );
            
            //payment balance should be equal to its amount minus invoice amount
            PaymentWS pym= api.getPayment(paymentId);
            assertNotNull(pym);
            System.out.println(pym.getBalance());
            //payment balance should be zero
            assertTrue(pym.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) == 0 );
            
            System.out.println("The delete refund successfully reversed the actions of the refund.");
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    /**
     * Delete a Refund for a Payment that pays one or more Invoice in full and 
     * still has non-zero positive balance left.
     */
    public void testDelRefundPaymentWithBalAndPaidInvoice() {
        
        try {

            //CREATE USER
            UserWS user= createUser(false, null, null);
            assertTrue(user.getUserId() > 0);
            System.out.println("User created successfully " + user.getUserId());

            //CREATE ORDER & INVOICE
            Integer invoiceId1= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId1);
            
            //second order and invoice for $100
            Integer invoiceId2= createOrderAndInvoice(api, user.getUserId());
            assertNotNull(invoiceId2);
            
            //check invoice balance greater then zero
            InvoiceWS invoice= api.getLatestInvoice(user.getUserId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0 );
            
            //MAKE PAYMENT of $300, when due invoice(s) total $200
            Integer paymentId= createPayment(api, "300.00", false, user.getUserId(), null);
            System.out.println("Created payment " + paymentId);
            assertNotNull("Didn't get the payment id", paymentId);

            System.out.println("created a big payment of $300.00 with " +
            		"balance (100), invoice(s) balance = 0, payment balance > 0");

            //check invoice balance is zero
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertEquals(invoice.getBalanceAsDecimal(), BigDecimal.ZERO );

            //check payment balance = zero since invoice paid
            PaymentWS payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());

            //payment has linked invoices
            assertTrue(payment.getInvoiceIds().length == 2 );

            //CREATE REFUND for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "300.00", true, user.getUserId(), paymentId);
            System.out.println("Created refund " + refundId);
            System.out.println("create refund - increases the balance of Invoice(s), reduces the balance of Payment");
            assertNotNull("Didn't get the payment id.", refundId);

            //check payment balance = 0
            payment= api.getPayment(paymentId);
            assertNotNull(payment);
            assertEquals(BigDecimal.ZERO, payment.getBalanceAsDecimal());
            
            //check invoice balance is greater than zero, increased due to Refund
            invoice= api.getInvoiceWS(invoice.getId());
            assertNotNull(invoice);
            assertTrue(invoice.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0);
            assertEquals(invoice.getBalanceAsDecimal(), new BigDecimal("100.00") );
            
            //invoice balance is equal to its total
            assertEquals(invoice.getBalanceAsDecimal(), invoice.getTotalAsDecimal());
            
            System.out.println("Invoice balance is " + invoice.getBalance());
            
            //DELETE THIS REFUND
            System.out.println("delete refund reduces the balance of Invoice(s), increases the balance of Payment");
            api.deletePayment(refundId);
            System.out.println("Deleted refund " + refundId);
            
            //invoice balance should be reduced by the refund amount less balance from payment 
            InvoiceWS inv= api.getInvoiceWS(invoiceId1);
            assertNotNull(inv);
            assertTrue(inv.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) == 0 );
            
            //other invoice
            inv= api.getInvoiceWS(invoiceId2);
            assertNotNull(inv);
            assertTrue(inv.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) == 0 );
            
            //payment balance should be equal to its amount minus invoice amount
            PaymentWS pym= api.getPayment(paymentId);
            assertNotNull(pym);
            System.out.println(pym.getBalance());
            //payment balance should be greater than Zero
            assertTrue(pym.getBalanceAsDecimal().compareTo(BigDecimal.ZERO) > 0 );
            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    
    //Helper method to create user
    private static UserWS createUser(boolean goodCC, Integer parentId, Integer currencyId) throws JbillingAPIException, IOException {
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

    //Helper method to create payment
    private static Integer createPayment(JbillingAPI api, String amount, boolean isRefund, Integer userId, Integer linkedPaymentId) {
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

    //Helper method to create order and invoice
    private static Integer createOrderAndInvoice(JbillingAPI api, Integer userId) {
        
        Integer invoiceId= null;
        
        try {
            OrderWS newOrder = new OrderWS();
            newOrder.setUserId(userId);
            newOrder.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
            newOrder.setPeriod(Constants.ORDER_PERIOD_ONCE);
            newOrder.setCurrencyId(new Integer(1));
            // notes can only be 200 long... but longer should not fail
            newOrder.setNotes("Lorem ipsum text.");

            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(2008, 1, 1);
            newOrder.setActiveSince(cal.getTime());
            newOrder.setCycleStarts(cal.getTime());

            // now add some lines
            OrderLineWS lines[] = new OrderLineWS[1];
            OrderLineWS line;

            line = new OrderLineWS();
            line.setPrice(new BigDecimal("100.00"));
            line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            line.setQuantity(new Integer(1));
            line.setAmount(new BigDecimal("100.00"));
            line.setDescription("Fist line");
            line.setItemId(new Integer(1));
            lines[0] = line;
            newOrder.setOrderLines(lines);
            System.out.println("Creating order ... " + newOrder);
            invoiceId = api.createOrderAndInvoice(newOrder);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return invoiceId;
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
