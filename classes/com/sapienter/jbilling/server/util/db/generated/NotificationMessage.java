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

import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.db.LanguageDTO;

@Entity
@Table(name="notification_message")
public class NotificationMessage  implements java.io.Serializable {


     private int id;
     private NotificationMessageType notificationMessageType;
     private CompanyDTO entity;
     private LanguageDTO language;
     private short useFlag;
     private Set<NotificationMessageSection> notificationMessageSections = new HashSet<NotificationMessageSection>(0);

    public NotificationMessage() {
    }

	
    public NotificationMessage(int id, CompanyDTO entity, LanguageDTO language, short useFlag) {
        this.id = id;
        this.entity = entity;
        this.language = language;
        this.useFlag = useFlag;
    }
    public NotificationMessage(int id, NotificationMessageType notificationMessageType, CompanyDTO entity, LanguageDTO language, short useFlag, Set<NotificationMessageSection> notificationMessageSections) {
       this.id = id;
       this.notificationMessageType = notificationMessageType;
       this.entity = entity;
       this.language = language;
       this.useFlag = useFlag;
       this.notificationMessageSections = notificationMessageSections;
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
    public NotificationMessageType getNotificationMessageType() {
        return this.notificationMessageType;
    }
    
    public void setNotificationMessageType(NotificationMessageType notificationMessageType) {
        this.notificationMessageType = notificationMessageType;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="entity_id", nullable=false)
    public CompanyDTO getEntity() {
        return this.entity;
    }
    
    public void setEntity(CompanyDTO entity) {
        this.entity = entity;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="language_id", nullable=false)
    public LanguageDTO getLanguage() {
        return this.language;
    }
    
    public void setLanguage(LanguageDTO language) {
        this.language = language;
    }
    
    @Column(name="use_flag", nullable=false)
    public short getUseFlag() {
        return this.useFlag;
    }
    
    public void setUseFlag(short useFlag) {
        this.useFlag = useFlag;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="notificationMessage")
    public Set<NotificationMessageSection> getNotificationMessageSections() {
        return this.notificationMessageSections;
    }
    
    public void setNotificationMessageSections(Set<NotificationMessageSection> notificationMessageSections) {
        this.notificationMessageSections = notificationMessageSections;
    }




}


