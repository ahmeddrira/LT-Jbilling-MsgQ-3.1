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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sapienter.jbilling.server.user.db.BaseUser;

@Entity
@Table(name="customer")
public class Customer  implements java.io.Serializable {


     private int id;
     private BaseUser baseUser;
     private InvoiceDeliveryMethod invoiceDeliveryMethod;
     private Partner partner;
     private Short referralFeePaid;
     private String notes;
     private Integer autoPaymentType;
     private Integer dueDateUnitId;
     private Integer dueDateValue;
     private Short dfFm;
     private Integer parentId;
     private Short isParent;
     private short excludeAging;
     private Short invoiceChild;
     private Integer currentOrderId;

    public Customer() {
    }

	
    public Customer(int id, InvoiceDeliveryMethod invoiceDeliveryMethod, short excludeAging) {
        this.id = id;
        this.invoiceDeliveryMethod = invoiceDeliveryMethod;
        this.excludeAging = excludeAging;
    }
    public Customer(int id, BaseUser baseUser, InvoiceDeliveryMethod invoiceDeliveryMethod, Partner partner, Short referralFeePaid, String notes, Integer autoPaymentType, Integer dueDateUnitId, Integer dueDateValue, Short dfFm, Integer parentId, Short isParent, short excludeAging, Short invoiceChild, Integer currentOrderId) {
       this.id = id;
       this.baseUser = baseUser;
       this.invoiceDeliveryMethod = invoiceDeliveryMethod;
       this.partner = partner;
       this.referralFeePaid = referralFeePaid;
       this.notes = notes;
       this.autoPaymentType = autoPaymentType;
       this.dueDateUnitId = dueDateUnitId;
       this.dueDateValue = dueDateValue;
       this.dfFm = dfFm;
       this.parentId = parentId;
       this.isParent = isParent;
       this.excludeAging = excludeAging;
       this.invoiceChild = invoiceChild;
       this.currentOrderId = currentOrderId;
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
    @JoinColumn(name="invoice_delivery_method_id", nullable=false)
    public InvoiceDeliveryMethod getInvoiceDeliveryMethod() {
        return this.invoiceDeliveryMethod;
    }
    
    public void setInvoiceDeliveryMethod(InvoiceDeliveryMethod invoiceDeliveryMethod) {
        this.invoiceDeliveryMethod = invoiceDeliveryMethod;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="partner_id")
    public Partner getPartner() {
        return this.partner;
    }
    
    public void setPartner(Partner partner) {
        this.partner = partner;
    }
    
    @Column(name="referral_fee_paid")
    public Short getReferralFeePaid() {
        return this.referralFeePaid;
    }
    
    public void setReferralFeePaid(Short referralFeePaid) {
        this.referralFeePaid = referralFeePaid;
    }
    
    @Column(name="notes", length=1000)
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Column(name="auto_payment_type")
    public Integer getAutoPaymentType() {
        return this.autoPaymentType;
    }
    
    public void setAutoPaymentType(Integer autoPaymentType) {
        this.autoPaymentType = autoPaymentType;
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
    
    @Column(name="parent_id")
    public Integer getParentId() {
        return this.parentId;
    }
    
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    
    @Column(name="is_parent")
    public Short getIsParent() {
        return this.isParent;
    }
    
    public void setIsParent(Short isParent) {
        this.isParent = isParent;
    }
    
    @Column(name="exclude_aging", nullable=false)
    public short getExcludeAging() {
        return this.excludeAging;
    }
    
    public void setExcludeAging(short excludeAging) {
        this.excludeAging = excludeAging;
    }
    
    @Column(name="invoice_child")
    public Short getInvoiceChild() {
        return this.invoiceChild;
    }
    
    public void setInvoiceChild(Short invoiceChild) {
        this.invoiceChild = invoiceChild;
    }
    
    @Column(name="current_order_id")
    public Integer getCurrentOrderId() {
        return this.currentOrderId;
    }
    
    public void setCurrentOrderId(Integer currentOrderId) {
        this.currentOrderId = currentOrderId;
    }




}


