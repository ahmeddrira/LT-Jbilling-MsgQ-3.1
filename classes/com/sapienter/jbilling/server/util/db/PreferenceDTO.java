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
package com.sapienter.jbilling.server.util.db;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.sapienter.jbilling.server.util.db.generated.JbillingTable;

@Entity
@Table(name="preference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PreferenceDTO  implements java.io.Serializable {


     private int id;
     private JbillingTable jbillingTable;
     private PreferenceTypeDTO preferenceType;
     private int foreignId;
     private Integer intValue;
     private String strValue;
     private Double floatValue;

    public PreferenceDTO() {
    }

	
    public PreferenceDTO(int id, JbillingTable jbillingTable, int foreignId) {
        this.id = id;
        this.jbillingTable = jbillingTable;
        this.foreignId = foreignId;
    }
    public PreferenceDTO(int id, JbillingTable jbillingTable, PreferenceTypeDTO preferenceType, int foreignId, Integer intValue, String strValue, Double floatValue) {
       this.id = id;
       this.jbillingTable = jbillingTable;
       this.preferenceType = preferenceType;
       this.foreignId = foreignId;
       this.intValue = intValue;
       this.strValue = strValue;
       this.floatValue = floatValue;
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
    @JoinColumn(name="table_id", nullable=false)
    public JbillingTable getJbillingTable() {
        return this.jbillingTable;
    }
    
    public void setJbillingTable(JbillingTable jbillingTable) {
        this.jbillingTable = jbillingTable;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="type_id")
    public PreferenceTypeDTO getPreferenceType() {
        return this.preferenceType;
    }
    
    public void setPreferenceType(PreferenceTypeDTO preferenceType) {
        this.preferenceType = preferenceType;
    }
    
    @Column(name="foreign_id", nullable=false)
    public int getForeignId() {
        return this.foreignId;
    }
    
    public void setForeignId(int foreignId) {
        this.foreignId = foreignId;
    }
    
    @Column(name="int_value")
    public Integer getIntValue() {
        return this.intValue;
    }
    
    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }
    
    @Column(name="str_value", length=200)
    public String getStrValue() {
        return this.strValue;
    }
    
    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }
    
    @Column(name="float_value", precision=17, scale=17)
    public Double getFloatValue() {
        return this.floatValue;
    }
    
    public void setFloatValue(Double floatValue) {
        this.floatValue = floatValue;
    }




}

