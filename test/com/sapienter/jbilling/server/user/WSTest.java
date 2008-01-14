/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
 */
package com.sapienter.jbilling.server.user;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;
import com.sapienter.jbilling.server.util.api.WebServicesConstants;

/**
 * @author Emil
 */
public class WSTest extends TestCase {
      
    public void testGetUser() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();

            System.out.println("Getting user 2");
            UserWS ret = api.getUserWS(new Integer(2));
            assertEquals(new Integer(2), ret.getUserId());
            try {
                System.out.println("Getting invalid user 13");
                ret = api.getUserWS(new Integer(13));
                fail("Shouldn't be able to access user 13");
            } catch(Exception e) {
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }


    public void testCreateUpdateDeleteUser() {
        try {
        	JbillingAPI api = JbillingAPIFactory.getAPI();
        	
            /*
             * Create - This passes the password validation routine.
             */
            UserWS newUser = createUser(true, 43);
            Integer newUserId = newUser.getUserId();
            String newUserName = newUser.getUserName();
            assertNotNull("The user was not created", newUserId);
            
            System.out.println("Getting the id of the new user");
            Integer ret = api.getUserId(newUserName);
            assertEquals("Id of new user found", newUserId, ret);
            
            //verify the created user       
            System.out.println("Getting created user ");
            UserWS retUser = api.getUserWS(newUserId);
            assertEquals("created username", retUser.getUserName(),
                    newUser.getUserName());
            assertEquals("created user first name", retUser.getContact().getFirstName(),
                    newUser.getContact().getFirstName());     
            assertEquals("create user parent id", new Integer(43), retUser.getParentId());
            System.out.println("My user: " + retUser);
            
            
            /*
             * Make a create mega call
             */
            System.out.println("Making mega call");
            retUser.setUserName("MU" + Long.toHexString(System.currentTimeMillis()));
            // need to reset the password, it came encrypted
            // let's use a long one
            retUser.setPassword("0fu3js8wl1;a$e2w)xRQ"); 
            // the new user shouldn't be a child
            retUser.setParentId(null);
            
            // need an order for it
            OrderWS newOrder = getOrder();
            
            CreateResponseWS mcRet = api.create(retUser,newOrder);
            
            System.out.println("Validating new invoice");
            // validate that the results are reasonable
            assertNotNull("Mega call result can't be null", mcRet);
            assertNotNull("Mega call invoice result can't be null", mcRet.getInvoiceId());
            // there should be a successfull payment
            assertEquals("Payment result OK", true, mcRet.getPaymentResult().getResult().booleanValue());
            assertEquals("Processor code", "fake-code-default", mcRet.getPaymentResult().getCode1());
            // get the invoice
            InvoiceWS retInvoice = api.getInvoiceWS(mcRet.getInvoiceId());
            assertNotNull("New invoice not present", retInvoice);
            assertEquals("Balance of invoice should be zero, is paid", retInvoice.getBalance(),
                    new Float(0));
            assertEquals("Total of invoice should be total of order", retInvoice.getTotal(),
                    new Float(20));
            assertEquals("New invoice paid", retInvoice.getToProcess(), new Integer(0));
            
            // TO-DO test that the invoice total is equal to the order total
 
            /*
             * Update
             */
            // now update the created user
            System.out.println("Updating user - Pass 1 - Should succeed");
            retUser.setPassword("newPassword1");
            retUser.getCreditCard().setNumber("4111111111111152");
            System.out.println("Updating user...");
            api.updateUser(retUser);
            
            // and ask for it to verify the modification
            System.out.println("Getting updated user ");
            retUser = api.getUserWS(newUserId);
            assertNotNull("Didn't get updated user", retUser);
            // The password should be the same as in the first step, no update happened.
            assertEquals("Password ", retUser.getPassword(),
            		"33aa7e0850c4234ff03beb205b9ea728");
            assertEquals("Contact name", retUser.getContact().getFirstName(),
                    newUser.getContact().getFirstName());
            assertEquals("Credit card updated", "4111111111111152",
                    retUser.getCreditCard().getNumber());

            System.out.println("Updating user - Pass 2 - Should fail due to invalid password");
            retUser.setPassword("newPassword");
            System.out.println("Updating user...");
            boolean catched = false;
            try {
            	api.updateUser(retUser);
            } catch (Throwable e) {
            	catched = true;
            }
            if (!catched) {
            	fail("User was updated - Password validation not working!");
            } else {
            	System.out.println("User was not updated. Password validation worked.");
            }

            // again, for the contact info, and no cc
            retUser.getContact().setFirstName("New Name");
            retUser.getContact().setLastName("New L.Name");
            retUser.setCreditCard(null);
            // call the update
            retUser.setPassword(null); // should not change the password
            api.updateUser(retUser);
            // fetch the user
            UserWS updatedUser = api.getUserWS(newUserId);
            assertEquals("updated f name", retUser.getContact().getFirstName(),
                    updatedUser.getContact().getFirstName());
            assertEquals("updated l name", retUser.getContact().getLastName(),
                    updatedUser.getContact().getLastName());
            assertEquals("Credit card should stay the same", "4111111111111152",
                    updatedUser.getCreditCard().getNumber());
            assertEquals("Password should stay the same", "33aa7e0850c4234ff03beb205b9ea728",
                    updatedUser.getPassword());
            
            System.out.println("Update result:" + updatedUser);

            // now update the contact only
            retUser.getContact().setFirstName("New Name2");
            api.updateUserContact(retUser.getUserId(),new Integer(2),retUser.getContact());
             // fetch the user
            updatedUser = api.getUserWS(newUserId);
            assertEquals("updated contact f name", retUser.getContact().getFirstName(),
                    updatedUser.getContact().getFirstName());
            
            // now update with a bogus contact type
            try {
                System.out.println("Updating with invalid contact type");
                api.updateUserContact(retUser.getUserId(),new Integer(1),retUser.getContact());
                fail("Should not update with an invalid contact type");
            } catch(Exception e) {
                // good
                System.out.println("Type rejected " + e.getMessage());
            }

            // update credit card details
            System.out.println("Removing credit card");
            api.updateCreditCard(newUserId, null);
            assertNull("Credit card removed",api.getUserWS(newUserId).getCreditCard());
            
            System.out.println("Creating credit card");
            String ccName = "New ccName";
            String ccNumber = "4012888888881881";
            Date ccExpiry = Util.truncateDate(Calendar.getInstance().getTime());

            CreditCardDTO cc = new CreditCardDTO();
            cc.setName(ccName);
            cc.setNumber(ccNumber);
            cc.setExpiry(ccExpiry);
            api.updateCreditCard(newUserId,cc);

            // check updated cc details
            retUser = api.getUserWS(newUserId);
            CreditCardDTO retCc = retUser.getCreditCard();
            assertEquals("new cc name", ccName, retCc.getName());
            assertEquals("updated cc number", ccNumber, retCc.getNumber());
            assertEquals("updated cc expiry", ccExpiry, retCc.getExpiry());
            
            System.out.println("Updating credit card");
            cc.setName("Updated ccName");
            cc.setNumber(null);
            api.updateCreditCard(newUserId,cc);
            retUser = api.getUserWS(newUserId);
            assertEquals("updated cc name", "Updated ccName", retUser.getCreditCard().getName());
            assertNotNull("cc number still there", retUser.getCreditCard().getNumber());

            // try to update cc of user from different company
            System.out.println("Attempting to update cc of a user from " 
                    + "a different company");
            try {
            	api.updateCreditCard(new Integer(13),cc);
                fail("Shouldn't be able to update cc of user 13");
            } catch(Exception e) {
            }

            /*
             * Delete
             */
            // now delete this new guy
            System.out.println("Deleting user..." + newUserId);
            api.deleteUser(newUserId);
            
            // try to fetch the deleted user
            System.out.println("Getting deleted user " + newUserId);
            updatedUser = api.getUserWS(newUserId);
            assertEquals(updatedUser.getDeleted(), new Integer(1));
            
            // verify I can't delete users from another company 
            try {
                System.out.println("Deleting user base user ... 13");
                api.getUserWS(new Integer(13));
                fail("Shouldn't be able to access user 13");
            } catch(Exception e) {
            }
            
            
            /*
             * Get list of active customers
             */
            System.out.println("Getting active users...");
            Integer[] users = api.getUsersInStatus(new Integer(1));
            assertEquals(1007,users.length);
            assertEquals("First return user ", 1, users[0].intValue());
            assertEquals("Last returned user ", 1074, users[1006].intValue());

            /*
             * Get list of not active customers
             */
            System.out.println("Getting NOTactive users...");
            users = api.getUsersNotInStatus(new Integer(1));
            assertEquals(users.length, 1);
            for (int f = 0; f < users.length; f++) {
                System.out.println("Got user " + users[f]);
            }

            /*
             * Get list using a custom field
             */
            System.out.println("Getting by custom field...");
            users = api.getUsersByCustomField(new Integer(1),new String("serial-from-ws"));
            
            // the one from the megacall is not deleted and has the custom field
            assertEquals(users.length, 1001); 
            assertEquals(users[1000], mcRet.getUserId());
            
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }
    
    public void testUserTransitions() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();

            System.out.println("Getting complete list of user transitions");
            UserTransitionResponseWS[] ret = api.getUserTransitions(null, null);
            
            if (ret == null)
            	fail("Transition list should not be empty!");
            assertEquals(6, ret.length);
            
            // Check the ids of the returned transitions
            assertEquals(ret[0].getId().intValue(), 1);
            assertEquals(ret[1].getId().intValue(), 2);
            // Check the value of returned data
            assertEquals(ret[0].getUserId().intValue(), 2);
            assertEquals(ret[0].getFromStatusId().intValue(), 2);
            assertEquals(ret[0].getToStatusId().intValue(), 1);
            assertEquals(ret[1].getUserId().intValue(), 2);
            assertEquals(ret[1].getFromStatusId().intValue(), 2);
            assertEquals(ret[1].getToStatusId().intValue(), 1);
            
            // save an ID for later
            Integer myId = ret[4].getId();

            System.out.println("Getting first partial list of user transitions");
            ret =  api.getUserTransitions(new Date(2000 - 1900,0,0), 
            				new Date(2007 - 1900, 0, 1));
            if (ret == null)
            	fail("Transition list should not be empty!");
            assertEquals(ret.length, 1);
            assertEquals(ret[0].getId().intValue(), 1);
            assertEquals(ret[0].getUserId().intValue(), 2);
            assertEquals(ret[0].getFromStatusId().intValue(), 2);
            assertEquals(ret[0].getToStatusId().intValue(), 1);
            
            System.out.println("Getting second partial list of user transitions");
            ret = api.getUserTransitions(null,null);
            if (ret == null)
            	fail("Transition list should not be empty!");
            assertEquals(5, ret.length);
            assertEquals(ret[0].getId().intValue(), 2);
            assertEquals(ret[0].getUserId().intValue(), 2);
            assertEquals(ret[0].getFromStatusId().intValue(), 2);
            assertEquals(ret[0].getToStatusId().intValue(), 1);
            
            System.out.println("Getting list after id");
            ret = api.getUserTransitionsAfterId(myId);
            if (ret == null)
                fail("Transition list should not be empty!");
            assertEquals("Only one transition after id " + myId, 1,ret.length);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }
    
    public void testAuthentication() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();

            System.out.println("Auth with wrong credentials");
            Integer result = api.authenticate("authuser", "notAGoodOne");
            assertEquals("Authentication has to fail", 
                    WebServicesConstants.AUTH_WRONG_CREDENTIALS, result);
            result = api.authenticate("authuser", "notAGoodOne");
            assertEquals("Authentication has to fail", 
                    WebServicesConstants.AUTH_WRONG_CREDENTIALS, result);
            System.out.println("Too many retries");
            result = api.authenticate("authuser", "notAGoodOne");
            assertEquals("Now locked out", 
                    WebServicesConstants.AUTH_LOCKED, result);

            // it is locked, but we know the secret password
            System.out.println("Auth for expired");
            result = api.authenticate("authuser", "totalSecret");
            assertEquals("Password should be expired", 
                    WebServicesConstants.AUTH_EXPIRED, result);
            
            // update the user's password
            Integer userId = api.getUserId("authuser");
            UserWS user = api.getUserWS(userId);
            user.setPassword("234qwe");
            api.updateUser(user);
            
            // try again ...
            System.out.println("Auth after password update");
            result = api.authenticate("authuser", "234qwe");
            assertEquals("Should auth ok now", 
                    WebServicesConstants.AUTH_OK, result);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
        
    }

    public void testParentChild() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();
            /*
             * Create - This passes the password validation routine.
             */
            UserWS newUser = new UserWS();
            newUser.setUserName("ws-parent");
            newUser.setPassword("asdfasdf1");
            newUser.setLanguageId(new Integer(1));
            newUser.setMainRoleId(new Integer(5));
            newUser.setIsParent(new Boolean(true));
            newUser.setStatusId(UserDTOEx.STATUS_ACTIVE);
            
            // add a contact
            ContactWS contact = new ContactWS();
            contact.setEmail("frodo@shire.com");
            newUser.setContact(contact);
            
            System.out.println("Creating parent user ...");
            // do the creation
            Integer parentId = api.createUser(newUser);
            assertNotNull("The user was not created", parentId);
            
            
            //verify the created user       
            System.out.println("Getting created user ");
            UserWS retUser = api.getUserWS(parentId);
            assertEquals("created username", retUser.getUserName(),
                    newUser.getUserName());
            assertEquals("create user is parent", new Boolean(true), retUser.getIsParent());
            
            System.out.println("Creating child user ...");
            // now create the child
            newUser.setIsParent(new Boolean(false));
            newUser.setParentId(parentId);
            newUser.setUserName("childOfParent");
            newUser.setPassword("asdfasdf1");
            newUser.setInvoiceChild(Boolean.TRUE);
            Integer childId = api.createUser(newUser);
            //test
            System.out.println("Getting created user ");
            retUser = api.getUserWS(childId);
            assertEquals("created username", retUser.getUserName(),
                    newUser.getUserName());
            assertEquals("created user parent", parentId, retUser.getParentId());
            assertEquals("created do not invoice child", Boolean.TRUE, retUser.getInvoiceChild());
            
            // test authentication of both
            System.out.println("Authenticating new users ");
            assertEquals("auth of parent", new Integer(0), 
                    api.authenticate("ws-parent", "asdfasdf1"));
            assertEquals("auth of child", new Integer(0), 
                    api.authenticate("childOfParent", "asdfasdf1"));
            
     
            // clean up
            api.deleteUser(parentId);
            api.deleteUser(childId);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
        
    }
    
    public void testGetByCC() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();
            Integer[] ids = api.getUsersByCreditCard("1152");
            assertNotNull("One customer with CC", ids);
            assertEquals("One customer with CC",1, ids.length);
            assertEquals("Created user with CC", 1074, ids[0].intValue());
                    
            // get the user
            assertNotNull("Getting found user",api.getUserWS(ids[0]));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }
    
    private UserWS createUser(boolean goodCC, Integer parentId) throws JbillingAPIException, IOException {
            JbillingAPI api = JbillingAPIFactory.getAPI();
            
            /*
             * Create - This passes the password validation routine.
             */
            UserWS newUser = new UserWS();
            newUser.setUserName("testUserName-" + Calendar.getInstance().getTimeInMillis());
            newUser.setPassword("asdfasdf1");
            newUser.setLanguageId(new Integer(1));
            newUser.setMainRoleId(new Integer(5));
            newUser.setParentId(parentId); // this parent exists
            newUser.setStatusId(UserDTOEx.STATUS_ACTIVE);
            
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
            cc.setNumber(goodCC ? "4111111111111152" : "4111111111111111");
            cc.setExpiry(Calendar.getInstance().getTime());
            newUser.setCreditCard(cc);
            
            System.out.println("Creating user ...");
            newUser.setUserId(api.createUser(newUser));
            
            return newUser;
    }
    
    private OrderWS getOrder() {
        // need an order for it
        OrderWS newOrder = new OrderWS();
        newOrder.setUserId(new Integer(-1)); // it does not matter, the user will be created
        newOrder.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        newOrder.setPeriod(new Integer(1)); // once
        newOrder.setCurrencyId(new Integer(1));
        
        // now add some lines
        OrderLineWS lines[] = new OrderLineWS[2];
        OrderLineWS line;
        
        line = new OrderLineWS();
        line.setPrice(new Float(10));
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setQuantity(new Integer(1));
        line.setAmount(new Float(10));
        line.setDescription("First line");
        line.setItemId(new Integer(1));
        lines[0] = line;
        
        line = new OrderLineWS();
        line.setPrice(new Float(10));
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setQuantity(new Integer(1));
        line.setAmount(new Float(10));
        line.setDescription("Second line");
        line.setItemId(new Integer(2));
        lines[1] = line;
        
        newOrder.setOrderLines(lines);

        return newOrder;
    }
    
    // name changed so it is not called in normal test runs
    public void XXtestLoad() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();
            for (int i = 0; i < 1000; i++) {
                Random rnd = new Random();
                UserWS newUser = createUser(rnd.nextBoolean(), null);
                OrderWS newOrder = getOrder();
                // change the quantities for viarety
                newOrder.getOrderLines()[0].setQuantity(rnd.nextInt(100) + 1);
                newOrder.getOrderLines()[0].setUseItem(true);
                newOrder.getOrderLines()[1].setQuantity(rnd.nextInt(100) + 1);
                newOrder.getOrderLines()[1].setUseItem(true);
                newOrder.setUserId(newUser.getUserId());
                api.createOrder(newOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
   }
}
