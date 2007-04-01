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

import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.util.Constants;

public abstract class AbstractPaymentEvent implements Event {
    private final PaymentDTOEx payment;
    private final Integer entityId;
    
    public static AbstractPaymentEvent forPaymentResult(Integer entityId, PaymentDTOEx payment){
		Integer result = payment.getResultId();
        // some processors don't do anything (fake), only pass to the next 
        // processor in the chain
		if (result == null){
			return null;
		}
		AbstractPaymentEvent event = null;
		if (Constants.RESULT_UNAVAILABLE.equals(result)){
			event = new PaymentProcessorUnavailableEvent(entityId, payment);
		} else if (Constants.RESULT_OK.equals(result)){
			event = new PaymentSuccessfulEvent(entityId, payment);
		} else if (Constants.RESULT_FAIL.equals(result)){
			event = new PaymentFailedEvent(entityId, payment);
		}
		return event;
    }
    
    public AbstractPaymentEvent(Integer entityId, PaymentDTOEx payment) {
        this.payment = payment;
        this.entityId = entityId;
    }
    
    public final Integer getEntityId() {
        return entityId;
    }
    
    public final PaymentDTOEx getPayment() {
        return payment;
    }

    public String toString() {
        return "Event " + getName() + " payment: " + payment + " entityId: " + entityId;
    }
    
    public String getPaymentProcessor(){
    	PaymentAuthorizationDTO auth = payment.getAuthorization();
    	return auth == null ? null : auth.getProcessor();
    }

}
