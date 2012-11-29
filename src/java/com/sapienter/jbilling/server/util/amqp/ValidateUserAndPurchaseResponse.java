package com.sapienter.jbilling.server.util.amqp;

import com.sapienter.jbilling.server.user.ValidateUserAndPurchaseWS;

public class ValidateUserAndPurchaseResponse extends ResponseBase {
	private ValidateUserAndPurchaseWS validateUserAndPurchaseWS;

	public ValidateUserAndPurchaseWS getValidateUserAndPurchaseWS() {
		return validateUserAndPurchaseWS;
	}

	public void setValidateUserAndPurchaseWS(ValidateUserAndPurchaseWS validateUserAndPurchaseWS) {
		this.validateUserAndPurchaseWS = validateUserAndPurchaseWS;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(
				"ValidateUserAndPurchaseResponse [validateUserAndPurchaseWS=")
				.append(validateUserAndPurchaseWS)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append(", getIsSuccess()=").append(getIsSuccess())
				.append(", getErrorMessage()=").append(getErrorMessage())
				.append(", getExceptionClass()=").append(getExceptionClass())
				.append("]");
		return builder.toString();
	}
}
