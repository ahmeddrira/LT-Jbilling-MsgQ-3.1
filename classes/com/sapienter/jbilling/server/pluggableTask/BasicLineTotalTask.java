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

package com.sapienter.jbilling.server.pluggableTask;

import java.math.BigDecimal;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.order.OrderLineDTOEx;

/**
 * Basic tasks that takes the quantity and multiplies it by the price to 
 * get the lines total. It also updates the order total with the addition
 * of all line totals
 * 
 */
public class BasicLineTotalTask extends PluggableTask implements OrderProcessingTask {

    /**
     * 
     * @see com.sapienter.jbilling.server.order.OrderProcessingTask#doProcessing(com.sapienter.betty.server.order.NewOrderDTO)
     */
    public void doProcessing(NewOrderDTO order) 
        throws TaskException {
        BigDecimal orderTotal = new BigDecimal(0);
        
        Logger log = Logger.getLogger(BasicLineTotalTask.class);
        
        // go over those that are not percentage
        for (Enumeration items = order.getOrderLinesMap().keys();
                items.hasMoreElements();) {
            Integer itemId = (Integer) items.nextElement();
            OrderLineDTOEx line = (OrderLineDTOEx) order.getOrderLinesMap().
                    get(itemId);

            if (line.getItem() != null && 
                    line.getItem().getPercentage() == null) { 
                BigDecimal amount = new BigDecimal(
                        line.getQuantity().toString());
                amount = amount.multiply(new BigDecimal(
                        line.getPrice().toString()));
                line.setAmount(new Float(amount.floatValue()));
                orderTotal = orderTotal.add(amount);
                log.debug("adding normal line. Total =" + orderTotal);
            }
        }
        
        // now the percetage items
        BigDecimal subTotal = orderTotal;
        for (Enumeration items = order.getOrderLinesMap().keys();
                items.hasMoreElements();) {
            Integer itemId = (Integer) items.nextElement();
            OrderLineDTOEx line = (OrderLineDTOEx) order.getOrderLinesMap().
                    get(itemId);

            if (line.getItem() != null && 
                    line.getItem().getPercentage() != null) {
                BigDecimal amount = subTotal.divide(new BigDecimal("100"), 
                        BigDecimal.ROUND_HALF_EVEN);
                amount = amount.multiply(new BigDecimal(
                        line.getPrice().toString()));
                line.setAmount(new Float(amount.floatValue()));
                orderTotal = orderTotal.add(amount);
                log.debug("adding percentage line. Total =" + orderTotal);
            }
        }

        order.setOrderTotal(new Float(orderTotal.floatValue()));
    }

}
