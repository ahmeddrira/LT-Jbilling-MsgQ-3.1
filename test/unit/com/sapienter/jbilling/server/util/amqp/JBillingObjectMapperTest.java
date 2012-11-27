package com.sapienter.jbilling.server.util.amqp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mvel2.optimizers.impl.refl.nodes.ArrayLength;

import com.sapienter.jbilling.server.user.ValidateUserAndPurchaseWS;

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
			Object o = messageClass.newInstance();
			// Test why quantity is not de-serialised 
			if (o instanceof ValidateUserAndPurchaseResponse) {
				ValidateUserAndPurchaseWS validateUserAndPurchaseWS = new ValidateUserAndPurchaseWS();
				validateUserAndPurchaseWS.setQuantity("23978");
				((ValidateUserAndPurchaseResponse) o).setValidateUserAndPurchaseWS(validateUserAndPurchaseWS);
			}
			String jsonStr = jbillingMapper.writeValueAsString(o);
			assertNotNull("Could not serialize " + messageClass.getName(), jsonStr);

			System.out.println("<=== deserializing " + messageClass.getName()
					+ "...");
			Object deserializedObj = jbillingMapper.readValue(jsonStr,
					messageClass);
			assertNotNull("Could not deserialize " + messageClass.getName() + ", jsonString='" + jsonStr + "'", deserializedObj);
			assertEquals(messageClass, deserializedObj.getClass());
			if (o instanceof ValidateUserAndPurchaseResponse) {
				assertEquals(o.getClass() + "::quantity", 
						((ValidateUserAndPurchaseResponse) o).getValidateUserAndPurchaseWS().getQuantityAsDecimal(), 
						((ValidateUserAndPurchaseResponse) deserializedObj).getValidateUserAndPurchaseWS().getQuantityAsDecimal()); 
			}
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
