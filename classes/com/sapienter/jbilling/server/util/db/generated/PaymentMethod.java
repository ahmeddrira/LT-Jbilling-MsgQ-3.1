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

@Entity
@Table(name="payment_method")
public class PaymentMethod  implements java.io.Serializable {


     private int id;
     private Set<Payment> payments = new HashSet<Payment>(0);
     private Set<CompanyDTO> entities = new HashSet<CompanyDTO>(0);
     private Set<ProcessRunTotalPm> processRunTotalPms = new HashSet<ProcessRunTotalPm>(0);

    public PaymentMethod() {
    }

	
    public PaymentMethod(int id) {
        this.id = id;
    }
    public PaymentMethod(int id, Set<Payment> payments, Set<CompanyDTO> entities, Set<ProcessRunTotalPm> processRunTotalPms) {
       this.id = id;
       this.payments = payments;
       this.entities = entities;
       this.processRunTotalPms = processRunTotalPms;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="paymentMethod")
    public Set<Payment> getPayments() {
        return this.payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="entity_payment_method_map", joinColumns = { 
        @JoinColumn(name="payment_method_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) })
    public Set<CompanyDTO> getEntities() {
        return this.entities;
    }
    
    public void setEntities(Set<CompanyDTO> entities) {
        this.entities = entities;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="paymentMethod")
    public Set<ProcessRunTotalPm> getProcessRunTotalPms() {
        return this.processRunTotalPms;
    }
    
    public void setProcessRunTotalPms(Set<ProcessRunTotalPm> processRunTotalPms) {
        this.processRunTotalPms = processRunTotalPms;
    }




}


