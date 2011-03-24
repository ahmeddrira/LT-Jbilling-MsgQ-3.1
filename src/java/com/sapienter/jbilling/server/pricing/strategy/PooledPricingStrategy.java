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

import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.order.UsageBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.pricing.db.AttributeDefinition;
import com.sapienter.jbilling.server.pricing.db.ChainPosition;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;
import com.sapienter.jbilling.server.pricing.util.ItemPoolUtils;

import java.math.BigDecimal;

import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.DECIMAL;
import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.INTEGER;

/**
 * PooledPricingStrategy
 *
 * Calculates a price based off of the number of subscribed "pool items". The size of the
 * pool dictates the calculated price (incremental volume pricing).
 *
 * @author Brian Cowdery
 * @since 23/03/11
 */
public class PooledPricingStrategy extends GraduatedPricingStrategy {

    public PooledPricingStrategy() {
        setAttributeDefinitions(
                new AttributeDefinition("pool_item_id", INTEGER, false),
                new AttributeDefinition("multiplier", DECIMAL, false)
        );

        setChainPositions(
                ChainPosition.START
        );

        setRequiresUsage(true);
    }

    /**
     * Calculates the included quantity based on the number of pool items purchased on the
     * users main subscription order. A multiplier is applied to to the pooled quantity to
     * obtain the total included quantity.
     *
     *      Included Quantity = Number of Pool Items * Multiplier
     *
     * @param pricingOrder target order for this pricing request
     * @param planPrice the plan price to apply
     * @return included quantity
     */
    @Override
    public BigDecimal getIncludedQuantity(OrderDTO pricingOrder,PriceModelDTO planPrice) {
        Integer poolItemId = AttributeUtils.getInteger(planPrice.getAttributes(), "pool_item_id");
        BigDecimal multiplier = AttributeUtils.getDecimal(planPrice.getAttributes(), "multiplier");

        return ItemPoolUtils.getPoolSize(pricingOrder.getUserId(), poolItemId, multiplier);
    }
}
