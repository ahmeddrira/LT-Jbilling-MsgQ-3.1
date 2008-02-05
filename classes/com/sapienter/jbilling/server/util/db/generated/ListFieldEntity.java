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

@Entity
@Table(name="list_field_entity")
public class ListFieldEntity  implements java.io.Serializable {


     private int id;
     private ListEntity listEntity;
     private ListField listField;
     private Integer minValue;
     private Integer maxValue;
     private String minStrValue;
     private String maxStrValue;
     private Date minDateValue;
     private Date maxDateValue;

    public ListFieldEntity() {
    }

	
    public ListFieldEntity(int id) {
        this.id = id;
    }
    public ListFieldEntity(int id, ListEntity listEntity, ListField listField, Integer minValue, Integer maxValue, String minStrValue, String maxStrValue, Date minDateValue, Date maxDateValue) {
       this.id = id;
       this.listEntity = listEntity;
       this.listField = listField;
       this.minValue = minValue;
       this.maxValue = maxValue;
       this.minStrValue = minStrValue;
       this.maxStrValue = maxStrValue;
       this.minDateValue = minDateValue;
       this.maxDateValue = maxDateValue;
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
    @JoinColumn(name="list_entity_id")
    public ListEntity getListEntity() {
        return this.listEntity;
    }
    
    public void setListEntity(ListEntity listEntity) {
        this.listEntity = listEntity;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="list_field_id")
    public ListField getListField() {
        return this.listField;
    }
    
    public void setListField(ListField listField) {
        this.listField = listField;
    }
    
    @Column(name="min_value")
    public Integer getMinValue() {
        return this.minValue;
    }
    
    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }
    
    @Column(name="max_value")
    public Integer getMaxValue() {
        return this.maxValue;
    }
    
    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }
    
    @Column(name="min_str_value", length=100)
    public String getMinStrValue() {
        return this.minStrValue;
    }
    
    public void setMinStrValue(String minStrValue) {
        this.minStrValue = minStrValue;
    }
    
    @Column(name="max_str_value", length=100)
    public String getMaxStrValue() {
        return this.maxStrValue;
    }
    
    public void setMaxStrValue(String maxStrValue) {
        this.maxStrValue = maxStrValue;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="min_date_value", length=29)
    public Date getMinDateValue() {
        return this.minDateValue;
    }
    
    public void setMinDateValue(Date minDateValue) {
        this.minDateValue = minDateValue;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="max_date_value", length=29)
    public Date getMaxDateValue() {
        return this.maxDateValue;
    }
    
    public void setMaxDateValue(Date maxDateValue) {
        this.maxDateValue = maxDateValue;
    }




}


