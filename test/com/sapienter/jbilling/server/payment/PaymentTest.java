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

package com.sapienter.jbilling.server.payment;

import java.util.Calendar;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PaymentSession;
import com.sapienter.jbilling.interfaces.PaymentSessionHome;

public class PaymentTest extends TestCase {

    /**
     * Constructor for PaymentTest.
     * @param arg0
     */
    public PaymentTest(String arg0) {
        super(arg0);
    }

    public void testPayment() {
        try {
            PaymentSessionHome customerHome =
                    (PaymentSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    PaymentSessionHome.class,
                    PaymentSessionHome.JNDI_NAME);
            PaymentSession remoteSession = customerHome.create();
            
            PaymentDTOEx payment = new PaymentDTOEx(null, new Float(13.3),
                    Calendar.getInstance().getTime(), 
                    Calendar.getInstance().getTime(),
                    new Integer(1), new Integer(0), new Integer(1),
                    new Integer(1), new Integer(0), new Integer(1));
            payment.setUserId(new Integer(7));
            Integer paymentId = remoteSession.applyPayment(payment, new Integer(3));
            
            // now ask for this payment
            payment = remoteSession.getPayment(paymentId, new Integer(1));
            assertEquals("amount", payment.getAmount(), new Float(13.3));
            assertEquals("method", payment.getMethodId(), new Integer(1));
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("Exception : " + e.getMessage());
        }
        
    }

}
