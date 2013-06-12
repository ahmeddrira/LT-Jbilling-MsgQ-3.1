package com.sapienter.jbilling.server.util.amqp.msg;

public class ValidateUserAndPurchaseRequest extends RequestBase {
	private int userId;
	private int itemId;
	private String fields;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValidateUserAndPurchaseRequest [userId=")
				.append(userId).append(", itemId=").append(itemId)
				.append(", fields=").append(fields)
				.append(", getCorrelationId()=").append(getCorrelationId())
				.append("]");
		return builder.toString();
	}

}
