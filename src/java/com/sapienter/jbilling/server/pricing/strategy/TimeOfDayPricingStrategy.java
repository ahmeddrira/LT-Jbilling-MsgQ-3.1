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
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.*;


/**
 * TimeOfDayPricingStrategy
 *
 * @author Brian Cowdery
 * @since 03/02/11
 */
public class TimeOfDayPricingStrategy implements PricingStrategy {

    private static final Logger LOG = Logger.getLogger(TimeOfDayPricingStrategy.class);

    private static final List<AttributeDefinition> ATTRIBUTE_LIST = Arrays.asList(
        new AttributeDefinition("date_field", STRING, false),    // pricing field name holding the date
        new AttributeDefinition("00:00", DECIMAL, true)          // price at start of day
    );

    public boolean requiresUsage() { return false; }
    public boolean hasRate() { return true; }
    public BigDecimal getRate() { return BigDecimal.ZERO; }

    public List<AttributeDefinition> getAttributeDefinitions() {
        return ATTRIBUTE_LIST;
    }

    /**
     * Sets the price depending on the current time. The time can be pulled from a pricing field
     * when applied through mediation, or if not found (not running mediation), the current time.
     *
     * This strategy uses attributes to define the time slices.
     *
     * For example, the attributes:
     * <code>
     *      "00:00" = 10.00
     *      "12:00" = 20.00
     * </code>
     *
     * Are handled as:
     * <code>
     *      Between 00:00 and 12:00 = $10.00
     *      After 12:00 = $12.00
     * </code>
     *
     *
     * @param result pricing result to apply pricing to
     * @param fields pricing fields
     * @param planPrice the plan price to apply
     * @param quantity quantity of item being priced
     * @param usage total item usage for this billing period
     */
    public void applyTo(PricingResult result, List<PricingField> fields, PriceModelDTO planPrice,
                        BigDecimal quantity, Usage usage) {

        // parse time ranges and prices
        // map is sorted in ascending order by time - earliest times first
        SortedMap<LocalTime, BigDecimal> prices = new TreeMap<LocalTime, BigDecimal>();
        for (Map.Entry<String, String> entry : planPrice.getAttributes().entrySet()) {
            if (entry.getKey().contains(":"))
                prices.put(AttributeUtils.parseTime(entry.getKey()), AttributeUtils.parseDecimal(entry.getValue()));
        }
        LOG.debug("Time-of-day pricing: " + prices);

        // get the current time from the pricing fields
        String fieldName = planPrice.getAttributes().get("date_field");
        LocalTime now = new LocalTime();
        if (fields != null && fieldName != null) {
            for (PricingField field : fields) {
                if (field.getName().equals(fieldName)) {
                    now = LocalTime.fromDateFields(field.getDateValue());
                    break;
                }
            }
        }

        // find the price
        for (LocalTime time : prices.keySet()) {
            if (now.isEqual(time) || now.isAfter(time)) {
                result.setPrice(prices.get(time));
                LOG.debug("Price for " + now + ": " + result.getPrice());
            }
        }
    }


}
