package com.sapienter.jbilling.server.util.amqp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class JBillingObjectMapperTest {

	private Class<?>[] testData = new Class<?>[] { 
			GetUserRequest.class,
			GetUserResponse.class, 
			GetOrderRequest.class,
			GetOrderResponse.class, 
			GetOrderByStringMetaDataRequest.class,
			GetOrderByStringMetaDataResponse.class,
			ValidateUserAndPurchaseRequest.class,
			ValidateUserAndPurchaseResponse.class,
			CreateOrderRequest.class, 
			CreateOrderResponse.class,
			DeleteOrderRequest.class, 
			DeleteOrderResponse.class, 
			UpdateOrderRequest.class,
			UpdateOrderResponse.class,
	};


	@Test
	public void test() throws JsonGenerationException, JsonMappingException, IOException, InstantiationException, IllegalAccessException {
		ObjectMapper jbillingMapper = new JBillingObjectMapper();

		for (Class<?> messageClass : testData) {
			System.out.println("===> Serializing " + messageClass.getName()
					+ "...");
			String jsonStr = jbillingMapper.writeValueAsString(messageClass
					.newInstance());
			assertNotNull("Could not serialize " + messageClass.getName(), jsonStr);

			System.out.println("<=== deserializing " + messageClass.getName()
					+ "...");
			Object deserializedObj = jbillingMapper.readValue(jsonStr,
					messageClass);
			assertNotNull("Could not deserialize " + messageClass.getName() + ", jsonString='" + jsonStr + "'", deserializedObj);
			assertEquals(messageClass, deserializedObj.getClass());
		}
	}
	
	@Test 
	public void testRequestResponseMap() throws InstantiationException, IllegalAccessException {
		int correlationId = 0;
		RequestResponseMap requestResponseMap = new	RequestResponseMap();
		
		Iterator<Class<?>> iter = Arrays.asList(testData).iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Class<? extends RequestBase> reqClazz = (Class<? extends RequestBase>) iter.next();
			@SuppressWarnings("unchecked")
			Class<? extends ResponseBase> respClazz = (Class<? extends ResponseBase>) iter.next();
			
			RequestBase request = reqClazz.newInstance();
			request.setCorrelationId(correlationId++);
			ResponseBase response = requestResponseMap.makeResponse(request);
			assertNotNull("No response made", response);
			assertEquals("CorrelationId", request.getCorrelationId(), response.getCorrelationId());
			assertEquals("Response class", respClazz, response.getClass());
		}
	}

}
