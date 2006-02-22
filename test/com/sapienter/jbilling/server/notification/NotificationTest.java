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

package com.sapienter.jbilling.server.notification;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.NotificationSession;
import com.sapienter.jbilling.interfaces.NotificationSessionHome;

public class NotificationTest extends TestCase {

    /**
     * Constructor for NotificationTest.
     * @param arg0
     */
    public NotificationTest(String arg0) {
        super(arg0);
    }

    public void testNotify() {

        try {
            NotificationSessionHome customerHome =
                    (NotificationSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    NotificationSessionHome.class,
                    NotificationSessionHome.JNDI_NAME);
            NotificationSession remoteSession = customerHome.create();
            
            MessageDTO message = new MessageDTO();
            message.setTypeId(MessageDTO.TYPE_INVOICE_EMAIL);
            message.addParameter("total", "11.5");
            message.addParameter("due date","10/20/2003");
            
            MessageSection section = new MessageSection(new Integer(1),
                    "Hello World");
                    
            message.addSection(section);
            //seciton = new MessageSection(new Integer
            remoteSession.notify(new Integer(5), message);
            // yikes, can't really assert anything ... :(
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
    }

    public void testSapienterEmail() {
    	Integer entityId = new Integer(1);
    	try {
    		System.out.println("Attempting sending . . . ");
	    	NotificationBL.sendSapienterEmail(entityId, "invoice_batch",
	    			Util.getSystemProperty("base_dir") + "invoices/" + entityId + 
					"-" + new Integer(1) + "-batch.pdf");
	    	System.out.println("DONE !");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
   			
    }
}
