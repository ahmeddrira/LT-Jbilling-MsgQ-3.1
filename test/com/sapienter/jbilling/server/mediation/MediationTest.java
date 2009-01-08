package com.sapienter.jbilling.server.mediation;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.mediation.db.MediationProcess;
import com.sapienter.jbilling.server.mediation.db.MediationRecordLineDTO;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

public class MediationTest extends TestCase {

    private MediationSession remoteMediation = null;

    protected void setUp() throws Exception {
        super.setUp();

        MediationSessionHome mediationHome =
                (MediationSessionHome) JNDILookup.getFactory(true).lookUpHome(
                MediationSessionHome.class,
                MediationSessionHome.JNDI_NAME);
        remoteMediation = mediationHome.create();

    }

    public void testTrigger() {
        try {
            remoteMediation.trigger();
            List<MediationProcess> all = remoteMediation.getAll(1);
            assertNotNull("process list can't be null", all);
            assertEquals("There should be one process after running the mediation process", 1, all.size());
            assertEquals("The process has to touch seven orders", new Integer(7),
                    all.get(0).getOrdersAffected());

            List allCfg = remoteMediation.getAllConfigurations(1);
            assertNotNull("config list can't be null", allCfg);
            assertEquals("There should be one configuration present", 1, allCfg.size());

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
                    assertEquals("Quantity of should be the combiend of all events", 1300.0, (double) order.getOrderLines()[0].getQuantity());
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
            int total = 0;
            for (OrderLineWS line : order.getOrderLines()) {
                total += line.getAmount();
            }
            assertEquals("Total of mixed price order", 6400, total);
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
                Double total = 0.0;
                Double quantity = 0.0;
                for (MediationRecordLineDTO line: lines) {
                    total += line.getAmount().doubleValue();
                    quantity += line.getQuantity().doubleValue();
                }
                assertTrue("Total of order " + id, total - order.getOrderLines()[0].getAmount() == 0.0);
                assertTrue("Qty of order " + id, quantity - order.getOrderLines()[0].getQuantity() == 0.0);
                System.out.println("Order adds up: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        }
    }

}
