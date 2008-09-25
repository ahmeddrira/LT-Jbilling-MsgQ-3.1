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

@Entity
@Table(name="process_run_total_pm")
public class ProcessRunTotalPm  implements java.io.Serializable {


     private int id;
     private PaymentMethod paymentMethod;
     private Integer processRunTotalId;
     private double total;

    public ProcessRunTotalPm() {
    }

	
    public ProcessRunTotalPm(int id, double total) {
        this.id = id;
        this.total = total;
    }
    public ProcessRunTotalPm(int id, PaymentMethod paymentMethod, Integer processRunTotalId, double total) {
       this.id = id;
       this.paymentMethod = paymentMethod;
       this.processRunTotalId = processRunTotalId;
       this.total = total;
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
    @JoinColumn(name="payment_method_id")
    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    @Column(name="process_run_total_id")
    public Integer getProcessRunTotalId() {
        return this.processRunTotalId;
    }
    
    public void setProcessRunTotalId(Integer processRunTotalId) {
        this.processRunTotalId = processRunTotalId;
    }
    
    @Column(name="total", nullable=false, precision=17, scale=17)
    public double getTotal() {
        return this.total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }




}


