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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sapienter.jbilling.server.user.db.BaseUser;

@Entity
@Table(name="language")
public class Language  implements java.io.Serializable {


     private int id;
     private String code;
     private String description;
     private Set<NotificationMessage> notificationMessages = new HashSet<NotificationMessage>(0);
     private Set<Company> entities = new HashSet<Company>(0);
     private Set<BaseUser> baseUsers = new HashSet<BaseUser>(0);
     private Set<InternationalDescription> internationalDescriptions = new HashSet<InternationalDescription>(0);

    public Language() {
    }

	
    public Language(int id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }
    public Language(int id, String code, String description, Set<NotificationMessage> notificationMessages, Set<Company> entities, Set<BaseUser> baseUsers, Set<InternationalDescription> internationalDescriptions) {
       this.id = id;
       this.code = code;
       this.description = description;
       this.notificationMessages = notificationMessages;
       this.entities = entities;
       this.baseUsers = baseUsers;
       this.internationalDescriptions = internationalDescriptions;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="code", nullable=false, length=2)
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Column(name="description", nullable=false, length=50)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="language")
    public Set<NotificationMessage> getNotificationMessages() {
        return this.notificationMessages;
    }
    
    public void setNotificationMessages(Set<NotificationMessage> notificationMessages) {
        this.notificationMessages = notificationMessages;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="language")
    public Set<Company> getEntities() {
        return this.entities;
    }
    
    public void setEntities(Set<Company> entities) {
        this.entities = entities;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="language")
    public Set<BaseUser> getBaseUsers() {
        return this.baseUsers;
    }
    
    public void setBaseUsers(Set<BaseUser> baseUsers) {
        this.baseUsers = baseUsers;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="language")
    public Set<InternationalDescription> getInternationalDescriptions() {
        return this.internationalDescriptions;
    }
    
    public void setInternationalDescriptions(Set<InternationalDescription> internationalDescriptions) {
        this.internationalDescriptions = internationalDescriptions;
    }




}


