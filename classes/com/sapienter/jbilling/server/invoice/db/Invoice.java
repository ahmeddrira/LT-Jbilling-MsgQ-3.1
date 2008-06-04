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
package com.sapienter.jbilling.server.invoice.db;


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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.sapienter.jbilling.server.order.db.OrderProcessDTO;
import com.sapienter.jbilling.server.process.db.BillingProcessDTO;
import com.sapienter.jbilling.server.user.db.BaseUser;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import com.sapienter.jbilling.server.util.db.generated.InvoiceLine;
import com.sapienter.jbilling.server.util.db.generated.PaperInvoiceBatch;
import com.sapienter.jbilling.server.util.db.generated.PaymentInvoice;

@Entity
@Table(name="invoice")
public class Invoice  implements java.io.Serializable {


     private int id;
     private BillingProcessDTO billingProcessDTO;
     private BaseUser baseUser;
     private CurrencyDTO currencyDTO;
     private Invoice invoice;
     private PaperInvoiceBatch paperInvoiceBatch;
     private Date createDatetime;
     private Date dueDate;
     private double total;
     private int paymentAttempts;
     private short toProcess;
     private Double balance;
     private double carriedBalance;
     private short inProcessPayment;
     private int isReview;
     private short deleted;
     private String customerNotes;
     private String publicNumber;
     private Date lastReminder;
     private Integer overdueStep;
     private Date createTimestamp;
     private Set<PaymentInvoice> paymentInvoices = new HashSet<PaymentInvoice>(0);
     private Set<InvoiceLine> invoiceLines = new HashSet<InvoiceLine>(0);
     private Set<Invoice> invoices = new HashSet<Invoice>(0);
     private Set<OrderProcessDTO> orderProcesses = new HashSet<OrderProcessDTO>(0);

    public Invoice() {
    }

	
    public Invoice(int id, CurrencyDTO currencyDTO, Date createDatetime, Date dueDate, double total, int paymentAttempts, short toProcess, double carriedBalance, short inProcessPayment, int isReview, short deleted, Date createTimestamp) {
        this.id = id;
        this.currencyDTO = currencyDTO;
        this.createDatetime = createDatetime;
        this.dueDate = dueDate;
        this.total = total;
        this.paymentAttempts = paymentAttempts;
        this.toProcess = toProcess;
        this.carriedBalance = carriedBalance;
        this.inProcessPayment = inProcessPayment;
        this.isReview = isReview;
        this.deleted = deleted;
        this.createTimestamp = createTimestamp;
    }
    public Invoice(int id, BillingProcessDTO billingProcessDTO, BaseUser baseUser, CurrencyDTO currencyDTO, Invoice invoice, PaperInvoiceBatch paperInvoiceBatch, Date createDatetime, Date dueDate, double total, int paymentAttempts, short toProcess, Double balance, double carriedBalance, short inProcessPayment, int isReview, short deleted, String customerNotes, String publicNumber, Date lastReminder, Integer overdueStep, Date createTimestamp, Set<PaymentInvoice> paymentInvoices, Set<InvoiceLine> invoiceLines, Set<Invoice> invoices) {
       this.id = id;
       this.billingProcessDTO = billingProcessDTO;
       this.baseUser = baseUser;
       this.currencyDTO = currencyDTO;
       this.invoice = invoice;
       this.paperInvoiceBatch = paperInvoiceBatch;
       this.createDatetime = createDatetime;
       this.dueDate = dueDate;
       this.total = total;
       this.paymentAttempts = paymentAttempts;
       this.toProcess = toProcess;
       this.balance = balance;
       this.carriedBalance = carriedBalance;
       this.inProcessPayment = inProcessPayment;
       this.isReview = isReview;
       this.deleted = deleted;
       this.customerNotes = customerNotes;
       this.publicNumber = publicNumber;
       this.lastReminder = lastReminder;
       this.overdueStep = overdueStep;
       this.createTimestamp = createTimestamp;
       this.paymentInvoices = paymentInvoices;
       this.invoiceLines = invoiceLines;
       this.invoices = invoices;
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
    @JoinColumn(name="billing_process_id")
    public BillingProcessDTO getBillingProcess() {
        return this.billingProcessDTO;
    }
    
    public void setBillingProcess(BillingProcessDTO billingProcessDTO) {
        this.billingProcessDTO = billingProcessDTO;
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
    @JoinColumn(name="delegated_invoice_id")
    public Invoice getInvoice() {
        return this.invoice;
    }
    
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="paper_invoice_batch_id")
    public PaperInvoiceBatch getPaperInvoiceBatch() {
        return this.paperInvoiceBatch;
    }
    
    public void setPaperInvoiceBatch(PaperInvoiceBatch paperInvoiceBatch) {
        this.paperInvoiceBatch = paperInvoiceBatch;
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
    @Column(name="due_date", nullable=false, length=13)
    public Date getDueDate() {
        return this.dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    @Column(name="total", nullable=false, precision=17, scale=17)
    public double getTotal() {
        return this.total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    @Column(name="payment_attempts", nullable=false)
    public int getPaymentAttempts() {
        return this.paymentAttempts;
    }
    
    public void setPaymentAttempts(int paymentAttempts) {
        this.paymentAttempts = paymentAttempts;
    }
    
    @Column(name="to_process", nullable=false)
    public short getToProcess() {
        return this.toProcess;
    }
    
    public void setToProcess(short toProcess) {
        this.toProcess = toProcess;
    }
    
    @Column(name="balance", precision=17, scale=17)
    public Double getBalance() {
        return this.balance;
    }
    
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    
    @Column(name="carried_balance", nullable=false, precision=17, scale=17)
    public double getCarriedBalance() {
        return this.carriedBalance;
    }
    
    public void setCarriedBalance(double carriedBalance) {
        this.carriedBalance = carriedBalance;
    }
    
    @Column(name="in_process_payment", nullable=false)
    public short getInProcessPayment() {
        return this.inProcessPayment;
    }
    
    public void setInProcessPayment(short inProcessPayment) {
        this.inProcessPayment = inProcessPayment;
    }
    
    @Column(name="is_review", nullable=false)
    public int getIsReview() {
        return this.isReview;
    }
    
    public void setIsReview(int isReview) {
        this.isReview = isReview;
    }
    
    @Column(name="deleted", nullable=false)
    public short getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="customer_notes", length=1000)
    public String getCustomerNotes() {
        return this.customerNotes;
    }
    
    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }
    
    @Column(name="public_number", length=40)
    public String getPublicNumber() {
        return this.publicNumber;
    }
    
    public void setPublicNumber(String publicNumber) {
        this.publicNumber = publicNumber;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_reminder", length=29)
    public Date getLastReminder() {
        return this.lastReminder;
    }
    
    public void setLastReminder(Date lastReminder) {
        this.lastReminder = lastReminder;
    }
    
    @Column(name="overdue_step")
    public Integer getOverdueStep() {
        return this.overdueStep;
    }
    
    public void setOverdueStep(Integer overdueStep) {
        this.overdueStep = overdueStep;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_timestamp", nullable=false, length=29)
    public Date getCreateTimestamp() {
        return this.createTimestamp;
    }
    
    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="invoice")
    public Set<PaymentInvoice> getPaymentInvoices() {
        return this.paymentInvoices;
    }
    
    public void setPaymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="invoice")
    public Set<InvoiceLine> getInvoiceLines() {
        return this.invoiceLines;
    }
    
    public void setInvoiceLines(Set<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="invoice")
    public Set<Invoice> getInvoices() {
        return this.invoices;
    }
    
    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="invoice")
    @Fetch ( FetchMode.SUBSELECT)
    public Set<OrderProcessDTO> getOrderProcesses() {
        return this.orderProcesses;
    }
    
    public void setOrderProcesses(Set<OrderProcessDTO> orderProcesses) {
        this.orderProcesses = orderProcesses;
    }
}


