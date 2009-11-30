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

package com.sapienter.jbilling.server.mediation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.mediation.db.MediationProcess;
import com.sapienter.jbilling.server.mediation.db.MediationRecordLineDTO;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.RemoteContext;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

public class MediationTest extends TestCase {

    private IMediationSessionBean remoteMediation = null;

    protected void setUp() throws Exception {
        super.setUp();

        remoteMediation = (IMediationSessionBean) RemoteContext.getBean(RemoteContext.Name.MEDIATION_REMOTE_SESSION);
    }

    public void testTrigger() {
        try {
            remoteMediation.trigger();
            List<MediationProcess> all = remoteMediation.getAll(1);
            assertNotNull("process list can't be null", all);
            assertEquals("There should be two processes after running the mediation process", 2, all.size());
            assertEquals("The process has to touch seven orders", new Integer(7), all.get(0).getOrdersAffected());

            List allCfg = remoteMediation.getAllConfigurations(1);
            assertNotNull("config list can't be null", allCfg);
            assertEquals("There should be two configurations present", 2, allCfg.size());

            System.out.println("Validating one-time orders...");
            JbillingAPI api = JbillingAPIFactory.getAPI();

            boolean foundFirst = false;
            boolean foundSecond = false;

            GregorianCalendar cal = new GregorianCalendar();
            cal.set(2007, GregorianCalendar.OCTOBER, 15);
            Date d1015 = cal.getTime();

            cal.set(2007, GregorianCalendar.NOVEMBER, 15);
            Date d1115 = cal.getTime();

            for (Integer orderId : api.getLastOrders(2, 100)) {
                OrderWS order = api.getOrder(orderId);
                //System.out.println("testing order " + order + " f1 " + foundFirst + 
                //        " f2 " + foundSecond);
                if (order.getActiveSince() != null) {
                    System.out.println(" c1 " +
                            Util.truncateDate(order.getActiveSince()).equals(d1015) +
                            " c2 " + Util.truncateDate(order.getActiveSince()).equals(d1115));
                }
                if (order.getPeriod().equals(Constants.ORDER_PERIOD_ONCE) &&
                        Util.equal(Util.truncateDate(order.getActiveSince()), Util.truncateDate(d1015))) {
                    foundFirst = true;
                    assertEquals("Quantity of should be the combiend of all events", new BigDecimal("2600.0"), order.getOrderLines()[0].getQuantityAsDecimal());
                }
                if (order.getPeriod().equals(Constants.ORDER_PERIOD_ONCE) &&
                        Util.equal(Util.truncateDate(order.getActiveSince()), Util.truncateDate(d1115))) {
                    foundSecond = true;
                }
            }

            assertTrue("The one time order for 10/15 is missing", foundFirst);
            assertTrue("The one time order for 11/15 is missing", foundSecond);

            // verify that the two events with different prices add up well
            OrderWS order = api.getLatestOrder(1055);
            BigDecimal total = BigDecimal.ZERO;
            for (OrderLineWS line : order.getOrderLines()) {
                total = total.add(line.getAmountAsDecimal());
            }
            assertEquals("Total of mixed price order", new BigDecimal("12800"), total);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        }
    }
    
    // test that the last 2 orders for gandalf have all the CDRs
    public void testOrderLineEvents() {
        try {
            
            JbillingAPI api = JbillingAPIFactory.getAPI();
            Integer ids[] = api.getLastOrders(2, 2); // last two orders for user 2
            for (Integer id: ids) {
                OrderWS order = api.getOrder(id);
                List<MediationRecordLineDTO> lines = remoteMediation.getEventsForOrder(order.getId());

                BigDecimal total = BigDecimal.ZERO;
                BigDecimal quantity = BigDecimal.ZERO;
                for (MediationRecordLineDTO line: lines) {
                    total = total.add(line.getAmount());
                    quantity = quantity.add(line.getQuantity());
                }
                
                assertEquals("Total of order " + id, BigDecimal.ZERO, total.subtract(order.getOrderLines()[0].getAmountAsDecimal()));
                assertEquals("Qty of order " + id, BigDecimal.ZERO, quantity.subtract(order.getOrderLines()[0].getQuantityAsDecimal()));
                System.out.println("Order adds up: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        }
    }

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, BigDecimal expected, BigDecimal actual) {
        assertEquals(message,
                     (Object) (expected == null ? null : expected.setScale(2, RoundingMode.HALF_UP)),
                     (Object) (actual == null ? null : actual.setScale(2, RoundingMode.HALF_UP)));
    }       
}
