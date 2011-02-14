/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.pricing;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.PlanItemWS;
import com.sapienter.jbilling.server.item.PlanWS;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.pricing.db.PriceModelStrategy;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Brian Cowdery
 * @since 06-08-2010
 */
public class WSTest extends TestCase {

    /*
        Testing plan "Crazy Brian's Discount Plan"
        Prices Item 2602 (Lemonade) at $0.05
     */

    private static final Integer PLAN_ID = 1;
    private static final Integer PLAN_ITEM_ID = 3000;               // crazy brian's discount plan
    private static final Integer PLAN_AFFECTED_ITEM_ID = 2602;      // lemonade

    private static final Integer NON_PLAN_ITEM_ID = 2800;           // long distance call


    public WSTest() {
    }

    public WSTest(String name) {
        super(name);
    }


    /**
     * Tests subscription / un-subscription to a plan by creating and deleting an order
     * containing the plan item.
     *
     * @throws Exception possible api exception
     */
    public void testCreateDeleteOrder() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        UserWS user = new UserWS();
        user.setUserName("plan-test-01-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Plan Test");
        contact.setLastName("Create Order (subscribe)");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());

        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);
        order.setPeriod(2);
        order.setCurrencyId(1);
        order.setActiveSince(new Date());

        // subscribe to plan item
        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(PLAN_ITEM_ID);
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });

        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());


        // verify customer price creation with API calls.
        assertTrue("Customer should be subscribed to plan.", api.isCustomerSubscribed(PLAN_ID, user.getUserId()));

        PlanItemWS price = api.getCustomerPrice(user.getUserId(), PLAN_AFFECTED_ITEM_ID);
        assertEquals("Affected item should be discounted.", new BigDecimal("0.50"), price.getModel().getRateAsDecimal());

        // delete order that subscribes the user to the plan
        api.deleteOrder(order.getId());

        // verify customer price removal with API calls.
        assertFalse("Customer no longer subscribed to plan.", api.isCustomerSubscribed(PLAN_ID, user.getUserId()));

        price = api.getCustomerPrice(user.getUserId(), PLAN_AFFECTED_ITEM_ID);
        assertNull("Customer no longer subscribed to plan.", price);

        // cleanup
        api.deleteUser(user.getUserId());
    }

    /**
     * Tests subscription to a plan by updating an order.
     *
     * @throws Exception possible api exception
     */
    public void testUpdateOrderSubscribe() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        // create user
        UserWS user = new UserWS();
        user.setUserName("plan-test-02-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Plan Test");
        contact.setLastName("Update Order (subscribe)");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());


        // create order
        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);
        order.setPeriod(2);
        order.setCurrencyId(1);
        order.setActiveSince(new Date());

        // subscribe to a non-plan junk item
        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(NON_PLAN_ITEM_ID);
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });
        
        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());


        // verify customer prices still empty with API calls.
        assertFalse("Customer should not subscribed to plan.", api.isCustomerSubscribed(PLAN_ID, user.getUserId()));

        PlanItemWS price = api.getCustomerPrice(user.getUserId(), PLAN_AFFECTED_ITEM_ID);
        assertNull("Order does not subscribe the customer to a plan. No price change.", price);

        // subscribe to plan item
        OrderLineWS planLine = new OrderLineWS();
        planLine.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        planLine.setItemId(PLAN_ITEM_ID);
        planLine.setUseItem(true);
        planLine.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line, planLine });

        api.updateOrder(order); // update order

        // verify price creation with API calls.
        assertTrue("Customer should be subscribed to plan.", api.isCustomerSubscribed(PLAN_ID, user.getUserId()));

        price = api.getCustomerPrice(user.getUserId(), PLAN_AFFECTED_ITEM_ID);
        assertEquals("Affected item should be discounted.", new BigDecimal("0.50"), price.getModel().getRateAsDecimal());

        // cleanup
        api.deleteOrder(order.getId());                
        api.deleteUser(user.getUserId());
    }

    /**
     * Tests un-subscription from a plan by updating an order.
     *
     * @throws Exception possible api exception
     */
    public void testUpdateOrderUnsubscribe() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        // create user
        UserWS user = new UserWS();
        user.setUserName("plan-test-03-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Plan Test");
        contact.setLastName("Update Order (un-subscribe)");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());


        // create order
        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);
        order.setPeriod(2);
        order.setCurrencyId(1);
        order.setActiveSince(new Date());

        // subscribe to plan item
        OrderLineWS planLine = new OrderLineWS();
        planLine.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        planLine.setItemId(PLAN_ITEM_ID);
        planLine.setUseItem(true);
        planLine.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { planLine });

        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());


        // verify price creation with API calls.
        assertTrue("Customer should be subscribed to plan.", api.isCustomerSubscribed(PLAN_ID, user.getUserId()));

        PlanItemWS price = api.getCustomerPrice(user.getUserId(), PLAN_AFFECTED_ITEM_ID);
        assertEquals("Affected item should be discounted.", new BigDecimal("0.50"), price.getModel().getRateAsDecimal());

        // remove plan item, replace with non-plan junk item
        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(NON_PLAN_ITEM_ID);
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });

        api.updateOrder(order); // update order

        // verify price removed
        assertFalse("Customer no longer subscribed to plan.", api.isCustomerSubscribed(PLAN_ID, user.getUserId()));

        price = api.getCustomerPrice(user.getUserId(), PLAN_AFFECTED_ITEM_ID);
        assertNull("Order does not subscribe the customer to a plan. No price change.", price);

        // cleanup
        api.deleteOrder(order.getId());
        api.deleteUser(user.getUserId());        
    }

    /**
     * Tests that the plan can be queried by the subscription item.
     *
     * @throws Exception possible api exception
     */
    public void testGetPlanBySubscriptionItem() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        Integer[] planIds = api.getPlansBySubscriptionItem(PLAN_ITEM_ID);
        assertEquals("Should only be 1 plan.", 1, planIds.length);
        assertEquals("Should be 'crazy brian's discount plan'", PLAN_ID, planIds[0]);
    }

    /**
     * Tests that the plan can be queried by the item's it affects.
     *
     * @throws Exception possible api exception
     */
    public void testGetPlansByAffectedItem() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        Integer[] planIds = api.getPlansByAffectedItem(PLAN_AFFECTED_ITEM_ID);
        assertEquals("Should only be 1 plan.", 1, planIds.length);
        assertEquals("Should be 'crazy brian's discount plan'", PLAN_ID, planIds[0]);
    }

    /**
     * Tests plan CRUD API calls.
     *
     * @throws Exception possible api exception
     */
    public void testCreateUpdateDeletePlan() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        final Integer LONG_DISTANCE_PLAN_ITEM = 2700;       // long distance plan A - fixed rate
        final Integer LONG_DISTANCE_CALL = 2800;            // long distance call
        final Integer LONG_DISTANCE_CALL_GENERIC = 2900;    // long distance call - generic


        // create plan
        PlanItemWS callPrice = new PlanItemWS();
        callPrice.setItemId(LONG_DISTANCE_CALL);
        callPrice.setModel(new PriceModelWS(PriceModelStrategy.METERED.name(), new BigDecimal("0.10"), 1));

        PlanWS plan = new PlanWS();
        plan.setItemId(LONG_DISTANCE_PLAN_ITEM);
        plan.setDescription("Discount long distance calls.");
        plan.addPlanItem(callPrice);

        plan.setId(api.createPlan(plan));

        // verify creation
        PlanWS fetchedPlan = api.getPlanWS(plan.getId());
        assertEquals(LONG_DISTANCE_PLAN_ITEM, fetchedPlan.getItemId());
        assertEquals("Discount long distance calls.", fetchedPlan.getDescription());

        PlanItemWS fetchedPrice = fetchedPlan.getPlanItems().get(0);
        assertEquals(LONG_DISTANCE_CALL, fetchedPrice.getItemId());
        assertEquals(PriceModelStrategy.METERED.name(), fetchedPrice.getModel().getType());
        assertEquals(new BigDecimal("0.10"), fetchedPrice.getModel().getRateAsDecimal());
        assertEquals(1, fetchedPrice.getModel().getCurrencyId().intValue());


        // update the plan
        // update the description and add a price for the generic LD item
        PlanItemWS genericPrice = new PlanItemWS();
        genericPrice.setItemId(LONG_DISTANCE_CALL_GENERIC);
        genericPrice.setModel(new PriceModelWS(PriceModelStrategy.METERED.name(), new BigDecimal("0.25"), 1));

        fetchedPlan.setDescription("Updated description.");
        fetchedPlan.addPlanItem(genericPrice);

        api.updatePlan(fetchedPlan);

        // verify update
        fetchedPlan = api.getPlanWS(fetchedPlan.getId());
        assertEquals(LONG_DISTANCE_PLAN_ITEM, fetchedPlan.getItemId());
        assertEquals("Updated description.", fetchedPlan.getDescription());

        fetchedPrice = fetchedPlan.getPlanItems().get(1);
        assertEquals(LONG_DISTANCE_CALL_GENERIC, fetchedPrice.getItemId());
        assertEquals(PriceModelStrategy.METERED.name(), fetchedPrice.getModel().getType());
        assertEquals(new BigDecimal("0.25"), fetchedPrice.getModel().getRateAsDecimal());
        assertEquals(1, fetchedPrice.getModel().getCurrencyId().intValue());


        // delete the plan
        api.deletePlan(fetchedPlan.getId());

        try {
            api.getPlanWS(fetchedPlan.getId());
            fail("plan deleted, should throw an exception.");
        } catch (SessionInternalError e) {
            assertTrue(e.getMessage().contains("No row with the given identifier exists"));
        }
    }

    /**
     * Tests that the customer is un-subscribed when the plan is deleted.
     *
     * @throws Exception possible api exception
     */
    public void testUnsubscribePlanDelete() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        final Integer LONG_DISTANCE_PLAN_ITEM = 2701;       // long distance plan B - fixed rate
        final Integer LONG_DISTANCE_CALL = 2800;            // long distance call


        // create plan
        PlanItemWS callPrice = new PlanItemWS();
        callPrice.setItemId(LONG_DISTANCE_CALL);
        callPrice.setModel(new PriceModelWS(PriceModelStrategy.METERED.name(), new BigDecimal("0.10"), 1));

        PlanWS plan = new PlanWS();
        plan.setItemId(LONG_DISTANCE_PLAN_ITEM);
        plan.setDescription("Discount long distance calls.");
        plan.addPlanItem(callPrice);

        plan.setId(api.createPlan(plan));
        assertNotNull("plan created", plan.getId());


        // subscribe a customer to the plan
        UserWS user = new UserWS();
        user.setUserName("plan-test-04-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Plan Test");
        contact.setLastName("Delete plan (un-subscribe)");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());

        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);
        order.setPeriod(2);
        order.setCurrencyId(1);
        order.setActiveSince(new Date());

        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(LONG_DISTANCE_PLAN_ITEM);
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });

        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());


        // verify that customer is subscribed
        assertTrue("Customer should be subscribed to plan.", api.isCustomerSubscribed(plan.getId(), user.getUserId()));

        // delete plan
        api.deletePlan(plan.getId());

        // verify that customer is no longer subscribed
        assertFalse("Customer should no longer be subscribed to plan.", api.isCustomerSubscribed(plan.getId(), user.getUserId()));

        // cleanup
        api.deleteOrder(order.getId());
        api.deleteUser(user.getUserId());
    }

    /**
     * Tests that the price is affected by the subscription to a plan
     *
     * @throws Exception possible api exception
     */
    public void testRateOrder() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        UserWS user = new UserWS();
        user.setUserName("plan-test-05-" + new Date().getTime());
        user.setPassword("password");
        user.setLanguageId(1);
        user.setCurrencyId(1);
        user.setMainRoleId(5);
        user.setStatusId(UserDTOEx.STATUS_ACTIVE);
        user.setBalanceType(Constants.BALANCE_NO_DYNAMIC);

        ContactWS contact = new ContactWS();
        contact.setEmail("test@test.com");
        contact.setFirstName("Plan Test");
        contact.setLastName("Rate order test");
        user.setContact(contact);

        user.setUserId(api.createUser(user)); // create user
        assertNotNull("customer created", user.getUserId());

        OrderWS order = new OrderWS();
    	order.setUserId(user.getUserId());
        order.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);
        order.setPeriod(2);
        order.setCurrencyId(1);
        order.setActiveSince(new Date());

        OrderLineWS line = new OrderLineWS();
        line.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        line.setItemId(PLAN_ITEM_ID);
        line.setUseItem(true);
        line.setQuantity(1);
        order.setOrderLines(new OrderLineWS[] { line });

        order.setId(api.createOrder(order)); // create order
        order = api.getOrder(order.getId());
        assertNotNull("order created", order.getId());


        // verify customer price creation with API calls.
        assertTrue("Customer should be subscribed to plan.", api.isCustomerSubscribed(PLAN_ID, user.getUserId()));

        PlanItemWS price = api.getCustomerPrice(user.getUserId(), PLAN_AFFECTED_ITEM_ID);
        assertEquals("Affected item should be discounted.", new BigDecimal("0.50"), price.getModel().getRateAsDecimal());


        // todo: cannot test rate order unless the PriceModelPricingTask is configured as the pricing plug-in
/*
        // test order using the affected plan item
        OrderWS testOrder = new OrderWS();
    	testOrder.setUserId(user.getUserId());
        testOrder.setBillingTypeId(Constants.ORDER_BILLING_POST_PAID);
        testOrder.setPeriod(2);
        testOrder.setCurrencyId(1);
        testOrder.setActiveSince(new Date());

        OrderLineWS testLine = new OrderLineWS();
        testLine.setTypeId(Constants.ORDER_LINE_TYPE_ITEM);
        testLine.setItemId(PLAN_AFFECTED_ITEM_ID);
        testLine.setUseItem(true);
        testLine.setQuantity(1);
        testOrder.setOrderLines(new OrderLineWS[] { testLine });


        // rate order and verify price
        testOrder = api.rateOrder(testOrder);
        assertEquals("Order line should be priced at $0.50.", new BigDecimal("0.50"), testOrder.getOrderLines()[0].getPriceAsDecimal());
*/

        // cleanup
        api.deleteOrder(order.getId());
        api.deleteUser(user.getUserId());
    }

    public void testWSSecurity() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        final Integer BAD_ITEM_ID = 4;        // item belonging to entity 2
        final Integer BAD_USER_ID = 13;       // user belonging to entity 2
        final Integer ENTITY_TWO_PLAN_ID = 2; // plan belonging to entity 2

        // getPlanWS
        try {
            api.getPlanWS(ENTITY_TWO_PLAN_ID);
            fail("Should not be able to get plan from another entity");
        } catch (SecurityException e) {
            assertTrue("Could not get plan for entity 2 subscription item", true);
        }

        // createPlan
        PlanWS createPlan = new PlanWS();
        createPlan.setItemId(BAD_ITEM_ID);
        createPlan.setDescription("Create plan with a bad item.");

        try {
            api.createPlan(createPlan);
            fail("Should not be able to create plans using items from another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not create plan using item belonging to entity 2", true);
        }

        // updatePlan
        PlanWS updatePlan = api.getPlanWS(PLAN_ID);
        updatePlan.setItemId(BAD_ITEM_ID);

        try {
            api.updatePlan(updatePlan);
            fail("Should not be able to update plan using items from another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not update plan using an item belonging to entity 2", true);
        }

        // deletePlan
        try {
            api.deletePlan(ENTITY_TWO_PLAN_ID);
            fail("Should not be able to delete a plan using an item from another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not delete plan for entity 2 subscription item", true);
        }

        // addPlanPrice
        PlanItemWS addPlanPrice = new PlanItemWS();
        addPlanPrice.setItemId(BAD_ITEM_ID);
        addPlanPrice.setModel(new PriceModelWS(PriceModelStrategy.METERED.name(), new BigDecimal("1.00"), 1));

        try {
            // cannot add to a plan we don't own
            api.addPlanPrice(ENTITY_TWO_PLAN_ID, addPlanPrice);
            fail("Should not be able to delete");
        } catch (SecurityException e) {
            assertTrue("Could not add price to a plan for entity 2 subscription item", true);
        }

        try {
            // cannot add a price for an item we don't own
            api.addPlanPrice(PLAN_ID, addPlanPrice);
            fail("Should not be able to add price for item from another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not add price for an item belonging to entity 2", true);
        }

        // isCustomerSubscribed
        try {
            api.isCustomerSubscribed(ENTITY_TWO_PLAN_ID, 2); // entity 2 plan, for gandalf (entity 1 user)
            fail("Should not be able to check subscription status for a plan from another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not check subscription status for plan belonging to entity 2", true);
        }

        // getSubscribedCustomers
        try {
            api.getSubscribedCustomers(ENTITY_TWO_PLAN_ID);
            fail("Should not be able to get subscribed customers for a plan from another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not get subscribed customers  for plan belonging to entity 2", true);
        }

        // getPlansbySubscriptionItem
        try {
            api.getPlansBySubscriptionItem(BAD_ITEM_ID);
            fail("Should not be able to get plans using for item belonging to another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not get plans by subscription item belonging to entity 2", true);
        }

        // getPlansByAffectedItem
        try {
            api.getPlansByAffectedItem(BAD_ITEM_ID);
            fail("Should not be able to get plans using for item belonging to another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not get plans by affected item belonging to entity 2", true);
        }

        // getCustomerPrice
        try {
            api.getCustomerPrice(BAD_USER_ID, PLAN_AFFECTED_ITEM_ID);
            fail("Should not be able to get price for a user belonging to another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not get price for user belonging to entity 2", true);

        }

        // getCustomerPriceByAttributes
        try {
            api.getCustomerPriceByAttributes(BAD_USER_ID, PLAN_AFFECTED_ITEM_ID, new HashMap<String, String>());
            fail("Should not be able to get price for a user belonging to another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not get price for user belonging to entity 2", true);
        }

        // getCustomerPriceByWildcardAttributes
        try {
            api.getCustomerPriceByWildcardAttributes(BAD_USER_ID, PLAN_AFFECTED_ITEM_ID, new HashMap<String, String>());
            fail("Should not be able to get price for a user belonging to another entity.");
        } catch (SecurityException e) {
            assertTrue("Could not get price for user belonging to entity 2", true);
        }
    }


    // note: old tests from original version, use as a basis for new WS tests
/*
    private ItemDTOEx planItem;

    public ItemDTOEx getPlanItem() throws Exception {
        if (planItem == null) {
            JbillingAPI api = JbillingAPIFactory.getAPI();

            for (ItemDTOEx item : api.getAllItems()){
                if (item.getId().equals(PLAN_ITEM_ID)) {
                    planItem = item;
                    break;
                }
            }
        }
        return planItem;
    }

    public void testGetPlanPriceByAttribute() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        ItemDTOEx planItem = getPlanItem();

        // price 1
        PriceModelWS planPrice1 = new PriceModelWS();
        planPrice1.setType(PriceModelWS.PLAN_TYPE_METERED);
        planPrice1.setPlanItem(planItem);
        planPrice1.setRate(new BigDecimal("0.10"));
        planPrice1.setPrecedence(0);
        planPrice1.addAttribute("lata", "0722");
        planPrice1.addAttribute("rateCenter", "CHARLOTTE");
        planPrice1.addAttribute("stateProv", "NC");

        PriceModelWS planPrice2 = new PriceModelWS();
        planPrice2.setType(PriceModelWS.PLAN_TYPE_METERED);
        planPrice2.setPlanItem(planItem);
        planPrice2.setRate(new BigDecimal("0.10"));
        planPrice2.setPrecedence(0);
        planPrice2.addAttribute("lata", "0723");
        planPrice2.addAttribute("rateCenter", "CHARLOTTE");
        planPrice2.addAttribute("stateProv", "NC");

        Integer[] planPriceIds = api.createPriceModels(new PriceModelWS[] { planPrice1, planPrice2 });

        // all attributes must match to be returned
        Map<String, String> attributes1 = new HashMap<String, String>();
        attributes1.put("lata", "0722");
        attributes1.put("rateCenter", "CHARLOTTE");
        attributes1.put("stateProv", "NC");

        PriceModelWS[] prices1 = api.getPriceModelsByItemAndAttributes(new Integer[]{ planItem.getId() }, attributes1);
        assertEquals("only 1 price with ALL attributes matching.", 1, prices1.length);

        // can selectively query attributes
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes2.put("rateCenter", "CHARLOTTE");

        PriceModelWS[] prices2 = api.getPriceModelsByItemAndAttributes(new Integer[]{ planItem.getId() }, attributes2);
        assertEquals("only 2 prices matching rateCenter.", 2, prices2.length);

        // clean up
        api.deletePriceModel(planPriceIds[0]);
        api.deletePriceModel(planPriceIds[1]);
    }

    public void testGetPlanPriceByWildcardAttribute() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        ItemDTOEx planItem = getPlanItem();

        // price 1
        PriceModelWS planPrice1 = new PriceModelWS();
        planPrice1.setType(PriceModelWS.PLAN_TYPE_METERED);
        planPrice1.setPlanItem(planItem);
        planPrice1.setRate(new BigDecimal("0.10"));
        planPrice1.setPrecedence(0);
        planPrice1.addAttribute("lata", null);
        planPrice1.addAttribute("rateCenter", "CHARLOTTE");
        planPrice1.addAttribute("stateProv", "NC");

        // price 2
        PriceModelWS planPrice2 = new PriceModelWS();
        planPrice2.setType(PriceModelWS.PLAN_TYPE_METERED);
        planPrice2.setPlanItem(planItem);
        planPrice2.setRate(new BigDecimal("0.10"));
        planPrice2.setPrecedence(0);
        planPrice2.addAttribute("lata", null);
        planPrice2.addAttribute("rateCenter", "CHARLOTTE");
        planPrice2.addAttribute("stateProv", "NC");

        Integer[] planPriceIds = api.createPriceModels(new PriceModelWS[] { planPrice1, planPrice2 });

        // null plan price attributes are treated as wild-cards
        // above pricing will only match on "rateCenter" and "stateProv" when queried with the wildcard method
        Map<String, String> attributes1 = new HashMap<String, String>();
        attributes1.put("lata", "9999");
        attributes1.put("rateCenter", "CHARLOTTE");
        attributes1.put("stateProv", "NC");

        PriceModelWS[] prices1 = api.getPriceModelsByItemAndWildcardAttributes(new Integer[]{ planItem.getId() }, attributes1);
        assertEquals("Both plan prices match, null lata treated as wildcard.", 2, prices1.length);

        // can selectively query attributes
        Map<String, String> attributes2 = new HashMap<String, String>();
        attributes2.put("lata", "9999");
        attributes2.put("rateCenter", "CHARLOTTE");

        PriceModelWS[] prices2 = api.getPriceModelsByItemAndWildcardAttributes(new Integer[]{ planItem.getId() }, attributes2);
        assertEquals("Both plan prices match, null lata treated as wildcard.", 2, prices2.length);

        // clean up
        api.deletePriceModel(planPriceIds[0]);
        api.deletePriceModel(planPriceIds[1]);
    }

    public void testGetPlanPriceByAttributeWithPrecedence() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        ItemDTOEx planItem = getPlanItem();

        // price 1
        PriceModelWS planPrice1 = new PriceModelWS();
        planPrice1.setType(PriceModelWS.PLAN_TYPE_METERED);
        planPrice1.setPlanItem(planItem);
        planPrice1.setRate(new BigDecimal("0.10"));
        planPrice1.setPrecedence(0);
        planPrice1.addAttribute("lata", "0722");
        planPrice1.addAttribute("rateCenter", "CHARLOTTE");
        planPrice1.addAttribute("stateProv", "NC");

        // price 2 with a higher precedence
        PriceModelWS planPrice2 = new PriceModelWS();
        planPrice2.setType(PriceModelWS.PLAN_TYPE_METERED);
        planPrice2.setPlanItem(planItem);
        planPrice2.setRate(new BigDecimal("0.05"));
        planPrice2.setPrecedence(10);
        planPrice2.addAttribute("lata", "0722");
        planPrice2.addAttribute("rateCenter", "CHARLOTTE");
        planPrice2.addAttribute("stateProv", "NC");

        Integer[] planPriceIds = api.createPriceModels(new PriceModelWS[] { planPrice1, planPrice2 });

        // both pricing returned ordered by precedence, price 2 should be first
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("lata", "0722");
        attributes.put("rateCenter", "CHARLOTTE");
        attributes.put("stateProv", "NC");

        PriceModelWS[] prices = api.getPriceModelsByItemAndAttributes(new Integer[]{ planItem.getId() }, attributes);
        assertEquals("should be 2 pricing plans matching", 2, prices.length);
        assertEquals("price 2 should be first (highest precedence)", planPrice2.getRate(), prices[0].getRate());

        // clean up
        api.deletePriceModel(planPriceIds[0]);
        api.deletePriceModel(planPriceIds[1]);
    }

    public void testGetPlanPriceByAttributeWithMultiplePlans() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        // price 1
        ItemDTOEx planItem1 = getPlanItem();

        PriceModelWS planPrice1 = new PriceModelWS();
        planPrice1.setType(PriceModelWS.PLAN_TYPE_METERED);
        planPrice1.setPlanItem(planItem1);
        planPrice1.setRate(new BigDecimal("0.10"));
        planPrice1.setPrecedence(0);
        planPrice1.addAttribute("lata", "0722");
        planPrice1.addAttribute("rateCenter", "CHARLOTTE");
        planPrice1.addAttribute("stateProv", "NC");

        // price 2 with a higher precedence
        ItemDTOEx planItem2 = getPlanItem();

        PriceModelWS planPrice2 = new PriceModelWS();
        planPrice2.setType(PriceModelWS.PLAN_TYPE_METERED);
        planPrice2.setPlanItem(planItem2);
        planPrice2.setRate(new BigDecimal("0.05"));
        planPrice2.setPrecedence(10);
        planPrice2.addAttribute("lata", "0722");
        planPrice2.addAttribute("rateCenter", "CHARLOTTE");
        planPrice2.addAttribute("stateProv", "NC");

        Integer[] planPriceIds = api.createPriceModels(new PriceModelWS[] { planPrice1, planPrice2 });

        // both pricing returned ordered by precedence, price 2 should be first
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("lata", "0722");
        attributes.put("rateCenter", "CHARLOTTE");
        attributes.put("stateProv", "NC");

        PriceModelWS[] prices = api.getPriceModelsByItemAndAttributes(new Integer[]{ planItem1.getId(), planItem2.getId() }, attributes);
        assertEquals("should be 2 pricing plans matching", 2, prices.length);
        assertEquals("price 2 should be first (highest precedence)", planPrice2.getRate(), prices[0].getRate());

        // clean up
        api.deletePriceModel(planPriceIds[0]);
        api.deletePriceModel(planPriceIds[1]);
    }
*/

    /*
        Convenience assertions for BigDecimal comparisons. 
     */

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, BigDecimal expected, BigDecimal actual) {
        assertEquals(message,
                     (Object) (expected == null ? null : expected.setScale(2, RoundingMode.HALF_UP)),
                     (Object) (actual == null ? null : actual.setScale(2, RoundingMode.HALF_UP)));
    }
}
