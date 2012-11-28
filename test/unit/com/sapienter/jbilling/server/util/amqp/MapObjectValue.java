package com.sapienter.jbilling.server.util.amqp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

public class MapObjectValue {
	private Object value;
	private int intValue;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
}
