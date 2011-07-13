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
import com.sapienter.jbilling.server.order.UsageBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.pricing.db.AttributeDefinition;
import com.sapienter.jbilling.server.pricing.db.ChainPosition;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.SortedMap;

import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.*;

/**
 * ItemSelectorPercentageStrategy
 *
 * @author Brian Cowdery
 * @since 13/07/11
 */
public class ItemPercentageSelectorStrategy extends ItemSelectorStrategy {

    private static final Logger LOG = Logger.getLogger(ItemPercentageSelectorStrategy.class);

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    public ItemPercentageSelectorStrategy() {
        setAttributeDefinitions(
                new AttributeDefinition("typeId", INTEGER, true),
                new AttributeDefinition("percentOfTypeId", INTEGER, true),
                new AttributeDefinition("0", INTEGER, false)
        );

        setChainPositions(
                ChainPosition.START
        );

        setRequiresUsage(false);
    }

    public void applyTo(OrderDTO pricingOrder, PricingResult result, List<PricingField> fields,
                        PriceModelDTO planPrice, BigDecimal quantity, Usage usage) {

        // price of selector trigger item will be the set $/unit rate
        result.setPrice(planPrice.getRate());

        if (pricingOrder != null) {
            // parse item selection tiers
            SortedMap<Integer, Integer> tiers = getTiers(planPrice.getAttributes());
            LOG.debug("Item selector percentage tiers: " + tiers);

            // items used for selection
            Integer typeId = AttributeUtils.getInteger(planPrice.getAttributes(), "typeId");
            Integer percentOfTypeId = AttributeUtils.getInteger(planPrice.getAttributes(), "percentOfTypeId");

            Usage typeUsage = new UsageBL(result.getUserId(), pricingOrder).getItemTypeUsage(typeId);
            Usage percentageTypeUsage = new UsageBL(result.getUserId(), pricingOrder).getItemTypeUsage(percentOfTypeId);

            BigDecimal percentage = getPercentageUsed(typeUsage.getQuantity(), percentageTypeUsage.getQuantity());
            LOG.debug("( " + typeUsage.getQuantity() + " / " + percentageTypeUsage.getQuantity() + " ) * 100 = " + percentage);
            LOG.debug("Selecting tier for usage percentage " + percentage);

            // find matching tier
            Integer selectedItemId = tiers.get(0);
            for (Integer tier : tiers.keySet()) {
                if (percentage.compareTo(new BigDecimal(tier)) >= 0) {
                    selectedItemId = tiers.get(tier);
                }
            }

            // add item
            if (selectedItemId != null) {
                addIfNotExists(pricingOrder, tiers, selectedItemId);
            } else {
                LOG.debug("No tier for usage percentage " + percentage);
            }

        } else {
            LOG.debug("No pricing order given (simple price fetch), skipping tiered item selection.");
        }
    }

    /**
     * Calculate the percentage
     * @param typeUsage
     * @param percentageOfUsage
     * @return
     */
    public BigDecimal getPercentageUsed(BigDecimal typeUsage, BigDecimal percentageOfUsage) {
        if (typeUsage.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        if (percentageOfUsage.compareTo(BigDecimal.ZERO) == 0) return ONE_HUNDRED;

        typeUsage = typeUsage.setScale(2);
        percentageOfUsage = percentageOfUsage.setScale(2);

        return typeUsage.divide(percentageOfUsage, RoundingMode.HALF_UP).multiply(ONE_HUNDRED);
    }
}
