package com.sapienter.jbilling.server.util.amqp;

public class RequestBase {
	private int correlationId;

	public int getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(int correlationId) {
		this.correlationId = correlationId;
	}
}
