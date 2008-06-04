/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.item;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.entity.ItemDTO;

/**
 * @author emilc
 * @jboss-net.xml-schema urn="sapienter:ItemDTOEx"
 */
public final class ItemDTOEx extends ItemDTO implements Serializable {
    private static final Logger LOG = Logger.getLogger(ItemDTOEx.class);
    
    private String description = null;
    private Integer[] types = null;
    private String promoCode = null;
    private Integer entityId = null;
    private Integer currencyId = null;
    private Float price = null;
    private Integer orderLineTypeId = null;
    // all the prices.ItemPriceDTOEx  
    private Vector prices = null;
    
    public ItemDTOEx(Integer id,String number, Integer entity, 
            String description,
            Integer manualPrice, Integer deleted, Integer currencyId,
            Float price, Float percentage, Integer orderLineTypeId) {
        super(id, number, percentage, manualPrice ,deleted);
        setEntityId(entity);
        setDescription(description);
        setCurrencyId(currencyId);
        setPrice(price);
        setOrderLineTypeId(orderLineTypeId);
        //types = new Vector();
    }
    
    public ItemDTOEx() {
        super();
        //types = new Vector();
    }
    
    /**
     * Returns the description.
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return
     */
    public Integer[] getTypes() {
        return types;
    }

    /*
     * Rules only work on collections of strings (oparator contains)
     */
    public Collection<String> getStrTypes() {
        Vector<String> retValue = new Vector<String>();
        for (Integer i: types) {
            retValue.add(i.toString());
        }
        return retValue;
    }

    /**
     * @param vector
     */
    public void setTypes(Integer[] vector) {
        types = vector;
    }

    /**
     * @return
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * @param string
     */
    public void setPromoCode(String string) {
        promoCode = string;
    }

    /**
     * @return
     */
    public Integer getEntityId() {
        return entityId;
    }

    /**
     * @param integer
     */
    public void setEntityId(Integer integer) {
        entityId = integer;
    }

    /**
     * @return
     */
    public Integer getOrderLineTypeId() {
        return orderLineTypeId;
    }

    /**
     * @param integer
     */
    public void setOrderLineTypeId(Integer typeId) {
        orderLineTypeId = typeId;
    }

    /**
     * @return
     */
    public Integer getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId
     */
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return
     */
    public Float getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(Float price) {
        this.price = price;
    }
    /**
     * @return
     */
    public Vector getPrices() {
        return prices;
    }

    /**
     * @param prices
     */
    public void setPrices(Vector prices) {
        this.prices = prices;
    }

}
