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

package com.sapienter.jbilling.server.notification;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.NotificationMessageArchiveEntityLocal;
import com.sapienter.jbilling.interfaces.NotificationMessageArchiveEntityLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.invoice.InvoiceBL;
import com.sapienter.jbilling.server.payment.PaymentBL;
import com.sapienter.jbilling.server.pluggableTask.NotificationTask;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskManager;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;

/**
 *
 * This is the session facade for the invoices in general. It is a statless
 * bean that provides services not directly linked to a particular operation
 *
 * @author emilc
 * @ejb:bean name="NotificationSession"
 *           display-name="A stateless bean for notifications"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="both"
 *           jndi-name="com/sapienter/jbilling/server/invoice/NotificationSession"
 * 
 **/
public class NotificationSessionBean implements SessionBean {

    Logger log = null;

    /**
    * Create the Session Bean
    * @throws CreateException
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
        log = Logger.getLogger(NotificationSessionBean.class);
    }
    
    /**
     * Sends an email with the invoice to a customer.
     * This is used to manually send an email invoice from the GUI
     * @param userId
     * @param invoiceId
     * @return
     * @ejb:interface-method view-type="remote"
    */
    public Boolean emailInvoice(Integer invoiceId) 
            throws SessionInternalError {
        Boolean retValue;
        try {
            InvoiceBL invoice = new InvoiceBL(invoiceId);
            UserBL user = new UserBL(invoice.getEntity().getUser());
            Integer entityId = user.getEntity().getEntity().getId();
            Integer languageId = user.getEntity().getLanguageIdField();
            NotificationBL notif = new NotificationBL();
            MessageDTO message = notif.getInvoiceEmailMessage(entityId, 
                    languageId, invoice.getEntity());
            retValue = notify(user.getEntity(), message);
        } catch (NamingException e) {
            log.error("Exception sending email invoice", e);
            throw new SessionInternalError(e);
        } catch (FinderException e) {
            log.error("Exception sending email invoice", e);
            throw new SessionInternalError(e);
        } catch (NotificationNotFoundException e) {
            retValue = new Boolean(false);
        } 
        
        return retValue;
    }
    
    /**
     * Sends an email with the invoice to a customer.
     * This is used to manually send an email invoice from the GUI
     * @param userId
     * @param invoiceId
     * @return
     * @ejb:interface-method view-type="remote"
    */
    public Boolean emailPayment(Integer paymentId) 
            throws SessionInternalError {
        Boolean retValue;
        try {
            PaymentBL payment = new PaymentBL(paymentId);
            UserBL user = new UserBL(payment.getEntity().getUser());
            Integer entityId = user.getEntity().getEntity().getId();
            NotificationBL notif = new NotificationBL();
            MessageDTO message = notif.getPaymentMessage(entityId, 
                    payment.getDTOEx(user.getEntity().getLanguageIdField()),
                    payment.getEntity().getResultId().equals(
                            Constants.RESULT_ENTERED) ||
                    payment.getEntity().getResultId().equals(
                            Constants.RESULT_OK));
            retValue = notify(user.getEntity(), message);
        } catch (NamingException e) {
            log.error("Exception sending email payment", e);
            throw new SessionInternalError(e);
        } catch (FinderException e) {
            log.error("Exception sending email payment", e);
            throw new SessionInternalError(e);
        } catch (NotificationNotFoundException e) {
            retValue = new Boolean(false);
        } 
        
        return retValue;
    }

    /**
    * @ejb:interface-method view-type="both"
    * @ejb.transaction type="Required"
    */
    public void notify(Integer userId, MessageDTO message) 
            throws SessionInternalError {

        try {
            UserBL user = new UserBL(userId);
            notify(user.getEntity(), message);            
        } catch (Exception e) {
            throw new SessionInternalError("Problems getting user entity" +                    " for id " + userId + "." + e.getMessage());
        } 
    }
    
   /**
    * Sends a notification to a user. Returns true if no exceptions were
    * thrown, otherwise false. This return value could be considered
    * as if this message was sent or not for most notifications (emails).
    * @ejb:interface-method view-type="local"
    * @ejb.transaction type="Required"
    */
    public Boolean notify(UserEntityLocal user, MessageDTO message) 
            throws SessionInternalError {
        
        Boolean retValue = new Boolean(true);
        try {
            // verify that the message is good
            if (message.validate() == false) {
                throw new SessionInternalError("Invalid message");
            }
            // parse this message contents with the parameters
            MessageSection sections[] = message.getContent();
            for (int f=0; f < sections.length; f++) {
                MessageSection section = sections[f];
                section.setContent(NotificationBL.parseParameters(
                        section.getContent(), message.getParameters()));
            }
            // now do the delivery with the pluggable tasks
            PluggableTaskManager taskManager =
                new PluggableTaskManager(
                    user.getEntity().getId(),
                    Constants.PLUGGABLE_TASK_NOTIFICATION);
            NotificationTask task =
                (NotificationTask) taskManager.getNextClass();

            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            NotificationMessageArchiveEntityLocalHome messageHome =
                    (NotificationMessageArchiveEntityLocalHome) EJBFactory.lookUpLocalHome(
                    NotificationMessageArchiveEntityLocalHome.class,
                    NotificationMessageArchiveEntityLocalHome.JNDI_NAME);
            
            Collection userMessages = user.getArchivedMessages();
            
            while (task != null) {
                NotificationMessageArchiveEntityLocal messageRecord =
                        messageHome.create(message.getTypeId(), sections);
                userMessages.add(messageRecord);
                try {
                    task.deliver(user, message);
                } catch (TaskException e) {
                    messageRecord.setResultMessage(Util.truncateString(
                            e.getMessage(), 200));
                    log.error(e);
                    retValue = new Boolean(false);
                }
                task = (NotificationTask) taskManager.getNextClass();
            }
        } catch (Exception e) {
            log.error("Exception in notify", e);
            throw new SessionInternalError(e);
        }   
        
        return retValue;
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public MessageDTO getDTO(Integer typeId, Integer languageId, 
            Integer entityId) throws SessionInternalError {
        try {
            NotificationBL notif = new NotificationBL();
            MessageDTO retValue = null;
            try {
                notif.set(typeId, languageId, entityId);
                retValue =  notif.getDTO();
            } catch (FinderException e1) {
                retValue = new MessageDTO();
                retValue.setTypeId(typeId);
                retValue.setLanguageId(languageId);
                MessageSection sections[] = 
                        new MessageSection[notif.getSections(typeId)];
                for (int f = 0; f < sections.length; f++) {
                    sections[f] = new MessageSection(new Integer(f + 1), "");
                }
                retValue.setContent(sections);
            }
            
            return retValue;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public Integer createUpdate(MessageDTO dto, 
            Integer entityId) throws SessionInternalError {
        try {
            NotificationBL notif = new NotificationBL();
            
            return notif.createUpdate(entityId, dto);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public String getEmails(Integer entityId, String separator) 
            throws SessionInternalError {
        try {
            NotificationBL notif = new NotificationBL();
            
            return notif.getEmails(separator, entityId);
        } catch (Exception e) {
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
    public void setSessionContext(SessionContext arg0)
            throws EJBException, RemoteException {
        log = Logger.getLogger(NotificationSessionBean.class);
    }

}
