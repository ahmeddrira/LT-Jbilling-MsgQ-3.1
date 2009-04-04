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
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;

/**
 * Receives messages from the provisioning commands rules task. Calls
 * the external provisioning logic through the provisioning session 
 * bean so it runs in a transaction. Configured in jboss-beans.xml
 * and message-driven-beans.xml.
 */
public class ExternalProvisioningMDB implements MessageDrivenBean, 
        MessageListener {

    private static final Logger LOG = Logger.getLogger(
            ExternalProvisioningMDB.class);

    public void onMessage(Message message) {
        try {
            LOG.debug("Received a message");

            // use a session bean to make sure the processing is done in 
            // a transaction
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ProvisioningProcessSessionLocalHome provisioningHome = 
                    (ProvisioningProcessSessionLocalHome) EJBFactory.lookUpLocalHome(
                    ProvisioningProcessSessionLocalHome.class,
                    ProvisioningProcessSessionLocalHome.JNDI_NAME);
            ProvisioningProcessSessionLocal provisioning = 
                    provisioningHome.create();

            provisioning.externalProvisioning(message);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    public void ejbRemove() throws EJBException {
        LOG.debug("Removing MDB " + this.hashCode());
    }

    public void ejbCreate() {
        LOG.debug("Creating MDB " + this.hashCode());
    }

    public void setMessageDrivenContext(MessageDrivenContext context)
            throws EJBException {
    }
}
