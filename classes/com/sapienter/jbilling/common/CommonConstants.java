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

package com.sapienter.jbilling.common;

import java.math.BigDecimal;

/**
 * @author Emil
 */
public interface CommonConstants {
    public static final String LIST_TYPE_ITEM_TYPE = "type";
    public static final String LIST_TYPE_CUSTOMER = "customer";
    public static final String LIST_TYPE_CUSTOMER_SIMPLE = "customerSimple";
    public static final String LIST_TYPE_PARTNERS_CUSTOMER = "partnersCustomer";
    public static final String LIST_TYPE_SUB_ACCOUNTS = "sub_accounts";
    public static final String LIST_TYPE_ITEM = "item";
    public static final String LIST_TYPE_ITEM_ORDER = "itemOrder";
    public static final String LIST_TYPE_ITEM_USER_PRICE = "price";
    public static final String LIST_TYPE_PROMOTION = "promotion";
    public static final String LIST_TYPE_PAYMENT = "payment";
    public static final String LIST_TYPE_PAYMENT_USER = "paymentUser";
    public static final String LIST_TYPE_ORDER = "order";
    public static final String LIST_TYPE_INVOICE = "invoice";
    public static final String LIST_TYPE_REFUND = "refund";
    public static final String LIST_TYPE_INVOICE_GRAL = "invoiceGeneral";
    public static final String LIST_TYPE_PROCESS = "process";
    public static final String LIST_TYPE_PROCESS_INVOICES = "processInvoices";
    public static final String LIST_TYPE_PROCESS_ORDERS= "processOrders";
    public static final String LIST_TYPE_NOTIFICATION_TYPE= "notificationType";
    public static final String LIST_TYPE_PARTNER = "partner";
    public static final String LIST_TYPE_PAYOUT = "payout";
    public static final String LIST_TYPE_INVOICE_ORDER = "invoicesOrder";
    
    // results from payments
    // this has to by in synch with how the database is initialized
    public static final Integer RESULT_OK = new Integer(1);
    public static final Integer RESULT_FAIL = new Integer(2);
    public static final Integer RESULT_UNAVAILABLE = new Integer(3);   
    public static final Integer RESULT_ENTERED = new Integer(4);   
    
    // user types, these have to by in synch with the user_type table
    // these are needed in the server side and the jsps
    public static final Integer TYPE_INTERNAL = new Integer(1);
    public static final Integer TYPE_ROOT = new Integer(2);
    public static final Integer TYPE_CLERK = new Integer(3);
    public static final Integer TYPE_PARTNER = new Integer(4);
    public static final Integer TYPE_CUSTOMER = new Integer(5);

    // payment methods (db - synch)
    public static final Integer PAYMENT_METHOD_CHEQUE = new Integer(1);
    public static final Integer PAYMENT_METHOD_VISA = new Integer(2);
    public static final Integer PAYMENT_METHOD_MASTERCARD = new Integer(3);
    public static final Integer PAYMENT_METHOD_AMEX = new Integer(4);
    public static final Integer PAYMENT_METHOD_ACH = new Integer(5);
    public static final Integer PAYMENT_METHOD_DISCOVERY = new Integer(6);
    public static final Integer PAYMENT_METHOD_DINERS = new Integer(7);
    public static final Integer PAYMENT_METHOD_PAYPAL = new Integer(8);
 
    // billing process review status
    public static final Integer REVIEW_STATUS_GENERATED = new Integer(1);
    public static final Integer REVIEW_STATUS_APPROVED = new Integer(2);
    public static final Integer REVIEW_STATUS_DISAPPROVED = new Integer(3);
    
    // these are the preference's types. This has to be in synch with the DB
    //public static Integer PREFERENCE_PAYMENT_WITH_PROCESS = new Integer(1); obsolete
    public static Integer PREFERENCE_CSS_LOCATION = new Integer(2);
    public static Integer PREFERENCE_LOGO_LOCATION = new Integer(3);
    public static Integer PREFERENCE_GRACE_PERIOD = new Integer(4);
    public static Integer PREFERENCE_PART_DEF_RATE = new Integer(5);
    public static Integer PREFERENCE_PART_DEF_FEE = new Integer(6);
    public static Integer PREFERENCE_PART_DEF_ONE_TIME = new Integer(7);
    public static Integer PREFERENCE_PART_DEF_PER_UNIT = new Integer(8);
    public static Integer PREFERENCE_PART_DEF_PER_VALUE = new Integer(9);
    public static Integer PREFERENCE_PART_DEF_AUTOMATIC = new Integer(10);
    public static Integer PREFERENCE_PART_DEF_CLERK = new Integer(11);
    public static Integer PREFERENCE_PART_DEF_FEE_CURR = new Integer(12);
    public static Integer PREFERENCE_PAPER_SELF_DELIVERY = new Integer(13);
    public static Integer PREFERENCE_SHOW_NOTE_IN_INVOICE = new Integer(14);
    public static Integer PREFERENCE_DAYS_ORDER_NOTIFICATION_S1 = new Integer(15);
    public static Integer PREFERENCE_DAYS_ORDER_NOTIFICATION_S2 = new Integer(16);
    public static Integer PREFERENCE_DAYS_ORDER_NOTIFICATION_S3 = new Integer(17);
    public static Integer PREFERENCE_INVOICE_PREFIX = new Integer(18);
    public static Integer PREFERENCE_INVOICE_NUMBER = new Integer(19);
    public static Integer PREFERENCE_INVOICE_DELETE = new Integer(20);
    public static Integer PREFERENCE_USE_INVOICE_REMINDERS = new Integer(21);
    public static Integer PREFERENCE_FIRST_REMINDER = new Integer(22);
    public static Integer PREFERENCE_NEXT_REMINDER = new Integer(23);
    public static Integer PREFERENCE_USE_DF_FM = new Integer(24);
    public static Integer PREFERENCE_USE_OVERDUE_PENALTY = new Integer(25);
    public static Integer PREFERENCE_PAGE_SIZE = new Integer(26);
    public static Integer PREFERENCE_USE_ORDER_ANTICIPATION = new Integer(27);
    public static Integer PREFERENCE_PAYPAL_ACCOUNT = new Integer(28);
    public static Integer PREFERENCE_PAYPAL_BUTTON_URL = new Integer(29);
    public static Integer PREFERENCE_URL_CALLBACK = new Integer(30);
    public static Integer PREFERENCE_CONTINUOUS_DATE = new Integer(31);
    public static Integer PREFERENCE_PDF_ATTACHMENT= new Integer(32);
    public static Integer PREFERENCE_ORDER_OWN_INVOICE = new Integer(33);
    public static Integer PREFERENCE_PRE_AUTHORIZE_CC = new Integer(34);
    public static Integer PREFERENCE_ORDER_IN_INVOICE_LINE = new Integer(35);
    public static Integer PREFERENCE_CUSTOMER_CONTACT_EDIT = new Integer(36);

    // order status, in synch with db
    public static final Integer ORDER_STATUS_ACTIVE = new Integer(1);
    public static final Integer ORDER_STATUS_FINISHED = new Integer(2);
    public static final Integer ORDER_STATUS_SUSPENDED = new Integer(3);
    public static final Integer ORDER_STATUS_SUSPENDED_AGEING = new Integer(4);
    
    // invoice delivery method types
    public static final Integer D_METHOD_EMAIL = new Integer(1);
    public static final Integer D_METHOD_PAPER = new Integer(2);
    public static final Integer D_METHOD_EMAIL_AND_PAPER = new Integer(3);
    
    // automatic payment methods
    // how a customer wants to pay in the automatic process
    public static final Integer AUTO_PAYMENT_TYPE_CC = new Integer(1);
    public static final Integer AUTO_PAYMENT_TYPE_ACH =  new Integer(2);
    public static final Integer AUTO_PAYMENT_TYPE_CHEQUE = new Integer(3);
    
    // types of PDF batch generation
    public static final Integer OPERATION_TYPE_CUSTOMER = new Integer(1);
    public static final Integer OPERATION_TYPE_RANGE = new Integer(2);
    public static final Integer OPERATION_TYPE_PROCESS = new Integer(3);
    public static final Integer OPERATION_TYPE_DATE = new Integer(4);
    public static final Integer OPERATION_TYPE_NUMBER = new Integer(5);
    
    // BigDecimal caculation constants
    public static final int BIGDECIMAL_SCALE = 10;
    public static final int BIGDECIMAL_ROUND = BigDecimal.ROUND_HALF_UP;
}
