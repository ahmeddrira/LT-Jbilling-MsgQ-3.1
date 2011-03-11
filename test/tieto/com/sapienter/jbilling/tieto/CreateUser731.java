/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.tieto;

import java.util.Calendar;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

/**
 * Unit test for the Tieto - TAG requirement:
 * #731 - "Request for createUser example".
 */
public class CreateUser731 extends TestCase {

    public CreateUser731() {
        super();
    }

    public CreateUser731(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Creates parent and child users.
     */
    public void testCreateUser() {
        
        System.out.println("#731 - \"Request for createUser example\"");

        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();

            /* Create the parent customer with credit card info. */

            UserWS parentUser = generateUser("test-parent-user");
            // user is a parent
            parentUser.setIsParent(true);
            // add a credit card
            parentUser.setCreditCard(generateCreditCard("4111111111111152"));

            // make the call and save the returned user id
            System.out.println("Creating parent user ...");
            parentUser.setUserId(api.createUser(parentUser));

            // check the user was created
            assertNotNull("The user was not created", parentUser.getUserId());
            System.out.println("User was created. Id: " + 
                    parentUser.getUserId());

            /* Create the child customer. */

            UserWS childUser = generateUser("test-child-user");
            childUser.setIsParent(new Boolean(false));
            childUser.setParentId(parentUser.getUserId());
            // parent receives invoices
            childUser.setInvoiceChild(Boolean.FALSE); 

            // make the call and save the returned user id
            System.out.println("Creating child user ...");
            childUser.setUserId(api.createUser(childUser));

            // check the user was created
            assertNotNull("The user was not created", childUser.getUserId());
            System.out.println("User was created. Id: " + 
                    childUser.getUserId());


        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    /**
     * Sets up and returns new user object with given username.
     */
    public static UserWS generateUser(String username)
            throws JbillingAPIException {
        UserWS newUser = new UserWS();
        newUser.setUserName(username);
        newUser.setPassword("123qwe");
        newUser.setLanguageId(1); // Language "1" = "English"
        newUser.setMainRoleId(5); // Role "5" = "Customer"
        newUser.setStatusId(1);  // Status "1" = "Active"

        // add contact info
        ContactWS contact = new ContactWS();
        contact.setOrganizationName("Test Business Name");
        contact.setEmail("joe.bloggs@test.com");
        contact.setAddress1("1 Main Rd.");
        contact.setCity("BigCity");
        contact.setStateProvince("SomeState");
        contact.setPostalCode("2345");
        contact.setPhoneNumber("123456789");
        newUser.setContact(contact);

        return newUser;
    } 

    /**
     * Sets up and returns new credit card object.
     */
    public static CreditCardDTO generateCreditCard(String number) 
            throws JbillingAPIException {
        CreditCardDTO cc = new CreditCardDTO();
        cc.setName("Joe Bloggs");
        cc.setNumber(number);

        // valid credit card must have a future expiry date to be valid for payment processing
        Calendar expiry = Calendar.getInstance();
        expiry.set(Calendar.YEAR, expiry.get(Calendar.YEAR) + 1);
        cc.setExpiry(expiry.getTime());        

        return cc;
    }
}
