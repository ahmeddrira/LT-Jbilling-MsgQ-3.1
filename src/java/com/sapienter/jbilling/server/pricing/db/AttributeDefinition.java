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

package com.sapienter.jbilling.server.pricing.db;

/**
 * Definition of attributes expected by a pricing strategy.
 *
 * @see com.sapienter.jbilling.server.pricing.util.AttributeUtils
 *
 * @author Brian Cowdery
 * @since 31/01/11
 */
public class AttributeDefinition {

    public enum Type { STRING, INTEGER, DECIMAL }

    private String name;
    private Type type = Type.STRING;
    private boolean required = false;

    public AttributeDefinition() {
    }

    public AttributeDefinition(String name) {
        this.name = name;
    }

    public AttributeDefinition(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public AttributeDefinition(String name, Type type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "Attribute{" + type.name() + ": " + name + (required ? "(required)" : "") + "}";
    }
}
