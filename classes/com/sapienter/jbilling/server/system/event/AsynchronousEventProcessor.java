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

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public abstract class AsynchronousEventProcessor<TType> extends EventProcessor<TType> {
    private static final Logger LOG = Logger.getLogger(AsynchronousEventProcessor.class); 

    private QueueConnection conn;
    private QueueSession session;
    private Queue myQueue;
    protected MapMessage message;
    
    protected AsynchronousEventProcessor() 
            throws JMSException, NamingException {
        setupPTP();
        message = session.createMapMessage();
    }
    
    private void setupPTP()
            throws JMSException, NamingException {
        InitialContext iniCtx = new InitialContext();
        Object tmp = iniCtx.lookup("java:/ConnectionFactory");
        QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
        conn = qcf.createQueueConnection();
        myQueue = (Queue) iniCtx.lookup("queue/" + getQueueName());
        session = conn.createQueueSession(false,
           QueueSession.AUTO_ACKNOWLEDGE);
        conn.start();
    }
    
    protected void doPost() 
            throws JMSException, NamingException {
        message.setIntProperty("entityId", getEntityId());
        // the extending processor is free of adding additional properties
        // such as the processor name for payment processing
        QueueSender sender = session.createSender(myQueue); 
        sender.send(message);
        sender.close();
    }
    
    protected abstract String getQueueName();
    protected abstract int getEntityId();
}
