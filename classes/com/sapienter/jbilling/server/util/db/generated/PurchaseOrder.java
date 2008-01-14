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

@Entity
@Table(name="purchase_order"
    ,schema="public"
)
public class PurchaseOrder  implements java.io.Serializable {


     private int id;
     private BaseUser baseUserByUserId;
     private BaseUser baseUserByCreatedBy;
     private Currency currency;
     private OrderStatus orderStatus;
     private OrderPeriod orderPeriod;
     private OrderBillingType orderBillingType;
     private Date activeSince;
     private Date activeUntil;
     private Date createDatetime;
     private Date nextBillableDay;
     private short deleted;
     private Short notify;
     private Date lastNotified;
     private Integer notificationStep;
     private Integer dueDateUnitId;
     private Integer dueDateValue;
     private Short dfFm;
     private Integer anticipatePeriods;
     private Short ownInvoice;
     private String notes;
     private Short notesInInvoice;
     private Set<OrderProcess> orderProcesses = new HashSet<OrderProcess>(0);
     private Set<OrderLine> orderLines = new HashSet<OrderLine>(0);

    public PurchaseOrder() {
    }

	
    public PurchaseOrder(int id, BaseUser baseUserByCreatedBy, Currency currency, OrderStatus orderStatus, OrderBillingType orderBillingType, Date createDatetime, short deleted) {
        this.id = id;
        this.baseUserByCreatedBy = baseUserByCreatedBy;
        this.currency = currency;
        this.orderStatus = orderStatus;
        this.orderBillingType = orderBillingType;
        this.createDatetime = createDatetime;
        this.deleted = deleted;
    }
    public PurchaseOrder(int id, BaseUser baseUserByUserId, BaseUser baseUserByCreatedBy, Currency currency, OrderStatus orderStatus, OrderPeriod orderPeriod, OrderBillingType orderBillingType, Date activeSince, Date activeUntil, Date createDatetime, Date nextBillableDay, short deleted, Short notify, Date lastNotified, Integer notificationStep, Integer dueDateUnitId, Integer dueDateValue, Short dfFm, Integer anticipatePeriods, Short ownInvoice, String notes, Short notesInInvoice, Set<OrderProcess> orderProcesses, Set<OrderLine> orderLines) {
       this.id = id;
       this.baseUserByUserId = baseUserByUserId;
       this.baseUserByCreatedBy = baseUserByCreatedBy;
       this.currency = currency;
       this.orderStatus = orderStatus;
       this.orderPeriod = orderPeriod;
       this.orderBillingType = orderBillingType;
       this.activeSince = activeSince;
       this.activeUntil = activeUntil;
       this.createDatetime = createDatetime;
       this.nextBillableDay = nextBillableDay;
       this.deleted = deleted;
       this.notify = notify;
       this.lastNotified = lastNotified;
       this.notificationStep = notificationStep;
       this.dueDateUnitId = dueDateUnitId;
       this.dueDateValue = dueDateValue;
       this.dfFm = dfFm;
       this.anticipatePeriods = anticipatePeriods;
       this.ownInvoice = ownInvoice;
       this.notes = notes;
       this.notesInInvoice = notesInInvoice;
       this.orderProcesses = orderProcesses;
       this.orderLines = orderLines;
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
    public BaseUser getBaseUserByUserId() {
        return this.baseUserByUserId;
    }
    
    public void setBaseUserByUserId(BaseUser baseUserByUserId) {
        this.baseUserByUserId = baseUserByUserId;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="created_by", nullable=false)
    public BaseUser getBaseUserByCreatedBy() {
        return this.baseUserByCreatedBy;
    }
    
    public void setBaseUserByCreatedBy(BaseUser baseUserByCreatedBy) {
        this.baseUserByCreatedBy = baseUserByCreatedBy;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="currency_id", nullable=false)
    public Currency getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="status_id", nullable=false)
    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }
    
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="period_id")
    public OrderPeriod getOrderPeriod() {
        return this.orderPeriod;
    }
    
    public void setOrderPeriod(OrderPeriod orderPeriod) {
        this.orderPeriod = orderPeriod;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="billing_type_id", nullable=false)
    public OrderBillingType getOrderBillingType() {
        return this.orderBillingType;
    }
    
    public void setOrderBillingType(OrderBillingType orderBillingType) {
        this.orderBillingType = orderBillingType;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="active_since", length=13)
    public Date getActiveSince() {
        return this.activeSince;
    }
    
    public void setActiveSince(Date activeSince) {
        this.activeSince = activeSince;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="active_until", length=13)
    public Date getActiveUntil() {
        return this.activeUntil;
    }
    
    public void setActiveUntil(Date activeUntil) {
        this.activeUntil = activeUntil;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="next_billable_day", length=29)
    public Date getNextBillableDay() {
        return this.nextBillableDay;
    }
    
    public void setNextBillableDay(Date nextBillableDay) {
        this.nextBillableDay = nextBillableDay;
    }
    
    @Column(name="deleted", nullable=false)
    public short getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="notify")
    public Short getNotify() {
        return this.notify;
    }
    
    public void setNotify(Short notify) {
        this.notify = notify;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_notified", length=29)
    public Date getLastNotified() {
        return this.lastNotified;
    }
    
    public void setLastNotified(Date lastNotified) {
        this.lastNotified = lastNotified;
    }
    
    @Column(name="notification_step")
    public Integer getNotificationStep() {
        return this.notificationStep;
    }
    
    public void setNotificationStep(Integer notificationStep) {
        this.notificationStep = notificationStep;
    }
    
    @Column(name="due_date_unit_id")
    public Integer getDueDateUnitId() {
        return this.dueDateUnitId;
    }
    
    public void setDueDateUnitId(Integer dueDateUnitId) {
        this.dueDateUnitId = dueDateUnitId;
    }
    
    @Column(name="due_date_value")
    public Integer getDueDateValue() {
        return this.dueDateValue;
    }
    
    public void setDueDateValue(Integer dueDateValue) {
        this.dueDateValue = dueDateValue;
    }
    
    @Column(name="df_fm")
    public Short getDfFm() {
        return this.dfFm;
    }
    
    public void setDfFm(Short dfFm) {
        this.dfFm = dfFm;
    }
    
    @Column(name="anticipate_periods")
    public Integer getAnticipatePeriods() {
        return this.anticipatePeriods;
    }
    
    public void setAnticipatePeriods(Integer anticipatePeriods) {
        this.anticipatePeriods = anticipatePeriods;
    }
    
    @Column(name="own_invoice")
    public Short getOwnInvoice() {
        return this.ownInvoice;
    }
    
    public void setOwnInvoice(Short ownInvoice) {
        this.ownInvoice = ownInvoice;
    }
    
    @Column(name="notes", length=200)
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Column(name="notes_in_invoice")
    public Short getNotesInInvoice() {
        return this.notesInInvoice;
    }
    
    public void setNotesInInvoice(Short notesInInvoice) {
        this.notesInInvoice = notesInInvoice;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="purchaseOrder")
    public Set<OrderProcess> getOrderProcesses() {
        return this.orderProcesses;
    }
    
    public void setOrderProcesses(Set<OrderProcess> orderProcesses) {
        this.orderProcesses = orderProcesses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="purchaseOrder")
    public Set<OrderLine> getOrderLines() {
        return this.orderLines;
    }
    
    public void setOrderLines(Set<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }




}


