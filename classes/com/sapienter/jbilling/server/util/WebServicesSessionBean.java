/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.commons.validator.ValidatorException;
import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.GatewayBL;
import com.sapienter.jbilling.common.JBCrypto;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.invoice.InvoiceWS;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.ItemSessionBean;
import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderDTOEx;
import com.sapienter.jbilling.server.order.OrderLineWS;
import com.sapienter.jbilling.server.order.OrderWS;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentBL;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.payment.PaymentSessionBean;
import com.sapienter.jbilling.server.payment.PaymentWS;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.process.BillingProcessBL;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;
import com.sapienter.jbilling.server.user.ContactWS;
import com.sapienter.jbilling.server.user.CreateResponseWS;
import com.sapienter.jbilling.server.user.CreditCardBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.UserSessionBean;
import com.sapienter.jbilling.server.user.UserTransitionResponseWS;
import com.sapienter.jbilling.server.user.UserWS;
import com.sapienter.jbilling.server.util.api.WebServicesConstants;

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
    
    // used only to know if an invoice was created in the mega call
    private Integer invoiceId = null;
    

    /*
     * INVOICES
     */
    
    /**
     * @ejb:interface-method view-type="both"
     */
    public InvoiceWS getInvoiceWS(Integer invoiceId)
            throws SessionInternalError {
        try {
            if (invoiceId == null) {
                return null;
            }
            InvoiceBL bl = new InvoiceBL(invoiceId);
            if (bl.getEntity().getDeleted().equals(new Integer(1)) || 
                    bl.getEntity().getIsReview().equals(new Integer(1))) {
                return null;
            }

            return bl.getWS();
        } catch (Exception e) {
            LOG.error("WS - getInvoiceWS", e);
            throw new SessionInternalError("Error getting invoice");
        }
    }
    
    /**
     * @ejb:interface-method view-type="both"
     */
    public InvoiceWS getLatestInvoice(Integer userId)
            throws SessionInternalError {
        InvoiceWS retValue = null;
        try {
            if (userId == null) {
                return null;
            }
            InvoiceBL bl = new InvoiceBL();
            Integer invoiceId = bl.getLastByUser(userId);
            if (invoiceId != null) {
                bl.set(invoiceId);
                retValue = bl.getWS();
            }
            return retValue;
        } catch (Exception e) {
            LOG.error("Exception in web service: getting latest invoice" +
                    " for user " + userId, e);
            throw new SessionInternalError("Error getting latest invoice");
        }
    }

    /**
     * @ejb:interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public Integer[] getLastInvoices(Integer userId, Integer number)
            throws SessionInternalError {
        try {
            if (userId == null || number == null) {
                return null;
            }
            
            InvoiceBL bl = new InvoiceBL();
            return bl.getManyWS(userId, number);
        } catch (Exception e) {
            LOG.error("Exception in web service: getting last invoices" +
                    " for user " + userId, e);
            throw new SessionInternalError("Error getting last invoices");
        }
    }

     /**
     * @ejb:interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public Integer[] getInvoicesByDate(String since, String until)
            throws SessionInternalError {
        try {
            Date dSince = com.sapienter.jbilling.common.Util.parseDate(since);
            Date dUntil = com.sapienter.jbilling.common.Util.parseDate(until);
            if (since == null || until == null) {
                return null;
            }
            
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId()); 

            InvoiceBL invoiceBl = new InvoiceBL();
            return invoiceBl.getInvoicesByCreateDateArray(entityId, dSince, dUntil);
        } catch (Exception e) {
            LOG.error("Exception in web service: getting invoices by date" +
                    since + until, e);
            throw new SessionInternalError("Error getting last invoices");
        }
    }
    
    /**
     * Deletes an invoice 
     * @ejb:interface-method view-type="both"
     * @param invoiceId
     * The id of the invoice to delete
     */
    public void deleteInvoice(Integer invoiceId) {
        try {
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer executorId = bl.getEntity().getUserId();
            InvoiceBL invoice = new InvoiceBL(invoiceId);
            invoice.delete(executorId);
        } catch(Exception e) {
            LOG.error("WS - deleteUser", e);
            throw new SessionInternalError("Error deleting user");
        }
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

        validateUser(newUser);

        try {
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId()); 
            LOG.info("WS - Creating user " + newUser);
            
            if (!bl.exists(newUser.getUserName(), entityId)) {
                
                ContactBL cBl = new ContactBL();
                UserDTOEx dto = new UserDTOEx(newUser, entityId);
                Integer userId = bl.create(dto);
                if (newUser.getContact() != null) {
                    cBl.createPrimaryForUser(new ContactDTOEx(
                            newUser.getContact()), userId, entityId);
                }
                
                if (newUser.getCreditCard() != null) {
                    CreditCardBL ccBL = new CreditCardBL();
                    ccBL.create(newUser.getCreditCard());
                    bl.getEntity().getCreditCard().add(ccBL.getEntity());
                }
                return userId;
            } 
            return null;
        // need to catch every single one to be able to throw inside
        } catch (NamingException e) {
            LOG.error("WS user creation error", e);
            throw new SessionInternalError("Error creating user");
        } catch (FinderException e) {
            LOG.error("WS user creation error", e);
            throw new SessionInternalError("Error creating user");
        } catch (CreateException e) {
            LOG.error("WS user creation error", e);
            throw new SessionInternalError("Error creating user");
        } catch (RemoveException e) {
            LOG.error("WS user creation error", e);
            throw new SessionInternalError("Error creating user");
        }
    }

    
    /**
     * @ejb:interface-method view-type="both"
     */
    public void deleteUser(Integer userId) 
            throws SessionInternalError {
        try {
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer executorId = bl.getEntity().getUserId();
            bl.set(userId);
            bl.delete(executorId);
        } catch(Exception e) {
            LOG.error("WS - deleteUser", e);
            throw new SessionInternalError("Error deleting user");
        }
    }

     /**
     * @ejb:interface-method view-type="both"
     */
    public void updateUserContact(Integer userId, Integer typeId, 
            ContactWS contact)
            throws SessionInternalError {
        
        try {
            UserBL bl = new UserBL();
            
            // get the entity
            bl.setRoot(context.getCallerPrincipal().getName());
            LOG.info("WS - Updating contact for user " + userId);
            
            // update the contact
            ContactBL cBl = new ContactBL();
            cBl.updateForUser(new ContactDTOEx(contact), userId, typeId);
            
        } catch (Exception e) {
            LOG.error("WS - updateUserContact", e);
            throw new SessionInternalError("Error updating contact");
        }
    }
   
    /**
     * @ejb:interface-method view-type="both"
     * @param user 
     */
    public void updateUser(UserWS user) 
            throws SessionInternalError {
        
        validateUser(user);
        
        try {
            UserBL bl = new UserBL();
            
            // get the entity
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());
            Integer executorId =   bl.getEntity().getUserId();
            LOG.info("WS - Updating user " + user);
            
            // Check whether the password changes or not.
            if (user.getPassword() != null) { 
                JBCrypto passwordCryptoService = JBCrypto.getPasswordCrypto(bl.getMainRole());
            	String newPassword = passwordCryptoService.encrypt(user.getPassword());
            	String oldPassword = bl.getEntity().getPassword();
            	if (!newPassword.equals(oldPassword)) {
            		// If the password is changing, validate it
            		if (!bl.validatePassword(user.getPassword())) {
            			throw new SessionInternalError("Error updating user");
            		}
            	}
            }

            // convert to a DTO
            UserDTOEx dto = new UserDTOEx(user, entityId);
            
            // update the user info
            bl.set(user.getUserId());
            bl.update(executorId, dto);
            
            // now update the contact info
            if (user.getContact() != null) {
                ContactBL cBl = new ContactBL();
                cBl.updatePrimaryForUser(new ContactDTOEx(user.getContact()),
                        user.getUserId());
            }
            
            // and the credit card
            if (user.getCreditCard() != null) {
                UserSessionBean sess = new UserSessionBean();
                sess.updateCreditCard(executorId, user.getUserId(), 
                        user.getCreditCard());
            }
            
        } catch (Exception e) {
            LOG.error("WS - updateUser", e);
            throw new SessionInternalError("Error updating user");
        }
    }

    /**
     * Retrieves a user with its contact and credit card information. 
     * @ejb:interface-method view-type="both"
     * @param userId
     * The id of the user to be returned
     */
    public UserWS getUserWS(Integer userId) 
            throws SessionInternalError {
        UserWS dto = null;
        // calling from dot.net seems to not have a context set. So then when calling
        // getCallerPrincipal the client gets a 'No security context set' exception
        // log.debug("principal = " + context.getCallerPrincipal().getName());
        try {
            UserBL bl = new UserBL(userId);
            dto = bl.getUserWS();
        } catch (Exception e) {
            LOG.error("WS - getUserWS", e);
            throw new SessionInternalError("Error getting user");
       }
        
        return dto;
    }
    
    /**
     * Retrieves aall the contacts of a user 
     * @ejb:interface-method view-type="both"
     * @param userId
     * The id of the user to be returned
     */
    public ContactWS[] getUserContactsWS(Integer userId) 
            throws SessionInternalError {
        ContactWS[] dtos = null;
        try {
            ContactBL contact = new ContactBL();
            Vector result = contact.getAll(userId);
            dtos = new ContactWS[result.size()];
            for (int f = 0; f < result.size(); f++) {
                dtos[f] = new ContactWS((ContactDTOEx) result.get(f));
            }
        } catch (Exception e) {
            LOG.error("WS - getUserWS", e);
            throw new SessionInternalError("Error getting user");
       }
        
        return dtos;
    }

    /**
     * Retrieves the user id for the given username 
     * @ejb:interface-method view-type="both"
     */
    public Integer getUserId(String username) 
            throws SessionInternalError {
        // find which entity are we talking here
        String root = context.getCallerPrincipal().getName();
        try {
            UserBL bl = new UserBL();
            bl.setRoot(root);
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());
            bl.set(username, entityId);
            return bl.getEntity().getUserId();
        } catch (Exception e) {
            LOG.error("WS - getUserId", e);
            throw new SessionInternalError("Error getting user id");
        }
        
    }

    /**
     * Retrieves an array of users in the required status 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getUsersInStatus(Integer statusId) 
            throws SessionInternalError {
        // find which entity are we talking here
        String root = context.getCallerPrincipal().getName();
        try {
            UserBL bl = new UserBL();
            bl.setRoot(root);
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());
            return getUsersByStatus(statusId, entityId, true);
        } catch (Exception e) {
            LOG.error("WS - getUsersInStatus", e);
            throw new SessionInternalError("Error getting users in status");
        }
    }

    /**
     * Retrieves an array of users in the required status 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getUsersNotInStatus(Integer statusId) 
            throws SessionInternalError {
        // find which entity are we talking here
        String root = context.getCallerPrincipal().getName();
        try {
            UserBL bl = new UserBL();
            bl.setRoot(root);
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());
            return getUsersByStatus(statusId, entityId, false);
        } catch (Exception e) {
            LOG.error("WS - getUsersNotInStatus", e);
            throw new SessionInternalError("Error getting users not in status");
        }
    }

    /**
     * Retrieves an array of users in the required status 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getUsersByCustomField(Integer typeId, String value) 
            throws SessionInternalError {
        // find which entity are we talking here
        String root = context.getCallerPrincipal().getName();
        try {
            UserBL bl = new UserBL();
            bl.setRoot(root);
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());

            CachedRowSet users = bl.getByCustomField(entityId, typeId, value);
            LOG.debug("got collection. Now converting");
            Integer[] ret = new Integer[users.size()];
            int f = 0;
            while (users.next()) {
                ret[f] = users.getInt(1);
                f++;
            }
            users.close();
            LOG.debug("done");
            return ret;
        } catch (Exception e) {
            LOG.error("WS - getUsersByCustomField", e);
            throw new SessionInternalError("Error getting users by custom field");
        }
    }

    /**
     * Retrieves an array of users in the required status 
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getUsersByCreditCard(String number) 
            throws SessionInternalError {
        // find which entity are we talking here
        String root = context.getCallerPrincipal().getName();
        try {
            UserBL bl = new UserBL();
            bl.setRoot(root);
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());

            CachedRowSet users = bl.getByCCNumber(entityId, number);
            LOG.debug("getUsersByCreditCard - got collection. Now converting");
            Integer[] ret = new Integer[users.size()];
            int f = 0;
            while (users.next()) {
                ret[f] = users.getInt(1);
                f++;
            }
            users.close();
            LOG.debug("done");
            return ret;
        } catch (Exception e) {
            LOG.error("WS - getUsersByCustomField", e);
            throw new SessionInternalError("Error getting users by custom field");
        }
    }

    /**
     * Retrieves an array of users in the required status 
     */
    public Integer[] getUsersByStatus(Integer statusId, Integer entityId, 
            boolean in) 
            throws SessionInternalError {
        try {
            LOG.debug("getting list of users. status:" + statusId +
                    " entity:" + entityId + " in:" + in);
            UserBL bl = new UserBL();
            CachedRowSet users = bl.getByStatus(entityId, statusId, in);
            LOG.debug("got collection. Now converting");
            Integer[] ret = new Integer[users.size()];
            int f = 0;
            while (users.next()) {
                ret[f] = users.getInt(1);
                f++;
            }
            users.close();
            LOG.debug("done");
            return ret;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * Creates a user, then an order for it, an invoice out the order
     * and tries the invoice to be paid by an online payment
     * This is ... the mega call !!! 
     * @ejb:interface-method view-type="both"
     */
    public CreateResponseWS create(UserWS user, OrderWS order) 
            throws SessionInternalError {
    
    	validateCaller();

    	CreateResponseWS retValue = new CreateResponseWS();

    	// the user first
        final Integer userId = createUser(user); 
        retValue.setUserId(userId);
        
        if (userId == null) {
            return retValue;
        }
        
        // the order and (if needed) invoice
        order.setUserId(userId);
        final int orderId = createOrderAndInvoice(order);
        retValue.setOrderId(orderId);
        if (invoiceId != null){
        	//we assume that createOrder have actually created an invoice
        	//it would be better to access it directly from createOrder() 
        	//but we don't want to change API for now
        	//so we will get it indirectly, as the latest invoice
        	
        	Integer lastInvoiceId = invoiceId;
        	retValue.setInvoiceId(lastInvoiceId);
        	
            //the payment, if we have a credit card
            if (user.getCreditCard() != null){
            	InvoiceEntityLocal invoice = findInvoice(lastInvoiceId);
            	PaymentDTOEx payment = doPayInvoice(invoice, user.getCreditCard());
                PaymentAuthorizationDTOEx result = null;
                if (payment != null) {
                    result = new PaymentAuthorizationDTOEx(payment.getAuthorization());
                    result.setResult(payment.getResultId().equals(Constants.RESULT_OK));
                }
        	    retValue.setPaymentResult(result);
        	    retValue.setPaymentId(payment.getId());
            }
        } else {
            throw new SessionInternalError("Invoice expected for order: " + orderId);
        }
            
        return retValue;
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
        Integer retValue = null;
        
        try {
            // the caller will tell us what entity is this
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());
            
            // prepare the DTO for the authentication call
            UserDTOEx user = new UserDTOEx();
            user.setEntityId(entityId);
            user.setUserName(username);
            user.setPassword(password);
            
            // do the authentication
            JNDILookup EJBFactory = JNDILookup.getFactory(false);            
            UserSessionHome UserHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);

            UserSession myRemoteSession = UserHome.create();
            retValue = myRemoteSession.authenticate(user);
            if (retValue.equals(Constants.AUTH_OK)) {
                // see if the password is not expired
                bl.set(user.getUserName(), entityId);
                if (bl.isPasswordExpired()) {
                    retValue = WebServicesConstants.AUTH_EXPIRED;
                } 
            }

        } catch (Exception e) {
            LOG.error("WS - authenticate: ", e);
            throw new SessionInternalError("Error authenticating user");
        } 

        return retValue;
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
    	if (invoiceId == null){
    		throw new SessionInternalError("Can not pay null invoice");
    	}
    	
    	final InvoiceEntityLocal invoice = findInvoice(invoiceId);
		CreditCardDTO creditCard = getCreditCard(invoice.getUser().getUserId());
		if (creditCard == null){
			return null;
		}
		
		PaymentDTOEx payment = doPayInvoice(invoice, creditCard);
        PaymentAuthorizationDTOEx result = null;
        if (payment != null) {
            result = new PaymentAuthorizationDTOEx(payment.getAuthorization());
            result.setResult(payment.getResultId().equals(Constants.RESULT_OK));
        }
		return result;
    }

    /**
     * Updates a user's credit card.
     * @ejb:interface-method view-type="both"
     * @param userId
     * The id of the user updating credit card data.
     * @param creditCard
     * The credit card data to be updated. 
     */
    public void updateCreditCard(Integer userId, CreditCardDTO creditCard)
            throws SessionInternalError {
        try {
            if (creditCard != null && (creditCard.getName() == null || 
                    creditCard.getExpiry() == null)) {
                LOG.debug("WS - updateCreditCard: " 
                        + "credit card validation error.");
                throw new SessionInternalError("Missing cc data.");
            }
            
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer executorId = bl.getEntity().getUserId();
            UserSessionBean sess = new UserSessionBean();
            sess.updateCreditCard(executorId, userId, creditCard); 
            
        } catch (Exception e) {
            LOG.error("WS - updateCreditCard: ", e);
            throw new SessionInternalError("Error updating user's credit card");
        }
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
        
        PaymentAuthorizationDTOEx retValue = null;
        // start by creating the order. It'll do the checks as well
        Integer orderId = createOrder(order);
        
        try {
            Integer userId = order.getUserId();
            CreditCardDTO cc = getCreditCard(userId);
            UserBL user = new UserBL();
            Integer entityId = user.getEntityId(userId);
            if (cc != null) {
                CreditCardBL ccBl = new CreditCardBL();
                // calling for the DTOEx of order seems an overkill to just get
                // the total. However, in a new order most related objects won't 
                // be there (process, invoices, etc).
                OrderDTOEx newOrder = DTOFactory.getOrderDTOEx(orderId, 1);
                retValue = ccBl.validatePreAuthorization(entityId, userId, cc,
                        newOrder.getTotal(), newOrder.getCurrencyId());
            }
        } catch(Exception e) {
            LOG.debug("Exception:", e);
            throw new SessionInternalError("error pre-validating order");
        }
        return retValue;
    }
    
    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer createOrder(OrderWS order) 
            throws SessionInternalError {
    	
    	Integer orderId = doCreateOrder(order);
    	return orderId;
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer createOrderAndInvoice(OrderWS order) 
            throws SessionInternalError {
        
        Integer orderId = doCreateOrder(order);
        doCreateInvoice(orderId);

        return orderId;
    }

    private void processItemLine(OrderWS order, Integer languageId,
            Integer entityId) 
        throws SessionInternalError, PluggableTaskException, TaskException {
        for (OrderLineWS line : order.getOrderLines()) {
            // get the related item
            ItemSessionBean itemSession = new ItemSessionBean();
            
            ItemDTOEx item = itemSession.get(line.getItemId(), 
                    languageId, order.getUserId(), order.getCurrencyId(),
                    entityId);
            line.setItem(item);
            if (line.getUseItem().booleanValue()) {
                if (item.getPrice() == null) {
                	line.setPrice(item.getPercentage());
                } else {
                	line.setPrice(item.getPrice());
                }
                if (line.getDescription() == null || 
                        line.getDescription().length() == 0) {
                    line.setDescription(item.getDescription());
                }
            }
        }
    }
    
    /**
     * @ejb:interface-method view-type="both"
     */
    public void updateOrder(OrderWS order)
            throws SessionInternalError {
        validateOrder(order);
        try {
            // get the info from the caller
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer executorId = bl.getEntity().getUserId();
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());
            Integer languageId = bl.getEntity().getLanguageIdField();
            
            // see if the related items should provide info
            processItemLine(order, languageId, entityId);

            // make a dto out of the ws
            NewOrderDTO dto = new NewOrderDTO(order);
            
            // call the update
            OrderBL orderBL = new OrderBL(dto.getId());
            orderBL.setDTO(dto);
            orderBL.recalculate(entityId);
            orderBL.update(executorId, dto);
            
            
        } catch(Exception e) {
            LOG.error("WS - updateOrder", e);
            throw new SessionInternalError("Error updating order");
        }
    } 

    /**
     * @ejb:interface-method view-type="both"
     */
    public OrderWS getOrder(Integer orderId) 
            throws SessionInternalError {
        try {
            // get the info from the caller
            UserBL userbl = new UserBL();
            userbl.setRoot(context.getCallerPrincipal().getName());
            Integer languageId = userbl.getEntity().getLanguageIdField();
            
            // now get the order
            OrderBL bl = new OrderBL(orderId);
            if (bl.getEntity().getDeleted().equals(new Integer(1))) {
                LOG.debug("Returning deleted order " + orderId);
            }
            return bl.getWS(languageId);
        } catch (Exception e) {
            LOG.error("WS - getOrder", e);
            throw new SessionInternalError("Error getting order");
        }
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer[] getOrderByPeriod(Integer userId, Integer periodId) 
            throws SessionInternalError {
        if (userId == null || periodId == null) {
            return null;
        }
        try {
            // get the info from the caller
            UserBL userbl = new UserBL();
            userbl.setRoot(context.getCallerPrincipal().getName());
            
            // now get the order
            OrderBL bl = new OrderBL();
            return bl.getByUserAndPeriod(userId, periodId);
        } catch (Exception e) {
            LOG.error("WS - getOrderByPeriod", e);
            throw new SessionInternalError("Error getting orders for a user " +
                    "by period");
        }
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public OrderLineWS getOrderLine(Integer orderLineId) 
            throws SessionInternalError {
        try {
            LOG.debug("WS - getOrderLine " + orderLineId);
            // now get the order
            OrderBL bl = new OrderBL();
            OrderLineWS retValue = null;
            
            try {
                retValue = bl.getOrderLineWS(orderLineId);
            } catch (FinderException e) {
                // ok, so a null goes back
            }
            
            return retValue; 
        } catch (Exception e) {
            LOG.error("WS - getOrderLine", e);
            throw new SessionInternalError("Error getting order line");
        }
    }

    /**
     * @ejb:interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public void updateOrderLine(OrderLineWS line) 
            throws SessionInternalError {
        try {
            LOG.debug("WS - updateOrderLine " + line);
            // now get the order
            OrderBL bl = new OrderBL();
            bl.updateOrderLine(line);
        } catch (Exception e) {
            LOG.error("WS - updateOrderLine", e);
            throw new SessionInternalError("Error updating order line");
        }
    }

    
    /**
     * @ejb:interface-method view-type="both"
     */
    public OrderWS getLatestOrder(Integer userId) 
            throws SessionInternalError {
        if (userId == null) {
            throw new SessionInternalError("User id can not be null");
        }
        try {
            OrderWS retValue = null;
            // get the info from the caller
            UserBL userbl = new UserBL();
            userbl.setRoot(context.getCallerPrincipal().getName());
            Integer languageId = userbl.getEntity().getLanguageIdField();
            
            // now get the order
            OrderBL bl = new OrderBL();
            Integer orderId = bl.getLatest(userId);
            if (orderId != null) {
                bl.set(orderId);
                retValue = bl.getWS(languageId);
            }
            return retValue;
        } catch (Exception e) {
            LOG.error("WS - getLatestOrder", e);
            throw new SessionInternalError("Error getting latest order");
        }
    }

    /**
     * @ejb:interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public Integer[] getLastOrders(Integer userId, Integer number)
            throws SessionInternalError {
        if (userId == null || number == null) {
            return null;
        }
        try {
            UserBL userbl = new UserBL();
            userbl.setRoot(context.getCallerPrincipal().getName());
            
            OrderBL order = new OrderBL();
            return order.getListIds(userId, number, userbl.getEntityId(userId));
        } catch (Exception e) {
            LOG.error("WS - getLastOrders", e);
            throw new SessionInternalError("Error getting last orders");
        }
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public void deleteOrder(Integer id) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL(id);
            bl.delete();
        } catch (Exception e) {
            LOG.error("WS - deleteOrder", e);
            throw new SessionInternalError("Error deleting order");
        }

    }
    
    /*
     * PAYMENT
     */
    /**
     * @ejb:interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public Integer applyPayment(PaymentWS payment, Integer invoiceId) 
            throws SessionInternalError {
        validatePayment(payment);
        try {
            payment.setIsRefund(new Integer(0));
            PaymentSessionBean session = new PaymentSessionBean();
            return session.applyPayment(new PaymentDTOEx(payment), invoiceId);
        } catch (Exception e) {
            LOG.error("WS - applyPayment", e);
            throw new SessionInternalError("Error applying payment");
        }
    }  
    
    /**
     * @ejb:interface-method view-type="both"
     */
    public PaymentWS getPayment(Integer paymentId) 
            throws SessionInternalError {
        try {
            // get the info from the caller
            UserBL userbl = new UserBL();
            userbl.setRoot(context.getCallerPrincipal().getName());
            Integer languageId = userbl.getEntity().getLanguageIdField();
            
            PaymentBL bl = new PaymentBL(paymentId);
            return new PaymentWS(bl.getDTOEx(languageId));
        } catch (Exception e) {
            LOG.error("WS - getPayment", e);
            throw new SessionInternalError("Error getting payment");
        }
    }

    /**
     * @ejb:interface-method view-type="both"
     */
    public PaymentWS getLatestPayment(Integer userId) 
            throws SessionInternalError {
        try {
            PaymentWS retValue = null;
            // get the info from the caller
            UserBL userbl = new UserBL();
            userbl.setRoot(context.getCallerPrincipal().getName());
            Integer languageId = userbl.getEntity().getLanguageIdField();
            
            PaymentBL bl = new PaymentBL();
            Integer paymentId = bl.getLatest(userId);
            if (paymentId != null) {
                bl.set(paymentId);
                retValue = new PaymentWS(bl.getDTOEx(languageId));
            }
            return retValue;
        } catch (Exception e) {
            LOG.error("WS - getLatestPayment", e);
            throw new SessionInternalError("Error getting latest payment");
        }
    }

    /**
     * @ejb:interface-method view-type="both"
     * @ejb.transaction type="Required"
     */
    public Integer[] getLastPayments(Integer userId, Integer number)
            throws SessionInternalError {
        if (userId == null || number == null) {
            return null;
        }
        LOG.debug("WS - getLastPayments " + userId + " " + number);
        try {
            UserBL userbl = new UserBL();
            userbl.setRoot(context.getCallerPrincipal().getName());
            Integer languageId = userbl.getEntity().getLanguageIdField();
            
            PaymentBL payment = new PaymentBL();
            return payment.getManyWS(userId, number, languageId);
        } catch (Exception e) {
            LOG.error("WS - getLastPayments", e);
            throw new SessionInternalError("Error getting last payments");
        }
    }

    /*
     * ITEM
     */
    /**
     * @ejb:interface-method view-type="both"
     */
    public Integer createItem(ItemDTOEx dto) 
            throws SessionInternalError {
        if (!ItemBL.validate(dto)) {
            throw new SessionInternalError("invalid argument");
        }
        try {
            // get the info from the caller
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer languageId = bl.getEntity().getLanguageIdField();
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());
            dto.setEntityId(entityId);
            
            // call the creation
            ItemBL itemBL = new ItemBL();
            return itemBL.create(dto, languageId);
            
        } catch(Exception e) {
            LOG.error("WS - createItem", e);
            throw new SessionInternalError("Error creating item");
        }

    }
    
    /**
     * Retrieves an array of items for the caller's entity. 
     * @ejb:interface-method view-type="both"
     * @return an array of items from the caller's entity
     */
    public ItemDTOEx[] getAllItems() throws SessionInternalError {
        try {
            UserBL userBL = new UserBL();
            userBL.setRoot(context.getCallerPrincipal().getName());
            Integer entityId = userBL.getEntityId(userBL.getEntity().getUserId()); 
            ItemBL itemBL = new ItemBL();
            return itemBL.getAllItems(entityId);
        } catch (Exception e) {
            LOG.error("WS - getAllItems", e);
            throw new SessionInternalError("Error getting all items");
        }
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
    	
    	UserTransitionResponseWS[] result = null;
    	Integer last = null;
    	// Obtain the current entity and language Ids
    	
    	try {
    		UserBL user = new UserBL();
    		user.setRoot(context.getCallerPrincipal().getName());
    		Integer callerId  = user.getEntity().getUserId();
            Integer entityId  = user.getEntityId(callerId);
    		EventLogger evLog = EventLogger.getInstance();
    		
    		if (from == null) {
    			last = evLog.getLastTransitionEvent(entityId);
    		}

    		if (last != null) {
    			result = user.getUserTransitionsById(entityId, last, to);
    		} else {
    			result = user.getUserTransitionsByDate(entityId, from, to);
    		}
    		
    		if (result == null) {
    			LOG.info("Data retrieved but resultset is null");
    		} else {
    			LOG.info("Data retrieved. Result size = " + result.length);
    		}

    		// Log the last value returned if there was any. This happens always,
    		// unless the returned array is empty.
    		if (result!= null && result.length > 0) {
    			LOG.info("Registering transition list event");
    			evLog.audit(callerId, Constants.TABLE_EVENT_LOG, callerId, EventLogger.MODULE_WEBSERVICES,
    						EventLogger.USER_TRANSITIONS_LIST, result[result.length - 1].getId(),
    						result[0].getId().toString(), null);
    		}
    	} catch (Exception e) {
    		throw new SessionInternalError("Error accessing database [" + e.getLocalizedMessage() + "]", this.getClass(), e);
    	}
    	return result;
    }

    /**
     * @ejb.interface-method view-type="both"
     * @return UserTransitionResponseWS[] an array of objects containing the result
     * of the extraction, or <code>null</code> if there is no data thas satisfies
     * the extraction parameters.
     */
    public UserTransitionResponseWS[] getUserTransitionsAfterId(Integer id) 
            throws SessionInternalError {
        
        UserTransitionResponseWS[] result = null;
        // Obtain the current entity and language Ids
        
        try {
            UserBL user = new UserBL();
            user.setRoot(context.getCallerPrincipal().getName());
            Integer callerId  = user.getEntity().getUserId();
            Integer entityId = user.getEntityId(callerId);
            EventLogger evLog = EventLogger.getInstance();
            
            result = user.getUserTransitionsById(entityId, id, null);
            
            if (result == null) {
                LOG.info("Data retrieved but resultset is null");
            } else {
                LOG.info("Data retrieved. Result size = " + result.length);
            }

            // Log the last value returned if there was any. This happens always,
            // unless the returned array is empty.
            if (result!= null && result.length > 0) {
                LOG.info("Registering transition list event");
                evLog.audit(callerId, Constants.TABLE_EVENT_LOG, callerId, EventLogger.MODULE_WEBSERVICES,
                            EventLogger.USER_TRANSITIONS_LIST, result[result.length - 1].getId(),
                            result[0].getId().toString(), null);
            }
        } catch (Exception e) {
            throw new SessionInternalError("Error accessing database [" + e.getLocalizedMessage() + "]", this.getClass(), e);
        }
        return result;
    }

    private Integer zero2null(Integer var) {
        if (var != null && var.intValue() == 0) {
            return null;
        } else {
            return var;
        }
    }
    
    private Date zero2null(Date var) {
        if (var != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(var);
            if (cal.get(Calendar.YEAR) == 1) {
                return null;
            }
        }
        
        return var;
        
    }
    
    private void validateUser(UserWS newUser) 
            throws SessionInternalError{
        // do the validation
        if (newUser == null) {
            throw new SessionInternalError("Null parameter");
        }
        // C# sends a 0 when it is null ...
        newUser.setCurrencyId(zero2null(newUser.getCurrencyId()));
        newUser.setPartnerId(zero2null(newUser.getPartnerId()));
        newUser.setParentId(zero2null(newUser.getParentId()));
        newUser.setMainRoleId(zero2null(newUser.getMainRoleId()));
        newUser.setLanguageId(zero2null(newUser.getLanguageId()));
        newUser.setStatusId(zero2null(newUser.getStatusId()));
        // clean up the cc number from spaces and '-'
        if (newUser.getCreditCard() != null && 
                newUser.getCreditCard().getNumber() != null) {
            newUser.getCreditCard().setNumber(CreditCardBL.cleanUpNumber(
                    newUser.getCreditCard().getNumber()));
        }

        try {
            GatewayBL valid = new GatewayBL();
            // the user
            if (!valid.validate("UserWS", newUser)) {
                throw new SessionInternalError(valid.getText());
            }
            // the contact
            if (!valid.validate("ContactDTO", newUser.getContact())) {
                throw new SessionInternalError(valid.getText());
            }
            // the credit card (optional)
            if (newUser.getCreditCard() != null && !valid.validate("CreditCardDTO", 
                    newUser.getCreditCard())) {
                throw new SessionInternalError(valid.getText());
            }
            // additional validation
            if  (newUser.getMainRoleId().equals(Constants.TYPE_CUSTOMER) ||
                    newUser.getMainRoleId().equals(Constants.TYPE_PARTNER)) {
            } else {
                throw new SessionInternalError("Valid user roles are customer (5) " +
                        "and partner (4)");
            }
            if (newUser.getCurrencyId() != null && 
                    newUser.getCurrencyId().intValue() <= 0) {
                throw new SessionInternalError("Invalid currency code");
            }
            if (newUser.getStatusId().intValue() <= 0) {
                throw new SessionInternalError("Invalid status code");
            }
        } catch (ValidatorException e) {
            LOG.error("validating ws", e);
            throw new SessionInternalError("Invalid parameter");
        }
    }
    
    private void validateOrder(OrderWS order) 
            throws SessionInternalError {
        if (order == null) {
            throw new SessionInternalError("Null parameter");
        }
        order.setUserId(zero2null(order.getUserId()));
        order.setPeriod(zero2null(order.getPeriod()));
        order.setBillingTypeId(zero2null(order.getBillingTypeId()));
        order.setStatusId(zero2null(order.getStatusId()));
        order.setCurrencyId(zero2null(order.getCurrencyId()));
        order.setNotificationStep(zero2null(order.getNotificationStep()));
        order.setDueDateUnitId(zero2null(order.getDueDateUnitId()));
        order.setDueDateValue(zero2null(order.getDueDateValue()));
        order.setDfFm(zero2null(order.getDfFm()));
        order.setAnticipatePeriods(zero2null(order.getAnticipatePeriods()));
        order.setActiveSince(zero2null(order.getActiveSince()));
        order.setActiveUntil(zero2null(order.getActiveUntil()));
        order.setNextBillableDay(zero2null(order.getNextBillableDay()));
        order.setLastNotified(null);
        try {
            GatewayBL valid = new GatewayBL();
            // the order
            if (!valid.validate("OrderWS", order)) {
                throw new SessionInternalError(valid.getText());
            }
            // the lines
            for (int f = 0 ; f < order.getOrderLines().length; f++) {
                OrderLineWS line = order.getOrderLines()[f];
                if (!valid.validate("OrderLineWS", line)) {
                    throw new SessionInternalError(valid.getText());
                }
                if (line.getUseItem() == null) {
                    line.setUseItem(new Boolean(false));
                }
                line.setItemId(zero2null(line.getItemId()));
                String error = "";
                // if use the item, I need the item id
                if (line.getUseItem().booleanValue()) {
                    if (line.getItemId() == null || 
                            line.getItemId().intValue() == 0) {
                        error += "OrderLineWS: if useItem == true the itemId " +
                                "is required - ";
                    }
                    if (line.getQuantity() == null || 
                            line.getQuantity().intValue() == 0) {
                        error += "OrderLineWS: if useItem == true the quantity " +
                                "is required - ";
                    }
                } else {
                    // I need the amount and description
                    if (line.getAmount() == null) {
                        error += "OrderLineWS: if useItem == false the item amount " +
                                "is required - ";
                    }
                    if (line.getDescription() == null || 
                            line.getDescription().length() == 0) {
                        error += "OrderLineWS: if useItem == false the description " +
                                "is required - ";
                    }
                }
                if (error.length() > 0) {
                    throw new SessionInternalError(error);
                }
            }
        } catch (ValidatorException e) {
            LOG.error("validating ws", e);
            throw new SessionInternalError("Invalid parameter");
        }
    }

    private void validatePayment(PaymentWS payment) 
            throws SessionInternalError {
        if (payment == null) {
            throw new SessionInternalError("Null parameter");
        }
        payment.setUserId(zero2null(payment.getUserId()));
        payment.setMethodId(zero2null(payment.getMethodId()));
        payment.setCurrencyId(zero2null(payment.getCurrencyId()));
        payment.setPaymentId(zero2null(payment.getPaymentId()));
        
        try {
            GatewayBL valid = new GatewayBL();
            // the payment
            if (!valid.validate("PaymentWS", payment)) {
                throw new SessionInternalError(valid.getText());
            }
            // may be there is a cc
            if (payment.getCreditCard() != null && !valid.validate(
                    "CreditCardDTO", payment.getCreditCard())) {
                throw new SessionInternalError(valid.getText());
            }
            // may be there is a cheque
            if (payment.getCheque() != null && !valid.validate(
                    "PaymentInfoChequeDTO", payment.getCheque())) {
                throw new SessionInternalError(valid.getText());
            }
            // may be there is a ach
            if (payment.getAch() != null && !valid.validate(
                    "AchDTO", payment.getAch())) {
                throw new SessionInternalError(valid.getText());
            }
        } catch (ValidatorException e) {
            LOG.error("validating ws", e);
            throw new SessionInternalError("Invalid parameter");
        }
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
    }

	private InvoiceEntityLocal doCreateInvoice(Integer orderId) {
		try {
            BillingProcessBL process = new BillingProcessBL();
            InvoiceEntityLocal invoice = process.generateInvoice(orderId, null);
            invoiceId = invoice == null ? null : invoice.getId();
            return invoice;
        } catch (Exception e){
            LOG.error("WS - create invoice:", e);
            throw new SessionInternalError("Error while generating a new invoice");
        }
	}

	private void validateCaller() {
		String root = context.getCallerPrincipal().getName();
        try {
            UserBL bl = new UserBL();
            bl.setRoot(root);
            bl.getEntityId(bl.getEntity().getUserId());
        } catch (Exception e) {
            throw new SessionInternalError("Error identifiying the caller");
        }
	}
    
	private PaymentDTOEx doPayInvoice(InvoiceEntityLocal invoice, CreditCardDTO creditCard) 
		throws SessionInternalError {

		if (invoice.getBalance() == null || invoice.getBalance() <= 0){
			LOG.warn("Can not pay invoice: " + invoice.getId() + ", balance: " + invoice.getBalance());
			return null;
		}

		try {
			PaymentSessionBean payment = new PaymentSessionBean();
		    PaymentDTOEx paymentDto = new PaymentDTOEx();
		    paymentDto.setIsRefund(0);
		    paymentDto.setAmount(invoice.getBalance());
		    paymentDto.setCreditCard(creditCard);
		    paymentDto.setCurrencyId(invoice.getCurrencyId());
		    paymentDto.setUserId(invoice.getUser().getUserId());
		    paymentDto.setMethodId(
		            com.sapienter.jbilling.common.Util.getPaymentMethod(
		                    creditCard.getNumber()));
		    
		    // make the call
		    payment.processAndUpdateInvoice(paymentDto, invoice);
		    
		    return paymentDto;
	    } catch (Exception e){
            LOG.error("WS - make payment:", e);
            throw new SessionInternalError("Error while making payment for invoice: " + invoice.getId());
	    }
	}
	
	/**
	 * Conveniance method to find a credit card
	 */
	private CreditCardDTO getCreditCard(Integer userId){
		if (userId == null){
			return null;
		}

		CreditCardDTO result = null;
        try {
            UserBL user = new UserBL(userId);
            Integer entityId = user.getEntityId(userId);
            if (user.hasCreditCard()) {
                    // find it
                PaymentDTOEx paymentDto = PaymentBL.findPaymentInstrument(
                        entityId, userId);
                // it might have a credit card, but it might not be valid or 
                // just not found by the plug-in
                if (paymentDto != null) {
                    result = paymentDto.getCreditCard();
                }
            }
        } catch (Exception e) {
            LOG.error("WS - finding a credit card", e);
            throw new SessionInternalError("Error finding a credit card for user: " + userId);
        }

		return result;
	}

    private Integer doCreateOrder(OrderWS order) throws SessionInternalError {
        validateOrder(order);
        try {
            // get the info from the caller
            UserBL bl = new UserBL();
            bl.setRoot(context.getCallerPrincipal().getName());
            Integer executorId = bl.getEntity().getUserId();
            Integer entityId = bl.getEntityId(bl.getEntity().getUserId());
            Integer languageId = bl.getEntity().getLanguageIdField();
            
            // we'll need the langauge later
            bl.set(order.getUserId());
            
            // see if the related items should provide info
            processItemLine(order, languageId, entityId);
            
            // make a dto out of the ws
            NewOrderDTO dto = new NewOrderDTO(order);
            
            // call the creation
            OrderBL orderBL = new OrderBL();
            orderBL.setDTO(dto);
            LOG.debug("Order has " + dto.getOrderLinesMap().size() + " lines");
            // apply order total tasks
            orderBL.recalculate(entityId);
            
            return orderBL.create(entityId, executorId, dto);
            
        } catch(Exception e) {
            LOG.debug("Exception:", e);
            throw new SessionInternalError("error creating purchase order");
        }
    }
    
	private InvoiceEntityLocal findInvoice(Integer invoiceId) {
		final InvoiceEntityLocal invoice;
    	try {
			invoice = new InvoiceBL(invoiceId).getEntity();
		} catch (NamingException e) {
			LOG.error("WS: findInvoice error: ", e);
			throw new SessionInternalError("Configuration problems");
		} catch (FinderException e) {
			LOG.error("WS: findInvoice error: ", e);
			throw new SessionInternalError("Unknown invoice : " + invoiceId);
		}
		return invoice;
	}

}
