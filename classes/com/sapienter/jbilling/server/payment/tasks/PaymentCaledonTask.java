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

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PaymentTask;
import com.sapienter.jbilling.server.pluggableTask.PaymentTaskWithTimeout;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.util.Constants;

/**
 * Pluggable payment task for Caledon Gateway.
 * 
 * This class implements payment processing via Caledon Card Services.
 * 
 * This pluggable task requires the following parameters to be
 * configured:
 * 
 * caledon_termid - Terminal ID provided by Caledon Gateway.
 * 
 * caledon_password - Needed only if you're using password-based
 * authentication with Caledon (alternative is IP based authentication,
 * which does not requires this parameter).
 * 
 * caledon_url - If not provided, it defaults to "https://lt3a.caledoncard.com",
 * otherwise the URL provided in configuration is used.
 * 
 * timeout_sec - Number of seconds for service timeout (inherited from
 * PaymentTaskWithTimeout).
 * 
 * @author emirc
 */
public class PaymentCaledonTask extends PaymentTaskWithTimeout implements
		PaymentTask {

	// Constants
	public static final String CALEDON_TERMID   = "caledon_termid";
	public static final String CALEDON_PASSWORD = "caledon_password";
	public static final String CALEDON_URL      = "caledon_url";
	
	private static final String DEFAULT_URL     = "https://lt3a.caledoncard.com";
	private static final String PROCESSOR       = "Caledon";
	
	private static final Logger log = Logger.getLogger(PaymentCaledonTask.class);
	
	// Internal variables
	private String termid;
	private String password;
	private String url = DEFAULT_URL;
	
	@Override
	public void initializeParamters(PluggableTaskDTO task)
			throws PluggableTaskException {
		
		super.initializeParamters(task);
		termid   = ensureGetParameter(CALEDON_TERMID);
		password = getOptionalParameter(CALEDON_PASSWORD, null);
		url      = getOptionalParameter(CALEDON_URL, DEFAULT_URL);
		if (log.isDebugEnabled()) {
			log.debug("Caledon parameters: termid=" + termid + 
					", password=" + password + ", url=" + url);
		}
	}
	
	@Override
	public boolean confirmPreAuth(PaymentAuthorizationDTO auth,
			PaymentDTOEx paymentInfo) throws PluggableTaskException {
		
		if (log.isDebugEnabled()) {
			log.debug("Performing PreAuth for Caledon gateway");
		}
		
		if (paymentInfo.getCreditCard() == null) {
			throw new PluggableTaskException("Credit card must " +
					"exist in order to pre-authorize a payment");
		}
		// Instantiate the service
		CaledonProcessor service = new CaledonProcessor(url);
		
		// Populate service request data
		CaledonRequestDTO req = new CaledonRequestDTO();
		req.setType(CaledonRequestType.COMPLETION);
		req.setTermid(termid);
		if (password != null) {
			req.setPass(password);
		}
		req.setAmount(paymentInfo.getAmount());
		req.setCard(paymentInfo.getCreditCard().getNumber());
		req.setExp(new SimpleDateFormat("MMyy").format(paymentInfo.getCreditCard().getExpiry()));
		req.setRef(auth.getTransactionId());
		
		// Execute the request
		CaledonResponseDTO resp;
		try {
			resp = service.process(req);
		} catch (CaledonProcessingException e) {
			log.error("Error processing Caledon pre-authorization", e);
			return true;
		}
		
		// Determine result and update payment information
		if (resp.getCode().equals("0000")) {
			paymentInfo.setResultId(Constants.RESULT_OK);
		} else {
			paymentInfo.setResultId(Constants.RESULT_FAIL);
		}
		
		PaymentAuthorizationDTO auth1 = new PaymentAuthorizationDTO();
		
		auth1.setTransactionId("CFR" + paymentInfo.getId());
		auth1.setProcessor(PROCESSOR);
		auth1.setCode1(resp.getAuth());
		auth1.setResponseMessage(resp.getText());
		storeProcessedAuthorization(paymentInfo, auth1);
		return false;
	}

	@Override
	public void failure(Integer userId, Integer retry) {
		// Ignores failure
	}

	@Override
	public boolean preAuth(PaymentDTOEx paymentInfo)
			throws PluggableTaskException {
		
		if (log.isDebugEnabled()) {
			log.debug("Performing PreAuth for Caledon gateway");
		}
		
		if (paymentInfo.getCreditCard() == null) {
			throw new PluggableTaskException("Credit card must " +
					"exist in order to pre-authorize a payment");
		}
		// Instantiate the service
		CaledonProcessor service = new CaledonProcessor(url);
		
		// Populate service request data
		CaledonRequestDTO req = new CaledonRequestDTO();
		req.setType(CaledonRequestType.PREAUTH);
		req.setTermid(termid);
		if (password != null) {
			req.setPass(password);
		}
		String txId = "PRE" + paymentInfo.getId();
		req.setAmount(paymentInfo.getAmount());
		req.setCard(paymentInfo.getCreditCard().getNumber());
		req.setExp(new SimpleDateFormat("MMyy").format(paymentInfo.getCreditCard().getExpiry()));
		req.setRef(txId);
		
		// Execute the request
		CaledonResponseDTO resp;
		try {
			resp = service.process(req);
		} catch (CaledonProcessingException e) {
			log.error("Error processing Caledon pre-authorization", e);
			return true;
		}
		
		// Determine result and update payment information
		if (resp.getCode().equals("0000")) {
			paymentInfo.setResultId(Constants.RESULT_OK);
		} else {
			paymentInfo.setResultId(Constants.RESULT_FAIL);
		}
		paymentInfo.setResultStr(resp.getText());
		
		PaymentAuthorizationDTO auth = new PaymentAuthorizationDTO();
		
		auth.setTransactionId(txId);
		auth.setProcessor(PROCESSOR);
		auth.setCode1(resp.getAuth());
		auth.setResponseMessage(resp.getText());
		storeProcessedAuthorization(paymentInfo, auth);
		
		return false;
	}

	@Override
	public boolean process(PaymentDTOEx paymentInfo)
			throws PluggableTaskException {
		
		if (log.isDebugEnabled()) {
			log.debug("Performing payment processing for Caledon gateway");
		}
		
		if (paymentInfo.getCreditCard() == null) {
			throw new PluggableTaskException("Credit card must " +
					"exist in order to perform payment or refund");
		}
		// Instantiate the service
		CaledonProcessor service = new CaledonProcessor(url);
		
		// Populate service request data
		CaledonRequestDTO req = new CaledonRequestDTO();
		
		req.setTermid(termid);
		if (password != null) {
			req.setPass(password);
		}
		String txId = "PAY" + paymentInfo.getId();
		
		boolean isRefund = (paymentInfo.getIsRefund() == null || paymentInfo.getIsRefund() == 0);
		
		if (!isRefund) {
			
			// Process requested is a normal payment.
			req.setType(CaledonRequestType.PREAUTH);
			req.setAmount(paymentInfo.getAmount());
			req.setCard(paymentInfo.getCreditCard().getNumber());
			req.setExp(new SimpleDateFormat("MMyy").format(paymentInfo.getCreditCard().getExpiry()));
			req.setRef(txId);
		
		} else {
			
			// Process requested is a refund.
			if (paymentInfo.getPayment() == null) {
				log.error("Refund should refer to a previous payment.");
			}
			req.setType(CaledonRequestType.VOID);
			req.setAmount(paymentInfo.getAmount());
			req.setCard(paymentInfo.getCreditCard().getNumber());
			req.setExp(new SimpleDateFormat("MMyy").format(paymentInfo.getCreditCard().getExpiry()));
			req.setRef(paymentInfo.getPayment().getAuthorization().getTransactionId());
		}
		// Execute the request
		CaledonResponseDTO resp;
		try {
			resp = service.process(req);
		} catch (CaledonProcessingException e) {
			log.error("Error processing Caledon pre-authorization", e);
			return true;
		}
		
		// Determine result and update payment information
		PaymentAuthorizationDTOEx auth = new PaymentAuthorizationDTOEx();
		
		if (resp.getCode().equals("0000")) {
			paymentInfo.setResultId(Constants.RESULT_OK);
			auth.setResult(Boolean.TRUE);
		} else {
			paymentInfo.setResultId(Constants.RESULT_FAIL);
			auth.setResult(Boolean.FALSE);
		}
		
		auth.setProcessor(PROCESSOR);
		auth.setCode1(resp.getAuth());
		auth.setTransactionId(txId);
	    auth.setResponseMessage(resp.getText());
	    storeProcessedAuthorization(paymentInfo, auth);
		
	    return true;
	}

}
