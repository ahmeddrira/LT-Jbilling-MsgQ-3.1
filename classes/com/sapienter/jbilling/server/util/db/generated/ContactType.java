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
@Table(name="contact_type")
public class ContactType  implements java.io.Serializable {


     private int id;
     private CompanyDTO entity;
     private Integer isPrimary;
     private Set<ContactMap> contactMaps = new HashSet<ContactMap>(0);

    public ContactType() {
    }

	
    public ContactType(int id) {
        this.id = id;
    }
    public ContactType(int id, CompanyDTO entity, Integer isPrimary, Set<ContactMap> contactMaps) {
       this.id = id;
       this.entity = entity;
       this.isPrimary = isPrimary;
       this.contactMaps = contactMaps;
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
    
    @Column(name="is_primary")
    public Integer getIsPrimary() {
        return this.isPrimary;
    }
    
    public void setIsPrimary(Integer isPrimary) {
        this.isPrimary = isPrimary;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="contactType")
    public Set<ContactMap> getContactMaps() {
        return this.contactMaps;
    }
    
    public void setContactMaps(Set<ContactMap> contactMaps) {
        this.contactMaps = contactMaps;
    }




}


