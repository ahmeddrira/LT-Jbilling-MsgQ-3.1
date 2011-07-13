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
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderHelper;
import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.order.UsageBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.pricing.db.AttributeDefinition;
import com.sapienter.jbilling.server.pricing.db.ChainPosition;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;
import com.sapienter.jbilling.server.user.db.UserDTO;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.*;

/**
 * PlanSelectorStrategy
 *
 * @author Brian Cowdery
 * @since 12/07/11
 */
public class ItemSelectorStrategy extends AbstractPricingStrategy {

    private static final Logger LOG = Logger.getLogger(ItemSelectorStrategy.class);

    public ItemSelectorStrategy() {
        setAttributeDefinitions(
                new AttributeDefinition("typeId", INTEGER, true),
                new AttributeDefinition("1", INTEGER, false)
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
            LOG.debug("Item selector tiers: " + tiers);

            // items used for selection
            Integer typeId =  AttributeUtils.getInteger(planPrice.getAttributes(), "typeId");
            Usage typeUsage = new UsageBL(result.getUserId(), pricingOrder).getItemTypeUsage(typeId);
            LOG.debug("Selecting tier for usage level " + typeUsage.getQuantity());

            // find matching tier
            Integer selectedItemId = tiers.get(1);
            for (Integer tier : tiers.keySet()) {
                if (typeUsage.getQuantity().compareTo(new BigDecimal(tier)) >= 0) {
                    selectedItemId = tiers.get(tier);
                }
            }

            // add item
            if (selectedItemId != null) {
                addIfNotExists(pricingOrder, tiers, selectedItemId);
            } else {
                LOG.debug("No tier for usage level " + typeUsage.getQuantity());
            }

        } else {
            LOG.debug("No pricing order given (simple price fetch), skipping tiered item selection.");
        }
    }

    /**
     * Adds the given itemId to the order only if it does not already exist. If an item from another tier
     * is present it will be removed before the new tier item is added.
     *
     * @param order order to add item to
     * @param tiers tiers of quantities and items
     * @param itemId item id to add
     */
    protected void addIfNotExists(OrderDTO order, Map<Integer, Integer> tiers, Integer itemId) {
        if (OrderHelper.collect(order, itemId).isEmpty()) {

            // remove other tiers from the order
            Collection<Integer> items = tiers.values();
            items.remove(itemId);

            OrderHelper.delete(order, items);

            // add new item for tier
            UserDTO user = order.getBaseUserByUserId();
            new OrderBL().addItem(order, itemId, BigDecimal.ONE,
                                  user.getLanguage().getId(), user.getId(), user.getCompany().getId(), user.getCurrency().getId(),
                                  null);
        } else {
            LOG.debug("Order already contains item " + itemId + ", no need to add.");
        }
    }

    /**
     * Parses the price model attributes and returns a map of tier quantities and corresponding
     * items to be added at each tier. The map is sorted in ascending order by quantity (smallest first).
     *
     * @param attributes attributes to parse
     * @return tiers of quantities and items
     */
    protected SortedMap<Integer, Integer> getTiers(Map<String, String> attributes) {
        SortedMap<Integer, Integer> tiers = new TreeMap<Integer, Integer>();

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            if (entry.getKey().matches("^\\d+$")) {
                tiers.put(AttributeUtils.parseInteger(entry.getKey()), AttributeUtils.parseInteger(entry.getValue()));
            }
        }

        return tiers;
    }
}
