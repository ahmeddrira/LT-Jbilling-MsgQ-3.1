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
package com.sapienter.jbilling.server.system.event;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.order.event.NewActiveUntilEvent;
import com.sapienter.jbilling.server.order.event.NewStatusEvent;
import com.sapienter.jbilling.server.payment.event.PaymentFailedEvent;
import com.sapienter.jbilling.server.payment.event.PaymentSuccessfulEvent;
import com.sapienter.jbilling.server.user.event.SubscriptionStatusEventProcessor;


/**
 * This class provides a link between an event and a pluggable task.
 * PTs subscribe to observe events. When an event happens, the manager
 * will call the observing PTs through the event processors.
 * A PT subscribing has to do so by adding an event processor to the list. 
 * @author Emiliano Conde
 */
public final class EventManager {
    private static final Logger LOG = Logger.getLogger(EventManager.class); 

    // this represents the subscriptions of processors to events
    static Hashtable<Class, Class[]> subscriptions;
    static {
        subscriptions = new Hashtable<Class, Class[]>();
        // PaymentFailedEvent
        subscriptions.put(PaymentFailedEvent.class, 
                new Class[] { SubscriptionStatusEventProcessor.class, } );
        // PaymentSuccessful
        subscriptions.put(PaymentSuccessfulEvent.class,
                new Class[] { SubscriptionStatusEventProcessor.class, } );
        // NewActiveUntil (orders)
        subscriptions.put(NewActiveUntilEvent.class,
                new Class[] { SubscriptionStatusEventProcessor.class, } );
        // NewStatus (orders)
        subscriptions.put(NewStatusEvent.class,
                new Class[] { SubscriptionStatusEventProcessor.class, } );
    }

    public static final void process(Event event){
        LOG.debug("processing event " + event);
        Class processors[] = (Class[]) subscriptions.get(event.getClass());
        if (processors == null) {
            LOG.warn("No processors for class " + event.getClass());
            return;
        }
        for (int f = 0; f < processors.length; f++) {
            // create a new processor
            EventProcessor processor;
            try {
                processor = (EventProcessor) processors[f].newInstance();
                LOG.debug("Now processing with " + processor);
                processor.process(event);
            } catch (Exception e) {
                throw new SessionInternalError("Error processing an event " + event, 
                        EventManager.class, e);
            }                
        }
    }
}
