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
@Table(name="item_user_price"
    ,schema="public"
)
public class ItemUserPrice  implements java.io.Serializable {


     private int id;
     private BaseUser baseUser;
     private Currency currency;
     private Item item;
     private double price;

    public ItemUserPrice() {
    }

	
    public ItemUserPrice(int id, Currency currency, double price) {
        this.id = id;
        this.currency = currency;
        this.price = price;
    }
    public ItemUserPrice(int id, BaseUser baseUser, Currency currency, Item item, double price) {
       this.id = id;
       this.baseUser = baseUser;
       this.currency = currency;
       this.item = item;
       this.price = price;
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
    public Currency getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
    
    @Column(name="price", nullable=false, precision=17, scale=17)
    public double getPrice() {
        return this.price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }




}


