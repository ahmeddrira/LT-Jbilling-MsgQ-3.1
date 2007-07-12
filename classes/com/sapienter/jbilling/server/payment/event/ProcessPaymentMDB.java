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

import java.util.Calendar;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.BillingProcessSessionLocal;
import com.sapienter.jbilling.interfaces.BillingProcessSessionLocalHome;
import com.sapienter.jbilling.server.process.BillingProcessRunBL;

/*
 * This message bean is not configured using xdoclet.
 * The configuration needs to be done specifically for each installation/scenario
 * using the files jboos-beans.xml and message-driven-beans.xml
 */
public class ProcessPaymentMDB implements MessageDrivenBean, MessageListener {
    
    private final Logger LOG = Logger.getLogger(ProcessPaymentMDB.class);

    public void ejbRemove() throws EJBException {
        LOG.debug("Removing MDB " + this.hashCode());
    }

    public void ejbCreate() {
        LOG.debug("Creating MDB " + this.hashCode());
    }

    public void setMessageDrivenContext(MessageDrivenContext context)
            throws EJBException {
    }

    public void onMessage(Message message) {
        try {
            LOG.debug("Processing message. Processor " + message.getStringProperty("processor") + 
                    " entity " + message.getIntProperty("entityId") + " by " + this.hashCode());
            MapMessage myMessage = (MapMessage) message;
            
            // use a session bean to make sure the processing is done in one transaction
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            BillingProcessSessionLocalHome processHome =
                (BillingProcessSessionLocalHome) EJBFactory.lookUpLocalHome(
                BillingProcessSessionLocalHome.class,
                BillingProcessSessionLocalHome.JNDI_NAME);
            BillingProcessSessionLocal process = processHome.create();

            String type = message.getStringProperty("type"); 
            if (type.equals("payment")) {
                process.processPayment(
                        (myMessage.getInt("processId") == -1) ? null : myMessage.getInt("processId"),
                        (myMessage.getInt("runId") == -1) ? null : myMessage.getInt("runId"),
                        (myMessage.getInt("invoiceId") == -1) ? null : myMessage.getInt("invoiceId"));
            } else if (type.equals("ender")) {
                BillingProcessRunBL run = new BillingProcessRunBL(myMessage.getInt("runId"));
                run.getEntity().setPaymentFinished(Calendar.getInstance().getTime());
            } else {
                LOG.error("Can not process message of type " + type);
            }
        } catch (Exception e) {
            LOG.error("Generating payment", e);
        }
    }

}
