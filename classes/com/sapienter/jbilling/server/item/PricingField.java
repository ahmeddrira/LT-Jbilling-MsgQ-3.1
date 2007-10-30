/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/
package com.sapienter.jbilling.server.item;

import java.util.Date;

import com.sapienter.jbilling.common.SessionInternalError;

public class PricingField {
    private final String name;
    private final String strValue;
    private final Date dateValue;
    private final Type type;
    private final Float floatValue;
    private final Integer intValue;
    
    private enum Type {STRING, INTEGER, FLOAT, DATE};
    
    public PricingField(String name, String strValue) {
        this.name = name;
        this.strValue = strValue;
        type = Type.STRING;
        floatValue = null;
        intValue = null;
        dateValue = null;
    }

    public PricingField(String name, Date dateValue) {
        this.name = name;
        this.dateValue = dateValue;
        type = Type.DATE;
        floatValue = null;
        intValue = null;
        strValue = null;
    }

    public PricingField(String name, Float floatValue) {
        this.name = name;
        this.floatValue = floatValue;
        type = Type.FLOAT;
        strValue = null;
        intValue = null;
        dateValue = null;
    }

    public PricingField(String name, Integer intValue) {
        this.name = name;
        this.intValue = intValue;
        type = Type.INTEGER;
        floatValue = null;
        strValue = null;
        dateValue = null;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public Integer getIntValue() {
        return intValue;
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
        if (floatValue != null)
            return floatValue;
        if (dateValue != null)
            return dateValue;
        
        throw new SessionInternalError("There is no value");
    }
}
