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

package com.sapienter.jbilling.server.metafields.db;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.metafields.MetaFieldBL;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Common class for extending by entities that can contain meta-fields. This class enforces a set
 * of convenience methods for accessing the meta data.
 *
 * @author Alexander Aksenov
 * @since 08.10.11
 */

@MappedSuperclass
public abstract class CustomizedEntity implements java.io.Serializable {

    private List<MetaFieldValue> metaFields = new LinkedList<MetaFieldValue>();

    @Transient
    protected List<MetaFieldValue> getMetaFieldsList() {
        return metaFields;
    }

    public void setMetaFields(List<MetaFieldValue> fields) {
        this.metaFields = fields;
    }

    /**
     * Returns the meta field by name if it's been defined for this object.
     *
     * @param name meta field name
     * @return field if found, null if not set.
     */
    @Transient
    public MetaFieldValue getMetaField(String name) {
        for (MetaFieldValue value : metaFields) {
            if (value.getField() != null && value.getField().getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Adds a meta field to this object. If there is already a field associated with
     * this object then the existing value should be updated.
     *
     * @param field field to update.
     */
    @Transient
    public void setMetaField(MetaFieldValue field) {
        MetaFieldValue oldValue = getMetaField(field.getField().getName());
        if (oldValue != null) {
            metaFields.remove(oldValue);
        }
        metaFields.add(field);
    }

    @Transient
    public void updateMetaFields(CustomizedEntity dto) {
        Map<String, MetaField> availableMetaFields = getAvailableMetaFields();
        for (String fieldName : availableMetaFields.keySet()) {
            MetaFieldValue newValue = dto.getMetaField(fieldName);
            setMetaField(fieldName, newValue != null ? newValue.getValue() : null);
        }
        validateCurrentMetaFields();
    }

    /**
     * Sets the value of a meta field that is already associated with this object. If
     * the field does not already exist, or if the value class is of an incorrect type
     * then an IllegalArgumentException will be thrown.
     *
     * @param name  field name
     * @param value field value
     * @throws IllegalArgumentException thrown if field name does not exist, or if value is of an incorrect type.
     */
    @Transient
    public void setMetaField(String name, Object value) throws IllegalArgumentException {
        MetaFieldValue fieldValue = getMetaField(name);
        if (fieldValue != null) {
            try {
                fieldValue.setValue(value);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Incorrect type for meta field with name " + name, ex);
            }
        } else {
            EntityType type = getCustomizedEntityType();
            if (type == null) {
                throw new IllegalArgumentException("Meta Fields could not be specified for current entity");
            }
            MetaField fieldName = new MetaFieldDAS().getFieldByName(type, name);
            if (fieldName == null) {
                throw new IllegalArgumentException("Meta Field with name " + name + " was not defined for current entity");
            }
            MetaFieldValue field = fieldName.createValue();
            try {
                field.setValue(value);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Incorrect type for meta field with name " + name, ex);
            }
            setMetaField(field);
        }
    }

    /**
     * Validates all metafilds for current entity (configured) for filling mandatory and validity of specified values
     */
    @Transient
    public void validateMetaFields() {
        Map<String, MetaField> availableMetaFields = getAvailableMetaFields();
        for (String fieldName : availableMetaFields.keySet()) {
            MetaField field = availableMetaFields.get(fieldName);
            if (field.isMandatory()) {
                MetaFieldValue value = getMetaField(fieldName);
                if (value == null) {
                    throw new SessionInternalError("Validation failed.", new String[]{"MetaFieldValue,value,value.cannot.be.null"});
                }
                value.validate();
            }
        }
    }

    /**
     * Validates current (filled) meta fields for current object, checks that all MetaFieldValues are valid
     */
    @Transient
    public void validateCurrentMetaFields() {
        if (metaFields == null) return;
        for (MetaFieldValue value : metaFields) {
            value.validate();
        }
    }

    public final static class MetaFieldValuesOrderComparator implements Comparator<MetaFieldValue> {
        public int compare(MetaFieldValue o1, MetaFieldValue o2) {
            if (o1.getField().getDisplayOrder() == null && o2.getField().getDisplayOrder() == null) {
                return 0;
            }
            if (o1.getField().getDisplayOrder() != null) {
                return o1.getField().getDisplayOrder().compareTo(o2.getField().getDisplayOrder());
            } else {
                return -1 * o2.getField().getDisplayOrder().compareTo(o1.getField().getDisplayOrder());
            }
        }
    }

    @Transient
    protected abstract EntityType getCustomizedEntityType();

    @Transient
    public Map<String, MetaField> getAvailableMetaFields() {
        EntityType type = getCustomizedEntityType();
        return MetaFieldBL.getAvailableFields(type);
    }

}
