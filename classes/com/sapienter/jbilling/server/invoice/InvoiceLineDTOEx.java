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
        // default to non-percentage
        setIsPercentage(new Integer(0));
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
            Float price, Double quantity, Integer typeId, Integer deleted,
            Integer itemId, Integer sourceUserId, Integer isPercentage) {
        super(id, description, amount, price, quantity, deleted, itemId, 
                sourceUserId, isPercentage);
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
    
    public void setQuantity( Integer quantity ) {
    	setQuantity( new Double(quantity) );
    }
    
    public String toString() {
        return "type " + typeId + " position " + orderPosition + " "
                + super.toString(); 
    }
}
