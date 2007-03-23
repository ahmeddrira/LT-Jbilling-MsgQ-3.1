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
 * Created on Mar 12, 2004
 *
 */
package com.sapienter.jbilling.server.item;

import java.io.Serializable;
import com.sapienter.jbilling.server.entity.ItemPriceDTO;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:ItemPriceDTOEx"
 */
public class ItemPriceDTOEx extends ItemPriceDTO implements Serializable {

    private String name = null;
    // this is useful for the form, exposing a Float is trouble
    private String priceForm = null;
    /**
     * 
     */
    public ItemPriceDTOEx() {
        super();
    }

    /**
     * @param id
     * @param price
     * @param currencyId
     */
    public ItemPriceDTOEx(Integer id, Float price, Integer currencyId) {
        super(id, price, currencyId);
    }

    /**
     * @param otherValue
     */
    public ItemPriceDTOEx(ItemPriceDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return "name = " + name + " priceForm = " + priceForm + super.toString();
    }

    /**
     * @return
     */
    public String getPriceForm() {
        return priceForm;
    }

    /**
     * @param priceForm
     */
    public void setPriceForm(String priceForm) {
        this.priceForm = priceForm;
    }

}
