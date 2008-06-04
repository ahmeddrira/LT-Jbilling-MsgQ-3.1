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
 * Created on Jan 1, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.payment;

import java.io.Serializable;
import java.util.Date;

import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.entity.PaymentDTO;
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:PaymentWS"
 */
public class PaymentWS extends PaymentDTO implements Serializable {

    private Integer userId = null;
    private PaymentInfoChequeDTO cheque = null;
    private CreditCardDTO creditCard = null;
    private AchDTO ach = null;
    private String method = null;
    private Integer invoiceIds[] = null;
    // refund specific fields
    private Integer paymentId = null; // this is the payment refunded / to refund
    private PaymentAuthorizationDTO authorization = null; 

    public PaymentWS(PaymentDTOEx dto) {
        super(dto);
        userId = dto.getUserId();
        cheque = dto.getCheque();
        creditCard = dto.getCreditCard();
        method = dto.getMethod();
        ach = dto.getAch();
        invoiceIds = new Integer[dto.getInvoiceIds().size()];
        
        for (int f = 0; f < dto.getInvoiceIds().size(); f++) {
            invoiceIds[f] = (Integer) dto.getInvoiceIds().get(f);
        }
        
        if (dto.getPayment() != null) {
            paymentId = dto.getPayment().getId();
        } else {
            paymentId = null;
        }
        
        authorization = dto.getAuthorization();
    }
    /**
     * 
     */
    public PaymentWS() {
        super();
    }

    /**
     * @param id
     * @param amount
     * @param createDateTime
     * @param paymentDate
     * @param attempt
     * @param deleted
     * @param methodId
     * @param resultId
     * @param isRefund
     */
    public PaymentWS(Integer id, Float amount, Date createDateTime,
            Date paymentDate, Integer attempt, Integer deleted,
            Integer methodId, Integer resultId, Integer isRefund,
            Integer isPreauth,
            Integer currencyId, Float balance) {
        super(id, amount, balance, createDateTime, null, paymentDate, attempt,
                deleted, methodId, resultId, isRefund, isPreauth, currencyId);
    }

    /**
     * @param otherValue
     */
    public PaymentWS(PaymentDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public PaymentInfoChequeDTO getCheque() {
        return cheque;
    }

    /**
     * @param cheque
     */
    public void setCheque(PaymentInfoChequeDTO cheque) {
        this.cheque = cheque;
    }

    /**
     * @return
     */
    public CreditCardDTO getCreditCard() {
        return creditCard;
    }

    /**
     * @param creditCard
     */
    public void setCreditCard(CreditCardDTO creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * @return
     */
    public Integer[] getInvoiceIds() {
        return invoiceIds;
    }

    /**
     * @param invoiceIds
     */
    public void setInvoiceIds(Integer[] invoiceIds) {
        this.invoiceIds = invoiceIds;
    }

    /**
     * @return
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return
     */
    public PaymentAuthorizationDTO getAuthorization() {
        return authorization;
    }

    /**
     * @param authorization
     */
    public void setAuthorization(PaymentAuthorizationDTO authorization) {
        this.authorization = authorization;
    }

    /**
     * @return
     */
    public Integer getPaymentId() {
        return paymentId;
    }

    /**
     * @param payment
     */
    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

	/**
	 * @return Returns the ach.
	 */
	public AchDTO getAch() {
		return ach;
	}
	/**
	 * @param ach The ach to set.
	 */
	public void setAch(AchDTO ach) {
		this.ach = ach;
	}
}
