/*
 JBILLING CONFIDENTIAL
 _____________________

 [2003] - [2012] Enterprise jBilling Software Ltd.
 All Rights Reserved.

 NOTICE:  All information contained herein is, and remains
 the property of Enterprise jBilling Software.
 The intellectual and technical concepts contained
 herein are proprietary to Enterprise jBilling Software
 and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material
 is strictly forbidden.
 */

package com.sapienter.jbilling.server.payment;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import com.sapienter.jbilling.server.metafields.MetaFieldValueWS;
import junit.framework.TestCase;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserBL;
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
        try {
            api = JbillingAPIFactory.getAPI();
        } catch (Exception e) { 
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 1. Simplest test scenario - A refund affects linked payments balance.
     */
    public void testRefundPayment() {

        System.out.println("testRefundPayment().");
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

//    /**
//     * 2. A refund should bring the User's balance to its Original value before payment
//     */
//   public void testRefundUserBalanceUnchanged() {
//       System.out.println("testRefundUserBalanceUnchanged()");
//        try {
//            //create user
//            UserWS user= createUser(false, null, null);
//            assertTrue(user.getUserId() > 0);
//            System.out.println("User created successfully " + user.getUserId());
//
//            user= api.getUserWS(user.getUserId());
//            UserBL userBl= new UserBL(user.getUserId());
//            assertNotNull("This should not be null",userBl);
//            assertEquals(userBl.getBalance(user.getUserId()), BigDecimal.ZERO);
//
//            //make payment
//            Integer paymentId= createPayment(api, "100.00", false, user.getUserId(), null);
//            System.out.println("Created payment " + paymentId);
//            assertNotNull("Didn't get the payment id", paymentId);
//
//            //check payment balance = payment amount
//            PaymentWS payment= api.getPayment(paymentId);
//            assertNotNull(payment);
//            assertEquals(payment.getAmountAsDecimal(), payment.getBalanceAsDecimal());
//
//            //check user's balance
//            user= api.getUserWS(user.getUserId());
//            BigDecimal userBalance= userBl.getBalance(user.getUserId());
//            assertNotNull(userBalance);
//            assertTrue("User Balance should have been negetive", BigDecimal.ZERO.compareTo(userBalance) > 0);
//
//            assertTrue(payment.getInvoiceIds().length == 0 );
//
//            //create refund for above payment, refund amount = payment amount
//            Integer refundId= createPayment(api, "100.00", true, user.getUserId(), paymentId);
//            System.out.println("Created refund " + refundId);
//            assertNotNull("Didn't get the payment id", refundId);
//
//            //check user's balance = 0
//            user = api.getUserWS(user.getUserId());
//            assertNotNull(user);
//            assertEquals(BigDecimal.ZERO, userBl.getBalance(user.getUserId()));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//   }

    /**
     * 3. A refund must link to a Payment ID (negetive)
     * because a refund is only issued against a surplus
     */
    public void testRefundFailWhenNoPaymentLinked() {
        System.out.println("testRefundFailWhenNoPaymentLinked()");
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
            } catch (SessionInternalError e) {
                //check for validation error
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * 4. Test payment balance unchanged when linked payment has zero balance and linked invoices,
     * but invoice balance increased from previous balance
     */
   public void testRefundPaymentWithInvoiceLinked() {
       System.out.println("testRefundPaymentWithInvoiceLinked()");
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
     * having some balance left. Result: payment balance is Refund amount less amount used to pay invoice originally.
     * Invoice balance is equal to its total (used to be zero).
     */
    public void testRefundWithPaymentBalance() {
        System.out.println("testRefundWithPaymentBalance()");

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
            Integer refundId= createPayment(api, "200.00", true, user.getUserId(), paymentId);
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
            assertEquals("Invoice balance should have been $100.00", invoice.getBalanceAsDecimal(), invoice.getTotalAsDecimal());

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
        System.out.println("testRefundPaymentLinkedManyInvoices()");

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
            Integer paymentId= createPayment(api, "300.00", false, user.getUserId(), null);
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
            assertTrue(payment.getInvoiceIds().length == 3 );

            //CREATE REFUND for above payment, refund amount = payment amount
            Integer refundId= createPayment(api, "300.00", true, user.getUserId(), paymentId);
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

            InvoiceWS[] userInvoices= api.getAllInvoicesForUser(user.getUserId());

            for (InvoiceWS inv: userInvoices) {
                assertEquals("Should have been $100.00", new BigDecimal("100.00"), inv.getBalanceAsDecimal());
            }

            System.out.println("Invoice balance is.. " + invoice.getBalance());

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
        System.out.println("testDeletePaymentThatHasRefund()");
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

    //Helper method to create user
    private static UserWS createUser(boolean goodCC, Integer parentId, Integer currencyId) throws JbillingAPIException, IOException {
        System.out.println("createUser called");
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

        MetaFieldValueWS metaField1 = new MetaFieldValueWS();
        metaField1.setFieldName("partner.prompt.fee");
        metaField1.setValue("serial-from-ws");

        MetaFieldValueWS metaField2 = new MetaFieldValueWS();
        metaField2.setFieldName("ccf.payment_processor");
        metaField2.setValue("FAKE_2"); // the plug-in parameter of the processor

        newUser.setMetaFields(new MetaFieldValueWS[]{metaField1, metaField2});

        // add a contact
        ContactWS contact = new ContactWS();
        contact.setEmail("frodo@shire.com");
        contact.setFirstName("Frodo");
        contact.setLastName("Baggins");
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