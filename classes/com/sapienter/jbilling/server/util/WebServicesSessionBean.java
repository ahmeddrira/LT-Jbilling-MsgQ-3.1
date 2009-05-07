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

/*
 * Created on Jan 27, 2005
 * One session bean to expose as a single web service, thus, one wsdl
 */
package com.sapienter.jbilling.server.util;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
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
import com.sapienter.jbilling.server.user.db.CreditCardDTO;

/**
 * @author Emil
 *
 * @ejb:bean name="WebServicesSession"
 *           display-name="A stateless bean for web services"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="both"
 *           jndi-name="com/sapienter/jbilling/server/util/WebServicesSession"
 *           
 * @ejb.transaction type="Never"
 *
 * @ejb:permission role-name = "2" view-type = "remote"
 *           
 * @jboss-net.web-service urn="billing" 
 *                        expose-all="true"
 * @jboss.security-proxy name="com.sapienter.jbilling.server.util.WSMethodSecurityProxy"
 * @jboss.container-configuration name="Remote API"
 */
public class WebServicesSessionBean implements SessionBean {

    private static final Logger LOG = Logger.getLogger(WebServicesSessionBean.class);
    private SessionContext context = null;
    private WebServicesSessionSpringBean session;

    /*
     * INVOICES
     */
    /**
     * @ejb:interface-method view-type="both"
     */
    public InvoiceWS getInvoiceWS(Integer invoiceId)
            throws SessionInternalError {
        setCallerName();
        return session.getInvoiceWS(invoiceId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public InvoiceWS getLatestInvoice(Integer userId)
            throws SessionInternalError {
        setCallerName();
        return session.getLatestInvoice(userId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getLastInvoices(Integer userId, Integer number)
            throws SessionInternalError {
        setCallerName();
        return session.getLastInvoices(userId, number);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getInvoicesByDate(String since, String until)
            throws SessionInternalError {
        setCallerName();
        return session.getInvoicesByDate(since, until);
    }

    /**
     * Deletes an invoice 
     * @ejb:interface-method view-type="both"
     * @param invoiceId
     * The id of the invoice to delete
     */
    public void deleteInvoice(Integer invoiceId) {
        setCallerName();
        session.deleteInvoice(invoiceId);
    }

    /*
     * USERS
     */
    /**
     * Creates a new user. The user to be created has to be of the roles customer
     * or partner.
     * The username has to be unique, otherwise the creating won't go through. If 
     * that is the case, the return value will be null.
     * @ejb:interface-method view-type="both"
     * @param newUser 
     * The user object with all the information of the new user. If contact or 
     * credit card information are present, they will be included in the creation
     * although they are not mandatory.
     * @return The id of the new user, or null if non was created
     */
    public Integer createUser(UserWS newUser)
            throws SessionInternalError {
        setCallerName();
        return session.createUser(newUser);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public void deleteUser(Integer userId)
            throws SessionInternalError {
        setCallerName();
        session.deleteUser(userId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public void updateUserContact(Integer userId, Integer typeId,
            ContactWS contact)
            throws SessionInternalError {
        setCallerName();
        session.updateUserContact(userId, typeId, contact);
    }

    /**
     * @ejb:interface-method view-type="both"
     * @param user 
     */
    public void updateUser(UserWS user)
            throws SessionInternalError {
        setCallerName();
        session.updateUser(user);
    }

    /**
     * Retrieves a user with its contact and credit card information. 
     * @ejb:interface-method view-type="both"
     * @param userId
     * The id of the user to be returned
     */
    public UserWS getUserWS(Integer userId)
            throws SessionInternalError {
        setCallerName();
        return session.getUserWS(userId);
    }

    /**
     * Retrieves aall the contacts of a user 
     * @ejb:interface-method view-type="both"
     * @param userId
     * The id of the user to be returned
     */
    public ContactWS[] getUserContactsWS(Integer userId)
            throws SessionInternalError {
        setCallerName();
        return session.getUserContactsWS(userId);
    }

    /**
     * Retrieves the user id for the given username 
     * @ejb:interface-method view-type="both"
     */
    public Integer getUserId(String username)
            throws SessionInternalError {
        setCallerName();
        return session.getUserId(username);
    }

    /**
     * Retrieves an array of users in the required status 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getUsersInStatus(Integer statusId)
            throws SessionInternalError {
        setCallerName();
        return session.getUsersInStatus(statusId);
    }

    /**
     * Retrieves an array of users in the required status 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getUsersNotInStatus(Integer statusId)
            throws SessionInternalError {
        setCallerName();
        return session.getUsersNotInStatus(statusId);
    }

    /**
     * Retrieves an array of users in the required status 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getUsersByCustomField(Integer typeId, String value)
            throws SessionInternalError {
        setCallerName();
        return session.getUsersByCustomField(typeId, value);
    }
    
    /**
     * Retrieves an array of users in the required status 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getUsersByCreditCard(String number)
            throws SessionInternalError {
        setCallerName();
        return session.getUsersByCreditCard(number);
    }

    /**
     * Retrieves an array of users in the required status 
     */
    public Integer[] getUsersByStatus(Integer statusId, Integer entityId,
            boolean in)
            throws SessionInternalError {
        setCallerName();
        return session.getUsersByStatus(statusId, entityId, in);
    }

    /**
     * Creates a user, then an order for it, an invoice out the order
     * and tries the invoice to be paid by an online payment
     * This is ... the mega call !!! 
     * @ejb:interface-method view-type="both"
     */
    public CreateResponseWS create(UserWS user, OrderWS order)
            throws SessionInternalError {
        setCallerName();
        return session.create(user, order);
    }

    /**
     * Validates the credentials and returns if the user can login or not
     * @param username
     * @param password
     * @return
     * 0 if the user can login (success), or grater than 0 if the user can not login.
     * See the constants in WebServicesConstants (AUTH*) for details.
     * @throws SessionInternalError
     * 
     * @ejb:interface-method view-type="both"
     */
    public Integer authenticate(String username, String password)
            throws SessionInternalError {
        setCallerName();
        return session.authenticate(username, password);
    }

    /**
     * Pays given invoice, using the first credit card available for invoice'd
     * user.
     * 
     * @return <code>null</code> if invoice has not positive balance, or if
     *         user does not have credit card
     * @return resulting authorization record. The payment itself can be found by
     * calling getLatestPayment
     *  
     * @ejb:interface-method view-type="both"
     */
    public PaymentAuthorizationDTOEx payInvoice(Integer invoiceId) throws SessionInternalError {
        setCallerName();
        return session.payInvoice(invoiceId);
    }

    /**
     * Updates a user's credit card.
     * @ejb:interface-method view-type="both"
     * @param userId
     * The id of the user updating credit card data.
     * @param creditCard
     * The credit card data to be updated. 
     */
    public void updateCreditCard(Integer userId, com.sapienter.jbilling.server.entity.CreditCardDTO creditCard)
            throws SessionInternalError {
        setCallerName();
        session.updateCreditCard(userId, creditCard);
    }

    /*
     * ORDERS
     */
    /**
     * @ejb:interface-method view-type="both"
     * @return the information of the payment aurhotization, or NULL if the 
     * user does not have a credit card
     */
    public PaymentAuthorizationDTOEx createOrderPreAuthorize(OrderWS order)
            throws SessionInternalError {
        setCallerName();
        return session.createOrderPreAuthorize(order);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer createOrder(OrderWS order)
            throws SessionInternalError {
        setCallerName();
        return session.createOrder(order);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public OrderWS rateOrder(OrderWS order)
            throws SessionInternalError {
        setCallerName();
        return session.rateOrder(order);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public void updateItem(ItemDTOEx item) {
        setCallerName();
        session.updateItem(item);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer createOrderAndInvoice(OrderWS order)
            throws SessionInternalError {
        setCallerName();
        return session.createOrderAndInvoice(order);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public void updateOrder(OrderWS order)
            throws SessionInternalError {
        setCallerName();
        session.updateOrder(order);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public OrderWS getOrder(Integer orderId)
            throws SessionInternalError {
        setCallerName();
        return session.getOrder(orderId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getOrderByPeriod(Integer userId, Integer periodId)
            throws SessionInternalError {
        setCallerName();
        return session.getOrderByPeriod(userId, periodId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public OrderLineWS getOrderLine(Integer orderLineId)
            throws SessionInternalError {
        setCallerName();
        return session.getOrderLine(orderLineId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public void updateOrderLine(OrderLineWS line)
            throws SessionInternalError {
        setCallerName();
        session.updateOrderLine(line);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public OrderWS getLatestOrder(Integer userId)
            throws SessionInternalError {
        setCallerName();
        return session.getLatestOrder(userId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getLastOrders(Integer userId, Integer number)
            throws SessionInternalError {
        setCallerName();
        return session.getLastOrders(userId, number);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public void deleteOrder(Integer id)
            throws SessionInternalError {
        setCallerName();
        session.deleteOrder(id);
    }

    /*
     * PAYMENT
     */
    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer applyPayment(PaymentWS payment, Integer invoiceId)
            throws SessionInternalError {
        setCallerName();
        return session.applyPayment(payment, invoiceId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public PaymentWS getPayment(Integer paymentId)
            throws SessionInternalError {
        setCallerName();
        return session.getPayment(paymentId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public PaymentWS getLatestPayment(Integer userId)
            throws SessionInternalError {
        setCallerName();
        return session.getLatestPayment(userId);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getLastPayments(Integer userId, Integer number)
            throws SessionInternalError {
        setCallerName();
        return session.getLastPayments(userId, number);
    }

    /*
     * ITEM
     */
    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer createItem(ItemDTOEx item)
            throws SessionInternalError {
        setCallerName();
        return session.createItem(item);
    }

    /**
     * Retrieves an array of items for the caller's entity. 
     * @ejb:interface-method view-type="both"
     * @return an array of items from the caller's entity
     */
    public ItemDTOEx[] getAllItems() throws SessionInternalError {
        setCallerName();
        return session.getAllItems();
    }

    /**
     * @ejb.interface-method view-type="both"
     * Implementation of the User Transitions List webservice. This accepts a
     * start and end date as arguments, and produces an array of data containing
     * the user transitions logged in the requested time range.
     * @param from Date indicating the lower limit for the extraction of transition
     * logs. It can be <code>null</code>, in such a case, the extraction will start
     * where the last extraction left off. If no extractions have been done so far and
     * this parameter is null, the function will extract from the oldest transition
     * logged.
     * @param to Date indicatin the upper limit for the extraction of transition logs.
     * It can be <code>null</code>, in which case the extraction will have no upper
     * limit. 
     * @return UserTransitionResponseWS[] an array of objects containing the result
     * of the extraction, or <code>null</code> if there is no data thas satisfies
     * the extraction parameters.
     */
    public UserTransitionResponseWS[] getUserTransitions(Date from, Date to)
            throws SessionInternalError {
        setCallerName();
        return session.getUserTransitions(from, to);
    }

    /**
     * @ejb.interface-method view-type="both"
     * @return UserTransitionResponseWS[] an array of objects containing the result
     * of the extraction, or <code>null</code> if there is no data thas satisfies
     * the extraction parameters.
     */
    public UserTransitionResponseWS[] getUserTransitionsAfterId(Integer id)
            throws SessionInternalError {
        setCallerName();
        return session.getUserTransitionsAfterId(id);
    }

    /**
     * @ejb.interface-method view-type="both"
     */
    public ItemDTOEx getItem(Integer itemId, Integer userId, String pricing) {
        setCallerName();
        return session.getItem(itemId, userId, pricing);
    }

    // EJB methods
    public void ejbCreate() throws CreateException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext arg0) throws EJBException,
            RemoteException {
        context = arg0;
        session = (WebServicesSessionSpringBean) Context.getBean(
                Context.Name.WEB_SERVICES_SESSION);
    }

    private void setCallerName() {
        session.setCallerName(context.getCallerPrincipal().getName());
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public InvoiceWS getLatestInvoiceByItemType(Integer userId, Integer itemTypeId)
            throws SessionInternalError {
        return session.getLatestInvoiceByItemType(userId, itemTypeId);
    }

    /**
     * Return 'number' most recent invoices that contain a line item with an
     * item of the given item type.
     * 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getLastInvoicesByItemType(Integer userId, Integer itemTypeId, Integer number)
            throws SessionInternalError {
        return session.getLastInvoicesByItemType(userId, itemTypeId, number);
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public OrderWS getLatestOrderByItemType(Integer userId, Integer itemTypeId)
            throws SessionInternalError {
        setCallerName();
        return session.getLatestOrderByItemType(userId, itemTypeId);
    }
 
    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getLastOrdersByItemType(Integer userId, Integer itemTypeId, Integer number)
            throws SessionInternalError {
        setCallerName();
        return session.getLastOrdersByItemType(userId, itemTypeId, number);
    }

}
