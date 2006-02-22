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
