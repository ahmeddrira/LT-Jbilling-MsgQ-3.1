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

/*
 * Created on Nov 18, 2004
 *
 */
package com.sapienter.jbilling.server.order;

import java.io.Serializable;
import java.util.Comparator;

import com.sapienter.jbilling.server.order.db.OrderLineDTO;

/**
 * @author Emil
 *
 */
public class OrderLineComparator implements Comparator, Serializable {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        int retValue = 0;
        OrderLineDTO perA = (OrderLineDTO) o1;
        OrderLineDTO perB = (OrderLineDTO) o2;
        
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
