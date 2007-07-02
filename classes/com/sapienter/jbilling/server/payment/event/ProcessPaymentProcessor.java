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

import javax.jms.JMSException;
import javax.naming.NamingException;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.payment.tasks.IAsyncPaymentParameters;
import com.sapienter.jbilling.server.system.event.AsynchronousEventProcessor;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.util.Constants;

/*
 * All this class has to do is to populate the 'message' field.
 * The actual posting on the queue is done by the parent/caller
 */
public class ProcessPaymentProcessor extends AsynchronousEventProcessor<IAsyncPaymentParameters> {

    private Integer entityId;
    private final String queueName = "processors";
    
    public ProcessPaymentProcessor() 
            throws JMSException, NamingException {
        super();
    }
    
    @Override
    public void process(Event event) {
        entityId = event.getEntityId();
        if (event instanceof ProcessPaymentEvent) {
            ProcessPaymentEvent pEvent;
            pEvent = (ProcessPaymentEvent) event;
            processPayment(pEvent);
        } else if (event instanceof EndProcessPaymentEvent) {
            EndProcessPaymentEvent endEvent = (EndProcessPaymentEvent) event;
            processEnd(endEvent);
        } else {
            throw new SessionInternalError("Can only process payment events");
        }
    }
    
    private void processPayment(ProcessPaymentEvent event) {
        // transform the event into map message fields
        try {
            message.setInt("invoiceId", event.getInvoiceId());
            message.setInt("processId", (event.getProcessId() == null) ? -1 : event.getProcessId());
            message.setInt("runId", (event.getRunId() == null) ? -1 : event.getRunId());
            message.setStringProperty("type", "payment");
            
            // add additional fields from the associated plug-in
            IAsyncPaymentParameters task = getPluggableTask(entityId, 
                    Constants.PLUGGABLE_TASK_ASYNC_PAYMENT_PARAMS);
            task.addParameters(message);
        } catch (Exception e) {
            throw new SessionInternalError("Error transforming message ", 
                    this.getClass(), e);
        }
    }
    
    private void processEnd(EndProcessPaymentEvent event) {
        try {
            message.setInt("runId", event.getRunId());
            message.setStringProperty("type", "ender");
        } catch (Exception e) {
            throw new SessionInternalError("Error transforming message ", 
                    this.getClass(), e);
        }
    }
    
    @Override
    protected int getEntityId() {
        return entityId.intValue();
    }
    
    @Override
    protected String getQueueName() {
        return queueName;
    }
    
}
