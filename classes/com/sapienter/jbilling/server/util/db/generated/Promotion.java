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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sapienter.jbilling.server.item.db.Item;
import com.sapienter.jbilling.server.user.db.BaseUser;

@Entity
@Table(name="promotion")
public class Promotion  implements java.io.Serializable {


     private int id;
     private Item item;
     private String code;
     private String notes;
     private short once;
     private Date since;
     private Date until;
     private Set<BaseUser> baseUsers = new HashSet<BaseUser>(0);

    public Promotion() {
    }

	
    public Promotion(int id, String code, short once) {
        this.id = id;
        this.code = code;
        this.once = once;
    }
    public Promotion(int id, Item item, String code, String notes, short once, Date since, Date until, Set<BaseUser> baseUsers) {
       this.id = id;
       this.item = item;
       this.code = code;
       this.notes = notes;
       this.once = once;
       this.since = since;
       this.until = until;
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
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    @Column(name="code", nullable=false, length=50)
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Column(name="notes", length=200)
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Column(name="once", nullable=false)
    public short getOnce() {
        return this.once;
    }
    
    public void setOnce(short once) {
        this.once = once;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="since", length=13)
    public Date getSince() {
        return this.since;
    }
    
    public void setSince(Date since) {
        this.since = since;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="until", length=13)
    public Date getUntil() {
        return this.until;
    }
    
    public void setUntil(Date until) {
        this.until = until;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="promotion_user_map", joinColumns = { 
        @JoinColumn(name="promotion_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="user_id", updatable=false) })
    public Set<BaseUser> getBaseUsers() {
        return this.baseUsers;
    }
    
    public void setBaseUsers(Set<BaseUser> baseUsers) {
        this.baseUsers = baseUsers;
    }




}


