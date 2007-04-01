package com.sapienter.jbilling.server.payment.event;

import com.sapienter.jbilling.server.payment.PaymentDTOEx;

public class PaymentSuccessfulEvent extends AbstractPaymentEvent {
    public PaymentSuccessfulEvent(Integer entityId, PaymentDTOEx payment) {
        super(entityId, payment);
    }
    
    public String getName() {
        return "Payment Successful";
    }
    
}
