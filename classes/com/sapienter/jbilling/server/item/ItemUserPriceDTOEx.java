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

package com.sapienter.jbilling.server.item;

import com.sapienter.jbilling.server.entity.ItemUserPriceDTO;

public class ItemUserPriceDTOEx extends ItemUserPriceDTO {

    Integer userId = null;
    Integer itemId = null;
    
    /**
     * 
     */
    public ItemUserPriceDTOEx() {
        super();
    }

    /**
     * @param id
     * @param price
     */
    public ItemUserPriceDTOEx(Integer id, Float price, Integer currencyId) {
        super(id, price, currencyId);
    }

    /**
     * @param otherValue
     */
    public ItemUserPriceDTOEx(ItemUserPriceDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @return
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param integer
     */
    public void setItemId(Integer integer) {
        itemId = integer;
    }

    /**
     * @param integer
     */
    public void setUserId(Integer integer) {
        userId = integer;
    }

}
