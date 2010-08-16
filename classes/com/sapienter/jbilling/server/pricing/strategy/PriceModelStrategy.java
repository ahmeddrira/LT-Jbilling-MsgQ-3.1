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

/**
 * Simple mapping enum for PricingStrategy implementations. This class is used
 * to produce PricingStrategy instances for modeled prices.
 *
 * Enum type strings are also mapped in the {@link com.sapienter.jbilling.server.pricing.PriceModelWS}
 * class for convenience when using the Web Services API.
 *
 * @author Brian Cowdery
 * @since 05-08-2010
 */
public enum PriceModelStrategy {

    /** Flat pricing strategy, always sets price to ZERO */
    FLAT      (new FlatPricingStrategy()),

    /** Metered pricing strategy, sets a configurable $/unit rate */
    METERED   (new MeteredPricingStrategy()),

    /** Graduated pricing strategy, allows a set number of included units before enforcing a $/unit rate */
    GRADUATED (new GraduatedPricingStrategy());

    private final PricingStrategy strategy;

    PriceModelStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public PricingStrategy getStrategy() {
        return strategy;
    }
}
