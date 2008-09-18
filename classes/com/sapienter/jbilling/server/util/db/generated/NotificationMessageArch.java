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


import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sapienter.jbilling.server.user.db.UserDTO;

@Entity
@Table(name="notification_message_arch")
public class NotificationMessageArch  implements java.io.Serializable {


     private int id;
     private UserDTO baseUser;
     private Integer typeId;
     private Date createDatetime;
     private String resultMessage;
     private Set<NotificationMessageArchLine> notificationMessageArchLines = new HashSet<NotificationMessageArchLine>(0);

    public NotificationMessageArch() {
    }

	
    public NotificationMessageArch(int id, Date createDatetime) {
        this.id = id;
        this.createDatetime = createDatetime;
    }
    public NotificationMessageArch(int id, UserDTO baseUser, Integer typeId, Date createDatetime, String resultMessage, Set<NotificationMessageArchLine> notificationMessageArchLines) {
       this.id = id;
       this.baseUser = baseUser;
       this.typeId = typeId;
       this.createDatetime = createDatetime;
       this.resultMessage = resultMessage;
       this.notificationMessageArchLines = notificationMessageArchLines;
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
    @JoinColumn(name="user_id")
    public UserDTO getBaseUser() {
        return this.baseUser;
    }
    
    public void setBaseUser(UserDTO baseUser) {
        this.baseUser = baseUser;
    }
    
    @Column(name="type_id")
    public Integer getTypeId() {
        return this.typeId;
    }
    
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    
    @Column(name="result_message", length=200)
    public String getResultMessage() {
        return this.resultMessage;
    }
    
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="notificationMessageArch")
    public Set<NotificationMessageArchLine> getNotificationMessageArchLines() {
        return this.notificationMessageArchLines;
    }
    
    public void setNotificationMessageArchLines(Set<NotificationMessageArchLine> notificationMessageArchLines) {
        this.notificationMessageArchLines = notificationMessageArchLines;
    }




}


