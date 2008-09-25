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
import javax.persistence.UniqueConstraint;

import com.sapienter.jbilling.server.user.db.CompanyDTO;

@Entity
@Table(name="list_entity" , uniqueConstraints = @UniqueConstraint(columnNames={"list_id", "entity_id"}) 
)
public class ListEntity  implements java.io.Serializable {


     private int id;
     private List list;
     private CompanyDTO entity;
     private int totalRecords;
     private Date lastUpdate;
     private Set<ListFieldEntity> listFieldEntities = new HashSet<ListFieldEntity>(0);

    public ListEntity() {
    }

	
    public ListEntity(int id, CompanyDTO entity, int totalRecords, Date lastUpdate) {
        this.id = id;
        this.entity = entity;
        this.totalRecords = totalRecords;
        this.lastUpdate = lastUpdate;
    }
    public ListEntity(int id, List list, CompanyDTO entity, int totalRecords, Date lastUpdate, Set<ListFieldEntity> listFieldEntities) {
       this.id = id;
       this.list = list;
       this.entity = entity;
       this.totalRecords = totalRecords;
       this.lastUpdate = lastUpdate;
       this.listFieldEntities = listFieldEntities;
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
    @JoinColumn(name="list_id")
    public List getList() {
        return this.list;
    }
    
    public void setList(List list) {
        this.list = list;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="entity_id", nullable=false)
    public CompanyDTO getEntity() {
        return this.entity;
    }
    
    public void setEntity(CompanyDTO entity) {
        this.entity = entity;
    }
    
    @Column(name="total_records", nullable=false)
    public int getTotalRecords() {
        return this.totalRecords;
    }
    
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="last_update", nullable=false, length=13)
    public Date getLastUpdate() {
        return this.lastUpdate;
    }
    
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="listEntity")
    public Set<ListFieldEntity> getListFieldEntities() {
        return this.listFieldEntities;
    }
    
    public void setListFieldEntities(Set<ListFieldEntity> listFieldEntities) {
        this.listFieldEntities = listFieldEntities;
    }




}


