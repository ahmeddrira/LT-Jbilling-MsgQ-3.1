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
package com.sapienter.jbilling.server.util.db.generated;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sapienter.jbilling.server.invoice.db.Invoice;

@Entity
@Table(name="payment_invoice"
    , uniqueConstraints = @UniqueConstraint(columnNames="id") 
)
public class PaymentInvoice  implements java.io.Serializable {


     private PaymentInvoiceId id;
     private Payment payment;
     private Invoice invoice;

    public PaymentInvoice() {
    }

	
    public PaymentInvoice(PaymentInvoiceId id) {
        this.id = id;
    }
    public PaymentInvoice(PaymentInvoiceId id, Payment payment, Invoice invoice) {
       this.id = id;
       this.payment = payment;
       this.invoice = invoice;
    }
   
     @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="paymentId", column=@Column(name="payment_id") ), 
        @AttributeOverride(name="invoiceId", column=@Column(name="invoice_id") ), 
        @AttributeOverride(name="amount", column=@Column(name="amount", precision=17, scale=17) ), 
        @AttributeOverride(name="createDatetime", column=@Column(name="create_datetime", length=29) ), 
        @AttributeOverride(name="id", column=@Column(name="id", unique=true, nullable=false) ) } )
    public PaymentInvoiceId getId() {
        return this.id;
    }
    
    public void setId(PaymentInvoiceId id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="payment_id", insertable=false, updatable=false)
    public Payment getPayment() {
        return this.payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="invoice_id", insertable=false, updatable=false)
    public Invoice getInvoice() {
        return this.invoice;
    }
    
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }




}


