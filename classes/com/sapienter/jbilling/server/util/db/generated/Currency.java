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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sapienter.jbilling.server.order.db.PurchaseOrder;
import com.sapienter.jbilling.server.user.db.BaseUser;

@Entity
@Table(name="currency")
public class Currency  implements java.io.Serializable {


     private int id;
     private String symbol;
     private String code;
     private String countryCode;
     private Set<Company> entities = new HashSet<Company>(0);
     private Set<BaseUser> baseUsers = new HashSet<BaseUser>(0);
     private Set<PurchaseOrder> purchaseOrders = new HashSet<PurchaseOrder>(0);
     private Set<Partner> partners = new HashSet<Partner>(0);
     private Set<Payment> payments = new HashSet<Payment>(0);
     private Set<CurrencyExchange> currencyExchanges = new HashSet<CurrencyExchange>(0);
     private Set<Company> entities_1 = new HashSet<Company>(0);
     private Set<Invoice> invoices = new HashSet<Invoice>(0);
     private Set<ItemPrice> itemPrices = new HashSet<ItemPrice>(0);
     private Set<ProcessRunTotal> processRunTotals = new HashSet<ProcessRunTotal>(0);
     private Set<ItemUserPrice> itemUserPrices = new HashSet<ItemUserPrice>(0);

    public Currency() {
    }

	
    public Currency(int id, String symbol, String code, String countryCode) {
        this.id = id;
        this.symbol = symbol;
        this.code = code;
        this.countryCode = countryCode;
    }
    public Currency(int id, String symbol, String code, String countryCode, Set<Company> entities, Set<BaseUser> baseUsers, Set<PurchaseOrder> purchaseOrders, Set<Partner> partners, Set<Payment> payments, Set<CurrencyExchange> currencyExchanges, Set<Company> entities_1, Set<Invoice> invoices, Set<ItemPrice> itemPrices, Set<ProcessRunTotal> processRunTotals, Set<ItemUserPrice> itemUserPrices) {
       this.id = id;
       this.symbol = symbol;
       this.code = code;
       this.countryCode = countryCode;
       this.entities = entities;
       this.baseUsers = baseUsers;
       this.purchaseOrders = purchaseOrders;
       this.partners = partners;
       this.payments = payments;
       this.currencyExchanges = currencyExchanges;
       this.entities_1 = entities_1;
       this.invoices = invoices;
       this.itemPrices = itemPrices;
       this.processRunTotals = processRunTotals;
       this.itemUserPrices = itemUserPrices;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="symbol", nullable=false, length=10)
    public String getSymbol() {
        return this.symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    @Column(name="code", nullable=false, length=3)
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Column(name="country_code", nullable=false, length=2)
    public String getCountryCode() {
        return this.countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<Company> getEntities() {
        return this.entities;
    }
    
    public void setEntities(Set<Company> entities) {
        this.entities = entities;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<BaseUser> getBaseUsers() {
        return this.baseUsers;
    }
    
    public void setBaseUsers(Set<BaseUser> baseUsers) {
        this.baseUsers = baseUsers;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }
    
    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<Partner> getPartners() {
        return this.partners;
    }
    
    public void setPartners(Set<Partner> partners) {
        this.partners = partners;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<Payment> getPayments() {
        return this.payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<CurrencyExchange> getCurrencyExchanges() {
        return this.currencyExchanges;
    }
    
    public void setCurrencyExchanges(Set<CurrencyExchange> currencyExchanges) {
        this.currencyExchanges = currencyExchanges;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="currency_entity_map", joinColumns = { 
        @JoinColumn(name="currency_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) })
    public Set<Company> getEntities_1() {
        return this.entities_1;
    }
    
    public void setEntities_1(Set<Company> entities_1) {
        this.entities_1 = entities_1;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<Invoice> getInvoices() {
        return this.invoices;
    }
    
    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<ItemPrice> getItemPrices() {
        return this.itemPrices;
    }
    
    public void setItemPrices(Set<ItemPrice> itemPrices) {
        this.itemPrices = itemPrices;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<ProcessRunTotal> getProcessRunTotals() {
        return this.processRunTotals;
    }
    
    public void setProcessRunTotals(Set<ProcessRunTotal> processRunTotals) {
        this.processRunTotals = processRunTotals;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<ItemUserPrice> getItemUserPrices() {
        return this.itemUserPrices;
    }
    
    public void setItemUserPrices(Set<ItemUserPrice> itemUserPrices) {
        this.itemUserPrices = itemUserPrices;
    }




}


