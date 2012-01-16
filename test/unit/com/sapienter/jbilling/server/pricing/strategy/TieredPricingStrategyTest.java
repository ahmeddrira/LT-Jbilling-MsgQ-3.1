/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
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
        strategy.applyTo(null, result, null, planPrice, new BigDecimal(10), getUsage(new BigDecimal(1000))); // 10 purchased, 1000 usage

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
        strategy.applyTo(null, result, null, planPrice, new BigDecimal(500), getUsage(BigDecimal.ZERO)); // 500 purchased, 0 usage
        assertEquals(new BigDecimal("3"), result.getPrice());
    }

    public void testApplyToTierNoMatch() throws Exception {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.TIERED);

        BigDecimal rate = new BigDecimal("1.00"); // round numbers for easy math :)
        //planPrice.setRate(rate);
        planPrice.addAttribute("500",  "3");
        planPrice.addAttribute("1500", "2");
        planPrice.addAttribute("3000", "1");
        planPrice.addAttribute("5000", "0.5");

        PricingResult result = new PricingResult(1, 2, 3);
        strategy.applyTo(null, result, null, planPrice, new BigDecimal(1000), getUsage(new BigDecimal(2200)));

        assertEquals(new BigDecimal("1.59"), result.getPrice());

        PricingResult result2 = new PricingResult(1, 2, 3);
        strategy.applyTo(null, result2, null, planPrice, new BigDecimal(100), getUsage(new BigDecimal(5000)));

        assertEquals(new BigDecimal("1.19"), result2.getPrice());
    }
}
