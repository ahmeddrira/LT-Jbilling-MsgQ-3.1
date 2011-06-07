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

package com.sapienter.jbilling.server.integration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.Constants;
import junit.framework.TestCase;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;
import com.sapienter.jbilling.server.entity.CreditCardDTO;

public class APITest extends TestCase {

    JbillingAPI api;

    /**
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        api = JbillingAPIFactory.getAPI();
    }


    public void testValidateCreditCard() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        // level 1 validations
        // validate that credit card has name
        CreditCardDTO creditCard = new CreditCardDTO();
        creditCard.setName("");
        creditCard.setNumber("4111111111111152");
        assertFalse("Missing credit card name.", api.validateCreditCard(creditCard, null, 1));

        creditCard.setName(null);
        assertFalse("Null credit card name.", api.validateCreditCard(creditCard, null, 1));

        // validate that credit card has number
        creditCard.setName("Frodo Baggins");
        creditCard.setNumber("");
        assertFalse("Missing credit card number.", api.validateCreditCard(creditCard, null, 1));

        creditCard.setNumber(null);
        assertFalse("Null credit card number.", api.validateCreditCard(creditCard, null, 1));

        // validate credit card number (Luhn check)
        creditCard.setName("Frodo Baggins");
        creditCard.setNumber("4123456789000000");
        assertFalse("Credit card number failed Luhn check.", api.validateCreditCard(creditCard, null, 1));

        // positive test
        creditCard.setName("Frodo Baggins");
        creditCard.setNumber("4111111111111152");
        assertTrue("Credit card is valid.", api.validateCreditCard(creditCard, null, 1));


        // level 2 validations
        // validate that contact has address
        ContactWS contact = new ContactWS();
        contact.setAddress1("");
        assertFalse("Missing contact address.", api.validateCreditCard(creditCard, contact, 2));

        contact.setAddress1(null);
        assertFalse("Null contact address.", api.validateCreditCard(creditCard, contact, 2));

        // validate that credit card has security code
        contact.setAddress1("123 Fake Street");
        creditCard.setSecurityCode("");
        assertFalse("Missing credit card security code.", api.validateCreditCard(creditCard, contact, 2));

        creditCard.setSecurityCode(null);
        assertFalse("Null credit card security code.", api.validateCreditCard(creditCard, contact, 2));

        creditCard.setSecurityCode("abc");
        assertFalse("Credit card security code not a number.", api.validateCreditCard(creditCard, contact, 2));

        // positive test
        contact.setAddress1("123 Fake Street");
        creditCard.setSecurityCode("456");
        assertTrue("Credit card and contact is valid.", api.validateCreditCard(creditCard, contact, 2));

        // level 3
        // pre-authorization test
        assertTrue("Credit card passes pre-authorization", api.validateCreditCard(creditCard, contact, 3));
    }
}
