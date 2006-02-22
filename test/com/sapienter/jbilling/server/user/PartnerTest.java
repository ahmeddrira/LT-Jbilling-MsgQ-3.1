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
 * Created on Apr 5, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.user;

import java.util.Calendar;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @author Emil
 */
public class PartnerTest extends TestCase {

    public void testPartnerGeneral() {
        try {
            PartnerDTOEx partner = null;
            UserSessionHome userHome =
                    (UserSessionHome) JNDILookup.getFactory(true).lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            UserSession lSession = userHome.create();
            
            Calendar cal = Calendar.getInstance();
            cal.clear();
            
            /* 
             *  first run
             */
            cal.set(2004, Calendar.MARCH, 15);
            lSession.processPayouts(cal.getTime());
            
            // partner 1
            partner = lSession.getPartnerDTO(new Integer(1));
            // no payouts
            assertEquals("No new payouts for 1", 1, partner.getPayouts().length);
            cal.set(2004, Calendar.APRIL, 1);
            assertEquals("1:next payout still apr 1", partner.getNextPayoutDate(), 
                    cal.getTime());
            
            // partner 2
            partner = lSession.getPartnerDTO(new Integer(2));
            // no payouts, this guy doens't get paid in the batch
            assertEquals("No new payouts for 2", 0, partner.getPayouts().length);
            // still she should get paid
            assertEquals("2: due payout ", 5F,
                    partner.getDuePayout().floatValue(), 0F);
            cal.set(2004, Calendar.MARCH, 1);
            assertEquals("2:next payout ", partner.getNextPayoutDate(), 
                    cal.getTime());

            // partner 3
            partner = lSession.getPartnerDTO(new Integer(3));
            // a new payout
            assertEquals("3: two payouts", 2, partner.getPayouts().length);
            assertEquals("3: payout total", 11.320F, 
                    partner.getPayouts()[0].getPaymentsAmount().floatValue(), 
                    0.001F);
            assertEquals("3: sucessful payment in new payout", Constants.RESULT_OK,
                    partner.getPayouts()[0].getPayment().getResultId());
            assertEquals("3 due payout zero", new Float(0), partner.getDuePayout());
            cal.set(2004, Calendar.MARCH, 25);
            assertEquals("3:next payout 10 days later ", partner.getNextPayoutDate(), 
                    cal.getTime());
            
            /*
             * second run
             */
            cal.set(2004, Calendar.APRIL, 1);
            lSession.processPayouts(cal.getTime());
            
            // partner 1
            partner = lSession.getPartnerDTO(new Integer(1));
            // new payout
            assertEquals("1:New payout", 2, partner.getPayouts().length);
            assertEquals("1: payout total", 6.666F, 
                    partner.getPayouts()[0].getPayment().getAmount().floatValue(), 
                    0.001F);
            assertEquals("1: payout payments total", 16.666F, 
                    partner.getPayouts()[0].getPaymentsAmount().floatValue(), 
                    0.001F);
            assertEquals("1: payout refunds total", 10F, 
                    partner.getPayouts()[0].getRefundsAmount().floatValue(), 
                    0.001F);
            assertEquals("1: sucessful payment in new payout", Constants.RESULT_OK,
                    partner.getPayouts()[0].getPayment().getResultId());
            assertEquals("1 due payout zero", new Float(0), partner.getDuePayout());
            
            
            // partner 2
            partner = lSession.getPartnerDTO(new Integer(2));
            // no payouts, this guy doens't get paid in the batch
            assertEquals("No new payouts for 2", 0, partner.getPayouts().length);
            // still she should get paid
            assertEquals("2: due payout ", 5F,
                    partner.getDuePayout().floatValue(), 0F);
            cal.set(2004, Calendar.MARCH, 1);
            assertEquals("2:next payout ", partner.getNextPayoutDate(), 
                    cal.getTime());

            // partner 3
            partner = lSession.getPartnerDTO(new Integer(3));
            // a new payout
            assertEquals("3: three payouts", 3, partner.getPayouts().length);
            assertEquals("3: payout total", 0F, 
                    partner.getPayouts()[0].getPayment().getAmount().floatValue(), 
                    0.0F);
            assertEquals("3 due payout zero", new Float(0), partner.getDuePayout());
            cal.set(2004, Calendar.MARCH, 25);
            cal.add(Calendar.DATE, 10);
            assertEquals("3 (2):next payout ", partner.getNextPayoutDate(), 
                    cal.getTime());
          
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception caught:" + e);
        }
    }

}
