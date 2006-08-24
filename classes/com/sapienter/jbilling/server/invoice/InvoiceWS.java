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
 * Created on Jan 1, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.invoice;

import java.io.Serializable;
import java.util.Date;

import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.entity.InvoiceLineDTO;
import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.payment.PaymentInvoiceMapDTOEx;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:InvoiceWS"
 */
public class InvoiceWS extends InvoiceDTO implements Serializable {

    private Integer delegatedInvoiceId = null;
    private Integer payments[] = null;
    private Integer userId = null;
    private InvoiceLineDTO invoiceLines[] = null;
    private Integer orders[] = null; 

    public InvoiceWS(InvoiceDTOEx dto) {
        super(dto);
        delegatedInvoiceId = dto.getDelegatedInvoiceId();
        userId = dto.getUserId();
        payments = new Integer[dto.getPaymentMap().size()];
        invoiceLines = new InvoiceLineDTO[dto.getInvoiceLines().size()];
        orders = new Integer[dto.getOrders().size()];
        
        int f;
        for (f = 0; f < dto.getPaymentMap().size(); f++) {
            PaymentInvoiceMapDTOEx map = 
                    (PaymentInvoiceMapDTOEx) dto.getPaymentMap().get(f);
            payments[f] = map.getPaymentId();
        }
        for (f = 0; f < dto.getOrders().size(); f++) {
            OrderDTO order = (OrderDTO) dto.getOrders().get(f);
            orders[f] = order.getId();
        }
        for (f = 0; f < dto.getInvoiceLines().size(); f++) {
            invoiceLines[f] = (InvoiceLineDTO) dto.getInvoiceLines().get(f);
        }
    }
    
    public String toString() {
        StringBuffer str = new StringBuffer();
        
        str.append("delegated invoice = [" + delegatedInvoiceId + "]");
        str.append(" payments = ");
        for (int f = 0; f < payments.length; f++) {
            str.append(payments[f].toString());
        }
        str.append(" userId = [" + userId );
        str.append(" invoiceLines = ");
        for (int f = 0; f < invoiceLines.length; f++) {
            str.append(invoiceLines[f].toString());
        }
        str.append(" orders = ");
        for (int f = 0; f < orders.length; f++) {
            str.append(orders[f].toString());
        }
        str.append("InvoiceDTO = [" + super.toString());
        return str.toString();
    }
    /**
     * 
     */
    public InvoiceWS() {
        super();
    }

    /**
     * @param id
     * @param createDateTime
     * @param dueDate
     * @param total
     * @param toProcess
     * @param balance
     * @param inProcessPayment
     * @param deleted
     * @param paymentAttempts
     * @param isReview
     */
    public InvoiceWS(Integer id, Date createDateTime, 
            Date createTimeStamp, Date dueDate, 
            Float total, Integer toProcess, Float balance, 
            Float carriedBalance,
            Integer inProcessPayment, Integer deleted, Integer paymentAttempts,
            Integer isReview, Integer currencyId, String notes,
            String number, Date lastReminder, Integer overdueStep) {
        super(id, createDateTime, createTimeStamp, lastReminder, dueDate, total, toProcess, balance,
                carriedBalance, inProcessPayment, deleted, paymentAttempts, 
                isReview, currencyId, notes, number, overdueStep);
    }

    /**
     * @param otherValue
     */
    public InvoiceWS(InvoiceDTO otherValue) {
        super(otherValue);
    }

    public Integer getDelegatedInvoiceId() {
        return delegatedInvoiceId;
    }

    public void setDelegatedInvoiceId(Integer delegatedInvoiceId) {
        this.delegatedInvoiceId = delegatedInvoiceId;
    }

    public InvoiceLineDTO[] getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(InvoiceLineDTO[] invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public Integer[] getOrders() {
        return orders;
    }

    public void setOrders(Integer[] orders) {
        this.orders = orders;
    }

    public Integer[] getPayments() {
        return payments;
    }

    public void setPayments(Integer[] payments) {
        this.payments = payments;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
