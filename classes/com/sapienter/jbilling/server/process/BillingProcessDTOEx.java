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
 * Created on Oct 7, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.process;

import java.util.Date;
import java.util.Vector;

import com.sapienter.jbilling.server.entity.BillingProcessDTO;

/**
 * @author Emil
 */
public class BillingProcessDTOEx extends BillingProcessDTO {

    private Vector<BillingProcessRunDTOEx> runs = null; 
    private BillingProcessRunDTOEx grandTotal = null;
    private Integer retries = null;
    private Date billingDateEnd = null;

    // the number of orders included in this process
    private Integer ordersProcessed = null;

    /**
     * 
     */
    public BillingProcessDTOEx() {
        super();
    }

    /**
     * @param id
     * @param entityId
     * @param billingDate
     * @param periodUnitId
     * @param periodValue
     */
    public BillingProcessDTOEx(Integer id, Integer entityId, Date billingDate,
            Integer periodUnitId, Integer periodValue, Integer isReview,
            Integer retries) {
        super(id, entityId, billingDate, periodUnitId, periodValue, isReview,
                retries);
    }

    /**
     * @param otherValue
     */
    public BillingProcessDTOEx(BillingProcessDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public Vector getRuns() {
        return runs;
    }

    /**
     * @param runs
     */
    public void setRuns(Vector runs) {
        this.runs = runs;
    }

    /**
     * @return
     */
    public BillingProcessRunDTOEx getGrandTotal() {
        return grandTotal;
    }

    /**
     * @param grandTotal
     */
    public void setGrandTotal(BillingProcessRunDTOEx grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Date getBillingDateEnd() {
        return billingDateEnd;
    }

    public void setBillingDateEnd(Date billingDateEnd) {
        this.billingDateEnd = billingDateEnd;
    }
    
    /**
     * The process will go over orders. This will happen only in the first run.
     * Subsequent runs will go over only invoices, not orders. Thus, we include
     * the number of orders in the process instead of the run
     * @return
     */
    public Integer getOrdersProcessed() {
        return ordersProcessed;
    }

    /**
     * @param ordersProcessed
     */
    public void setOrdersProcessed(Integer ordersProcessed) {
        this.ordersProcessed = ordersProcessed;
    }    

}
