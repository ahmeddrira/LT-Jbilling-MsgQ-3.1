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

/*
 * Created on Dec 30, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.order;

import java.io.Serializable;
import java.util.Date;

import com.sapienter.jbilling.server.entity.OrderLineDTO;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:OrderLineWS"
 */
public class OrderLineWS extends OrderLineDTOEx implements Serializable {

    private Integer typeId = null;
    private Boolean useItem = null;
    /**
     * 
     */
    public OrderLineWS() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param itemId
     * @param description
     * @param amount
     * @param quantity
     * @param price
     * @param itemPrice
     * @param createDate
     * @param deleted
     */
    public OrderLineWS(
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
            deleted,
            newTypeId,
            editable);
    }

    /**
     * @param otherValue
     */
    public OrderLineWS(OrderLineDTO otherValue) {
        super(otherValue);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Boolean getUseItem() {
        return useItem == null ? new Boolean(false) : useItem;
    }
    public void setUseItem(Boolean useItem) {
        this.useItem = useItem;
    }
    
    public String toString() {
        return super.toString() + " typeId = " + typeId + " useItem = " + useItem;
    }
}
