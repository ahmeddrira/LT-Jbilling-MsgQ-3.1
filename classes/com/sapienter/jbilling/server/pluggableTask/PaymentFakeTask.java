package com.sapienter.jbilling.server.pluggableTask;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.interfaces.PluggableTaskEntityLocal;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.util.Constants;

/**
 * Fake payment processor, providing the ability to configure a jbilling
 * installation for testing, where credit card transactions are not processed by
 * a real payment processor.
 * 
 * Behaviour of this processor completely depends on the actual credit card
 * number. In particlular:
 * 
 * <ul>
 * <li>If the number ends with an even number, it will always result on a
 * successful payment</li>
 * <li>If the number ends with an odd number it will always result on a failed
 * payment</li>
 * <li>If the number ends with "0" or with any not number symbol, then the
 * result will be 'unavailable'.</li>
 * </ul>
 */
public class PaymentFakeTask extends PaymentTaskBase implements PaymentTask {
	public static final String PARAM_PROCESSOR_NAME_OPTIONAL = "processor_name";
	public static final String VALUE_PROCESSOR_NAME_DEFAULT = "fake-processor";
	
	public static final String PARAM_CODE1_OPTIONAL = "code";
	public static final String VALUE_CODE1_DEFAULT = "fake-code-default";
	
	public static final String PARAM_HANDLE_ALL_REQUESTS = "all";
	public static final String PARAM_NAME_PREFIX = "name_prefix";
	
	private static final String PREAUTH_TRANSACTION_PREFIX = ":preauth:";
	
	private boolean myShouldBlockOtherProcessors;
	private Filter myFilter = Filter.ACCEPT_ALL;
	
	private static final Logger LOG = Logger.getLogger(PaymentFakeTask.class); 
	
	public void failure(Integer userId, Integer retry) {
		// nothing to do -- ageing process would probably started by real
		// implementation
	}
	
	@Override
	public void initializeParamters(PluggableTaskEntityLocal task) throws PluggableTaskException {
		super.initializeParamters(task);
		
		myShouldBlockOtherProcessors = Boolean.parseBoolean((String) parameters.get(PARAM_HANDLE_ALL_REQUESTS));
		myFilter = Filter.ACCEPT_ALL;
		if (!myShouldBlockOtherProcessors){
			myFilter = createFilter((String) parameters.get(PARAM_NAME_PREFIX));
		}
	}
	
	public boolean process(PaymentDTOEx paymentInfo) throws PluggableTaskException {
		Result result = doFakeAuthorization(paymentInfo, null);
		return result.shouldCallOtherProcessors();
	}
	
	public PaymentAuthorizationDTOEx preAuth(PaymentDTOEx paymentInfo)
		throws PluggableTaskException {
		
		String transactionId = generatePreAuthTransactionId();
		Result result = doFakeAuthorization(paymentInfo, transactionId);
		
		return result.shouldCallOtherProcessors() ? null : result.getAuthorizationData();
	}
	
	public PaymentAuthorizationDTOEx confirmPreAuth(PaymentAuthorizationDTOEx auth, PaymentDTOEx paymentInfo)
		throws PluggableTaskException {
		
		if (!getFakeProcessorName().equals(auth.getProcessor())){
			return null;
		}
		
		if (!isPreAuthTransactionId(auth.getTransactionId())){
			LOG.warn("AuthorizationDTOEx with transaction id: " + auth.getTransactionId() + " is used as preauth data");
			return null;
		}
		
		Result result = doFakeAuthorization(paymentInfo, null);
		return result.shouldCallOtherProcessors() ? null : result.getAuthorizationData();
	}
		
	private Result doFakeAuthorization(PaymentDTOEx payment, String transactionId) throws PluggableTaskException {
		CreditCardDTO creditCard = payment.getCreditCard();
		if (creditCard == null || !myFilter.accept(creditCard)){
			//give real processors a chance 
			return new Result(null, true);
		}

		Integer resultId = getProcessResultId(creditCard);
		payment.setResultId(resultId);
		PaymentAuthorizationDTOEx authInfo = createAuthorizationDTO(resultId, transactionId);
		storeProcessedAuthorization(payment, authInfo);
		
		boolean wasProcessed = (Constants.RESULT_FAIL.equals(resultId) || Constants.RESULT_OK.equals(resultId));
		boolean shouldCallOthers = !wasProcessed && !myShouldBlockOtherProcessors;
		return new Result(authInfo, shouldCallOthers);
	}
	
	private String generatePreAuthTransactionId(){
		return PREAUTH_TRANSACTION_PREFIX + System.currentTimeMillis();
	}
	
	private boolean isPreAuthTransactionId(String transactionId){
		return transactionId != null && transactionId.startsWith(PREAUTH_TRANSACTION_PREFIX);
	}
	
	private Integer getProcessResultId(CreditCardDTO card){
		String cardNumber = card.getNumber();
		char last = (cardNumber.length() == 0) ? 
				' ' : cardNumber.charAt(cardNumber.length() - 1);
		
		switch (last){
			case '2':
			case '4':
			case '6':
			case '8':
				return Constants.RESULT_OK;

			case '1':
			case '3':
			case '5':
			case '7':
			case '9':
				return Constants.RESULT_FAIL;

			default: 
				return Constants.RESULT_UNAVAILABLE; 
		}
	}
	
	private PaymentAuthorizationDTOEx createAuthorizationDTO(Integer resultConstant, String transactionId) {
		return createAuthorizationDTO(Constants.RESULT_OK.equals(resultConstant), transactionId);
	}

	private PaymentAuthorizationDTOEx createAuthorizationDTO(boolean isAuthorized, String transactionId) {
		PaymentAuthorizationDTOEx auth = new PaymentAuthorizationDTOEx();
		auth.setProcessor(getFakeProcessorName());
		auth.setResult(Boolean.valueOf(isAuthorized));
		auth.setCode1(getFakeCode1());
		auth.setTransactionId(transactionId);
		return auth;
	}

	private String getFakeProcessorName(){
		String result = (String) parameters.get(PARAM_PROCESSOR_NAME_OPTIONAL);
		if (result == null){
			result = VALUE_PROCESSOR_NAME_DEFAULT;
		}
		return result;
	}
	
	private String getFakeCode1(){
		String result = (String) parameters.get(PARAM_CODE1_OPTIONAL);
		if (result == null){
			result = VALUE_CODE1_DEFAULT;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "PaymentFakeTask: " + System.identityHashCode(this) + 
			", blocking: " + myShouldBlockOtherProcessors + 
			", filter: " + myFilter.toString() + 
			", code1: " + getFakeCode1();
	}

	private static Filter createFilter(String prefix) {
		return (prefix == null || prefix.trim().length() == 0) ? 
				Filter.ACCEPT_ALL : new NameStartsWithFilter(prefix);
	}

	/**
	 * Selects requests that should be processed by fake implementation
	 */
	private static interface Filter {
		public boolean accept(CreditCardDTO card);

		public static Filter ACCEPT_ALL = new Filter(){
			public boolean accept(CreditCardDTO card) {
				return true;
			}
			
			@Override
			public String toString() {
				return "Filter#ACCEPT_ALL";
			}
		};
	}
	
	private static class NameStartsWithFilter implements Filter {
		private final String myPrefix;

		public NameStartsWithFilter(String prefix){
			myPrefix = prefix;
		}
		
		public boolean accept(CreditCardDTO card) {
			String name = card.getName();
			return name != null && name.startsWith(myPrefix);
		}
		
		@Override
		public String toString() {
			return "Filter#startsWith:" + myPrefix;
		}
	}
	
}
