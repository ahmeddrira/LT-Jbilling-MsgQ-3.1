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

import com.sapienter.jbilling.server.metafields.db.value.BooleanMetaFieldValue;
import com.sapienter.jbilling.server.metafields.db.value.DateMetaFieldValue;
import com.sapienter.jbilling.server.metafields.db.value.DecimalMetaFieldValue;
import com.sapienter.jbilling.server.metafields.db.value.IntegerMetaFieldValue;
import com.sapienter.jbilling.server.metafields.db.value.JsonMetaFieldValue;
import com.sapienter.jbilling.server.metafields.db.value.StringMetaFieldValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serializable;

/**
 * A meta-field name that is associated with a particular entity type. The field names define
 * the allowed values and data-types of the values that can be attached to an entity.
 *
 * @author Brian Cowdery
 * @since 03-Oct-2011
 */
@Entity
@Table(name = "meta_field_name")
@TableGenerator(
    name = "meta_field_GEN",
    table = "jbilling_seqs",
    pkColumnName = "name",
    valueColumnName = "next_id",
    pkColumnValue = "meta_field",
    allocationSize = 10
)
public class MetaField implements Serializable {

    private Integer id;
    private String name;
    private EntityType entityType = EntityType.USER;
    private DataType dataType = DataType.INTEGER;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "meta_field_GEN")
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 25)
    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 25)
    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public MetaFieldValue createValue() {
        switch (getDataType()) {
            case STRING:
                return new StringMetaFieldValue(this);

            case INTEGER:
                return new IntegerMetaFieldValue(this);

            case DECIMAL:
                return new DecimalMetaFieldValue(this);

            case BOOLEAN:
                return new BooleanMetaFieldValue(this);

            case DATE:
                return new DateMetaFieldValue(this);

            case JSON_OBJECT:
                return new JsonMetaFieldValue(this);

            case ENUMERATION:
                // todo: should return a new field type that's appropriate for the enumeration.
                break;
        }

        return null;
    }

    @Override
    public String toString() {
        return "MetaField{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", entityType=" + entityType +
               ", dataType=" + dataType +
               '}';
    }
}
