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
 * Created on Apr 3, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.user;

import java.math.BigDecimal;
import java.util.Date;

import com.sapienter.jbilling.server.entity.PartnerPayoutDTO;
import com.sapienter.jbilling.server.entity.PaymentDTO;

/**
 * @author Emil
 */
public class PartnerPayoutDTOEx extends PartnerPayoutDTO {

    private Integer paymentId = null;
    private Integer partnerId = null;
    private PaymentDTO payment = null;
    /**
     * 
     */
    public PartnerPayoutDTOEx() {
        super();
        payment = new PaymentDTO();
    }

    /**
     * @param id
     * @param endingDate
     * @param paymentsAmount
     * @param refundsAmount
     * @param balanceLeft
     */
    public PartnerPayoutDTOEx(
        Integer id,
        Date startinDate,
        Date endingDate,
        Float paymentsAmount,
        Float refundsAmount,
        Float balanceLeft) {
        super(id,startinDate,  endingDate, paymentsAmount, refundsAmount, balanceLeft);
        payment = new PaymentDTO();
    }

    /**
     * @param otherValue
     */
    public PartnerPayoutDTOEx(PartnerPayoutDTO otherValue) {
        super(otherValue);
        payment = new PaymentDTO();
    }

    /**
     * @return
     */
    public Integer getPaymentId() {
        return paymentId;
    }

    /**
     * @param paymentId
     */
    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
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

    /**
     * @return
     */
    public PaymentDTO getPayment() {
        return payment;
    }

    /**
     * @param payment
     */
    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }
    
    public Float getTotal() {
        BigDecimal balance = (getBalanceLeft() == null) ? new BigDecimal("0") : 
                new BigDecimal(getBalanceLeft().toString());
        return new Float(balance.add(new BigDecimal(getPayment().getAmount().toString())).floatValue());
    }
}
