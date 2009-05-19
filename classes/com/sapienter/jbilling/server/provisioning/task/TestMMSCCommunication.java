/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.provisioning.task;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.sapienter.jbilling.server.pluggableTask.TaskException;

/**
 * Dummy MMSC communication class for testing MMSCProvisioningTask.
 */
public class TestMMSCCommunication implements IMMSCCommunication {
	private static final Logger LOG = Logger
			.getLogger(TestMMSCCommunication.class);

	public Map<String, String> addCustomer(String loginUser,
			String loginPassword, String portalId, String applicationId,
			String transactionId, String channeld, String referenceId,
			String tag, String userId, String msisdn, String subscriptionType,
			String bnet) throws TaskException {
		LOG.debug("Calling Dummy method addCustomer");
		Map<String, String> response = new HashMap<String, String>();
		// wait for command rules task transaction to complete
		//pause(2000);

		response.put(MMSCProvisioningTask.TRANSACTION_ID, transactionId);
		response.put(MMSCProvisioningTask.STATUS_CODE,
				MMSCProvisioningTask.STATUS_CODE_OK);
		response.put(MMSCProvisioningTask.STATUS_MESSAGE,
				"Customer Added Successfully");

		return response;
	}

	public Map<String, String> deleteCustomer(String loginUser,
			String loginPassword, String portalId, String applicationId,
			String transactionId, String channeld, String referenceId,
			String tag, String userId, String msisdn, String bnet)
			throws TaskException {
		LOG.debug("Calling Dummy method deleteCustomer");
		return addCustomer(loginUser, loginPassword, portalId, applicationId,
				transactionId, channeld, referenceId, tag, userId, msisdn,
				null, bnet);
	}

	public Map<String, String> modifyCustomer(String loginUser,
			String loginPassword, String portalId, String applicationId,
			String transactionId, String channeld, String referenceId,
			String tag, String userId, String msisdn, String bnet)
			throws TaskException {
		LOG.debug("Calling Dummy method modifyCustomer");
		return addCustomer(loginUser, loginPassword, portalId, applicationId,
				transactionId, channeld, referenceId, tag, userId, msisdn,
				null, bnet);
	}

	private void pause(long t) {
		LOG.debug("TestMMSCCommunication: pausing for " + t + " ms...");

		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
		}
	}

}
