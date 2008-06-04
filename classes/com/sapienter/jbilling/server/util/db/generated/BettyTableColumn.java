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
@Table(name="betty_table_column")
public class BettyTableColumn  implements java.io.Serializable {


     private BettyTableColumnId id;
     private JbillingTable jbillingTable;
     private String name;

    public BettyTableColumn() {
    }

    public BettyTableColumn(BettyTableColumnId id, JbillingTable jbillingTable, String name) {
       this.id = id;
       this.jbillingTable = jbillingTable;
       this.name = name;
    }
   
     @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="tableId", column=@Column(name="table_id", nullable=false) ), 
        @AttributeOverride(name="id", column=@Column(name="id", nullable=false) ) } )
    public BettyTableColumnId getId() {
        return this.id;
    }
    
    public void setId(BettyTableColumnId id) {
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
    
    @Column(name="name", nullable=false, length=50)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }




}


