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
