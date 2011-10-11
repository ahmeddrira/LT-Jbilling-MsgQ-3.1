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

import com.sapienter.jbilling.server.metafields.db.EntityType;
import com.sapienter.jbilling.server.metafields.db.MetaField;
import com.sapienter.jbilling.server.metafields.db.MetaFieldDAS;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Business Logic for meta-fields.
 *
 * @author Brian Cowdery
 * @since 03-Oct-2011
 */
public class MetaFieldBL {

    public static MetaField getFieldByName(EntityType entityType, String name) {
        return new MetaFieldDAS().getFieldByName(entityType, name);
    }

    /**
     * Returns a map of MetaField's for the given entity type keyed by the field
     * name's plain text name. Basically a list of name-value-pair names with the original
     * MetaField object to be used when building new fields.
     *
     * @param entityType entity type to query
     * @return map with available fields
     */
    public static Map<String, MetaField> getAvailableFields(EntityType entityType) {
        List<MetaField> entityFields = new MetaFieldDAS().getAvailableFields(entityType);
        Map<String, MetaField> result = new LinkedHashMap<String, MetaField>();
        for (MetaField field : entityFields) {
            result.put(field.getName(), field);
        }
        return result;
    }
}
