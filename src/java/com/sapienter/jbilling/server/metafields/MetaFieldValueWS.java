/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.metafields;

import com.sapienter.jbilling.server.metafields.db.DataType;
import com.sapienter.jbilling.server.metafields.db.MetaFieldValue;

import java.io.Serializable;

/**
 * @author Alexander Aksenov
 * @since 09.10.11
 */
public class MetaFieldValueWS implements Serializable {

    private String fieldName;
    private boolean disabled;
    private boolean mandatory;
    private DataType dataType;
    private Object defaultValue;

    private Integer id;
    private Object value;

    public MetaFieldValueWS() {
    }

    public MetaFieldValueWS(MetaFieldValue metaFieldValue) {
        this.id = metaFieldValue.getId();
        this.value = metaFieldValue.getValue();

        if (metaFieldValue.getField() != null) {
            this.fieldName = metaFieldValue.getField().getName();
            this.disabled = metaFieldValue.getField().isDisabled();
            this.mandatory = metaFieldValue.getField().isMandatory();
            this.dataType = metaFieldValue.getField().getDataType();
            this.defaultValue = metaFieldValue.getField().getDefaultValue() != null ? metaFieldValue.getField().getDefaultValue().getValue() : null;
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
