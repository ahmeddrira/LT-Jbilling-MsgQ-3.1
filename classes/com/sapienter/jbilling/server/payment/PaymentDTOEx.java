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

package com.sapienter.jbilling.server.payment;

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.entity.AchDTO;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.entity.PaymentDTO;
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;

public class PaymentDTOEx extends PaymentDTO {
    
    private Integer userId = null;
    private PaymentInfoChequeDTO cheque = null;
    private AchDTO ach = null;
    private CreditCardDTO creditCard = null;
    private String method = null;
    private Vector invoiceIds = null;
    private Vector paymentMaps = null;
    private PaymentDTOEx payment = null; // for refunds
    private String resultStr = null;
    private Integer payoutId = null;
    // now we only support one of these
    private PaymentAuthorizationDTO authorization = null; // useful in refuds

    public PaymentDTOEx(PaymentWS dto) {
        super(dto);
        userId = dto.getUserId();
        cheque = dto.getCheque();
        creditCard = dto.getCreditCard();
        method = dto.getMethod();
        ach = dto.getAch();
        invoiceIds = new Vector();
        paymentMaps = new Vector();
        
        if (dto.getInvoiceIds() != null) {
            for (int f = 0; f < dto.getInvoiceIds().length; f++) {
                invoiceIds.add(dto.getInvoiceIds()[f]);
            }
        }
        
        if (dto.getPaymentId() != null) {
            payment = new PaymentDTOEx();
            payment.setId(dto.getPaymentId());
        } else {
            payment = null;
        }
        
        authorization = dto.getAuthorization();
            
    }    
    /**
     * 
     */
    public PaymentDTOEx() {
        super();
        invoiceIds = new Vector();
        paymentMaps = new Vector();
    }

    /**
     * @param id
     * @param amount
     * @param createDateTime
     * @param attempt
     * @param deleted
     * @param methodId
     */
    public PaymentDTOEx(Integer id, Float amount, Date createDateTime,
            Date updateDateTime,
            Date paymentDate, Integer attempt, Integer deleted,
            Integer methodId, Integer resultId, Integer isRefund,
            Integer isPreauth, Integer currencyId, Float balance) {
        super(id, amount, balance, createDateTime, updateDateTime,
                paymentDate, attempt, deleted, methodId, resultId, isRefund, 
                isPreauth, currencyId);
        invoiceIds = new Vector();
        paymentMaps = new Vector();
    }

    /**
     * @param otherValue
     */
    public PaymentDTOEx(PaymentDTO otherValue) {
        super(otherValue);
        invoiceIds = new Vector();
        paymentMaps = new Vector();
    }

    public boolean validate() {
        boolean retValue = true;
        
        // check some mandatory fields
        if (getMethodId() == null || getResultId() == null) {
            retValue = false;
        }
        
        return retValue;
    }
    
    public String toString() {
        
        StringBuffer maps = new StringBuffer();
        if (paymentMaps != null) {
            for (int f = 0; f < paymentMaps.size(); f++) {
                maps.append(paymentMaps.get(f).toString());
                maps.append(" - ");
            }
        }

        // had to repeat this code :( To exclude the number
        StringBuffer cc = new StringBuffer("{");
        if (creditCard != null) {
            cc.append("id=" + creditCard.getId() + " " + "expiry="
                    + creditCard.getExpiry() + " " + "name="
                    + creditCard.getName() + " " + "type="
                    + creditCard.getType() + " " + "deleted="
                    + creditCard.getDeleted() + " " + "securityCode="
                    + creditCard.getSecurityCode());
        }        
        cc.append('}');

        
        return super.toString() + " credit card:" + cc.toString() + 
            " cheque:" + cheque + " payment maps:" + maps.toString();
    } 
    /**
     * @return
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param integer
     */
    public void setUserId(Integer integer) {
        userId = integer;
    }

 
    /**
     * @return
     */
    public PaymentInfoChequeDTO getCheque() {
        return cheque;
    }

    /**
     * @param chequeDTO
     */
    public void setCheque(PaymentInfoChequeDTO chequeDTO) {
        cheque = chequeDTO;
    }

    /**
     * @return
     */
    public CreditCardDTO getCreditCard() {
        return creditCard;
    }

    /**
     * @param cardDTO
     */
    public void setCreditCard(CreditCardDTO cardDTO) {
        creditCard = cardDTO;
    }

    /**
     * @return
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param string
     */
    public void setMethod(String string) {
        method = string;
    }


    /**
     * @return
     */
    public Vector getInvoiceIds() {
        return invoiceIds;
    }

    /**
     * @param vector
     */
    public void setInvoiceIds(Vector vector) {
        invoiceIds = vector;
    }

    /**
     * @return
     */
    public PaymentDTOEx getPayment() {
        return payment;
    }

    /**
     * @param ex
     */
    public void setPayment(PaymentDTOEx ex) {
        payment = ex;
    }

    /**
     * @return
     */
    public PaymentAuthorizationDTO getAuthorization() {
        Logger.getLogger(PaymentDTOEx.class).debug("Returning " + 
                authorization + " for payemnt " + getId());
        return authorization;
    }

    /**
     * @param authorizationDTO
     */
    public void setAuthorization(PaymentAuthorizationDTO authorizationDTO) {
        authorization = authorizationDTO;
    }

    /**
     * @return
     */
    public String getResultStr() {
        return resultStr;
    }

    /**
     * @param resultStr
     */
    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    /**
     * @return
     */
    public Integer getPayoutId() {
        return payoutId;
    }

    /**
     * @param payoutId
     */
    public void setPayoutId(Integer payoutId) {
        this.payoutId = payoutId;
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
    public Vector getPaymentMaps() {
        Logger.getLogger(PaymentDTOEx.class).debug("Returning " + 
                paymentMaps.size() + " elements in the map");
        return paymentMaps;
    }
    
    public void addPaymentMap(PaymentInvoiceMapDTOEx map) {
        Logger.getLogger(PaymentDTOEx.class).debug("Adding map to the vector ");
        paymentMaps.add(map);
    }
}
