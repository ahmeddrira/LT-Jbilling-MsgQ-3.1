package com.sapienter.jbilling.server.util.amqp.msg;

public class CreateOrderResponse extends ResponseBase {
	private Integer orderId;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateOrderResponse [orderId=").append(orderId)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append(", getIsSuccess()=").append(getIsSuccess())
				.append(", getErrorMessage()=").append(getErrorMessage())
				.append(", getExceptionClass()=").append(getExceptionClass())
				.append("]");
		return builder.toString();
	}
}
