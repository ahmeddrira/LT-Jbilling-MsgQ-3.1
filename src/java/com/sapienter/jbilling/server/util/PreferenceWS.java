/*

    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sapienter.jbilling.server.util;

import java.math.BigDecimal;

import com.sapienter.jbilling.server.util.db.JbillingTable;

public class PreferenceWS implements java.io.Serializable {

    private int id;
    private JbillingTable jbillingTable;
    private PreferenceTypeWS preferenceType;
    private int foreignId;
    private Integer intValue;
    private String strValue;
    private BigDecimal floatValue;

    public PreferenceWS() {
    }

    public PreferenceWS(int id, JbillingTable jbillingTable, int foreignId) {
        this.id = id;
        this.jbillingTable = jbillingTable;
        this.foreignId = foreignId;
    }

    public PreferenceWS(int id, JbillingTable jbillingTable,
            PreferenceTypeWS preferenceType, int foreignId, Integer intValue,
            String strValue, BigDecimal floatValue) {
        this.id = id;
        this.jbillingTable = jbillingTable;
        this.preferenceType = preferenceType;
        this.foreignId = foreignId;
        this.intValue = intValue;
        this.strValue = strValue;
        this.floatValue = floatValue;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JbillingTable getJbillingTable() {
        return this.jbillingTable;
    }

    public void setJbillingTable(JbillingTable jbillingTable) {
        this.jbillingTable = jbillingTable;
    }

    public PreferenceTypeWS getPreferenceType() {
        return this.preferenceType;
    }

    public void setPreferenceType(PreferenceTypeWS preferenceType) {
        this.preferenceType = preferenceType;
    }

    public int getForeignId() {
        return this.foreignId;
    }

    public void setForeignId(int foreignId) {
        this.foreignId = foreignId;
    }

    public Integer getIntValue() {
        return this.intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getStrValue() {
        return this.strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public BigDecimal getFloatValue() {
        return this.floatValue;
    }

    public void setFloatValue(BigDecimal floatValue) {
        this.floatValue = floatValue;
    }
}
