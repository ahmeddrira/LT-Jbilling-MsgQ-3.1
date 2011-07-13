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

import com.sapienter.jbilling.common.Constants;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.INTEGER;

/**
 * Tiered pricing strategy.
 *
 * Price set on bands of usage.
 *
 * @author Vikas Bodani
 * @since 15-03-2011
 */
public class TieredPricingStrategy extends AbstractPricingStrategy {

    private static final Logger LOG = Logger.getLogger(TieredPricingStrategy.class);

    public TieredPricingStrategy() {
        setAttributeDefinitions(
                new AttributeDefinition("0", INTEGER, false)
        );

        setChainPositions(
                ChainPosition.START
        );

        setRequiresUsage(true);
    }

    public void applyTo(OrderDTO pricingOrder, PricingResult result, List<PricingField> fields,
                        PriceModelDTO planPrice, BigDecimal quantity, Usage usage) {

        if (usage == null || usage.getQuantity() == null)
            throw new IllegalArgumentException("Usage quantity cannot be null for TieredPricingStrategy.");

        // parse pricing tiers
        SortedMap<Integer, BigDecimal> tiers = getTiers(planPrice.getAttributes());
        LOG.debug("Pricing tiers: " + tiers);
        LOG.debug("Selecting pricing tier for usage level " + usage.getQuantity());

        // find matching tier
        BigDecimal price = tiers.get(0);
        for (Integer tier : tiers.keySet()) {
            if (usage.getQuantity().compareTo(new BigDecimal(tier)) >= 0) {
                price = tiers.get(tier);
            }
        }

        if (price != null) {
            result.setPrice(price);
        } else {
            LOG.debug("No pricing tier for usage level " + usage.getQuantity());
        }
    }

    /**
     * Parses the price model attributes and returns a map of tier quantities and corresponding
     * prices for each tier. The map is sorted in ascending order by quantity (smallest first).
     *
     * @param attributes attributes to parse
     * @return tiers of quantities and prices
     */
    protected SortedMap<Integer, BigDecimal> getTiers(Map<String, String> attributes) {
        SortedMap<Integer, BigDecimal> tiers = new TreeMap<Integer, BigDecimal>();

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            if (entry.getKey().matches("^\\d+$")) {
                tiers.put(AttributeUtils.parseInteger(entry.getKey()), AttributeUtils.parseDecimal(entry.getValue()));
            }
        }

        return tiers;
    }
}
