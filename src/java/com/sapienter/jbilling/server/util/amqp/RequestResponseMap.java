package com.sapienter.jbilling.server.util.amqp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

public class RequestResponseMap {

	private static final Logger LOGGER = Logger
			.getLogger(RequestResponseMap.class);

	private final Map<Class<? extends RequestBase>, Class<? extends ResponseBase>> requestResponseMap;

	public RequestResponseMap() {
		requestResponseMap = new HashMap<Class<? extends RequestBase>, Class<? extends ResponseBase>>();
		requestResponseMap.put(CreateOrderRequest.class,
				CreateOrderResponse.class);
		requestResponseMap.put(GetOrderRequest.class, GetOrderResponse.class);
		requestResponseMap.put(GetOrderByStringMetaDataRequest.class,
				GetOrderByStringMetaDataResponse.class);
		requestResponseMap.put(UpdateOrderRequest.class,
				UpdateOrderResponse.class);
		requestResponseMap.put(DeleteOrderRequest.class,
				DeleteOrderResponse.class);
		requestResponseMap.put(GetUserRequest.class, GetUserResponse.class);
		requestResponseMap.put(ValidateUserAndPurchaseRequest.class,
				ValidateUserAndPurchaseResponse.class);
	}
	
	public Map<Class<? extends RequestBase>, Class<? extends ResponseBase>> getMap() {
		return requestResponseMap;
	}

	@SuppressWarnings("unchecked")
	public <T extends ResponseBase> T makeResponse(RequestBase request) {
		Assert.notNull(requestResponseMap,
				"requestResponseMap not initialised!");
		Class<? extends ResponseBase> responseClass = requestResponseMap
				.get(request.getClass());
		if (responseClass == null) {
			LOGGER.warn("Unknown request class " + request.getClass());
			return null;
		}

		T response = null;
		try {
			response = (T) responseClass.newInstance();
			response.setCorrelationId(request.getCorrelationId());
			return response;
		} catch (InstantiationException e) {
			LOGGER.warn("Cannot instantiate response of type "
					+ responseClass.getName());
		} catch (IllegalAccessException e) {
			LOGGER.warn("Cannot instantiate response of type "
					+ responseClass.getName());
		}

		// Failed to create response
		return null;
	}
}
