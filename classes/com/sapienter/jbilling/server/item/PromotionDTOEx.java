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

import java.util.Date;

import com.sapienter.jbilling.server.entity.PromotionDTO;

public class PromotionDTOEx extends PromotionDTO {

    Integer itemId = null;
    /**
     * 
     */
    public PromotionDTOEx() {
        super();
    }

    /**
     * @param id
     * @param code
     * @param notes
     * @param once
     * @param since
     * @param until
     */
    public PromotionDTOEx(
        Integer id,
        String code,
        String notes,
        Integer once,
        Date since,
        Date until, Integer itemId) {
        super(id, code, notes, once, since, until);
        this.itemId = itemId; 
    }

    /**
     * @param otherValue
     */
    public PromotionDTOEx(PromotionDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param integer
     */
    public void setItemId(Integer integer) {
        itemId = integer;
    }

}
