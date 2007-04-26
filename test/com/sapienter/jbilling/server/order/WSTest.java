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

/*
 * Created on Dec 18, 2003
 *
 */
package com.sapienter.jbilling.server.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

/**
 * @author Emil
 */
public class WSTest  extends TestCase {
	private static final Integer GANDALF_USER_ID = 2;
      
    public void testCreateUpdateDelete() {
        try {
        	
            JbillingAPI api = JbillingAPIFactory.getAPI();
        	int i;

            /*
             * Create
             */
            OrderWS newOrder = new OrderWS();
            newOrder.setUserId(GANDALF_USER_ID); 
            newOrder.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
            newOrder.setPeriod(new Integer(1)); // once
            newOrder.setCurrencyId(new Integer(1));
            
            // now add some lines
            OrderLineWS lines[] = new OrderLineWS[4];
            OrderLineWS line;
            
            line = new OrderLineWS();
            line.setPrice(new Float(10));
            line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            line.setQuantity(new Integer(1));
            line.setAmount(new Float(10));
            line.setDescription("Fist line");
            line.setItemId(new Integer(1));
            lines[0] = line;
            
            // this is an item line
            line = new OrderLineWS();
            line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            line.setQuantity(new Integer(1));
            line.setItemId(new Integer(2));
            // take the description from the item
            line.setUseItem(new Boolean(true));
            lines[1] = line;
            
            // this is an item line
            line = new OrderLineWS();
            line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            line.setQuantity(new Integer(1));
            line.setItemId(new Integer(3));
            line.setUseItem(new Boolean(true));
            lines[2] = line;

            // this is a discount (10%)
            line = new OrderLineWS();
            line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            line.setQuantity(new Integer(1));
            line.setItemId(new Integer(14));
            line.setUseItem(new Boolean(true));
            lines[3] = line;

            newOrder.setOrderLines(lines);
            
            System.out.println("Creating order ...");
            Integer ret = api.createOrder(newOrder);
            assertNotNull("The order was not created", ret);
            // create another one so we can test get by period.
            ret = api.createOrder(newOrder);
            System.out.println("Created order " + ret);
            
            /*
             * get
             */
            //verify the created order       
            // try getting one that doesn't belong to us
            try {
                api.getOrder(new Integer(5));
                fail("Order 5 belongs to entity 2");
            } catch (Exception e) {
            }
            System.out.println("Getting created order");
            OrderWS retOrder = api.getOrder(ret);
            assertEquals("created order billing type", retOrder.getBillingTypeId(),
                    newOrder.getBillingTypeId());
            assertEquals("created order billing period", retOrder.getPeriod(),
                    newOrder.getPeriod());
            //retOrder
            
            /*
             * get order line
             */
            // try getting one that doesn't belong to us
            try {
                System.out.println("Getting bad order line");
                api.getOrderLine(new Integer(6));
                fail("Order line 6 belongs to entity 6");
            } catch (Exception e) {
            }
            System.out.println("Getting created order line");
            boolean found = false;
            OrderLineWS retOrderLine = null;
            OrderLineWS normalOrderLine = null;
            Integer lineId = null;
            for (i = 0; i < retOrder.getOrderLines().length; i++) {
	            lineId = retOrder.getOrderLines()[i].getId();
	            retOrderLine = api.getOrderLine(lineId);
	            if (retOrderLine.getItemId().equals(new Integer(14))) {
	                assertEquals("created line item id", retOrderLine.getItemId(), 
	                        new Integer(14));
	                assertEquals("total of discount", new Float(-4.5), retOrderLine.getAmount());
	                found = true;
	            } else {
	            	normalOrderLine = retOrderLine;
	            	if (found) break;
	            }
            }
            assertTrue("Order line not found", found);
            
            /*
             * Update the order line
             */
            retOrderLine = normalOrderLine; // use a normal one, not the percentage
            retOrderLine.setQuantity(new Integer(99));
            lineId = retOrderLine.getId();
            try {
                System.out.println("Updating bad order line");
                retOrderLine.setId(new Integer(6));
                api.updateOrderLine(retOrderLine);
                fail("Order line 6 belongs to entity 301");
            } catch (Exception e) {
            }
            retOrderLine.setId(lineId);
            System.out.println("Update order line");
            api.updateOrderLine(retOrderLine);
            retOrderLine = api.getOrderLine(retOrderLine.getId());
            assertEquals("updated quantity", retOrderLine.getQuantity(),
                    new Integer(99));
            //delete a line through updating with quantity = 0
            System.out.println("Delete order line");
            retOrderLine.setQuantity(new Integer(0));
            api.updateOrderLine(retOrderLine);
            int totalLines = retOrder.getOrderLines().length;
            retOrder = api.getOrder(retOrder.getId());
            // the order has to have one less line now
            assertEquals("order should have one less line", totalLines, 
                    retOrder.getOrderLines().length + 1);
             
            /*
             * Update
             */
            // now update the created order
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(2003, 9, 29, 0, 0, 0);
            retOrder.setActiveSince(cal.getTime());
            retOrder.getOrderLines()[1].setDescription("Modified description");
            retOrder.getOrderLines()[1].setQuantity(new Integer(2));
            retOrder.setStatusId(new Integer(2));
            // also update the next billable day
            retOrder.setNextBillableDay(cal.getTime());
            System.out.println("Updating order...");
            api.updateOrder(retOrder);
            
            // try to update an order of another entity
            try {
            	System.out.println("Updating bad order...");
                retOrder.setId(new Integer(5));
                api.updateOrder(retOrder);
                fail("Order 5 belongs to entity 2");
            } catch (Exception e) {
            }
            // and ask for it to verify the modification
            System.out.println("Getting updated order ");
            retOrder = api.getOrder(ret);
            assertNotNull("Didn't get updated order", retOrder);
            assertEquals("Active since", retOrder.getActiveSince(),
                    cal.getTime()); 
            assertEquals("Status id", new Integer(2), retOrder.getStatusId());
            assertEquals("Modified line description", "Modified description",
            		retOrder.getOrderLines()[1].getDescription());
            assertEquals("Modified quantity", new Integer(2),
            		retOrder.getOrderLines()[1].getQuantity());
            assertEquals("New billable date", cal.getTimeInMillis(), 
                    retOrder.getNextBillableDay().getTime());
            for (i = 0; i < retOrder.getOrderLines().length; i++) {
            	retOrderLine = retOrder.getOrderLines()[i];
	            if (retOrderLine.getItemId().equals(new Integer(14))) {
	            	// the is one less line for 15
	            	// but one extra item for 20
	            	// difference is 5 and 10% of that is 0.5  thus 4.5 + 0.5 = 5
	                assertEquals("total of discount", new Float(-5.0), retOrderLine.getAmount());
	                break;
	            } 
            }
            
            assertFalse(i == retOrder.getOrderLines().length);
           
            /*
             * Get latest
             */
            System.out.println("Getting latest");
            OrderWS lastOrder = api.getLatestOrder(new Integer(2));
            assertNotNull("Didn't get any latest order", lastOrder);
            assertEquals("Latest id", ret, lastOrder.getId());
            // now one for an invalid user
            System.out.println("Getting latest invalid");
            try {
            	retOrder = api.getLatestOrder(new Integer(13));
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }

            /*
             * Get last
             */
            System.out.println("Getting last 5 ... ");
            Integer[] list = api.getLastOrders(new Integer(2), new Integer(5));
            assertNotNull("Missing list", list);
            assertTrue("No more than five", list.length <= 5);
            
            // the first in the list is the last one created
            retOrder = api.getOrder(new Integer(list[0]));
            assertEquals("Latest id", ret, retOrder.getId());
            
            // try to get the orders of my neighbor
            try {
                System.out.println("Getting last 5 - invalid");
                api.getOrder(new Integer(5));
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }

            /*
             * Delete
             */        
            System.out.println("Deleteing order " + ret);
            api.deleteOrder(ret);
            // try to delete from my neightbor
            try {
            	api.deleteOrder(new Integer(5));
                fail("Order 5 belongs to entity 2");
            } catch (Exception e) {
            }
            // try to get the deleted order
            try {
                System.out.println("Getting deleted order ");
                retOrder = api.getOrder(ret);
                fail("Order " + ret + " should have been deleted");
            } catch (Exception e) {
            }
           
            /*
             * Get by user and period
             */
            System.out.println("Getting orders by period for invalid user " + ret);
            // try to get from my neightbor
            try {
            	api.getOrderByPeriod(new Integer(13), new Integer(1));
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }
            // now from a valid user
            System.out.println("Getting orders by period ");
            Integer orders[] = api.getOrderByPeriod(new Integer(2), new Integer(1));
            System.out.println("Got total orders " + orders.length +
                    " first is " + orders[0]);
            
            /*
             * Create an order with pre-authorization
             */
            System.out.println("Create an order with pre-authorization" + ret);
            PaymentAuthorizationDTOEx auth = (PaymentAuthorizationDTOEx) 
        	api.createOrderPreAuthorize(newOrder);
            assertNotNull("Missing list", auth);
            // the test processor should always approve gandalf
            assertEquals("Result is ok", new Boolean(true), auth.getResult());
            System.out.println("Order pre-authorized. Approval code = " + auth.getApprovalCode());
            // check the last one is a new one
            System.out.println("Getting latest");
            retOrder = api.getLatestOrder(new Integer(2));
            System.out.println("Order created with ID = " + retOrder.getId());
            assertNotSame("New order is there", retOrder.getId(), lastOrder.getId());
            // delete this order
            System.out.println("Deleteing order " + retOrder.getId());
            api.deleteOrder(retOrder.getId());


        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }
    
    public void testCreateOrderAutoCreatesAnInvoice() throws Exception {
    	final int USER_ID = GANDALF_USER_ID;
    	InvoiceWS before = callGetLatestInvoice(USER_ID);
    	assertTrue(before == null || before.getId() != null);
    	
    	OrderWS order = createMockOrder(USER_ID, 3, 42);
    	Integer orderId = callCreateOrder(order);
        assertNotNull(orderId);
        
        InvoiceWS afterNormalOrder = callGetLatestInvoice(USER_ID);
        assertNotNull("createOrder should create invoice", afterNormalOrder);
        assertNotNull("invoice without id", afterNormalOrder.getId());
        
        if (before != null){
        	assertFalse("createOrder should create the most recent invoice", afterNormalOrder.getId().equals(before.getId()));
        }
        
        //even if empty
    	OrderWS emptyOrder = createMockOrder(USER_ID, 0, 123); //empty
    	Integer emptyOrderId = callCreateOrder(emptyOrder);
        assertNotNull(emptyOrderId);
        
        InvoiceWS afterEmptyOrder = callGetLatestInvoice(USER_ID);
        assertNotNull("invoice without id", afterEmptyOrder.getId());
        assertNotNull("createOrder should create invoice even for empty order", afterEmptyOrder);
        assertFalse("createOrder should create the most recent invoice", afterNormalOrder.getId().equals(afterEmptyOrder.getId()));
    }

    public void testCreateNotActiveOrderDoesNotCreateInvoices() throws Exception {
    	final int USER_ID = GANDALF_USER_ID;
    	InvoiceWS before = callGetLatestInvoice(USER_ID);
    	
    	OrderWS orderWS = createMockOrder(USER_ID, 2, 234);
    	orderWS.setActiveSince(nextWeek());
    	Integer orderId = callCreateOrder(orderWS);
    	assertNotNull(orderId);
    	
    	InvoiceWS after = callGetLatestInvoice(USER_ID);
    	
    	if (before == null){
    		assertNull("Not yet active order -- no new invoices expected", after);
    	} else {
    		assertEquals("Not yet active order -- no new invoices expected", before.getId(), after.getId());
    	}
    }
    
    public void testCreatedOrderIsCorrect() throws Exception {
    	final int USER_ID = GANDALF_USER_ID;
    	final int LINES = 3;
    	
    	OrderWS requestOrder = createMockOrder(USER_ID, LINES, 567);
    	assertEquals(LINES, requestOrder.getOrderLines().length);
    	Integer orderId = callCreateOrder(requestOrder);
    	assertNotNull(orderId);
    	
        JbillingAPI api = JbillingAPIFactory.getAPI();
    	OrderWS resultOrder = api.getOrder(orderId);
    	assertNotNull(resultOrder);
    	assertEquals(orderId, resultOrder.getId());
    	assertEquals(LINES, resultOrder.getOrderLines().length);
    	
    	HashMap<String, OrderLineWS> actualByDescription = new HashMap<String, OrderLineWS>();
    	for (OrderLineWS next : resultOrder.getOrderLines()){
    		assertNotNull(next.getId());
    		assertNotNull(next.getDescription());
    		actualByDescription.put(next.getDescription(), next);
    	}
    	
    	for (int i = 0; i < LINES; i++){
    		OrderLineWS nextRequested = requestOrder.getOrderLines()[i];
    		OrderLineWS nextActual = actualByDescription.remove(nextRequested.getDescription());
    		assertNotNull(nextActual);

    		assertEquals(nextRequested.getDescription(), nextActual.getDescription());
    		assertEquals(nextRequested.getAmount(), nextActual.getAmount());
    		assertEquals(nextRequested.getQuantity(), nextActual.getQuantity());
    		assertEquals(nextRequested.getPrice(), nextActual.getPrice());
    	}
    }
    
    public void testAutoCreatedInvoiceIsCorrect() throws Exception {
    	final int USER_ID = GANDALF_USER_ID;
    	final int LINES = 2;

    	// it is critical to make sure that this invoice can not be composed by
		// previous payments
    	// so, make the price unusual
    	final float PRICE = 687654.29f;
    	
    	OrderWS orderWS = createMockOrder(USER_ID, LINES, PRICE);
    	Integer orderId = callCreateOrder(orderWS);
    	InvoiceWS invoice = callGetLatestInvoice(USER_ID);
    	assertNotNull(invoice.getOrders());
    	assertTrue("Expected: " + orderId + ", actual: " + Arrays.toString(invoice.getOrders()), Arrays.equals(new Integer[] {orderId}, invoice.getOrders()));
    	
    	assertNotNull(invoice.getInvoiceLines());
    	assertEquals(LINES, invoice.getInvoiceLines().length);
    	
    	assertEmptyArray(invoice.getPayments());
    	assertEquals(Integer.valueOf(0), invoice.getPaymentAttempts());
    	
    	assertNotNull(invoice.getBalance());
    	assertEquals(PRICE * LINES, invoice.getBalance().floatValue(), 0.00000001f);
    }
    
    public void testAutoCreatedInvoiceIsPayable() throws Exception {
    	final int USER_ID = GANDALF_USER_ID;
    	callCreateOrder(createMockOrder(USER_ID, 1, 789));
    	InvoiceWS invoice = callGetLatestInvoice(USER_ID);
    	assertNotNull(invoice);
    	assertNotNull(invoice.getId());
        assertEquals("new invoice is not paid", 1, invoice.getToProcess().intValue());
        assertTrue("new invoice with a balance", invoice.getBalance().floatValue() > 0);
        JbillingAPI api = JbillingAPIFactory.getAPI();
    	PaymentAuthorizationDTOEx auth = api.payInvoice(invoice.getId());
    	assertNotNull(auth);
        assertEquals("Payment result OK", true, auth.getResult().booleanValue());
        assertEquals("Processor code", "The transaction has been approved", 
                auth.getResponseMessage());
                
        // now the invoice should be shown as paid
        invoice = callGetLatestInvoice(USER_ID);
        assertNotNull(invoice);
        assertNotNull(invoice.getId());
        assertEquals("new invoice is now paid", 0, invoice.getToProcess().intValue());
        assertTrue("new invoice without a balance", invoice.getBalance().floatValue() == 0F);

    }
    
    public void testEmptyInvoiceIsNotPayable() throws Exception {
    	final int USER_ID = GANDALF_USER_ID;
    	callCreateOrder(createMockOrder(USER_ID, 0, 890)); 
    	InvoiceWS invoice = callGetLatestInvoice(USER_ID);
    	assertNotNull(invoice);
    	assertNotNull(invoice.getId());
        JbillingAPI api = JbillingAPIFactory.getAPI();
        PaymentAuthorizationDTOEx auth = api.payInvoice(invoice.getId());
    	assertNull(auth);
    }
    
    private Date nextWeek() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		return calendar.getTime();
	}

    private InvoiceWS callGetLatestInvoice(int userId) throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();
    	return api.getLatestInvoice(userId);
    }
    
    private Integer callCreateOrder(OrderWS order) throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();
    	return api.createOrder(order);
    	
    }
    
	private OrderWS createMockOrder(int userId, int orderLinesCount, float linePrice) {
		OrderWS order = new OrderWS();
    	order.setUserId(userId); 
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(1); // once
        order.setCurrencyId(1);
        
        ArrayList<OrderLineWS> lines = new ArrayList<OrderLineWS>(orderLinesCount);
        for (int i = 0; i < orderLinesCount; i++){
            OrderLineWS nextLine = new OrderLineWS();
            nextLine.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            nextLine.setDescription("Order line: " + i);
            nextLine.setItemId(i + 1);
            nextLine.setQuantity(1);
            nextLine.setPrice(linePrice);
            nextLine.setAmount(nextLine.getQuantity() * linePrice);
            
            lines.add(nextLine);
        }
        order.setOrderLines(lines.toArray(new OrderLineWS[lines.size()]));
		return order;
	}
	
	private void assertEmptyArray(Object[] array){
		assertNotNull(array);
		assertEquals("Empty array expected: " + Arrays.toString(array), 0, array.length);
	}
	
}
