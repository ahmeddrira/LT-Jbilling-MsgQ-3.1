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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sapienter.jbilling.server.user.db.CompanyDTO;

@Entity
@Table(name="report")
public class Report  implements java.io.Serializable {


     private int id;
     private String titlekey;
     private String instructionskey;
     private String tablesList;
     private String whereStr;
     private int idColumn;
     private String link;
     private Set<CompanyDTO> entities = new HashSet<CompanyDTO>(0);
     private Set<ReportField> reportFields = new HashSet<ReportField>(0);
     private Set<ReportType> reportTypes = new HashSet<ReportType>(0);
     private Set<ReportUser> reportUsers = new HashSet<ReportUser>(0);

    public Report() {
    }

	
    public Report(int id, String tablesList, String whereStr, int idColumn) {
        this.id = id;
        this.tablesList = tablesList;
        this.whereStr = whereStr;
        this.idColumn = idColumn;
    }
    public Report(int id, String titlekey, String instructionskey, String tablesList, String whereStr, int idColumn, String link, Set<CompanyDTO> entities, Set<ReportField> reportFields, Set<ReportType> reportTypes, Set<ReportUser> reportUsers) {
       this.id = id;
       this.titlekey = titlekey;
       this.instructionskey = instructionskey;
       this.tablesList = tablesList;
       this.whereStr = whereStr;
       this.idColumn = idColumn;
       this.link = link;
       this.entities = entities;
       this.reportFields = reportFields;
       this.reportTypes = reportTypes;
       this.reportUsers = reportUsers;
    }
   
     @Id 
    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="titlekey", length=50)
    public String getTitlekey() {
        return this.titlekey;
    }
    
    public void setTitlekey(String titlekey) {
        this.titlekey = titlekey;
    }
    
    @Column(name="instructionskey", length=50)
    public String getInstructionskey() {
        return this.instructionskey;
    }
    
    public void setInstructionskey(String instructionskey) {
        this.instructionskey = instructionskey;
    }
    
    @Column(name="tables_list", nullable=false, length=1000)
    public String getTablesList() {
        return this.tablesList;
    }
    
    public void setTablesList(String tablesList) {
        this.tablesList = tablesList;
    }
    
    @Column(name="where_str", nullable=false, length=1000)
    public String getWhereStr() {
        return this.whereStr;
    }
    
    public void setWhereStr(String whereStr) {
        this.whereStr = whereStr;
    }
    
    @Column(name="id_column", nullable=false)
    public int getIdColumn() {
        return this.idColumn;
    }
    
    public void setIdColumn(int idColumn) {
        this.idColumn = idColumn;
    }
    
    @Column(name="link", length=200)
    public String getLink() {
        return this.link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="report_entity_map", joinColumns = { 
        @JoinColumn(name="report_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="entity_id", updatable=false) })
    public Set<CompanyDTO> getEntities() {
        return this.entities;
    }
    
    public void setEntities(Set<CompanyDTO> entities) {
        this.entities = entities;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="report")
    public Set<ReportField> getReportFields() {
        return this.reportFields;
    }
    
    public void setReportFields(Set<ReportField> reportFields) {
        this.reportFields = reportFields;
    }
@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="report_type_map", joinColumns = { 
        @JoinColumn(name="report_id", updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="type_id", updatable=false) })
    public Set<ReportType> getReportTypes() {
        return this.reportTypes;
    }
    
    public void setReportTypes(Set<ReportType> reportTypes) {
        this.reportTypes = reportTypes;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="report")
    public Set<ReportUser> getReportUsers() {
        return this.reportUsers;
    }
    
    public void setReportUsers(Set<ReportUser> reportUsers) {
        this.reportUsers = reportUsers;
    }




}


