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

import java.util.Comparator;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.PricingField;

public class GroupRecordComparator implements Comparator<Record> {

    public final Format format;
    
    public GroupRecordComparator(Format format) {
        this.format = format;
    }
    
    public int compare(Record r1, Record r2) {
        // handle the nulls
        if (r1 == null && r2 == null) {
            return 0;
        } else if (r1 == null) {
            return 1;
        } else if (r2 == null) {
            return -1;
        }
        
        // if there aren't any key fields, then assume that all the
        // records are different
        boolean atLeastOne = false;
        
        // so none are null
        for (FormatField field: format.getFields()) {
            if (field.getIsKey()) {
                String pField1 = null, pField2 = null;
                atLeastOne = true;
                // find this field in both records
                // optimize by having the fields as hashmaps, rather than vectors
                for (PricingField pfield: r1.getFields()) {
                    if (pfield.getName().equals(field.getName())) {
                        pField1 = pfield.getValue().toString();
                        break;
                    }
                }
                for (PricingField pfield: r2.getFields()) {
                    if (pfield.getName().equals(field.getName())) {
                        pField2 = pfield.getValue().toString();
                        break;
                    }
                }
                if (pField1 == null || pField2 == null) {
                    throw new SessionInternalError("Can not find field for comparison of:" + r1 
                            + " with " + r2);
                }
                
                if (!pField1.equals(pField2)) {
                    return pField1.compareTo(pField2);
                }
            }
        }
        return atLeastOne ? 0 : -1;
    }

}
