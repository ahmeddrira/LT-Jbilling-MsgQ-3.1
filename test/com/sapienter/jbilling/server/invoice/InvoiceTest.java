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
 * Created on Oct 1, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sapienter.jbilling.server.invoice;

import java.util.Date;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.InvoiceSession;
import com.sapienter.jbilling.interfaces.InvoiceSessionHome;

/**
 * @author Emil
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InvoiceTest extends TestCase {
    public void testReminders() {
        try {
            InvoiceSessionHome invoiceHome =
                (InvoiceSessionHome) JNDILookup.getFactory(true).lookUpHome(
                InvoiceSessionHome.class,
                InvoiceSessionHome.JNDI_NAME);
            InvoiceSession invoiceSession = invoiceHome.create();
            invoiceSession.sendReminders(new Date(104, 9, 2));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
    }
    
    public void testPenalty() {
        try {
            InvoiceSessionHome invoiceHome =
                (InvoiceSessionHome) JNDILookup.getFactory(true).lookUpHome(
                InvoiceSessionHome.class,
                InvoiceSessionHome.JNDI_NAME);
            InvoiceSession invoiceSession = invoiceHome.create();
            invoiceSession.processOverdue(new Date(105, 0, 6));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
        
    }
}
