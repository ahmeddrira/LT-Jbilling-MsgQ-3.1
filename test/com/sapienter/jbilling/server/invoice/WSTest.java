/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.invoice;

import junit.framework.TestCase;

import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

/**
 * @author Emil
 */
public class WSTest extends TestCase {
      
    public void testGet() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();

            // get
            // try getting one that doesn't belong to us
            try {
                System.out.println("Getting invalid invoice");
                api.getInvoiceWS(75);
                fail("Invoice 75 belongs to entity 2");
            } catch (Exception e) {
            }
            
            System.out.println("Getting invoice");
            InvoiceWS retInvoice = api.getInvoiceWS(15);
            assertNotNull("invoice not returned", retInvoice);
            assertEquals("invoice id", retInvoice.getId(),
                    new Integer(15));
            System.out.println("Got = " + retInvoice);
            
            // latest
            // first, from a guy that is not mine
            try {
            	api.getLatestInvoice(13);
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }
            System.out.println("Getting latest invoice");
            retInvoice = api.getLatestInvoice(2);
            assertNotNull("invoice not returned", retInvoice);
            assertEquals("invoice's user id", retInvoice.getUserId(),
                    new Integer(2));
            System.out.println("Got = " + retInvoice);
            Integer lastInvoice = retInvoice.getId();
            
            // List of last
            // first, from a guy that is not mine
            try {
            	api.getLastInvoices(13, 5);
                fail("User 13 belongs to entity 2");
            } catch (Exception e) {
            }
            System.out.println("Getting last 5 invoices");
            Integer invoices[] = api.getLastInvoices(2, 5);
            assertNotNull("invoice not returned", invoices);
            
            retInvoice = api.getInvoiceWS(invoices[0]);
            assertEquals("invoice's user id", new Integer(2),
                    retInvoice.getUserId());
            System.out.println("Got = " + invoices.length + " invoices");
            for (int f = 0; f < invoices.length; f++) {
                System.out.println(" Invoice " + (f + 1) + invoices[f]);
            }
            
            // now I want just the two latest
            System.out.println("Getting last 2 invoices");
            invoices = api.getLastInvoices(2, 2);
            assertNotNull("invoice not returned", invoices);
            retInvoice = api.getInvoiceWS(invoices[0]);
            assertEquals("invoice's user id", new Integer(2),
                    retInvoice.getUserId());
            assertEquals("invoice's has to be latest", lastInvoice,
                    retInvoice.getId());
            assertEquals("there should be only 2", 2, invoices.length);
            
            // get some by date
            System.out.println("Getting by date (empty)");
            Integer invoices2[] = api.getInvoicesByDate("2000-01-01", "2005-01-01");
            assertNotNull("invoice not returned", invoices2);
            assertTrue("array not empty", invoices2.length == 0);
            
            System.out.println("Getting by date");
            invoices2 = api.getInvoicesByDate("2006-01-01", "2007-01-01");
            assertNotNull("invoice not returned", invoices2);
            assertFalse("array not empty", invoices2.length == 0);
            System.out.println("Got array " + invoices2.length + " getting " + invoices2[0]);
            retInvoice = api.getInvoiceWS(invoices2[0]);
            assertNotNull("invoice not there", retInvoice);
            System.out.println("Got invoice " + retInvoice);
            
            System.out.println("Done!");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

    public void testDelete() {
        try {
            JbillingAPI api = JbillingAPIFactory.getAPI();
            Integer invoiceId = new Integer(1);
            assertNotNull(api.getInvoiceWS(invoiceId));
            api.deleteInvoice(invoiceId);
            try {
                api.getInvoiceWS(invoiceId);
                fail("Invoice should not have been deleted");
            } catch(Exception e) {
                //ok
            }
            
            // try to delete an invoice that is not mine
            try {
                api.deleteInvoice(new Integer(75));
                fail("Not my invoice. It should not have been deleted");
            } catch(Exception e) {
                //ok
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
        
    }

}
