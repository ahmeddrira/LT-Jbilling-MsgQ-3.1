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
 * Created on Apr 6, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.user;

import java.util.Date;

import com.sapienter.jbilling.server.entity.PartnerDTO;
import com.sapienter.jbilling.server.entity.PartnerRangeDTO;

/**
 * @author Emil
 */
public class PartnerDTOEx extends PartnerDTO {
    private PartnerPayoutDTOEx payouts[] = null;
    private Integer relatedClerkUserId = null;
    private PartnerRangeDTO[] ranges = null;
    /**
     * 
     */
    public PartnerDTOEx() {
        super();
    }

    /**
     * @param id
     * @param balance
     * @param totalPayments
     * @param totalRefunds
     * @param totalPayouts
     * @param percentageRate
     * @param referralFee
     * @param feeCurrencyId
     * @param oneTime
     * @param periodUnitId
     * @param periodValue
     * @param nextPayoutDate
     * @param duePayout
     * @param automaticProcess
     */
    public PartnerDTOEx(
        Integer id,
        Float balance,
        Float totalPayments,
        Float totalRefunds,
        Float totalPayouts,
        Float percentageRate,
        Float referralFee,
        Integer feeCurrencyId,
        Integer oneTime,
        Integer periodUnitId,
        Integer periodValue,
        Date nextPayoutDate,
        Float duePayout,
        Integer automaticProcess) {
        super(
            id,
            balance,
            totalPayments,
            totalRefunds,
            totalPayouts,
            percentageRate,
            referralFee,
            feeCurrencyId,
            oneTime,
            periodUnitId,
            periodValue,
            nextPayoutDate,
            duePayout,
            automaticProcess);
    }

    /**
     * @param otherValue
     */
    public PartnerDTOEx(PartnerDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public PartnerPayoutDTOEx[] getPayouts() {
        return payouts;
    }

    /**
     * @param payouts
     */
    public void setPayouts(PartnerPayoutDTOEx[] payouts) {
        this.payouts = payouts;
    }

    /**
     * @return
     */
    public Integer getRelatedClerkUserId() {
        return relatedClerkUserId;
    }

    /**
     * @param relatedClerckUserId
     */
    public void setRelatedClerkUserId(Integer relatedClerckUserId) {
        this.relatedClerkUserId = relatedClerckUserId;
    }


    public PartnerRangeDTO[] getRanges() {
        return ranges;
    }
    public void setRanges(PartnerRangeDTO[] ranges) {
        this.ranges = ranges;
    }
    
    /**
     * validate that the ranges start from 1, have no superpositions and
     * no gaps
     * @return
     *  0 = ok
     *  2 = range end <= range start
     *  3 = gap
     */
    public int validateRanges() {
        int retValue = 0;
        int last = 0;
        
        for (int f = 0; f < ranges.length; f++) {
            if (ranges[f].getRangeTo().intValue() <=
                ranges[f].getRangeFrom().intValue()) {
                retValue = 2;
            } else if (ranges[f].getRangeFrom().intValue() != last + 1) {
                    retValue = 3;
            } else {
                last = ranges[f].getRangeTo().intValue();
            }
            if (retValue != 0)
                break;
        }
        return retValue;
    }
}
