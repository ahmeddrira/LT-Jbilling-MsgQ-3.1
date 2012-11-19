package com.sapienter.jbilling.server.util.amqp;

public class MessageServiceConstants {

	public static String exchangeName = "jbilling";
	public static String requestQueueName = "jbilling.request";
	public static String requestRoutingKey = requestQueueName;
	public static String responseQueueHeader = "jbilling.response.queue";
	public static String responseExchangeHeader = "jbilling.response.exchange";
	public static String mappedHeaders = "jbilling.*";

	public static String getExchangeName() {
		return exchangeName;
	}

	public static String getRequestQueueName() {
		return requestQueueName;
	}

	public static String getRequestRoutingKey() {
		return requestRoutingKey;
	}

	public static String getResponseQueueHeader() {
		return responseQueueHeader;
	}

	public static String getResponseExchangeHeader() {
		return responseExchangeHeader;
	}

	public static String getMappedHeaders() {
		return mappedHeaders;
	}

}
