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

import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Brian Cowdery
 * @since 05-08-2010
 */
public class GraduatedPricingStrategy implements PricingStrategy {

    public boolean isGraduated() { return true; }
    public boolean requiresUsage() { return true; }

    public boolean hasRate() { return false; }
    public BigDecimal getRate() { return null; }

    /**
     * Sets the price per minute to zero if the current total usage plus the quantity
     * being purchased is less than this plan's included quantity. The plan rate is set
     * only when the customer runs out of included items.
     *
     * This method applies a weighted scale to the set rate if only some of the purchased
     * usage runs over the number of included quantity.
     *
     * <code>
     *      rated_qty = (purchased_qty + current usage) - included
     *      percent = rated_qty / purchased_qty
     *
     *      price = percent * rate
     * </code>
     *
     * @param result pricing result to apply pricing to
     * @param planPrice the plan price to apply
     * @param quantity quantity of item being priced
     * @param usage total item usage for this billing period
     */
    public void applyTo(PricingResult result, PriceModelDTO planPrice, BigDecimal quantity, BigDecimal usage) {
        if (usage == null)
            throw new IllegalArgumentException("Usage cannot be null for GraduatedPricingStrategy.");


        BigDecimal total = quantity.add(usage);

        if (usage.compareTo(planPrice.getIncludedQuantity()) >= 0) {
            // included usage exceeded by current usage
            result.setPrice(planPrice.getRate());

        } else if (total.compareTo(planPrice.getIncludedQuantity()) > 0) {
            // current usage + purchased quantity exceeds included
            // determine the percentage rate for minutes used OVER the included.
            BigDecimal rated = total.subtract(planPrice.getIncludedQuantity());
            BigDecimal percent = rated.divide(quantity, 2, RoundingMode.HALF_UP);
            result.setPrice(percent.multiply(planPrice.getRate()));

        } else {
            // call within included usage
            result.setPrice(BigDecimal.ZERO);
        }        
    }
}