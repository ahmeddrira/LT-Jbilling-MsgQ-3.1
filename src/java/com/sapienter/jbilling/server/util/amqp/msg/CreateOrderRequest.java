package com.sapienter.jbilling.server.util.amqp.msg;

import com.sapienter.jbilling.server.util.amqp.dto.OrderDTO;

public class CreateOrderRequest extends RequestBase {
	private OrderDTO order;

	public OrderDTO getOrder() {
		return order;
	}

	public void setOrder(OrderDTO order) {
		this.order = order;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateOrderRequest [order=").append(order)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append("]");
		return builder.toString();
	}
}
