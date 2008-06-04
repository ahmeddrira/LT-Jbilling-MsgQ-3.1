/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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
