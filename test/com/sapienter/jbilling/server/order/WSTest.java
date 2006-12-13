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
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.order;

import java.util.Calendar;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

import com.sapienter.jbilling.server.util.Constants;

/**
 * @author Emil
 */
public class WSTest extends TestCase {
      
    public void testCreateUpdateDelete() {
        try {
        	int i;
            /* If using https, you need an ssh key. You can configure ANT to
             * pass on the java properties like this:
             * export ANT_OPTS="-Djavax.net.ssl.trustStore=c:\\\\sapienter\\\\ssl\\\\client.keystore -Djavax.net.ssl.trustStorePassword=claudius"
             */
            String endpoint = "http://localhost/jboss-net/services/billing";
            
            Service  service = new Service();
            Call  call = (Call) service.createCall();
            call.setTargetEndpointAddress( new java.net.URL(endpoint) );
            call.setOperationName("createOrder");
            call.setUsername("admin");
            call.setPassword("asdfasdf");
            

            // OrderWS            
            QName qn = new QName("http://www.sapienter.com/billing", "OrderWS");
            BeanSerializerFactory ser1 = new BeanSerializerFactory(
                    OrderWS.class, qn);
            BeanDeserializerFactory ser2 = new BeanDeserializerFactory (
                    OrderWS.class, qn);
            call.registerTypeMapping(OrderWS.class, qn, ser1, ser2); 

            // OrderLineWS            
            qn = new QName("http://www.sapienter.com/billing", "OrderLineWS");
            ser1 = new BeanSerializerFactory(
                    OrderLineWS.class, qn);
            ser2 = new BeanDeserializerFactory (
                    OrderLineWS.class, qn);
            call.registerTypeMapping(OrderLineWS.class, qn, ser1, ser2); 

            /*
             * Create
             */
            OrderWS newOrder = new OrderWS();
            newOrder.setUserId(new Integer(2)); // for gandlaf
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
            Integer ret = (Integer) call.invoke( new Object[] { newOrder } );
            assertNotNull("The order was not created", ret);
            // create another one so we can test get by period.
            ret = (Integer) call.invoke( new Object[] { newOrder } );
            System.out.println("Created order " + ret);
            
            /*
             * get
             */
            //verify the created order       
            call.setOperationName("getOrder");
            // try getting one that doesn't belong to us
            try {
                call.invoke( new Object[] { new Integer(5) } );
                fail("Order 5 belongs to entity 2");
            } catch (Exception e) {
            }
            System.out.println("Getting created order");
            OrderWS retOrder = (OrderWS) call.invoke( new Object[] { ret } );
            assertEquals("created order billing type", retOrder.getBillingTypeId(),
                    newOrder.getBillingTypeId());
            assertEquals("created order billing period", retOrder.getPeriod(),
                    newOrder.getPeriod());
            //retOrder
            
            /*
             * get order line
             */
            call.setOperationName("getOrderLine");
            // try getting one that doesn't belong to us
            try {
                System.out.println("Getting bad order line");
                call.invoke( new Object[] { new Integer(6) } );
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
	            retOrderLine = (OrderLineWS) call.invoke( new Object[] { lineId } );
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
            call.setOperationName("updateOrderLine");
            retOrderLine.setQuantity(new Integer(99));
            lineId = retOrderLine.getId();
            try {
                System.out.println("Updating bad order line");
                retOrderLine.setId(new Integer(6));
                call.invoke( new Object[] { retOrderLine } );
                fail("Order line 6 belongs to entity 301");
            } catch (Exception e) {
            }
            retOrderLine.setId(lineId);
            System.out.println("Update order line");
            call.invoke( new Object[] { retOrderLine } );
            call.setOperationName("getOrderLine");
            retOrderLine = (OrderLineWS) call.invoke( new Object[] { retOrderLine.getId() } );
            assertEquals("updated quantity", retOrderLine.getQuantity(),
                    new Integer(99));
            //delete a line through updating with quantity = 0
            System.out.println("Delete order line");
            call.setOperationName("updateOrderLine");
            retOrderLine.setQuantity(new Integer(0));
            call.invoke( new Object[] { retOrderLine } );
            int totalLines = retOrder.getOrderLines().length;
            call.setOperationName("getOrder");
            retOrder = (OrderWS) call.invoke( new Object[] { retOrder.getId() } );
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
            call.setOperationName("updateOrder");
            call.setReturnClass(null);
            System.out.println("Updating order...");
            call.invoke( new Object[] { retOrder } );
            // try to update an order of another entity
            try {
            	System.out.println("Updating bad order...");
                retOrder.setId(new Integer(5));
                call.invoke( new Object[] { retOrder } );
                fail("Order 5 belongs to entity 2");
            } catch (Exception e) {
            }
            // and ask for it to verify the modification
            call.setOperationName("getOrder");
            System.out.println("Getting updated order ");
            retOrder = (OrderWS) call.invoke( new Object[] { ret } );
            assertNotNull("Didn't get updated order", retOrder);
            assertEquals("Active since", retOrder.getActiveSince(),
                    cal.getTime()); 
            assertEquals("Status id", new Integer(2), retOrder.getStatusId());
            assertEquals("Modified line description", "Modified description",
            		retOrder.getOrderLines()[1].getDescription());
            assertEquals("Modified quantity", new Integer(2),
            		retOrder.getOrderLines()[1].getQuantity());
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
            call.setOperationName("getLatestOrder");
            System.out.println("Getting latest");
            retOrder = (OrderWS) call.invoke( new Object[] { 
                    new Integer(2) } );
            assertNotNull("Didn't get any latest order", retOrder);
            assertEquals("Latest id", ret, retOrder.getId());
            // now one for an invalid user
            System.out.println("Getting latest invalid");
            try {
                retOrder = (OrderWS) call.invoke( new Object[] { 
                        new Integer(13) } );
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }

            /*
             * Get last
             */
            call.setOperationName("getLastOrders");
            System.out.println("Getting last 5 ... ");
            int list[] = (int[]) call.invoke( new Object[] { 
                    new Integer(2), new Integer(5) } );
            assertNotNull("Missing list", list);
            assertTrue("No more than five", list.length <= 5);
            
            call.setOperationName("getOrder");
            // the first in the list is the last one created
            retOrder = (OrderWS) call.invoke( new Object[] { new Integer(list[0]) } );
            assertEquals("Latest id", ret, retOrder.getId());
            
            // try to get the orders of my neighbor
            try {
                System.out.println("Getting last 5 - invalid");
                call.invoke( new Object[] { new Integer(13),
                        new Integer(5)} );
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }

            /*
             * Delete
             */        
            call.setOperationName("deleteOrder");
            System.out.println("Deleteing order " + ret);
            call.invoke( new Object[] { ret } );
            // try to delete from my neightbor
            try {
                call.invoke( new Object[] { new Integer(5) } );
                fail("Order 5 belongs to entity 2");
            } catch (Exception e) {
            }
            // try to get the deleted order
            try {
                call.setOperationName("getOrder");
                System.out.println("Getting deleted order ");
                retOrder = (OrderWS) call.invoke( new Object[] { ret } );
                fail("Order " + ret + " should have been deleted");
            } catch (Exception e) {
            }
           
            /*
             * Get by user and period
             */
            call.setOperationName("getOrderByPeriod");
            System.out.println("Getting orders by period for invalid user " + ret);
            // try to get from my neightbor
            try {
                call.invoke( new Object[] { new Integer(13), new Integer(1) } );
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }
            // now from a valid user
            System.out.println("Getting orders by period ");
            int orders[] = (int[]) call.invoke( new Object[] { 
                    new Integer(2), new Integer(1) } );
            System.out.println("Got total orders " + orders.length +
                    " first is " + orders[0]);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    
}
