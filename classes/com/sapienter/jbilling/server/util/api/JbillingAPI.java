/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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
