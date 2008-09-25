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
