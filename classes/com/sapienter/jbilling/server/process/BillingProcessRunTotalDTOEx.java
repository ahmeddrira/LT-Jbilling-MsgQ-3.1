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
