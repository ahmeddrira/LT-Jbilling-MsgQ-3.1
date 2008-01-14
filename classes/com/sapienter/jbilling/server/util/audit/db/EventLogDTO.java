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
package com.sapienter.jbilling.server.util.audit.db;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.sapienter.jbilling.server.util.db.generated.BaseUser;
import com.sapienter.jbilling.server.util.db.generated.Company;
import com.sapienter.jbilling.server.util.db.generated.JbillingTable;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@TableGenerator(
        name="event_log_GEN",
        table="jbilling_table",
        pkColumnName = "name",
        valueColumnName = "next_id",
        pkColumnValue="event_log",
        allocationSize=10
        )
@Table(name="event_log")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class EventLogDTO  implements java.io.Serializable {

    @Id @GeneratedValue(strategy=GenerationType.TABLE, generator="event_log_GEN")
    @Column(name="id", unique=true, nullable=false)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="table_id")
    private JbillingTable jbillingTable;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private BaseUser baseUser;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="message_id", nullable=false)
    private EventLogMessageDTO eventLogMessage;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="module_id", nullable=false)
    private EventLogModuleDTO eventLogModule;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="entity_id")
    private Company company;

    @Column(name="foreign_id", nullable=false)
    private int foreignId;

    @Column(name="create_datetime", nullable=false, length=29)
    private Date createDatetime;

    @Column(name="level_field", nullable=false)
    private int levelField;

    @Column(name="old_num")
    private Integer oldNum;

    @Column(name="old_str", length=1000)
    private String oldStr;

    @Column(name="old_date", length=29)
    private Date oldDate;     

    @Version
    @Column(name="OPTLOCK")
    private Integer versionNum;


    protected EventLogDTO() {
    }

	
    public EventLogDTO(Integer id, JbillingTable jbillingTable, BaseUser baseUser, EventLogMessageDTO eventLogMessage, EventLogModuleDTO eventLogModule, Company entity, int foreignId, int levelField, Integer oldNum, String oldStr, Date oldDate) {
       this.id = id;
       this.jbillingTable = jbillingTable;
       this.baseUser = baseUser;
       this.eventLogMessage = eventLogMessage;
       this.eventLogModule = eventLogModule;
       this.company = entity;
       this.foreignId = foreignId;
       this.createDatetime = Calendar.getInstance().getTime();
       this.levelField = levelField;
       this.oldNum = oldNum;
       this.oldStr = oldStr;
       this.oldDate = oldDate;
    }
   
    public Integer getId() {
        return id;
    }
    
    public JbillingTable getJbillingTable() {
        return this.jbillingTable;
    }
    
    public BaseUser getBaseUser() {
        return this.baseUser;
    }
    
    public EventLogMessageDTO getEventLogMessage() {
        return this.eventLogMessage;
    }
    
    public EventLogModuleDTO getEventLogModule() {
        return this.eventLogModule;
    }
        
    public Company getCompany() {
        return this.company;
    }
        
    public int getForeignId() {
        return this.foreignId;
    }

    public Date getCreateDatetime() {
        return this.createDatetime;
    }
    
    public int getLevelField() {
        return this.levelField;
    }
    
    public Integer getOldNum() {
        return this.oldNum;
    }
    
    public String getOldStr() {
        return this.oldStr;
    }

    public Date getOldDate() {
        return this.oldDate;
    }

    protected int getVersionNum() { return versionNum; }


}


