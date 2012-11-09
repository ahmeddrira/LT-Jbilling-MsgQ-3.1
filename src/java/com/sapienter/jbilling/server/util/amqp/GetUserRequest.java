package com.sapienter.jbilling.server.util.amqp;

public class GetUserRequest extends RequestBase {
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		
		this.userId = userId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetUserRequest [userId=").append(userId)
				.append(", getCorrelationId=").append(getCorrelationId()).append("]");
		return builder.toString();
	}
}
