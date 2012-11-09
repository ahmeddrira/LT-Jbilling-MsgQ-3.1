package com.sapienter.jbilling.server.util.amqp;

import com.sapienter.jbilling.server.user.ValidatePurchaseWS;

public class ValidateUserAndPurchaseResponse extends ResponseBase {
	private ValidatePurchaseWS validatePurchaseWS;

	public ValidatePurchaseWS getValidatePurchaseWS() {
		return validatePurchaseWS;
	}

	public void setValidatePurchaseWS(ValidatePurchaseWS validatePurchaseWS) {
		this.validatePurchaseWS = validatePurchaseWS;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValidateUserAndPurchaseResponse [validatePurchaseWS=")
				.append(validatePurchaseWS).append(", getCorrelationId()=")
				.append(getCorrelationId()).append(", getIsSuccess()=")
				.append(getIsSuccess()).append(", getErrorMessage()=")
				.append(getErrorMessage()).append(", getCause()=")
				.append(getCause()).append("]");
		return builder.toString();
	}
}
