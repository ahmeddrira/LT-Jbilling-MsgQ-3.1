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

import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.pricing.db.AttributeDefinition;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.*;

/**
 * Capped, graduated pricing strategy.
 *
 * Only usage over the included quantity, and under the set maximum total $ amount will be billed.
 *
 * @author Brian Cowdery
 * @since 31/01/11
 */
public class CappedGraduatedPricingStrategy extends GraduatedPricingStrategy {

    private static final List<AttributeDefinition> ATTRIBUTE_LIST = Arrays.asList(
        new AttributeDefinition("included", DECIMAL, true),
        new AttributeDefinition("max", DECIMAL, true)
    );

    @Override
    public List<AttributeDefinition> getAttributeDefinitions() {
        return ATTRIBUTE_LIST;
    }

    /**
     * Graduated pricing strategy with a maximum total usage cap.
     *
     * @see GraduatedPricingStrategy
     *
     * @param result pricing result to apply pricing to
     * @param fields pricing fields (not used by this strategy)
     * @param planPrice the plan price to apply
     * @param quantity quantity of item being priced
     * @param usage total item usage for this billing period
     */
    @Override
    public void applyTo(PricingResult result, List<PricingField> fields, PriceModelDTO planPrice,
                        BigDecimal quantity, Usage usage) {

        if (usage == null || usage.getAmount() == null)
            throw new IllegalArgumentException("Usage amount cannot be null for CappedGraduatedPricingStrategy.");

        BigDecimal maximum = AttributeUtils.getDecimal(planPrice.getAttributes(), "max");
        if (usage.getAmount().compareTo(maximum) <= 0) {
            // usage cap not yet reached, price normally
            super.applyTo(result, fields, planPrice, quantity, usage);
        } else {
            // cap reached, price at zero
            result.setPrice(BigDecimal.ZERO);
        }

        // only bill up to the set maximum cap
        // calculate a unit price that brings the total cost back down to the maximum cap
        if (result.getPrice() != null) {

            BigDecimal total = usage.getAmount().add(quantity.multiply(result.getPrice()));
            if (total.compareTo(maximum) >= 0) {
                BigDecimal billable = maximum.subtract(usage.getAmount());             // remainder to reach maximum
                BigDecimal price = billable.divide(quantity, 4, RoundingMode.HALF_UP); // price per unit
                result.setPrice(price);
            }
        }
    }
}
