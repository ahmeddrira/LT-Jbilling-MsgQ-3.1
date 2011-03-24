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

package com.sapienter.jbilling.server.pricing.tasks;

import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.item.db.PlanItemDTO;
import com.sapienter.jbilling.server.item.tasks.IPricing;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.order.UsageBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.user.CustomerPriceBL;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pricing plug-in that calculates prices using the customer price map and PriceModelDTO
 * pricing strategies. This plug-in allows for complex pricing strategies to be applied
 * based on a customers subscribed plans, quantity purchased and the current usage.
 *
 * @author Brian Cowdery
 * @since 16-08-2010
 */
public class PriceModelPricingTask extends PluggableTask implements IPricing {
    private static final Logger LOG = Logger.getLogger(PriceModelPricingTask.class);

    private static final Integer MAX_RESULTS = 1;

    private static final String PARAM_USE_ATTRIBUTES = "use_attributes";
    private static final String PARAM_USE_WILDCARDS = "use_wildcards";

    private static final boolean DEFAULT_USE_ATTRIBUTES = false;
    private static final boolean DEFAULT_USE_WILDCARDS = false;

    public BigDecimal getPrice(Integer itemId,
                               BigDecimal quantity,
                               Integer userId,
                               Integer currencyId,
                               List<PricingField> fields,
                               BigDecimal defaultPrice,
                               OrderDTO pricingOrder) throws TaskException {

        LOG.debug("Pricing item " + itemId + ", quantity " + quantity + " - for user " + userId);

        if (userId != null) {
            // get customer pricing model, use fields as attributes
            Map<String, String> attributes = getAttributes(fields);
            PriceModelDTO model = getCustomerPriceModel(userId, itemId, attributes);

            // no customer price, this means the customer has not subscribed to a plan affecting this
            // item, or does not have a customer specific price set. Use the item default price.
            if (model == null) {
                LOG.debug("No customer price found, using item default price model.");
                model = new ItemBL(itemId).getEntity().getDefaultPrice();
            }

            // apply price model
            if (model != null) {
                LOG.debug("Applying price model " + model);

                // fetch current usage of the item if the pricing strategy requires it
                Usage usage = null;
                if (model.getStrategy().requiresUsage()) {
                    usage = new UsageBL(userId, pricingOrder).getItemUsage(itemId);

                    LOG.debug("Current usage of item " + itemId + ": " + usage);
                } else {
                    LOG.debug("Pricing strategy " + model.getType() + " does not require usage.");
                }
                                
                PricingResult result = new PricingResult(itemId, quantity, userId, currencyId);
                model.applyTo(pricingOrder, result, fields, result.getQuantity(), usage);
                LOG.debug("Price discovered: " + result.getPrice());
                return result.getPrice();
            }
        }        

        LOG.debug("No price model found, using default price.");
        return defaultPrice;
    }

    /**
     * Fetches a price model for the given pricing request.
     *
     * If the parameter "use_attributes" is set, the given pricing fields will be used as
     * query attributes to determine the pricing model.
     *
     * If the parameter "use_wildcards" is set, the price model lookup will allow matches
     * on wildcard attributes (stored in the database as "*").
     *
     * @param userId id of the user pricing the item
     * @param itemId id of the item to price
     * @param attributes attributes from pricing fields
     * @return found pricing model, or null if none found
     */
    public PriceModelDTO getCustomerPriceModel(Integer userId, Integer itemId, Map<String, String> attributes) {
        CustomerPriceBL customerPriceBl = new CustomerPriceBL(userId);

        if (getParameter(PARAM_USE_ATTRIBUTES, DEFAULT_USE_ATTRIBUTES) && !attributes.isEmpty()) {
            if (getParameter(PARAM_USE_WILDCARDS, DEFAULT_USE_WILDCARDS)) {
                LOG.debug("Fetching customer price using wildcard attributes: " + attributes);
                List<PlanItemDTO> items = customerPriceBl.getPricesByWildcardAttributes(itemId, attributes, MAX_RESULTS);
                return !items.isEmpty() ? items.get(0).getModel() : null;
            } else {
                LOG.debug("Fetching customer price using attributes: " + attributes);
                List<PlanItemDTO> items = customerPriceBl.getPricesByAttributes(itemId, attributes, MAX_RESULTS);
                return !items.isEmpty() ? items.get(0).getModel() : null;
            }

        } else {
            // not configured to query prices with attributes, or no attributes given
            // determine customer price normally
            LOG.debug("Fetching customer price without attributes (no PricingFields given or 'use_attributes' = false)");
            PlanItemDTO item = customerPriceBl.getPrice(itemId);
            return item != null ? item.getModel() : null;
        }
    }

    /**
     * Convert pricing fields into price model query attributes.
     *
     * @param fields pricing fields to convert
     * @return map of string attributes
     */
    public Map<String, String> getAttributes(List<PricingField> fields) {
        Map<String, String> attributes = new HashMap<String, String>();
        if (fields != null) {
            for (PricingField field : fields)
                attributes.put(field.getName(), field.getStrValue());
        }
        return attributes;
    }
}
