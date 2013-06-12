package com.sapienter.jbilling.server.util.amqp.msg;

public class GetOrderByStringMetaDataRequest extends RequestBase {
	private String metaFieldName;
	private String metaFieldValue;

	public String getMetaFieldName() {
		return metaFieldName;
	}

	public void setMetaFieldName(String metaFieldName) {
		this.metaFieldName = metaFieldName;
	}

	public String getMetaFieldValue() {
		return metaFieldValue;
	}

	public void setMetaFieldValue(String metaFieldValue) {
		this.metaFieldValue = metaFieldValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetOrderByStringMetaDataRequest [metaFieldName=")
				.append(metaFieldName).append(", metaFieldValue=")
				.append(metaFieldValue).append(", getCorrelationId()=")
				.append(getCorrelationId()).append("]");
		return builder.toString();
	}
}
