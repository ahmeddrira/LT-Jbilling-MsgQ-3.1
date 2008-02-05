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

import com.sapienter.jbilling.server.order.db.PurchaseOrder;
import com.sapienter.jbilling.server.util.audit.db.EventLogDTO;
import com.sapienter.jbilling.server.util.db.generated.Ach;
import com.sapienter.jbilling.server.util.db.generated.Company;
import com.sapienter.jbilling.server.util.db.generated.CreditCard;
import com.sapienter.jbilling.server.util.db.generated.Currency;
import com.sapienter.jbilling.server.util.db.generated.Customer;
import com.sapienter.jbilling.server.util.db.generated.Invoice;
import com.sapienter.jbilling.server.util.db.generated.ItemUserPrice;
import com.sapienter.jbilling.server.util.db.generated.Language;
import com.sapienter.jbilling.server.util.db.generated.NotificationMessageArch;
import com.sapienter.jbilling.server.util.db.generated.Partner;
import com.sapienter.jbilling.server.util.db.generated.Payment;
import com.sapienter.jbilling.server.util.db.generated.PermissionUser;
import com.sapienter.jbilling.server.util.db.generated.Promotion;
import com.sapienter.jbilling.server.util.db.generated.ReportUser;
import com.sapienter.jbilling.server.util.db.generated.Role;
import com.sapienter.jbilling.server.util.db.generated.SubscriberStatus;
import com.sapienter.jbilling.server.util.db.generated.UserStatus;

/*
 * TODO: This class is not fully done for JPA. It is not replacing UserEntity.
 * When it does, rename it (UserDTO) and add the generator.
 */

@Entity
@Table(name="base_user")
public class BaseUser  implements java.io.Serializable {


     private int id;
     private Currency currency;
     private Company company;
     private SubscriberStatus subscriberStatus;
     private UserStatus userStatus;
     private Language language;
     private String password;
     private int deleted;
     private Date createDatetime;
     private Date lastStatusChange;
     private Date lastLogin;
     private String userName;
     private int failedAttempts;
     private Set<Payment> payments = new HashSet<Payment>(0);
     private Set<Ach> achs = new HashSet<Ach>(0);
     private Set<PermissionUser> permissionUsers = new HashSet<PermissionUser>(0);
     private Set<ReportUser> reportUsers = new HashSet<ReportUser>(0);
     private Set<Partner> partnersForRelatedClerk = new HashSet<Partner>(0);
     private Set<Customer> customers = new HashSet<Customer>(0);
     private Set<Partner> partnersForUserId = new HashSet<Partner>(0);
     private Set<PurchaseOrder> purchaseOrdersForCreatedBy = new HashSet<PurchaseOrder>(0);
     private Set<PurchaseOrder> purchaseOrdersForUserId = new HashSet<PurchaseOrder>(0);
     private Set<CreditCard> creditCards = new HashSet<CreditCard>(0);
     private Set<NotificationMessageArch> notificationMessageArchs = new HashSet<NotificationMessageArch>(0);
     private Set<Role> roles = new HashSet<Role>(0);
     private Set<Promotion> promotions = new HashSet<Promotion>(0);
     private Set<EventLogDTO> eventLogs = new HashSet<EventLogDTO>(0);
     private Set<Invoice> invoices = new HashSet<Invoice>(0);
     private Set<ItemUserPrice> itemUserPrices = new HashSet<ItemUserPrice>(0);

    public BaseUser() {
    }

	
    public BaseUser(int id, short deleted, Date createDatetime, int failedAttempts) {
        this.id = id;
        this.deleted = deleted;
        this.createDatetime = createDatetime;
        this.failedAttempts = failedAttempts;
    }
    public BaseUser(int id, Currency currency, Company entity, SubscriberStatus subscriberStatus, UserStatus userStatus, Language language, String password, short deleted, Date createDatetime, Date lastStatusChange, Date lastLogin, String userName, int failedAttempts, Set<Payment> payments, Set<Ach> achs, Set<PermissionUser> permissionUsers, Set<ReportUser> reportUsers, Set<Partner> partnersForRelatedClerk, Set<Customer> customers, Set<Partner> partnersForUserId, Set<PurchaseOrder> purchaseOrdersForCreatedBy, Set<PurchaseOrder> purchaseOrdersForUserId, Set<CreditCard> creditCards, Set<NotificationMessageArch> notificationMessageArchs, Set<Role> roles, Set<Promotion> promotions, Set<EventLogDTO> eventLogs, Set<Invoice> invoices, Set<ItemUserPrice> itemUserPrices) {
       this.id = id;
       this.currency = currency;
       this.company = entity;
       this.subscriberStatus = subscriberStatus;
       this.userStatus = userStatus;
       this.language = language;
       this.password = password;
       this.deleted = deleted;
       this.createDatetime = createDatetime;
       this.lastStatusChange = lastStatusChange;
       this.lastLogin = lastLogin;
       this.userName = userName;
       this.failedAttempts = failedAttempts;
       this.payments = payments;
       this.achs = achs;
       this.permissionUsers = permissionUsers;
       this.reportUsers = reportUsers;
       this.partnersForRelatedClerk = partnersForRelatedClerk;
       this.customers = customers;
       this.partnersForUserId = partnersForUserId;
       this.purchaseOrdersForCreatedBy = purchaseOrdersForCreatedBy;
       this.purchaseOrdersForUserId = purchaseOrdersForUserId;
       this.creditCards = creditCards;
       this.notificationMessageArchs = notificationMessageArchs;
       this.roles = roles;
       this.promotions = promotions;
       this.eventLogs = eventLogs;
       this.invoices = invoices;
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
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="currency_id")
    public Currency getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="entity_id")
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company entity) {
        this.company = entity;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="subscriber_status")
    public SubscriberStatus getSubscriberStatus() {
        return this.subscriberStatus;
    }
    
    public void setSubscriberStatus(SubscriberStatus subscriberStatus) {
        this.subscriberStatus = subscriberStatus;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="status_id")
    public UserStatus getUserStatus() {
        return this.userStatus;
    }
    
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="language_id")
    public Language getLanguage() {
        return this.language;
    }
    
    public void setLanguage(Language language) {
        this.language = language;
    }
    
    @Column(name="password", length=40)
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Column(name="deleted", nullable=false)
    public int getDeleted() {
        return this.deleted;
    }
    
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_status_change", length=29)
    public Date getLastStatusChange() {
        return this.lastStatusChange;
    }
    
    public void setLastStatusChange(Date lastStatusChange) {
        this.lastStatusChange = lastStatusChange;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_login", length=29)
    public Date getLastLogin() {
        return this.lastLogin;
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    @Column(name="user_name", length=50)
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Column(name="failed_attempts", nullable=false)
    public int getFailedAttempts() {
        return this.failedAttempts;
    }
    
    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<Payment> getPayments() {
        return this.payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<Ach> getAchs() {
        return this.achs;
    }
    
    public void setAchs(Set<Ach> achs) {
        this.achs = achs;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<PermissionUser> getPermissionUsers() {
        return this.permissionUsers;
    }
    
    public void setPermissionUsers(Set<PermissionUser> permissionUsers) {
        this.permissionUsers = permissionUsers;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<ReportUser> getReportUsers() {
        return this.reportUsers;
    }
    
    public void setReportUsers(Set<ReportUser> reportUsers) {
        this.reportUsers = reportUsers;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUserByRelatedClerk")
    public Set<Partner> getPartnersForRelatedClerk() {
        return this.partnersForRelatedClerk;
    }
    
    public void setPartnersForRelatedClerk(Set<Partner> partnersForRelatedClerk) {
        this.partnersForRelatedClerk = partnersForRelatedClerk;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<Customer> getCustomers() {
        return this.customers;
    }
    
    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUserByUserId")
    public Set<Partner> getPartnersForUserId() {
        return this.partnersForUserId;
    }
    
    public void setPartnersForUserId(Set<Partner> partnersForUserId) {
        this.partnersForUserId = partnersForUserId;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUserByCreatedBy")
    public Set<PurchaseOrder> getPurchaseOrdersForCreatedBy() {
        return this.purchaseOrdersForCreatedBy;
    }
    
    public void setPurchaseOrdersForCreatedBy(Set<PurchaseOrder> purchaseOrdersForCreatedBy) {
        this.purchaseOrdersForCreatedBy = purchaseOrdersForCreatedBy;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUserByUserId")
    public Set<PurchaseOrder> getPurchaseOrdersForUserId() {
        return this.purchaseOrdersForUserId;
    }
    
    public void setPurchaseOrdersForUserId(Set<PurchaseOrder> purchaseOrdersForUserId) {
        this.purchaseOrdersForUserId = purchaseOrdersForUserId;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="user_credit_card_map",  joinColumns = { 
        @JoinColumn(name="user_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="credit_card_id", updatable=false) })
    public Set<CreditCard> getCreditCards() {
        return this.creditCards;
    }
    
    public void setCreditCards(Set<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<NotificationMessageArch> getNotificationMessageArchs() {
        return this.notificationMessageArchs;
    }
    
    public void setNotificationMessageArchs(Set<NotificationMessageArch> notificationMessageArchs) {
        this.notificationMessageArchs = notificationMessageArchs;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="user_role_map", joinColumns = { 
        @JoinColumn(name="user_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="role_id", updatable=false) })
    public Set<Role> getRoles() {
        return this.roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="promotion_user_map",  joinColumns = { 
        @JoinColumn(name="user_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="promotion_id", updatable=false) })
    public Set<Promotion> getPromotions() {
        return this.promotions;
    }
    
    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<EventLogDTO> getEventLogs() {
        return this.eventLogs;
    }
    
    public void setEventLogs(Set<EventLogDTO> eventLogs) {
        this.eventLogs = eventLogs;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<Invoice> getInvoices() {
        return this.invoices;
    }
    
    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="baseUser")
    public Set<ItemUserPrice> getItemUserPrices() {
        return this.itemUserPrices;
    }
    
    public void setItemUserPrices(Set<ItemUserPrice> itemUserPrices) {
        this.itemUserPrices = itemUserPrices;
    }




}


