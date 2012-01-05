/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

package com.sapienter.jbilling.server.pricing.strategy;

import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.order.Usage;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.pricing.db.AttributeDefinition;
import com.sapienter.jbilling.server.pricing.db.ChainPosition;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.sapienter.jbilling.server.pricing.db.AttributeDefinition.Type.*;

/**
 * VolumePricingStrategy
 *
 * @author Brian Cowdery
 * @since 14/07/11
 */
public class VolumePricingStrategy extends AbstractPricingStrategy {

    private static final Logger LOG = Logger.getLogger(VolumePricingStrategy.class);

    public VolumePricingStrategy() {
        setAttributeDefinitions(
                new AttributeDefinition("0", DECIMAL, false)
        );

        setChainPositions(
                ChainPosition.START
        );

        setRequiresUsage(true);
    }

    public void applyTo(OrderDTO pricingOrder, PricingResult result, List<PricingField> fields,
                        PriceModelDTO planPrice, BigDecimal quantity, Usage usage) {

        if (usage == null || usage.getQuantity() == null)
            throw new IllegalArgumentException("Usage quantity cannot be null for VolumePricingStrategy.");

        // parse pricing tiers
        SortedMap<Integer, BigDecimal> tiers = getTiers(planPrice.getAttributes());
        LOG.debug("Volume pricing: " + tiers);
        LOG.debug("Selecting volume price for usage level " + usage.getQuantity());

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
            LOG.debug("No volume price for usage level " + usage.getQuantity());
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
