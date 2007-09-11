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

package com.sapienter.jbilling.server.invoice;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

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
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceSessionLocal;
import com.sapienter.jbilling.interfaces.InvoiceSessionLocalHome;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.pluggableTask.PaperInvoiceNotificationTask;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskBL;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.process.BillingProcessBL;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.PreferenceBL;

/**
 *
 * This is the session facade for the invoices in general. It is a statless
 * bean that provides services not directly linked to a particular operation
 *
 * @author emilc
 * @ejb:bean name="InvoiceSession"
 *           display-name="A stateless bean for invoices"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="both"
 *           jndi-name="com/sapienter/jbilling/server/invoice/InvoiceSession"
 * 
 **/
public class InvoiceSessionBean implements SessionBean {

    private Logger log = null;
    private SessionContext context = null;

    /**
    * Create the Session Bean
    * @throws CreateException
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public InvoiceDTO getInvoice(Integer invoiceId) throws SessionInternalError {
        try {
            InvoiceBL invoice = new InvoiceBL(invoiceId);
            return invoice.getDTO();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void create(Integer entityId, Integer userId, 
            NewInvoiceDTO newInvoice) 
            throws SessionInternalError {
        try {
            InvoiceBL invoice = new InvoiceBL();
            UserBL user = new UserBL(userId);
            if (user.getEntity().getEntity().getId().equals(entityId)) {
                invoice.create(userId, newInvoice, null);
                invoice.createLines(newInvoice);
            } else {
                throw new SessionInternalError("User " + userId + " doesn't " +
                        "belong to entity " + entityId);
            }
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
     public String getFileName(Integer invoiceId) throws SessionInternalError {
         try {
             InvoiceBL invoice = new InvoiceBL(invoiceId);
             UserBL user = new UserBL(invoice.getEntity().getUser());
             ResourceBundle bundle = ResourceBundle.getBundle(
                     "entityNotifications", user.getLocale());

             String ret = bundle.getString("invoice.file.name") + '-' +
                     invoice.getEntity().getNumber().replaceAll(
                             "[\\\\~!@#\\$%\\^&\\*\\(\\)\\+`=\\]\\[';/\\.,<>\\?:\"{}\\|]", "_");
             log.debug("name = " + ret);
             return ret;
         } catch (Exception e) {
             throw new SessionInternalError(e);
         }
     }

    /**
     * The transaction requirements of this are not big. The 'atom' is 
     * just a single invoice. If the next one fails, it's ok that the
     * previous ones got updated. In fact, they should, since the email
     * has been sent.
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void sendReminders(Date today) throws SessionInternalError {
        try {
            InvoiceBL invoice = new InvoiceBL();
            invoice.sendReminders(today);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="NotSupported"
     */
    public void processOverdue(Date today) throws SessionInternalError {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            InvoiceSessionLocalHome home =
                (InvoiceSessionLocalHome) EJBFactory.lookUpLocalHome(
                InvoiceSessionLocalHome.class,
                InvoiceSessionLocalHome.JNDI_NAME);
            InvoiceSessionLocal invoiceSession = home.create();;

            // go over all the entities
            EntityBL entity = new EntityBL();
            for (Iterator it = entity.getHome().findEntities().iterator();
                    it.hasNext(); ){
                EntityEntityLocal thisEntity = (EntityEntityLocal) it.next();
                Integer entityId = thisEntity.getId();
                PreferenceBL pref = new PreferenceBL();
                try {
                    pref.set(entityId, 
                            Constants.PREFERENCE_USE_OVERDUE_PENALTY);
                } catch (FinderException e) {}
                if (pref.getInt() == 1) {    
                    invoiceSession.processOverdue(today, entityId);
                }
            }
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    
    /**
     * Again, this is only to allow the demarcation of a transaction.
     * @ejb:interface-method view-type="local"
     * @ejb.transaction type="RequiresNew"
     */
    public void processOverdue(Date today, Integer entityId)
            throws NamingException, SessionInternalError, SQLException,
                PluggableTaskException {
        InvoiceBL invoice = new InvoiceBL();
        invoice.processOverdue(today, entityId);
    }
 
    /**
    * @ejb:interface-method view-type="remote"
    */
    public InvoiceDTOEx getInvoiceEx(Integer invoiceId, Integer languageId) 
            throws SessionInternalError {
        try {
            if (invoiceId == null) {
                return null;
            }
            InvoiceBL invoice = new InvoiceBL(invoiceId);
            return invoice.getDTOEx(languageId, true);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public byte[] getPDFInvoice(Integer invoiceId)
    		throws SessionInternalError {
        try {
            if (invoiceId == null) {
                return null;
            }
            NotificationBL notification = new NotificationBL();
            InvoiceBL invoiceBl = new InvoiceBL(invoiceId);
            Integer entityId = invoiceBl.getEntity().getUser().
					getEntity().getId();
            // the language doesn't matter when getting a paper invoice
            MessageDTO message = notification.getInvoicePaperMessage(
            		entityId, null, invoiceBl.getEntity().getUser().
                        getLanguageIdField(), invoiceBl.getEntity());
            PaperInvoiceNotificationTask task = 
            		new PaperInvoiceNotificationTask();
            PluggableTaskBL taskBL = new PluggableTaskBL();
            taskBL.set(entityId, Constants.PLUGGABLE_TASK_T_PAPER_INVOICE);
            task.initializeParamters(taskBL.getDTO());
            return task.getPDF(invoiceBl.getEntity().getUser(), message);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }
    


    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void delete(Integer invoiceId, Integer executorId)
            throws SessionInternalError {
        try {
            InvoiceBL invoice = new InvoiceBL(invoiceId);
            invoice.delete(executorId);
        } catch (NamingException e) {
            throw new SessionInternalError(e);
        } catch (FinderException e) {
            throw new SessionInternalError("Can not find the invoice" +
                    invoiceId + ". Nothing to delete.");
        } catch (RemoveException e) {
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
        log = Logger.getLogger(InvoiceSessionBean.class);
        context = aContext;
    }
    
	/**
     * The real path is known only to the web server
     * It should have the token _FILE_NAME_ to be replaced by the generated file
	 * @ejb:interface-method view-type="remote"
	 */
	public String generatePDFFile(java.util.Map map, String realPath) throws SessionInternalError {
		Integer operationType = (Integer) map.get("operationType");

		try {
			InvoiceBL invoiceBL = new InvoiceBL();
			sun.jdbc.rowset.CachedRowSet cachedRowSet = null;
			Integer entityId = (Integer) map.get("entityId");

			if (operationType
					.equals(com.sapienter.jbilling.common.Constants.OPERATION_TYPE_CUSTOMER)) {
				Integer customer = (Integer) map.get("customer");
				
				//security check is done here for speed
				UserBL customerUserBL = null;
				try {
					customerUserBL = new UserBL(customer);
				} catch(FinderException e) {		
				}
				if ((customerUserBL != null) && customerUserBL.getEntity().getEntity().getId().equals(entityId)) {
					cachedRowSet = invoiceBL.getInvoicesByUserId(customer);
				}				
			} else if (operationType
					.equals(com.sapienter.jbilling.common.Constants.OPERATION_TYPE_RANGE)) {
				//security check is done in SQL
				cachedRowSet = invoiceBL.getInvoicesByIdRange(
						(Integer) map.get("from"), 
						(Integer) map.get("to"),
						entityId);
			} else if (operationType
					.equals(com.sapienter.jbilling.common.Constants.OPERATION_TYPE_PROCESS)) {
				Integer process = (Integer) map.get("process");
				
				//security check is done here for speed
				BillingProcessBL billingProcessBL = null;
				try {
					billingProcessBL = new BillingProcessBL(process);
				} catch(FinderException e) {		
				}				
				if ((billingProcessBL!= null) && billingProcessBL.getEntity().getEntityId().equals(entityId)) {
					cachedRowSet = invoiceBL.getInvoicesToPrintByProcessId(process);	
				}				
			} else if (operationType
                    .equals(com.sapienter.jbilling.common.Constants.OPERATION_TYPE_DATE)) {
                Date from = (Date) map.get("date_from");
                Date to = (Date) map.get("date_to");
                
                cachedRowSet = invoiceBL.getInvoicesByCreateDate(entityId, from, to);
            } else if (operationType
                    .equals(com.sapienter.jbilling.common.Constants.OPERATION_TYPE_NUMBER)) {
                String from = (String) map.get("number_from");
                String to = (String) map.get("number_to");
                Integer from_id = invoiceBL.convertNumberToID(entityId, from);
                Integer to_id = invoiceBL.convertNumberToID(entityId, to);
                
                if (from_id != null && to_id != null && 
                        from_id.compareTo(to_id) <= 0) {
                    cachedRowSet = invoiceBL.getInvoicesByIdRange(
                            from_id, to_id, entityId);
                }
            }
			
			if (cachedRowSet == null) {
				return null;
			} else {
				PaperInvoiceBatchBL paperInvoiceBatchBL = new PaperInvoiceBatchBL();
				return paperInvoiceBatchBL.generateFile(cachedRowSet, entityId, realPath);
			}

		} catch (Exception e) {
			throw new SessionInternalError(e);
		}
	}
	

}    
