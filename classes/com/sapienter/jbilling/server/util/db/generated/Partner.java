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

import com.sapienter.jbilling.server.user.db.BaseUser;

@Entity
@Table(name="partner")
public class Partner  implements java.io.Serializable {


     private int id;
     private PeriodUnit periodUnit;
     private BaseUser baseUserByUserId;
     private BaseUser baseUserByRelatedClerk;
     private Currency currency;
     private double balance;
     private double totalPayments;
     private double totalRefunds;
     private double totalPayouts;
     private Double percentageRate;
     private Double referralFee;
     private short oneTime;
     private int periodValue;
     private Date nextPayoutDate;
     private Double duePayout;
     private short automaticProcess;
     private Set<PartnerPayout> partnerPayouts = new HashSet<PartnerPayout>(0);
     private Set<Customer> customers = new HashSet<Customer>(0);

    public Partner() {
    }

	
    public Partner(int id, PeriodUnit periodUnit, double balance, double totalPayments, double totalRefunds, double totalPayouts, short oneTime, int periodValue, Date nextPayoutDate, short automaticProcess) {
        this.id = id;
        this.periodUnit = periodUnit;
        this.balance = balance;
        this.totalPayments = totalPayments;
        this.totalRefunds = totalRefunds;
        this.totalPayouts = totalPayouts;
        this.oneTime = oneTime;
        this.periodValue = periodValue;
        this.nextPayoutDate = nextPayoutDate;
        this.automaticProcess = automaticProcess;
    }
    public Partner(int id, PeriodUnit periodUnit, BaseUser baseUserByUserId, BaseUser baseUserByRelatedClerk, Currency currency, double balance, double totalPayments, double totalRefunds, double totalPayouts, Double percentageRate, Double referralFee, short oneTime, int periodValue, Date nextPayoutDate, Double duePayout, short automaticProcess, Set<PartnerPayout> partnerPayouts, Set<Customer> customers) {
       this.id = id;
       this.periodUnit = periodUnit;
       this.baseUserByUserId = baseUserByUserId;
       this.baseUserByRelatedClerk = baseUserByRelatedClerk;
       this.currency = currency;
       this.balance = balance;
       this.totalPayments = totalPayments;
       this.totalRefunds = totalRefunds;
       this.totalPayouts = totalPayouts;
       this.percentageRate = percentageRate;
       this.referralFee = referralFee;
       this.oneTime = oneTime;
       this.periodValue = periodValue;
       this.nextPayoutDate = nextPayoutDate;
       this.duePayout = duePayout;
       this.automaticProcess = automaticProcess;
       this.partnerPayouts = partnerPayouts;
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
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="period_unit_id", nullable=false)
    public PeriodUnit getPeriodUnit() {
        return this.periodUnit;
    }
    
    public void setPeriodUnit(PeriodUnit periodUnit) {
        this.periodUnit = periodUnit;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    public BaseUser getBaseUserByUserId() {
        return this.baseUserByUserId;
    }
    
    public void setBaseUserByUserId(BaseUser baseUserByUserId) {
        this.baseUserByUserId = baseUserByUserId;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="related_clerk")
    public BaseUser getBaseUserByRelatedClerk() {
        return this.baseUserByRelatedClerk;
    }
    
    public void setBaseUserByRelatedClerk(BaseUser baseUserByRelatedClerk) {
        this.baseUserByRelatedClerk = baseUserByRelatedClerk;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fee_currency_id")
    public Currency getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    
    @Column(name="balance", nullable=false, precision=17, scale=17)
    public double getBalance() {
        return this.balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    @Column(name="total_payments", nullable=false, precision=17, scale=17)
    public double getTotalPayments() {
        return this.totalPayments;
    }
    
    public void setTotalPayments(double totalPayments) {
        this.totalPayments = totalPayments;
    }
    
    @Column(name="total_refunds", nullable=false, precision=17, scale=17)
    public double getTotalRefunds() {
        return this.totalRefunds;
    }
    
    public void setTotalRefunds(double totalRefunds) {
        this.totalRefunds = totalRefunds;
    }
    
    @Column(name="total_payouts", nullable=false, precision=17, scale=17)
    public double getTotalPayouts() {
        return this.totalPayouts;
    }
    
    public void setTotalPayouts(double totalPayouts) {
        this.totalPayouts = totalPayouts;
    }
    
    @Column(name="percentage_rate", precision=17, scale=17)
    public Double getPercentageRate() {
        return this.percentageRate;
    }
    
    public void setPercentageRate(Double percentageRate) {
        this.percentageRate = percentageRate;
    }
    
    @Column(name="referral_fee", precision=17, scale=17)
    public Double getReferralFee() {
        return this.referralFee;
    }
    
    public void setReferralFee(Double referralFee) {
        this.referralFee = referralFee;
    }
    
    @Column(name="one_time", nullable=false)
    public short getOneTime() {
        return this.oneTime;
    }
    
    public void setOneTime(short oneTime) {
        this.oneTime = oneTime;
    }
    
    @Column(name="period_value", nullable=false)
    public int getPeriodValue() {
        return this.periodValue;
    }
    
    public void setPeriodValue(int periodValue) {
        this.periodValue = periodValue;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="next_payout_date", nullable=false, length=13)
    public Date getNextPayoutDate() {
        return this.nextPayoutDate;
    }
    
    public void setNextPayoutDate(Date nextPayoutDate) {
        this.nextPayoutDate = nextPayoutDate;
    }
    
    @Column(name="due_payout", precision=17, scale=17)
    public Double getDuePayout() {
        return this.duePayout;
    }
    
    public void setDuePayout(Double duePayout) {
        this.duePayout = duePayout;
    }
    
    @Column(name="automatic_process", nullable=false)
    public short getAutomaticProcess() {
        return this.automaticProcess;
    }
    
    public void setAutomaticProcess(short automaticProcess) {
        this.automaticProcess = automaticProcess;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="partner")
    public Set<PartnerPayout> getPartnerPayouts() {
        return this.partnerPayouts;
    }
    
    public void setPartnerPayouts(Set<PartnerPayout> partnerPayouts) {
        this.partnerPayouts = partnerPayouts;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="partner")
    public Set<Customer> getCustomers() {
        return this.customers;
    }
    
    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }




}


