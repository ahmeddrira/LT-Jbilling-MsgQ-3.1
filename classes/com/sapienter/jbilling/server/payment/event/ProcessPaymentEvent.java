package com.sapienter.jbilling.server.payment.event;

import com.sapienter.jbilling.server.system.event.Event;

public class ProcessPaymentEvent implements Event {
    private final Integer invoiceId;
    private final Integer processId;
    private final Integer runId;
    private final Integer entityId;
    
    public ProcessPaymentEvent(Integer invoiceId,Integer processId,Integer runId,Integer entityId) {
        this.runId = runId;
        this.invoiceId = invoiceId;
        this.processId = processId;
        this.entityId= entityId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public String getName() {
        return "Process Payment";
    }

    public final Integer getInvoiceId() {
        return invoiceId;
    }

    public final Integer getProcessId() {
        return processId;
    }

    public final Integer getRunId() {
        return runId;
    }

}
