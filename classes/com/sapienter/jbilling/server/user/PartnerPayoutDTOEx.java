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
