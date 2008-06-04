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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="notification_message_type")
public class NotificationMessageType  implements java.io.Serializable {


     private int id;
     private int sections;
     private Set<NotificationMessage> notificationMessages = new HashSet<NotificationMessage>(0);

    public NotificationMessageType() {
    }

	
    public NotificationMessageType(int id, int sections) {
        this.id = id;
        this.sections = sections;
    }
    public NotificationMessageType(int id, int sections, Set<NotificationMessage> notificationMessages) {
       this.id = id;
       this.sections = sections;
       this.notificationMessages = notificationMessages;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="sections", nullable=false)
    public int getSections() {
        return this.sections;
    }
    
    public void setSections(int sections) {
        this.sections = sections;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="notificationMessageType")
    public Set<NotificationMessage> getNotificationMessages() {
        return this.notificationMessages;
    }
    
    public void setNotificationMessages(Set<NotificationMessage> notificationMessages) {
        this.notificationMessages = notificationMessages;
    }




}


