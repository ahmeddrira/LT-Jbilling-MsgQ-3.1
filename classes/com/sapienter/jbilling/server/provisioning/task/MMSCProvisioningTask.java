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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.util.Context;

/**
 * @author othman
 * 
 *         MMSC external provisioning plug-in. Contains logic for communicating
 *         to MMSC webservice. Actual delivery of messages is left for
 *         implementations of IMMSCCommunication. The configuration file
 *         jbilling-provisioning.xml is used for selecting the mmsc
 *         IMMSCCommunication class.
 */
public class MMSCProvisioningTask extends PluggableTask implements
		IExternalProvisioning {
	public static final String PARAM_ID = "id";
	public static final String PARAM_ID_DEFAULT = "mmsc";
	// MMSCProvisioningTask plugin parameters
		public static final String PARAM_LOGIN_USER = "loginUser";
	public static final String PARAM_LOGIN_PASSWORD = "loginPassword";
	public static final String PARAM_PORTAL_ID = "portalId";
	public static final String PARAM_APPLICATION_ID = "applicationId";
	public static final String PARAM_BNET = "bnet";
	// other MMSC service interface parameters
	public static final String TRANSACTION_ID = "transactionId";
	public static final String CHANNEL_ID = "channeld";
	public static final String REFERENCE_ID = "referenceId";
	public static final String TAG = "tag";
	public static final String USER_ID = "userId";
	public static final String MSISDN = "msisdn";
	public static final String SUBSCRIPTION_TYPE = "subscriptionType";
	
	public static final String METHOD_NAME = "methodName";
	// MMSC service methods names. These names should match the methods names in
	// IMMSCCommunication class
	public static final String ADD_CUSTOMER = "addCustomer";
	public static final String MODIFY_CUSTOMER = "modifyCustomer";
	public static final String DELETE_CUSTOMER = "deleteCustomer";
	// MMSC Service-level error codes
	public static final String STATUS_CODE = "statusCode";
	public static final String STATUS_MESSAGE = "statusMessage";
	public static final String STATUS_CODE_OK = "0";
	public static final String STATUS_CODE_SERVICE_ERROR = "-1";
	public static final String STATUS_CODE_MSISDN_ERROR = "1";
	public static final String STATUS_CODE_SUBSCRIPTION_ERROR = "2";
	public static final String STATUS_CODE_BNET_ERROR = "3";

	private static final Logger LOG = Logger.getLogger(MMSCProvisioningTask.class);

	/**
	 * Sends command to MMSC system. Returns response.
	 */
	
	public Map<String, Object> sendRequest(String id, String command)
			throws TaskException {
		
		
		
		// send command and return results
		return parseResponse(sendCommand(command, id));
	}
	
	
	/**
	 * Method call MMSC webservice method.
	 *
	 * @param command
	 * @param id
	 * @return
	 * @throws TaskException
	 */
	private Map<String, String> sendCommand(String command, String id)
			throws TaskException {
		IMMSCCommunication mmsc = (IMMSCCommunication) Context
				.getBean(Context.Name.MMSC);
		Map<String, String> response = null;
		Map<String, String> params = getParameters(command, id);
		if (params == null || params.isEmpty())
			throw new TaskException("NULL or Empty Parameters List!");
		String methodName = params.get(METHOD_NAME);
		if (methodName == null)
			throw new TaskException("Expected Method Name!");

		String loginUser = (String) params.get(PARAM_LOGIN_USER);

		String loginPassword = (String) params.get(PARAM_LOGIN_PASSWORD);

		String portalId = (String) params.get(PARAM_PORTAL_ID);

		String applicationId = (String) params.get(PARAM_APPLICATION_ID);
		String transactionId = (String) params.get(TRANSACTION_ID);
		String channeld = (String) params.get(CHANNEL_ID);
		String referenceId = (String) params.get(REFERENCE_ID);
		String tag = (String) params.get(TAG);
		String userId = (String) params.get(USER_ID);

		String msisdn = (String) params.get(MSISDN);
		if (msisdn == null) {
			throw new TaskException("parameter '" + MSISDN + "' is Mandatory ");
		}
		String subscriptionType = (String) params.get(SUBSCRIPTION_TYPE);
		String bnet = (String) params.get(PARAM_BNET);

		if (methodName.equals(ADD_CUSTOMER)) {
			if (subscriptionType == null) {
				throw new TaskException("parameter '" + SUBSCRIPTION_TYPE
						+ "' is Mandatory ");
			}
			response = mmsc.addCustomer(loginUser, loginPassword, portalId,
					applicationId, transactionId, channeld, referenceId, tag,
					userId, msisdn, subscriptionType, bnet);
		} else if (methodName.equals(MODIFY_CUSTOMER)) {

			response = mmsc.modifyCustomer(loginUser, loginPassword, portalId,
					applicationId, transactionId, channeld, referenceId, tag,
					userId, msisdn, bnet);
		} else if (methodName.equals(DELETE_CUSTOMER)) {

			response = mmsc.deleteCustomer(loginUser, loginPassword, portalId,
					applicationId, transactionId, channeld, referenceId, tag,
					userId, msisdn, bnet);
		} else {
			throw new TaskException("webservice method '" + methodName
					+ "' is Not Found! ");
		}

		return response;
	}

	/**
	 * method to parse MMSC command
	 * @param command
	 * @return
	 * @throws TaskException
	 */
	private Map<String, String> parseCommand(String command)
			throws TaskException {

		// sample command pattern to parse:
		// "addCustomer:msisdn,46701055555:subscriptionType,HK;"

		LOG.debug("parsing command string pattern: " + command);
		Map<String, String> params = new LinkedHashMap<String, String>();
		StringTokenizer st = new StringTokenizer(command, ":;");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			String[] entry = token.split(",");
			if (entry.length > 2)
				throw new TaskException(
						"Error parsing command: Expected two Tokens but found too many tokens: "
								+ token);
			else if (entry.length == 1) {
				// found method name
				params.put(METHOD_NAME, entry[0]);
			} else {
				params.put(entry[0], entry[1]);
			}
		}

		return params;

	}

	/**
	 * returns Parametres Map extracted from MMSC command String
	 * 
	 * @param command
	 * @param id
	 * @return
	 * @throws TaskException
	 */
	private Map<String, String> getParameters(String command, String id)
			throws TaskException {

		Map<String, String> params = new LinkedHashMap<String, String>();

		// collect plugin parameters
		String username = (String) parameters.get(PARAM_LOGIN_USER);
		if (username == null) {
			throw new TaskException("No '" + PARAM_LOGIN_USER + "' plug-in "
					+ "parameter found.");
		}
		params.put(PARAM_LOGIN_USER, username);

		String password = (String) parameters.get(PARAM_LOGIN_PASSWORD);
		if (password == null) {
			throw new TaskException("No '" + PARAM_LOGIN_PASSWORD
					+ "' plug-in " + "parameter found.");
		}
		params.put(PARAM_LOGIN_PASSWORD, password);

		String portalId = (String) parameters.get(PARAM_PORTAL_ID);
		if (portalId == null) {
			throw new TaskException("No '" + PARAM_PORTAL_ID + "' plug-in "
					+ "parameter found.");
		}
		params.put(PARAM_PORTAL_ID, portalId);

		String applicationId = (String) parameters.get(PARAM_APPLICATION_ID);
		if (applicationId == null) {
			throw new TaskException("No '" + PARAM_APPLICATION_ID
					+ "' plug-in " + "parameter found.");
		}
		params.put(PARAM_APPLICATION_ID, applicationId);

		String bnet = (String) parameters.get(PARAM_BNET);
		if (bnet == null) {
			throw new TaskException("No '" + PARAM_BNET + "' plug-in "
					+ "parameter found.");
		}

		params.put(TRANSACTION_ID, id);

		Map<String, String> parsedCommand = parseCommand(command);

		adjustParameter(params, parsedCommand, CHANNEL_ID);
		adjustParameter(params, parsedCommand, REFERENCE_ID);
		adjustParameter(params, parsedCommand, TAG);
		adjustParameter(params, parsedCommand, USER_ID);
		// append parsed command key/value pairs
		params.putAll(parsedCommand);
		// last add bnet parameter
		params.put(PARAM_BNET, bnet);

		return params;

	}

	/**
	 * adds key to Map if not exists.then removes key from parsed command Map.
	 * 
	 * @param params
	 * @param parsedCommand
	 * @param key
	 */
	private void adjustParameter(Map<String, String> params,
			Map<String, String> parsedCommand, String key) {
		if (!parsedCommand.containsKey(key))
			params.put(key, null);
		else {
			String val = parsedCommand.get(key);
			params.put(key, val);
			parsedCommand.remove(key);
		}
	}

	

	/**
	 * Method to parse the response from the MMSC system into a Map. If
	 * 'statusCode' is '0', 'result' is 'success', otherwise 'fail'.
	 *
	 * @param response
	 * @return
	 * @throws TaskException
	 */
	private Map<String, Object> parseResponse(Map<String, String> response)
			throws TaskException {

		Map<String, Object> results = new HashMap<String, Object>();

		String value = response.get(TRANSACTION_ID);
		if (value == null) {
			throw new TaskException("Expected '" + TRANSACTION_ID
					+ "' in response");
		}
		// set TRANSACTION_ID value
		results.put(TRANSACTION_ID, value);

		value = response.get(STATUS_CODE);
		if (value == null) {
			throw new TaskException("Expected '" + STATUS_CODE
					+ "' in response");
		}
		// set STATUS_CODE value
		results.put(STATUS_CODE, value);

		// set result value
		if (value.equals(STATUS_CODE_OK)) {
			results.put("result", "success");
		} else {
			results.put("result", "fail");
		}

		value = response.get(STATUS_MESSAGE);
		if (value == null) {
			throw new TaskException("Expected '" + STATUS_MESSAGE
					+ "' in response");
		}
		results.put(STATUS_MESSAGE, value);

		return results;
	}

	/**
	 * Returns the id of the task.
	 */
	public String getId() {
		String id = (String) parameters.get(PARAM_ID);
		if (id != null) {
			return id;
		}
		return PARAM_ID_DEFAULT;
	}

	/**
	 * For allowing unit testing outside jBilling.
	 */
	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}


}
