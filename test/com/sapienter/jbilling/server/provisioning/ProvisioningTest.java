package com.sapienter.jbilling.server.provisioning;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import junit.framework.TestCase;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;

public class ProvisioningTest extends TestCase {
    private static final int           ORDER_LINES_COUNT  = 6;
    private static final Integer       USER_ID            = 1000;
    private static Integer[]           itemIds            = {
        1, 2, 3, 24, 240, 14
    };
    private static Integer[]           provisioningStatus = new Integer[6];
    private ProvisioningProcessSession remoteProvisioning = null;
    JbillingAPI                        api;

    /**
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        api = JbillingAPIFactory.getAPI();

        ProvisioningProcessSessionHome provisioningHome = (ProvisioningProcessSessionHome) JNDILookup.getFactory(
                                                              true).lookUpHome(
                                                              ProvisioningProcessSessionHome.class,
                                                              ProvisioningProcessSessionHome.JNDI_NAME);

        remoteProvisioning = provisioningHome.create();
    }

    private void pause(long t) {
        
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testNewQuantityEvent() {
        try {
            provisioningStatus[0] = Constants.PROVISIONING_STATUS_ACTIVE;
            provisioningStatus[1] = Constants.PROVISIONING_STATUS_INACTIVE;
            provisioningStatus[2] = null;
            provisioningStatus[3] = Constants.PROVISIONING_STATUS_PENDING_ACTIVE;
            provisioningStatus[4] = Constants.PROVISIONING_STATUS_PENDING_INACTIVE;
            provisioningStatus[5] = null;

            OrderWS newOrder = createMockOrder(USER_ID.intValue(), ORDER_LINES_COUNT, 77f);

            newOrder.setActiveSince(null);

            // create order through api
            Integer ret = api.createOrder(newOrder);

            System.out.println("Created order." + ret);
            assertNotNull("The order was not created", ret);
            System.out.println("running provisioning batch process..");
            pause(4000);
            remoteProvisioning.trigger();
            pause(4000);
            System.out.println("Getting back order " + ret);

            OrderWS retOrder = api.getOrder(ret);

            System.out.println("got order: " + retOrder);

            OrderLineWS[] retLine = retOrder.getOrderLines();

            for (int i = 0; i < retLine.length; i++) {
                if (i == 0) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_ACTIVE);
                }

                if (i == 1) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_ACTIVE);
                }

                if (i == 2) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(), null);
                }

                if (i == 3) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_PENDING_ACTIVE);
                }

                if (i == 4) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_PENDING_INACTIVE);
                }

                if (i == 5) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(), null);
                }
            }
        } catch (JbillingAPIException e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        } catch (SessionInternalError e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        }
    }

    public void testSubscriptionActiveEvent() {
        try {
            provisioningStatus[0] = Constants.PROVISIONING_STATUS_ACTIVE;
            provisioningStatus[1] = Constants.PROVISIONING_STATUS_INACTIVE;
            provisioningStatus[2] = null;
            provisioningStatus[3] = Constants.PROVISIONING_STATUS_PENDING_ACTIVE;
            provisioningStatus[4] = Constants.PROVISIONING_STATUS_PENDING_INACTIVE;
            provisioningStatus[5] = null;

            OrderWS newOrder = createMockOrder(USER_ID.intValue(), ORDER_LINES_COUNT, 77f);

            // newOrder.setActiveSince(weeksFromToday(1));
            Calendar cal = Calendar.getInstance();

            cal.clear();
            cal.set(2008, 9, 29, 0, 0, 0);
            newOrder.setActiveSince(cal.getTime());

            // create order through api
            Integer ret = api.createOrder(newOrder);

            System.out.println("Created order." + ret);
            assertNotNull("The order was not created", ret);
            System.out.println("running provisioning batch process..");
            pause(4000);
            remoteProvisioning.trigger();
            pause(4000);
            System.out.println("Getting back order " + ret);

            OrderWS retOrder = api.getOrder(ret);

            System.out.println("got order: " + retOrder);

            OrderLineWS[] retLine = retOrder.getOrderLines();

            for (int i = 0; i < retLine.length; i++) {
                if (i == 0) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_ACTIVE);
                }

                if (i == 1) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_ACTIVE);
                }

                if (i == 2) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(), null);
                }

                if (i == 3) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_PENDING_ACTIVE);
                }

                if (i == 4) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_PENDING_INACTIVE);
                }

                if (i == 5) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(), null);
                }
            }
        } catch (JbillingAPIException e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        } catch (SessionInternalError e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        }
    }

    public void testSubscriptionInActiveEvent() {
        try {
            provisioningStatus[0] = Constants.PROVISIONING_STATUS_ACTIVE;
            provisioningStatus[1] = Constants.PROVISIONING_STATUS_ACTIVE;
            provisioningStatus[2] = null;
            provisioningStatus[3] = Constants.PROVISIONING_STATUS_PENDING_ACTIVE;
            provisioningStatus[4] = Constants.PROVISIONING_STATUS_PENDING_INACTIVE;
            provisioningStatus[5] = null;

            OrderWS newOrder = createMockOrder(USER_ID.intValue(), ORDER_LINES_COUNT, 77f);

            // newOrder.setActiveSince(weeksFromToday(1));
            Calendar cal = Calendar.getInstance();

            cal.clear();
            cal.set(2008, 9, 29, 0, 0, 0);
            newOrder.setActiveUntil(cal.getTime());

            // create order through api
            Integer ret = api.createOrder(newOrder);

            System.out.println("Created order." + ret);
            assertNotNull("The order was not created", ret);
            System.out.println("running provisioning batch process..");
            pause(4000);
            remoteProvisioning.trigger();
            pause(4000);
            System.out.println("Getting back order " + ret);

            OrderWS retOrder = api.getOrder(ret);

            System.out.println("got order: " + retOrder);

            OrderLineWS[] retLine = retOrder.getOrderLines();

            for (int i = 0; i < retLine.length; i++) {
                if (i == 0) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_ACTIVE);
                }

                if (i == 1) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_INACTIVE);
                }

                if (i == 2) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(), null);
                }

                if (i == 3) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_PENDING_ACTIVE);
                }

                if (i == 4) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(),
                                 Constants.PROVISIONING_STATUS_PENDING_INACTIVE);
                }

                if (i == 5) {
                    assertEquals("order line " + (i + 1) + "", retLine[i].getProvisioningStatus(), null);
                }
            }
        } catch (JbillingAPIException e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        } catch (SessionInternalError e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("Exception!" + e.getMessage());
        }
    }

    public static OrderWS createMockOrder(int userId, int orderLinesCount, float linePrice) {
        OrderWS order = new OrderWS();

        order.setUserId(userId);
        order.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
        order.setPeriod(1);    // once
        order.setCurrencyId(1);

        ArrayList<OrderLineWS> lines = new ArrayList<OrderLineWS>(orderLinesCount);

        for (int i = 0; i < orderLinesCount; i++) {
            OrderLineWS nextLine = new OrderLineWS();

            nextLine.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
            nextLine.setDescription("Order line: " + i);
            nextLine.setItemId(itemIds[i]);
            nextLine.setQuantity(1);
            nextLine.setPrice(linePrice);
            nextLine.setAmount(nextLine.getQuantity().floatValue() * linePrice);
            nextLine.setProvisioningStatus(provisioningStatus[i]);
            lines.add(nextLine);
        }

        order.setOrderLines(lines.toArray(new OrderLineWS[lines.size()]));

        return order;
    }

    /*private Date weeksFromToday(int weekNumber) {
        Calendar calendar = new GregorianCalendar();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.WEEK_OF_YEAR, weekNumber);

        return calendar.getTime();
    }*/
}
