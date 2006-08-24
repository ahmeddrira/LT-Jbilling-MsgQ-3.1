package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="PaymentInvoiceMapEntity" 
 *          display-name="Object representation of the table PAYMENT_INVOICE" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PaymentInvoiceMapEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PaymentInvoiceMapEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="payment_invoice"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "payment_invoice"
 * 
 * @ejb.value-object name="PaymentInvoiceMap"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */

public abstract class PaymentInvoiceMapEntityBean implements EntityBean {
    private JNDILookup EJBFactory = null;

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(InvoiceEntityLocal invoice, 
            PaymentEntityLocal payment, Float amount)
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
                        Constants.TABLE_PAYMENT_INVOICE_MAP));

        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the purchase order table");
        }

        setId(newId);
        setCreateDateTime(Calendar.getInstance().getTime());
        setAmount(amount);

        return newId;
    }

    public void ejbPostCreate(InvoiceEntityLocal invoice, 
            PaymentEntityLocal payment, Float amount) {
        setInvoice(invoice);
        setPayment(payment);
    }

    //  CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer ruleId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="amount"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getAmount();
    public abstract void setAmount(Float amount);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDateTime();
    public abstract void setCreateDateTime(Date date);

    // CMR fields -------------------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payments-invoices"
     *               role-name="payment-pays-invoice_map"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="invoice_id"            
     */
    public abstract InvoiceEntityLocal getInvoice();
    public abstract void setInvoice(InvoiceEntityLocal invoice);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoices-payments"
     *          role-name="invoice-gets_paid_by-payment_map"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="payment_id"            
     */
    public abstract PaymentEntityLocal getPayment();
    public abstract void setPayment(PaymentEntityLocal payment);

    // EJB callback -------------------------------------------------------------
    public void ejbActivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    public void ejbLoad() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    public void ejbPassivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    public void ejbRemove() throws RemoveException, EJBException,
            RemoteException {
        // TODO Auto-generated method stub

    }

    public void ejbStore() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    public void setEntityContext(EntityContext arg0) throws EJBException,
            RemoteException {
        // TODO Auto-generated method stub

    }

    public void unsetEntityContext() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

}
