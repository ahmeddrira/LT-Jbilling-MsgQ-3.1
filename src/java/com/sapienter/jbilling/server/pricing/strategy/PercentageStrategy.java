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
import com.sapienter.jbilling.server.pricing.db.ChainPosition;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.*;

/**
 * PercentagePricingStrategy
 *
 * @author Brian Cowdery
 * @since 07/02/11
 */
public class PercentageStrategy extends AbstractPricingStrategy {

    public PercentageStrategy() {
        setChainPositions(
                ChainPosition.MIDDLE,
                ChainPosition.END
        );
    }

    /**
     * Applies a percentage to the given pricing result. This strategy is designed to be at the middle
     * or end of a pricing chain, when the base rate has already been determined.
     *
     * The PriceModelDTO rate is handled as a decimal percentage by this strategy. A rate of "0.80"
     * would be applied as 80%, "1.25" as %125 and so on.
     *
     * @param result pricing result to apply pricing to
     * @param fields pricing fields
     * @param planPrice the plan price to apply
     * @param quantity quantity of item being priced
     * @param usage total item usage for this billing period
     */
    public void applyTo(PricingResult result, List<PricingField> fields, PriceModelDTO planPrice,
                        BigDecimal quantity, Usage usage) {

        if (result.getPrice() != null) {
            result.setPrice(result.getPrice().multiply(planPrice.getRate()));
        }
    }
}
