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
import com.sapienter.jbilling.server.util.db.CurrencyDTO;

@Entity
@Table(name="payment")
public class Payment  implements java.io.Serializable {


     private int id;
     private BaseUser baseUser;
     private CurrencyDTO currencyDTO;
     private PaymentMethod paymentMethod;
     private Payment payment;
     private CreditCard creditCard;
     private PaymentResult paymentResult;
     private Ach ach;
     private Integer attempt;
     private double amount;
     private Date createDatetime;
     private Date paymentDate;
     private short deleted;
     private short isRefund;
     private Integer payoutId;
     private Double balance;
     private Date updateDatetime;
     private short isPreauth;
     private Set<PaymentInvoice> paymentInvoices = new HashSet<PaymentInvoice>(0);
     private Set<PaymentAuthorization> paymentAuthorizations = new HashSet<PaymentAuthorization>(0);
     private Set<Payment> payments = new HashSet<Payment>(0);
     private Set<PartnerPayout> partnerPayouts = new HashSet<PartnerPayout>(0);
     private Set<PaymentInfoCheque> paymentInfoCheques = new HashSet<PaymentInfoCheque>(0);

    public Payment() {
    }

	
    public Payment(int id, CurrencyDTO currencyDTO, PaymentMethod paymentMethod, double amount, Date createDatetime, short deleted, short isRefund, short isPreauth) {
        this.id = id;
        this.currencyDTO = currencyDTO;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.createDatetime = createDatetime;
        this.deleted = deleted;
        this.isRefund = isRefund;
        this.isPreauth = isPreauth;
    }
    public Payment(int id, BaseUser baseUser, CurrencyDTO currencyDTO, PaymentMethod paymentMethod, Payment payment, CreditCard creditCard, PaymentResult paymentResult, Ach ach, Integer attempt, double amount, Date createDatetime, Date paymentDate, short deleted, short isRefund, Integer payoutId, Double balance, Date updateDatetime, short isPreauth, Set<PaymentInvoice> paymentInvoices, Set<PaymentAuthorization> paymentAuthorizations, Set<Payment> payments, Set<PartnerPayout> partnerPayouts, Set<PaymentInfoCheque> paymentInfoCheques) {
       this.id = id;
       this.baseUser = baseUser;
       this.currencyDTO = currencyDTO;
       this.paymentMethod = paymentMethod;
       this.payment = payment;
       this.creditCard = creditCard;
       this.paymentResult = paymentResult;
       this.ach = ach;
       this.attempt = attempt;
       this.amount = amount;
       this.createDatetime = createDatetime;
       this.paymentDate = paymentDate;
       this.deleted = deleted;
       this.isRefund = isRefund;
       this.payoutId = payoutId;
       this.balance = balance;
       this.updateDatetime = updateDatetime;
       this.isPreauth = isPreauth;
       this.paymentInvoices = paymentInvoices;
       this.paymentAuthorizations = paymentAuthorizations;
       this.payments = payments;
       this.partnerPayouts = partnerPayouts;
       this.paymentInfoCheques = paymentInfoCheques;
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
    @JoinColumn(name="user_id")
    public BaseUser getBaseUser() {
        return this.baseUser;
    }
    
    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="currency_id", nullable=false)
    public CurrencyDTO getCurrency() {
        return this.currencyDTO;
    }
    
    public void setCurrency(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="method_id", nullable=false)
    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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
    @JoinColumn(name="credit_card_id")
    public CreditCard getCreditCard() {
        return this.creditCard;
    }
    
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="result_id")
    public PaymentResult getPaymentResult() {
        return this.paymentResult;
    }
    
    public void setPaymentResult(PaymentResult paymentResult) {
        this.paymentResult = paymentResult;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ach_id")
    public Ach getAch() {
        return this.ach;
    }
    
    public void setAch(Ach ach) {
        this.ach = ach;
    }
    
    @Column(name="attempt")
    public Integer getAttempt() {
        return this.attempt;
    }
    
    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }
    
    @Column(name="amount", nullable=false, precision=17, scale=17)
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="payment_date", length=13)
    public Date getPaymentDate() {
        return this.paymentDate;
    }
    
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    @Column(name="deleted", nullable=false)
    public short getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="is_refund", nullable=false)
    public short getIsRefund() {
        return this.isRefund;
    }
    
    public void setIsRefund(short isRefund) {
        this.isRefund = isRefund;
    }
    
    @Column(name="payout_id")
    public Integer getPayoutId() {
        return this.payoutId;
    }
    
    public void setPayoutId(Integer payoutId) {
        this.payoutId = payoutId;
    }
    
    @Column(name="balance", precision=17, scale=17)
    public Double getBalance() {
        return this.balance;
    }
    
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_datetime", length=29)
    public Date getUpdateDatetime() {
        return this.updateDatetime;
    }
    
    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
    
    @Column(name="is_preauth", nullable=false)
    public short getIsPreauth() {
        return this.isPreauth;
    }
    
    public void setIsPreauth(short isPreauth) {
        this.isPreauth = isPreauth;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="payment")
    public Set<PaymentInvoice> getPaymentInvoices() {
        return this.paymentInvoices;
    }
    
    public void setPaymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="payment")
    public Set<PaymentAuthorization> getPaymentAuthorizations() {
        return this.paymentAuthorizations;
    }
    
    public void setPaymentAuthorizations(Set<PaymentAuthorization> paymentAuthorizations) {
        this.paymentAuthorizations = paymentAuthorizations;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="payment")
    public Set<Payment> getPayments() {
        return this.payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="payment")
    public Set<PartnerPayout> getPartnerPayouts() {
        return this.partnerPayouts;
    }
    
    public void setPartnerPayouts(Set<PartnerPayout> partnerPayouts) {
        this.partnerPayouts = partnerPayouts;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="payment")
    public Set<PaymentInfoCheque> getPaymentInfoCheques() {
        return this.paymentInfoCheques;
    }
    
    public void setPaymentInfoCheques(Set<PaymentInfoCheque> paymentInfoCheques) {
        this.paymentInfoCheques = paymentInfoCheques;
    }




}


