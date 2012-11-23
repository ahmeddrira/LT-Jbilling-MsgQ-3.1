package com.sapienter.jbilling.server.util.amqp;


import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.ValidatePurchaseWS;
import com.sapienter.jbilling.server.user.ValidateUserAndPurchaseWS;

@Transactional(propagation = Propagation.REQUIRED)
public class MessageServiceAPI {
	private static final Logger LOG = Logger.getLogger(MessageServiceAPI.class);
	
	private MessageServiceBL messageServiceBL;

	public MessageServiceBL getMessageServiceBL() {
		return messageServiceBL;
	}

	public void setMessageServiceBL(MessageServiceBL messageServiceBL) {
		this.messageServiceBL = messageServiceBL;
	}

	public ValidateUserAndPurchaseResponse process(
			ValidateUserAndPurchaseRequest request) {
		LOG.info("Process request " + request);

		ValidateUserAndPurchaseResponse response = new ValidateUserAndPurchaseResponse();
		response.setCorrelationId(request.getCorrelationId());

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

		GetOrderResponse response = new GetOrderResponse();
		response.setCorrelationId(request.getCorrelationId());

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

		GetOrderByStringMetaDataResponse response = new GetOrderByStringMetaDataResponse();
		response.setCorrelationId(request.getCorrelationId());

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

		DeleteOrderResponse response = new DeleteOrderResponse();
		response.setCorrelationId(request.getCorrelationId());

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

		CreateOrderResponse response = new CreateOrderResponse();
		response.setCorrelationId(request.getCorrelationId());

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

		GetUserResponse response = new GetUserResponse();
		response.setCorrelationId(request.getCorrelationId());

		UserBL bl = new UserBL(request.getUserId());
		response.setUserWS(bl.getUserWS());
		response.setIsSuccess(true);

		LOG.info("Request response " + response);
		return response;
	}

	public UpdateOrderResponse process(UpdateOrderRequest request) {
		LOG.info("Process request " + request);

		UpdateOrderResponse response = new UpdateOrderResponse();
		response.setCorrelationId(request.getCorrelationId());

		messageServiceBL.doUpdateOrder(request.getOrderWS());

		response.setIsSuccess(true);

		LOG.info("Request response " + response);
		return response;
	}

}
