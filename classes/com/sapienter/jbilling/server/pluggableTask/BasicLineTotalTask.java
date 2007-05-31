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

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.order.OrderLineDTOEx;
import com.sapienter.jbilling.server.util.Constants;

/**
 * Basic tasks that takes the quantity and multiplies it by the price to 
 * get the lines total. It also updates the order total with the addition
 * of all line totals
 * 
 */
public class BasicLineTotalTask extends PluggableTask implements OrderProcessingTask {

    private final Logger LOG = Logger.getLogger(BasicLineTotalTask.class);

    /**
     * 
     * @see com.sapienter.jbilling.server.order.OrderProcessingTask#doProcessing(com.sapienter.betty.server.order.NewOrderDTO)
     */
    public void doProcessing(NewOrderDTO order) 
        throws TaskException {
        // calculations are done with 10 decimals. 
        // The final total is the rounded to 2 decimals.
        BigDecimal orderTotal = new BigDecimal("0.0000000000");
        BigDecimal taxPerTotal = new BigDecimal("0.0000000000");
        BigDecimal taxNonPerTotal = new BigDecimal("0.0000000000");
        BigDecimal nonTaxPerTotal = new BigDecimal("0.0000000000");
        BigDecimal nonTaxNonPerTotal = new BigDecimal("0.0000000000");
        
        
        
        // step one, go over the non-percentage items,
        // collecting both tax and non-tax values
        for (OrderLineDTOEx line : order.getRawOrderLines()) {
            if (line.getItem() != null && 
                    line.getItem().getPercentage() == null) { 
                BigDecimal amount = new BigDecimal(
                        line.getQuantity().toString());
                amount = amount.multiply(new BigDecimal(
                        line.getPrice().toString()));
                line.setAmount(new Float(amount.floatValue()));
                if (line.getTypeId().equals(Constants.ORDER_LINE_TYPE_TAX)) {
                    taxNonPerTotal = taxNonPerTotal.add(amount);
                } else {
                    nonTaxNonPerTotal = nonTaxNonPerTotal.add(amount);
                }
                LOG.debug("adding normal line. Totals =" + taxNonPerTotal + 
                        " - " + nonTaxNonPerTotal);
            }
        }
        
        // step two non tax percetage items
        for (OrderLineDTOEx line : order.getRawOrderLines()) {
            if (line.getItem() != null && 
                    line.getItem().getPercentage() != null &&
                    !line.getTypeId().equals(Constants.ORDER_LINE_TYPE_TAX)) {
                BigDecimal amount = nonTaxNonPerTotal.divide(new BigDecimal("100"), 
                        Constants.BIGDECIMAL_ROUND);
                amount = amount.multiply(new BigDecimal(
                        line.getPrice().toString()));
                amount = amount.setScale(2, Constants.BIGDECIMAL_ROUND);
                line.setAmount(new Float(amount.floatValue()));
                nonTaxPerTotal = nonTaxPerTotal.add(amount);
                LOG.debug("adding no tax percentage line. Total =" + nonTaxPerTotal);
            }
        }
        
        // step three: tax percetage items
        BigDecimal allNonTaxes = nonTaxNonPerTotal.add(nonTaxPerTotal);
        for (OrderLineDTOEx line : order.getRawOrderLines()) {
            if (line.getItem() != null && 
                    line.getItem().getPercentage() != null &&
                    line.getTypeId().equals(Constants.ORDER_LINE_TYPE_TAX)) {
                BigDecimal amount = allNonTaxes.divide(new BigDecimal("100"), 
                        BigDecimal.ROUND_HALF_EVEN);
                amount = amount.multiply(new BigDecimal(
                        line.getPrice().toString()));
                amount = amount.setScale(2, Constants.BIGDECIMAL_ROUND);
                line.setAmount(new Float(amount.floatValue()));
                taxPerTotal = taxPerTotal.add(amount);
                LOG.debug("adding tax percentage line. Total =" + taxPerTotal);
            }
        }

        orderTotal = taxNonPerTotal.add(taxPerTotal).add(nonTaxPerTotal)
                .add(nonTaxNonPerTotal);
        orderTotal = orderTotal.setScale(2, Constants.BIGDECIMAL_ROUND);
        order.setOrderTotal(new Float(orderTotal.floatValue()));
    }

}
