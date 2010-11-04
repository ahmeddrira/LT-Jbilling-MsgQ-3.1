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
import java.util.HashSet;
import java.util.Set;

import com.sapienter.jbilling.server.util.db.AbstractDescription;

public class PreferenceTypeWS extends AbstractDescription implements
        java.io.Serializable {

    private int id;
    private Integer intDefValue;
    private String strDefValue;
    private BigDecimal floatDefValue;
    private Set<PreferenceWS> preferences = new HashSet<PreferenceWS>(0);

    public PreferenceTypeWS() {
    }

    public PreferenceTypeWS(int id) {
        this.id = id;
    }

//    public PreferenceTypeWS(int id, Integer intDefValue, String strDefValue,
//            BigDecimal floatDefValue, Set<PreferenceWS> preferences) {
//        this.id = id;
//        this.intDefValue = intDefValue;
//        this.strDefValue = strDefValue;
//        this.floatDefValue = floatDefValue;
//        this.preferences = preferences;
//    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIntDefValue() {
        return this.intDefValue;
    }

    public void setIntDefValue(Integer intDefValue) {
        this.intDefValue = intDefValue;
    }

    public String getStrDefValue() {
        return this.strDefValue;
    }

    public void setStrDefValue(String strDefValue) {
        this.strDefValue = strDefValue;
    }

    public BigDecimal getFloatDefValue() {
        return this.floatDefValue;
    }

    public void setFloatDefValue(BigDecimal floatDefValue) {
        this.floatDefValue = floatDefValue;
    }

    public Set<PreferenceWS> getPreferences() {
        return this.preferences;
    }

    public void setPreferences(Set<PreferenceWS> preferences) {
        this.preferences = preferences;
    }

    protected String getTable() {
        return Constants.TABLE_PREFERENCE_TYPE;
    }
}
