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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sapienter.jbilling.server.item.db.Item;

@Entity
@Table(name="item_type")
public class ItemType  implements java.io.Serializable {


     private int id;
     private Company entity;
     private String description;
     private int orderLineTypeId;
     private Set<Item> items = new HashSet<Item>(0);

    public ItemType() {
    }

	
    public ItemType(int id, Company entity, int orderLineTypeId) {
        this.id = id;
        this.entity = entity;
        this.orderLineTypeId = orderLineTypeId;
    }
    public ItemType(int id, Company entity, String description, int orderLineTypeId, Set<Item> items) {
       this.id = id;
       this.entity = entity;
       this.description = description;
       this.orderLineTypeId = orderLineTypeId;
       this.items = items;
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
    @JoinColumn(name="entity_id", nullable=false)
    public Company getEntity() {
        return this.entity;
    }
    
    public void setEntity(Company entity) {
        this.entity = entity;
    }
    
    @Column(name="description", length=100)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="order_line_type_id", nullable=false)
    public int getOrderLineTypeId() {
        return this.orderLineTypeId;
    }
    
    public void setOrderLineTypeId(int orderLineTypeId) {
        this.orderLineTypeId = orderLineTypeId;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="item_type_map", joinColumns = { 
        @JoinColumn(name="type_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="item_id", updatable=false) })
    public Set<Item> getItems() {
        return this.items;
    }
    
    public void setItems(Set<Item> items) {
        this.items = items;
    }




}


