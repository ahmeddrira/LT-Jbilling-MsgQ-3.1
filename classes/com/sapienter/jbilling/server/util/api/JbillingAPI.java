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

import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.CreateResponseWS;
import com.sapienter.jbilling.server.user.UserTransitionResponseWS;
import com.sapienter.jbilling.server.user.UserWS;

public interface JbillingAPI {
    public InvoiceWS getInvoiceWS(Integer invoiceId)
            throws JbillingAPIException;

    public InvoiceWS getLatestInvoice(Integer userId)
            throws JbillingAPIException;

    public Integer[] getLastInvoices(Integer userId, Integer number)
            throws JbillingAPIException;

    public Integer[] getInvoicesByDate(String since, String until)
            throws JbillingAPIException;

    public Integer createUser(UserWS newUser) throws JbillingAPIException;

    public void deleteUser(Integer userId) throws JbillingAPIException;

    public void deleteInvoice(Integer invoiceId) throws JbillingAPIException;

    public void updateUserContact(Integer userId, Integer typeId,
            ContactWS contact) throws JbillingAPIException;

    public void updateUser(UserWS user) throws JbillingAPIException;

    public UserWS getUserWS(Integer userId) throws JbillingAPIException;

    public ContactWS[] getUserContactsWS(Integer userId)
            throws JbillingAPIException;

    public Integer getUserId(String username) throws JbillingAPIException;

    public Integer[] getUsersInStatus(Integer statusId)
            throws JbillingAPIException;

    public Integer[] getUsersByCreditCard(String number)
        throws JbillingAPIException;

    public Integer[] getUsersNotInStatus(Integer statusId)
            throws JbillingAPIException;

    public Integer[] getUsersByCustomField(Integer typeId, String value)
            throws JbillingAPIException;

    public CreateResponseWS create(UserWS user, OrderWS order)
            throws JbillingAPIException;

    public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId)
            throws JbillingAPIException;

    public void updateCreditCard(Integer userId, CreditCardDTO creditCard)
            throws JbillingAPIException;

    public PaymentAuthorizationDTOEx createOrderPreAuthorize(OrderWS order)
            throws JbillingAPIException;

    public Integer createOrder(OrderWS order) throws JbillingAPIException;

    public Integer createOrderAndInvoice(OrderWS order) throws JbillingAPIException;

    public void updateOrder(OrderWS order) throws JbillingAPIException;

    public OrderWS getOrder(Integer orderId) throws JbillingAPIException;

    public Integer[] getOrderByPeriod(Integer userId, Integer periodId)
            throws JbillingAPIException;

    public OrderLineWS getOrderLine(Integer orderLineId)
            throws JbillingAPIException;

    public void updateOrderLine(OrderLineWS line) throws JbillingAPIException;

    public OrderWS getLatestOrder(Integer userId) throws JbillingAPIException;

    public Integer[] getLastOrders(Integer userId, Integer number)
            throws JbillingAPIException;

    public void deleteOrder(Integer id) throws JbillingAPIException;

    public Integer applyPayment(PaymentWS payment, Integer invoiceId)
            throws JbillingAPIException;

    public PaymentWS getPayment(Integer paymentId) throws JbillingAPIException;

    public PaymentWS getLatestPayment(Integer userId)
            throws JbillingAPIException;

    public Integer[] getLastPayments(Integer userId, Integer number)
            throws JbillingAPIException;

    public Integer createItem(ItemDTOEx dto) throws JbillingAPIException;

    public ItemDTOEx[] getAllItems() throws JbillingAPIException;

    public UserTransitionResponseWS[] getUserTransitions(Date from, Date to)
            throws JbillingAPIException;

    public UserTransitionResponseWS[] getUserTransitionsAfterId(Integer id)
            throws JbillingAPIException;

    public Integer authenticate(String username, String password)
            throws JbillingAPIException;
}
