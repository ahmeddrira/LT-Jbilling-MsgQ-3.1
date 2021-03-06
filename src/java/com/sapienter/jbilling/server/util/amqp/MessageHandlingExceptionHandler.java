package com.sapienter.jbilling.server.util.amqp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.Assert;

import com.liquidtelecom.jbilling.api.util.RequestResponseMap;
import com.liquidtelecom.jbilling.api.msg.RequestBase;
import com.liquidtelecom.jbilling.api.msg.ResponseBase;


/**
 * Class handles any message handling exceptions thrown and converts them to the
 * appropriate response. Will need extending as more request/responses are added
 * 
 * @author smit005
 * 
 */
public class MessageHandlingExceptionHandler {
	private static final Logger LOG = Logger
			.getLogger(MessageHandlingExceptionHandler.class);

	private RequestResponseMap requestResponseMap;

	@Required
	public void setRequestResponseMap(RequestResponseMap requestResponseMap) {
		this.requestResponseMap = requestResponseMap;
	}

	private ResponseBase makeResponse(MessagingException exception) {
		Object msgPayLoad = exception.getFailedMessage().getPayload();

		if (!(msgPayLoad instanceof RequestBase)) {
			LOG.warn("Unexpected message type " + msgPayLoad.getClass());
			return null;
		}
		RequestBase request = (RequestBase) exception.getFailedMessage()
				.getPayload();

		Assert.notNull(requestResponseMap,
				"requestResponseMap not initialised!");
		ResponseBase response = requestResponseMap.makeResponse(request);
		Assert.notNull(response, "Could not create response for request "
				+ request);

		response.setCorrelationId(request.getCorrelationId());
		response.setIsSuccess(false);
		response.setExceptionClass(exception.getClass());
		response.setErrorMessage(exception.getMessage());

		return response;

	}

	// public Message<ResponseBase> handleErrorMessage(ErrorMessage
	// errorMessage) {
	// LOG.warn("Handle messaging error message " + errorMessage);
	//
	// Assert.isInstanceOf(MessagingException.class, errorMessage.getPayload(),
	// "Unexpected class type in MessagingExceptin payload (" +
	// errorMessage.getPayload().getClass() + ")");
	// ResponseBase response = makeResponse((MessagingException)
	// errorMessage.getPayload());
	// if (response != null) {
	// return
	// MessageBuilder.withPayload(response).copyHeaders(errorMessage.getHeaders()).build();
	// }
	//
	// return null;
	// }
	//
	public Message<ResponseBase> handleMessagingException(
			MessagingException exception) {
		LOG.warn("Handle messaging exception " + exception);

		ResponseBase response = makeResponse(exception);
		if (response != null) {
			LOG.info("Made response for exception. response=" + response.toString());

			return MessageBuilder.withPayload(response)
					.copyHeaders(exception.getFailedMessage().getHeaders())
					.build();
		}

		return null;
	}

}
