package com.sapienter.jbilling.server.util.amqp.msg;

import com.sapienter.jbilling.server.util.amqp.dto.UserDTO;

public class GetUserResponse extends ResponseBase {
	private UserDTO user;

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetUserResponse [user=").append(user)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append(", getIsSuccess()=").append(getIsSuccess())
				.append(", getErrorMessage()=").append(getErrorMessage())
				.append("]");
		return builder.toString();
	}
}
