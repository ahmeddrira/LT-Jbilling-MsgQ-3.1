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

/*
 * Created on Apr 7, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.user;

import com.sapienter.jbilling.server.entity.CustomerDTO;

/**
 * @author Emil
 */
public class CustomerDTOEx extends CustomerDTO {

    private Integer partnerId = null;
    private Integer parentId = null;
    private Integer totalSubAccounts = null;
    /**
     * 
     */
    public CustomerDTOEx() {
        super();
    }

    /**
     * @param id
     * @param referralFeePaid
     */
    public CustomerDTOEx(Integer id, Integer referralFeePaid, 
            String notes, Integer deliveryMethodId, Integer autoPaymentType,
            Integer dueDateUnitId, Integer dueDateValue, Integer isParent, 
            Integer excludeAging, Integer invoiceChild, Integer currentOrderId ) {
        super(id, referralFeePaid, notes, deliveryMethodId, autoPaymentType,
                dueDateUnitId, dueDateValue, null, isParent, excludeAging,
                invoiceChild, currentOrderId);
    }

    /**
     * @param otherValue
     */
    public CustomerDTOEx(CustomerDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public Integer getPartnerId() {
        return partnerId;
    }

    /**
     * @param partnerId
     */
    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public Integer getTotalSubAccounts() {
        return totalSubAccounts;
    }
    public void setTotalSubAccounts(Integer totalSubAccounts) {
        this.totalSubAccounts = totalSubAccounts;
    }
}
