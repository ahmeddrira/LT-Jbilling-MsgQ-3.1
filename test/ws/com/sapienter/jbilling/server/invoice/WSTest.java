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

package com.sapienter.jbilling.server.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import com.sapienter.jbilling.server.process.BillingProcessConfigurationWS;
import com.sapienter.jbilling.server.process.db.PeriodUnitDTO;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.PreferenceTypeWS;
import com.sapienter.jbilling.server.util.PreferenceWS;
import junit.framework.TestCase;

import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;
import org.joda.time.DateMidnight;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Emil
 */
public class WSTest extends TestCase {

    public void testGet() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();

            // get
            // try getting one that doesn't belong to us
            try {
                System.out.println("Getting invalid invoice");
                api.getInvoiceWS(75);
                fail("Invoice 75 belongs to entity 2");
            } catch (Exception e) {
            }

            System.out.println("Getting invoice");
            InvoiceWS retInvoice = api.getInvoiceWS(15);
            assertNotNull("invoice not returned", retInvoice);
            assertEquals("invoice id", retInvoice.getId(), new Integer(15));
            System.out.println("Got = " + retInvoice);

            // latest
            // first, from a guy that is not mine
            try {
            	api.getLatestInvoice(13);
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }
            System.out.println("Getting latest invoice");
            retInvoice = api.getLatestInvoice(2);
            assertNotNull("invoice not returned", retInvoice);
            assertEquals("invoice's user id", retInvoice.getUserId(), new Integer(2));
            System.out.println("Got = " + retInvoice);
            Integer lastInvoice = retInvoice.getId();

            // List of last
            // first, from a guy that is not mine
            try {
            	api.getLastInvoices(13, 5);
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }
            System.out.println("Getting last 5 invoices");
            Integer invoices[] = api.getLastInvoices(2, 5);
            assertNotNull("invoice not returned", invoices);

            retInvoice = api.getInvoiceWS(invoices[0]);
            assertEquals("invoice's user id", new Integer(2), retInvoice.getUserId());
            System.out.println("Got = " + invoices.length + " invoices");
            for (int f = 0; f < invoices.length; f++) {
                System.out.println(" Invoice " + (f + 1) + invoices[f]);
            }

            // now I want just the two latest
            System.out.println("Getting last 2 invoices");
            invoices = api.getLastInvoices(2, 2);
            assertNotNull("invoice not returned", invoices);
            retInvoice = api.getInvoiceWS(invoices[0]);
            assertEquals("invoice's user id", new Integer(2), retInvoice.getUserId());
            assertEquals("invoice's has to be latest", lastInvoice, retInvoice.getId());
            assertEquals("there should be only 2", 2, invoices.length);

            // get some by date
            System.out.println("Getting by date (empty)");
            Integer invoices2[] = api.getInvoicesByDate("2000-01-01", "2005-01-01");
            // CXF returns null instead of empty arrays
            // assertNotNull("invoice not returned", invoices2);
            if (invoices2 != null) {
                assertTrue("array not empty", invoices2.length == 0);
            }

            System.out.println("Getting by date");
            invoices2 = api.getInvoicesByDate("2006-01-01", "2007-01-01");
            assertNotNull("invoice not returned", invoices2);
            assertFalse("array not empty", invoices2.length == 0);
            System.out.println("Got array " + invoices2.length + " getting " + invoices2[0]);
            retInvoice = api.getInvoiceWS(invoices2[0]);
            assertNotNull("invoice not there", retInvoice);
            System.out.println("Got invoice " + retInvoice);

            System.out.println("Done!");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    public void testDelete() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();
            Integer invoiceId = new Integer(1);
            assertNotNull(api.getInvoiceWS(invoiceId));
            api.deleteInvoice(invoiceId);
            try {
                api.getInvoiceWS(invoiceId);
                fail("Invoice should not have been deleted");
            } catch(Exception e) {
                //ok
            }

            // try to delete an invoice that is not mine
            try {
                api.deleteInvoice(new Integer(75));
                fail("Not my invoice. It should not have been deleted");
            } catch(Exception e) {
                //ok
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }

    }

    public void testCreateInvoice() {
        try {
            final Integer USER_ID = 10730; // user has no orders
            JbillingAPI api = JbillingAPIFactory.getAPI();

            // setup order
            OrderWS order = new OrderWS();
            order.setUserId(USER_ID);
            order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
            order.setPeriod(1); // once
            order.setCurrencyId(1);
            order.setActiveSince(new Date());

            OrderLineWS line = new OrderLineWS();
            line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            line.setDescription("Order line");
            line.setItemId(1);
            line.setQuantity(1);
            line.setPrice(new BigDecimal("10.00"));
            line.setAmount(new BigDecimal("10.00"));

            order.setOrderLines(new OrderLineWS[] { line });

            /*
             * Test invoicing of one-time and recurring orders
             */

            // create 1st order
            Integer orderId1 = api.createOrder(order);

            // create 2nd order
            line.setPrice(new BigDecimal("20.00"));
            line.setAmount(new BigDecimal("20.00"));
            Integer orderId2 = api.createOrder(order);

            // create invoice
            Integer[] invoices = api.createInvoice(USER_ID, false);

            assertEquals("Number of invoices returned", 1, invoices.length);
            InvoiceWS invoice = api.getInvoiceWS(invoices[0]);

            assertNull("Invoice is not delegated.", invoice.getDelegatedInvoiceId());
            assertEquals("Invoice does not have a carried balance.", BigDecimal.ZERO, invoice.getCarriedBalanceAsDecimal());

            Integer[] invoicedOrderIds = invoice.getOrders();
            assertEquals("Number of orders invoiced", 2, invoicedOrderIds.length);
            Arrays.sort(invoicedOrderIds);
            assertEquals("Order 1 invoiced", orderId1, invoicedOrderIds[0]);
            assertEquals("Order 2 invoiced", orderId2, invoicedOrderIds[1]);
            assertEquals("Total is 30.0", new BigDecimal("30.00"), invoice.getTotalAsDecimal());

            // clean up
            api.deleteInvoice(invoices[0]);
            api.deleteOrder(orderId1);
            api.deleteOrder(orderId2);

            /*
             * Test only recurring order can generate invoice.
             */

            // one-time order
            line.setPrice(new BigDecimal("2.00"));
            line.setAmount(new BigDecimal("2.00"));
            orderId1 = api.createOrder(order);

            // try to create invoice, but none should be returned
            invoices = api.createInvoice(USER_ID, true);

            // Note: CXF returns null for empty array
            if (invoices != null) {
                assertEquals("Number of invoices returned", 0, invoices.length);
            }

            // recurring order
            order.setPeriod(2); // monthly
            line.setPrice(new BigDecimal("3.00"));
            line.setAmount(new BigDecimal("3.00"));
            orderId2 = api.createOrder(order);

            // create invoice
            invoices = api.createInvoice(USER_ID, true);

            assertEquals("Number of invoices returned", 1, invoices.length);
            invoice = api.getInvoiceWS(invoices[0]);
            invoicedOrderIds = invoice.getOrders();
            assertEquals("Number of orders invoiced", 2, invoicedOrderIds.length);
            Arrays.sort(invoicedOrderIds);
            assertEquals("Order 1 invoiced", orderId1, invoicedOrderIds[0]);
            assertEquals("Order 2 invoiced", orderId2, invoicedOrderIds[1]);
            assertEquals("Total is 5.0", new BigDecimal("5.00"), invoice.getTotalAsDecimal());

            // clean up
            api.deleteInvoice(invoices[0]);
            api.deleteOrder(orderId1);
            api.deleteOrder(orderId2);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    public void testCreateInvoiceFromOrder() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        final Integer USER_ID = 10730; // user has no orders

        // setup orders
        OrderWS order = new OrderWS();
        order.setUserId(USER_ID);
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(1); // once
        order.setCurrencyId(1);
        order.setActiveSince(new Date());

        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setDescription("Order line");
        line.setItemId(1);
        line.setQuantity(1);
        line.setPrice(new BigDecimal("10.00"));
        line.setAmount(new BigDecimal("10.00"));

        order.setOrderLines(new OrderLineWS[] { line });

        // create orders
        Integer orderId1 = api.createOrder(order);
        Integer orderId2 = api.createOrder(order);

        // generate invoice using first order
        Integer invoiceId = api.createInvoiceFromOrder(orderId1, null);

        assertNotNull("Order 1 created", orderId1);
        assertNotNull("Order 2 created", orderId2);
        assertNotNull("Invoice created", invoiceId);

        Integer[] invoiceIds = api.getLastInvoices(USER_ID, 2);
        assertEquals("Only 1 invoice was generated", 1, invoiceIds.length);        

        InvoiceWS invoice = api.getInvoiceWS(invoiceId);
        assertEquals("Invoice total is $10.00", new BigDecimal("10.00"), invoice.getTotalAsDecimal());
        assertEquals("Only 1 order invoiced", 1, invoice.getOrders().length);
        assertEquals("Invoice generated from 1st order", orderId1, invoice.getOrders()[0]);

        // add second order to invoice
        Integer invoiceId2 = api.createInvoiceFromOrder(orderId2, invoiceId);
        assertEquals("Order added to the same invoice", invoiceId, invoiceId2);

        invoiceIds = api.getLastInvoices(USER_ID, 2);
        assertEquals("Still only 1 invoice generated", 1, invoiceIds.length);

        invoice = api.getInvoiceWS(invoiceId);
        assertEquals("Invoice total is $20.00", new BigDecimal("20.00"), invoice.getTotalAsDecimal());
        assertEquals("2 orders invoiced", 2, invoice.getOrders().length);

        // cleanup
        api.deleteInvoice(invoiceId);
        api.deleteOrder(orderId1);
        api.deleteOrder(orderId2);
    }

    public void testCreateInvoiceSecurity() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();
            try {
                api.createInvoice(13, false);
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }


    /**
     * Tests that when a past due invoice is processed it will generate a new invoice for the
     * current period that contains all previously un-paid balances as the carried balance.
     *
     * Invoices that have been carried still show the original balance for reporting/paper-trail
     * purposes, but will not be re-processed by the system as part of the normal billing process.
     *
     * @throws Exception
     */
    public void testCreateWithCarryOver() throws Exception {
        final Integer USER_ID = 10743;          // user has one past-due invoice to be carried forward
        final Integer OVERDUE_INVOICE_ID = 70;  // holds a $20 balance

        JbillingAPI api = JbillingAPIFactory.getAPI();

        // new order witha  single line item
        OrderWS order = new OrderWS();
        order.setUserId(USER_ID);
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(1); // once
        order.setCurrencyId(1);
        order.setActiveSince(new Date());

        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setDescription("Order line");
        line.setItemId(1);
        line.setQuantity(1);
        line.setPrice(new BigDecimal("10.00"));
        line.setAmount(new BigDecimal("10.00"));

        order.setOrderLines(new OrderLineWS[] { line });

        // create order
        Integer orderId = api.createOrder(order);

        // create invoice
        Integer invoiceId = api.createInvoice(USER_ID, false)[0];

        // validate that the overdue invoice has been carried forward to the newly created invoice
        InvoiceWS overdue = api.getInvoiceWS(OVERDUE_INVOICE_ID);

        assertEquals("Status updated to 'unpaid and carried'",
                     Constants.INVOICE_STATUS_UNPAID_AND_CARRIED, overdue.getStatusId());
        assertEquals("Carried invoice will not be re-processed",
                     0, overdue.getToProcess().intValue());
        assertEquals("Overdue invoice holds original balance",
                     new BigDecimal("20.00"), overdue.getBalanceAsDecimal());

        assertEquals("Overdue invoice delegated to the newly created invoice",
                     invoiceId, overdue.getDelegatedInvoiceId());

        // validate that the newly created invoice contains the carried balance
        InvoiceWS invoice = api.getInvoiceWS(invoiceId);

        assertEquals("New invoice balance is equal to the current period charges",
                     new BigDecimal("10.00"), invoice.getBalanceAsDecimal());
        assertEquals("New invoice holds the carried balance equal to the old invoice balance",
                     overdue.getBalanceAsDecimal(), invoice.getCarriedBalanceAsDecimal());
        assertEquals("New invoice total is equal to the current charges plus the carried total",
                     new BigDecimal("30.00"), invoice.getTotalAsDecimal());
    }

    public void testGetUserInvoicesByDate() {
        try {
            final Integer USER_ID = 2; // user has some invoices
            JbillingAPI api = JbillingAPIFactory.getAPI();

            // invoice dates: 2006-07-26
            // select the week
            Integer[] result = api.getUserInvoicesByDate(USER_ID, "2006-07-23", "2006-07-29");
            // note: invoice 1 gets deleted
            assertEquals("Number of invoices returned", 3, result.length);
            assertEquals("Invoice id 4", 4, result[0].intValue());
            assertEquals("Invoice id 3", 3, result[1].intValue());
            assertEquals("Invoice id 2", 2, result[2].intValue());

            // test since date inclusive
            result = api.getUserInvoicesByDate(USER_ID, "2006-07-26", "2006-07-29");
            assertEquals("Number of invoices returned", 3, result.length);
            assertEquals("Invoice id 4", 4, result[0].intValue());
            assertEquals("Invoice id 3", 3, result[1].intValue());
            assertEquals("Invoice id 2", 2, result[2].intValue());

            // test until date inclusive
            result = api.getUserInvoicesByDate(USER_ID, "2006-07-23", "2006-07-26");
            assertEquals("Number of invoices returned", 3, result.length);
            assertEquals("Invoice id 4", 4, result[0].intValue());
            assertEquals("Invoice id 3", 3, result[1].intValue());
            assertEquals("Invoice id 2", 2, result[2].intValue());

            // test date with no invoices
            result = api.getUserInvoicesByDate(USER_ID, "2005-07-23", "2005-07-29");
            // Note: CXF returns null for empty array
            if (result != null) {
                assertEquals("Number of invoices returned", 0, result.length);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }
    
    public void testGetTotalAsDecimal() {
        List<Integer> invoiceIds = new ArrayList<Integer>();
        List<Integer> orderIds = new ArrayList<Integer>();
        JbillingAPI api = null;
        try {
            final Integer USER_ID = 10730; // user has no orders
            api = JbillingAPIFactory.getAPI();

            // test BigDecimal behavior
            assertFalse(new BigDecimal("1.1").equals(new BigDecimal("1.10")));
            assertTrue(new BigDecimal("1.1").compareTo(new BigDecimal("1.10")) == 0);

            // with items 2 and 3 10% discount should apply
            orderIds.add(api.createOrder(com.sapienter.jbilling.server.order.WSTest.createMockOrder(USER_ID, 3, new BigDecimal("0.32"))));
            orderIds.add(api.createOrder(com.sapienter.jbilling.server.order.WSTest.createMockOrder(USER_ID, 3, new BigDecimal("0.32"))));

            invoiceIds.addAll(Arrays.asList(api.createInvoice(USER_ID, false)));
            assertEquals(1, invoiceIds.size());

            InvoiceWS invoice = api.getInvoiceWS(invoiceIds.get(0));
            assertEquals("1.7280000000", invoice.getTotal());
            assertEquals(new BigDecimal("1.728"), invoice.getTotalAsDecimal());            

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        } finally {
            try {
                for (Integer integer : invoiceIds) {
                    api.deleteInvoice(integer);
                }
                System.out.println("Successfully deleted invoices: " + invoiceIds.size());
                for (Integer integer : orderIds) {
                    api.deleteOrder(integer);
                }
                System.out.println("Successfully deleted orders: " + orderIds.size());
            } catch (Exception ignore) {
            }
        }
    }

    public void testGetPaperInvoicePDF() throws Exception {
        final Integer USER_ID = 2; // user has invoices

        JbillingAPI api = JbillingAPIFactory.getAPI();

        Integer[] invoiceIds = api.getLastInvoices(USER_ID, 1);
        assertEquals("Invoice found for user", 1, invoiceIds.length);

        byte[] pdf = api.getPaperInvoicePDF(invoiceIds[0]);
        assertTrue("PDF invoice bytes returned", pdf.length > 0);        
    }

    public void testPostPaidGenerateInvoice() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        resetBillingConfiguration(); // make sure we're using the same config/dates

        UserWS user = new UserWS();
        user.setUserName("invoice-test-01-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Invoice Test");
        contact.setLastName("Post-paid with due date");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());

        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);
        order.setPeriod(2); // monthly
        order.setCurrencyId(1);
        order.setActiveSince(new DateMidnight(2011, 1, 1).toDate()); // active since January 01, 2011

        // create an order to be invoiced
        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(2602); // lemonade
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });

        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());


        // generate the invoice with a target billing & due date, "use process date for invoice" is off so
        // order date will be used when calculating invoice due date:
        //
        //      post-paid order starts on January 01, 2010
        //      invoiced on February 01, 2011
        //      45 day due date
        //
        // post paid == invoice after 1 period, use order start date + 1 month
        // January 01, 2011 + 1 month + 45 days = March 18, 2011
        Date billingDate = new DateMidnight(2011, 2, 1).toDate();
        Integer[] invoiceIds = api.createInvoiceWithDate(user.getUserId(), billingDate, PeriodUnitDTO.DAY, 45, false);
        assertEquals("1 invoice generated", 1, invoiceIds.length);

        InvoiceWS invoice = api.getInvoiceWS(invoiceIds[0]);
        assertEquals("due date 45 days from order start date", new DateMidnight(2011, 3, 18).toDate(), invoice.getDueDate());

        // cleanup
        api.deleteInvoice(invoice.getId());
        api.deleteOrder(order.getId());
        api.deleteUser(user.getUserId());
    }


    public void testPrePaidGenerateInvoicePostdated() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        resetBillingConfiguration(); // make sure we're using the same config/dates

        UserWS user = new UserWS();
        user.setUserName("invoice-test-02-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Invoice Test");
        contact.setLastName("Pre-paid (past) with due date");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());

        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(2); // monthly
        order.setCurrencyId(1);
        order.setActiveSince(new DateMidnight(2010, 12, 1).toDate()); // active since December 01, 2010
        order.setCycleStarts(new DateMidnight(2010, 12, 1).toDate()); // cycle starts December 01, 2010

        // create an order to be invoiced
        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(2602); // lemonade
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });

        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());


        // generate the invoice with a target billing & due date, "use process date for invoice" is off so
        // order date will be used when calculating invoice due date:
        //
        //      pre-paid order starts on December 01, 2010
        //      invoiced on January 01, 2011
        //      45 day due date
        //
        // pre paid == invoice immediately, use order start date
        // December 01, 2010 + 45 = January 15, 2011
        Date billingDate = new DateMidnight(2011, 1, 1).toDate();
        Integer[] invoiceIds = api.createInvoiceWithDate(user.getUserId(), billingDate, PeriodUnitDTO.DAY, 45, false);
        assertEquals("1 invoice generated", 1, invoiceIds.length);

        InvoiceWS invoice = api.getInvoiceWS(invoiceIds[0]);
        assertEquals("due date 45 days from order start date", new DateMidnight(2011, 1, 15).toDate(), invoice.getDueDate());

        // cleanup
        api.deleteInvoice(invoice.getId());
        api.deleteOrder(order.getId());
        api.deleteUser(user.getUserId());
    }

    public void testPrePaidGenerateInvoice() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        resetBillingConfiguration(); // make sure we're using the same config/dates

        UserWS user = new UserWS();
        user.setUserName("invoice-test-03-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Invoice Test");
        contact.setLastName("Pre-paid with due date");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());

        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(2); // monthly
        order.setCurrencyId(1);
        order.setActiveSince(new DateMidnight(2011, 1, 1).toDate()); // active since January 01, 2011
        order.setCycleStarts(new DateMidnight(2011, 1, 1).toDate()); // cycle starts January 01, 2011

        // create an order to be invoiced
        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(2602); // lemonade
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });

        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());


        // generate the invoice with a target billing & due date, "use process date for invoice" is off so
        // order date will be used when calculating invoice due date:
        //
        //      order starts on January 01, 2011
        //      invoiced on January 01, 2011
        //      45 day due date
        //
        // pre paid == invoice immediately, use order start date
        // January 01, 2011 + 45 = February 15, 2011
        Date billingDate = new DateMidnight(2011, 1, 1).toDate();
        Integer[] invoiceIds = api.createInvoiceWithDate(user.getUserId(), billingDate, PeriodUnitDTO.DAY, 45, false);
        assertEquals("1 invoice generated", 1, invoiceIds.length);

        InvoiceWS invoice = api.getInvoiceWS(invoiceIds[0]);
        assertEquals("due date 45 days from order start date", new DateMidnight(2011, 2, 15).toDate(), invoice.getDueDate());

        // cleanup
        api.deleteInvoice(invoice.getId());
        api.deleteOrder(order.getId());
        api.deleteUser(user.getUserId());
    }

    /**
     * Test generation of an invoice with a specific target due date when the customer
     * has multiple orders to be invoiced.
     *
     * This test case covers bug #1487
     *
     * @throws Exception possible api exception
     */
    public void testMultiOrderGenerateInvoice() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        resetBillingConfiguration(); // make sure we're using the same config/dates

        UserWS user = new UserWS();
        user.setUserName("invoice-test-04-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Invoice Test");
        contact.setLastName("Multi-order with due date");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());

        // create orders

        // order 1
        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(2); // monthly
        order.setCurrencyId(1);
        order.setActiveSince(new DateMidnight(2011, 1, 1).toDate()); // active since January 01, 2011
        order.setCycleStarts(new DateMidnight(2011, 1, 1).toDate()); // cycle starts January 01, 2011

        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(2602); // lemonade
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });

        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());

        // order 2
        OrderWS order2 = new OrderWS();
    	order2.setUserId(user.getUserId());
        order2.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order2.setPeriod(2); // monthly
        order2.setCurrencyId(1);
        order2.setActiveSince(new DateMidnight(2011, 1, 1).toDate()); // active since January 01, 2011
        order2.setCycleStarts(new DateMidnight(2011, 1, 1).toDate()); // cycle starts January 01, 2011

        OrderLineWS line2 = new OrderLineWS();
        line2.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line2.setItemId(2602); // lemonade
        line2.setUseItem(true);
        line2.setQuantity(2);
        order2.setOrderLines(new OrderLineWS[] {line2});

        order2.setId(api.createOrder(order2)); // create order
        order2 = api.getOrder(order2.getId());
        assertNotNull("order created", order2.getId());

        // order 2
        OrderWS order3 = new OrderWS();
    	order3.setUserId(user.getUserId());
        order3.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order3.setPeriod(2); // monthly
        order3.setCurrencyId(1);
        order3.setActiveSince(new DateMidnight(2011, 1, 1).toDate()); // active since January 01, 2011
        order3.setCycleStarts(new DateMidnight(2011, 1, 1).toDate()); // cycle starts January 01, 2011

        OrderLineWS line3 = new OrderLineWS();
        line3.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line3.setItemId(2602); // lemonade
        line3.setUseItem(true);
        line3.setQuantity(3);
        order3.setOrderLines(new OrderLineWS[] {line3});

        order3.setId(api.createOrder(order3)); // create order
        order3 = api.getOrder(order3.getId());
        assertNotNull("order created", order3.getId());


        // generate the invoice with a target billing & due date, "use process date for invoice" is off so
        // order date will be used when calculating invoice due date:
        //
        //      all three orders start on January 01, 2011
        //      invoiced on January 01, 2011
        //      45 day due date
        //
        // pre paid == invoice immediately, use order start date
        // January 01, 2011 + 45 = February 15, 2011
        Date billingDate = new DateMidnight(2011, 1, 1).toDate();
        Integer[] invoiceIds = api.createInvoiceWithDate(user.getUserId(), billingDate, PeriodUnitDTO.DAY, 45, true);
        assertEquals("1 invoice generated", 1, invoiceIds.length);

        InvoiceWS invoice = api.getInvoiceWS(invoiceIds[0]);
        assertEquals("due date 45 days from order start date", new DateMidnight(2011, 2, 15).toDate(), invoice.getDueDate());

        // cleanup
        api.deleteInvoice(invoice.getId());
        api.deleteOrder(order.getId());
        api.deleteOrder(order2.getId());
        api.deleteOrder(order3.getId());
        api.deleteUser(user.getUserId());
    }

    /**
     * Resets the billing configuration to the default state found in a fresh
     * load of the testing 'jbilling_test.sql' file.
     *
     * @throws Exception possible api exception
     */
    public void resetBillingConfiguration() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        BillingProcessConfigurationWS config = api.getBillingProcessConfiguration();
        config.setNextRunDate(new DateMidnight(2006, 10, 26).toDate());
        config.setGenerateReport(1);
        config.setDaysForReport(3);
        config.setRetries(0);
        config.setDaysForRetry(1);
        config.setDueDateValue(1);
        config.setDueDateUnitId(PeriodUnitDTO.MONTH);

        config.setOnlyRecurring(1);
        config.setInvoiceDateProcess(0);
        config.setAutoPayment(0);
        config.setMaximumPeriods(1);
        config.setAutoPayment(1);

        api.createUpdateBillingProcessConfiguration(config);

        // reset continuous invoice date
        PreferenceWS continuousDate = new PreferenceWS(new PreferenceTypeWS(Constants.PREFERENCE_CONTINUOUS_DATE), null);
        api.updatePreference(continuousDate);

    }

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, BigDecimal expected, BigDecimal actual) {
        if (expected == null && actual == null) {
            return;
        }
        assertTrue(message + ". expected <" + expected + "> but was <" + actual + ">", expected.compareTo(actual) == 0);
    }
}
