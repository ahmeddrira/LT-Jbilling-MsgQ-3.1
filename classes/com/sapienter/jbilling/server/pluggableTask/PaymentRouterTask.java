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
package com.sapienter.jbilling.server.pluggableTask;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskBL;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;
import com.sapienter.jbilling.server.user.contact.db.ContactFieldDTO;

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
		
		String processorName = getProcessorName(userId);
        if (processorName == null) {
            return null;
        }
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
    
    public String getProcessorName(Integer userId) throws PluggableTaskException {
        ContactBL contactLoader;
        String processorName = null;
        contactLoader = new ContactBL();
        contactLoader.set(userId);

        ContactDTOEx contact = contactLoader.getDTO();
        ContactFieldDTO paymentProcessorField = (ContactFieldDTO) contact.getFieldsTable().get(
                parameters.get(PARAM_CUSTOM_FIELD_PAYMENT_PROCESSOR));
        if (paymentProcessorField == null){
            LOG.warn("Can't find CCF with type " + 
                    parameters.get(PARAM_CUSTOM_FIELD_PAYMENT_PROCESSOR) +
                    " contact = " + contact);
            processorName = null;
        } else {
            processorName = paymentProcessorField.getContent();
        }
        return processorName;
    }

	private PaymentTask instantiateTask(Integer taskId)
			throws PluggableTaskException {
		PluggableTaskBL<PaymentTask> taskLoader;
		try {
			taskLoader = new PluggableTaskBL<PaymentTask>(taskId);
            return taskLoader.instantiateTask();
		} catch (FinderException e) {
			throw new PluggableTaskException("Task can not be found: id: "
					+ taskId, e);
		}

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
