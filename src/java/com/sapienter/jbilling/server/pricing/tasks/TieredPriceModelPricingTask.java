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
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.order.UsageBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Pricing plug-in that can be used with multi-teired user structures to include pricing plans
 * from parent accounts. This plug in will walk through each parent account checking for an item
 * price. If no parent holds a price affecting the item, the default item price will be used.
 *
 * This plug in is also capable of performing configurable usage calculations to determine the
 * total item usage depending on how the tiered structure is configured. It can be configured to
 * calculate usage either for the user making the request, or for the user holding the pricing plan
 * (which can be a parent user!) - optionally including sub-account usage.
 *
 * Plug-in parameters:
 *
 *      usage_type          one of 'USER' or 'PLAN_USER', denotes what user should be followed
 *                          when determining item usage for pricing. If 'USER', usage will be
 *                          calculated for the user that made the pricing request. If 'PLAN_USER',
 *                          usage will be calculated using the user that holds the pricing plan.
 *
 *      usage_sub_accounts  [true|false], if true, will include any usage in direct sub accounts
 *                          when determining the total item usage for pricing.                           
 *
 * @see PriceModelPricingTask
 *
 * @author Brian Cowdery
 * @since 08-09-2010
 */
public class TieredPriceModelPricingTask extends PriceModelPricingTask {
    private static final Logger LOG = Logger.getLogger(TieredPriceModelPricingTask.class);

    private static final String PARAM_USAGE_TYPE = "usage_type";
    private static final String PARAM_USAGE_SUB_ACCOUNTS = "usage_sub_accounts";

    private static final String DEFAULT_USAGE_TYPE = UsageType.PLAN_USER.name();
    private static final boolean DEFAULT_USAGE_SUB_ACCOUNTS = false;

    private enum UsageType {
        /** Count usage from the user making the pricing request */
        USER,

        /** Count usage from the user holding the plan (default) */
        PLAN_USER;

        public static UsageType valueOfIgnoreCase(String value) {
            return UsageType.valueOf(value.trim().toUpperCase());            
        }                
    }

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

            // iterate through parents until a price is found.
            PriceModelDTO model = getCustomerPriceModel(userId, itemId, attributes);
            CustomerDTO customer = new UserBL(userId).getEntity().getCustomer();            
            while (customer.getParent() != null && model == null) {
                customer = customer.getParent();

                LOG.debug("Looking for price from parent user " + customer.getBaseUser().getId());
                model = getCustomerPriceModel(customer.getBaseUser().getId(), itemId, attributes);
            }

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
                    try {
                        UsageType type = UsageType.valueOfIgnoreCase(getParameter(PARAM_USAGE_TYPE, DEFAULT_USAGE_TYPE));
                        usage = getUsage(type, itemId, userId, customer.getBaseUser().getId(), pricingOrder);
                    } catch (PluggableTaskException e) {
                        throw new TaskException(e);
                    }
                    LOG.debug("Current usage of item " + itemId + ": " + usage);
                } else {
                    LOG.debug("Pricing strategy " + model.getType() + " does not require usage.");
                }

                PricingResult result = new PricingResult(itemId, quantity, userId, currencyId);
                model.applyTo(pricingOrder, result, fields, result.getQuantity(), usage);
                return result.getPrice();
            }
        }

        LOG.debug("No price model found, using default price.");
        return defaultPrice;
    }

    /**
     * Returns the total usage of the given item for the set UsageType, and optionally include charges
     * made to sub-accounts in the usage calculation.
     *
     * @param type usage type to query, may use either USER or PLAN_USER to determine usage
     * @param itemId item id to get usage for
     * @param userId user id making the price request
     * @param planUserId user holding the pricing plan
     * @param pricingOrder working order (order being edited/created)
     * @return usage for customer and usage type
     * @throws PluggableTaskException thrown if plug-in parameters cannot be parsed
     */
    private Usage getUsage(UsageType type, Integer itemId, Integer userId, Integer planUserId, OrderDTO pricingOrder)
            throws PluggableTaskException {

        UsageBL usage;
        switch (type) {
            case USER:
                usage = new UsageBL(userId, pricingOrder);
                break;

            default:
            case PLAN_USER:
                usage = new UsageBL(planUserId, pricingOrder);
                break;
        }

        if (getParameter(PARAM_USAGE_SUB_ACCOUNTS, DEFAULT_USAGE_SUB_ACCOUNTS)) {
            return usage.getSubAccountItemUsage(itemId);
        } else {
            return usage.getItemUsage(itemId);
        }
    }
}
