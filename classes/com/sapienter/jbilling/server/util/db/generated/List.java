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
@Table(name="list")
public class List  implements java.io.Serializable {


     private int id;
     private String legacyName;
     private String titleKey;
     private String instrKey;
     private Set<ListField> listFields = new HashSet<ListField>(0);
     private Set<ListEntity> listEntities = new HashSet<ListEntity>(0);

    public List() {
    }

	
    public List(int id, String titleKey) {
        this.id = id;
        this.titleKey = titleKey;
    }
    public List(int id, String legacyName, String titleKey, String instrKey, Set<ListField> listFields, Set<ListEntity> listEntities) {
       this.id = id;
       this.legacyName = legacyName;
       this.titleKey = titleKey;
       this.instrKey = instrKey;
       this.listFields = listFields;
       this.listEntities = listEntities;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="legacy_name", length=30)
    public String getLegacyName() {
        return this.legacyName;
    }
    
    public void setLegacyName(String legacyName) {
        this.legacyName = legacyName;
    }
    
    @Column(name="title_key", nullable=false, length=100)
    public String getTitleKey() {
        return this.titleKey;
    }
    
    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }
    
    @Column(name="instr_key", length=100)
    public String getInstrKey() {
        return this.instrKey;
    }
    
    public void setInstrKey(String instrKey) {
        this.instrKey = instrKey;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="list")
    public Set<ListField> getListFields() {
        return this.listFields;
    }
    
    public void setListFields(Set<ListField> listFields) {
        this.listFields = listFields;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="list")
    public Set<ListEntity> getListEntities() {
        return this.listEntities;
    }
    
    public void setListEntities(Set<ListEntity> listEntities) {
        this.listEntities = listEntities;
    }




}


