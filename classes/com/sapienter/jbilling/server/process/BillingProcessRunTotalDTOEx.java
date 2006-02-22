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
 * Created on Mar 17, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.process;

import java.util.Hashtable;

import com.sapienter.jbilling.server.entity.BillingProcessRunTotalDTO;

/**
 * @author Emil
 */
public class BillingProcessRunTotalDTOEx extends BillingProcessRunTotalDTO {

    private Hashtable pmTotals = null;
    private String currencyName = null;
    /**
     * 
     */
    public BillingProcessRunTotalDTOEx() {
        super();
        pmTotals = new Hashtable();
    }

    /**
     * @param id
     * @param total
     * @param currencyId
     */
    public BillingProcessRunTotalDTOEx(Integer id, Integer currencyId,
            Float totalInvoiced, Float totalPaid, Float totalNotPaid) {
        super(id, currencyId, totalInvoiced, totalPaid, totalNotPaid);
        pmTotals = new Hashtable();
    }

    /**
     * @param otherValue
     */
    public BillingProcessRunTotalDTOEx(BillingProcessRunTotalDTO otherValue) {
        super(otherValue);
        pmTotals = new Hashtable();
    }

   /**
     * @return
     */
    public Hashtable getPmTotals() {
        return pmTotals;
    }

    /**
     * @param pmTotals
     */
    public void setPmTotals(Hashtable pmTotals) {
        this.pmTotals = pmTotals;
    }

    /**
     * @return
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * @param currencyName
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

}
