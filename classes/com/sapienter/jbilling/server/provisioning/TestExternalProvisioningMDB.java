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

package com.sapienter.jbilling.server.provisioning;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.server.util.Constants;

/**
 * Tests responses from the external provisioning module. Receives 
 * messages via JMS. Note that ProvisioningCommandsMDB also processes
 * these messages. See also TestExternalProvisioningTask, 
 * ProvisioningTest, provisioning_commands.drl and jbilling-provisioning.xml.
 * Configured in jboss-beans.xml and message-driven-beans.xml. 
 */
public class TestExternalProvisioningMDB implements MessageDrivenBean, MessageListener {
    private static final Logger LOG = Logger.getLogger(TestExternalProvisioningMDB.class);

    private int count = 0; // what message is expected to be received
    private boolean fail = false; // whether the test has failed

    public void onMessage(Message message) {
        try {
            MapMessage myMessage = (MapMessage) message;

            LOG.debug("Got a message. Command: " + 
                    myMessage.getStringProperty("in_command"));

            // only test messages generated by the result test command
            if (!myMessage.getStringProperty("in_command")
                    .equals("result_test")) {
                return;
            }

            // pause while ProvisioningCommandsMDB is updating the order line
            pause(2000);

            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ProvisioningProcessSessionHome provisioningHome = 
                    (ProvisioningProcessSessionHome) EJBFactory.lookUpHome(
                    ProvisioningProcessSessionHome.class,
                    ProvisioningProcessSessionHome.JNDI_NAME);
            ProvisioningProcessSession provisioning = provisioningHome.create();

            // only sucessful after receiving the last expected message 
            // sucessfully (and no other tests failed). 
            boolean success = false;

            String result = myMessage.getStringProperty("out_result");
            switch (count) {
                case 0:
                    if (!result.equals("success")) {
                        fail = true;
                        LOG.error("Expected a result of 'success', but got '" +
                                result + "'");
                    } else {
                        LOG.debug("Got 'success' result");
                    }
                    break;

                case 1:
                    if (!result.equals("unavailable")) {
                        fail = true;
                        LOG.error("Expected a result of 'unavailable', but " +
                                "got '" + result + "'");
                    } else {
                        LOG.debug("Got 'unavailable' result");
                        String exception = myMessage.getStringProperty("exception");
                        if (exception == null) {
                            fail = true;
                            LOG.error("Expected an exception property.");
                        } else if (!exception.equals("com.sapienter.jbilling.server.pluggableTask.TaskException: Test Exception")) {
                            fail = true;
                            LOG.error("Expected a Task Exception, but got: " + 
                                exception);
                        } else {
                            LOG.debug("Got a Task Exception");
                        }
                    }
                    break;

                case 2:
                    if (!result.equals("fail")) {
                        fail = true;
                        LOG.error("Expected a result of 'fail', but got '" +
                                result + "'");
                    } else {
                        LOG.debug("Got 'fail' result");
                        if (!fail) {
                            success = true;
                        }
                    }
                    break;

                default:
                    fail = true;
                    LOG.error("Too many messages.");
                    break;
            }

            Integer orderLineId =  myMessage.getIntProperty("in_order_line_id");

            // Set the order line's provisioning status to 'ACTIVE' if
            // test is complete, others to 'FAILED' (overrides 
            // ProvisioningCommandsMDB).
            if (success) {
                LOG.debug("Provisioning status of order line id " + 
                        orderLineId + " updated to ACTIVE");
                provisioning.updateProvisioningStatus(orderLineId,
                        Constants.PROVISIONING_STATUS_ACTIVE);
            } else {
                LOG.debug("Provisioning status of order line id " + 
                        orderLineId + " updated to FAILED");
                provisioning.updateProvisioningStatus(orderLineId,
                        Constants.PROVISIONING_STATUS_FAILED);
            }

            count++;

        } catch (Exception e) {
            LOG.error("processing provisioning command", e);
        }
    }

    private void pause(long t) {
        LOG.debug("TestExternalProvisioningMDB: pausing for " + t + " ms...");

        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
        }
    }

    public void ejbRemove() throws EJBException {
        LOG.debug("Removing MDB " + this.hashCode());
    }

    public void ejbCreate() {
        LOG.debug("Creating MDB " + this.hashCode());
    }

    public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException {
    }
}