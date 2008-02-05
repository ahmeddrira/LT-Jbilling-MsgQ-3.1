/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
@Table(name="period_unit")
public class PeriodUnit  implements java.io.Serializable {


     private int id;
     private Set<Partner> partners = new HashSet<Partner>(0);
     private Set<OrderPeriod> orderPeriods = new HashSet<OrderPeriod>(0);
     private Set<BillingProcess> billingProcesses = new HashSet<BillingProcess>(0);
     private Set<BillingProcessConfiguration> billingProcessConfigurations = new HashSet<BillingProcessConfiguration>(0);

    public PeriodUnit() {
    }

	
    public PeriodUnit(int id) {
        this.id = id;
    }
    public PeriodUnit(int id, Set<Partner> partners, Set<OrderPeriod> orderPeriods, Set<BillingProcess> billingProcesses, Set<BillingProcessConfiguration> billingProcessConfigurations) {
       this.id = id;
       this.partners = partners;
       this.orderPeriods = orderPeriods;
       this.billingProcesses = billingProcesses;
       this.billingProcessConfigurations = billingProcessConfigurations;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="periodUnit")
    public Set<Partner> getPartners() {
        return this.partners;
    }
    
    public void setPartners(Set<Partner> partners) {
        this.partners = partners;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="periodUnit")
    public Set<OrderPeriod> getOrderPeriods() {
        return this.orderPeriods;
    }
    
    public void setOrderPeriods(Set<OrderPeriod> orderPeriods) {
        this.orderPeriods = orderPeriods;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="periodUnit")
    public Set<BillingProcess> getBillingProcesses() {
        return this.billingProcesses;
    }
    
    public void setBillingProcesses(Set<BillingProcess> billingProcesses) {
        this.billingProcesses = billingProcesses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="periodUnit")
    public Set<BillingProcessConfiguration> getBillingProcessConfigurations() {
        return this.billingProcessConfigurations;
    }
    
    public void setBillingProcessConfigurations(Set<BillingProcessConfiguration> billingProcessConfigurations) {
        this.billingProcessConfigurations = billingProcessConfigurations;
    }




}


