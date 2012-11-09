package com.sapienter.jbilling.server.util.amqp;

import com.sapienter.jbilling.server.metafields.MetaFieldValueWS;

public class GetOrderByStringMetaDataRequest extends RequestBase {
	private MetaFieldValueWS metaFieldValueWS;

	public MetaFieldValueWS getMetaFieldValueWS() {
		return metaFieldValueWS;
	}

	public void setMetaFieldValueWS(MetaFieldValueWS metaFieldValueWS) {
		this.metaFieldValueWS = metaFieldValueWS;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GetOrderByStringMetaDataRequest [metaFieldValueWS=")
				.append(metaFieldValueWS).append(", getCorrelationId()=")
				.append(getCorrelationId()).append("]");
		return builder.toString();
	}

}
