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
package com.sapienter.jbilling.server.user;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
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
            Random rnd = new Random();
            UserWS newUser = new UserWS();
            newUser.setUserName("webServicesUserNameCreated" + rnd.nextInt(100));
            String newUserName = newUser.getUserName();
            newUser.setPassword("asdfasdf1");
            newUser.setLanguageId(new Integer(1));
            newUser.setMainRoleId(new Integer(5));
            newUser.setParentId(new Integer(43)); // this parent exists
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
            cc.setNumber("4111111111111152");
            cc.setExpiry(Calendar.getInstance().getTime());
            newUser.setCreditCard(cc);
            
            System.out.println("Creating user ...");
            Integer newUserId = api.createUser(newUser);
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
            line.setDescription("Fist line");
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
            System.out.println("Updating user...");
            api.updateUser(retUser);
            
            // and ask for it to verify the modification
            System.out.println("Getting updated user ");
            retUser = api.getUserWS(newUserId);
            assertNotNull("Didn't get updated user", retUser);
            // The password should be the same as in the first step, no update happened.
            assertEquals("Password ", retUser.getPassword(),
            		"pgdu8KCGZJ/0xwo1RdgSe");
            assertEquals("Contact name", retUser.getContact().getFirstName(),
                    newUser.getContact().getFirstName());

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
            retUser.setPassword("newPassword2"); // reset, the one I have is crypted
            api.updateUser(retUser);
            // fetch the user
            UserWS updatedUser = api.getUserWS(newUserId);
            assertEquals("updated f name", retUser.getContact().getFirstName(),
                    updatedUser.getContact().getFirstName());
            assertEquals("updated l name", retUser.getContact().getLastName(),
                    updatedUser.getContact().getLastName());
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
            System.out.println("Updating credit card");
            String ccName = "Updated ccName";
            String ccNumber = "4012888888881881";
            Date ccExpiry = Calendar.getInstance().getTime();

            cc = new CreditCardDTO();
            cc.setName(ccName);
            cc.setNumber(ccNumber);
            cc.setExpiry(ccExpiry);
            api.updateCreditCard(newUserId,cc);

            // check updated cc details
            retUser = api.getUserWS(newUserId);
            CreditCardDTO retCc = retUser.getCreditCard();
            assertEquals("updated cc name", ccName, retCc.getName());
            assertEquals("updated cc number", ccNumber, retCc.getNumber());
            assertEquals("updated cc expiry", ccExpiry, retCc.getExpiry());

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
            assertEquals(5,users.length);
            for (int f = 0; f < users.length; f++) {
                System.out.println("Got user " + users[f]);
            }

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
            
            // only the one from the megacall is not deleted and has the custom field
            assertEquals(users.length, 1); 
            assertEquals(users[0], mcRet.getUserId());
            
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
            assertEquals(3, ret.length);
            
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
            assertEquals(2, ret.length);
            assertEquals(ret[0].getId().intValue(), 2);
            assertEquals(ret[0].getUserId().intValue(), 2);
            assertEquals(ret[0].getFromStatusId().intValue(), 2);
            assertEquals(ret[0].getToStatusId().intValue(), 1);
            
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
}
