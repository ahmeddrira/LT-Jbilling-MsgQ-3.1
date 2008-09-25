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

import com.sapienter.jbilling.server.user.db.CompanyDTO;

@Entity
@Table(name="contact_field_type")
public class ContactFieldType  implements java.io.Serializable {


     private int id;
     private CompanyDTO entity;
     private String promptKey;
     private String dataType;
     private Integer customerReadonly;
     private Set<ContactField> contactFields = new HashSet<ContactField>(0);

    public ContactFieldType() {
    }

	
    public ContactFieldType(int id, String promptKey, String dataType) {
        this.id = id;
        this.promptKey = promptKey;
        this.dataType = dataType;
    }
    public ContactFieldType(int id, CompanyDTO entity, String promptKey, String dataType, Integer customerReadonly, Set<ContactField> contactFields) {
       this.id = id;
       this.entity = entity;
       this.promptKey = promptKey;
       this.dataType = dataType;
       this.customerReadonly = customerReadonly;
       this.contactFields = contactFields;
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
    public CompanyDTO getEntity() {
        return this.entity;
    }
    
    public void setEntity(CompanyDTO entity) {
        this.entity = entity;
    }
    
    @Column(name="prompt_key", nullable=false, length=50)
    public String getPromptKey() {
        return this.promptKey;
    }
    
    public void setPromptKey(String promptKey) {
        this.promptKey = promptKey;
    }
    
    @Column(name="data_type", nullable=false, length=10)
    public String getDataType() {
        return this.dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    @Column(name="customer_readonly")
    public Integer getCustomerReadonly() {
        return this.customerReadonly;
    }
    
    public void setCustomerReadonly(Integer customerReadonly) {
        this.customerReadonly = customerReadonly;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="contactFieldType")
    public Set<ContactField> getContactFields() {
        return this.contactFields;
    }
    
    public void setContactFields(Set<ContactField> contactFields) {
        this.contactFields = contactFields;
    }




}


