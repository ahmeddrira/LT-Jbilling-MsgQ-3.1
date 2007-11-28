/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
package com.sapienter.jbilling.server.mediation;

import java.util.Vector;

public class Format {
    private Vector<FormatField> fields = null;
    
    public Format() {
        fields = new Vector<FormatField>();
    }
    
    public void addField(FormatField newField) {
        fields.add(newField);
    }

    public Vector<FormatField> getFields() {
        return fields;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (FormatField field: fields) {
            sb.append("field: " + field.toString() + "\n");
        }
        return sb.toString();
    }
}
