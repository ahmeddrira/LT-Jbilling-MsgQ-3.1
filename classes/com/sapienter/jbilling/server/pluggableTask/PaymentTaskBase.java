package com.sapienter.jbilling.server.pluggableTask;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationBL;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;

public abstract class PaymentTaskBase extends PluggableTask implements PaymentTask {
	
	protected final String ensureGetParameter(String key) throws PluggableTaskException {
		Object value = parameters.get(key);
		if (false == value instanceof String) {
			throw new PluggableTaskException("Missed or wrong parameter for: "
					+ key + ", string expected: " + value);
		}

		return (String) value;
	}

	protected final String getOptionalParameter(String key, String valueIfNull) {
		Object value = parameters.get(key);
		return (value instanceof String) ? (String)value : valueIfNull;
	}
	
	protected final boolean getBooleanParameter(String key){
		return Boolean.parseBoolean(getOptionalParameter(key, "false"));
	}

	protected final void storeProcessedAuthorization(PaymentDTOEx paymentInfo,
			PaymentAuthorizationDTO auth) throws PluggableTaskException {
		
		try {
			new PaymentAuthorizationBL().create(auth, paymentInfo.getId());
            paymentInfo.setAuthorization(auth);
		} catch (CreateException e){
			throw new PluggableTaskException("Can't create auth entity", e);
		} catch (NamingException e){
			throw new PluggableTaskException("Configuration problems", e);
		}
	}
	
	/**
	 * Usefull for processors that want to use the same template method for
	 * process() and preauth() methods
	 */
	protected static final class Result {
		private final boolean myCallOtherProcessors;
		private final PaymentAuthorizationDTO myAuthorizationData;
		
		public Result(PaymentAuthorizationDTO data, boolean shouldCallOthers){
			myAuthorizationData = data;
			myCallOtherProcessors = shouldCallOthers;
		}
		
		public PaymentAuthorizationDTO getAuthorizationData() {
			return myAuthorizationData;
		}
		
		public boolean shouldCallOtherProcessors() {
			return myCallOtherProcessors;
		}
        
        public String toString() {
            return "Result: myCallOtherProcessors " + myCallOtherProcessors + 
                " data " + myAuthorizationData;
        }
	}
	
	protected static final Result NOT_APPLICABLE = new Result(null, true);

}
