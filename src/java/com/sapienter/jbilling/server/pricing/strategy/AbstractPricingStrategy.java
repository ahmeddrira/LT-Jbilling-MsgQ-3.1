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

import com.sapienter.jbilling.server.pricing.db.AttributeDefinition;
import com.sapienter.jbilling.server.pricing.db.ChainPosition;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * AbstractPricingStrategy
 *
 * @author Brian Cowdery
 * @since 07/02/11
 */
public abstract class AbstractPricingStrategy implements PricingStrategy {

    private List<AttributeDefinition> attributeDefinitions = Collections.emptyList();
    private List<ChainPosition> chainPositions = Collections.emptyList();
    private BigDecimal rate = null;
    private boolean requiresUsage = false;


    public List<AttributeDefinition> getAttributeDefinitions() {
        return attributeDefinitions;
    }

    public void setAttributeDefinitions(AttributeDefinition ...attributeDefinitions) {
        this.attributeDefinitions = Collections.unmodifiableList(Arrays.asList(attributeDefinitions));
    }

    public List<ChainPosition> getChainPositions() {
        return chainPositions;
    }

    public void setChainPositions(ChainPosition ...chainPositions) {
        this.chainPositions = Collections.unmodifiableList(Arrays.asList(chainPositions));
    }

    public boolean hasRate() {
        return rate != null;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public boolean requiresUsage() {
        return requiresUsage;
    }

    public void setRequiresUsage(boolean requiresUsage) {
        this.requiresUsage = requiresUsage;
    }
}
