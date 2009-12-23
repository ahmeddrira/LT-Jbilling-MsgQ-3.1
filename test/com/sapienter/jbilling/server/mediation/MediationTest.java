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

    public MediationTest() {
    }

    public MediationTest(String name) {
        super(name);
    }

    @Override
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
            for (MediationProcess process: all) {
                if (process.getConfiguration().getId() == 10) {
                    assertEquals("The process touches an order for each event",
                                 10129, process.getOrdersAffected().intValue());
                }
            }

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
                    assertEquals("Quantity of should be the combiend of all events",
                            new BigDecimal("1300.0"), order.getOrderLines()[0].getQuantityAsDecimal());
                }
                if (order.getPeriod().equals(Constants.ORDER_PERIOD_ONCE) &&
                        Util.equal(Util.truncateDate(order.getActiveSince()), Util.truncateDate(d1115))) {
                    foundSecond = true;
                    assertEquals("Quantity of second order should be 300 ",
                            new BigDecimal("300.0"), order.getOrderLines()[0].getQuantityAsDecimal());
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

            // note, only one of the mediated call items is rated, and the default price of the other is zero
            assertEquals("Total of mixed price order", new BigDecimal("1400"), total);
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

    public void testLongDistancePlanA() throws Exception {
        final Integer MEDIATION_TEST_1_USER = 10760;

        JbillingAPI api = JbillingAPIFactory.getAPI();

        Integer[] ids = api.getLastOrders(MEDIATION_TEST_1_USER, 1);
        OrderWS order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("56240 minutes", new BigDecimal("56240"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$79248 total", new BigDecimal("79248"), order.getOrderLines()[0].getAmountAsDecimal());
    }

    public void testLongDistancePlanB() throws Exception {
        final Integer MEDIATION_TEST_2_USER = 10761;

        JbillingAPI api = JbillingAPIFactory.getAPI();

        Integer[] ids = api.getLastOrders(MEDIATION_TEST_2_USER, 1);
        OrderWS order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("29250 minutes", new BigDecimal("29250"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$1462.50 total", new BigDecimal("1462.50"), order.getOrderLines()[0].getAmountAsDecimal());
    }

    // todo: Fails - lines are not correctly converted to "included" items, the original call item is not removed. 
    /*
    public void testLongDistancePlanIncludedItems() throws Exception {
        final Integer MEDIATION_TEST_3_USER = 10762;

        JbillingAPI api = JbillingAPIFactory.getAPI();

        Integer[] ids = api.getLastOrders(MEDIATION_TEST_3_USER, 1);
        OrderWS order = api.getOrder(ids[0]);

        assertEquals("2 lines added", 2, order.getOrderLines().length);

        // 650 minutes of calls @ 0.30/min
        assertEquals("650 units",  new BigDecimal("650"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$195 total", new BigDecimal("195"), order.getOrderLines()[0].getAmountAsDecimal());

        assertEquals("first 1000 units free",  new BigDecimal("1000"), order.getOrderLines()[1].getQuantityAsDecimal());
        assertEquals("$0 for free units", BigDecimal.ZERO, order.getOrderLines()[1].getAmountAsDecimal());
    }
    */

    public void testRateCard() throws Exception {
        final Integer MEDIATION_TEST_4_USER = 10770; // mediation-batch-test-04
        final Integer MEDIATION_TEST_5_USER = 10771;
        final Integer MEDIATION_TEST_6_USER = 10772;
        final Integer MEDIATION_TEST_7_USER = 10773;
        final Integer MEDIATION_TEST_8_USER = 10774;
        final Integer MEDIATION_TEST_9_USER = 10775;
        final Integer MEDIATION_TEST_10_USER = 10776;

        JbillingAPI api = JbillingAPIFactory.getAPI();

        // 150 calls to 1876999* @ 0.470/min - only 150 of the calls for this user are rated!
        Integer[] ids = api.getLastOrders(MEDIATION_TEST_4_USER, 1);
        OrderWS order = api.getOrder(ids[0]);
        
        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("67800 minutes", new BigDecimal("67800"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$5322.75 total", new BigDecimal("5322.75"), order.getOrderLines()[0].getAmountAsDecimal());

        // 150 calls to 1809986* @ 0.260/min - only 150 of the calls for this user are rated!
        ids = api.getLastOrders(MEDIATION_TEST_5_USER, 1);
        order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("67800 minutes", new BigDecimal("67800"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$2944.50 total", new BigDecimal("2944.50"), order.getOrderLines()[0].getAmountAsDecimal());

        // 150 calls to 1784593* @ 0.490/min - only 150 of the calls for this user are rated!
        ids = api.getLastOrders(MEDIATION_TEST_6_USER, 1);
        order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("112950 minutes", new BigDecimal("112950"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$5549.25 total", new BigDecimal("5549.25"), order.getOrderLines()[0].getAmountAsDecimal());


        // 300 calls to 502979* @ 0.250/min - two batches of events totalling 300 rated calls.
        ids = api.getLastOrders(MEDIATION_TEST_7_USER, 1);
        order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("154125 minutes", new BigDecimal("154125"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$13125.00 total", new BigDecimal("13125"), order.getOrderLines()[0].getAmountAsDecimal());        


        // 300 calls to 52* @ 0.180/min - two batches or calls, one being processed at the beginning and the other
        //                                at the end of the mediation run, totalling 300 rated calls
        ids = api.getLastOrders(MEDIATION_TEST_8_USER, 1);
        order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("154125 minutes", new BigDecimal("154125"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$9450.00 total", new BigDecimal("9450"), order.getOrderLines()[0].getAmountAsDecimal());

        // 300 calls to 40* @ 0.130/min - two batches of events totalling 300 rated calls.
        ids = api.getLastOrders(MEDIATION_TEST_9_USER, 1);
        order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("154125 minutes", new BigDecimal("154125"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$6825.00 total", new BigDecimal("6825.00"), order.getOrderLines()[0].getAmountAsDecimal());

        // 900 calls to various numbers, with corresponding ratings.
        ids = api.getLastOrders(MEDIATION_TEST_10_USER, 1);
        order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("79275  minutes", new BigDecimal("79275"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$20498.25 total", new BigDecimal("20498.25"), order.getOrderLines()[0].getAmountAsDecimal());
    }

    public void testDuplicateEvents() throws Exception {
        final Integer MEDIATION_TEST_11_USER = 10777;

        JbillingAPI api = JbillingAPIFactory.getAPI();

        // 300 calls to 40* @ 0.130/min - 150 of those calls have a duplicate account code and will be ignored
        Integer[] ids = api.getLastOrders(MEDIATION_TEST_11_USER, 1);
        OrderWS order = api.getOrder(ids[0]);

        assertEquals("1 line added", 1, order.getOrderLines().length);
        assertEquals("112950 minutes", new BigDecimal("112950"), order.getOrderLines()[0].getQuantityAsDecimal());
        assertEquals("$1472.25 total", new BigDecimal("1472.25"), order.getOrderLines()[0].getAmountAsDecimal());
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
