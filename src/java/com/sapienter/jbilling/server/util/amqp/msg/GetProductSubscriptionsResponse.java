package com.sapienter.jbilling.server.util.amqp.msg;

import java.util.Collection;

import com.sapienter.jbilling.server.util.amqp.dto.ProductSubscriptionDTO;

public class GetProductSubscriptionsResponse extends ResponseBase {
	private Collection<ProductSubscriptionDTO> subscriptions;

	public Collection<ProductSubscriptionDTO> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Collection<ProductSubscriptionDTO> subscriptions) {
		this.subscriptions = subscriptions;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetProductSubscriptionsResponse [subscriptions=")
				.append(subscriptions).append(", getCorrelationId()=")
				.append(getCorrelationId()).append(", getIsSuccess()=")
				.append(getIsSuccess()).append(", getErrorMessage()=")
				.append(getErrorMessage()).append(", getExceptionClass()=")
				.append(getExceptionClass()).append("]");
		return builder.toString();
	}
}
