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
package com.sapienter.jbilling.server.item.db;


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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.db.AbstractDescription;
import com.sapienter.jbilling.server.util.db.generated.InvoiceLine;
import com.sapienter.jbilling.server.util.db.generated.ItemPrice;
import com.sapienter.jbilling.server.util.db.generated.ItemType;
import com.sapienter.jbilling.server.util.db.generated.ItemUserPrice;
import com.sapienter.jbilling.server.util.db.generated.Promotion;

@Entity
@Table(name="item")
public class Item extends AbstractDescription {


     private int id;
     private CompanyDTO entity;
     private String internalNumber;
     private Double percentage;
     private short priceManual;
     private short deleted;
     private short hasDecimals;
     private Set<OrderLineDTO> orderLineDTOs = new HashSet<OrderLineDTO>(0);
     private Set<Promotion> promotions = new HashSet<Promotion>(0);
     private Set<ItemType> itemTypes = new HashSet<ItemType>(0);
     private Set<InvoiceLine> invoiceLines = new HashSet<InvoiceLine>(0);
     private Set<ItemUserPrice> itemUserPrices = new HashSet<ItemUserPrice>(0);
     private Set<ItemPrice> itemPrices = new HashSet<ItemPrice>(0);

    public Item() {
    }

	
    public Item(int id, short priceManual, short deleted, short hasDecimals) {
        this.id = id;
        this.priceManual = priceManual;
        this.deleted = deleted;
        this.hasDecimals = hasDecimals;
    }
    public Item(int id, CompanyDTO entity, String internalNumber, Double percentage, short priceManual, short deleted, short hasDecimals, Set<OrderLineDTO> orderLineDTOs, Set<Promotion> promotions, Set<ItemType> itemTypes, Set<InvoiceLine> invoiceLines, Set<ItemUserPrice> itemUserPrices, Set<ItemPrice> itemPrices) {
       this.id = id;
       this.entity = entity;
       this.internalNumber = internalNumber;
       this.percentage = percentage;
       this.priceManual = priceManual;
       this.deleted = deleted;
       this.hasDecimals = hasDecimals;
       this.orderLineDTOs = orderLineDTOs;
       this.promotions = promotions;
       this.itemTypes = itemTypes;
       this.invoiceLines = invoiceLines;
       this.itemUserPrices = itemUserPrices;
       this.itemPrices = itemPrices;
    }

    @Transient
    protected String getTable() {
        return Constants.TABLE_ITEM;
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
    @JoinColumn(name="entity_id")
    public CompanyDTO getEntity() {
        return this.entity;
    }
    
    public void setEntity(CompanyDTO entity) {
        this.entity = entity;
    }
    
    @Column(name="internal_number", length=50)
    public String getInternalNumber() {
        return this.internalNumber;
    }
    
    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }
    
    @Column(name="percentage", precision=17, scale=17)
    public Double getPercentage() {
        return this.percentage;
    }
    
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
    
    @Column(name="price_manual", nullable=false)
    public short getPriceManual() {
        return this.priceManual;
    }
    
    public void setPriceManual(short priceManual) {
        this.priceManual = priceManual;
    }
    
    @Column(name="deleted", nullable=false)
    public short getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(short deleted) {
        this.deleted = deleted;
    }
    
    @Column(name="has_decimals", nullable=false)
    public short getHasDecimals() {
        return this.hasDecimals;
    }
    
    public void setHasDecimals(short hasDecimals) {
        this.hasDecimals = hasDecimals;
    }
    
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="item")
    public Set<OrderLineDTO> getOrderLines() {
        return this.orderLineDTOs;
    }
    
    public void setOrderLines(Set<OrderLineDTO> orderLineDTOs) {
        this.orderLineDTOs = orderLineDTOs;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="item")
    public Set<Promotion> getPromotions() {
        return this.promotions;
    }
    
    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="item_type_map", joinColumns = { 
        @JoinColumn(name="item_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="type_id", updatable=false) })
    public Set<ItemType> getItemTypes() {
        return this.itemTypes;
    }
    
    public void setItemTypes(Set<ItemType> itemTypes) {
        this.itemTypes = itemTypes;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="item")
    public Set<InvoiceLine> getInvoiceLines() {
        return this.invoiceLines;
    }
    
    public void setInvoiceLines(Set<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="item")
    public Set<ItemUserPrice> getItemUserPrices() {
        return this.itemUserPrices;
    }
    
    public void setItemUserPrices(Set<ItemUserPrice> itemUserPrices) {
        this.itemUserPrices = itemUserPrices;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="item")
    public Set<ItemPrice> getItemPrices() {
        return this.itemPrices;
    }
    
    public void setItemPrices(Set<ItemPrice> itemPrices) {
        this.itemPrices = itemPrices;
    }

    @Transient
    public String getNumber() {
    	return getInternalNumber();
    }
}


