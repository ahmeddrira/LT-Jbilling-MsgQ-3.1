package com.sapienter.jbilling.server.util.amqp;

import com.sapienter.jbilling.server.order.OrderWS;

public class GetOrderResponse extends ResponseBase {
	private OrderWS orderWS;

	public OrderWS getOrderWS() {
		return orderWS;
	}

	public void setOrderWS(OrderWS orderWS) {
		this.orderWS = orderWS;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetOrderResponse [orderWS=").append(orderWS)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append(", getIsSuccess()=").append(getIsSuccess())
				.append(", getErrorMessage()=").append(getErrorMessage())
				.append(", getExceptionClass()=").append(getExceptionClass())
				.append("]");
		return builder.toString();
	}
}
