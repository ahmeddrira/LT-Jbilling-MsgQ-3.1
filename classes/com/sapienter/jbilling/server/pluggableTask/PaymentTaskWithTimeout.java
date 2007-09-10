package com.sapienter.jbilling.server.pluggableTask;

import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;

public abstract class PaymentTaskWithTimeout extends PaymentTaskBase {
	public static final String PARAM_TIMEOUT_SECONDS = "timeout_sec";
	
	private int myTimeout;
	
	@Override
	public void initializeParamters(PluggableTaskDTO task)
			throws PluggableTaskException {
		
		super.initializeParamters(task);

		String timeoutText = ensureGetParameter(PARAM_TIMEOUT_SECONDS);
		try {
			myTimeout = Integer.parseInt(timeoutText);  
		} catch (NumberFormatException e){
			throw new PluggableTaskException("" // 
					+ "Integer expected for parameter: " + PARAM_TIMEOUT_SECONDS // 
					+ ", actual: " + timeoutText);
		}
	}
	
	protected int getTimeoutSeconds() {
		return myTimeout;
	}
}
