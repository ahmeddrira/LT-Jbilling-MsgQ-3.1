package com.sapienter.jbilling.server.util.amqp.msg;

import com.sapienter.jbilling.server.order.OrderWS;

public class CreateOrderRequest extends RequestBase {
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
		builder.append("CreateOrderRequest [orderWS=").append(orderWS)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append("]");
		return builder.toString();
	}
}
