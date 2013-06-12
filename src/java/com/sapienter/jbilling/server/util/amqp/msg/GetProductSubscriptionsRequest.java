package com.sapienter.jbilling.server.util.amqp.msg;

public class GetProductSubscriptionsRequest extends RequestBase {
	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetProductSubscriptionsRequest [userId=")
				.append(userId).append(", getCorrelationId()=")
				.append(getCorrelationId()).append("]");
		return builder.toString();
	}
}
