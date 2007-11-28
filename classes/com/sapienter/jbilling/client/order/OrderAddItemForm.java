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

package com.sapienter.jbilling.client.order;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author emilc
 *
 */
public class OrderAddItemForm extends ValidatorForm implements Serializable {
    private Integer quantity = null;
    private Integer itemID = null;
    /**
     * Returns the itemID.
     * @return Integer
     */
    public Integer getItemID() {
        return itemID;
    }

    /**
     * Returns the quantity.
     * @return Integer
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the itemID.
     * @param itemID The itemID to set
     */
    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    /**
     * Sets the quantity.
     * @param quantity The quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        itemID = null;
        quantity = null;
    }

}
