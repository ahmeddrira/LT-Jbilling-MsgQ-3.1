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
import javax.persistence.Table;

@Entity
@Table(name="report_type")
public class ReportType  implements java.io.Serializable {


     private int id;
     private short showable;
     private Set<Report> reports = new HashSet<Report>(0);

    public ReportType() {
    }

	
    public ReportType(int id, short showable) {
        this.id = id;
        this.showable = showable;
    }
    public ReportType(int id, short showable, Set<Report> reports) {
       this.id = id;
       this.showable = showable;
       this.reports = reports;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="showable", nullable=false)
    public short getShowable() {
        return this.showable;
    }
    
    public void setShowable(short showable) {
        this.showable = showable;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="report_type_map", joinColumns = { 
        @JoinColumn(name="type_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="report_id", updatable=false) })
    public Set<Report> getReports() {
        return this.reports;
    }
    
    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }




}


