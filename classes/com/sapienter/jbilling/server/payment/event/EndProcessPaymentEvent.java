package com.sapienter.jbilling.server.payment.event;

import com.sapienter.jbilling.server.system.event.Event;

public class EndProcessPaymentEvent implements Event {

    private final Integer runId;
    private final Integer entityId;
    
    public EndProcessPaymentEvent(Integer runId,Integer entityId) {
        this.runId = runId;
        this.entityId= entityId;
    }

    public Integer getEntityId() {
        return entityId;
    }
    
    public Integer getRunId() {
        return runId;
    }

    public String getName() {
        return "End of asych payment processing event";
    }

}
