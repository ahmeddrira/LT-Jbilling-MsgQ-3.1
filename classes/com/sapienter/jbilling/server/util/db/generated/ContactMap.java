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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="contact_map"
    , uniqueConstraints = @UniqueConstraint(columnNames="contact_id") 
)
public class ContactMap  implements java.io.Serializable {


     private int id;
     private JbillingTable jbillingTable;
     private ContactType contactType;
     private Contact contact;
     private int foreignId;

    public ContactMap() {
    }

	
    public ContactMap(int id, JbillingTable jbillingTable, ContactType contactType, int foreignId) {
        this.id = id;
        this.jbillingTable = jbillingTable;
        this.contactType = contactType;
        this.foreignId = foreignId;
    }
    public ContactMap(int id, JbillingTable jbillingTable, ContactType contactType, Contact contact, int foreignId) {
       this.id = id;
       this.jbillingTable = jbillingTable;
       this.contactType = contactType;
       this.contact = contact;
       this.foreignId = foreignId;
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
    @JoinColumn(name="type_id", nullable=false)
    public ContactType getContactType() {
        return this.contactType;
    }
    
    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="contact_id", unique=true)
    public Contact getContact() {
        return this.contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
    @Column(name="foreign_id", nullable=false)
    public int getForeignId() {
        return this.foreignId;
    }
    
    public void setForeignId(int foreignId) {
        this.foreignId = foreignId;
    }




}


