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
 * Created on Nov 6, 2004
 *
 */
package com.sapienter.jbilling.server.invoice;

import java.io.Serializable;

import com.sapienter.jbilling.server.entity.InvoiceLineDTO;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:InvoiceLineDTOEx"
 */
public class InvoiceLineDTOEx extends InvoiceLineDTO implements Serializable {

    private Integer typeId = null;
    private Integer orderPosition = null;
    
    /**
     * 
     */
    public InvoiceLineDTOEx() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param description
     * @param amount
     * @param price
     * @param quantity
     * @param typeId
     * @param deleted
     * @param itemId
     */
    public InvoiceLineDTOEx(Integer id, String description, Float amount,
            Float price, Integer quantity, Integer typeId, Integer deleted,
            Integer itemId, Integer sourceUserId) {
        super(id, description, amount, price, quantity, deleted, itemId, 
                sourceUserId);
        setTypeId(typeId);
    }

    /**
     * @param otherValue
     */
    public InvoiceLineDTOEx(InvoiceLineDTO otherValue) {
        super(otherValue);
        // TODO Auto-generated constructor stub
    }

    public Integer getOrderPosition() {
        return orderPosition;
    }
    public void setOrderPosition(Integer orderPosition) {
        this.orderPosition = orderPosition;
    }
    public Integer getTypeId() {
        return typeId;
    }
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    
    public String toString() {
        return "type " + typeId + " position " + orderPosition + " "
                + super.toString(); 
    }
}
