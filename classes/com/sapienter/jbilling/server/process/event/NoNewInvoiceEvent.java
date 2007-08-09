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
package com.sapienter.jbilling.server.process.event;

import java.util.Date;

import com.sapienter.jbilling.server.system.event.Event;

public class NoNewInvoiceEvent implements Event {
    private final Integer entityId;
    private final Integer userId;
    private final Date billingProcess;
    private final Integer subscriberStauts; // helps determine if the event has to be processed
    
    public NoNewInvoiceEvent(Integer entityId, Integer userId, 
            Date billingProcess, Integer subscriberStatus) {
        this.entityId = entityId;
        this.userId = userId;
        this.billingProcess = billingProcess;
        this.subscriberStauts = subscriberStatus;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public String getName() {
        return "Billing process produced no new invoices for this user";
    }

    public final Date getBillingProcess() {
        return billingProcess;
    }

    public final Integer getSubscriberStatus() {
        return subscriberStauts;
    }

    public final Integer getUserId() {
        return userId;
    }

}
