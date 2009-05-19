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
