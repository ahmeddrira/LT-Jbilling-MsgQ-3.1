package com.sapienter.jbilling.server.util.amqp.msg;

import com.sapienter.jbilling.server.util.amqp.dto.OrderDTO;

public class UpdateOrderResponse extends ResponseBase {
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
		builder.append("UpdateOrderResponse [order=").append(order)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append(", getIsSuccess()=").append(getIsSuccess())
				.append(", getErrorMessage()=").append(getErrorMessage())
				.append(", getExceptionClass()=").append(getExceptionClass())
				.append("]");
		return builder.toString();
	}
}
