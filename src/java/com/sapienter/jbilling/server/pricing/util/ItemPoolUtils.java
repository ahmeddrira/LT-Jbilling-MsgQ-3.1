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

package com.sapienter.jbilling.server.pricing.util;

import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.order.UsageBL;

import java.math.BigDecimal;

/**
 * Simple utility to count the total number of items on purchase orders for
 * a given user, within the users current billing period.
 *
 * The number of purchased items represents the "pool" of usage allowed by a
 * customer.
 *
 * @author Brian Cowdery
 * @since 24/03/11
 */
public class ItemPoolUtils {

    /**
     * Counts the pool size for the given user id and item, with no multiplier.
     *
     * @param userId user id
     * @param itemId item id
     * @return pooled quantity
     */
    public static BigDecimal getPoolSize(Integer userId, Integer itemId) {
        return getPoolSize(userId, itemId, BigDecimal.ONE);
    }

    /**
     * Counts the pool size for the given user id and item, multiplied by
     * the given multiplier.
     *
     * @param userId user id
     * @param itemId item id
     * @param multiplier multiplier
     * @return pool size
     */
    public static BigDecimal getPoolSize(Integer userId, Integer itemId, BigDecimal multiplier) {
        Usage pool = new UsageBL(userId).getItemUsage(itemId);
        return pool.getQuantity().multiply(multiplier);
    }
}
