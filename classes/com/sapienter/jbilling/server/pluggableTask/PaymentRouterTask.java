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
package com.sapienter.jbilling.server.pluggableTask;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.interfaces.PluggableTaskEntityLocal;
import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;
import com.sapienter.jbilling.server.user.ContactFieldDTOEx;

public class PaymentRouterTask extends PluggableTask implements PaymentTask {
	public static final String PARAM_CUSTOM_FIELD_PAYMENT_PROCESSOR = "custom_field_id";
	private static final Logger LOG = Logger.getLogger(PaymentRouterTask.class);

	public void failure(Integer userId, Integer retry) {
		// ignore, failure is already forced by broken delegate
	}

	public boolean process(PaymentDTOEx paymentInfo)
			throws PluggableTaskException {
	    LOG.debug("Routing for " + paymentInfo);
		PaymentTask delegate = selectDelegate(paymentInfo.getUserId());
		if (delegate == null) {
			// give them a chance
            LOG.error("ATTENTION! " +
                    "Could not find a process to delegate for user : " +
                    paymentInfo.getUserId());
			return false;
		}

		delegate.process(paymentInfo);

        LOG.debug("done");
		// they already used their chance
		return false;
	}
	
	public boolean preAuth(PaymentDTOEx paymentInfo) throws PluggableTaskException {
		PaymentTask delegate = selectDelegate(paymentInfo.getUserId());
        delegate.preAuth(paymentInfo);

        // they already used their chance
        return false;
	}
	
	public boolean confirmPreAuth(PaymentAuthorizationDTO auth, PaymentDTOEx paymentInfo)
			throws PluggableTaskException {
	
		PaymentTask delegate = selectDelegate(paymentInfo.getUserId());
		if (delegate == null){
			LOG.error("ATTENTION! " +
					"Delegate is recently changed for user : " + paymentInfo.getUserId() + " with not captured transaction: " +  auth.getTransactionId());
			return false;
		}
		delegate.confirmPreAuth(auth, paymentInfo);
        // they already used their chance
        return false;
	}

	private PaymentTask selectDelegate(Integer userId)
			throws PluggableTaskException {
		ContactBL contactLoader;
		try {
			contactLoader = new ContactBL();
			contactLoader.set(userId);
		} catch (NamingException e) {
			throw new PluggableTaskException("Configuration problems", e);
		} catch (FinderException e) {
			throw new PluggableTaskException("Valid userId expected, actual:"
					+ userId, e);
		}

		ContactDTOEx contact = contactLoader.getDTO();
		ContactFieldDTOEx paymentProcessorField = (ContactFieldDTOEx) contact.getFields().get(
                parameters.get(PARAM_CUSTOM_FIELD_PAYMENT_PROCESSOR));
		if (paymentProcessorField == null){
            LOG.warn("Can't find CCF with type " + 
                    parameters.get(PARAM_CUSTOM_FIELD_PAYMENT_PROCESSOR) +
                    " contact = " + contact);
			return null;
		}
		
		String processorName = paymentProcessorField.getContent();
		Integer selectedTaskId;
		try {
            // it is a task parameter the id of the processor
			selectedTaskId = intValueOf(parameters.get(processorName));
		} catch (NumberFormatException e) {
			throw new PluggableTaskException("Invalid payment task id :"
					+ processorName + " for userId: " + userId);
		}
		if (selectedTaskId == null) {
            LOG.warn("Could not find processor for " + parameters.get(processorName));
			return null;
		}

        LOG.debug("Delegating to task id " + selectedTaskId);
		PaymentTask selectedTask = instantiateTask(selectedTaskId);
		
		if (selectedTask instanceof PaymentRouterTask) {
			throw new PluggableTaskException(
					"Endless delegation cycle of PaymentRouterTask found for user: "
							+ userId);
		}
		return selectedTask;
	}

	private PaymentTask instantiateTask(Integer taskId)
			throws PluggableTaskException {
		PluggableTaskBL taskLoader;
		try {
			taskLoader = new PluggableTaskBL(taskId);
		} catch (FinderException e) {
			throw new PluggableTaskException("Task can not be found: id: "
					+ taskId, e);
		}

		PluggableTaskEntityLocal localTask = taskLoader.getEntity();
		String fqn = localTask.getType().getClassName();
		PaymentTask result;
		try {
			Class<? extends PaymentTask> taskClazz = Class.forName(fqn)
					.asSubclass(PaymentTask.class);
			result = taskClazz.newInstance();
		} catch (ClassCastException e) {
			throw new PluggableTaskException("Task id: " + taskId
					+ ": implementation class does not implements PaymentTask:"
					+ fqn, e);
		} catch (InstantiationException e) {
			throw new PluggableTaskException("Task id: " + taskId
					+ ": Can not instantiate : " + fqn, e);
		} catch (IllegalAccessException e) {
			throw new PluggableTaskException("Task id: " + taskId
					+ ": Can not find public constructor for : " + fqn, e);
		} catch (ClassNotFoundException e) {
			throw new PluggableTaskException("Task id: " + taskId
					+ ": Unknown class: " + fqn, e);
		}

		if (result instanceof PluggableTask) {
			PluggableTask pluggable = (PluggableTask) result;
			pluggable.initializeParamters(localTask);
		}
		return result;
	}
	
	private Integer intValueOf(Object object) {
		if (object instanceof Number) {
			return Integer.valueOf(((Number) object).intValue());
		}
		if (object instanceof String) {
			String parseMe = (String) object;
			return Integer.parseInt(parseMe);
		}
		return null;
	}
	
}
