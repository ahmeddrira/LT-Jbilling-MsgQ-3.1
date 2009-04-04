/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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
            Integer entityId = invoice.getEntity().getBaseUser().getEntity().getId();
            Integer userId = invoice.getEntity().getBaseUser().getUserId();
            
            PluggableTaskManager taskManager = new PluggableTaskManager(entityId, 
                    Constants.PLUGGABLE_TASK_PAYMENT);

            // search for PaymentRouterTask in the payment chain
            PaymentRouterTask router = null;
            Object task = taskManager.getNextClass();
            while (task != null) {
                if (task instanceof PaymentRouterTask) {
                    router = (PaymentRouterTask) task;
                    break;
                }
                task = taskManager.getNextClass();
            }
            
            if (router == null) {
                throw new TaskException("Can not find router task");
            }
            
            message.setStringProperty("processor", router.getProcessorName(userId));
        } catch (Exception e) {
            throw new TaskException(e);
        } 
    }

}
