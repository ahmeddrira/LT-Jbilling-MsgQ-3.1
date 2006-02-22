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
            Integer excludeAging ) {
        super(id, referralFeePaid, notes, deliveryMethodId, autoPaymentType,
                dueDateUnitId, dueDateValue, null, isParent, excludeAging);
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
