package com.sapienter.jbilling.server.payment;

import java.util.Date;

import com.sapienter.jbilling.server.entity.PaymentInvoiceMapDTO;


public class PaymentInvoiceMapDTOEx extends PaymentInvoiceMapDTO {
    private Integer paymentId;
    private Integer invoiceId;
    private Integer currencyId;

    public PaymentInvoiceMapDTOEx(Integer id, Float amount, Date create) {
        super(id, amount, create);
    }
    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
    public Integer getCurrencyId() {
        return currencyId;
    }
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }
    
    public String toString() {
        return "id = " + getId() +
                " paymentId=" + paymentId + 
                " invoiceId=" + invoiceId +
                " currencyId=" + currencyId +
                " amount=" + getAmount() +
                " date=" + getCreateDateTime();
    }
}
