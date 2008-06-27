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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sapienter.jbilling.server.invoice.db.Invoice;
import com.sapienter.jbilling.server.item.db.Item;

@Entity
@Table(name="invoice_line")
public class InvoiceLine  implements java.io.Serializable {


     private int id;
     private InvoiceLineType invoiceLineType;
     private Item item;
     private Invoice invoice;
     private double amount;
     private Double quantity;
     private Double price;
     private short deleted;
     private String description;
     private Integer sourceUserId;
     private short isPercentage;

    public InvoiceLine() {
    }

	
    public InvoiceLine(int id, double amount, short deleted, short isPercentage) {
        this.id = id;
        this.amount = amount;
        this.deleted = deleted;
        this.isPercentage = isPercentage;
    }
    public InvoiceLine(int id, InvoiceLineType invoiceLineType, Item item, Invoice invoice, double amount, Double quantity, Double price, short deleted, String description, Integer sourceUserId, short isPercentage) {
       this.id = id;
       this.invoiceLineType = invoiceLineType;
       this.item = item;
       this.invoice = invoice;
       this.amount = amount;
       this.quantity = quantity;
       this.price = price;
       this.deleted = deleted;
       this.description = description;
       this.sourceUserId = sourceUserId;
       this.isPercentage = isPercentage;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="type_id")
    public InvoiceLineType getInvoiceLineType() {
        return this.invoiceLineType;
    }
    
    public void setInvoiceLineType(InvoiceLineType invoiceLineType) {
        this.invoiceLineType = invoiceLineType;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="invoice_id")
    public Invoice getInvoice() {
        return this.invoice;
    }
    
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    
    @Column(name="amount", nullable=false, precision=17, scale=17)
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    @Column(name="quantity")
    public Double getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    
    public void setQuantity(Integer quantity) {
        setQuantity( new Double(quantity) );
    }
    
    @Column(name="price", precision=17, scale=17)
    public Double getPrice() {
        return this.price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    @Column(name="deleted", nullable=false)
    public short getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="description", length=1000)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="source_user_id")
    public Integer getSourceUserId() {
        return this.sourceUserId;
    }
    
    public void setSourceUserId(Integer sourceUserId) {
        this.sourceUserId = sourceUserId;
    }
    
    @Column(name="is_percentage", nullable=false)
    public short getIsPercentage() {
        return this.isPercentage;
    }
    
    public void setIsPercentage(short isPercentage) {
        this.isPercentage = isPercentage;
    }




}


