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


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sapienter.jbilling.server.user.db.BaseUser;

@Entity
@Table(name="report_user"
    ,schema="public"
)
public class ReportUser  implements java.io.Serializable {


     private int id;
     private BaseUser baseUser;
     private Report report;
     private Date createDatetime;
     private String title;

    public ReportUser() {
    }

	
    public ReportUser(int id, Date createDatetime) {
        this.id = id;
        this.createDatetime = createDatetime;
    }
    public ReportUser(int id, BaseUser baseUser, Report report, Date createDatetime, String title) {
       this.id = id;
       this.baseUser = baseUser;
       this.report = report;
       this.createDatetime = createDatetime;
       this.title = title;
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
    public BaseUser getBaseUser() {
        return this.baseUser;
    }
    
    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="report_id")
    public Report getReport() {
        return this.report;
    }
    
    public void setReport(Report report) {
        this.report = report;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_datetime", nullable=false, length=29)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
    
    @Column(name="title", length=50)
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }




}


