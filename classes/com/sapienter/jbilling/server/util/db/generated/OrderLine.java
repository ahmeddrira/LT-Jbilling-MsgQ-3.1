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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sapienter.jbilling.server.order.db.PurchaseOrder;

@Entity
@Table(name="order_line"
    ,schema="public"
)
public class OrderLine  implements java.io.Serializable {


     private int id;
     private OrderLineType orderLineType;
     private Item item;
     private PurchaseOrder purchaseOrder;
     private double amount;
     private Integer quantity;
     private Double price;
     private Short itemPrice;
     private Date createDatetime;
     private short deleted;
     private String description;

    public OrderLine() {
    }

	
    public OrderLine(int id, double amount, Date createDatetime, short deleted) {
        this.id = id;
        this.amount = amount;
        this.createDatetime = createDatetime;
        this.deleted = deleted;
    }
    public OrderLine(int id, OrderLineType orderLineType, Item item, PurchaseOrder purchaseOrder, double amount, Integer quantity, Double price, Short itemPrice, Date createDatetime, short deleted, String description) {
       this.id = id;
       this.orderLineType = orderLineType;
       this.item = item;
       this.purchaseOrder = purchaseOrder;
       this.amount = amount;
       this.quantity = quantity;
       this.price = price;
       this.itemPrice = itemPrice;
       this.createDatetime = createDatetime;
       this.deleted = deleted;
       this.description = description;
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
    @JoinColumn(name="type_id")
    public OrderLineType getOrderLineType() {
        return this.orderLineType;
    }
    
    public void setOrderLineType(OrderLineType orderLineType) {
        this.orderLineType = orderLineType;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    public Item getItem() {
        return this.item;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id")
    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }
    
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
    
    @Column(name="amount", nullable=false, precision=17, scale=17)
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    @Column(name="quantity")
    public Integer getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    @Column(name="price", precision=17, scale=17)
    public Double getPrice() {
        return this.price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    @Column(name="item_price")
    public Short getItemPrice() {
        return this.itemPrice;
    }
    
    public void setItemPrice(Short itemPrice) {
        this.itemPrice = itemPrice;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    
    @Column(name="deleted", nullable=false)
    public short getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="description", length=1000)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }




}


