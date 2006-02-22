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
public class OrderLineWS extends OrderLineDTO implements Serializable {

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
        Date createDate,
        Integer deleted) {
        super(
            id,
            itemId,
            description,
            amount,
            quantity,
            price,
            itemPrice,
            createDate,
            deleted);
        // TODO Auto-generated constructor stub
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
