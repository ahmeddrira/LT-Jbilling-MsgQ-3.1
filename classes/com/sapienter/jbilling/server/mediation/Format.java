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
