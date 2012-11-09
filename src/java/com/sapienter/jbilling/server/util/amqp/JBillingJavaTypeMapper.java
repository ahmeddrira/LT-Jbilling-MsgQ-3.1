package com.sapienter.jbilling.server.util.amqp;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.amqp.support.converter.DefaultJavaTypeMapper;


/**
 * Extended Java Type Mapper which is configured to map the request and response classes.
 * Required because the default class loader in JBilling is the GrailClassLoader and it cannot
 * find the POJO classes.
 * 
 * @author smit005
 *
 */
public class JBillingJavaTypeMapper extends DefaultJavaTypeMapper {
	
	public JBillingJavaTypeMapper(Map<Class<? extends RequestBase>, Class<? extends ResponseBase>> requestResponseMap) {
		super();
		
		Map<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
		for (Entry<Class<? extends RequestBase>, Class<? extends ResponseBase>> entry : requestResponseMap.entrySet()) {
			// Map the request (entry key)
			idClassMapping.put(entry.getKey().toString(), entry.getKey());
			// Map the response (entry value)
			idClassMapping.put(entry.getValue().toString(), entry.getValue());
		}
		
		this.setIdClassMapping(idClassMapping);
	}
	
}
