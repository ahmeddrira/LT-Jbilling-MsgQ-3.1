package com.sapienter.jbilling.server.util.amqp;

import com.sapienter.jbilling.server.util.amqp.CreateOrderRequest;
import com.sapienter.jbilling.server.util.amqp.CreateOrderResponse;
import com.sapienter.jbilling.server.util.amqp.DeleteOrderRequest;
import com.sapienter.jbilling.server.util.amqp.DeleteOrderResponse;
import com.sapienter.jbilling.server.util.amqp.GetOrderByStringMetaDataRequest;
import com.sapienter.jbilling.server.util.amqp.GetOrderByStringMetaDataResponse;
import com.sapienter.jbilling.server.util.amqp.GetOrderRequest;
import com.sapienter.jbilling.server.util.amqp.GetOrderResponse;
import com.sapienter.jbilling.server.util.amqp.GetUserRequest;
import com.sapienter.jbilling.server.util.amqp.GetUserResponse;
import com.sapienter.jbilling.server.util.amqp.ValidateUserAndPurchaseRequest;
import com.sapienter.jbilling.server.util.amqp.ValidateUserAndPurchaseResponse;

/**
 * Interface defines the available API operations. Note this is for clients wanting to use
 * synchronous messaging.
 *  
 * @author smit005
 *
 */
public interface JBillingMessageQueueAPI {
	CreateOrderResponse createOrder(CreateOrderRequest request);
	DeleteOrderResponse deleteOrder(DeleteOrderRequest request);
	GetOrderResponse getOrder(GetOrderRequest request);
	GetOrderByStringMetaDataResponse getOrderByStringMetaData(GetOrderByStringMetaDataRequest request);
	GetUserResponse getUser(GetUserRequest request);
	ValidateUserAndPurchaseResponse validateUserAndPurchase(ValidateUserAndPurchaseRequest request);
}
