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


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="international_description")
public class InternationalDescription  implements java.io.Serializable {


     private InternationalDescriptionId id;
     private JbillingTable jbillingTable;
     private Language language;
     private String content;

    public InternationalDescription() {
    }

    public InternationalDescription(InternationalDescriptionId id, JbillingTable jbillingTable, Language language, String content) {
       this.id = id;
       this.jbillingTable = jbillingTable;
       this.language = language;
       this.content = content;
    }
   
     @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="tableId", column=@Column(name="table_id", nullable=false) ), 
        @AttributeOverride(name="foreignId", column=@Column(name="foreign_id", nullable=false) ), 
        @AttributeOverride(name="psudoColumn", column=@Column(name="psudo_column", nullable=false, length=20) ), 
        @AttributeOverride(name="languageId", column=@Column(name="language_id", nullable=false) ) } )
    public InternationalDescriptionId getId() {
        return this.id;
    }
    
    public void setId(InternationalDescriptionId id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="table_id", nullable=false, insertable=false, updatable=false)
    public JbillingTable getJbillingTable() {
        return this.jbillingTable;
    }
    
    public void setJbillingTable(JbillingTable jbillingTable) {
        this.jbillingTable = jbillingTable;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="language_id", nullable=false, insertable=false, updatable=false)
    public Language getLanguage() {
        return this.language;
    }
    
    public void setLanguage(Language language) {
        this.language = language;
    }
    
    @Column(name="content", nullable=false, length=5000)
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }




}


