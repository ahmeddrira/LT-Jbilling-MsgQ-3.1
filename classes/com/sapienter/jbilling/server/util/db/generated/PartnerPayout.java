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
@Table(name="partner_payout")
public class PartnerPayout  implements java.io.Serializable {


     private int id;
     private Payment payment;
     private Partner partner;
     private Date startingDate;
     private Date endingDate;
     private double paymentsAmount;
     private double refundsAmount;
     private double balanceLeft;

    public PartnerPayout() {
    }

	
    public PartnerPayout(int id, Date startingDate, Date endingDate, double paymentsAmount, double refundsAmount, double balanceLeft) {
        this.id = id;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.paymentsAmount = paymentsAmount;
        this.refundsAmount = refundsAmount;
        this.balanceLeft = balanceLeft;
    }
    public PartnerPayout(int id, Payment payment, Partner partner, Date startingDate, Date endingDate, double paymentsAmount, double refundsAmount, double balanceLeft) {
       this.id = id;
       this.payment = payment;
       this.partner = partner;
       this.startingDate = startingDate;
       this.endingDate = endingDate;
       this.paymentsAmount = paymentsAmount;
       this.refundsAmount = refundsAmount;
       this.balanceLeft = balanceLeft;
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
    @JoinColumn(name="payment_id")
    public Payment getPayment() {
        return this.payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="partner_id")
    public Partner getPartner() {
        return this.partner;
    }
    
    public void setPartner(Partner partner) {
        this.partner = partner;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="starting_date", nullable=false, length=13)
    public Date getStartingDate() {
        return this.startingDate;
    }
    
    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="ending_date", nullable=false, length=13)
    public Date getEndingDate() {
        return this.endingDate;
    }
    
    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }
    
    @Column(name="payments_amount", nullable=false, precision=17, scale=17)
    public double getPaymentsAmount() {
        return this.paymentsAmount;
    }
    
    public void setPaymentsAmount(double paymentsAmount) {
        this.paymentsAmount = paymentsAmount;
    }
    
    @Column(name="refunds_amount", nullable=false, precision=17, scale=17)
    public double getRefundsAmount() {
        return this.refundsAmount;
    }
    
    public void setRefundsAmount(double refundsAmount) {
        this.refundsAmount = refundsAmount;
    }
    
    @Column(name="balance_left", nullable=false, precision=17, scale=17)
    public double getBalanceLeft() {
        return this.balanceLeft;
    }
    
    public void setBalanceLeft(double balanceLeft) {
        this.balanceLeft = balanceLeft;
    }




}


