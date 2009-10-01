/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.invoice;

import com.sapienter.jbilling.server.entity.InvoiceLineDTO;
import java.io.Serializable;


/**
 * @author Emil
 */
public class InvoiceWS implements Serializable {

    private Integer delegatedInvoiceId = null;
    private Integer payments[] = null;
    private Integer userId = null;
    private InvoiceLineDTO invoiceLines[] = null;
    private Integer orders[] = null;
    // original DTO
    private java.lang.Integer id;
    private java.util.Date createDateTime;
    private java.util.Date createTimeStamp;
    private java.util.Date lastReminder;
    private java.util.Date dueDate;
    private java.lang.Float total;
    private java.lang.Integer toProcess;
    private java.lang.Integer statusId;
    private java.lang.Float balance;
    private java.lang.Float carriedBalance;
    private java.lang.Integer inProcessPayment;
    private java.lang.Integer deleted;
    private java.lang.Integer paymentAttempts;
    private java.lang.Integer isReview;
    private java.lang.Integer currencyId;
    private java.lang.String customerNotes;
    private java.lang.String number;
    private java.lang.Integer overdueStep;

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    public java.util.Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(java.util.Date createDateTime) {
        this.createDateTime = createDateTime;

    }


    public java.util.Date getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public void setCreateTimeStamp(java.util.Date createTimeStamp) {
        this.createTimeStamp = createTimeStamp;

    }

    public java.util.Date getLastReminder() {
        return this.lastReminder;
    }

    public void setLastReminder(java.util.Date lastReminder) {
        this.lastReminder = lastReminder;

    }

    public java.util.Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(java.util.Date dueDate) {
        this.dueDate = dueDate;

    }

    public java.lang.Float getTotal() {
        return this.total;
    }

    public void setTotal(java.lang.Float total) {
        this.total = total;

    }

    public java.lang.Integer getToProcess() {
        return this.toProcess;
    }

    public void setToProcess(java.lang.Integer toProcess) {
        this.toProcess = toProcess;

    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public java.lang.Float getBalance() {
        return this.balance;
    }

    public void setBalance(java.lang.Float balance) {
        this.balance = balance;

    }

    public java.lang.Float getCarriedBalance() {
        return this.carriedBalance;
    }

    public void setCarriedBalance(java.lang.Float carriedBalance) {
        this.carriedBalance = carriedBalance;

    }

    public java.lang.Integer getInProcessPayment() {
        return this.inProcessPayment;
    }

    public void setInProcessPayment(java.lang.Integer inProcessPayment) {
        this.inProcessPayment = inProcessPayment;

    }

    public java.lang.Integer getDeleted() {
        return this.deleted;
    }

    public void setDeleted(java.lang.Integer deleted) {
        this.deleted = deleted;

    }

    public java.lang.Integer getPaymentAttempts() {
        return this.paymentAttempts;
    }

    public void setPaymentAttempts(java.lang.Integer paymentAttempts) {
        this.paymentAttempts = paymentAttempts;

    }

    public java.lang.Integer getIsReview() {
        return this.isReview;
    }

    public void setIsReview(java.lang.Integer isReview) {
        this.isReview = isReview;

    }

    public java.lang.Integer getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(java.lang.Integer currencyId) {
        this.currencyId = currencyId;

    }

    public java.lang.String getCustomerNotes() {
        return this.customerNotes;
    }

    public void setCustomerNotes(java.lang.String customerNotes) {
        this.customerNotes = customerNotes;

    }

    public java.lang.String getNumber() {
        return this.number;
    }

    public void setNumber(java.lang.String number) {
        this.number = number;

    }

    public java.lang.Integer getOverdueStep() {
        return this.overdueStep;
    }

    public void setOverdueStep(java.lang.Integer overdueStep) {
        this.overdueStep = overdueStep;

    }

    public java.lang.Integer getUserId() {
        return this.userId;
    }

    public void setUserId(java.lang.Integer userId) {
        this.userId = userId;

    }

    public String toString() {
        StringBuffer str = new StringBuffer();

        str.append("delegated invoice = [" + delegatedInvoiceId + "]");
        str.append(" payments = ");
        if (payments != null) {
            for (int f = 0; f < payments.length; f++) {
                str.append(payments[f].toString());
            }
        }
        str.append(" userId = [" + userId);
        str.append(" invoiceLines = ");

        if (invoiceLines != null) {
            for (InvoiceLineDTO line : invoiceLines) {
                str.append(line.toString());
            }
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
     * @param otherValue
     */

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

}
