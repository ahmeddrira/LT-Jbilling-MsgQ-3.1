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
      
    /*
    public void testGet() {
        try {
        	String endpoint = "http://localhost/jboss-net/services/billing";
            
            Service  service = new Service();
            Call  call = (Call) service.createCall();
            call.setTargetEndpointAddress( new java.net.URL(endpoint) );
            call.setOperationName("getInvoiceWS");
            call.setUsername("testapi");
            call.setPassword("asdfasdf");
            

            // InvoiceWS            
            QName qn = new QName("http://www.sapienter.com/billing", "InvoiceWS");
            BeanSerializerFactory ser1 = new BeanSerializerFactory(
                    InvoiceWS.class, qn);
            BeanDeserializerFactory ser2 = new BeanDeserializerFactory (
                    InvoiceWS.class, qn);
            call.registerTypeMapping(InvoiceWS.class, qn, ser1, ser2); 

            // InvoiceLineDTO            
            qn = new QName("http://www.sapienter.com/billing", "InvoiceLineDTO");
            ser1 = new BeanSerializerFactory(
                    InvoiceLineDTO.class, qn);
            ser2 = new BeanDeserializerFactory (
                    InvoiceLineDTO.class, qn);
            call.registerTypeMapping(InvoiceLineDTO.class, qn, ser1, ser2); 

            // InvoiceLineDTOEx            
            qn = new QName("http://www.sapienter.com/billing", "InvoiceLineDTOEx");
            ser1 = new BeanSerializerFactory(
                    InvoiceLineDTOEx.class, qn);
            ser2 = new BeanDeserializerFactory (
                    InvoiceLineDTOEx.class, qn);
            call.registerTypeMapping(InvoiceLineDTOEx.class, qn, ser1, ser2); 

            // get

            // try getting one that doesn't belong to us
            try {
                System.out.println("Getting invalid invoice");
                call.invoke( new Object[] { new Integer(24) } );
                fail("Invoice 24 belongs to entity 2");
            } catch (Exception e) {
            }
            System.out.println("Getting invoice");
            InvoiceWS retInvoice = (InvoiceWS) call.invoke( 
                    new Object[] { new Integer(171) } );
            assertNotNull("invoice not returned", retInvoice);
            assertEquals("invoice id", retInvoice.getId(),
                    new Integer(171));
            System.out.println("Got = " + retInvoice);
            
            // latest
            
            call.setOperationName("getLatestInvoice");
            // first, from a guy that is not mine
            try {
                System.out.println("Getting latest from invalid user");
                call.invoke( new Object[] { new Integer(1736) } );
                fail("User 1736 belongs to entity 301");
            } catch (Exception e) {
            }
            System.out.println("Getting latest invoice");
            retInvoice = (InvoiceWS) call.invoke( 
                    new Object[] { new Integer(1906) } );
            assertNotNull("invoice not returned", retInvoice);
            assertEquals("invoice's user id", retInvoice.getUserId(),
                    new Integer(1906));
            System.out.println("Got = " + retInvoice);
            Integer lastInvoice = retInvoice.getId();
            
            // List of last

            call.setOperationName("getLastInvoices");
            // first, from a guy that is not mine
            try {
                System.out.println("Getting last 5 from invalid user");
                call.invoke( new Object[] { new Integer(1736), 
                        new Integer(5)} );
                fail("User 1736 belongs to entity 301");
            } catch (Exception e) {
            }
            System.out.println("Getting last 5 invoices");
            int invoices[] = (int[]) call.invoke( 
                    new Object[] { new Integer(1906), new Integer(5) } );
            assertNotNull("invoice not returned", invoices);
            
            call.setOperationName("getInvoiceWS");
            retInvoice = (InvoiceWS) call.invoke( new Object[] {
                    new Integer(invoices[0]) } );
            assertEquals("invoice's user id", new Integer(1906),
                    retInvoice.getUserId());
            System.out.println("Got = " + invoices.length + " invoices");
            for (int f = 0; f < invoices.length; f++) {
                System.out.println(" Invoice " + (f + 1) + invoices[f]);
            }
            
            // now I want just the two latest
            System.out.println("Getting last 2 invoices");
            call.setOperationName("getLastInvoices");
            invoices = (int[]) call.invoke( 
                    new Object[] { new Integer(1906), new Integer(2) } );
            assertNotNull("invoice not returned", invoices);
            call.setOperationName("getInvoiceWS");
            retInvoice = (InvoiceWS) call.invoke( new Object[] {
                    new Integer(invoices[0]) } );
            assertEquals("invoice's user id", new Integer(1906),
                    retInvoice.getUserId());
            assertEquals("invoice's has to be latest", lastInvoice,
                    retInvoice.getId());
            assertEquals("there should be only 2", 2, invoices.length);
            
            // get some by date
            System.out.println("Getting by date (empty)");
            call.setOperationName("getInvoicesByDate");
            invoices = (int[]) call.invoke( 
                    new Object[] { "2006-01-01", "2016-01-01" } );
            assertNotNull("invoice not returned", invoices);
            assertTrue("array not empty", invoices.length == 0);
            
            System.out.println("Getting by date");
            invoices = (int[]) call.invoke( 
                    new Object[] { "2000-01-01", "2006-01-01" } );
            assertNotNull("invoice not returned", invoices);
            assertFalse("array not empty", invoices.length == 0);
            System.out.println("Got array " + invoices.length + " getting " + invoices[0]);
            call.setOperationName("getInvoiceWS");
            retInvoice = (InvoiceWS) call.invoke( new Object[] {
                    new Integer(invoices[0]) } );
            assertNotNull("invoice not there", retInvoice);
            System.out.println("Got invoice " + retInvoice);
            
            System.out.println("Done!");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }
*/
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
