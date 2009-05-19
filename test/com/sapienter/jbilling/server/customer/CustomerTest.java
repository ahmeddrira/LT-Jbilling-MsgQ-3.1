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

package com.sapienter.jbilling.server.customer;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.CustomerSession;
import com.sapienter.jbilling.interfaces.CustomerSessionHome;
import com.sapienter.jbilling.server.user.contact.db.ContactDTO;

public class CustomerTest extends TestCase {

    /**
     * Constructor for CustomerTest.
     * @param arg0
     */
    public CustomerTest(String arg0) {
        super(arg0);
    }
    
    public void testPrimaryContact() {
        try {
            CustomerSessionHome customerHome =
                    (CustomerSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    CustomerSessionHome.class,
                    CustomerSessionHome.JNDI_NAME);
            CustomerSession remoteSession = customerHome.create();

            ContactDTO contact = remoteSession.getPrimaryContactDTO(
                    new Integer(5));
                    
            // null is not good
            assertNotNull(contact);
            // now verify that the data corresponds to the stuff in InitializeDB
            assertEquals("Address 1","6958 Broadway", contact.getAddress1());
            assertNull("Address 2", contact.getAddress2());
            assertEquals("City","Vancouver", contact.getCity());
            assertEquals("Deleted", new Integer(0), contact.getDeleted());
            assertEquals("Country code","CA", contact.getCountryCode());
            assertEquals("Email", "emilconde@telus.net", contact.getEmail());
            assertEquals("Fax area code", new Integer(604), contact.getFaxAreaCode());
            assertNull("Fax country code", contact.getFaxCountryCode());
            assertEquals("Fax number", "359-9998", contact.getFaxNumber());
            assertEquals("First Name", "Richar", contact.getFirstName());
            assertEquals("Initial","", contact.getInitial());
            assertEquals("Last Name", "Donald", contact.getLastName());
            assertEquals("Org name", "Smartt Net", contact.getOrganizationName());
            assertEquals("phone area code", new Integer(604), contact.getPhoneAreaCode());
            assertNull("phone country code", contact.getPhoneCountryCode());
            assertEquals("phone number","378-9853", contact.getPhoneNumber());
            assertEquals("postal code","V6R1W9", contact.getPostalCode());
            assertEquals("state/prov","BC", contact.getStateProvince());
            assertEquals("title","Billing Supervisor", contact.getTitle());
            
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
    }

}
