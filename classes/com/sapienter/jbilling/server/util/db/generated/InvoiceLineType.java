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


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="invoice_line_type")
public class InvoiceLineType  implements java.io.Serializable {


     private int id;
     private String description;
     private int orderPosition;
     private Set<InvoiceLine> invoiceLines = new HashSet<InvoiceLine>(0);

    public InvoiceLineType() {
    }

	
    public InvoiceLineType(int id, String description, int orderPosition) {
        this.id = id;
        this.description = description;
        this.orderPosition = orderPosition;
    }
    public InvoiceLineType(int id, String description, int orderPosition, Set<InvoiceLine> invoiceLines) {
       this.id = id;
       this.description = description;
       this.orderPosition = orderPosition;
       this.invoiceLines = invoiceLines;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="description", nullable=false, length=50)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="order_position", nullable=false)
    public int getOrderPosition() {
        return this.orderPosition;
    }
    
    public void setOrderPosition(int orderPosition) {
        this.orderPosition = orderPosition;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="invoiceLineType")
    public Set<InvoiceLine> getInvoiceLines() {
        return this.invoiceLines;
    }
    
    public void setInvoiceLines(Set<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }




}


