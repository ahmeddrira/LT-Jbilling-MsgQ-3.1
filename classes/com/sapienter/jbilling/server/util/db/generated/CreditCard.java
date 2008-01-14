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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="credit_card"
    ,schema="public"
)
public class CreditCard  implements java.io.Serializable {


     private int id;
     private String ccNumber;
     private Date ccExpiry;
     private String name;
     private int ccType;
     private short deleted;
     private Integer securityCode;
     private String ccNumberPlain;
     private Set<Payment> payments = new HashSet<Payment>(0);
     private Set<BaseUser> baseUsers = new HashSet<BaseUser>(0);

    public CreditCard() {
    }

	
    public CreditCard(int id, String ccNumber, Date ccExpiry, int ccType, short deleted) {
        this.id = id;
        this.ccNumber = ccNumber;
        this.ccExpiry = ccExpiry;
        this.ccType = ccType;
        this.deleted = deleted;
    }
    public CreditCard(int id, String ccNumber, Date ccExpiry, String name, int ccType, short deleted, Integer securityCode, String ccNumberPlain, Set<Payment> payments, Set<BaseUser> baseUsers) {
       this.id = id;
       this.ccNumber = ccNumber;
       this.ccExpiry = ccExpiry;
       this.name = name;
       this.ccType = ccType;
       this.deleted = deleted;
       this.securityCode = securityCode;
       this.ccNumberPlain = ccNumberPlain;
       this.payments = payments;
       this.baseUsers = baseUsers;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="cc_number", nullable=false, length=100)
    public String getCcNumber() {
        return this.ccNumber;
    }
    
    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="cc_expiry", nullable=false, length=13)
    public Date getCcExpiry() {
        return this.ccExpiry;
    }
    
    public void setCcExpiry(Date ccExpiry) {
        this.ccExpiry = ccExpiry;
    }
    
    @Column(name="name", length=150)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="cc_type", nullable=false)
    public int getCcType() {
        return this.ccType;
    }
    
    public void setCcType(int ccType) {
        this.ccType = ccType;
    }
    
    @Column(name="deleted", nullable=false)
    public short getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="security_code")
    public Integer getSecurityCode() {
        return this.securityCode;
    }
    
    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }
    
    @Column(name="cc_number_plain", length=20)
    public String getCcNumberPlain() {
        return this.ccNumberPlain;
    }
    
    public void setCcNumberPlain(String ccNumberPlain) {
        this.ccNumberPlain = ccNumberPlain;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="creditCard")
    public Set<Payment> getPayments() {
        return this.payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="user_credit_card_map", schema="public", joinColumns = { 
        @JoinColumn(name="credit_card_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="user_id", updatable=false) })
    public Set<BaseUser> getBaseUsers() {
        return this.baseUsers;
    }
    
    public void setBaseUsers(Set<BaseUser> baseUsers) {
        this.baseUsers = baseUsers;
    }




}


