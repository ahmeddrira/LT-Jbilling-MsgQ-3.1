/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.pricing.strategy;

import com.sapienter.jbilling.server.BigDecimalTestCase;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import junit.framework.TestCase;

import java.math.BigDecimal;

/**
 * CappedGraduatedPricingStrategyTest
 *
 * @author Brian Cowdery
 * @since 03/02/11
 */
public class CappedGraduatedPricingStrategyTest extends BigDecimalTestCase {

    // class under test
    private PricingStrategy strategy = new CappedGraduatedPricingStrategy();


    public CappedGraduatedPricingStrategyTest() {
    }

    public CappedGraduatedPricingStrategyTest(String name) {
        super(name);
    }

    /**
     * Convenience test method to build a usage object for the given quantity and amount
     * @param quantity quantity
     * @param amount amount
     * @return usage object
     */
    private Usage getUsage(Integer quantity, Integer amount) {
        Usage usage = new Usage();
        usage.setQuantity(new BigDecimal(quantity));
        usage.setAmount(new BigDecimal(amount));

        return usage;
    }

    public void testMaximum() {
        PriceModelDTO model = new PriceModelDTO();
        model.setRate(new BigDecimal("1.00"));
        model.addAttribute("included", "2");
        model.addAttribute("max", "10.00");

        PricingResult result = new PricingResult(1, 2, 3);

        // test 1 unit of purchase, should be free
        BigDecimal quantity = new BigDecimal(1);
        strategy.applyTo(null, result, null, model, quantity, getUsage(0, 0));
        assertEquals(BigDecimal.ZERO, result.getPrice());

        // test 4 unit of purchase, 2 over included, should be $2.00 total
        quantity = new BigDecimal(4);
        strategy.applyTo(null, result, null, model, quantity, getUsage(0, 0));
        assertEquals(new BigDecimal("2.00"), result.getPrice().multiply(quantity));

        // test 14 unit of purchase, totals $12.00, but maximum cap is set to $10.00
        quantity = new BigDecimal(14);
        strategy.applyTo(null, result, null, model, quantity, getUsage(0, 0));
        assertEquals(new BigDecimal("10.00"), result.getPrice().multiply(quantity));

        // HUGE purchase, still shouldn't exceed cap
        quantity = new BigDecimal(100);
        strategy.applyTo(null, result, null, model, quantity, getUsage(0, 0));
        assertEquals(new BigDecimal("10.00"), result.getPrice().multiply(quantity));
    }

    public void testMaximumWithExistingUsage() {
        PriceModelDTO model = new PriceModelDTO();
        model.setRate(new BigDecimal("1.00"));
        model.addAttribute("included", "2");
        model.addAttribute("max", "10.00");

        PricingResult result = new PricingResult(1, 2, 3);

        // test 1 unit of purchase, with 2 units of existing usage
        BigDecimal quantity = new BigDecimal(1);
        strategy.applyTo(null, result, null, model, quantity, getUsage(2, 0));
        assertEquals(new BigDecimal("1.00"), result.getPrice());

        // test 1 unit of purchase, with 10 units of existing usage and the cap exceeded
        quantity = new BigDecimal(1);
        strategy.applyTo(null, result, null, model, quantity, getUsage(10, 10));
        assertEquals(BigDecimal.ZERO, result.getPrice());
    }
}
