package com.sapienter.jbilling.server.util.amqp;

public class GetOrderRequest extends RequestBase {
	private int orderId;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetOrderRequest [orderId=").append(orderId)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append("]");
		return builder.toString();
	}
}
