package com.sapienter.jbilling.server.util.amqp.msg;

import com.sapienter.jbilling.server.user.UserWS;

public class GetUserResponse extends ResponseBase {
	private UserWS userWS;

	public UserWS getUserWS() {
		return userWS;
	}

	public void setUserWS(UserWS userWS) {
		this.userWS = userWS;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetUserResponse [userWS=").append(userWS)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append(", getIsSuccess()=").append(getIsSuccess())
				.append(", getErrorMessage()=").append(getErrorMessage())
				.append("]");
		return builder.toString();
	}
}
