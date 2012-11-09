package com.sapienter.jbilling.server.util.amqp;


public class ResponseBase {
	private int correlationId;
	private Boolean isSuccess;
	private String errorMessage;
	private Exception cause;
	
	public int getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(int correlationId) {
		this.correlationId = correlationId;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public void setCause(Exception sie) {
		cause = sie;
	}	
	
	public Exception getCause() {
		return cause;
	}
}
