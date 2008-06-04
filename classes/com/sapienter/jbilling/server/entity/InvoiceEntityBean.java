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

package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.BillingProcessEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.PaperInvoiceBatchEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.interfaces.UserEntityLocalHome;
import com.sapienter.jbilling.server.invoice.NewInvoiceDTO;
import com.sapienter.jbilling.server.invoice.db.Invoice;
import com.sapienter.jbilling.server.invoice.db.InvoiceDAS;
import com.sapienter.jbilling.server.order.db.OrderProcessDTO;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="InvoiceEntity" 
 *          display-name="Object representation of the table INVOICE"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/InvoiceEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/InvoiceEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="invoice"
 *
 * @ejb:finder signature="Collection findProccesableByUser(java.lang.Integer userId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM invoice a 
 *                     WHERE a.user.userId = ?1
 *                       AND a.toProcess = 1 
 *                       AND a.isReview = 0
 *                       AND a.deleted = 0"
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="Collection findWithBalanceByUser(java.lang.Integer userId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM invoice a 
 *                     WHERE a.user.userId = ?1
 *                       AND a.balance <> 0 
 *                       AND a.isReview = 0
 *                       AND a.deleted = 0"
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="Collection findProccesableByProcess(java.lang.Integer processId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM invoice a 
 *                     WHERE a.billingProcess.id = ?1
 *                       AND a.toProcess = 1 
 *                       AND a.isReview = 0
 *                       AND a.inProcessPayment = 1
 *                       AND a.deleted = 0"
 *             result-type-mapping="Local"
 *
 * @ejb.value-object name="Invoice"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "invoice"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class InvoiceEntityBean implements EntityBean {
    private JNDILookup EJBFactory = null;

    private Logger log = null;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer userId, NewInvoiceDTO invoice,
            Vector invoices, BillingProcessEntityLocal process)
        throws CreateException {

        Integer newId;

        try {
 
            EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId =
                new Integer(
                    generator.getNextSequenceNumber(
                        Constants.TABLE_INVOICE));

        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the purchase order table");
        }

        setId(newId);
        setCreateDateTime(invoice.getBillingDate());
        setCreateTimeStamp(Calendar.getInstance().getTime());
        setDeleted(new Integer(0));
        setDueDate(invoice.getDueDate());
        setTotal(invoice.getTotal());
        setBalance(invoice.getBalance());
        setCarriedBalance(invoice.getCarriedBalance());
        setPaymentAttempts(new Integer(0));
        setInProcessPayment(invoice.getInProcessPayment());
        setIsReview(invoice.getIsReview());
        setCurrencyId(invoice.getCurrencyId());
        // Initially the invoices are processable, this will be changed
        // when the invoice gets fully paid. This doesn't mean that the
        // invoice will be picked up by the main process, because of the
        // due date. (fix: if the total is > 0)
        if (invoice.getTotal().compareTo(new Float(0)) <= 0) {
            setToProcess(new Integer(0));
        } else {
            setToProcess(new Integer(1));
        }

        return newId;
    }

    public void ejbPostCreate(Integer userId, NewInvoiceDTO invoice,
            Vector invoices, BillingProcessEntityLocal process) {

        if (getIsReview().intValue() == 0) {
            setIncludedInvoices(invoices);
        }

        UserEntityLocal user;

        try {

            EJBFactory = JNDILookup.getFactory(false);
            UserEntityLocalHome userHome =
                (UserEntityLocalHome) EJBFactory.lookUpLocalHome(
                    UserEntityLocalHome.class,
                    UserEntityLocalHome.JNDI_NAME);
            user = userHome.findByPrimaryKey(userId);
        } catch (Exception e) {
            throw new EJBException(
                e.getMessage() +"User not found when creating invoice record:" 
                + userId);
        }

        setUser(user);
        setBillingProcess(process);

    }
    /*
     * Some custom field calls
     */
    /**
     * @ejb:interface-method view-type="local"
     * This is necessary only because torque is not creating hsql tables properly,
     * making columns not null and with a default
     */     
     public Integer getPaymentAttemptsField() {
         if (getPaymentAttempts() == null) {
             log.warn("invoice.payment_attempts shouldn't be able to be null");
             return new Integer(0);
         } 
         
         return getPaymentAttempts();
     }
    // CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer itemId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDateTime();
    public abstract void setCreateDateTime(Date create);

     /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="create_timestamp"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateTimeStamp();
    public abstract void setCreateTimeStamp(Date create);

   /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="last_reminder"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getLastReminder();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setLastReminder(Date date);


    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="due_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getDueDate();
    public abstract void setDueDate(Date due);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="total"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getTotal();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setTotal(Float total);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="to_process"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getToProcess();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setToProcess(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="balance"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getBalance();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setBalance(Float balance);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="carried_balance"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getCarriedBalance();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCarriedBalance(Float amount);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="in_process_payment"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getInProcessPayment();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setInProcessPayment(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="deleted"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDeleted();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDeleted(Integer deleted);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="payment_attempts"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPaymentAttempts();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPaymentAttempts(Integer attempt);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="is_review"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIsReview();
    /**
     * @ejb:interface-method view-type="local"
     * @param flag
     */
    public abstract void setIsReview(Integer flag);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="currency_id"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Integer getCurrencyId();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setCurrencyId(Integer currencyId);
     
     /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="customer_notes"
     * @jboss.method-attributes read-only="true"
      */
     public abstract String getCustomerNotes();
     /**
      * @ejb:interface-method view-type="local"
      */
     public abstract void setCustomerNotes(String notes);

     /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="public_number"
     * @jboss.method-attributes read-only="true"
      */
     public abstract String getNumber();
     /**
      * @ejb:interface-method view-type="local"
      */
     public abstract void setNumber(String number);

     /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="overdue_step"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Integer getOverdueStep();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setOverdueStep(Integer step);
 
    // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     */
    public Collection<OrderProcessDTO> getOrders() {
    	InvoiceDAS das = new InvoiceDAS();
    	Invoice dto = das.find(getId());
    	return dto.getOrderProcesses();
    }
    /*
    public void setOrders(Collection<OrderDTO> orders) {
    	
    }
    */

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoice-invoice_lines"
     *               role-name="invoice-has-lines"
     */
    public abstract Collection getInvoiceLines();
    public abstract void setInvoiceLines(Collection lines);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-invoices"
     *               role-name="invoice-belongs-to-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     */
    public abstract UserEntityLocal getUser();
    public abstract void setUser(UserEntityLocal user);

    /**
     * This is a circular relationship
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoices-invoice"
     *               role-name="due_invoices-carries_to-invoice"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="delegated_invoice_id"            
     */
    public abstract InvoiceEntityLocal getDelegatedInvoice();
    public abstract void setDelegatedInvoice(InvoiceEntityLocal invoice);

    /**
     * This is a circular relationship
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoices-invoice"
     *               role-name="invoice-includes-due_invoices"
     */
    public abstract Collection getIncludedInvoices();
    public abstract void setIncludedInvoices(Collection invoices);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payments-invoices"
     *               role-name="invoice-gets_paid_by-payment"
     */
    public abstract Collection getPaymentMap();
    public abstract void setPaymentMap(Collection payments);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="process-invoices"
     *               role-name="invoice-belongs-to-process"
     *               cascade-delete="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="billing_process_id"            
     */
    public abstract BillingProcessEntityLocal getBillingProcess();
    public abstract void setBillingProcess(BillingProcessEntityLocal billing);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoices-paper_batch"
     *               role-name="invoice-is_in-paper_batch"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="paper_invoice_batch_id"            
     */
    public abstract PaperInvoiceBatchEntityLocal getBatch();
    public abstract void setBatch(PaperInvoiceBatchEntityLocal batch);

    // EJB callbacks -----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove()
        throws RemoveException, EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() throws EJBException, RemoteException {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext arg0)
            throws EJBException, RemoteException {
        try { 
            log = Logger.getLogger(this.getClass());
        } catch (Exception e) {}
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
