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

package com.sapienter.jbilling.server.pricing;

import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 06-08-2010
 */
public class PriceModelWS implements Serializable {

    // convenience type strings mapped from PriceModelStrategy
    public static final String PLAN_TYPE_FLAT = "FLAT";
    public static final String PLAN_TYPE_METERED = "METERED";
    public static final String PLAN_TYPE_GRADUATED = "GRADUATED";

    // convenience constants for WS, copied from PriceModelDTO
    public static final ItemDTOEx DEFAULT_PLAN_ITEM = null; // default pricing doesn't have an item
    public static final Integer DEFAULT_PRECEDENCE = -1;   
    public static final String ATTRIBUTE_WILDCARD = "*";
    
    private Integer id;
    private String type;
    private Map<String, String> attributes = new HashMap<String, String>();
    private ItemDTOEx planItem;
    private Integer precedence;
    private BigDecimal rate;
    private BigDecimal includedQuantity;
    private boolean defaultPricing = false;

    public PriceModelWS() {
    }

    public PriceModelWS(PriceModelDTO planPrice, ItemDTOEx planItem) {
        this.id = planPrice.getId();
        this.type = planPrice.getType().name();
        this.attributes = new HashMap<String,String>(planPrice.getAttributes());
        this.planItem = planItem;
        this.precedence = planPrice.getPrecedence();
        this.rate = planPrice.getRate();
        this.includedQuantity = planPrice.getIncludedQuantity();
        this.defaultPricing = planPrice.isDefaultPricing();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }

    public ItemDTOEx getPlanItem() {
        return planItem;
    }

    public void setPlanItem(ItemDTOEx planItem) {
        this.planItem = planItem;
    }

    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getIncludedQuantity() {
        return includedQuantity;
    }

    public void setIncludedQuantity(BigDecimal includedQuantity) {
        this.includedQuantity = includedQuantity;
    }

    public boolean isDefaultPricing() {
        return defaultPricing;
    }

    public void setDefaultPricing(boolean defaultPricing) {
        this.defaultPricing = defaultPricing;
    }

    @Override
    public String toString() {
        return "PriceModelWS{"
                + "id=" + id
                + ", type='" + type + '\''
                + ", attributes=" + attributes
                + ", planItemId=" + (planItem != null ? planItem.getId() : null)
                + ", precedence=" + precedence
                + ", rate=" + rate
                + ", includedQuantity=" + includedQuantity
                + ", defaultPricing=" + defaultPricing
                + '}';
    }
}
