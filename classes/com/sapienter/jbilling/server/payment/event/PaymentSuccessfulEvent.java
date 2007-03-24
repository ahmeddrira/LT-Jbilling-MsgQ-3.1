package com.sapienter.jbilling.server.payment.event;

import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.system.event.Event;

public class PaymentSuccessfulEvent implements Event {
    private PaymentDTOEx payment;
    private Integer entityId;
    
    public PaymentSuccessfulEvent(Integer entityId, PaymentDTOEx payment) {
        this.payment = payment;
        this.entityId = entityId;
    }
    
    public String getName() {
        return "Payment Successful";
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
