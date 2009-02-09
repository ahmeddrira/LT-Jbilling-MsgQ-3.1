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

package com.sapienter.jbilling.server.payment;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.CreditCardEntityLocal;
import com.sapienter.jbilling.interfaces.CreditCardEntityLocalHome;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentAuthorizationEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentEntityLocalHome;
import com.sapienter.jbilling.interfaces.PaymentInfoChequeEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentInfoChequeEntityLocalHome;
import com.sapienter.jbilling.interfaces.PaymentInvoiceMapEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentInvoiceMapEntityLocalHome;
import com.sapienter.jbilling.interfaces.PaymentMethodEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentMethodEntityLocalHome;
import com.sapienter.jbilling.interfaces.PaymentResultEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentResultEntityLocalHome;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.entity.PaymentAuthorizationDTO;
import com.sapienter.jbilling.server.entity.PaymentDTO;
import com.sapienter.jbilling.server.entity.PaymentInfoChequeDTO;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.list.ResultList;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.notification.NotificationNotFoundException;
import com.sapienter.jbilling.server.notification.NotificationSessionBean;
import com.sapienter.jbilling.server.payment.db.PaymentDAS;
import com.sapienter.jbilling.server.payment.event.AbstractPaymentEvent;
import com.sapienter.jbilling.server.pluggableTask.PaymentInfoTask;
import com.sapienter.jbilling.server.pluggableTask.PaymentTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.process.ConfigurationBL;
import com.sapienter.jbilling.server.system.event.EventManager;
import com.sapienter.jbilling.server.user.AchBL;
import com.sapienter.jbilling.server.user.CreditCardBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.partner.db.PartnerPayout;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.util.audit.EventLogger;

public class PaymentBL extends ResultList 
        implements PaymentSQL {
    private JNDILookup EJBFactory = null;
    private PaymentEntityLocalHome paymentHome = null;
    private PaymentInfoChequeEntityLocalHome chequeHome = null;
    private CreditCardEntityLocalHome ccHome = null;
    private PaymentMethodEntityLocalHome methodHome = null;
    private PaymentResultEntityLocalHome resultHome = null;
    private PaymentInvoiceMapEntityLocalHome mapHome = null;
    private PaymentEntityLocal payment = null;
    private static final Logger LOG = Logger.getLogger(PaymentBL.class); 
    private EventLogger eLogger = null;

    public PaymentBL(Integer paymentId) 
            throws NamingException, FinderException {
        init();
        set(paymentId);
    }

    public PaymentBL() {
        init();
    }
    
    public PaymentBL(PaymentEntityLocal payment) {
        init();
        this.payment = payment;
    }
    
    public void set(PaymentEntityLocal payment) {
        this.payment = payment;
    }

    private void init() {
        try {
            eLogger = EventLogger.getInstance();        
            EJBFactory = JNDILookup.getFactory(false);
            paymentHome = (PaymentEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    PaymentEntityLocalHome.class,
                    PaymentEntityLocalHome.JNDI_NAME);
   
            chequeHome = (PaymentInfoChequeEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    PaymentInfoChequeEntityLocalHome.class,
                    PaymentInfoChequeEntityLocalHome.JNDI_NAME);
 
            ccHome = (CreditCardEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    CreditCardEntityLocalHome.class,
                    CreditCardEntityLocalHome.JNDI_NAME);

            methodHome = (PaymentMethodEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    PaymentMethodEntityLocalHome.class,
                    PaymentMethodEntityLocalHome.JNDI_NAME);

            resultHome = (PaymentResultEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    PaymentResultEntityLocalHome.class,
                    PaymentResultEntityLocalHome.JNDI_NAME);

            mapHome = (PaymentInvoiceMapEntityLocalHome) 
                    EJBFactory.lookUpLocalHome(
                    PaymentInvoiceMapEntityLocalHome.class,
                    PaymentInvoiceMapEntityLocalHome.JNDI_NAME);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    public PaymentEntityLocal getEntity() {
        return payment;
    }
    
    public PaymentEntityLocalHome getHome() {
        return paymentHome;
    }
    
    public String getMethodDescription(Integer methodId, Integer languageId) 
            throws FinderException {
        PaymentMethodEntityLocal method = methodHome.findByPrimaryKey(
                methodId);
        return method.getDescription(languageId);
    }
    
    public void set(Integer id) throws FinderException {
        payment = paymentHome.findByPrimaryKey(id);
    }
    
    
    
    public void create(PaymentDTOEx dto) 
            throws CreateException, NamingException, FinderException {
        // create the record
        payment = paymentHome.create(dto.getAmount(),
                dto.getMethodId(), dto.getUserId(), 
                dto.getAttempt(), dto.getResultId(), dto.getCurrencyId());
            
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setBalance(dto.getBalance());
        // now verify if an info record should be created as well
        if (dto.getCheque() != null) {
            // create the db record
            PaymentInfoChequeEntityLocal cheque = chequeHome.create();
            cheque.setBank(dto.getCheque().getBank());
            cheque.setNumber(dto.getCheque().getNumber());
            cheque.setDate(dto.getCheque().getDate());
                
            // update the relationship dto-info
            payment.setChequeInfo(cheque);
        }
        
        if (dto.getCreditCard() != null) {
            CreditCardEntityLocal cc = ccHome.create(dto.getCreditCard().
                    getNumber(), dto.getCreditCard().getExpiry());
            cc.setName(dto.getCreditCard().getName());
            payment.setCreditCardInfo(cc);
        }
        
        if (dto.getAch() != null) {
        	AchBL achBl = new AchBL();
            achBl.create(dto.getAch());
            payment.setAchInfo(achBl.getEntity());
        }
        
        // may be this is a refund
        if (dto.getIsRefund().intValue() == 1) {
            payment.setIsRefund(new Integer(1));
            // now all refunds have balance = 0
            payment.setBalance(new Float(0));
            if (dto.getPayment() != null) {
                // this refund is link to a payment
                PaymentBL linkedPayment = new PaymentBL(dto.getPayment().
                        getId());
                payment.setPayment(linkedPayment.getEntity());
            }
        }
        
        // preauth payments
        if (dto.getIsPreauth() != null && dto.getIsPreauth().intValue() == 1) {
            payment.setIsPreauth(1);
        }
        
        dto.setId(payment.getId());
        // add a log row for convenience
        UserDAS user = new UserDAS();
        eLogger.auditBySystem(user.find(dto.getUserId()).getCompany().getId(), 
        		Constants.TABLE_PAYMENT, dto.getId(),
        		EventLogger.MODULE_PAYMENT_MAINTENANCE, EventLogger.ROW_CREATED, null, null, null);
    }
    
    void createMap(InvoiceEntityLocal invoice, Float amount) 
            throws CreateException {
        Float realAmount;
        if (payment.getResultId().equals(Constants.RESULT_FAIL) ||
                payment.getResultId().equals(Constants.RESULT_UNAVAILABLE)) {
            realAmount = new Float(0);
        } else {
            realAmount = amount;
        }
        mapHome.create(invoice, payment, realAmount);
    }
    
    /**
     * Updates a payment record, including related cheque or credit card
     * records. Only valid for entered payments not linked to an invoice.
     * @param dto The DTO with all the information of the new payment record.
     */
    public void update(Integer executorId, PaymentDTOEx dto) 
            throws FinderException, NamingException, SessionInternalError {
        // the payment should've been already set when constructing this
        // object
        if (payment == null) {
            throw new FinderException("Payment to update not set");
        }
        
        // we better log this, so this change can be traced
        eLogger.audit(executorId, Constants.TABLE_PAYMENT, 
                payment.getId(),
                EventLogger.MODULE_PAYMENT_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null,  
                payment.getAmount().toString(), null);
        
        // start with the payment's own fields
        payment.setUpdateDateTime(Calendar.getInstance().getTime());
        payment.setAmount(dto.getAmount());
        // since the payment can't be linked to an invoice, the balance
        // has to be equal to the total of the payment
        payment.setBalance(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        
        // now the records related to the method
        if (dto.getCheque() != null) {
            PaymentInfoChequeEntityLocal cheque = payment.getChequeInfo();
            cheque.setBank(dto.getCheque().getBank());
            cheque.setNumber(dto.getCheque().getNumber());
            cheque.setDate(dto.getCheque().getDate());
        } else if (dto.getCreditCard() != null) {
            CreditCardBL cc = new CreditCardBL(payment.getCreditCardInfo());
            cc.update(executorId, dto.getCreditCard(), null);
        } else if (dto.getAch() != null) {
            AchBL achBl = new AchBL(payment.getAchInfo());
            achBl.update(executorId, dto.getAch());
        }
    }
    
    /**
     * Goes through the payment pluggable tasks, and calls them with the
     * payment information to get the payment processed.
     * If a call fails because of the availability of the processor, it
     * will try with the next task. Otherwise it will return the result
     * of the process (approved or declined).
     * @return the constant of the result
     * allowing for the caller to attempt it again with different payment
     * information (like another cc number) 
     */
    public Integer processPayment(Integer entityId, PaymentDTOEx info) 
            throws SessionInternalError {
        Integer retValue = null;
        try {
            PluggableTaskManager taskManager =
                    new PluggableTaskManager(entityId,
                    Constants.PLUGGABLE_TASK_PAYMENT);
            PaymentTask task = (PaymentTask) taskManager.getNextClass();
            
            if (task == null) {
                // at least there has to be one task configurated !
                LOG.warn("No payment pluggable" +
                        "tasks configurated for entity " + entityId);
                return null;
            }
            
            create(info);
            boolean processorUnavailable = true;
            while (task != null && processorUnavailable) {
                // see if this user has pre-auths
                PaymentAuthorizationBL authBL = new PaymentAuthorizationBL();
                PaymentAuthorizationDTO auth = authBL.getPreAuthorization(info.getUserId());
                if (auth != null) {
                    processorUnavailable = task.confirmPreAuth(auth, info);
                    if (!processorUnavailable) {
                        if (info.getResultId() == Constants.RESULT_FAIL) {
                            processorUnavailable = task.process(info);
                        }
                        // in any case, don't use this preAuth again
                        authBL.markAsUsed(info);
                    }
                } else {
                    // get this payment processed
                    processorUnavailable = task.process(info);
                }
                
                // allow the pluggable task to do something if the payment 
                // failed (like notification, suspension, etc ... )
                if (!processorUnavailable && info.getResultId() == 
                        Constants.RESULT_FAIL) {
                    task.failure(info.getUserId(), info.getAttempt());  
                }
                // trigger an event
                AbstractPaymentEvent event = 
                		AbstractPaymentEvent.forPaymentResult(entityId, info);
                		
                if (event != null){
                	EventManager.process(event);
                }

                // get the next task
                task = (PaymentTask) taskManager.getNextClass();
            }
            
            // if after all the tasks, the processor in unavailable, 
            // return that
            if (processorUnavailable) {
                retValue = Constants.RESULT_UNAVAILABLE;
            } else {
                retValue = info.getResultId();
            }
            
            // the balance of the payment depends on the result
            if (retValue.equals(Constants.RESULT_OK) || 
                    retValue.equals(Constants.RESULT_ENTERED)) {
                payment.setBalance(payment.getAmount());
            } else {
                payment.setBalance(new Float(0));
            }
        } catch (Exception e) {
            LOG.fatal("Problems handling payment task.", e);
            throw new SessionInternalError(
                "Problems handling payment task.");
        }  
        
        // add a notification to the user if the payment was good or bad
        if (retValue.equals(Constants.RESULT_OK) || 
                retValue.equals(Constants.RESULT_FAIL)) {
            sendNotification(info, entityId);
        }
        return retValue;      
    }
    
    public PaymentDTO getDTO() {
        return new PaymentDTO(payment.getId(), payment.getAmount(), 
		        payment.getBalance(),
                payment.getCreateDateTime(), 
                payment.getUpdateDateTime(),payment.getPaymentDate(),
                payment.getAttempt(), payment.getDeleted(), 
                payment.getMethodId(), payment.getResultId(), 
                payment.getIsRefund(), payment.getIsPreauth(),
                payment.getCurrencyId(), payment.getUserId());
    }
    
    public PaymentDTOEx getDTOEx(Integer language) 
            throws FinderException, NamingException {
        PaymentDTOEx dto = new PaymentDTOEx(getDTO());
        dto.setUserId(payment.getUser().getUserId());
        // now add all the invoices that were paid by this payment
        Iterator it = payment.getInvoicesMap().iterator();
        while (it.hasNext()) {
            PaymentInvoiceMapEntityLocal map = 
                    (PaymentInvoiceMapEntityLocal) it.next();
            dto.getInvoiceIds().add(map.getInvoice().getId());
            
            dto.addPaymentMap(getMapDTO(map.getId()));
        } 
        
        // cheque info if applies
        PaymentInfoChequeDTO chequeDto = null;
        if (payment.getChequeInfo() != null) {
            chequeDto = new PaymentInfoChequeDTO();
            chequeDto.setBank(payment.getChequeInfo().getBank());
            chequeDto.setDate(payment.getChequeInfo().getDate());
            chequeDto.setId(payment.getChequeInfo().getId());
            chequeDto.setNumber(payment.getChequeInfo().getNumber());
        }
        dto.setCheque(chequeDto);

        // credit card info if applies        
        CreditCardDTO ccDto = null;
        if (payment.getCreditCardInfo() != null) {
            ccDto = new CreditCardDTO();
            ccDto.setNumber(payment.getCreditCardInfo().getNumber());
            ccDto.setExpiry(payment.getCreditCardInfo().getExpiry());
            ccDto.setName(payment.getCreditCardInfo().getName());
            ccDto.setType(payment.getCreditCardInfo().getType());
        }
        dto.setCreditCard(ccDto);
        
        // ach if applies
        if (payment.getAchInfo() != null) {
        	AchBL achBl = new AchBL(payment.getAchInfo());
            dto.setAch(achBl.getDTO());
        } else {
        	dto.setAch(null);
        }
        
        // payment method (international)
        PaymentMethodEntityLocal method = methodHome.findByPrimaryKey(payment.getMethodId());
        dto.setMethod(method.getDescription(language));

        // refund fields if applicable
        dto.setIsRefund(payment.getIsRefund());
        if (payment.getPayment() != null) {
            PaymentBL linkedPayment = new PaymentBL(payment.getPayment().
                    getId());
            dto.setPayment(linkedPayment.getDTOEx(language));
        }      
        
        // the first authorization if any
        if (!payment.getAuthorizations().isEmpty()) {
            PaymentAuthorizationBL authBL = new PaymentAuthorizationBL(
                    (PaymentAuthorizationEntityLocal) payment.
                        getAuthorizations().iterator().next());
            dto.setAuthorization(authBL.getDTO());
        }  
        
        // the result in string mode (international)
        if (payment.getResultId() != null) {
            PaymentResultEntityLocal result = resultHome.findByPrimaryKey(
                    payment.getResultId()); 
            dto.setResultStr(result.getDescription(language));
        }
        
        // to which payout this payment has been included
        if (payment.getPayout().size() > 0) {
            dto.setPayoutId(((PartnerPayout) payment.getPayout().toArray()[0]).getId());
        }
        
        return dto;
    }
        

    public CachedRowSet getList(Integer entityID, Integer languageId, 
            Integer userRole, Integer userId, boolean isRefund) 
            throws SQLException, Exception {
                
        // the first variable specifies if this is a normal payment or 
        // a refund list
        if(userRole.equals(Constants.TYPE_ROOT) ||
                userRole.equals(Constants.TYPE_CLERK)) {
            prepareStatement(PaymentSQL.rootClerkList);
            cachedResults.setInt(1, isRefund ? 1 : 0);
            cachedResults.setInt(2, entityID.intValue());
            cachedResults.setInt(3, languageId.intValue());
        } else if(userRole.equals(Constants.TYPE_PARTNER)) {
            prepareStatement(PaymentSQL.partnerList);
            cachedResults.setInt(1, isRefund ? 1 : 0);
            cachedResults.setInt(2,entityID.intValue());
            cachedResults.setInt(3, userId.intValue());
            cachedResults.setInt(4, languageId.intValue());
        } else if(userRole.equals(Constants.TYPE_CUSTOMER)) {
            prepareStatement(PaymentSQL.customerList);
            cachedResults.setInt(1, isRefund ? 1 : 0);
            cachedResults.setInt(2, userId.intValue());
            cachedResults.setInt(3, languageId.intValue());
        } else {
            throw new Exception("The payments list for the type " + userRole + 
                    " is not supported");
        }

        execute();
        conn.close();
        return cachedResults;
    }

    
    /**
     * Does the actual work of deleteing the payment
     * @throws SessionInternalError
     */
    
    public void delete() throws SessionInternalError {
    	
    	try {
            LOG.debug("Deleting payment " + payment.getId());
            payment.setUpdateDateTime(Calendar.getInstance().getTime());
            payment.setDeleted(new Integer(1));
            
            eLogger.auditBySystem(payment.getUser().getEntity().getId(),
                    Constants.TABLE_PAYMENT, payment.getId(),
                    EventLogger.MODULE_PAYMENT_MAINTENANCE, 
                    EventLogger.ROW_DELETED, null, null, null);

        } catch (Exception e) {
            LOG.warn("Problem deleteing payment.", e);
            throw new SessionInternalError("Problem deleteing payment.");
        }
     }
    
    /*
     * This is the list of payment that are refundable. It shows when
     * entering a refund.
     */
    
    public CachedRowSet getRefundableList(Integer languageId, 
            Integer userId) 
            throws SQLException, Exception {
        prepareStatement(PaymentSQL.refundableList); 
        cachedResults.setInt(1, 0); // is not a refund   
        cachedResults.setInt(2, userId.intValue());
        cachedResults.setInt(3, languageId.intValue());
        execute();
        conn.close();
        return cachedResults;
    }

    public boolean isMethodAccepted(Integer entityId, 
            Integer paymentMethodId) 
            throws FinderException {
            
        boolean retValue = false;
        
        PaymentMethodEntityLocal method = methodHome.findByPrimaryKey(
            paymentMethodId);
        
        for (Iterator it = method.getEntitys().iterator(); it.hasNext();) {
            if (((CompanyDTO) it.next()).getId() == entityId) {
                retValue = true;
                break;
            }
        }
        return retValue;
    }

    public static PaymentDTOEx findPaymentInstrument(Integer entityId, Integer userId) 
            throws PluggableTaskException, SessionInternalError, TaskException {
        
        PluggableTaskManager taskManager = new PluggableTaskManager(entityId,
                Constants.PLUGGABLE_TASK_PAYMENT_INFO);
        PaymentInfoTask task = (PaymentInfoTask) taskManager.getNextClass();
            
        if (task == null) {
            // at least there has to be one task configurated !
            Logger.getLogger(PaymentBL.class).fatal("No payment info pluggable" +
                    "tasks configurated for entity " + entityId);
            throw new SessionInternalError("No payment info pluggable" +
                    "tasks configurated for entity " + entityId);
        }
            
        // get this payment information. Now we only expect one pl.tsk
        // to get the info, I don't see how more could help
        return task.getPaymentInfo(userId);

    }
    
    
    public static boolean validate(PaymentWS dto) {
        boolean retValue = true;
        
        if (dto.getAmount() == null || dto.getMethodId() == null ||
                dto.getIsRefund() == null || dto.getResultId() == null ||
                dto.getUserId() == null || (dto.getCheque() == null &&
                    dto.getCreditCard() == null)) {
            retValue = false;
        } else if (dto.getCreditCard() != null) {
            retValue = CreditCardBL.validate(dto.getCreditCard());
        } else if (dto.getCheque() != null) {
            retValue = validate(dto.getCheque());
        }
        
        return retValue;
    }
    
    public static boolean validate(PaymentInfoChequeDTO dto) {
        boolean retValue = true;
        
        if (dto.getDate() == null || dto.getNumber() == null) {
            retValue = false;
        }
    
        return retValue;
    }    
    
    public Integer getLatest(Integer userId) throws SessionInternalError {
        Integer retValue = null;
        try {
            prepareStatement(PaymentSQL.getLatest);
            cachedResults.setInt(1, userId.intValue());
            execute();
            if (cachedResults.next()) {
                int value = cachedResults.getInt(1);
                if (!cachedResults.wasNull()) {
                    retValue = new Integer(value);
                }
            }
            cachedResults.close();
            conn.close();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }

        return retValue;
    }

    public Integer[] getManyWS(Integer userId, Integer number, 
            Integer languageId) 
            throws NamingException, FinderException {
        List<Integer> result = new PaymentDAS().findIdsByUserLatestFirst(userId, number);
        return result.toArray(new Integer[result.size()]);

    }

    private Vector<PaymentEntityLocal> getPaymentsWithBalance(Integer userId) {
        // this will usually return 0 or 1 records, rearly a few more
        Vector<PaymentEntityLocal> paymentsVector = null;
        try {
            Collection payments = paymentHome.findWithBalance(userId);
            paymentsVector = new Vector<PaymentEntityLocal>(payments); // needed for the sort
            Collections.sort(paymentsVector, new PaymentEntityComparator());
            Collections.reverse(paymentsVector);
        } catch (FinderException e) {
            paymentsVector = new Vector<PaymentEntityLocal>(); // empty
        }

        return paymentsVector;
    }
    
    /**
     * make sure to call this only with the configuration for 
     * automatic application of unsued payments is set
     */
    public void automaticPaymentApplication(InvoiceEntityLocal invoice) 
            throws RemoveException, CreateException, SessionInternalError,
                FinderException, NamingException, SQLException {
        Vector payments = getPaymentsWithBalance(invoice.getUser().getUserId());
        
        for (int f = 0; f < payments.size() && 
                invoice.getBalance().floatValue() > 0; f++) {
            payment = (PaymentEntityLocal) payments.get(f);
            if (payment.getResultId().equals(Constants.RESULT_FAIL) ||
                    payment.getResultId().equals(Constants.RESULT_UNAVAILABLE)) {
                continue;
            }
            // this is not actually getting de Ex, so it is faster
            PaymentDTOEx dto = new PaymentDTOEx(getDTO()); 
            
            // not pretty, but the methods are there
            PaymentSessionBean psb = new PaymentSessionBean();
            // make the link between the payment and the invoice
            Float paidAmount = new Float(psb.applyPayment(dto, invoice, true));
            createMap(invoice, paidAmount);

            // notify the customer
            dto.setUserId(invoice.getUser().getUserId()); // needed for the notification
            // the notification only understands ok or not, if the payment is entered
            // it has to show as ok
            dto.setResultId(Constants.RESULT_OK);
            sendNotification(dto, payment.getUser().getEntity().getId());
                
        }
    }

    /**
     * sends an notification with a payment
     */
    public void sendNotification(PaymentDTOEx info, Integer entityId)
            throws SessionInternalError {
        try {
            NotificationBL notif = new NotificationBL();
            MessageDTO message = notif.getPaymentMessage(entityId, 
                    info, info.getResultId().equals(Constants.RESULT_OK)); 
            NotificationSessionBean notificationSess = (NotificationSessionBean) Context.getBean(
                        Context.NOTIFICATION_SESSION);
            notificationSess.notify(info.getUserId(), message);
        } catch (NamingException e1) {
            throw new SessionInternalError(e1);
        } catch (NotificationNotFoundException e1) {
            // won't send anyting because the entity didn't specify the
            // notification
            LOG.warn("Can not notify a customer about a payment " +
                    "beacuse the entity lacks the notification. " +
                    "entity = " + entityId);
        }
    }
    
    /*
     * The payment doesn't have to be set. 
     * It adjusts the balances of both the payment and the invoice and
     * deletes the map row.
     */
    public void removeInvoiceLink(Integer mapId) {
        try {
            // find the map
            PaymentInvoiceMapEntityLocal map = mapHome.findByPrimaryKey(mapId);
            // start returning the money to the payment's balance
            BigDecimal amount = new BigDecimal(map.getAmount().toString());
            payment = map.getPayment();
            amount = amount.add(new BigDecimal(payment.getBalance().toString()));
            payment.setBalance(new Float(amount.floatValue()));
            // the balace of the invoice also increases
            amount = new BigDecimal(map.getAmount().toString());
            InvoiceEntityLocal invoice = map.getInvoice();
            amount = amount.add(new BigDecimal(invoice.getBalance().toString()));
            invoice.setBalance(new Float(amount.floatValue()));
            // this invoice probably has to be paid now
            if (invoice.getBalance().floatValue() >= 0.01) {
                invoice.setToProcess(new Integer(1));
            }
            
            // get rid of the map all together
            map.remove();
            
            // log that this was deleted, otherwise there will be no trace
            eLogger.info(invoice.getUser().getEntity().getId(), mapId,
                    EventLogger.MODULE_PAYMENT_MAINTENANCE, 
                    EventLogger.ROW_DELETED, Constants.TABLE_PAYMENT_INVOICE_MAP);

        } catch (Exception e) {
            LOG.error("Exception removing payment-invoice link", e);
            throw new SessionInternalError(e);
        } 
    }

    public PaymentInvoiceMapDTOEx getMapDTO(Integer mapId) 
            throws FinderException {
        //      find the map
        PaymentInvoiceMapEntityLocal map = mapHome.findByPrimaryKey(mapId);
        PaymentInvoiceMapDTOEx dto = new PaymentInvoiceMapDTOEx(map.getId(),
                map.getAmount(), map.getCreateDateTime());
        dto.setPaymentId(map.getPayment().getId());
        dto.setInvoiceId(map.getInvoice().getId());
        dto.setCurrencyId(map.getPayment().getCurrencyId());
        return dto;
    }

    /**
     * Checks first if the configuration is for automatic linking.
     * Looks for any invoice with balance, and then links it with any payment with balance.
     * @return
     */
    public boolean linkPaymentsWithInvoice(Integer userId) {
    	LOG.debug("Attempting to link payments to invoices. User " + userId);
    	boolean retValue = false;
    	try {
			UserBL user = new UserBL(userId);
			Integer entityId = user.getEntityId(userId);
			ConfigurationBL conf = new ConfigurationBL(entityId);
			if (conf.getEntity().getAutoPaymentApplication() != null &&
					conf.getEntity().getAutoPaymentApplication().intValue() == 1) {
				// now find an invoice
				InvoiceBL invBl = new InvoiceBL();
				Integer invoiceId = null;
				CachedRowSet set = invBl.getPayableInvoicesByUser(userId);
				if (set.next()) {
					invoiceId = set.getInt(1);
					invBl.set(invoiceId);
					automaticPaymentApplication(invBl.getEntity());
					LOG.debug("done");
				} else {
					set.close();
					LOG.debug("Can't find any invoice to pay");
				}
				
			} else {
				LOG.debug("Configuration set to not link payments");
			}
		} catch (Exception e) {
			throw new SessionInternalError("Linking payments to invoices", PaymentBL.class, e);
		}
    	return retValue;
    }
}

