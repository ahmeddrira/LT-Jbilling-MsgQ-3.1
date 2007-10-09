/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
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
