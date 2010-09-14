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

import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import com.sapienter.jbilling.server.util.api.JbillingAPIException;
import com.sapienter.jbilling.server.util.api.JbillingAPIFactory;
import junit.framework.TestCase;
import org.hibernate.ObjectNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 06-08-2010
 */
public class WSTest extends TestCase {

    private static final Integer PLAN_ITEM_ID = 3000;

    private ItemDTOEx planItem;

    public WSTest() {
    }

    public WSTest(String name) {
        super(name);
    }
    
/*
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

    public void testCreate() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        ItemDTOEx planItem = getPlanItem();

        // model with wildcard attributes
        PriceModelWS wildcardPrice = new PriceModelWS();
        wildcardPrice.setType(PriceModelWS.PLAN_TYPE_GRADUATED);
        wildcardPrice.setPlanItem(planItem);
        wildcardPrice.setRate(new BigDecimal("0.20"));
        wildcardPrice.setPrecedence(10);
        wildcardPrice.setIncludedQuantity(new BigDecimal(1000));
        wildcardPrice.addAttribute("lata", null);
        wildcardPrice.addAttribute("rateCenter", null);
        wildcardPrice.addAttribute("stateProv", "NC");

        // default model
        PriceModelWS defaultPrice = new PriceModelWS();
        defaultPrice.setType(PriceModelWS.PLAN_TYPE_METERED);
        defaultPrice.setRate(new BigDecimal("0.10"));
        defaultPrice.setDefaultPricing(true);
        defaultPrice.addAttribute("lata", "0772");
        defaultPrice.addAttribute("rateCenter", "CHARLOTTE");
        defaultPrice.addAttribute("stateProv", "NC");

        Integer[] ids = api.createPriceModels(new PriceModelWS[]{ wildcardPrice, defaultPrice });

        PriceModelWS price1 = api.getPriceModel(ids[0]);
        assertEquals(PriceModelWS.PLAN_TYPE_GRADUATED, price1.getType());
        assertEquals(new BigDecimal("0.20"), price1.getRate());
        assertEquals(10, price1.getPrecedence().intValue());
        assertEquals(new BigDecimal(1000), price1.getIncludedQuantity());
        assertFalse(price1.isDefaultPricing());
        assertEquals(PriceModelWS.ATTRIBUTE_WILDCARD, price1.getAttributes().get("lata"));
        assertEquals(PriceModelWS.ATTRIBUTE_WILDCARD, price1.getAttributes().get("rateCenter"));
        assertEquals("NC", price1.getAttributes().get("stateProv"));

        PriceModelWS price2 = api.getPriceModel(ids[1]);
        assertEquals(PriceModelWS.PLAN_TYPE_METERED, price2.getType());
        assertNull(price2.getPlanItem());                       // default plan has no item
        assertEquals(new BigDecimal("0.10"), price2.getRate());
        assertEquals(-1, price2.getPrecedence().intValue());    // default precedence
        assertTrue(price2.isDefaultPricing());
        assertEquals("0772", price2.getAttributes().get("lata"));
        assertEquals("CHARLOTTE", price2.getAttributes().get("rateCenter"));
        assertEquals("NC", price2.getAttributes().get("stateProv"));

        // cleanup
        api.deletePriceModel(ids[0]);
        api.deletePriceModel(ids[1]);
    }

    public void testUpdate() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        ItemDTOEx planItem = getPlanItem();

        // simple graduated plan
        PriceModelWS graduated = new PriceModelWS();
        graduated.setType(PriceModelWS.PLAN_TYPE_GRADUATED);
        graduated.setPlanItem(planItem);
        graduated.setRate(new BigDecimal("0.20"));
        graduated.setPrecedence(10);
        graduated.setIncludedQuantity(new BigDecimal(1000));
        graduated.addAttribute("lata", null);
        graduated.addAttribute("rateCenter", null);
        graduated.addAttribute("stateProv", "NC");

        Integer[] ids = api.createPriceModels(new PriceModelWS[]{ graduated });

        // update to a FLAT plan
        // non graduated pricing always have ZERO included minutes
        // flat pricing always have a rate of ZERO
        PriceModelWS flat = api.getPriceModel(ids[0]);
        flat.setType(PriceModelWS.PLAN_TYPE_FLAT);
        flat.setPrecedence(5);

        api.updatePriceModel(flat);
        PriceModelWS updated = api.getPriceModel(ids[0]);

        assertEquals(flat.getId(), updated.getId());
        assertEquals(PriceModelWS.PLAN_TYPE_FLAT, updated.getType());
        assertEquals(BigDecimal.ZERO, updated.getRate());
        assertEquals(BigDecimal.ZERO, updated.getIncludedQuantity());
        assertEquals(5, updated.getPrecedence().intValue());
        assertEquals(PriceModelWS.ATTRIBUTE_WILDCARD, updated.getAttributes().get("lata"));
        assertEquals(PriceModelWS.ATTRIBUTE_WILDCARD, updated.getAttributes().get("rateCenter"));
        assertEquals("NC", updated.getAttributes().get("stateProv"));

        // cleanup
        api.deletePriceModel(ids[0]);
    }

    public void testDelete() throws Exception {
        JbillingAPI api = JbillingAPIFactory.getAPI();

        ItemDTOEx planItem = getPlanItem();

        // simple metered plan
        PriceModelWS metered = new PriceModelWS();
        metered.setType(PriceModelWS.PLAN_TYPE_METERED);
        metered.setPlanItem(planItem);
        metered.setRate(new BigDecimal("0.10"));
        metered.setPrecedence(0);

        Integer[] ids = api.createPriceModels(new PriceModelWS[]{ metered });
        api.deletePriceModel(ids[0]);

        try {
            PriceModelWS deleted = api.getPriceModel(ids[0]);
            fail("Should throw an org.hibernate.ObjectNotFoundException");
        } catch (JbillingAPIException e) {
            assertTrue(e.getCause().getCause() instanceof ObjectNotFoundException);
        }
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
