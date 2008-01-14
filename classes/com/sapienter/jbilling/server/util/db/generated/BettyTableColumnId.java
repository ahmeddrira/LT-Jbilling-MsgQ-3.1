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
import javax.persistence.Embeddable;

@Embeddable
public class BettyTableColumnId  implements java.io.Serializable {


     private int tableId;
     private int id;

    public BettyTableColumnId() {
    }

    public BettyTableColumnId(int tableId, int id) {
       this.tableId = tableId;
       this.id = id;
    }
   

    @Column(name="table_id", nullable=false)
    public int getTableId() {
        return this.tableId;
    }
    
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    @Column(name="id", nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof BettyTableColumnId) ) return false;
		 BettyTableColumnId castOther = ( BettyTableColumnId ) other; 
         
		 return (this.getTableId()==castOther.getTableId())
 && (this.getId()==castOther.getId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getTableId();
         result = 37 * result + this.getId();
         return result;
   }   


}


