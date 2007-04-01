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

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.pluggableTask.ProcessorAlarm;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.system.event.EventProcessor;
import com.sapienter.jbilling.server.util.Constants;

public class GatewayAlarmEventProcessor extends EventProcessor<ProcessorAlarm> {
	private static final Logger LOG = Logger.getLogger(GatewayAlarmEventProcessor.class);
	
	@Override
	public void process(Event event) {
		if (false == event instanceof AbstractPaymentEvent){
			return;
		}
		
		AbstractPaymentEvent paymentEvent = (AbstractPaymentEvent)event;
		ProcessorAlarm alarm = getPluggableTask(event.getEntityId(), 
				Constants.PLUGGABLE_TASK_PROCESSOR_ALARM);
		
		if (alarm == null){
            // it is OK not to have an alarm configured
            LOG.info("Alarm not present for entity " + event.getEntityId());
			return;
		}
		
		String paymentProcessor = paymentEvent.getPaymentProcessor();
		if (paymentProcessor == null){
			LOG.warn("Payment event without payment processor id : " + event);
			paymentProcessor = "";
		}
		alarm.init(paymentProcessor, event.getEntityId());
		if (event instanceof PaymentFailedEvent){
			alarm.fail();
		} else if (event instanceof PaymentProcessorUnavailableEvent){
			alarm.unavailable();
		} else if (event instanceof PaymentSuccessfulEvent){
			alarm.successful();
		}
	}
	
	
}
