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
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.db.PriceModelStrategy;
import junit.framework.TestCase;

import java.math.BigDecimal;

/**
 * PercentageStrategyTest
 *
 * @author Brian Cowdery
 * @since 07/02/11
 */
public class PercentageStrategyTest extends BigDecimalTestCase {

    public PercentageStrategyTest() {
    }

    public PercentageStrategyTest(String name) {
        super(name);
    }

    /**
     * Tests that a percentage can be applied to a previously determined price.
     */
    public void testPercentage() {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.PERCENTAGE);
        planPrice.setRate(new BigDecimal("0.80")); // %80

        PricingResult result = new PricingResult(1, 2, 3);
        result.setPrice(new BigDecimal("10.00"));

        // test that the price has been reduced by %80
        // $10 * 0.80 = $8
        planPrice.applyTo(result, null, null, null);
        assertEquals(new BigDecimal("8.00"), result.getPrice());
    }

    /**
     * Tests that the price must be set to apply a percentage. A null price should not
     * cause an exception or other unwanted behaviour.
     */
    public void testNullPrice() {
        PriceModelDTO planPrice = new PriceModelDTO();
        planPrice.setType(PriceModelStrategy.PERCENTAGE);
        planPrice.setRate(new BigDecimal("0.80")); // %80

        // result without price
        PricingResult result = new PricingResult(1, 2, 3);

        // applying without a price shouldn't cause an exception
        // price should still be null
        planPrice.applyTo(result, null, null, null);
        assertNull(result.getPrice());
    }
}
