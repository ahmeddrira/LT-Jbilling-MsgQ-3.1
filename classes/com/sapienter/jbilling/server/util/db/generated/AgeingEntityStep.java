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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ageing_entity_step")
public class AgeingEntityStep  implements java.io.Serializable {


     private int id;
     private Company company;
     private UserStatus userStatus;
     private int days;

    public AgeingEntityStep() {
    }

	
    public AgeingEntityStep(int id, int days) {
        this.id = id;
        this.days = days;
    }
    public AgeingEntityStep(int id, Company entity, UserStatus userStatus, int days) {
       this.id = id;
       this.company = entity;
       this.userStatus = userStatus;
       this.days = days;
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
    @JoinColumn(name="entity_id")
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company entity) {
        this.company = entity;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="status_id")
    public UserStatus getUserStatus() {
        return this.userStatus;
    }
    
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
    
    @Column(name="days", nullable=false)
    public int getDays() {
        return this.days;
    }
    
    public void setDays(int days) {
        this.days = days;
    }




}

