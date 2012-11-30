package com.sapienter.jbilling.server.util.amqp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.StaleObjectStateException;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.sapienter.jbilling.server.user.UserWS;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Test that JBilling API requests and responses can be serialised. This is
 * required because some of the JBilling DTO's have complicated overridden
 * methods which confuse the JSON object mappers.
 * 
 * @author smit005
 * 
 */
public class JBillingObjectMapperTest {

	PodamFactory factory = new PodamFactoryImpl(new JBillingDataStrategy());
	ObjectMapper jbillingMapper = new JBillingObjectMapper();

	private Class<?>[] requestResponses = new Class<?>[] { GetUserRequest.class,
			GetUserResponse.class, GetOrderRequest.class,
			GetOrderResponse.class, GetOrderByStringMetaDataRequest.class,
			GetOrderByStringMetaDataResponse.class,
			ValidateUserAndPurchaseRequest.class,
			ValidateUserAndPurchaseResponse.class, CreateOrderRequest.class,
			CreateOrderResponse.class, DeleteOrderRequest.class,
			DeleteOrderResponse.class, UpdateOrderRequest.class,
			UpdateOrderResponse.class,};
	
	private <T> void test(Class<T> clazz) throws Exception {
		T o = factory.manufacturePojo(clazz);

		String jsonStr = jbillingMapper.writeValueAsString(o);
		assertNotNull("Could not serialize " + clazz.getName(), jsonStr);

		System.out.println("<=== deserializing " + clazz.getName() + "...");
		T deserializedObj = jbillingMapper.readValue(jsonStr, clazz);
		assertNotNull("Could not deserialize " + clazz.getName()
				+ ", jsonString='" + jsonStr + "'", deserializedObj);
		assertEquals(clazz, deserializedObj.getClass());
		// Can't compare the JSON strings as there are so many mixin's filtering
		// out the 'facade' properties of the JB DTO's
		// assertEquals("JSON Strings", jsonStr,
		// jbillingMapper.writeValueAsString(deserializedObj));

	}

	@Test
	public void test() throws Exception {
		for (Class<?> messageClass : requestResponses) {
			test(messageClass);
		}
	}

	@Test
	public void testRequestResponseMap() throws InstantiationException,
			IllegalAccessException {
		int correlationId = 0;
		RequestResponseMap requestResponseMap = new RequestResponseMap();

		Iterator<Class<?>> iter = Arrays.asList(requestResponses).iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("unchecked")
			Class<? extends RequestBase> reqClazz = (Class<? extends RequestBase>) iter
					.next();
			@SuppressWarnings("unchecked")
			Class<? extends ResponseBase> respClazz = (Class<? extends ResponseBase>) iter
					.next();

			RequestBase request = reqClazz.newInstance();
			request.setCorrelationId(correlationId++);
			ResponseBase response = requestResponseMap.makeResponse(request);
			assertNotNull("No response made", response);
			assertEquals("CorrelationId", request.getCorrelationId(),
					response.getCorrelationId());
			assertEquals("Response class", respClazz, response.getClass());
		}
	}

	@Test
	public void testDynamicBalance() throws Exception {
		UserWS inUserWS = new UserWS();
		inUserWS.setDynamicBalance(BigDecimal.valueOf(20330.23));
		
		String jsonStr = jbillingMapper.writeValueAsString(inUserWS);
		assertNotNull("Could not serialize " + UserWS.class, jsonStr);

		UserWS outUserWS = jbillingMapper.readValue(jsonStr, UserWS.class);
		assertNotNull("Could not deserialize " + UserWS.class
				+ ", jsonString='" + jsonStr + "'", outUserWS);
		assertEquals("Dynamic balance", inUserWS.getDynamicBalance(), outUserWS.getDynamicBalance());
	}
}
