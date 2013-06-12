package com.sapienter.jbilling.server.util.amqp;

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
import com.sapienter.jbilling.server.util.amqp.msg.UpdateOrderRequest;
import com.sapienter.jbilling.server.util.amqp.msg.UpdateOrderResponse;
import com.sapienter.jbilling.server.util.amqp.msg.ValidateUserAndPurchaseRequest;
import com.sapienter.jbilling.server.util.amqp.msg.ValidateUserAndPurchaseResponse;

/**
 * Interface defines the available API operations. Note this is for clients wanting to use
 * synchronous messaging.
 *  
 * @author smit005
 *
 */
public interface JBillingMessageQueueAPI {
	CreateOrderResponse createOrder(CreateOrderRequest request);
	UpdateOrderResponse updateOrder(UpdateOrderRequest request);
	DeleteOrderResponse deleteOrder(DeleteOrderRequest request);
	GetOrderResponse getOrder(GetOrderRequest request);
	GetOrderByStringMetaDataResponse getOrderByStringMetaData(GetOrderByStringMetaDataRequest request);
	GetUserResponse getUser(GetUserRequest request);
	ValidateUserAndPurchaseResponse validateUserAndPurchase(ValidateUserAndPurchaseRequest request);
	GetProductSubscriptionsResponse getSubscriptions(GetProductSubscriptionsRequest request);
}
