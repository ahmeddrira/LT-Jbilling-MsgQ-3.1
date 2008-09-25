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


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sapienter.jbilling.server.invoice.db.Invoice;
import com.sapienter.jbilling.server.process.db.BillingProcessDTO;

@Entity
@Table(name="paper_invoice_batch")
public class PaperInvoiceBatch  implements java.io.Serializable {


     private int id;
     private int totalInvoices;
     private Date deliveryDate;
     private short isSelfManaged;
     private Set<BillingProcessDTO> billingProcesses = new HashSet<BillingProcessDTO>(0);
     private Set<Invoice> invoices = new HashSet<Invoice>(0);

    public PaperInvoiceBatch() {
    }

	
    public PaperInvoiceBatch(int id, int totalInvoices, short isSelfManaged) {
        this.id = id;
        this.totalInvoices = totalInvoices;
        this.isSelfManaged = isSelfManaged;
    }
    public PaperInvoiceBatch(int id, int totalInvoices, Date deliveryDate, short isSelfManaged, Set<BillingProcessDTO> billingProcesses, Set<Invoice> invoices) {
       this.id = id;
       this.totalInvoices = totalInvoices;
       this.deliveryDate = deliveryDate;
       this.isSelfManaged = isSelfManaged;
       this.billingProcesses = billingProcesses;
       this.invoices = invoices;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="total_invoices", nullable=false)
    public int getTotalInvoices() {
        return this.totalInvoices;
    }
    
    public void setTotalInvoices(int totalInvoices) {
        this.totalInvoices = totalInvoices;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="delivery_date", length=13)
    public Date getDeliveryDate() {
        return this.deliveryDate;
    }
    
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    @Column(name="is_self_managed", nullable=false)
    public short getIsSelfManaged() {
        return this.isSelfManaged;
    }
    
    public void setIsSelfManaged(short isSelfManaged) {
        this.isSelfManaged = isSelfManaged;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="paperInvoiceBatch")
    public Set<BillingProcessDTO> getBillingProcesses() {
        return this.billingProcesses;
    }
    
    public void setBillingProcesses(Set<BillingProcessDTO> billingProcesses) {
        this.billingProcesses = billingProcesses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="paperInvoiceBatch")
    public Set<Invoice> getInvoices() {
        return this.invoices;
    }
    
    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }




}


