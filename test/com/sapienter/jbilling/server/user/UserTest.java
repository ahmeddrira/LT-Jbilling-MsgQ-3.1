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

package com.sapienter.jbilling.server.user;

import java.util.Calendar;
import java.util.Vector;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.BillingProcessSession;
import com.sapienter.jbilling.interfaces.BillingProcessSessionHome;
import com.sapienter.jbilling.interfaces.NewOrderSession;
import com.sapienter.jbilling.interfaces.NewOrderSessionHome;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.interfaces.PaymentSession;
import com.sapienter.jbilling.interfaces.PaymentSessionHome;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.ContactDTO;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.util.Constants;

/**
 * Test Cases for the user classes
 * 
 * @author emilc
 *
 */
public class UserTest extends TestCase {


    public void testUserGeneral() {
        try {
            Integer userId = new Integer(1);
            UserDTOEx clientUser =
                new UserDTOEx(
                    userId,
                    new Integer(1),
                    "emil",
                    "asdfasdf",
                    null,
                    new Integer(1), // language en
                    new Integer(1),  // role root
                    new Integer(1), // US$
                    null, null);

            UserSessionHome userHome =
                    (UserSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            UserSession lSession = userHome.create();
            
            // test the authentication
            UserDTOEx result = lSession.authenticate(clientUser); 
            assertNotNull("call to authenitcate",result);
            
            // test the permissions
            assertTrue("permission 1 is granted",result.isGranted(new Integer(1)));
            // check some falses with some ridiculus values
            assertFalse(result.isGranted(new Integer(-1)));
            assertFalse(result.isGranted(new Integer(99999)));
            
            // test the menu
            Menu menu = result.getMenu();
            assertNotNull("menu", menu);
            assertNotNull("options not null", menu.getOptions());
            assertFalse("options not empty", menu.getOptions().isEmpty());
            menu.selectOption(new Integer(4)); // select system
            assertFalse("system suboptions not empty", 
                    menu.getSubOptions().isEmpty());
            menu.selectOption(new Integer(5)); // select user
            assertFalse("system lm options not empty", 
                    menu.getLMOptions().isEmpty());
           
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    public void testCreateDelete() {
        try {
            UserSessionHome userHome =
                    (UserSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            UserSession lSession = userHome.create();
            
            UserDTOEx clientUser = new UserDTOEx(
                null,
                new Integer(1),
                "phantom",
                "asdfasdf",
                null,
                new Integer(1), // language en
                new Integer(1),  // root
                new Integer(1),  // US$
                null, null
            );
            Vector roles = new Vector();
            roles.add(new Integer(1));
            clientUser.setRoles(roles);
            
            ContactDTO contact = new ContactDTO();
            contact.setFirstName("JUnit test!");

            // first call should create ok
            Integer newUserId = lSession.create(clientUser, contact);
            assertNotNull("creation", newUserId);
            // second should fail as already there
            assertNull("create existing", lSession.create(clientUser, null));

            // now update this user
            clientUser = lSession.getUserDTOEx(newUserId);
            clientUser.setPassword("qwerqwer");
            lSession.update(newUserId, clientUser);
            clientUser = lSession.getUserDTOEx(newUserId);
            assertEquals("update - password", clientUser.getPassword(), 
                    "qwerqwer");
                    
            // test the contacts
            contact = lSession.getPrimaryContactDTO(newUserId);
            assertEquals("contact - first name", contact.getFirstName(),
                    "JUnit test!");
            contact.setLastName("testable string");
            lSession.setPrimaryContact(contact, newUserId);
            contact = lSession.getPrimaryContactDTO(newUserId);
            assertEquals("contact - last name", contact.getLastName(),
                    "testable string");
                    
                    
            // now delete the new user
            lSession.delete(new Integer(1), newUserId);
            
 
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }
    
    public void testStatusChange() {
        try {
            UserSessionHome userHome = (UserSessionHome) JNDILookup.getFactory(
                    true).lookUpHome(UserSessionHome.class, 
                        UserSessionHome.JNDI_NAME);
            UserSession lSession = userHome.create();
            OrderSessionHome orderHome = (OrderSessionHome) JNDILookup.getFactory(
                    true).lookUpHome(OrderSessionHome.class, 
                        OrderSessionHome.JNDI_NAME);
            OrderSession orderSession = orderHome.create();
            Integer entityId = new Integer(1);

            // create the user to play with, so this test can run many times,
            // without a fresh db
            UserDTOEx clientUser = new UserDTOEx(
                null,
                entityId,
                "phantom2",
                "asdfasdf",
                null,
                new Integer(1), // language en
                new Integer(4),  // customer
                new Integer(1),  // US$
                null, null
            );
            Vector roles = new Vector();
            roles.add(new Integer(1));
            clientUser.setRoles(roles);
            
            ContactDTO contact = new ContactDTO();
            contact.setFirstName("JUnit test!");
            contact.setEmail("emilconde@telus.net");

            // create the user
            Integer userId = lSession.create(clientUser, contact);
            
            // add an order 
            NewOrderDTO summary = new NewOrderDTO();
            summary.setPeriod(new Integer(1));
            summary.setUserId(userId);
            NewOrderSessionHome nOrderHome =
                    (NewOrderSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    NewOrderSessionHome.class,
                    NewOrderSessionHome.JNDI_NAME); 

            // complete this order's data
            summary.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);

            NewOrderSession nOrderS = nOrderHome.create(summary, new Integer(1));

            NewOrderDTO thisOrder = nOrderS.addItem(new Integer(1), 
                    new Integer(5), userId, entityId);
            // add a few items
            thisOrder = nOrderS.addItem(new Integer(2), new Integer(3), userId, entityId);
            Integer orderId = nOrderS.createUpdate(entityId, 
                    new Integer(1), thisOrder);

            
            
            // let's do a full circle
            for (int f = UserDTOEx.STATUS_ACTIVE.intValue(); 
                    f < UserDTOEx.STATUS_DELETED.intValue(); f++) {
                lSession.setUserStatus(null, userId, new Integer(f));
                assertEquals("status has changed", new Integer(f), 
                        lSession.getUserDTOEx(userId).getStatusId());
                // check that her order is with the right status
                OrderDTO order = orderSession.getOrder(orderId);
                if (f < UserDTOEx.STATUS_ACTIVE.intValue() + 4) {
                    assertEquals("order status active", order.getStatusId(),
                            Constants.ORDER_STATUS_ACTIVE);
                } else {
                    assertEquals("order status suspended", order.getStatusId(),
                            Constants.ORDER_STATUS_SUSPENDED);
                }
            }
            
            
            // Now this user will finally pay, she should be out of the
            // ageing after it
            PaymentSessionHome customerHome =
                    (PaymentSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    PaymentSessionHome.class,
                    PaymentSessionHome.JNDI_NAME);
            PaymentSession remoteSession = customerHome.create();
            //we need an invoice first
            BillingProcessSessionHome processHome =
                    (BillingProcessSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    BillingProcessSessionHome.class,
                    BillingProcessSessionHome.JNDI_NAME);
            BillingProcessSession process = processHome.create();
            InvoiceDTO invoice = process.generateInvoice(orderId);
            
            
            PaymentDTOEx payment = new PaymentDTOEx(null, invoice.getBalance(),
                    Calendar.getInstance().getTime(), 
                    Calendar.getInstance().getTime(),
                    new Integer(1), new Integer(0), new Integer(1),
                    new Integer(1), new Integer(0), invoice.getCurrencyId());
            payment.setUserId(userId);
            remoteSession.applyPayment(payment,invoice.getId());
            clientUser = lSession.getUserDTOEx(userId);
            assertEquals("User back to active", UserDTOEx.STATUS_ACTIVE, 
                    clientUser.getStatusId());

            
            // then delete it
            lSession.setUserStatus(null, userId, UserDTOEx.STATUS_DELETED);
            assertEquals("user is deleted", new Integer(1), 
                    lSession.getUserDTOEx(userId).getDeleted());
            OrderDTO order = orderSession.getOrder(orderId);
            assertEquals("order deleted", new Integer(1), order.getDeleted());

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

}
