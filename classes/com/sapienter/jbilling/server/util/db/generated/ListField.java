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
@Table(name="list_field")
public class ListField  implements java.io.Serializable {


     private int id;
     private List list;
     private String titleKey;
     private String columnName;
     private short ordenable;
     private short searchable;
     private String dataType;
     private Set<ListFieldEntity> listFieldEntities = new HashSet<ListFieldEntity>(0);

    public ListField() {
    }

	
    public ListField(int id, String titleKey, String columnName, short ordenable, short searchable, String dataType) {
        this.id = id;
        this.titleKey = titleKey;
        this.columnName = columnName;
        this.ordenable = ordenable;
        this.searchable = searchable;
        this.dataType = dataType;
    }
    public ListField(int id, List list, String titleKey, String columnName, short ordenable, short searchable, String dataType, Set<ListFieldEntity> listFieldEntities) {
       this.id = id;
       this.list = list;
       this.titleKey = titleKey;
       this.columnName = columnName;
       this.ordenable = ordenable;
       this.searchable = searchable;
       this.dataType = dataType;
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
    
    @Column(name="title_key", nullable=false, length=100)
    public String getTitleKey() {
        return this.titleKey;
    }
    
    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }
    
    @Column(name="column_name", nullable=false, length=50)
    public String getColumnName() {
        return this.columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    @Column(name="ordenable", nullable=false)
    public short getOrdenable() {
        return this.ordenable;
    }
    
    public void setOrdenable(short ordenable) {
        this.ordenable = ordenable;
    }
    
    @Column(name="searchable", nullable=false)
    public short getSearchable() {
        return this.searchable;
    }
    
    public void setSearchable(short searchable) {
        this.searchable = searchable;
    }
    
    @Column(name="data_type", nullable=false, length=10)
    public String getDataType() {
        return this.dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="listField")
    public Set<ListFieldEntity> getListFieldEntities() {
        return this.listFieldEntities;
    }
    
    public void setListFieldEntities(Set<ListFieldEntity> listFieldEntities) {
        this.listFieldEntities = listFieldEntities;
    }




}


