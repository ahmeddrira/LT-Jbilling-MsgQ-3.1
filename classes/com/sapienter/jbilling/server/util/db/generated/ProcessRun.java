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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sapienter.jbilling.server.process.db.BillingProcessDTO;

@Entity
@Table(name="process_run")
public class ProcessRun  implements java.io.Serializable {


     private int id;
     private BillingProcessDTO billingProcessDTO;
     private Date runDate;
     private Date started;
     private Date finished;
     private Integer invoicesGenerated;
     private Date paymentFinished;
     private Set<ProcessRunTotal> processRunTotals = new HashSet<ProcessRunTotal>(0);

    public ProcessRun() {
    }

	
    public ProcessRun(int id, Date runDate, Date started) {
        this.id = id;
        this.runDate = runDate;
        this.started = started;
    }
    public ProcessRun(int id, BillingProcessDTO billingProcessDTO, Date runDate, Date started, Date finished, Integer invoicesGenerated, Date paymentFinished, Set<ProcessRunTotal> processRunTotals) {
       this.id = id;
       this.billingProcessDTO = billingProcessDTO;
       this.runDate = runDate;
       this.started = started;
       this.finished = finished;
       this.invoicesGenerated = invoicesGenerated;
       this.paymentFinished = paymentFinished;
       this.processRunTotals = processRunTotals;
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
    @JoinColumn(name="process_id")
    public BillingProcessDTO getBillingProcess() {
        return this.billingProcessDTO;
    }
    
    public void setBillingProcess(BillingProcessDTO billingProcessDTO) {
        this.billingProcessDTO = billingProcessDTO;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="run_date", nullable=false, length=13)
    public Date getRunDate() {
        return this.runDate;
    }
    
    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="started", nullable=false, length=29)
    public Date getStarted() {
        return this.started;
    }
    
    public void setStarted(Date started) {
        this.started = started;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="finished", length=29)
    public Date getFinished() {
        return this.finished;
    }
    
    public void setFinished(Date finished) {
        this.finished = finished;
    }
    
    @Column(name="invoices_generated")
    public Integer getInvoicesGenerated() {
        return this.invoicesGenerated;
    }
    
    public void setInvoicesGenerated(Integer invoicesGenerated) {
        this.invoicesGenerated = invoicesGenerated;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="payment_finished", length=29)
    public Date getPaymentFinished() {
        return this.paymentFinished;
    }
    
    public void setPaymentFinished(Date paymentFinished) {
        this.paymentFinished = paymentFinished;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="processRun")
    public Set<ProcessRunTotal> getProcessRunTotals() {
        return this.processRunTotals;
    }
    
    public void setProcessRunTotals(Set<ProcessRunTotal> processRunTotals) {
        this.processRunTotals = processRunTotals;
    }




}


