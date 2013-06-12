package com.sapienter.jbilling.server.util.amqp.msg;

import com.sapienter.jbilling.server.order.OrderWS;

public class UpdateOrderRequest extends RequestBase {
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
		builder.append("UpdateOrderRequest [orderWS=").append(orderWS)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append(", ")
				.append(super.toString()).append("]");
		return builder.toString();
	}
}
