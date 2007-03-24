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
package com.sapienter.jbilling.server.payment.event;

import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.system.event.Event;

public class PaymentFailedEvent implements Event {

    private PaymentDTOEx payment;
    private Integer entityId;
    
    public PaymentFailedEvent(Integer entityId, PaymentDTOEx payment) {
        this.payment = payment;
        this.entityId = entityId;
    }
    
    public String getName() {
        return "Payment Failed";
    }
    
    public Integer getEntityId() {
        return entityId;
    }
    
    public String toString() {
        return "Event " + getName() + " payment: " + payment + " entityId: " + entityId;
    }

    public PaymentDTOEx getPayment() {
        return payment;
    }
}
