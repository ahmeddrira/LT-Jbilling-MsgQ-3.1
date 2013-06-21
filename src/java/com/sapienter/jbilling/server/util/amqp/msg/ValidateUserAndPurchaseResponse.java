package com.sapienter.jbilling.server.util.amqp.msg;

import java.math.BigDecimal;

import com.sapienter.jbilling.server.util.amqp.types.AccountStatusType;

public class ValidateUserAndPurchaseResponse extends ResponseBase {
	private AccountStatusType accountStatus;
	private BigDecimal dynamicBalance;
	private boolean isValidated;
	private String[] validationMessages;
	private boolean isAuthorised;
	private BigDecimal quantity;

	public AccountStatusType getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatusType accountStatus) {
		this.accountStatus = accountStatus;
	}

	public BigDecimal getDynamicBalance() {
		return dynamicBalance;
	}

	public void setDynamicBalance(BigDecimal dynamicBalance) {
		this.dynamicBalance = dynamicBalance;
	}

	public boolean isValidated() {
		return isValidated;
	}

	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	public String[] getValidationMessages() {
		return validationMessages;
	}

	public void setValidationMessages(String[] validationMessages) {
		this.validationMessages = validationMessages;
	}

	public boolean isAuthorised() {
		return isAuthorised;
	}

	public void setAuthorised(boolean isAuthorised) {
		this.isAuthorised = isAuthorised;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValidateUserAndPurchaseResponse [accountStatus=")
				.append(accountStatus).append(", dynamicBalance=")
				.append(dynamicBalance).append(", isValidated=")
				.append(isValidated).append(", validationMessages=")
				.append(validationMessages).append(", isAuthorised=")
				.append(isAuthorised).append(", quantity=").append(quantity)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append(", getIsSuccess()=").append(getIsSuccess())
				.append(", getErrorMessage()=").append(getErrorMessage())
				.append(", getExceptionClass()=").append(getExceptionClass())
				.append("]");
		return builder.toString();
	}
}
