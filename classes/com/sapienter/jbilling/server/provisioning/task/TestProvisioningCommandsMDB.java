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



package com.sapienter.jbilling.server.provisioning.task;


import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/*
* Dummy message driven bean .
* acts as the 'external provisioning module'. It will simply return the JMS message with all the names of the properties
* changed to be prefixed with 'in_'. A new 'result' property will also be added to the message with
* either a 'success' or 'fail' message, as to be expected by the JUnit tests. Perhaps the value of a
* property could trigger whether a 'success' or 'fail' response it returned
 */
public class TestProvisioningCommandsMDB implements MessageDrivenBean, MessageListener {
    private final Logger    LOG             = Logger.getLogger(TestProvisioningCommandsMDB.class);
    private TopicConnection conn            = null;
    private String          resultPropValue = "success";
    private TopicSession    session         = null;
    private Topic           topic           = null;
    private String          topicName       = "provisioning_commands_reply_topic";

    public void ejbRemove() throws EJBException {
        LOG.debug("Removing Dummy MDB " + this.hashCode());
    }

    public void ejbCreate() {
        LOG.debug("Creating Dummy MDB " + this.hashCode());
    }

    public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException {}

    public void onMessage(Message message) {
        try {
            LOG.debug("Dummy MDB : command= " + message.getStringProperty("command") + "- entity= "
                      + message.getIntProperty("entityId") + " -processing message by Dummy MDB = " + this.hashCode());

            MapMessage myMessage = (MapMessage) message;

            processMessage(myMessage);
            pause(2000);
            sendReply(myMessage);
        } catch (Exception e) {
            LOG.error("processing provisioning command reply", e);
        }
    }

    public void setupPubSub() throws JMSException, NamingException {
        InitialContext         iniCtx = new InitialContext();
        Object                 tmp    = iniCtx.lookup("ConnectionFactory");
        TopicConnectionFactory tcf    = (TopicConnectionFactory) tmp;

        conn    = tcf.createTopicConnection();
        topic   = (Topic) iniCtx.lookup("topic/" + topicName);
        session = conn.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        conn.start();
    }

    public void sendReply(MapMessage tm) throws JMSException, NamingException {
        LOG.debug("Begin sendReply");

        // Setup the PubSub connection, session
        setupPubSub();

        // Send message
        TopicPublisher send = session.createPublisher(topic);

        send.publish(tm);
        LOG.debug("sendReply, sent message=" + tm.toString());
        send.close();
    }

    private void pause(long t) {
        LOG.debug("Dummy MDB: pausing for " + t + " ms...");

        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processMessage(MapMessage message) throws JMSException {
        LOG.debug("TestProvisioningCommandsMDB.processMessage()");

        LinkedList<StringPair> in_commandQueue = new LinkedList<StringPair>();
        Enumeration            props           = message.getPropertyNames();

        LOG.debug("Messge Property Names: ");
        LOG.debug(props);

        if (props == null) {
            return;
        }

        while (props.hasMoreElements()) {
            String propName  = (String) props.nextElement();
            String propValue = message.getStringProperty(propName);

            LOG.debug("( " + propName + "," + propValue + " )");

            if ((propName == null) || (propValue == null)) {
                throw new JMSException("Message property is null !");
            } else {
                String     in_propName = "in_" + propName;
                StringPair in_prop     = new StringPair(in_propName, propValue);

                in_commandQueue.add(in_prop);
            }
        }

        if (in_commandQueue.isEmpty()) {
            return;
        }

        // clear old message properties
        message.clearProperties();
        LOG.debug("cleared old properties for message: " + message);

        // populate Message with new 'in_' prefixed properties
        for (Iterator<StringPair> it = in_commandQueue.iterator(); it.hasNext(); ) {
            StringPair param = it.next();

            message.setStringProperty(param.getName(), param.getValue());
            LOG.debug("Message:" + message.toString() + " - added new property : (" + param.getName() + ","
                      + param.getValue() + ")");
        }

        // add result property
        message.setStringProperty("result", resultPropValue);
    }
}
