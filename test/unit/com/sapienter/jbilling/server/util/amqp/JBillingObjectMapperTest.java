package com.sapienter.jbilling.server.util.amqp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class JBillingObjectMapperTest {

	@Test
	public void test() throws JsonGenerationException, JsonMappingException, IOException, InstantiationException, IllegalAccessException {
		ObjectMapper jbillingMapper = new JBillingObjectMapper();

		Class<?>[] testData = new Class<?>[] { 
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
		};

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

}
