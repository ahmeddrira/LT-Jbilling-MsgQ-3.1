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

import com.sapienter.jbilling.server.util.db.CurrencyDTO;

@Entity
@Table(name="process_run_total")
public class ProcessRunTotal  implements java.io.Serializable {


     private int id;
     private ProcessRun processRun;
     private CurrencyDTO currencyDTO;
     private Double totalInvoiced;
     private Double totalPaid;
     private Double totalNotPaid;

    public ProcessRunTotal() {
    }

	
    public ProcessRunTotal(int id, CurrencyDTO currencyDTO) {
        this.id = id;
        this.currencyDTO = currencyDTO;
    }
    public ProcessRunTotal(int id, ProcessRun processRun, CurrencyDTO currencyDTO, Double totalInvoiced, Double totalPaid, Double totalNotPaid) {
       this.id = id;
       this.processRun = processRun;
       this.currencyDTO = currencyDTO;
       this.totalInvoiced = totalInvoiced;
       this.totalPaid = totalPaid;
       this.totalNotPaid = totalNotPaid;
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
    @JoinColumn(name="process_run_id")
    public ProcessRun getProcessRun() {
        return this.processRun;
    }
    
    public void setProcessRun(ProcessRun processRun) {
        this.processRun = processRun;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="currency_id", nullable=false)
    public CurrencyDTO getCurrency() {
        return this.currencyDTO;
    }
    
    public void setCurrency(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }
    
    @Column(name="total_invoiced", precision=17, scale=17)
    public Double getTotalInvoiced() {
        return this.totalInvoiced;
    }
    
    public void setTotalInvoiced(Double totalInvoiced) {
        this.totalInvoiced = totalInvoiced;
    }
    
    @Column(name="total_paid", precision=17, scale=17)
    public Double getTotalPaid() {
        return this.totalPaid;
    }
    
    public void setTotalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
    }
    
    @Column(name="total_not_paid", precision=17, scale=17)
    public Double getTotalNotPaid() {
        return this.totalNotPaid;
    }
    
    public void setTotalNotPaid(Double totalNotPaid) {
        this.totalNotPaid = totalNotPaid;
    }




}


