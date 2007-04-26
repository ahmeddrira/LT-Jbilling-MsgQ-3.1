/*
 The contents of this file are subject to the Jbilling Public License
 Version 1.1 (the "License"); you may not use this file except in
 compliance with the License. You may obtain a copy of the License at
 http://www.jbilling.com/JPL/

 Software distributed under the License is distributed on an "AS IS"
 basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 License for the specific language governing rights and limitations
 under the License.

 The Original Code is jbilling.

 The Initial Developer of the Original Code is Emiliano Conde.
 Portions created by Sapienter Billing Software Corp. are Copyright 
 (C) Sapienter Billing Software Corp. All Rights Reserved.

 Contributor(s): ______________________________________.
*/
package com.sapienter.jbilling.server.util.api;

import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.InvoiceLineDTO;
import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;
import com.sapienter.jbilling.server.invoice.InvoiceLineDTOEx;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemPriceDTOEx;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.CreateResponseWS;
import com.sapienter.jbilling.server.user.UserTransitionResponseWS;
import com.sapienter.jbilling.server.user.UserWS;

/**
 * A web services implementation of JbillingAPI. All api implementations
 * invoke web service call and return the results as returned by the web service call.
 *  
 * @author Narinder
 *
 */
public class AxisAPI implements JbillingAPI {
	private static final String QNAME = "http://www.sapienter.com/billing";
	private final String username;
	private final String password;
	private final String endPoint;
    private static final Logger LOG = Logger.getLogger(AxisAPI.class);
    private final Call call;
	
	/**
	 * Constructor taking username password required to access jbilling apis.
	 * @param username
	 * @param password
	 */
	protected AxisAPI(String username, String password, String endPoint) throws JbillingAPIException {
		this.username = username;
		this.password = password;
        this.endPoint = endPoint;
        call = prepareAxisCall();
	}
	
	/**
	 * Convenience method to construct an AXIS api call.
	 * @return
	 * @throws JbillingAPIException if there is any error during preperation of
	 * Axis API call.
	 */
	private Call prepareAxisCall() throws JbillingAPIException{
		try{
	        Service  service = new Service();
	        Call  call = (Call) service.createCall();
	        call.setTargetEndpointAddress( new java.net.URL(endPoint) );
	        call.setUsername(username);
	        call.setPassword(password);
	        registerTypeMappings(call);
	        return call;
		} catch (Exception t){
            LOG.error("Preparing call", t);
			throw new JbillingAPIException(t);
		}
	}
	
	/**
	 * Register type mappings required to marshall/ unmarshall SOAP request/ response.
	 * @param call
	 * @throws Exception
	 */
	private void registerTypeMappings(Call call) throws Exception{
		
	    // PaymentWS
        QName qn = new QName(QNAME, "PaymentWS");
        BeanSerializerFactory ser1 = new BeanSerializerFactory(
                PaymentWS.class, qn);
        BeanDeserializerFactory ser2 = new BeanDeserializerFactory (
                PaymentWS.class, qn);
        call.registerTypeMapping(PaymentWS.class, qn, ser1, ser2); 

        // PaymentInfoChequeDTO            
        qn = new QName(QNAME, "PaymentInfoChequeDTO");
        ser1 = new BeanSerializerFactory(
                PaymentInfoChequeDTO.class, qn);
        ser2 = new BeanDeserializerFactory (
                PaymentInfoChequeDTO.class, qn);
        call.registerTypeMapping(PaymentInfoChequeDTO.class, qn, ser1, ser2); 

        // PaymentAuthorizationDTO            
        qn = new QName(QNAME, "PaymentAuthorizationDTO");
        ser1 = new BeanSerializerFactory(PaymentAuthorizationDTO.class, qn);
        ser2 = new BeanDeserializerFactory ( PaymentAuthorizationDTO.class,
                 qn);
        call.registerTypeMapping(PaymentAuthorizationDTO.class, qn, ser1, ser2); 
        
        // InvoiceWS            
        qn = new QName(QNAME, "InvoiceWS");
        ser1 = new BeanSerializerFactory(
                InvoiceWS.class, qn);
        ser2 = new BeanDeserializerFactory (
                InvoiceWS.class, qn);
        call.registerTypeMapping(InvoiceWS.class, qn, ser1, ser2); 

        // InvoiceLineDTO            
        qn = new QName(QNAME, "InvoiceLineDTO");
        ser1 = new BeanSerializerFactory(
                InvoiceLineDTO.class, qn);
        ser2 = new BeanDeserializerFactory (
                InvoiceLineDTO.class, qn);
        call.registerTypeMapping(InvoiceLineDTO.class, qn, ser1, ser2); 

        // InvoiceLineDTOEx            
        qn = new QName(QNAME, "InvoiceLineDTOEx");
        ser1 = new BeanSerializerFactory(
                InvoiceLineDTOEx.class, qn);
        ser2 = new BeanDeserializerFactory (
                InvoiceLineDTOEx.class, qn);
        call.registerTypeMapping(InvoiceLineDTOEx.class, qn, ser1, ser2); 
        
        // UserWS            
        qn = new QName(QNAME, "UserWS");
        ser1 = new BeanSerializerFactory(
                UserWS.class, qn);
        ser2 = new BeanDeserializerFactory (
                UserWS.class, qn);
        call.registerTypeMapping(UserWS.class, qn, ser1, ser2); 

        // ContactWS
        qn = new QName(QNAME, "ContactWS");
        ser1 = new BeanSerializerFactory(
                ContactWS.class, qn);
        ser2 = new BeanDeserializerFactory (
                ContactWS.class, qn);
        call.registerTypeMapping(ContactWS.class, qn, ser1, ser2); 

        // CreditCardDTO            
        qn = new QName(QNAME, "CreditCardDTO");
        ser1 = new BeanSerializerFactory(
                CreditCardDTO.class, qn);
        ser2 = new BeanDeserializerFactory (
                CreditCardDTO.class, qn);
        call.registerTypeMapping(CreditCardDTO.class, qn, ser1, ser2); 
        
        // OrderWS            
        qn = new QName(QNAME, "OrderWS");
        ser1 = new BeanSerializerFactory(
                OrderWS.class, qn);
        ser2 = new BeanDeserializerFactory (
                OrderWS.class, qn);
        call.registerTypeMapping(OrderWS.class, qn, ser1, ser2); 

        // OrderLineWS            
        qn = new QName(QNAME, "OrderLineWS");
        ser1 = new BeanSerializerFactory(
                OrderLineWS.class, qn);
        ser2 = new BeanDeserializerFactory (
                OrderLineWS.class, qn);
        call.registerTypeMapping(OrderLineWS.class, qn, ser1, ser2); 

        // CreateResponseWS            
        qn = new QName(QNAME, "CreateResponseWS");
        ser1 = new BeanSerializerFactory(
                CreateResponseWS.class, qn);
        ser2 = new BeanDeserializerFactory (
                CreateResponseWS.class, qn);
        call.registerTypeMapping(CreateResponseWS.class, qn, ser1, ser2); 
            
        // PaymentAuthorizationDTOEx            
        qn = new QName(QNAME, "PaymentAuthorizationDTOEx");
        ser1 = new BeanSerializerFactory(
                PaymentAuthorizationDTOEx.class, qn);
        ser2 = new BeanDeserializerFactory (
                PaymentAuthorizationDTOEx.class, qn);
        call.registerTypeMapping(PaymentAuthorizationDTOEx.class, qn, ser1, ser2); 
        
        // ItemDTOEx            
        qn = new QName(QNAME, "ItemDTOEx");
        ser1 = new BeanSerializerFactory(
                ItemDTOEx.class, qn);
        ser2 = new BeanDeserializerFactory (
                ItemDTOEx.class, qn);
        call.registerTypeMapping(ItemDTOEx.class, qn, ser1, ser2); 
        
        qn = new QName(QNAME, "ItemPriceDTOEx");
        ser1 = new BeanSerializerFactory(
                ItemPriceDTOEx.class, qn);
        ser2 = new BeanDeserializerFactory(
                ItemPriceDTOEx.class, qn);
        call.registerTypeMapping(ItemPriceDTOEx.class, qn, ser1, ser2);
        
        qn = new QName(QNAME, "UserTransitionResponseWS");
        ser1 = new BeanSerializerFactory(
                UserTransitionResponseWS.class, qn);
        ser2 = new BeanDeserializerFactory (
                UserTransitionResponseWS.class, qn);
        call.registerTypeMapping(UserTransitionResponseWS.class, 
        		qn, ser1, ser2);
	}
	

	public Integer applyPayment(PaymentWS payment, Integer invoiceId)
			throws JbillingAPIException {
        Integer ret = (Integer)invokeAxisCall(WebServicesConstants.APPLY_PAYMENT,
        					new Object[]{payment,invoiceId});
		return ret;
	}

	public CreateResponseWS create(UserWS user, OrderWS order)
			throws JbillingAPIException {
        CreateResponseWS mcRet = (CreateResponseWS) invokeAxisCall(
            WebServicesConstants.CREATE,new Object[] { user, order} );
        return mcRet;
	}

	public Integer createItem(ItemDTOEx dto) throws JbillingAPIException {
        Integer ret = (Integer)invokeAxisCall(WebServicesConstants.CREATE_ITEM,
    		new Object[]{dto});
        return ret;
	}

	public Integer createOrder(OrderWS order) throws JbillingAPIException {
		Integer ret = (Integer)invokeAxisCall(WebServicesConstants.CREATE_ORDER,
			new Object[]{order});
		return ret;
	}

	public PaymentAuthorizationDTOEx createOrderPreAuthorize(OrderWS order)
			throws JbillingAPIException {
		PaymentAuthorizationDTOEx ret = (PaymentAuthorizationDTOEx)invokeAxisCall(
			WebServicesConstants.CREATE_ORDER_PREAUTH,new Object[]{order});
		return ret;
	}

	public Integer createUser(UserWS newUser) throws JbillingAPIException {
		Integer ret = (Integer)invokeAxisCall(WebServicesConstants.CREATE_USER,
			new Object[]{newUser});
		return ret;
	}

	public void deleteOrder(Integer id) throws JbillingAPIException {
		invokeAxisCall(WebServicesConstants.DELETE_ORDER,new Object[]{id});
	}

	public void deleteUser(Integer userId) throws JbillingAPIException {
		invokeAxisCall(WebServicesConstants.DELETE_USER,new Object[]{userId});
	}

	public ItemDTOEx[] getAllItems() throws JbillingAPIException {
		ItemDTOEx[] items = (ItemDTOEx[]) invokeAxisCall(WebServicesConstants.GET_ALL_ITEMS,
			new Object[]{});
		return items;
	}

	public InvoiceWS getInvoiceWS(Integer invoiceId)
			throws JbillingAPIException {
		InvoiceWS invoice = (InvoiceWS) invokeAxisCall(WebServicesConstants.GET_INVOICE,
			new Object[]{invoiceId});
		return invoice;
	}

	public Integer[] getInvoicesByDate(String since, String until)
			throws JbillingAPIException {
		int[] ret = (int[])invokeAxisCall(WebServicesConstants.GET_INVOICES_BY_DATE,
			new Object[]{since,until});
		Integer[] invoiceIds = convertToIntegerArray(ret);
		return invoiceIds;
	}

	public Integer[] getLastInvoices(Integer userId, Integer number)
			throws JbillingAPIException {
		int[] ret = (int[])invokeAxisCall(WebServicesConstants.GET_LAST_INVOICES,
			new Object[]{userId,number});
		Integer[] invoiceIds = convertToIntegerArray(ret);
		return invoiceIds;
	}

	public Integer[] getLastOrders(Integer userId, Integer number)
			throws JbillingAPIException {
		int[] ret = (int[])invokeAxisCall(WebServicesConstants.GET_LAST_ORDERS,
			new Object[]{userId,number});
		Integer[] orderIds = convertToIntegerArray(ret);
		return orderIds;
	}

	public Integer[] getLastPayments(Integer userId, Integer number)
			throws JbillingAPIException {
        int retPayments[] = (int[]) invokeAxisCall(WebServicesConstants.GET_LAST_PAYMENTS,
        	new Object[]{userId,number});
        Integer[] payments = convertToIntegerArray(retPayments);
		return payments;
}

	public InvoiceWS getLatestInvoice(Integer userId)
			throws JbillingAPIException {
		InvoiceWS invoice = (InvoiceWS) invokeAxisCall(WebServicesConstants.GET_LATEST_INVOICE,
			new Object[]{userId});
		return invoice;
	}

	public OrderWS getLatestOrder(Integer userId) throws JbillingAPIException {
		OrderWS order = (OrderWS) invokeAxisCall(WebServicesConstants.GET_LATEST_ORDER,
			new Object[]{userId});
		return order;
	}

	public PaymentWS getLatestPayment(Integer userId)
			throws JbillingAPIException {
		PaymentWS payment = (PaymentWS) invokeAxisCall(WebServicesConstants.GET_LATEST_PAYMENT,
			new Object[]{userId});
		return payment;
	}

	public OrderWS getOrder(Integer orderId) throws JbillingAPIException {
		OrderWS order = (OrderWS) invokeAxisCall(WebServicesConstants.GET_ORDER,
			new Object[]{orderId});
		return order;
	}

	public Integer[] getOrderByPeriod(Integer userId, Integer periodId)
			throws JbillingAPIException {
		int[] orderIds = (int[]) invokeAxisCall(WebServicesConstants.GET_ORDER_BY_PERIOD,
			new Object[]{userId,periodId});
		Integer[] ret = convertToIntegerArray(orderIds);
		return ret;
	}

	public OrderLineWS getOrderLine(Integer orderLineId)
			throws JbillingAPIException {
		OrderLineWS orderLine = (OrderLineWS) invokeAxisCall(WebServicesConstants.GET_ORDER_LINE,
			new Object[]{orderLineId});
		return orderLine;
	}

	public PaymentWS getPayment(Integer paymentId) throws JbillingAPIException {
		PaymentWS payment = (PaymentWS) invokeAxisCall(WebServicesConstants.GET_PAYMENT,
			new Object[]{paymentId});
		return payment;
	}

	public ContactWS[] getUserContactsWS(Integer userId)
			throws JbillingAPIException {
		return null;
	}

	public Integer getUserId(String username) throws JbillingAPIException {
		Integer ret = (Integer) invokeAxisCall(WebServicesConstants.GET_USER_ID,
			new Object[]{username});
		return ret;
	}

	public UserTransitionResponseWS[] getUserTransitions(Date from, Date to)
			throws JbillingAPIException {
		UserTransitionResponseWS[] response = (UserTransitionResponseWS[])
			invokeAxisCall(WebServicesConstants.GET_USER_TRANSITIONS,
				new Object[]{from,to});
		return response;
	}

	public UserWS getUserWS(Integer userId) throws JbillingAPIException {
		UserWS user = (UserWS) invokeAxisCall(WebServicesConstants.GET_USER,
			new Object[]{userId});
		return user;
	}

	public Integer[] getUsersByCustomField(Integer typeId, String value)
			throws JbillingAPIException {
		int[] ret = (int[]) invokeAxisCall(WebServicesConstants.GET_USERS_BY_CUSTOM_FIELD,
			new Object[]{typeId,value});
		Integer[] userIds = convertToIntegerArray(ret);
		return userIds;
	}

	public Integer[] getUsersInStatus(Integer statusId)
			throws JbillingAPIException {
		int[] ret = (int[]) invokeAxisCall(WebServicesConstants.GET_USERS_IN_STATUS,
			new Object[]{statusId});
		Integer[] userIds = convertToIntegerArray(ret);
		return userIds;
	}

	public Integer[] getUsersNotInStatus(Integer statusId)
			throws JbillingAPIException {
		int[] ret = (int[]) invokeAxisCall(WebServicesConstants.GET_USERS_NOT_IN_STATUS,
			new Object[]{statusId});
		Integer[] userIds = convertToIntegerArray(ret);
		return userIds;
	}

	public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId)
			throws JbillingAPIException {
		PaymentAuthorizationDTOEx paymentAuth = (PaymentAuthorizationDTOEx)invokeAxisCall(
			WebServicesConstants.PAY_INVOICE,new Object[]{invoiceId});
		return paymentAuth;
	}

	public void updateCreditCard(Integer userId, CreditCardDTO creditCard)
			throws JbillingAPIException {
		invokeAxisCall(WebServicesConstants.UPDATE_CREDIT_CARD,new Object[]{userId,creditCard});
	}

	public void updateOrder(OrderWS order) throws JbillingAPIException {
		invokeAxisCall(WebServicesConstants.UPDATE_ORDER,new Object[]{order});
	}

	public void updateOrderLine(OrderLineWS line) throws JbillingAPIException {
		invokeAxisCall(WebServicesConstants.UPDATE_ORDERLINE,new Object[]{line});
	}

	public void updateUser(UserWS user) throws JbillingAPIException {
		invokeAxisCall(WebServicesConstants.UPDATE_USER,new Object[]{user});
	}

	public void updateUserContact(Integer userId, Integer typeId,
			ContactWS contact) throws JbillingAPIException {
		invokeAxisCall(WebServicesConstants.UPDATE_USER_CONTACT,new Object[]{userId,typeId,
			contact});
	}
	
	private Integer[] convertToIntegerArray(int[] array){
		Integer[] ret = new Integer[array.length];
		for(int i=0;i<array.length;i++){
			ret[i] = array[i];
		}
		return ret;
	}
	
	/**
	 * Invokes a web service call using AXIS.  
	 * @param operationName end point Web service operation to call.
	 * @param params parameters to the web service call being invoked.
	 * @return
	 * @throws JbillingAPIException if there are any errors during invocation of
	 *       web service call.
	 */
	private Object invokeAxisCall(String operationName,Object[] params) throws JbillingAPIException{
	    LOG.debug("Invoking web service call " + operationName +
					" using parameters " + getParametersInfo(params));
		try{
			call.setOperationName(operationName);
			Object ret =  call.invoke(params);
			LOG.debug("Got back " + ret + " from web service call");
			return ret;
		}catch(Throwable t){
			throw new JbillingAPIException("Error during invocation of web service " 
				+ operationName + " using parameters " + getParametersInfo(params) +
				". Underlying error message is " + t.getMessage());
		}
	}
	
	/**
	 * Returns a String built from parameters supplied to the AXIS api call. This
	 * is for debugging/ logging purposes only.
	 * @param params
	 * @return
	 */
	private String getParametersInfo(Object[] params){
		StringBuffer sb = new StringBuffer("[");
		for(int i=0;i<params.length;i++){
			sb.append(params[i]);
		}
		sb.append("]");
		return sb.toString();
	}
}
