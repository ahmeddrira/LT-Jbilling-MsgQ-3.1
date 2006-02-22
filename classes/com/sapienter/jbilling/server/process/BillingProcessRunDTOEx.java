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

    Vector totals = null;
    /**
     * 
     */
    public BillingProcessRunDTOEx() {
        super();
        totals = new Vector();
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
            Date finished, Integer invoiceGenerated) {
        super(id, runDate, started, finished, invoiceGenerated);
        totals = new Vector();
        
    }

    public Vector getTotals() {
        return totals;
    }

    public void setTotals(Vector totals) {
        this.totals = totals;
    }

}
