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
