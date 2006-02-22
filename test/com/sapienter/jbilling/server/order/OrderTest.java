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

package com.sapienter.jbilling.server.order;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.entity.OrderLineDTO;
import com.sapienter.jbilling.server.process.BillingProcessTest;

public class OrderTest extends TestCase {

    public OrderTest(String arg0) {
        super(arg0);
    }
    
    public void testGetOrder() {
        try {
            OrderSessionHome customerHome =
                    (OrderSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    OrderSessionHome.class,
                    OrderSessionHome.JNDI_NAME);
            OrderSession remoteSession = customerHome.create();
             
            OrderDTO order = remoteSession.getOrder(new Integer(10));
            assertNotNull(order);
             
            // check a few fields here and there
            assertEquals("create date", BillingProcessTest.parseDate(
                   "2002-12-01"), order.getCreateDate());
            assertEquals("created by", new Integer(1), order.getCreatedBy());
            assertEquals("billing type", new Integer(2), order.getBillingTypeId());            
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
        
    }

    public void testGetOrderEx() {
        try {
            OrderSessionHome customerHome =
                    (OrderSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    OrderSessionHome.class,
                    OrderSessionHome.JNDI_NAME);
            OrderSession remoteSession = customerHome.create();
            OrderDTOEx order = remoteSession.getOrderEx(new Integer(11),
                    new Integer(1));
            
            assertNotNull(order);
            assertNotNull(order.getInvoices());
            assertNotNull(order.getOrderLines());
            assertTrue(order.getOrderLines().size() > 0);
            
            OrderLineDTO line = (OrderLineDTO) order.getOrderLines().get(1);
            assertNotNull(line);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
    }

    public void testNotification() {
        try {
            OrderSessionHome customerHome =
                    (OrderSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    OrderSessionHome.class,
                    OrderSessionHome.JNDI_NAME);
            OrderSession remoteSession = customerHome.create();
            
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(2003, Calendar.MARCH, 28);
            remoteSession.reviewNotifications(cal.getTime());
            
            // get the order that should have generated  the notif
            OrderDTO order = remoteSession.getOrder(new Integer(25));
            assertEquals("Notif flag", order.getNotify(), new Integer(1));
            assertEquals("Step", order.getNotificationStep(), new Integer(1));
            assertNotNull("Notif date", order.getLastNotified());
            Date last = order.getLastNotified();

            //if I call it again a day later, it shouldn't notify
            cal.set(2003, Calendar.MARCH, 29);
            remoteSession.reviewNotifications(cal.getTime());
            order = remoteSession.getOrder(new Integer(25));
            assertEquals("Last notif 1", last, order.getLastNotified());
            
            // make another call the same day that expires
            cal.set(2003, Calendar.APRIL, 1);
            remoteSession.reviewNotifications(cal.getTime());
            order = remoteSession.getOrder(new Integer(25));
            assertEquals("Notif flag 2", order.getNotify(), new Integer(0));
            assertEquals("Step 2", order.getNotificationStep(), new Integer(3));
            assertNotSame("Last notif 2", last, order.getLastNotified());
            
            // if I call it again, it shouldn't notify
            last = order.getLastNotified();
            remoteSession.reviewNotifications(cal.getTime());
            order = remoteSession.getOrder(new Integer(25));
            assertEquals("Last notif 3", last, order.getLastNotified());
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
    }

}
