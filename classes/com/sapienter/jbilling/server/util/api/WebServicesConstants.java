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

/**
 * Constants used by AxisAPI.
 * @author Narinder
 *
 */
public class WebServicesConstants {
	public final static String APPLY_PAYMENT = "applyPayment";
	public final static String CREATE = "create";
	public final static String CREATE_ITEM = "createItem";
	public final static String CREATE_ORDER = "createOrder";
    public final static String CREATE_ORDER_AND_INVOICE = "createOrderAndInvoice";
	public final static String CREATE_USER = "createUser";
	public final static String DELETE_ORDER = "deleteOrder";
	public final static String DELETE_USER = "deleteUser";
    public final static String DELETE_INVOICE = "deleteInvoice";
	public final static String GET_ALL_ITEMS = "getAllItems";
	public final static String GET_INVOICES_BY_DATE = "getInvoicesByDate";
	public final static String GET_INVOICE = "getInvoiceWS";
	public final static String GET_LAST_INVOICES = "getLastInvoices";
	public final static String GET_LATEST_INVOICE = "getLatestInvoice";
	public final static String GET_LAST_ORDERS = "getLastOrders";
	public final static String GET_LATEST_ORDER = "getLatestOrder";
	public final static String GET_LAST_PAYMENTS = "getLastPayments";
	public final static String GET_LATEST_PAYMENT = "getLatestPayment";
	public final static String GET_ORDER = "getOrder";
	public final static String GET_ORDER_LINE = "getOrderLine";
	public final static String GET_ORDER_BY_PERIOD = "getOrderByPeriod";
	public final static String GET_PAYMENT = "getPayment";
	public final static String GET_USER_TRANSITIONS = "getUserTransitions";
    public final static String GET_USER_TRANSITIONS_AFTER_ID = "getUserTransitionsAfterId";
	public final static String GET_USER = "getUserWS";
	public final static String GET_USER_ID = "getUserId";
	public final static String GET_USERS_BY_CUSTOM_FIELD = "getUsersByCustomField";
	public final static String GET_USERS_IN_STATUS = "getUsersInStatus";
	public final static String GET_USERS_NOT_IN_STATUS = "getUsersNotInStatus";
	public final static String CREATE_ORDER_PREAUTH = "createOrderPreAuthorize";
	public final static String UPDATE_CREDIT_CARD = "updateCreditCard";
	public final static String UPDATE_USER = "updateUser";
	public final static String UPDATE_ORDER = "updateOrder";
	public final static String UPDATE_ORDERLINE = "updateOrderLine";
	public final static String UPDATE_USER_CONTACT = "updateUserContact";
	public final static String PAY_INVOICE = "payInvoice";
    public final static String AUTHENTICATE = "authenticate";
    public final static String GET_USERS_BY_CCNUMBER = "getUsersByCreditCard";
    
    /*
     *  return values for authentication method
     */
    // ok
    public final static Integer AUTH_OK = new Integer(0);
    // invalid user name or password
    public final static Integer AUTH_WRONG_CREDENTIALS = new Integer(1);
    // same as previous, but on this attempt the password has be changed
    // to lock the account
    public final static Integer AUTH_LOCKED = new Integer(2);
    // the password is good, but too old. Needs to call update
    public final static Integer AUTH_EXPIRED = new Integer(3);
}
