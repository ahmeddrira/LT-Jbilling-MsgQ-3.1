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
package com.sapienter.jbilling.server.util.db;


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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.sapienter.jbilling.server.invoice.db.Invoice;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.user.partner.db.Partner;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.db.generated.ItemPrice;
import com.sapienter.jbilling.server.util.db.generated.ItemUserPrice;
import com.sapienter.jbilling.server.util.db.generated.Payment;
import com.sapienter.jbilling.server.util.db.generated.ProcessRunTotal;

@Entity
@Table(name="currency")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CurrencyDTO extends AbstractDescription  implements java.io.Serializable {


     private int id;
     private String symbol;
     private String code;
     private String countryCode;
     private Set<CompanyDTO> entities = new HashSet<CompanyDTO>(0);
     private Set<UserDTO> baseUsers = new HashSet<UserDTO>(0);
     private Set<OrderDTO> orderDTOs = new HashSet<OrderDTO>(0);
     private Set<Partner> partners = new HashSet<Partner>(0);
     private Set<Payment> payments = new HashSet<Payment>(0);
     private Set<CurrencyExchangeDTO> currencyExchanges = new HashSet<CurrencyExchangeDTO>(0);
     private Set<CompanyDTO> entities_1 = new HashSet<CompanyDTO>(0);
     private Set<Invoice> invoices = new HashSet<Invoice>(0);
     private Set<ItemPrice> itemPrices = new HashSet<ItemPrice>(0);
     private Set<ProcessRunTotal> processRunTotals = new HashSet<ProcessRunTotal>(0);
     private Set<ItemUserPrice> itemUserPrices = new HashSet<ItemUserPrice>(0);

     // from EX
     private String name = null;
     private Boolean inUse = null;
     private String rate = null; // will be converted to float
     private Double sysRate = null;


    public CurrencyDTO() {
    }

    // for stubs
    public CurrencyDTO(Integer id) {
    	this.id = id;
    }
	
    public CurrencyDTO(int id, String symbol, String code, String countryCode) {
        this.id = id;
        this.symbol = symbol;
        this.code = code;
        this.countryCode = countryCode;
    }
    public CurrencyDTO(int id, String symbol, String code, String countryCode, Set<CompanyDTO> entities, Set<UserDTO> baseUsers, Set<OrderDTO> orderDTOs, Set<Partner> partners, Set<Payment> payments, Set<CurrencyExchangeDTO> currencyExchanges, Set<CompanyDTO> entities_1, Set<Invoice> invoices, Set<ItemPrice> itemPrices, Set<ProcessRunTotal> processRunTotals, Set<ItemUserPrice> itemUserPrices) {
       this.id = id;
       this.symbol = symbol;
       this.code = code;
       this.countryCode = countryCode;
       this.entities = entities;
       this.baseUsers = baseUsers;
       this.orderDTOs = orderDTOs;
       this.partners = partners;
       this.payments = payments;
       this.currencyExchanges = currencyExchanges;
       this.entities_1 = entities_1;
       this.invoices = invoices;
       this.itemPrices = itemPrices;
       this.processRunTotals = processRunTotals;
       this.itemUserPrices = itemUserPrices;
    }
 
    @Transient
    protected String getTable() {
    	return Constants.TABLE_CURRENCY;
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
    public Set<CompanyDTO> getEntities() {
        return this.entities;
    }
    
    public void setEntities(Set<CompanyDTO> entities) {
        this.entities = entities;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<UserDTO> getBaseUsers() {
        return this.baseUsers;
    }
    
    public void setBaseUsers(Set<UserDTO> baseUsers) {
        this.baseUsers = baseUsers;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="currency")
    public Set<OrderDTO> getPurchaseOrders() {
        return this.orderDTOs;
    }
    
    public void setPurchaseOrders(Set<OrderDTO> orderDTOs) {
        this.orderDTOs = orderDTOs;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="feeCurrency")
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
    public Set<CurrencyExchangeDTO> getCurrencyExchanges() {
        return this.currencyExchanges;
    }
    
    public void setCurrencyExchanges(Set<CurrencyExchangeDTO> currencyExchanges) {
        this.currencyExchanges = currencyExchanges;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="currency_entity_map", joinColumns = { 
        @JoinColumn(name="currency_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) })
    public Set<CompanyDTO> getEntities_1() {
        return this.entities_1;
    }
    
    public void setEntities_1(Set<CompanyDTO> entities_1) {
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

    @Transient
    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    @Transient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Transient
    public Double getSysRate() {
        return sysRate;
    }

    public void setSysRate(Double sysRate) {
        this.sysRate = sysRate;
    }
}


