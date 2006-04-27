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

package com.sapienter.jbilling.server.payment;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.CustomerEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.NotificationSessionLocal;
import com.sapienter.jbilling.interfaces.NotificationSessionLocalHome;
import com.sapienter.jbilling.interfaces.PartnerEntityLocal;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.process.AgeingBL;
import com.sapienter.jbilling.server.user.PartnerBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.PreferenceBL;

/**
 *
 * This is the session facade for the payments in general. It is a statless
 * bean that provides services not directly linked to a particular operation
 *
 * @author emilc
 * @ejb:bean name="PaymentSession"
 *           display-name="A stateless bean for payments"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="both"
 *           jndi-name="com/sapienter/jbilling/server/invoice/PaymentSession"
 * 
 */
public class PaymentSessionBean implements SessionBean {

    private Logger log = null;
    private SessionContext context = null;

    /**
    * Create the Session Bean
    * @throws CreateException
    * @ejb:create-method view-type="both"
    */
    public void ejbCreate() throws CreateException {
    }

   /**
    * This method goes over all the over due invoices for a given entity and
    * generates a payment record for each of them.
    *  
    * @ejb:interface-method view-type="local"
    */
    public void processPayments(Integer entityId) throws SessionInternalError {
        try {
            entityId.intValue(); // just to avoid the warning ;)
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /** 
    * This is meant to be called from the billing process, where the information
    * about how the payment is going to be done is not known. This method will
    * call a pluggable task that finds this information (usually a cc) before
    * calling the realtime processing.
    * Later, this will have to be changed for some file creation with all the
    * payment information to be sent in a batch mode to the processor at the 
    * end of the billing process. 
    * This is called only if the user being process has as a preference to 
    * process the payment with billing process, meaning that a payment has
    * to be created and processed real-time.
    * @return If the payment was not successful for any reason, null, 
    * otherwise the payment method used for the payment
    * @ejb:interface-method view-type="local"
    * @ejb.transaction type="Required"
    */
    public Integer generatePayment(InvoiceEntityLocal invoice) 
            throws SessionInternalError {
        
        // go fetch the entity for this invoice
        Integer entityId = invoice.getUser().getEntity().getId();
        Integer retValue = null;
        // create the dto with the information of the payment to create
        try {
            // get this payment information. Now we only expect one pl.tsk
            // to get the info, I don't see how more could help
            PaymentDTOEx dto = PaymentBL.findPaymentInstrument(entityId,
                    invoice.getUser().getUserId());

            // it could be that the user doesn't have a payment 
            // instrument (cc) in the db, or that is invalid (expired).
            if (dto != null) {
                dto.setIsRefund(new Integer(0)); //it is not a refund
                dto.setUserId(invoice.getUser().getUserId());
                dto.setAmount(invoice.getBalance());
                dto.setCurrencyId(invoice.getCurrencyId());
                dto.setAttempt(new Integer(invoice.getPaymentAttemptsField().
                        intValue() + 1));
                // when the payment is generated by the system (instead of
                // entered manually by a user), the payment date is sysdate
                dto.setPaymentDate(Calendar.getInstance().getTime());
                Integer result = processAndUpdateInvoice(dto, invoice);
                if (result != null && result.equals(Constants.RESULT_OK)) {
                    retValue = dto.getMethodId();
                }
            }
            
        } catch (Exception e) {
            log.fatal("Problems generating payment.", e);
            throw new SessionInternalError(
                "Problems generating payment.");
        } 
        
        return retValue;
    }
    
    
    /**
     * It creates the payment record, makes the calls to the authorization
     * processor and updates the invoice if successfull.
     * 
     * @param dto
     * @param invoice
     * @throws SessionInternalError
     */
    public Integer processAndUpdateInvoice(PaymentDTOEx dto, 
            InvoiceEntityLocal invoice) throws SessionInternalError {
        if (log == null) {
            log = Logger.getLogger(PaymentSessionBean.class); //leave or get the web services broken
        }
        try {
            PaymentBL bl = new PaymentBL();
            Integer entityId = invoice.getUser().getEntity().getId();
            
            // set the attempt
            if (dto.getIsRefund().intValue() == 0) {
                // take the attempt from the invoice
                dto.setAttempt(new Integer(invoice.getPaymentAttempts().
                        intValue() + 1));
            } else { // is a refund
                dto.setAttempt(new Integer(1));
            } 
                
            // payment notifications require some fields from the related
            // invoice
            dto.getInvoiceIds().add(invoice.getId());
                
            // process the payment (will create the db record as well, if
            // there is any actual processing). Do not process negative
            // payments (from negative invoices).
            Integer result = null;
            if (dto.getAmount().floatValue() > 0) {
                result = bl.processPayment(entityId, dto);
            } else {
                log.warn("Skiping payment processing. Payment with negative " +
                        "amount " + dto.getAmount());
            }
            // only if there was any processing at all
            if (result != null) {
                // update the dto with the created id
                dto.setId(bl.getEntity().getId());
                // the balance will be the same as the amount
                // if the payment failed, it won't be applied to the invoice
                // so the amount will be ignored
                dto.setBalance(dto.getAmount());
                
                if (dto.getIsRefund().intValue() == 0) {
                    // now update the line invoice-payment
                    invoice.getPayments().add(bl.getEntity());
                }

                // after the process, update the payment record
                bl.getEntity().setResultId(result);
                // Note: I could use the return of the last call to fetch another
                // dto with a different cc number to retry the payment
                    
                // get all the invoice's fields updated with this payment
                applyPayment(dto, invoice, result.equals(Constants.RESULT_OK));
            }
            return result;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
     * This is called from the client to process real-time a payment, usually
     * cc. 
     * 
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     * @param dto
     * @param invoiceId
     * @throws SessionInternalError
     */
    public Integer processAndUpdateInvoice(PaymentDTOEx dto, 
            Integer invoiceId, Integer entityId) throws SessionInternalError {
        try {
            if (dto.getIsRefund().intValue() == 0 && invoiceId != null) {
                InvoiceBL bl = new InvoiceBL(invoiceId);
                return processAndUpdateInvoice(dto, bl.getEntity());
            } else if (dto.getIsRefund().intValue() == 1 && 
                    dto.getPayment() != null && 
                    !dto.getPayment().getInvoiceIds().isEmpty()) {
                InvoiceBL bl = new InvoiceBL((Integer) dto.
                        getPayment().getInvoiceIds().get(0));
                return processAndUpdateInvoice(dto, bl.getEntity());
            } else {
                // without an invoice, it's just creating the payment row
                // and calling the processor
                log.info("method called without invoice");
                
                PaymentBL bl = new PaymentBL();
                Integer result = bl.processPayment(entityId, dto);
                if (result != null) {
                    bl.getEntity().setResultId(result);
                }
                return result;

            }
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * Applys a payment to an invoice, updating the invoices fields with
     * this payment.
     * @param payment
     * @param invoice
     * @param success
     * @throws SessionInternalError
     */
    public void applyPayment(PaymentDTOEx payment, InvoiceEntityLocal invoice,
            boolean success) 
            throws SessionInternalError, NamingException, FinderException,
                CreateException, RemoveException, SQLException {
        
        log = Logger.getLogger(PaymentSessionBean.class); // leave it or break web services
        if (invoice != null) {

            // set the attempt of the invoice
            log.debug("applying payment to invoice " + invoice.getId());
            if (payment.getIsRefund().intValue() == 0) {
                //invoice can't take nulls. Default to 1 if so.
                invoice.setPaymentAttempts(payment.getAttempt() == null ? 
                        new Integer(1) : payment.getAttempt());
            }
            if (success) {
                // update the invoice's balance if applicable
                Float balance = invoice.getBalance();
                if (balance != null) {
					boolean balanceSign = (balance.floatValue() < 0) ? 
							false : true;
                    float newBalance;
                    if (payment.getIsRefund().intValue() == 0) {
                        newBalance = balance.floatValue() - 
                                payment.getBalance().floatValue();
                        // I need the payment record to update its balance
                        if (payment.getId() == null) {
                            throw new SessionInternalError("The ID of the " +
                                    "payment to has to be present in the DTO");
                        }
                        PaymentBL paymentBL = new PaymentBL(payment.getId());
                        float newPaymentBalance = payment.getBalance().floatValue() -
                                balance.floatValue();
                        if (newPaymentBalance < 0) {
                                newPaymentBalance = 0;
                        }
                        paymentBL.getEntity().setBalance(new Float(
                                newPaymentBalance));
                        payment.setBalance(new Float(newPaymentBalance));
                    } else { // refunds add to the invoice
                        newBalance = balance.floatValue() + 
                                payment.getAmount().floatValue();
                    }
                        
					// only level the balance if the original balance wasn't negative
                    if (newBalance < 0.01 && balanceSign) {
                        // the payment balance was greater than the invoice's
                        newBalance = 0;
                    }
                    
                    invoice.setBalance(new Float(newBalance));
                    
                    // update the to_process flag if the balance is 0
                    if (newBalance == 0) {
                        invoice.setToProcess(new Integer(0));
                    } else { // a refund might make this invoice payabale again
                        invoice.setToProcess(new Integer(1));
                    }
                } else {
                    // with no balance, we assume the the invoice got all paid
                    invoice.setToProcess(new Integer(0));
                }
                // if the user is in the ageing process, she should be out
                if (invoice.getToProcess().equals(new Integer(0))) {
                    AgeingBL ageing = new AgeingBL();
                    ageing.out(invoice.getUser(), invoice.getId());
                }
                // update the partner if this customer belongs to one
                CustomerEntityLocal customer = invoice.getUser().getCustomer();
                if (customer != null && customer.getPartner() != null) {
                    PartnerEntityLocal partner = customer.getPartner();
                    float pBalance = partner.getBalance().
                            floatValue();
                    float paymentAmount = payment.getAmount().floatValue();
                    if (payment.getIsRefund().intValue() == 0) {
                        pBalance += paymentAmount;
                        partner.setTotalPayments(new Float(
                                partner.getTotalPayments().floatValue() + 
                                    paymentAmount));
                        
                    } else { 
                        pBalance -= paymentAmount;    
                        partner.setTotalRefunds(new Float(
                                partner.getTotalRefunds().floatValue() + 
                                    paymentAmount));
                    }
                    partner.setBalance(new Float(pBalance));
                } 
            }
        }
        
    }

    /**
     * This method is called from the client, when a payment needs only to 
     * be applyed without realtime authorization by a processor
     * Finds this invoice entity, creates the payment record and calls the 
     * apply payment  
     * Id does suport invoiceId = null because it is possible to get a payment
     * that is not paying a specific invoice, a deposit for prepaid models.
     * 
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public Integer applyPayment(PaymentDTOEx payment, Integer invoiceId)  
            throws SessionInternalError {
        try {
            // create the payment record
            PaymentBL paymentBl = new PaymentBL();
            // set the attempt to an initial value, if the invoice is there,
            // it's going to be updated
            payment.setAttempt(new Integer(1));
            // a payment that is applied, has always the same result
            payment.setResultId(Constants.RESULT_ENTERED);
            payment.setBalance(payment.getAmount());
            paymentBl.create(payment);
            // this is necessary for the caller to get the Id of the
            // payment just created
            payment.setId(paymentBl.getEntity().getId());
            if (payment.getIsRefund().intValue() == 0) { // normal payment
                if (invoiceId != null) {
                    // find the invoice
                    InvoiceBL invoiceBl = new InvoiceBL(invoiceId);
                    // set the attmpts from the invoice
                    payment.setAttempt(new Integer(invoiceBl.getEntity().
                            getPaymentAttemptsField().intValue() + 1));
                    // link it with the invoice
                    paymentBl.getEntity().getInvoices().add(invoiceBl.getEntity());
                    // apply the payment to the invoice
                    applyPayment(payment, invoiceBl.getEntity(), true);
                }
            } else {
                if (payment.getPayment() != null && !payment.getPayment().
                        getInvoiceIds().isEmpty()) {
                    // so this refund is linked to a payment, and that payment
                    // is linked to at least one invoice.
                    InvoiceBL invoiceBL = new InvoiceBL((Integer) payment.
                            getPayment().getInvoiceIds().get(0));
                    applyPayment(payment, invoiceBL.getEntity(), true);
                }
            }
            
            return paymentBl.getEntity().getId();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }  
    
    /** 
    * @ejb:interface-method view-type="remote"
    */
    public PaymentDTOEx getPayment(Integer id, Integer languageId) 
            throws SessionInternalError {
        try {
            PaymentBL bl = new PaymentBL(id);
            return bl.getDTOEx(languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    

    /** 
    * @ejb:interface-method view-type="remote"
    */
    public boolean isMethodAccepted(Integer entityId, 
            Integer paymentMethodId) 
            throws SessionInternalError {
        if (paymentMethodId == null) {
            // if this is a credit card and it has not been
            // identified by the first digit, the method will be null
            return false;
        }
        try {
            PaymentBL bl = new PaymentBL();
            
            return bl.isMethodAccepted(entityId, paymentMethodId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    } 
    
    /** 
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="RequiresNew"
     */
    public Integer processPayout(PaymentDTOEx payment, 
            Date start, Date end, Integer partnerId, Boolean process) 
            throws SessionInternalError {
        try {
            PartnerBL partner = new PartnerBL();
            return partner.processPayout(partnerId, start, end, payment,
                    process);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /** 
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="RequiresNew"
     */
    public Boolean processPaypalPayment(Integer invoiceId, String entityEmail,
            Float amount, String currency) 
            throws SessionInternalError {
        try {
            boolean ret = false;
            InvoiceBL invoice = new InvoiceBL(invoiceId);
            
            // validate the entity
            Integer entityId = invoice.getEntity().getUser().getEntity().
                    getId();
            PreferenceBL pref = new PreferenceBL();
            pref.set(entityId, Constants.PREFERENCE_PAYPAL_ACCOUNT);
            String paypalAccount = pref.getString();
            if (paypalAccount != null && paypalAccount.equals(entityEmail)) {
                // now the currency
                CurrencyBL curr = new CurrencyBL(
                        invoice.getEntity().getCurrencyId());
                if (curr.getEntity().getCode().equals(currency)) {
                    // all good, make the payment
                    PaymentDTOEx payment = new PaymentDTOEx();
                    payment.setAmount(amount);
                    payment.setMethodId(Constants.PAYMENT_METHOD_PAYPAL);
                    payment.setUserId(invoice.getEntity().getUser().getUserId());
                    payment.setCurrencyId(curr.getEntity().getId());
                    payment.setCreateDateTime(Calendar.getInstance().getTime());
                    payment.setPaymentDate(Calendar.getInstance().getTime());
                    payment.setIsRefund(new Integer(0));
                    applyPayment(payment, invoiceId);
                    ret = true;
                    
                    // notify the customer that the payment was received
                    JNDILookup EJBFactory = JNDILookup.getFactory(false);
                    NotificationBL notif = new NotificationBL();
                    MessageDTO message = notif.getPaymentMessage(entityId, 
                            payment, true);
                    NotificationSessionLocalHome notificationHome =
                        (NotificationSessionLocalHome) EJBFactory.lookUpLocalHome(
                        NotificationSessionLocalHome.class,
                        NotificationSessionLocalHome.JNDI_NAME);

                    NotificationSessionLocal notificationSess = 
                            notificationHome.create();
                    notificationSess.notify(payment.getUserId(), message);
                    
                } else {
                    log.debug("wrong currency " + currency);
                }
            } else {
                log.debug("wrong entity paypal account " + paypalAccount + " " 
                        + entityEmail);
            }
            
            return new Boolean(ret);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /** 
     * Clients with the right priviliges can update payments with result
     * 'entered' that are not linked to an invoice
     *  
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void update(Integer executorId, PaymentDTOEx dto) 
            throws SessionInternalError, FinderException {
        if (dto.getId() == null) {
            throw new SessionInternalError("ID missing in payment to update");
        }
        
        log.debug("updateting payment " + dto.getId());
        try {
            PaymentBL bl = new PaymentBL(dto.getId());
            if (bl.getEntity().getResultId().equals(Constants.RESULT_ENTERED)) {
                
            } else {
                throw new SessionInternalError("Payment update only available" +
                        " for entered payments");
            }
            
            bl.update(executorId, dto);
        } catch (NamingException e) {
            throw new SessionInternalError(e);
        }
    }

    // EJB Callbacks -------------------------------------------------

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
    public void setSessionContext(SessionContext aContext)
            throws EJBException, RemoteException {
        log = Logger.getLogger(PaymentSessionBean.class);
        context = aContext;
    }

}
