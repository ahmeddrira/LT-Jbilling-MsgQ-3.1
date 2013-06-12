package com.sapienter.jbilling.server.util.amqp;


import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.ValidatePurchaseWS;
import com.sapienter.jbilling.server.user.ValidateUserAndPurchaseWS;
import com.sapienter.jbilling.server.util.amqp.dto.ProductSubscriptionDTO;
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

@Transactional(propagation = Propagation.REQUIRED)
public class MessageServiceAPI {
	private static final Logger LOG = Logger.getLogger(MessageServiceAPI.class);
	
	private MessageServiceBL messageServiceBL;
	private RequestResponseMap requestResponseMap;
	
	public void setMessageServiceBL(MessageServiceBL messageServiceBL) {
		this.messageServiceBL = messageServiceBL;
	}

	public void setRequestResponseMap(RequestResponseMap requestResponseMap) {
		this.requestResponseMap = requestResponseMap;
	}

	public ValidateUserAndPurchaseResponse process(
			ValidateUserAndPurchaseRequest request) {
		LOG.info("Process request " + request);

		ValidateUserAndPurchaseResponse response = requestResponseMap.makeResponse(request);

		UserBL userBL = new UserBL(request.getUserId());

		ValidatePurchaseWS validatePurchaseWS = messageServiceBL.doValidatePurchase(userBL,
				new Integer[] { request.getItemId() },
				new String[] { request.getFields() });
		if (validatePurchaseWS == null) {
			response.setIsSuccess(false);
			response.setErrorMessage("Failed to validate purchase");
		} else {
			ValidateUserAndPurchaseWS validateUserAndPurchaseWS = new ValidateUserAndPurchaseWS();
			validateUserAndPurchaseWS.setAccountStatus(userBL.getDto()
					.getStatus().getId());
			validateUserAndPurchaseWS.setDynamicBalance(userBL.getDto()
					.getCustomer().getDynamicBalance());
			validateUserAndPurchaseWS.setSuccess(validatePurchaseWS
					.getSuccess());
			validateUserAndPurchaseWS.setMessage(validatePurchaseWS
					.getMessage());
			validateUserAndPurchaseWS.setAuthorized(validatePurchaseWS
					.getAuthorized());
			validateUserAndPurchaseWS.setQuantity(validatePurchaseWS
					.getQuantity());

			response.setValidateUserAndPurchaseWS(validateUserAndPurchaseWS);
			response.setIsSuccess(true);
		}

		LOG.info("Request response " + response);
		return response;
	}

	private OrderWS getOrder(int orderId) {
		// get the info from the caller
		Integer languageId = messageServiceBL.getCallerLanguageId();

		// now get the order. Avoid the proxy since this is for the client
		OrderDAS das = new OrderDAS();
		OrderDTO order = das.findNow(orderId);
		if (order != null) { // not found
			OrderBL bl = new OrderBL(order);
			if (order.getDeleted() == 1) {
				LOG.debug("Returning deleted order " + orderId);
			}
			return bl.getWS(languageId);
		}

		return null;
	}

	public GetOrderResponse process(GetOrderRequest request) {
		LOG.info("Process request " + request);

		GetOrderResponse response = requestResponseMap.makeResponse(request);

		OrderWS orderWS = getOrder(request.getOrderId());
		if (orderWS == null) {
			response.setIsSuccess(false);
			response.setErrorMessage("Cannot find order for orderId="
					+ request.getOrderId());
		} else {
			response.setOrderWS(orderWS);
			response.setIsSuccess(true);
		}

		LOG.info("Request response " + response);
		return response;
	}

	public GetOrderByStringMetaDataResponse process(
			GetOrderByStringMetaDataRequest request) {
		LOG.info("Process request " + request);

		GetOrderByStringMetaDataResponse response = requestResponseMap.makeResponse(request);

		OrderBL bl = new OrderBL();
		// get the order
		Integer id = bl.getIdByStringMetaData(request.getMetaFieldValueWS());
		if (id == null) {
			response.setIsSuccess(false);
			response.setErrorMessage("Cannot find order for metaFieldValue="
					+ request.getMetaFieldValueWS());
		} else {
			OrderWS orderWS = getOrder(id);
			if (orderWS == null) {
				response.setIsSuccess(false);
				response.setErrorMessage("Cannot find order for orderId=" + id);
			} else {
				response.setOrderWS(orderWS);
				response.setIsSuccess(true);
			}
		}

		LOG.info("Request response " + response);
		return response;
	}

	public DeleteOrderResponse process(DeleteOrderRequest request) {
		LOG.info("Process request " + request);

		DeleteOrderResponse response = requestResponseMap.makeResponse(request);

		// now get the order
		OrderBL bl = new OrderBL();
		bl.setForUpdate(request.getOrderId());
		bl.delete(messageServiceBL.getCallerId());

		response.setIsSuccess(true);

		LOG.info("Request response " + response);
		return response;
	}

	public CreateOrderResponse process(CreateOrderRequest request) {
		LOG.info("Process request " + request);

		CreateOrderResponse response = requestResponseMap.makeResponse(request);

		OrderWS orderWS = messageServiceBL.doCreateOrder(request.getOrderWS(), true);
		if (orderWS == null) {
			response.setIsSuccess(false);
			response.setErrorMessage("Failed to created order for request "
					+ request);
		} else {
			response.setIsSuccess(true);
			response.setOrderId(orderWS.getId());
		}

		LOG.info("Request response " + response);
		return response;
	}

	public GetUserResponse process(GetUserRequest request) {
		LOG.info("Process request " + request);

		GetUserResponse response = requestResponseMap.makeResponse(request);

		UserBL bl = new UserBL(request.getUserId());
		response.setUserWS(bl.getUserWS());
		response.setIsSuccess(true);

		LOG.info("Request response " + response);
		return response;
	}

	public UpdateOrderResponse process(UpdateOrderRequest request) {
		LOG.info("Process request " + request);

		UpdateOrderResponse response = requestResponseMap.makeResponse(request);

		messageServiceBL.doUpdateOrder(request.getOrderWS());

		response.setIsSuccess(true);

		LOG.info("Request response " + response);
		return response;
	}
	
	public GetProductSubscriptionsResponse process(GetProductSubscriptionsRequest request) {
		LOG.info("Process request " + request);

		GetProductSubscriptionsResponse response = requestResponseMap.makeResponse(request);

		Collection<ProductSubscriptionDTO> subscriptions = new ArrayList<ProductSubscriptionDTO>();
		for (ItemDTOEx item : messageServiceBL.getSubscribedItems(request.getUserId())) {
			ProductSubscriptionDTO prodSub = new ProductSubscriptionDTO();
			prodSub.setItemId(item.getId());
			prodSub.setCurrencyId(item.getCurrencyId());
			prodSub.setDeleted(item.getDeleted());
			prodSub.setDescription(item.getDescription());
			prodSub.setEntityId(item.getEntityId());
			prodSub.setGlCode(item.getGlCode());
			prodSub.setNumber(item.getNumber());
			prodSub.setPromoCode(item.getPromoCode());
			
			subscriptions.add(prodSub);
		}
		
		response.setSubscriptions(subscriptions);		
		response.setIsSuccess(true);

		LOG.info("Request response " + response);
		return response;		
	}

}
