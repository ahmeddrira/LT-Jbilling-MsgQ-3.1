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

@Entity
@Table(name="notification_message_section")
public class NotificationMessageSection  implements java.io.Serializable {


     private int id;
     private NotificationMessage notificationMessage;
     private Integer section;
     private Set<NotificationMessageLine> notificationMessageLines = new HashSet<NotificationMessageLine>(0);

    public NotificationMessageSection() {
    }

	
    public NotificationMessageSection(int id) {
        this.id = id;
    }
    public NotificationMessageSection(int id, NotificationMessage notificationMessage, Integer section, Set<NotificationMessageLine> notificationMessageLines) {
       this.id = id;
       this.notificationMessage = notificationMessage;
       this.section = section;
       this.notificationMessageLines = notificationMessageLines;
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
    @JoinColumn(name="message_id")
    public NotificationMessage getNotificationMessage() {
        return this.notificationMessage;
    }
    
    public void setNotificationMessage(NotificationMessage notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
    
    @Column(name="section")
    public Integer getSection() {
        return this.section;
    }
    
    public void setSection(Integer section) {
        this.section = section;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="notificationMessageSection")
    public Set<NotificationMessageLine> getNotificationMessageLines() {
        return this.notificationMessageLines;
    }
    
    public void setNotificationMessageLines(Set<NotificationMessageLine> notificationMessageLines) {
        this.notificationMessageLines = notificationMessageLines;
    }




}


