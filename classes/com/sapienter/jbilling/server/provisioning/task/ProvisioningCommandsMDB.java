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

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.log4j.Logger;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.server.provisioning.ProvisioningProcessSession;
import com.sapienter.jbilling.server.provisioning.ProvisioningProcessSessionHome;



/*
* This message bean is not configured using xdoclet.
* The configuration needs to be done specifically for each installation/scenario
* using the files jboss-beans.xml and message-driven-beans.xml
 */
public class ProvisioningCommandsMDB implements MessageDrivenBean, MessageListener {
    private final Logger LOG = Logger.getLogger(ProvisioningCommandsMDB.class);

    public void ejbRemove() throws EJBException {
        LOG.debug("Removing MDB " + this.hashCode());
    }

    public void ejbCreate() {
        LOG.debug("Creating MDB " + this.hashCode());
    }

    public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException {}

    public void onMessage(Message message) {
        try {
            LOG.debug("Provisioning command MDB " + " command=" + message.getStringProperty("in_command") + "- entity="
                      + message.getIntProperty("in_entityId") + " - Processing message by  " + this.hashCode());

            MapMessage myMessage            = (MapMessage) message;
            String     in_order_line_id_str = myMessage.getStringProperty("in_order_line_id");
            String     in_order_id_str      = myMessage.getStringProperty("in_order_id");
            Integer    in_order_id          = null;
            Integer    in_order_line_id     = null;

            try {
                in_order_line_id = Integer.parseInt(in_order_line_id_str.trim());
            } catch (Exception e) {}

            LOG.debug("Message property in_order_line_id value : " + in_order_line_id);

            try {
                in_order_id = Integer.parseInt(in_order_id_str.trim());
            } catch (Exception e) {}

            LOG.debug("Message property in_order_id value : " + in_order_id);

            String result = myMessage.getStringProperty("result");

            LOG.debug("Message property result value : " + result);

            ProvisioningProcessSessionHome provisioningHome = (ProvisioningProcessSessionHome) JNDILookup.getFactory(
                                                                  true).lookUpHome(
                                                                  ProvisioningProcessSessionHome.class,
                                                                  ProvisioningProcessSessionHome.JNDI_NAME);
            ProvisioningProcessSession remoteProvisioning = provisioningHome.create();

            remoteProvisioning.updateProvisioningStatus(in_order_id, in_order_line_id, result);
        } catch (Exception e) {
            LOG.error("processing provisioning command", e);
        }
    }
}
