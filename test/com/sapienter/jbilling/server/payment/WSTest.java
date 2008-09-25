/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * Created on Dec 18, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.payment;

import java.util.Calendar;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

/**
 * @author Emil
 */
public class WSTest extends TestCase {
	
      
    public void testApplyGet() {
        try {
        	
            JbillingAPI api = JbillingAPIFactory.getAPI();
           
            /*
             * apply payment
             */
            PaymentWS payment = new PaymentWS();
            payment.setAmount(new Float(15));
            payment.setIsRefund(new Integer(0));
            payment.setMethodId(Constants.PAYMENT_METHOD_CHEQUE);
            payment.setPaymentDate(Calendar.getInstance().getTime());
            payment.setResultId(Constants.RESULT_ENTERED);
            payment.setCurrencyId(new Integer(1));
            payment.setUserId(new Integer(2));
            
            PaymentInfoChequeDTO cheque = new PaymentInfoChequeDTO();
            cheque.setBank("ws bank");
            cheque.setDate(Calendar.getInstance().getTime());
            cheque.setNumber("2232-2323-2323");
            payment.setCheque(cheque);
           
            System.out.println("Applying payment");
            Integer ret = api.applyPayment(payment, new Integer(35));
            System.out.println("Created payemnt " + ret);
            assertNotNull("Didn't get the payment id", ret);
            
            /*
             * get
             */
            //verify the created payment       
            System.out.println("Getting created payment");
            PaymentWS retPayment = api.getPayment(ret);
            assertNotNull("didn't get payment ", retPayment);
            assertEquals("created payment result", retPayment.getResultId(),
                    payment.getResultId());
            assertEquals("created payment cheque ", retPayment.getCheque().getNumber(),
                    payment.getCheque().getNumber());
            assertEquals("created payment user ", retPayment.getUserId(), 
                    payment.getUserId());
            System.out.println("Validated created payment and paid invoice");
            assertNotNull("payment not related to invoice", retPayment.getInvoiceIds());
            assertTrue("payment not related to invoice", 
                    retPayment.getInvoiceIds().length == 1);
            assertEquals("payment not related to invoice", 
                    retPayment.getInvoiceIds()[0], new Integer(35));
            
            InvoiceWS retInvoice = api.getInvoiceWS(retPayment.getInvoiceIds()[0]);
            assertNotNull("New invoice not present", retInvoice);
            assertEquals("Balance of invoice should be total of order", retInvoice.getBalance(),
                    new Float(0));
            assertEquals("Total of invoice should be total of order", retInvoice.getTotal(),
                    new Float(15));
            assertEquals("New invoice not paid", retInvoice.getToProcess(), new Integer(0));
            assertNotNull("invoice not related to payment", retInvoice.getPayments());
            assertTrue("invoice not related to payment", 
                    retInvoice.getPayments().length == 1);
            assertEquals("invoice not related to payment", 
                    retInvoice.getPayments()[0], retPayment.getId());
            
            /*
             * get latest
             */
            //verify the created payment       
            System.out.println("Getting latest");
            retPayment = api.getLatestPayment(new Integer(2));
            assertNotNull("didn't get payment ", retPayment);
            assertEquals("latest id", ret, retPayment.getId());
            assertEquals("created payment result", retPayment.getResultId(),
                    payment.getResultId());
            assertEquals("created payment cheque ", retPayment.getCheque().getNumber(),
                    payment.getCheque().getNumber());
            assertEquals("created payment user ", retPayment.getUserId(), 
                    payment.getUserId());
            try {
                System.out.println("Getting latest - invalid");
                retPayment = api.getLatestPayment(new Integer(13));
                fail("User 13 belongs to entity 301");
            } catch (Exception e) {
            }
            
            /*
             * get last
             */
            System.out.println("Getting last");
            Integer retPayments[] = api.getLastPayments(new Integer(2), 
            		new Integer(2));
            assertNotNull("didn't get payment ", retPayments);
            // fetch the payment
            
            
            retPayment = api.getPayment(retPayments[0]);
            
            assertEquals("created payment result", retPayment.getResultId(),
                    payment.getResultId());
            assertEquals("created payment cheque ", retPayment.getCheque().getNumber(),
                    payment.getCheque().getNumber());
            assertEquals("created payment user ", retPayment.getUserId(), 
                    payment.getUserId());
            assertTrue("No more than two records", retPayments.length <= 2);
            try {
                System.out.println("Getting last - invalid");
                retPayments = api.getLastPayments(new Integer(13), 
                	new Integer(2));
                fail("User 13 belongs to entity 301");
            } catch (Exception e) {
            }
            
            
            /*
             * TODO test refunds. There are no refund WS methods.
             * Using applyPayment with is_refund = 1 DOES NOT work
             */

 
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    public void testBlacklistFilter() {
        try {
            final Integer USER_ID = 1000;

            JbillingAPI api = JbillingAPIFactory.getAPI();

            // create a new order and invoice it
            OrderWS order = new OrderWS();            
            order.setUserId(USER_ID); 
            order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
            order.setPeriod(2);
            order.setCurrencyId(new Integer(1));

            // add a line
            OrderLineWS lines[] = new OrderLineWS[1];
            OrderLineWS line;
            line = new OrderLineWS();
            line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            line.setQuantity(new Integer(1));
            line.setItemId(new Integer(1));
            line.setUseItem(new Boolean(true));
            lines[0] = line;

            order.setOrderLines(lines);

            // create the order and invoice it
            System.out.println("Creating and invoicing order ...");
            Integer orderId = api.createOrderAndInvoice(order);
            assertNotNull("The order was not created", orderId);

            // get invoice id
        	InvoiceWS invoice = api.getLatestInvoice(USER_ID);
        	assertNotNull("Couldn't get last invoice", invoice);
        	Integer invoiceId = invoice.getId();
    	    assertNotNull("Invoice id was null", invoiceId);

            // try paying the invoice
            System.out.println("Trying to pay invoice for blacklisted user ...");
            PaymentAuthorizationDTOEx authInfo = api.payInvoice(invoiceId);
        	assertNotNull("Payment result empty", authInfo);

            // check that it was failed by the test blacklist filter
            assertFalse("Payment wasn't failed", authInfo.getResult().booleanValue());
            assertEquals("Processor response", "This user is blacklisted", 
                authInfo.getResponseMessage());

            // remove invoice and order
            api.deleteInvoice(invoiceId);
            api.deleteOrder(orderId);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }
}
