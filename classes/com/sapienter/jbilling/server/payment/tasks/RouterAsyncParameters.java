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
package com.sapienter.jbilling.server.payment.tasks;

import javax.jms.MapMessage;

import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.pluggableTask.PaymentRouterTask;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.util.Constants;

public class RouterAsyncParameters extends PluggableTask implements IAsyncPaymentParameters  {

    public void addParameters(MapMessage message) throws TaskException {
        try {
            InvoiceBL invoice = new InvoiceBL(message.getInt("invoiceId"));
            Integer entityId = invoice.getEntity().getUser().getEntity().getId();
            Integer userId = invoice.getEntity().getUser().getUserId();
            
            PluggableTaskManager taskManager = new PluggableTaskManager(entityId, 
                    Constants.PLUGGABLE_TASK_PAYMENT);
            // the router task HAS to be the first in the payment chain
            PaymentRouterTask router = (PaymentRouterTask) taskManager.getNextClass();
            if (router == null) {
                throw new TaskException("Can not find router task");
            }
            
            message.setStringProperty("processor", router.getProcessorName(userId));
        } catch (Exception e) {
            throw new TaskException(e);
        } 
    }

}
