package com.sapienter.jbilling.server.util.amqp;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.message.ErrorMessage;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.Assert;

/**
 * Class handles any message handling exceptions thrown and converts them to the appropriate response.
 * Will need extending as more request/responses are added
 * 
 * @author smit005
 *
 */
public class MessageHandlingExceptionHandler {
	private static final Logger LOG = Logger.getLogger(MessageHandlingExceptionHandler.class);

	private Map<Class<? extends RequestBase>, Class<? extends ResponseBase>> requestResponseMap;
	
	@Required
	public void setRequestResponseMap(Map<Class<? extends RequestBase>, Class<? extends ResponseBase>> requestResponseMap) {
		this.requestResponseMap = requestResponseMap;
	}

	private ResponseBase makeResponse(MessagingException exception) {
		Object msgPayLoad = exception.getFailedMessage().getPayload();
		
		if (! (msgPayLoad instanceof RequestBase)) {
			LOG.warn("Unexpected message type " + msgPayLoad.getClass());
			return null;
		}
		
		Assert.notNull(requestResponseMap, "requestResponseMap not initialised!");
		Class<? extends ResponseBase> responseClass = requestResponseMap.get(msgPayLoad.getClass());
		if (responseClass == null) {
			LOG.warn("Unknown request class " + msgPayLoad.getClass());
			return null;			
		}
		
		RequestBase request = (RequestBase) msgPayLoad;
		try {
			ResponseBase response = responseClass.newInstance();
			response.setCorrelationId(request.getCorrelationId());
			response.setIsSuccess(false);
			response.setCause(exception);
			response.setErrorMessage(exception.getMessage());
			
			return response;					
		} catch (InstantiationException e) {
			LOG.warn("Cannot instantiate response of type " + responseClass.getName());
		} catch (IllegalAccessException e) {
			LOG.warn("Cannot instantiate response of type " + responseClass.getName());
		}
		
		return null;
	}
	
	public Message<ResponseBase> handleErrorMessage(ErrorMessage errorMessage) {
		LOG.warn("Handle messaging error message " + errorMessage);

		Assert.isInstanceOf(MessagingException.class, errorMessage.getPayload(), 
				"Unexpected class type in MessagingExceptin payload (" + errorMessage.getPayload().getClass() + ")");
		ResponseBase response = makeResponse((MessagingException) errorMessage.getPayload());
		if (response != null) {
			return MessageBuilder.withPayload(response).copyHeaders(errorMessage.getHeaders()).build();
		}
		
		return null;
	}
	
	public Message<ResponseBase> handleMessagingException(MessagingException exception) {
		LOG.warn("Handle messaging exception " + exception);

		ResponseBase response = makeResponse(exception);
		if (response != null) {
			return MessageBuilder.withPayload(response).copyHeaders(exception.getFailedMessage().getHeaders()).build();					
		} 
	
		return null;
	}

}
