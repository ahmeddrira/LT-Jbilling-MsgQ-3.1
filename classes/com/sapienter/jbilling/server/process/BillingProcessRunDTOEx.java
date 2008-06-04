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
 * Created on Oct 12, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.process;

import java.util.Date;
import java.util.Vector;

import com.sapienter.jbilling.server.entity.BillingProcessRunDTO;

/**
 * @author Emil
 */
public class BillingProcessRunDTOEx extends BillingProcessRunDTO {

    Vector<BillingProcessRunTotalDTOEx> totals = null;
    /**
     * 
     */
    public BillingProcessRunDTOEx() {
        super();
        totals = new Vector<BillingProcessRunTotalDTOEx>();
        setInvoiceGenerated(new Integer(0));
    }

    /**
     * @param id
     * @param tryNumber
     * @param started
     * @param finished
     * @param invoiceGenerated
     * @param totalInvoiced
     * @param totalPaid
     * @param totalNotPaid
     */
    public BillingProcessRunDTOEx(Integer id, Date runDate, Date started,
            Date finished, Date paymentFinished, Integer invoiceGenerated) {
        super(id, runDate, started, finished, paymentFinished, invoiceGenerated);
        totals = new Vector<BillingProcessRunTotalDTOEx>();
        
    }

    public Vector<BillingProcessRunTotalDTOEx> getTotals() {
        return totals;
    }

    public void setTotals(Vector<BillingProcessRunTotalDTOEx> totals) {
        this.totals = totals;
    }

}
