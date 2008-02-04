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

import com.sapienter.jbilling.server.user.db.BaseUser;

@Entity
@Table(name="ach"
    ,schema="public"
)
public class Ach  implements java.io.Serializable {


     private int id;
     private BaseUser baseUser;
     private String abaRouting;
     private String bankAccount;
     private int accountType;
     private String bankName;
     private String accountName;
     private Set<Payment> payments = new HashSet<Payment>(0);

    public Ach() {
    }

	
    public Ach(int id, String abaRouting, String bankAccount, int accountType, String bankName, String accountName) {
        this.id = id;
        this.abaRouting = abaRouting;
        this.bankAccount = bankAccount;
        this.accountType = accountType;
        this.bankName = bankName;
        this.accountName = accountName;
    }
    public Ach(int id, BaseUser baseUser, String abaRouting, String bankAccount, int accountType, String bankName, String accountName, Set<Payment> payments) {
       this.id = id;
       this.baseUser = baseUser;
       this.abaRouting = abaRouting;
       this.bankAccount = bankAccount;
       this.accountType = accountType;
       this.bankName = bankName;
       this.accountName = accountName;
       this.payments = payments;
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
    
    @Column(name="aba_routing", nullable=false, length=9)
    public String getAbaRouting() {
        return this.abaRouting;
    }
    
    public void setAbaRouting(String abaRouting) {
        this.abaRouting = abaRouting;
    }
    
    @Column(name="bank_account", nullable=false, length=20)
    public String getBankAccount() {
        return this.bankAccount;
    }
    
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    
    @Column(name="account_type", nullable=false)
    public int getAccountType() {
        return this.accountType;
    }
    
    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
    
    @Column(name="bank_name", nullable=false, length=50)
    public String getBankName() {
        return this.bankName;
    }
    
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
    @Column(name="account_name", nullable=false, length=100)
    public String getAccountName() {
        return this.accountName;
    }
    
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ach")
    public Set<Payment> getPayments() {
        return this.payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }




}


