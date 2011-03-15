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

package com.sapienter.jbilling.server.pricing.strategy;

import com.sapienter.jbilling.server.BigDecimalTestCase;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.db.PriceModelStrategy;

import java.math.BigDecimal;

/**
 * @author Vikas Bodani
 * @since 15-03-2011
 */
public class TieredPricingStrategyTest extends BigDecimalTestCase {

    // class under test
    private PricingStrategy strategy = new TieredPricingStrategy();

    public TieredPricingStrategyTest() {
    }

    public TieredPricingStrategyTest(String name) {
        super(name);
    }

    /**
     * Convenience test method to build a usage object for the given quantity.
     * @param quantity quantity
     * @return usage object
     */
    private Usage getUsage(BigDecimal quantity) {
        Usage usage = new Usage();
        usage.setQuantity(quantity);

        return usage;
    }

    public void testApplyToNoAttributes() throws Exception {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.TIERED);

        BigDecimal rate = new BigDecimal("0.07");
        //planPrice.setRate(rate);

        PricingResult result = new PricingResult(1, 2, 3);
        strategy.applyTo(result, null, planPrice, new BigDecimal(10), getUsage(new BigDecimal(1000))); // 10 purchased, 1000 usage

        assertEquals(BigDecimal.ZERO, result.getPrice());
    }

    public void testApplyToMiddleTier() throws Exception {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.TIERED);

        BigDecimal rate = new BigDecimal("0.07");
        //planPrice.setRate(rate);
        planPrice.addAttribute("500",  "3");
        planPrice.addAttribute("1000", "2");
        planPrice.addAttribute("1500", "1");

        PricingResult result = new PricingResult(1, 2, 3);
        strategy.applyTo(result, null, planPrice, new BigDecimal(500), getUsage(BigDecimal.ZERO)); // 500 purchased, 0 usage
        assertEquals(new BigDecimal("3"), result.getPrice());
    }

    public void testApplyToTierNoMatch() throws Exception {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.TIERED);

        BigDecimal rate = new BigDecimal("1.00"); // round numbers for easy math :)
        //planPrice.setRate(rate);
        planPrice.addAttribute("500",  "3");
        planPrice.addAttribute("1000", "2");
        planPrice.addAttribute("1500", "1");

        PricingResult result = new PricingResult(1, 2, 3);
        strategy.applyTo(result, null, planPrice, new BigDecimal(600), getUsage(new BigDecimal(990))); 

        assertEquals(new BigDecimal("2.25786"), result.getPrice());

        PricingResult result2 = new PricingResult(1, 2, 3);
        strategy.applyTo(result2, null, planPrice, new BigDecimal(100), getUsage(new BigDecimal(1000)));

        assertEquals(new BigDecimal("2.4545"), result2.getPrice());
    }
}
