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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.ParameterParser;
import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.payment.db.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.payment.db.PaymentResultDAS;
import com.sapienter.jbilling.server.pluggableTask.PaymentTask;
import com.sapienter.jbilling.server.pluggableTask.PaymentTaskWithTimeout;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.contact.db.ContactDTO;
import com.sapienter.jbilling.server.user.db.CreditCardDTO;
import com.sapienter.jbilling.server.util.Constants;

/**
 * A pluggable PaymentTask that uses RBS WorldPay gateway for credit card
 * transactions.
 * 
 * The following parameters must be configured for this payment task to work: -
 * "URL" WordlPay gateway server url "StoreId" Store ID assigned by RBS WorldPay
 * "MerchantID" Merchant ID assigned by RBS WorldPay "TerminalId" Terminal ID
 * assigned by RBS WorldPay "SellerId" optional Store SellerId associated with
 * the StoreId. The SellerId is mandatory if the security flag is turned on for
 * the store. "Password" optional Password associated with the SellerId. The
 * Password is mandatory if the security flag is turned on for the store.
 * 
 * timeout_sec - number of seconds for timeout (in inheritance from
 * PaymentTaskWithTimeout)
 * 
 * @author othman
 */

public class PaymentWorldPayTask extends PaymentTaskWithTimeout implements
		PaymentTask {
	// ------------------------ Private classes -----------------
	/**
	 * Class for request parameters keeping
	 */
	public static class NVPList extends LinkedList<NameValuePair> {
		public void add(String name, String value) {
			add(new NameValuePair(name, value));
		}

		public NameValuePair[] toArray() {
			return super.toArray(new NameValuePair[size()]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			for (NameValuePair pair : this) {
				String entry = pair.getName() + "=" + pair.getValue() + "&";
				sb.append(entry);
			}
			return sb.substring(0, sb.length() - 1).toString();

		}

	}

	// ------------------------ Constants -----------------------
	// names of plugin parameters
	public interface Params {
		public static final String WORLD_Pay_URL = "URL";//  
		/* Store ID assigned by RBS WorldPay */
		public static final String STORE_ID = "STOREID";
		/* Merchant ID assigned by RBS WorldPay */
		public static final String MERCHANT_ID = "MERCHANTID";
		/* Terminal ID assigned by RBS WorldPay */
		public static final String TERMINAL_ID = "TERMINALID";
		/*
		 * optional Store SellerId associated with the StoreId. The SellerId is
		 * mandatory if the security flag is turned on for the store.
		 */
		public static final String SELLER_ID = "SELLERID";
		/*
		 * optional Password associated with the SellerId. The Password is
		 * mandatory if the security flag is turned on for the store.
		 */
		public static final String PASSWORD = "PASSWORD";
	}

	// Names of gateway parameters
	public interface WorldPayParams {
		// parameters for credit card payment
		interface CreditCard {
			// Credit card number
			public static final String CARD_NUMBER = "CARDNUMBER";
			/* Expiration date(MM/YY or MM/YYYY) */
			public static final String EXPIRATION_DATE = "EXPIRATIONDATE";
			// optional CVV/CVC Value
			public static final String CVV2 = "CVV2";
		}

		interface ForceParams {
			/* Approval Code for transaction */
			public static final String APPROVAL_CODE = "APPROVALCODE";
		}

		interface SettleParams {
			/* RBS WorldPay Order Number of transaction */
			public static final String ORDER_ID = "ORDERID";
		}

		// common parameters for ACH and credit card payment
		interface General {
			/* Order ID supplied by merchant (up to 32 char) */
			public static final String SVC_TYPE = "SVCTYPE";
			// Cardholder’s first name
			public static final String FIRST_NAME = "FIRSTNAME";
			// optional : Cardholder’s middle name
			public static final String LAST_NAME = "LASTNAME";
			// Cardholder’s billing street address
			public static final String STREET_ADDRESS = "STREETADDRESS";
			// Cardholder’s billing city
			public static final String CITY = "CITY";
			// Cardholder’s billing state
			public static final String STATE = "STATE";
			// Cardholder’s billing ZIP or postal code
			public static final String ZIP = "ZIP";
			// Cardholder’s country
			public static final String COUNTRY = "COUNTRY";
			// Amount to be charged
			public static final String AMOUNT = "AMOUNT";

		}

	}

	public interface WorldPayResponse {
		/* Transaction Approval Status(0=Approved, 1=Not Approved, 2=Exception) */
		public static final String TRANSACTION_STATUS = "TransactionStatus";
		/* The RBS WorldPay Visa/SecureCode response */
		public static final String ORDER_ID = "OrderId";
		/*
		 * The Approval Code returned by the Issuer if the authorization was
		 * Approved
		 */
		public static final String APPROVAL_CODE = "ApprovalCode";
		/*
		 * The AVS (Address Verification Service) response code if the
		 * authorization was Approved.
		 */
		public static final String AVS_RESPONSE = "AVSResponse";
		/*
		 * If the CVV2 value was set, the CVV2 Response if the authorization was
		 * Approved.
		 */
		public static final String CVV2_RESPONSE = "CVV2Response";
		// The Error Message
		public static final String ERROR_MSG = "ErrorMsg";
		// The Error Code
		public static final String ERROR_CODE = "ErrorCode";

	}

	// Type of transaction
	public enum SVCTYPE {
		PreAuth("AUTHORIZE"), Sale("SALE"), Settle("SETTLE"), Force(
				"FORCESETTLE"), PartialSettle("PARTIALSETTLE"), RefundOrder(
				"CREDITORDER"), RefundCredit("CREDIT");
		/*
		 * Cancel("Cancel"),ReAuthorize("ReAuthorize"),CodeCancel("CodeCancel"),
		 * DebitSale
		 * ("DebitSale"),DebitVoidSale("DebitVoidSale"),DebitReturn("DebitReturn"
		 * ),DebitBalance("DebitBalance"),
		 * EBTSale("EBTSale"),EBTVoidSale("EBTVoidSale"
		 * ),EBTReturn("EBTReturn"),EBTVoidReturn
		 * ("EBTVoidReturn"),EBTBalance("EBTBalance"),
		 * GiftRedeem("GiftRedeem"),GiftVoidSale
		 * ("GiftVoidSale"),GiftActivation("GiftActivation"
		 * ),GiftAddValue("GiftAddValue"),
		 * GiftVoid("GiftVoid"),GiftBalance("GiftBalance"
		 * ),ACHPayment("ACHPayment"
		 * ),Ack("Ack"),CheckGuarantee("CheckGuarantee"),CheckAuth("CheckAuth"),
		 * CheckCompletion
		 * ("CheckCompletion"),CheckInquiry("CheckInquiry"),CheckVoidSale
		 * ("CheckVoidSale"),AVSCheck("AVSCheck")
		 */

		private String code;

		private SVCTYPE(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	// transaction status response
	public enum TransactionStatus {
		Approved("0"), NotApproved("1"), Exception("2");

		private String code;

		private TransactionStatus(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	// Gateway urls
	public interface Urls {

		public static final String WorldPay_POST_URL = "https://tpdev.lynksystems.com/servlet/LynkePmtServlet";

	}

	/**
	 * Class for incapsulating authorization response
	 */
	public class WorldPayAuthorization {

		private final PaymentAuthorizationDTO paymentAuthDTO;

		public WorldPayAuthorization(String gatewayResponse) {
			LOG.debug("Payment authorization result of " + PROCESSOR
					+ " gateway parsing....");
			WorldPayResponseParser responseParser = new WorldPayResponseParser(
					gatewayResponse);
			paymentAuthDTO = new PaymentAuthorizationDTO();
			paymentAuthDTO.setProcessor(PROCESSOR);

			String approvalCode = responseParser
					.getValue(WorldPayResponse.APPROVAL_CODE);
			if (approvalCode != null) {
				paymentAuthDTO.setApprovalCode(approvalCode);
				LOG.debug("approvalCode [" + paymentAuthDTO.getApprovalCode()
						+ "]");
			}
			String transactionStatus = responseParser
					.getValue(WorldPayResponse.TRANSACTION_STATUS);
			if (transactionStatus != null) {
				paymentAuthDTO.setCode2(transactionStatus);
				LOG.debug("transactionStatus [" + paymentAuthDTO.getCode2()
						+ "]");
			}
			String orderID = responseParser.getValue(WorldPayResponse.ORDER_ID);
			if (orderID != null) {
				paymentAuthDTO.setTransactionId(orderID);
				paymentAuthDTO.setCode1(orderID);
				LOG.debug("transactionID/OrderID ["
						+ paymentAuthDTO.getTransactionId() + "]");
			}

			String errorMsg = responseParser
					.getValue(WorldPayResponse.ERROR_MSG);
			if (errorMsg != null) {
				paymentAuthDTO.setResponseMessage(errorMsg);
				LOG.debug("errorMessage ["
						+ paymentAuthDTO.getResponseMessage() + "]");
			}

		}

		public PaymentAuthorizationDTO getDTO() {
			return paymentAuthDTO;
		}

		public Integer getJBResultId() {
			Integer resultId = Constants.RESULT_UNAVAILABLE;
			if (isApproved(paymentAuthDTO.getCode2())) {
				resultId = Constants.RESULT_OK;
			}
			if (isNotApproved(paymentAuthDTO.getCode2())) {
				resultId = Constants.RESULT_FAIL;
			}
			if (isCommunicationProblem()) {
				resultId = Constants.RESULT_UNAVAILABLE;
			}

			return resultId;

		}

		public boolean isCommunicationProblem() {
			return isServerError(paymentAuthDTO.getCode2());
		}
	}

	/**
	 * Class for gateway's response parsing
	 */
	private class WorldPayResponseParser {

		private final String gatewayResponse;
		private List<NameValuePair> responseEntries;

		WorldPayResponseParser(String gatewayResponse) {
			this.gatewayResponse = gatewayResponse;
			parseResponse();
		}

		/**
		 * @return the gatewayResponse
		 */
		public String getGatewayResponse() {
			return gatewayResponse;
		}

		/**
		 * @return the responseEntries
		 */
		public List<NameValuePair> getResponseEntries() {
			return responseEntries;
		}

		public String getValue(String responseParamName) {
			String val = null;
			for (NameValuePair pair : responseEntries) {
				if (pair.getName().equals(responseParamName)) {
					val = pair.getValue();
					break;
				}
			}
			return val;
		}

		private void parseResponse() {
			ParameterParser parser = new ParameterParser();
			responseEntries = parser.parse(gatewayResponse, '&');

		}

	}

	// Payment processor identificator
	private static final String PROCESSOR = "WorldPay";
	// Credit card expiration format
	private static final String DATE_FORMAT = "MM/yyyy";
	private static final Logger LOG = Logger
			.getLogger(PaymentWorldPayTask.class);

	// ------------------------ Fields --------------------------
	private String url;
	private String merchantId;
	private String storeId;
	private String terminalId;
	private String sellerId;
	private String password;

	/**
	 * Take a transaction done with 'preAuth' and confirm it
	 * 
	 * @param auth
	 *            return value of preAuth.
	 * @param payment
	 *            payment data
	 * @return see prosess method description
	 */
	public boolean confirmPreAuth(PaymentAuthorizationDTO auth,
			PaymentDTOEx payment) throws PluggableTaskException {
		LOG.debug("ConfirmPreAuth processing for " + PROCESSOR + " gateway");
		if (!PROCESSOR.equals(auth.getProcessor())) {
			LOG.warn("The procesor of the pre-auth is not paypal, is "
					+ auth.getProcessor());
			// let the processor be called and failed, so the caller
			// can do something about it: probably call this one again but for
			// 'process'
		}
		CreditCardDTO card = payment.getCreditCard();
		if (card == null) {
			throw new PluggableTaskException(
					"Credit card is required capturing" + " payment: "
							+ payment.getId());
		}
		if (!isApplicable(payment)) {
			LOG.error("This payment can not be captured" + payment);
			return true;
		}
		return doProcess(payment, SVCTYPE.Settle, auth)
				.shouldCallOtherProcessors();
	}

	// ------------------------ Private methods -----------------
	/**
	 * Process transaction
	 * 
	 * @param payment
	 *            payment data
	 * @param transaction
	 *            type of transaction
	 * @param auth
	 *            data for confirmPreAuth operation
	 */
	private Result doProcess(PaymentDTOEx payment, SVCTYPE transaction,
			PaymentAuthorizationDTO auth) throws PluggableTaskException {
		if (!isApplicable(payment)) {
			return NOT_APPLICABLE;
		}
		if (payment.getCreditCard() == null) {
			LOG.error("Can't process without a credit card");
			throw new PluggableTaskException(
					"Credit card not present in payment");
		}
		if (payment.getAch() != null) {
			LOG.error("Can't process with a cheque");
			throw new PluggableTaskException("Can't process ACH charge");
		}
		NVPList request = new NVPList();
		fillData(request, payment, transaction);
		if (auth != null) {
			// add approvalCode & orderID parameters for this settelment transaction
			request.add(WorldPayParams.ForceParams.APPROVAL_CODE, auth
					.getApprovalCode());
			request.add(WorldPayParams.SettleParams.ORDER_ID, auth
					.getTransactionId());

		}
		if (payment.getIsRefund() == 1
				&& (payment.getPayment() == null || payment.getPayment()
						.getAuthorization() == null)) {
			LOG
					.error("Can't process refund without a payment with an authorization record");
			throw new PluggableTaskException(
					"Refund without previous authorization");
		}

		
		try {

			LOG.debug("Processing " + transaction + " for credit card");
			WorldPayAuthorization wrapper = new WorldPayAuthorization(makeCall(
					request, url));
			payment.setPaymentResult(new PaymentResultDAS().find(wrapper
					.getJBResultId()));
			// if transaction successful store it
			if (wrapper.getJBResultId().equals(Constants.RESULT_OK))
				storeProcessedAuthorization(payment, wrapper.getDTO());

			return new Result(wrapper.getDTO(), wrapper
					.isCommunicationProblem());
		} catch (Exception e) {
			LOG.error("Couldn't handle payment request due to error", e);
			payment.setPaymentResult(new PaymentResultDAS()
					.find(Constants.RESULT_UNAVAILABLE));
			return NOT_APPLICABLE;
		}
	}

	/**
	 * Method has been obsoleted
	 */
	public void failure(Integer userId, Integer retry) {
		// ignore
	}

	/**
	 * Fill request parameter with payment data
	 */
	private void fillData(NVPList request, PaymentDTOEx payment,
			SVCTYPE transaction) throws PluggableTaskException {

		request.add(Params.MERCHANT_ID, merchantId);
		request.add(Params.STORE_ID, storeId);
		request.add(Params.TERMINAL_ID, terminalId);
		request.add(Params.SELLER_ID, sellerId);
		request.add(Params.PASSWORD, password);
		try {
			ContactBL contact = new ContactBL();
			contact.set(payment.getUserId());
			ContactDTO contactEntity = contact.getEntity();
			request.add(WorldPayParams.General.STREET_ADDRESS, contactEntity
					.getAddress1());
			request.add(WorldPayParams.General.CITY, contactEntity.getCity());
			request.add(WorldPayParams.General.STATE, contactEntity
					.getStateProvince());
			request.add(WorldPayParams.General.ZIP, contactEntity
					.getPostalCode());

			request.add(WorldPayParams.General.FIRST_NAME, contactEntity
					.getFirstName());
			request.add(WorldPayParams.General.LAST_NAME, contactEntity
					.getLastName());
			request.add(WorldPayParams.General.COUNTRY, contactEntity
					.getCountryCode());

		} catch (Exception e) {
			throw new PluggableTaskException(
					"Error loading Contact for user id " + payment.getUserId(),
					e);
		}
		LOG.debug("Amount=" + payment.getAmount());
		request.add(WorldPayParams.General.AMOUNT, formatAmount(payment
				.getAmount()));
		request.add(WorldPayParams.General.SVC_TYPE, transaction.getCode());
		CreditCardDTO card = payment.getCreditCard();
		request.add(WorldPayParams.CreditCard.CARD_NUMBER, card.getNumber());
		String expirationDate = new SimpleDateFormat(DATE_FORMAT).format(card
				.getCcExpiry());
		LOG.debug("Expiration Date=" + card.getCcExpiry());
		LOG.debug("formated expiration Date=" + expirationDate);
		request.add(WorldPayParams.CreditCard.EXPIRATION_DATE, expirationDate);
		if (card.getSecurityCode() != null) {
			request.add(WorldPayParams.CreditCard.CVV2, String.valueOf(payment
					.getCreditCard().getSecurityCode()));

		}
	}

	/**
	 * Format number to gateway format
	 */
	private String formatAmount(float amount) {
		amount = Math.abs(amount); // for credits
		return (new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN))
				.toPlainString();
	}

	// ------------------------ Public methods ------------------
	/**
	 * initialize plugin parameters
	 */
	@Override
	public void initializeParamters(PluggableTaskDTO task)
			throws PluggableTaskException {
		super.initializeParamters(task);
		url = getOptionalParameter(Params.WORLD_Pay_URL, Urls.WorldPay_POST_URL);
		merchantId = ensureGetParameter(Params.MERCHANT_ID);
		storeId = ensureGetParameter(Params.STORE_ID);
		terminalId = ensureGetParameter(Params.TERMINAL_ID);
		sellerId = getOptionalParameter(Params.SELLER_ID, "");
		password = getOptionalParameter(Params.PASSWORD, "");
	}

	/**
	 * Method check if plugin could handle this operation
	 * 
	 * @param payment
	 *            payment data
	 */
	private boolean isApplicable(PaymentDTOEx payment) {
		if (payment.getCreditCard() == null && payment.getAch() == null) {
			LOG.warn("Can't process without a credit card or ach");
			return false;
		} else
			return true;

	}

	/**
	 * check if transaction was approved
	 * 
	 * @param transactionStatus
	 * @return
	 */
	private boolean isApproved(String transactionStatus) {
		return (transactionStatus.equals(TransactionStatus.Approved.getCode()));

	}

	/**
	 * check if transaction was not approved
	 * 
	 * @param transactionStatus
	 * @return
	 */
	private boolean isNotApproved(String transactionStatus) {
		return (transactionStatus.equals(TransactionStatus.NotApproved
				.getCode()));

	}

	/**
	 * Check if it server error
	 * 
	 * @param errorCode
	 *            error code which was returned by gateway
	 */
	private boolean isServerError(String transactionStatus) {
		return (transactionStatus.equals(TransactionStatus.Exception.getCode()));

	}

	/**
	 * Make request to worldpay gateway
	 * 
	 * @return response from gateway
	 */
	public String makeCall(NVPList request, String url) throws IOException {
		LOG.debug("Request to " + PROCESSOR + " gateway sending...");
		// create a singular HttpClient object
		HttpClient client = new HttpClient();
		client.setConnectionTimeout(getTimeoutSeconds() * 1000);
		PostMethod post = new PostMethod(url);
		// post.setRequestBody(request.toArray());
		LOG.debug("request query string : " + request.toString());
		post.setRequestBody(request.toString());
		// execute the method
		client.executeMethod(post);
		String responseBody = post.getResponseBodyAsString();
		LOG.debug("Got response:" + responseBody);
		// clean up the connection resources
		post.releaseConnection();
		post.recycle();
		return responseBody;
	}

	/**
	 * Do a credit card pre-authorization of a fixed amount.
	 * 
	 * @param payment
	 *            payment data
	 * @return see process method description
	 */
	public boolean preAuth(PaymentDTOEx payment) throws PluggableTaskException {
		LOG.debug("PreAuth processing for " + PROCESSOR + " gateway");
		return doProcess(payment, SVCTYPE.PreAuth, null)
				.shouldCallOtherProcessors();
	}

	/**
	 * Method for payment processisng
	 * 
	 * @param payment
	 *            payment data
	 * @return true if the next payment processor should be called by the
	 *         business plugin manager. In other words, for result of success of
	 *         failure, the return is false. If the communication with the
	 *         payment processor fails (server down, timeout, etc), return
	 *         false.
	 */
	public boolean process(PaymentDTOEx payment) throws PluggableTaskException {
		LOG.debug("Payment processing for " + PROCESSOR + " gateway");
		boolean result;

		SVCTYPE transaction = SVCTYPE.Sale;
		try {
			if (payment.getPayoutId() != null) {
				return true;
			}
			if (payment.getAmount() < 0 || (payment.getIsRefund() != 0)) {
				transaction = SVCTYPE.RefundCredit;
				LOG.debug("Doing a refund using credit card transaction");
				// note: formatAmount() will make amount positive for sending to
				// gateway
			}
			result = doProcess(payment, transaction, null)
					.shouldCallOtherProcessors();
			LOG.debug("Processing result is "
					+ payment.getPaymentResult().getId()
					+ ", return value of process is " + result);
		} catch (Exception e) {
			LOG.error("Exception", e);
			throw new PluggableTaskException(e);
		}
		return result;
	}

}