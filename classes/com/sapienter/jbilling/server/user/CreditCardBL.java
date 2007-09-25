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

package com.sapienter.jbilling.server.user;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.CreditCardEntityLocal;
import com.sapienter.jbilling.interfaces.CreditCardEntityLocalHome;
import com.sapienter.jbilling.interfaces.NotificationSessionLocal;
import com.sapienter.jbilling.interfaces.NotificationSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.entity.CreditCardDTO;
import com.sapienter.jbilling.server.list.ResultList;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.notification.NotificationNotFoundException;
import com.sapienter.jbilling.server.payment.PaymentAuthorizationDTOEx;
import com.sapienter.jbilling.server.payment.PaymentBL;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.payment.event.AbstractPaymentEvent;
import com.sapienter.jbilling.server.pluggableTask.PaymentTask;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.system.event.EventManager;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.EventLogger;

public class CreditCardBL extends ResultList 
        implements CreditCardSQL {
    private JNDILookup EJBFactory = null;
    private CreditCardEntityLocalHome creditCardHome = null;
    private CreditCardEntityLocal creditCard = null;
    private Logger log = null;
    private EventLogger eLogger = null;
    
    public CreditCardBL(Integer creditCardId) 
            throws NamingException, FinderException {
        init();
        set(creditCardId);
    }
    
    public CreditCardBL() throws NamingException {
        init();
    }
    
    public CreditCardBL(CreditCardEntityLocal row)
            throws NamingException {
        init();
        creditCard = row;
    }
    
    private void init() throws NamingException {
        log = Logger.getLogger(CreditCardBL.class);     
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        creditCardHome = (CreditCardEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                CreditCardEntityLocalHome.class,
                CreditCardEntityLocalHome.JNDI_NAME);
    }

    public CreditCardEntityLocal getEntity() {
        return creditCard;
    }
    
    public void set(Integer id) throws FinderException {
        creditCard = creditCardHome.findByPrimaryKey(id);
    }
    
    public void set(CreditCardEntityLocal pEntity) {
        creditCard = pEntity;
    }
    
    public Integer create(CreditCardDTO dto) 
            throws CreateException {
        creditCard = creditCardHome.create(dto.getNumber(), dto.getExpiry()); 
        creditCard.setName(dto.getName());
                
        return creditCard.getId();       
    }
    
    public void update(Integer executorId, CreditCardDTO dto) 
            throws SessionInternalError {
        if (executorId != null) {
            eLogger.audit(executorId, Constants.TABLE_CREDIT_CARD, 
                    creditCard.getId(),
                    EventLogger.MODULE_CREDIT_CARD_MAINTENANCE, 
                    EventLogger.ROW_UPDATED, null,  
                    null, creditCard.getExpiry());
        }
        creditCard.setExpiry(dto.getExpiry());
        creditCard.setName(dto.getName());
        // the number can be null, because calls from the API would do this
        // to leave the number unchanged (was returned masked)
        if (dto.getNumber() != null) {
            creditCard.setNumber(dto.getNumber());
        }
        creditCard.setDeleted(new Integer(0));
    }
    
    public void delete(Integer executorId) 
            throws RemoveException, NamingException, FinderException {
        // now delete this creditCard record
        eLogger.audit(executorId, Constants.TABLE_CREDIT_CARD, 
                creditCard.getId(),
                EventLogger.MODULE_CREDIT_CARD_MAINTENANCE, 
                EventLogger.ROW_DELETED, null, null,null);
        
        creditCard.setDeleted(new Integer(1));
    }
    
    public void notifyExipration(Date today) 
            throws SQLException, NamingException, FinderException,
                SessionInternalError, CreateException {
        log.debug("Sending credit card expiration notifications. Today " 
                + today);
        prepareStatement(CreditCardSQL.expiring);
        cachedResults.setDate(1, new java.sql.Date(today.getTime()));
        
        execute();
        while(cachedResults.next()) {
            Integer userId = new Integer(cachedResults.getInt(1));
            Integer ccId = new Integer(cachedResults.getInt(2));
            
            set(ccId);
            NotificationBL notif = new NotificationBL();
            UserBL user = new UserBL(userId);
            try {
                MessageDTO message = notif.getCreditCardMessage(user.getEntity().
                        getEntity().getId(), user.getEntity().getLanguageIdField(), 
                        userId, getDTO());
                
                NotificationSessionLocalHome notificationHome =
                    (NotificationSessionLocalHome) EJBFactory.lookUpLocalHome(
                    NotificationSessionLocalHome.class,
                    NotificationSessionLocalHome.JNDI_NAME);
    
                NotificationSessionLocal notificationSess = 
                    notificationHome.create();
                notificationSess.notify(userId, message);
            } catch (NotificationNotFoundException e) {
                log.warn("credit card message not set to user " + userId +
                        " because the entity lacks notification text");
            }
        }
        conn.close();

    }
    /**
     * Returns true if it makes sense to send this cc to the processor.
     * Otherwise false (like when the card is now expired). 
     */
    public boolean validate() {
        boolean retValue  = true;
        
        if (creditCard.getExpiry().before(Calendar.getInstance().getTime())) {
            retValue = false;
        } else {
            if (Util.getPaymentMethod(creditCard.getNumber()) == null) {
                retValue = false;
            }
        }
        
        return retValue;
    }
    
    static public boolean validate(CreditCardDTO dto) {
        boolean retValue = true;
        
        if (dto.getExpiry() == null || dto.getName() == null || 
                dto.getNumber() == null) {
            retValue = false;
            Logger.getLogger(CreditCardBL.class).debug("invalid " + dto);
        }
        
        return retValue;
    }
    
    public CreditCardDTO getDTO() {
        CreditCardDTO dto = new CreditCardDTO();
        
        dto.setId(creditCard.getId());
        dto.setDeleted(creditCard.getDeleted());
        dto.setExpiry(creditCard.getExpiry());
        dto.setName(creditCard.getName());
        dto.setNumber(creditCard.getNumber());
        dto.setType(creditCard.getType());
        dto.setSecurityCode(creditCard.getSecurityCode());
        
        return dto;
    }
    
    public Integer getPaymentMethod() {
        return Util.getPaymentMethod(creditCard.getNumber());
    }
    
    /**
     * removes spaces and '-' from the number.
     * @param number
     * @return
     */
    public static String cleanUpNumber(String number) {
        return number.replaceAll("[-\\ ]", "").trim();
    }
    
    public PaymentAuthorizationDTOEx validatePreAuthorization(Integer entityId, 
            Integer userId, CreditCardDTO cc, 
            Float amount, Integer currencyId) 
            throws NamingException, PluggableTaskException, FinderException, CreateException {

        // create a new payment record
        PaymentDTOEx paymentDto = new PaymentDTOEx();
        paymentDto.setAmount(amount);
        paymentDto.setCurrencyId(currencyId);
        paymentDto.setCreditCard(cc);
        paymentDto.setUserId(userId);
        paymentDto.setIsPreauth(new Integer(1));
        // filler fields, required
        paymentDto.setIsRefund(new Integer(0));
        paymentDto.setMethodId(Util.getPaymentMethod(cc.getNumber()));
        paymentDto.setAttempt(new Integer(1));
        paymentDto.setResultId(Constants.RESULT_ENTERED); // to be updated later
        paymentDto.setPaymentDate(Calendar.getInstance().getTime());
        paymentDto.setBalance(amount);
        
        PaymentBL payment = new PaymentBL();
        payment.create(paymentDto); // this updates the id
        
        // use the payment processor configured 
        PluggableTaskManager taskManager =
            new PluggableTaskManager(entityId,
            Constants.PLUGGABLE_TASK_PAYMENT);
        PaymentTask task = (PaymentTask) taskManager.getNextClass();
    
        boolean processNext = true;
        while (task != null && processNext) {
            processNext = task.preAuth(paymentDto);
            // get the next task
            task = (PaymentTask) taskManager.getNextClass();

            // at the time, a pre-auth acts just like a normal payment for events
            AbstractPaymentEvent event = AbstractPaymentEvent.forPaymentResult(entityId, paymentDto);
            if (event != null){
            	EventManager.process(event);
            }
        } 
        
        // update the result
        payment.getEntity().setResultId(paymentDto.getResultId());
        
        //create the return value
        PaymentAuthorizationDTOEx retValue = new PaymentAuthorizationDTOEx(
                paymentDto.getAuthorization());
        retValue.setResult(paymentDto.getResultId().equals(Constants.RESULT_OK));
        if (!retValue.getResult()) {
            // if it was not successfull, it should not have balance
            payment.getEntity().setBalance(0F);
        }
        
        return retValue;
    }

	public static String get4digitExpiry(CreditCardDTO cc) {
        String expiry = null;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(cc.getExpiry());
        expiry = String.valueOf(
                cal.get(GregorianCalendar.MONTH) + 1) + String.valueOf(
                    cal.get(GregorianCalendar.YEAR)).substring(2);
        if (expiry.length() == 3) {
            expiry = "0" + expiry;
        }
        
        return expiry;

    }
    
    /**
     * Deletes existing cc records and adds a new one.
     * @param executorId
     * Id of the user executing this method.
     * @param userId
     * Id of user who is updating cc.
     * @param cc
     * New cc data.
     */
    public void updateForUser(Integer executorId, Integer userId, 
                              CreditCardDTO cc) 
            throws CreateException, RemoveException, NamingException,
            FinderException {

        UserEntityLocal user = UserBL.getUserEntity(userId);

        Iterator iter = user.getCreditCard().iterator();
        // delete existing cc records
        while (iter.hasNext()) {
            set((CreditCardEntityLocal) iter.next());
            delete(executorId);
            iter.remove();
        }
        // add the new one
        create(cc);
        user.getCreditCard().add(creditCard);
    }
}
