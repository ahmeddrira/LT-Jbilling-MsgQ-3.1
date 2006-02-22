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

package com.sapienter.jbilling.server.customer;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.CustomerSession;
import com.sapienter.jbilling.interfaces.CustomerSessionHome;
import com.sapienter.jbilling.server.entity.ContactDTO;

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
