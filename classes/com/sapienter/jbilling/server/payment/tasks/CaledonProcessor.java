/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.payment.tasks;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

/**
 * This class implements the Caledon Payment gateway interface,
 * validates request parameters, executes the gateway call and
 * processes and unwraps the response.
 * @author emirc
 */
public class CaledonProcessor {
	
	private static final Logger log = Logger.getLogger(PaymentCaledonTask.class);

	private class Reflected {
		private String name;
		private CaledonParam param;
		private String propName;
	}
	
	private List<Reflected> inputDataMapper = new ArrayList<Reflected>();
	private Hashtable<String, PropertyDescriptor> outputDataMapper = new Hashtable<String, PropertyDescriptor>();
	
	private String url;
	
	public CaledonProcessor(String url) {
		this.url = url;
			
		PropertyDescriptor[] descs = PropertyUtils.getPropertyDescriptors(CaledonRequestDTO.class);
		for (int i = 0; i < descs.length; i++) {
			CaledonParam param;
			try {
				param = descs[i].getReadMethod().getAnnotation(CaledonParam.class);
			} catch (NullPointerException e) {
				continue;
			}
			if (param != null) {
				Reflected mapping = new Reflected();
				mapping.name = param.name();
				mapping.param = param;
				mapping.propName = descs[i].getName();
				if (param.position() >= 0) {
					inputDataMapper.add(param.position(), mapping);
				} else {
					inputDataMapper.add(mapping);
				}
			}
		}
		
		descs = PropertyUtils.getPropertyDescriptors(CaledonResponseDTO.class);
		for (int i = 0; i < descs.length; i++) {
			CaledonParam param;
			try {
				param = descs[i].getReadMethod().getAnnotation(CaledonParam.class);
			} catch (NullPointerException e) {
				continue;
			}
			if (param != null) {
				outputDataMapper.put(param.name(), descs[i]);
			}
		}
	}

	/**
	 * Processes a request to the Caledon Payment Gateway.
	 * @param req
	 * @return
	 */
	public CaledonResponseDTO process(CaledonRequestDTO req) 
			throws CaledonProcessingException {
		HttpMethod request = serializeRequest(req);
		HttpClient client = new HttpClient();
		
		int result = 0;
		try {
			result = client.executeMethod(request);
			if (result == 200) {
				return serializeResponse(
						request.getResponseBodyAsString());
			}
		} catch (HttpException e) {
			throw new CaledonProcessingException(e);
		} catch (IOException e) {
			throw new CaledonProcessingException(e);
		}
		throw new CaledonProcessingException("Unexpected server " +
				"error [" + result + "]");
	}

	public CaledonResponseDTO serializeResponse(String response) 
			throws CaledonProcessingException {
		
		CaledonResponseDTO result = null;
		try {
			// Parse the response data (it is formatted as an URL query string).
			Hashtable<String, String> output = new Hashtable<String,String>();
			StringTokenizer st = new StringTokenizer(response, "&");
			while(st.hasMoreTokens()) {
				StringTokenizer st1 = new StringTokenizer(st.nextToken(), "=");
				while (st1.hasMoreTokens()) {
					output.put(st1.nextToken(), URLDecoder.decode(st1.nextToken(), "UTF-8"));
				}
			}
			
			result = new CaledonResponseDTO();
			// Return parameters are now loaded in the hashtable. Populate the output DTO.
			for (Iterator<String> i = output.keySet().iterator(); i.hasNext(); ) {
				String key = i.next();
				if (!outputDataMapper.containsKey(key)) {
					if (log.isDebugEnabled()) {
						log.debug("Key [" + key + "] should not exist in output parameter");
					}
					continue;
				}
				BeanUtils.setProperty(result, outputDataMapper.get(key).getName(), output.get(key));
			}
			
		} catch (Throwable e) {
			throw new CaledonProcessingException("Error parsing response string [" + response + "]", e);
		}
		return result;
	}

	public HttpMethod serializeRequest(CaledonRequestDTO req) 
			throws CaledonProcessingException {
		
		validateRequest(req);
		
		try {
			GetMethod method = new GetMethod(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (Iterator<Reflected> i = inputDataMapper.listIterator(); i.hasNext(); ) {
				Reflected prop = i.next();
				String value = BeanUtils.getSimpleProperty(req, prop.propName);
				if (value != null && !value.equals("")) {
					NameValuePair param = new NameValuePair(prop.name, value);
					params.add(param);
				}
			}
			method.setQueryString(params.toArray(new NameValuePair[1]));
			
			if (log.isDebugEnabled()) {
				log.debug("Querying caledon service with parameters: " + method.getQueryString());
			}
			return method;
		} catch (Throwable e) {
			throw new CaledonProcessingException("Error while serializing request " +
					"to Caledon Gateway", e);
		}
	}

	private void validateRequest(CaledonRequestDTO req) 
			throws CaledonProcessingException {
		
		// Verify that the type corresponds to a valid request type
		CaledonRequestType type = req.getType();
		if (type == null || type == CaledonRequestType.ALL ||
				type == CaledonRequestType.NONE) {
			throw new CaledonProcessingException("Invalid request type, operation does not " +
					"exist");
		}
		
		String reqType = req.getTypeValue();
		
		try {
			for (Iterator<Reflected> i = inputDataMapper.listIterator(); i.hasNext(); ) {
				
				Reflected prop = i.next();
				String value = BeanUtils.getSimpleProperty(req, prop.propName);
				
				if (value != null && !value.equals("")) {
					// Value exists. Check that is is either required or optional
					
					if (!value.matches(prop.param.pattern())) {
						if (log.isDebugEnabled()) {
							log.debug("Parameter \"" + prop.name +
									"\" value [" + value + "] is invalid (pattern should be \""
									+ prop.param.pattern() + "\"");
						}
					}
					if (!(prop.param.mandatoryCodes().equals("*") ||
							prop.param.mandatoryCodes().contains(reqType)) &&
							!(prop.param.optionalCodes().equals("*") ||
							prop.param.optionalCodes().contains(reqType))) {
						
						if (log.isDebugEnabled()) {
							log.debug("Parameter \"" + prop.name +
									"\" is not expected for request type \"" + reqType + "\"");
						}
					}
					
				} else {
					// Value does not exists. Verify that it is not required here.
					if (prop.param.mandatoryCodes().equals("*") ||
							prop.param.mandatoryCodes().contains(reqType)) {
						
						if (log.isDebugEnabled()) {
							log.debug("Parameter \"" + prop.name +
									"\" is required for request type \"" + reqType + "\"");
						}
					}
				}
			}
		} catch (Throwable e) {
			throw new CaledonProcessingException("Error during validation of request " +
					"to Caledon Gateway", e);
		}
	}
}
