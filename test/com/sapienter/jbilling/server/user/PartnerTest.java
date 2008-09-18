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
            Partner partner = null;
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
