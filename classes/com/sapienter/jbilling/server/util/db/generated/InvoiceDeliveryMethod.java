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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.user.db.CustomerDTO;

@Entity
@Table(name="invoice_delivery_method")
public class InvoiceDeliveryMethod  implements java.io.Serializable {


     private int id;
     private Set<CompanyDTO> entities = new HashSet<CompanyDTO>(0);
     private Set<CustomerDTO> customers = new HashSet<CustomerDTO>(0);

    public InvoiceDeliveryMethod() {
    }

	
    public InvoiceDeliveryMethod(int id) {
        this.id = id;
    }
    public InvoiceDeliveryMethod(int id, Set<CompanyDTO> entities, Set<CustomerDTO> customers) {
       this.id = id;
       this.entities = entities;
       this.customers = customers;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="entity_delivery_method_map",joinColumns = { 
        @JoinColumn(name="method_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) })
    public Set<CompanyDTO> getEntities() {
        return this.entities;
    }
    
    public void setEntities(Set<CompanyDTO> entities) {
        this.entities = entities;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="invoiceDeliveryMethod")
    public Set<CustomerDTO> getCustomers() {
        return this.customers;
    }
    
    public void setCustomers(Set<CustomerDTO> customers) {
        this.customers = customers;
    }




}


