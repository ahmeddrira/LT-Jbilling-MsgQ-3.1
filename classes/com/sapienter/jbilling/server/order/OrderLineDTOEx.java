/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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

package com.sapienter.jbilling.server.order;

import java.util.Date;

import com.sapienter.jbilling.server.entity.OrderLineDTO;
import com.sapienter.jbilling.server.item.ItemDTOEx;

/**
 * Extension of the generated DTO to add the CMR fields
 * @author emilc
 * @jboss-net.xml-schema urn="sapienter:OrderLineDTOEx"
 */
public class OrderLineDTOEx extends OrderLineDTO {

    private Integer typeId = null;
    private Boolean editable = null;
    private ItemDTOEx item = null;
    private String priceStr = null;
    private Boolean totalReadOnly = null;

    /**
     * Constructor for OrderLineDTOEx.
     */
    public OrderLineDTOEx() {
        super();
    }

    /**
     * Constructor for OrderLineDTOEx.
     * @param id
     * @param itemId
     * @param description
     * @param amount
     * @param quantity
     * @param price
     * @param itemPrice
     * @param deleted
     */
    public OrderLineDTOEx(
        Integer id,
        Integer itemId,
        String description,
        Float amount,
        Integer quantity,
        Float price,
        Integer itemPrice,
        Date create,
        Integer deleted,
        Integer newTypeId,
        Boolean editable) {
        super(
            id,
            itemId,
            description,
            amount,
            quantity,
            price,
            itemPrice,
            create,
            deleted);
        typeId = newTypeId;
        this.editable = editable;
    }
    
    public OrderLineDTOEx(OrderLineDTO dto) {
        super(dto);
    }

    public OrderLineDTOEx(OrderLineWS dto) {
        super(dto);
        typeId = dto.getTypeId();
        item = dto.getItem();
    }

    // Accessors to extended fields

    /**
     * Returns the typeId.
     * @return Integer
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * Sets the typeId.
     * @param typeId The typeId to set
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * Returns the editable.
     * @return Boolean
     */
    public Boolean getEditable() {
        return editable;
    }

    /**
     * Sets the editable.
     * @param editable The editable to set
     */
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    /**
     * @return
     */
    public ItemDTOEx getItem() {
        return item;
    }

    /**
     * @param ex
     */
    public void setItem(ItemDTOEx ex) {
        item = ex;
    }

    public String getPriceStr() {
        return priceStr;
    }
    public void setPriceStr(String pricesStr) {
        this.priceStr = pricesStr;
    }
    
    public String toString() {
        return super.toString() + " priceStr=" + priceStr;
    }

    public Boolean getTotalReadOnly() {
        if (totalReadOnly == null) {
            setTotalReadOnly(false);
        }
        return totalReadOnly;
    }

    public void setTotalReadOnly(Boolean finalTotal) {
        this.totalReadOnly = finalTotal;
    }
    
}
