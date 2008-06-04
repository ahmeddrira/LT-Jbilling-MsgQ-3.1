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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="contact_field")
public class ContactField  implements java.io.Serializable {


     private int id;
     private ContactFieldType contactFieldType;
     private Contact contact;
     private String content;

    public ContactField() {
    }

	
    public ContactField(int id, String content) {
        this.id = id;
        this.content = content;
    }
    public ContactField(int id, ContactFieldType contactFieldType, Contact contact, String content) {
       this.id = id;
       this.contactFieldType = contactFieldType;
       this.contact = contact;
       this.content = content;
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
    @JoinColumn(name="type_id")
    public ContactFieldType getContactFieldType() {
        return this.contactFieldType;
    }
    
    public void setContactFieldType(ContactFieldType contactFieldType) {
        this.contactFieldType = contactFieldType;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="contact_id")
    public Contact getContact() {
        return this.contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
    @Column(name="content", nullable=false, length=100)
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }




}


