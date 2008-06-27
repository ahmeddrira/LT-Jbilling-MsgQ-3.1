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
package com.sapienter.jbilling.server.item;

import java.util.Calendar;
import java.util.Date;

import com.sapienter.jbilling.common.SessionInternalError;

public class PricingField {
    private final String name;
    private String strValue;
    private Date dateValue;
    private final Type type;
    private Double doubleValue;
    private Integer intValue;
    private Integer position = 1;
    
    public void setPosition(Integer position) {
        this.position = position;
    }
    
    public Integer getPosition() {
        return position;
    }
    
    public enum Type {STRING, INTEGER, FLOAT, DATE};
    
    public PricingField(PricingField field) {
        this.name = field.getName();
        this.strValue = field.getStrValue();
        this.dateValue = field.getDateValue();
        this.type = field.getType();
        this.doubleValue = field.getDoubleValue();
        this.intValue = field.getIntValue();
        this.position = field.getPosition();
    }
    
    public PricingField(String name, String strValue) {
        this.name = name;
        this.strValue = strValue;
        type = Type.STRING;
        intValue = null;
        dateValue = null;
        doubleValue = null;
    }

    public PricingField(String name, Date dateValue) {
        this.name = name;
        this.dateValue = dateValue;
        type = Type.DATE;
        intValue = null;
        strValue = null;
        doubleValue = null;
    }

    public PricingField(String name, Integer intValue) {
        this.name = name;
        this.intValue = intValue;
        type = Type.INTEGER;
        strValue = null;
        dateValue = null;
        doubleValue = null;
    }
    
    public PricingField(String name, Double doubleValue) {
        this.name = name;
        this.doubleValue = doubleValue;
        type = Type.FLOAT;
        strValue = null;
        intValue = null;
        dateValue = null;
    }

    public Date getDateValue() {
        return dateValue;
    }
    
    public Calendar getCalendarValue() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dateValue);
    	return cal;
    }

    // This method is only here for backward compatibility
    public Double getFloatValue() {
        return getDoubleValue();
    }

    public Integer getIntValue() {
        return intValue;
    }
    
    public Double getDoubleValue() {
        return doubleValue;
    }

    public String getName() {
        return name;
    }

    public String getStrValue() {
        return strValue;
    }

    public Type getType() {
        return type;
    }
    
    public Object getValue() {
        if (strValue != null) 
            return strValue;
        if (intValue != null)
            return intValue;
        if (dateValue != null)
            return dateValue;
        if (doubleValue != null)
            return doubleValue;
        
        throw new SessionInternalError("There is no value");
    }
    
    public static Type mapType(String myType) {
        if (myType.equalsIgnoreCase("string")) {
            return Type.STRING;
        } else if (myType.equalsIgnoreCase("integer")) {
            return Type.INTEGER;
        } else if (myType.equalsIgnoreCase("float") || myType.equalsIgnoreCase("double")) {
            return Type.FLOAT;
        } else if (myType.equalsIgnoreCase("date")) {
            return Type.DATE;
        } else {
            return null;
        }
    }
    
    public String toString() {
        return "name: " + name + " type: " + type + " value: " + getValue();
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }
    
    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }
}
