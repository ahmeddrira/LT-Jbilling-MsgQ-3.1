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
package com.sapienter.jbilling.server.user.db;


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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OrderBy;

import com.sapienter.jbilling.server.item.db.Item;
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO;
import com.sapienter.jbilling.server.process.db.BillingProcessDTO;
import com.sapienter.jbilling.server.user.contact.db.ContactFieldTypeDTO;
import com.sapienter.jbilling.server.user.contact.db.ContactTypeDTO;
import com.sapienter.jbilling.server.util.audit.db.EventLogDTO;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;
import com.sapienter.jbilling.server.util.db.LanguageDTO;
import com.sapienter.jbilling.server.util.db.generated.AgeingEntityStep;
import com.sapienter.jbilling.server.util.db.generated.BillingProcessConfiguration;
import com.sapienter.jbilling.server.util.db.generated.InvoiceDeliveryMethod;
import com.sapienter.jbilling.server.util.db.generated.ItemType;
import com.sapienter.jbilling.server.util.db.generated.ListEntity;
import com.sapienter.jbilling.server.util.db.generated.NotificationMessage;
import com.sapienter.jbilling.server.util.db.generated.PaymentMethod;
import com.sapienter.jbilling.server.util.db.generated.Report;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="entity")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class CompanyDTO  implements java.io.Serializable {


     private int id;
     private CurrencyDTO currencyDTO;
     private LanguageDTO language;
     private String externalId;
     private String description;
     private Date createDatetime;
     private Set<AgeingEntityStep> ageingEntitySteps = new HashSet<AgeingEntityStep>(0);
     private Set<PaymentMethod> paymentMethods = new HashSet<PaymentMethod>(0);
     private Set<OrderPeriodDTO> orderPeriodDTOs = new HashSet<OrderPeriodDTO>(0);
     private Set<BillingProcessDTO> billingProcesses = new HashSet<BillingProcessDTO>(0);
     private Set<UserDTO> baseUsers = new HashSet<UserDTO>(0);
     private Set<ContactTypeDTO> contactTypes = new HashSet<ContactTypeDTO>(0);
     private Set<Item> items = new HashSet<Item>(0);
     private Set<EventLogDTO> eventLogs = new HashSet<EventLogDTO>(0);
     private Set<NotificationMessage> notificationMessages = new HashSet<NotificationMessage>(0);
     private Set<Report> reports = new HashSet<Report>(0);
     private Set<ContactFieldTypeDTO> contactFieldTypes = new HashSet<ContactFieldTypeDTO>(0);
     private Set<CurrencyDTO> currencyDTOs = new HashSet<CurrencyDTO>(0);
     private Set<ItemType> itemTypes = new HashSet<ItemType>(0);
     private Set<BillingProcessConfiguration> billingProcessConfigurations = new HashSet<BillingProcessConfiguration>(0);
     private Set<InvoiceDeliveryMethod> invoiceDeliveryMethods = new HashSet<InvoiceDeliveryMethod>(0);
     private Set<ListEntity> listEntities = new HashSet<ListEntity>(0);

    public CompanyDTO() {
    }

    public CompanyDTO(int i) {
        id = i;
    }
	
    public CompanyDTO(int id, CurrencyDTO currencyDTO, LanguageDTO language, String description, Date createDatetime) {
        this.id = id;
        this.currencyDTO = currencyDTO;
        this.language = language;
        this.description = description;
        this.createDatetime = createDatetime;
    }
    public CompanyDTO(int id, CurrencyDTO currencyDTO, LanguageDTO language, String externalId, String description, Date createDatetime, Set<AgeingEntityStep> ageingEntitySteps, Set<PaymentMethod> paymentMethods, Set<OrderPeriodDTO> orderPeriodDTOs, Set<BillingProcessDTO> billingProcesses, Set<UserDTO> baseUsers, Set<ContactTypeDTO> contactTypes, Set<Item> items, Set<EventLogDTO> eventLogs, Set<NotificationMessage> notificationMessages, Set<Report> reports, Set<ContactFieldTypeDTO> contactFieldTypes, Set<CurrencyDTO> currencyDTOs, Set<ItemType> itemTypes, Set<BillingProcessConfiguration> billingProcessConfigurations, Set<InvoiceDeliveryMethod> invoiceDeliveryMethods, Set<ListEntity> listEntities) {
       this.id = id;
       this.currencyDTO = currencyDTO;
       this.language = language;
       this.externalId = externalId;
       this.description = description;
       this.createDatetime = createDatetime;
       this.ageingEntitySteps = ageingEntitySteps;
       this.paymentMethods = paymentMethods;
       this.orderPeriodDTOs = orderPeriodDTOs;
       this.billingProcesses = billingProcesses;
       this.baseUsers = baseUsers;
       this.contactTypes = contactTypes;
       this.items = items;
       this.eventLogs = eventLogs;
       this.notificationMessages = notificationMessages;
       this.reports = reports;
       this.contactFieldTypes = contactFieldTypes;
       this.currencyDTOs = currencyDTOs;
       this.itemTypes = itemTypes;
       this.billingProcessConfigurations = billingProcessConfigurations;
       this.invoiceDeliveryMethods = invoiceDeliveryMethods;
       this.listEntities = listEntities;
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
    @JoinColumn(name="currency_id", nullable=false)
    public CurrencyDTO getCurrency() {
        return this.currencyDTO;
    }
    
    public void setCurrency(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="language_id", nullable=false)
    public LanguageDTO getLanguage() {
        return this.language;
    }
    
    public void setLanguage(LanguageDTO language) {
        this.language = language;
    }
    
    @Column(name="external_id", length=20)
    public String getExternalId() {
        return this.externalId;
    }
    
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    
    @Column(name="description", nullable=false, length=100)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="company")
    @OrderBy (
        clause = "status_id"
    )
    public Set<AgeingEntityStep> getAgeingEntitySteps() {
        return this.ageingEntitySteps;
    }
    
    public void setAgeingEntitySteps(Set<AgeingEntityStep> ageingEntitySteps) {
        this.ageingEntitySteps = ageingEntitySteps;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="entity_payment_method_map", joinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="payment_method_id", updatable=false) })
    public Set<PaymentMethod> getPaymentMethods() {
        return this.paymentMethods;
    }
    
    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="company")
    public Set<OrderPeriodDTO> getOrderPeriods() {
        return this.orderPeriodDTOs;
    }
    public void setOrderPeriods(Set<OrderPeriodDTO> orderPeriodDTOs) {
        this.orderPeriodDTOs = orderPeriodDTOs;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
    public Set<BillingProcessDTO> getBillingProcesses() {
        return this.billingProcesses;
    }
    
    public void setBillingProcesses(Set<BillingProcessDTO> billingProcesses) {
        this.billingProcesses = billingProcesses;
    }
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy="company")
    @LazyCollection(value=LazyCollectionOption.EXTRA)
    @BatchSize(size=100)
    public Set<UserDTO> getBaseUsers() {
        return this.baseUsers;
    }
    
    public void setBaseUsers(Set<UserDTO> baseUsers) {
        this.baseUsers = baseUsers;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
    @OrderBy (
        clause = "id"
    )
    public Set<ContactTypeDTO> getContactTypes() {
        return this.contactTypes;
    }
    public void setContactTypes(Set<ContactTypeDTO> contactTypes) {
        this.contactTypes = contactTypes;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
    public Set<Item> getItems() {
        return this.items;
    }
    
    public void setItems(Set<Item> items) {
        this.items = items;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="company")
    public Set<EventLogDTO> getEventLogs() {
        return this.eventLogs;
    }
    
    public void setEventLogs(Set<EventLogDTO> eventLogs) {
        this.eventLogs = eventLogs;
    }
    
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
    public Set<NotificationMessage> getNotificationMessages() {
        return this.notificationMessages;
    }
    
    public void setNotificationMessages(Set<NotificationMessage> notificationMessages) {
        this.notificationMessages = notificationMessages;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="report_entity_map",joinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="report_id", updatable=false) })
    public Set<Report> getReports() {
        return this.reports;
    }
    
    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
    public Set<ContactFieldTypeDTO> getContactFieldTypes() {
        return this.contactFieldTypes;
    }
    
    public void setContactFieldTypes(Set<ContactFieldTypeDTO> contactFieldTypes) {
        this.contactFieldTypes = contactFieldTypes;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="currency_entity_map", joinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="currency_id", updatable=false) })
    public Set<CurrencyDTO> getCurrencies() {
        return this.currencyDTOs;
    }
    
    public void setCurrencies(Set<CurrencyDTO> currencyDTOs) {
        this.currencyDTOs = currencyDTOs;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
    public Set<ItemType> getItemTypes() {
        return this.itemTypes;
    }
    
    public void setItemTypes(Set<ItemType> itemTypes) {
        this.itemTypes = itemTypes;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
    public Set<BillingProcessConfiguration> getBillingProcessConfigurations() {
        return this.billingProcessConfigurations;
    }
    
    public void setBillingProcessConfigurations(Set<BillingProcessConfiguration> billingProcessConfigurations) {
        this.billingProcessConfigurations = billingProcessConfigurations;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="entity_delivery_method_map", joinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="method_id", updatable=false) })
    public Set<InvoiceDeliveryMethod> getInvoiceDeliveryMethods() {
        return this.invoiceDeliveryMethods;
    }
    
    public void setInvoiceDeliveryMethods(Set<InvoiceDeliveryMethod> invoiceDeliveryMethods) {
        this.invoiceDeliveryMethods = invoiceDeliveryMethods;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="entity")
    public Set<ListEntity> getListEntities() {
        return this.listEntities;
    }
    
    public void setListEntities(Set<ListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    /*
     * Conveniant methods to ease migration from entity beans
     */
    @Transient
    public Integer getCurrencyId() {
        return currencyDTO.getId();
    }
    
    @Transient
    public Integer getLanguageId() {
        return language.getId();
    }
    



}


