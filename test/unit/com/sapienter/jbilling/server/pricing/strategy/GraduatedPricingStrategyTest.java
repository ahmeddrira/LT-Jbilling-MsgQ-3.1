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
 * @author Brian Cowdery
 * @since 30-07-2010
 */
public class GraduatedPricingStrategyTest extends BigDecimalTestCase {

    // class under test
    private PricingStrategy strategy = new GraduatedPricingStrategy();

    public GraduatedPricingStrategyTest() {
    }

    public GraduatedPricingStrategyTest(String name) {
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

    public void testApplyToOverIncluded() throws Exception {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.GRADUATED);

        BigDecimal rate = new BigDecimal("0.07");
        planPrice.setRate(rate);
        planPrice.addAttribute("included", "1000");

        // included minutes already exceeded by current usage
        PricingResult result = new PricingResult(1, 2, 3);
        // total quantity = purchase quantity + usage = 1001
        BigDecimal purchaseQuantity = new BigDecimal(2);
        BigDecimal usageQuantity = new BigDecimal(1000);
        BigDecimal ratedQuantity = purchaseQuantity.add(usageQuantity).subtract(new BigDecimal(1000));

        // 1 purchased, 1000 usage = 1001 total quantity, 1000 included ==> 1 rated with billable = rate
        strategy.applyTo(null, result, null, planPrice, purchaseQuantity, getUsage(usageQuantity), false);

        // (ratedQuantity / totalQuantity) * rate = price
        assertEquals(rate, result.getPrice().multiply(purchaseQuantity.add(usageQuantity)).divide(ratedQuantity));
    }

    public void testApplyToUnderIncluded() throws Exception {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.GRADUATED);

        BigDecimal rate = new BigDecimal("0.07");
        planPrice.setRate(rate);
        planPrice.addAttribute("included", "1000");

        // included minutes already exceeded by current usage
        PricingResult result = new PricingResult(1, 2, 3);
        strategy.applyTo(null, result, null, planPrice, new BigDecimal(10), getUsage(BigDecimal.ZERO), false); // 10 purchased, 0 usage

        assertEquals(BigDecimal.ZERO, result.getPrice());
    }

    public void testApplyToPartialOverIncluded() throws Exception {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.GRADUATED);

        BigDecimal rate = new BigDecimal("1.00"); // round numbers for easy math :)
        planPrice.setRate(rate);
        planPrice.addAttribute("included", "1000");

        // 20 purchased, 990 usage
        // total quantity = purchase quantity + usage = 1010
        BigDecimal purchaseQuantity = new BigDecimal(20);
        BigDecimal usageQuantity = new BigDecimal(990);
        BigDecimal ratedQuantity = purchaseQuantity.add(usageQuantity).subtract(new BigDecimal(1000));

        // half of the call exceeds the included minutes
        // rate should be 50% of the plan rate
        PricingResult result = new PricingResult(1, 2, 3);
        strategy.applyTo(null, result, null, planPrice, purchaseQuantity, getUsage(usageQuantity), false); // 10 minutes included, 10 minutes rated

        // (ratedQuantity / totalQuantity) * rate = price
        assertEquals(rate, result.getPrice().multiply(purchaseQuantity.add(usageQuantity)).divide(ratedQuantity));

        // 100 purchased, 980 usage
        purchaseQuantity = new BigDecimal(100);
        usageQuantity = new BigDecimal(980);
        ratedQuantity = purchaseQuantity.add(usageQuantity).subtract(new BigDecimal(1000));

        // 80% of the call exceeds the included minutes
        // rate should be 80% of the plan rate
        PricingResult result2 = new PricingResult(1, 2, 3);
        strategy.applyTo(null, result2, null, planPrice, purchaseQuantity, getUsage(usageQuantity), false); // 20 minutes included, 80 minutes rated

        // (ratedQuantity / totalQuantity) * rate = price
        assertEquals(rate, result2.getPrice().multiply(purchaseQuantity.add(usageQuantity)).divide(ratedQuantity));
    }
}
