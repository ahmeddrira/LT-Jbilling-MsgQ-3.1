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
