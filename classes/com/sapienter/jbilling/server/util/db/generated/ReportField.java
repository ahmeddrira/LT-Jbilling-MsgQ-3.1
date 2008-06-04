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
@Table(name="report_field")
public class ReportField  implements java.io.Serializable {


     private int id;
     private Report report;
     private Integer reportUserId;
     private int positionNumber;
     private String tableName;
     private String columnName;
     private Integer orderPosition;
     private String whereValue;
     private String titleKey;
     private String functionName;
     private short isGrouped;
     private short isShown;
     private String dataType;
     private String operatorValue;
     private short functionable;
     private short selectable;
     private short ordenable;
     private short operatorable;
     private short whereable;

    public ReportField() {
    }

	
    public ReportField(int id, int positionNumber, String tableName, String columnName, short isGrouped, short isShown, String dataType, short functionable, short selectable, short ordenable, short operatorable, short whereable) {
        this.id = id;
        this.positionNumber = positionNumber;
        this.tableName = tableName;
        this.columnName = columnName;
        this.isGrouped = isGrouped;
        this.isShown = isShown;
        this.dataType = dataType;
        this.functionable = functionable;
        this.selectable = selectable;
        this.ordenable = ordenable;
        this.operatorable = operatorable;
        this.whereable = whereable;
    }
    public ReportField(int id, Report report, Integer reportUserId, int positionNumber, String tableName, String columnName, Integer orderPosition, String whereValue, String titleKey, String functionName, short isGrouped, short isShown, String dataType, String operatorValue, short functionable, short selectable, short ordenable, short operatorable, short whereable) {
       this.id = id;
       this.report = report;
       this.reportUserId = reportUserId;
       this.positionNumber = positionNumber;
       this.tableName = tableName;
       this.columnName = columnName;
       this.orderPosition = orderPosition;
       this.whereValue = whereValue;
       this.titleKey = titleKey;
       this.functionName = functionName;
       this.isGrouped = isGrouped;
       this.isShown = isShown;
       this.dataType = dataType;
       this.operatorValue = operatorValue;
       this.functionable = functionable;
       this.selectable = selectable;
       this.ordenable = ordenable;
       this.operatorable = operatorable;
       this.whereable = whereable;
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
    @JoinColumn(name="report_id")
    public Report getReport() {
        return this.report;
    }
    
    public void setReport(Report report) {
        this.report = report;
    }
    
    @Column(name="report_user_id")
    public Integer getReportUserId() {
        return this.reportUserId;
    }
    
    public void setReportUserId(Integer reportUserId) {
        this.reportUserId = reportUserId;
    }
    
    @Column(name="position_number", nullable=false)
    public int getPositionNumber() {
        return this.positionNumber;
    }
    
    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
    }
    
    @Column(name="table_name", nullable=false, length=50)
    public String getTableName() {
        return this.tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    @Column(name="column_name", nullable=false, length=50)
    public String getColumnName() {
        return this.columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    @Column(name="order_position")
    public Integer getOrderPosition() {
        return this.orderPosition;
    }
    
    public void setOrderPosition(Integer orderPosition) {
        this.orderPosition = orderPosition;
    }
    
    @Column(name="where_value", length=50)
    public String getWhereValue() {
        return this.whereValue;
    }
    
    public void setWhereValue(String whereValue) {
        this.whereValue = whereValue;
    }
    
    @Column(name="title_key", length=50)
    public String getTitleKey() {
        return this.titleKey;
    }
    
    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }
    
    @Column(name="function_name", length=10)
    public String getFunctionName() {
        return this.functionName;
    }
    
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
    
    @Column(name="is_grouped", nullable=false)
    public short getIsGrouped() {
        return this.isGrouped;
    }
    
    public void setIsGrouped(short isGrouped) {
        this.isGrouped = isGrouped;
    }
    
    @Column(name="is_shown", nullable=false)
    public short getIsShown() {
        return this.isShown;
    }
    
    public void setIsShown(short isShown) {
        this.isShown = isShown;
    }
    
    @Column(name="data_type", nullable=false, length=10)
    public String getDataType() {
        return this.dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    @Column(name="operator_value", length=2)
    public String getOperatorValue() {
        return this.operatorValue;
    }
    
    public void setOperatorValue(String operatorValue) {
        this.operatorValue = operatorValue;
    }
    
    @Column(name="functionable", nullable=false)
    public short getFunctionable() {
        return this.functionable;
    }
    
    public void setFunctionable(short functionable) {
        this.functionable = functionable;
    }
    
    @Column(name="selectable", nullable=false)
    public short getSelectable() {
        return this.selectable;
    }
    
    public void setSelectable(short selectable) {
        this.selectable = selectable;
    }
    
    @Column(name="ordenable", nullable=false)
    public short getOrdenable() {
        return this.ordenable;
    }
    
    public void setOrdenable(short ordenable) {
        this.ordenable = ordenable;
    }
    
    @Column(name="operatorable", nullable=false)
    public short getOperatorable() {
        return this.operatorable;
    }
    
    public void setOperatorable(short operatorable) {
        this.operatorable = operatorable;
    }
    
    @Column(name="whereable", nullable=false)
    public short getWhereable() {
        return this.whereable;
    }
    
    public void setWhereable(short whereable) {
        this.whereable = whereable;
    }




}


