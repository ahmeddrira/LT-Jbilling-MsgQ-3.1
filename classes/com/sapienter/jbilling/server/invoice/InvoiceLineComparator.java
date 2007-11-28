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
 * Created on Dec 11, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.invoice;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @author Emil
 */
public class InvoiceLineComparator implements Comparator {

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1) {
        InvoiceLineDTOEx perA = (InvoiceLineDTOEx) arg0;
        InvoiceLineDTOEx perB = (InvoiceLineDTOEx) arg1;
        int retValue;
        
        // the line type should tell first
        if (perA.getOrderPosition().equals(perB.getOrderPosition())) {
            
            try {
                if (perA.getTypeId().equals(
                        Constants.INVOICE_LINE_TYPE_SUB_ACCOUNT) &&
                        perB.getTypeId().equals(
                            Constants.INVOICE_LINE_TYPE_SUB_ACCOUNT)) {
                    // invoice lines have to be grouped by user
                    // find out both users
                    retValue = perA.getSourceUserId().compareTo(perB.getSourceUserId());
                    /*
                    Logger.getLogger(InvoiceLineComparator.class).debug(
                            "Testing two sub account lines. a.userid " + 
                            perA.getSourceUserId() + " b.userid " + perB.getSourceUserId() +
                            " result " + retValue);
                            */
                    if (retValue != 0) {
                        // these are lines for two different users, so 
                        // they are different enough now
                        return retValue;
                    }
                } 
                // use the number
                if (perA.getItemId() != null && perB.getItemId() 
                        != null) {
                    ItemBL itemA = new ItemBL(perA.getItemId());
                    ItemBL itemB = new ItemBL(perB.getItemId());
                    if (itemA.getEntity().getNumber() == null &&
                            itemB.getEntity().getNumber() == null) {
                        retValue = perA.getItemId().compareTo(
                                perB.getItemId());
                    } else if (itemA.getEntity().getNumber() == null) {
                        retValue = 1;
                    } else if (itemB.getEntity().getNumber() == null) {
                        retValue = -1;
                    } else {
                        // none are null
                        retValue = itemA.getEntity().getNumber().compareTo(
                                itemB.getEntity().getNumber());
                    }
                } else {
                    retValue = 0;
                }
            } catch (Exception e) {
                Logger.getLogger(InvoiceLineComparator.class).error(
                        "Comparing invoice lines " + perA + " " + perB, e);
                retValue = 0;
            }
        } else {
            retValue = perA.getOrderPosition().compareTo(perB.getOrderPosition());
        }
/*        
        Logger.getLogger(InvoiceLineComparator.class).debug(
                "Comparing " + perA.getId() + " " + perB.getId() +
                " result " + retValue);
*/        
        return retValue;
    }

}
