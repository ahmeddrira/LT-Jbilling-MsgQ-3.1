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

 package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PaperInvoiceBatchEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="BillingProcessEntity" 
 *          display-name="Object representation of the table BILLING_PROCESS" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/BillingProcessEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/BillingProcessEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="billing_process"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @ejb:finder signature="BillingProcessEntityLocal findReview(java.lang.Integer entityId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM billing_process a 
 *                     WHERE a.entityId = ?1
 *                       AND a.isReview = 1"
 *             result-type-mapping="Local"
 * 
 * @ejb.value-object name="BillingProcess"
 * 
 * @jboss:table-name "billing_process"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class BillingProcessEntityBean implements EntityBean {
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, Date billingProcess,
            Integer periodId, Integer periodValue, Integer retries)
        throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId =
                new Integer(
                    generator.getNextSequenceNumber(
                        Constants.TABLE_BILLING_PROCESS));
        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the billing process table");
        }
        setId(newId);
        setEntityId(entityId);
        setBillingDate(billingProcess);
        setPeriodUnitId(periodId);
        setPeriodValue(periodValue);
        setIsReview(new Integer(0));
        setRetriesToDo(retries);
        return newId;
    }

    public void ejbPostCreate(
        Integer entityId,
        Date billingProcess,
        Integer periodId,
        Integer periodValue, Integer retries) {
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
     * @jboss:column-name name="entity_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getEntityId();
    public abstract void setEntityId(Integer entityId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="billing_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getBillingDate();
    public abstract void setBillingDate(Date billing);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="period_unit_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPeriodUnitId();
    public abstract void setPeriodUnitId(Integer period);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="period_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPeriodValue();
    public abstract void setPeriodValue(Integer periodValue);

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
     * @jboss:column-name name="retries_to_do"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getRetriesToDo();
    /**
     * @ejb:interface-method view-type="local"
     * @param flag
     */
    public abstract void setRetriesToDo(Integer number);

    // CMR fields -------------------------------------------------------------
    /**
     * This realtionship will give access to the order entities and to the
     * order process information (period billed). It gives access to the
     * invoices too , but this is inderectly and is better to use getInvoices.
     * The process will go over orders. This will happen only in the first run.
     * Subsequent runs will go over only invoices, not orders. Thus, the
     * relationship is between the process and the orders, instead of the run
     * and the orders.
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="process-orders"
     *               role-name="process-generates-invoices"
     */
    public abstract Collection getOrderProcesses();
    public abstract void setOrderProcesses(Collection processes);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="process-invoices"
     *               role-name="process-generates-invoices"
     */
    public abstract Collection getInvoices();
    public abstract void setInvoices(Collection invoices);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="process-runs"
     *               role-name="process-has-runs"
     */
    public abstract Collection getRuns();
    public abstract void setRuns(Collection runs);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="process-paper_batch"
     *               role-name="process-has-paper_batch"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="paper_invoice_batch_id"            
     */
    public abstract PaperInvoiceBatchEntityLocal getBatch();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setBatch(PaperInvoiceBatchEntityLocal batch);

    // EJB Callbacks -------------------------------------------------------------

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
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
