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

import com.sapienter.jbilling.server.item.PricingField;

public class Record {
    private Vector<PricingField> fields = null;
    private int position;
    
    public Record() {
        fields = new Vector<PricingField>();
    }
    
    public int getPosition() {
        return position;
    }
    
    public void addField(PricingField field) {
        fields.add(field);
    }

    public void setPosition(int position) {
        this.position = position;
        for (PricingField field: fields) {
            field.setPosition(position);
        }
    }

    public Vector<PricingField> getFields() {
        return fields;
    }
 
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("record=position: " + position + "\n");
        for (PricingField field: fields) {
            sb.append("field: " + field + "\n");
        }
        return sb.toString();
    }
}
