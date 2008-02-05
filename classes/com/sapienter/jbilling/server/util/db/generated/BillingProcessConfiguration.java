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


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="billing_process_configuration")
public class BillingProcessConfiguration  implements java.io.Serializable {


     private int id;
     private PeriodUnit periodUnit;
     private Company entity;
     private Date nextRunDate;
     private short generateReport;
     private Integer retries;
     private Integer daysForRetry;
     private Integer daysForReport;
     private int reviewStatus;
     private int periodValue;
     private int dueDateUnitId;
     private int dueDateValue;
     private Short dfFm;
     private short onlyRecurring;
     private short invoiceDateProcess;
     private short autoPayment;
     private int maximumPeriods;
     private int autoPaymentApplication;

    public BillingProcessConfiguration() {
    }

	
    public BillingProcessConfiguration(int id, PeriodUnit periodUnit, Date nextRunDate, short generateReport, int reviewStatus, int periodValue, int dueDateUnitId, int dueDateValue, short onlyRecurring, short invoiceDateProcess, short autoPayment, int maximumPeriods, int autoPaymentApplication) {
        this.id = id;
        this.periodUnit = periodUnit;
        this.nextRunDate = nextRunDate;
        this.generateReport = generateReport;
        this.reviewStatus = reviewStatus;
        this.periodValue = periodValue;
        this.dueDateUnitId = dueDateUnitId;
        this.dueDateValue = dueDateValue;
        this.onlyRecurring = onlyRecurring;
        this.invoiceDateProcess = invoiceDateProcess;
        this.autoPayment = autoPayment;
        this.maximumPeriods = maximumPeriods;
        this.autoPaymentApplication = autoPaymentApplication;
    }
    public BillingProcessConfiguration(int id, PeriodUnit periodUnit, Company entity, Date nextRunDate, short generateReport, Integer retries, Integer daysForRetry, Integer daysForReport, int reviewStatus, int periodValue, int dueDateUnitId, int dueDateValue, Short dfFm, short onlyRecurring, short invoiceDateProcess, short autoPayment, int maximumPeriods, int autoPaymentApplication) {
       this.id = id;
       this.periodUnit = periodUnit;
       this.entity = entity;
       this.nextRunDate = nextRunDate;
       this.generateReport = generateReport;
       this.retries = retries;
       this.daysForRetry = daysForRetry;
       this.daysForReport = daysForReport;
       this.reviewStatus = reviewStatus;
       this.periodValue = periodValue;
       this.dueDateUnitId = dueDateUnitId;
       this.dueDateValue = dueDateValue;
       this.dfFm = dfFm;
       this.onlyRecurring = onlyRecurring;
       this.invoiceDateProcess = invoiceDateProcess;
       this.autoPayment = autoPayment;
       this.maximumPeriods = maximumPeriods;
       this.autoPaymentApplication = autoPaymentApplication;
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
    @JoinColumn(name="period_unit_id", nullable=false)
    public PeriodUnit getPeriodUnit() {
        return this.periodUnit;
    }
    
    public void setPeriodUnit(PeriodUnit periodUnit) {
        this.periodUnit = periodUnit;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="entity_id")
    public Company getEntity() {
        return this.entity;
    }
    
    public void setEntity(Company entity) {
        this.entity = entity;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="next_run_date", nullable=false, length=13)
    public Date getNextRunDate() {
        return this.nextRunDate;
    }
    
    public void setNextRunDate(Date nextRunDate) {
        this.nextRunDate = nextRunDate;
    }
    
    @Column(name="generate_report", nullable=false)
    public short getGenerateReport() {
        return this.generateReport;
    }
    
    public void setGenerateReport(short generateReport) {
        this.generateReport = generateReport;
    }
    
    @Column(name="retries")
    public Integer getRetries() {
        return this.retries;
    }
    
    public void setRetries(Integer retries) {
        this.retries = retries;
    }
    
    @Column(name="days_for_retry")
    public Integer getDaysForRetry() {
        return this.daysForRetry;
    }
    
    public void setDaysForRetry(Integer daysForRetry) {
        this.daysForRetry = daysForRetry;
    }
    
    @Column(name="days_for_report")
    public Integer getDaysForReport() {
        return this.daysForReport;
    }
    
    public void setDaysForReport(Integer daysForReport) {
        this.daysForReport = daysForReport;
    }
    
    @Column(name="review_status", nullable=false)
    public int getReviewStatus() {
        return this.reviewStatus;
    }
    
    public void setReviewStatus(int reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
    
    @Column(name="period_value", nullable=false)
    public int getPeriodValue() {
        return this.periodValue;
    }
    
    public void setPeriodValue(int periodValue) {
        this.periodValue = periodValue;
    }
    
    @Column(name="due_date_unit_id", nullable=false)
    public int getDueDateUnitId() {
        return this.dueDateUnitId;
    }
    
    public void setDueDateUnitId(int dueDateUnitId) {
        this.dueDateUnitId = dueDateUnitId;
    }
    
    @Column(name="due_date_value", nullable=false)
    public int getDueDateValue() {
        return this.dueDateValue;
    }
    
    public void setDueDateValue(int dueDateValue) {
        this.dueDateValue = dueDateValue;
    }
    
    @Column(name="df_fm")
    public Short getDfFm() {
        return this.dfFm;
    }
    
    public void setDfFm(Short dfFm) {
        this.dfFm = dfFm;
    }
    
    @Column(name="only_recurring", nullable=false)
    public short getOnlyRecurring() {
        return this.onlyRecurring;
    }
    
    public void setOnlyRecurring(short onlyRecurring) {
        this.onlyRecurring = onlyRecurring;
    }
    
    @Column(name="invoice_date_process", nullable=false)
    public short getInvoiceDateProcess() {
        return this.invoiceDateProcess;
    }
    
    public void setInvoiceDateProcess(short invoiceDateProcess) {
        this.invoiceDateProcess = invoiceDateProcess;
    }
    
    @Column(name="auto_payment", nullable=false)
    public short getAutoPayment() {
        return this.autoPayment;
    }
    
    public void setAutoPayment(short autoPayment) {
        this.autoPayment = autoPayment;
    }
    
    @Column(name="maximum_periods", nullable=false)
    public int getMaximumPeriods() {
        return this.maximumPeriods;
    }
    
    public void setMaximumPeriods(int maximumPeriods) {
        this.maximumPeriods = maximumPeriods;
    }
    
    @Column(name="auto_payment_application", nullable=false)
    public int getAutoPaymentApplication() {
        return this.autoPaymentApplication;
    }
    
    public void setAutoPaymentApplication(int autoPaymentApplication) {
        this.autoPaymentApplication = autoPaymentApplication;
    }




}


