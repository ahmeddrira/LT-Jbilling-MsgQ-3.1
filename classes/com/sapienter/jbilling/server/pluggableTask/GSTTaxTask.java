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

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.order.OrderLineDTOEx;
import com.sapienter.jbilling.server.util.Constants;

/**
 * Basic tasks that takes the quantity and multiplies it by the price to 
 * get the lines total. It also updates the order total with the addition
 * of all line totals
 * 
 * @author emilc
 *
 */
public class GSTTaxTask extends PluggableTask implements OrderProcessingTask {

    // pluggable task parameters names
    public static final String PARAMETER_RATE = "rate";
    public static final String PARAMETER_DESCRIPTION = "description";

    /**
     * 
     * @see com.sapienter.jbilling.server.order.OrderProcessingTask#doProcessing(com.sapienter.betty.server.order.NewOrderDTO)
     */
    public void doProcessing(NewOrderDTO order) throws TaskException {
        float orderTotal = order.getOrderTotal().floatValue();
        float taxRate = ((Float) parameters.get(PARAMETER_RATE))
                .floatValue();
        float gstTax = (orderTotal / 100F) * taxRate;
        OrderLineDTOEx taxLine = new OrderLineDTOEx();
        taxLine.setAmount(new Float(gstTax));
        taxLine.setDeleted(new Integer(0));
        taxLine.setDescription((String) parameters.get(PARAMETER_DESCRIPTION));
        taxLine.setTypeId(Constants.ORDER_LINE_TYPE_TAX);
        try {

            taxLine.setEditable(
                OrderBL.lookUpEditable(Constants.ORDER_LINE_TYPE_TAX));
        } catch (SessionInternalError e) {
            throw new TaskException("Error in GSTTaxTask. Bad order_line_type");
        }
        order.setOrderLine(new Integer(0), taxLine);

        order.setOrderTotal(new Float(orderTotal + gstTax));
    }

}
