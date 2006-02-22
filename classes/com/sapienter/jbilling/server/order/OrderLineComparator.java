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

/*
 * Created on Nov 18, 2004
 *
 */
package com.sapienter.jbilling.server.order;

import java.util.Comparator;

/**
 * @author Emil
 *
 */
public class OrderLineComparator implements Comparator {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        int retValue = 0;
        OrderLineDTOEx perA = (OrderLineDTOEx) o1;
        OrderLineDTOEx perB = (OrderLineDTOEx) o2;
        
        if (perA != null && perA.getItem() != null && 
                perA.getItem().getNumber() != null &&
                perB != null && perB.getItem() != null && 
                    perB.getItem().getNumber() != null) {
            retValue = perA.getItem().getNumber().compareTo(
                    perB.getItem().getNumber());
        } else if (perA != null && perA.getItem() != null && 
                    perA.getItem().getNumber() != null) {
                retValue = -1;
        } else if (perB != null && perB.getItem() != null && 
                perB.getItem().getNumber() != null) {
            retValue = 1;
        }
        
        return retValue;
    }

}
