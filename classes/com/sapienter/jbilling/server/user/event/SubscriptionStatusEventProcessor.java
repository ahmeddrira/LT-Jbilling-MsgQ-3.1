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
package com.sapienter.jbilling.server.user.event;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.payment.event.PaymentFailedEvent;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.system.event.EventProcessor;
import com.sapienter.jbilling.server.user.tasks.ISubscriptionStatusManager;
import com.sapienter.jbilling.server.util.Constants;

public class SubscriptionStatusEventProcessor extends EventProcessor<ISubscriptionStatusManager> {

    public void process(Event event) {
        // get an instance of the pluggable task
        ISubscriptionStatusManager task = getPluggableTask(event.getEntityId(),
                Constants.PLUGGABLE_TASK_SUBSCRIPTION_STATUS);
        
        if (task == null) {
            throw new SessionInternalError("There isn't a task configured " +
                    "to handle subscription status");
        }
        // depending on the event, call the right method of the task
        if (event instanceof PaymentFailedEvent) {
            PaymentFailedEvent pfEvent = (PaymentFailedEvent) event;
            task.paymentFailed(pfEvent.getEntityId(), pfEvent.getPayment());
        }
    }
}
