package com.sapienter.jbilling.server.util.amqp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.sapienter.jbilling.server.util.amqp.msg.CreateOrderRequest;
import com.sapienter.jbilling.server.util.amqp.msg.CreateOrderResponse;
import com.sapienter.jbilling.server.util.amqp.msg.DeleteOrderRequest;
import com.sapienter.jbilling.server.util.amqp.msg.DeleteOrderResponse;
import com.sapienter.jbilling.server.util.amqp.msg.GetOrderByStringMetaDataRequest;
import com.sapienter.jbilling.server.util.amqp.msg.GetOrderByStringMetaDataResponse;
import com.sapienter.jbilling.server.util.amqp.msg.GetOrderRequest;
import com.sapienter.jbilling.server.util.amqp.msg.GetOrderResponse;
import com.sapienter.jbilling.server.util.amqp.msg.GetProductSubscriptionsRequest;
import com.sapienter.jbilling.server.util.amqp.msg.GetProductSubscriptionsResponse;
import com.sapienter.jbilling.server.util.amqp.msg.GetUserRequest;
import com.sapienter.jbilling.server.util.amqp.msg.GetUserResponse;
import com.sapienter.jbilling.server.util.amqp.msg.RequestBase;
import com.sapienter.jbilling.server.util.amqp.msg.ResponseBase;
import com.sapienter.jbilling.server.util.amqp.msg.UpdateOrderRequest;
import com.sapienter.jbilling.server.util.amqp.msg.UpdateOrderResponse;
import com.sapienter.jbilling.server.util.amqp.msg.ValidateUserAndPurchaseRequest;
import com.sapienter.jbilling.server.util.amqp.msg.ValidateUserAndPurchaseResponse;

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
		requestResponseMap.put(GetProductSubscriptionsRequest.class,
				GetProductSubscriptionsResponse.class);
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
