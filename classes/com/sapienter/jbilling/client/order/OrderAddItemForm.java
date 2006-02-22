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
