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

import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.TreeMap;

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
    public static final String ATTRIBUTE_WILDCARD = "*";
    
    private Integer id;
    private String type;
    private SortedMap<String, String> attributes = new TreeMap<String, String>();
    private String rate;
    private Integer currencyId;
    private PriceModelWS next;

    public PriceModelWS() {
    }

    public PriceModelWS(String type) {
        this.type = type;
    }

    public PriceModelWS(String type, BigDecimal rate, Integer currencyId) {
        this.type = type;
        this.rate = (rate != null ? rate.toString() : null);
        this.currencyId = currencyId;
    }

    public PriceModelWS(PriceModelDTO model) {
        this.id = model.getId();
        this.attributes = new TreeMap<String,String>(model.getAttributes());

        setRate(model.getRate());

        if (model.getType() != null ) this.type = model.getType().name();
        if (model.getCurrency() != null) this.currencyId = model.getCurrency().getId();
        if (model.getNext() != null) this.next = new PriceModelWS(model.getNext());
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

    public SortedMap<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(SortedMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name, String value) {
        this.attributes.put(name, value);
    }

    public String getRate() {
        return rate;
    }

    public BigDecimal getRateAsDecimal() {
        return rate != null ? new BigDecimal(rate) : null;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = (rate != null ? rate.toString() : null);
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public PriceModelWS getNext() {
        return next;
    }

    public void setNext(PriceModelWS next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "PriceModelWS{"
                + "id=" + id
                + ", type='" + type + '\''
                + ", attributes=" + attributes
                + ", rate=" + rate
                + ", currencyId=" + currencyId
                + '}';
    }
}
