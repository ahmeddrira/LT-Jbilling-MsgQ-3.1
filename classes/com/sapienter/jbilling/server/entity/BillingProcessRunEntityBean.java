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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.BillingProcessEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="BillingProcessRunEntity" 
 *          display-name="Object representation of the table PROCESS_RUN" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/BillingProcessRunEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/BillingProcessRunEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="process_run"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb.value-object name="BillingProcessRun"
 * 
 * @jboss:table-name "process_run"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class BillingProcessRunEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Date runDate) 
            throws CreateException {

        Integer newId;

        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber(
                    Constants.TABLE_BILLING_PROCESS_RUN));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the process_run table");
        }

        setId(newId);
        setStarted(Calendar.getInstance().getTime());
        setRunDate(runDate);
        
        return newId;
    }
    public void ejbPostCreate(Date runDate) {}


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
     * @jboss:column-name name="run_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getRunDate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setRunDate(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="started"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getStarted();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setStarted(Date started);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="finished"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getFinished();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setFinished(Date finished);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="invoices_generated"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getInvoiceGenerated();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setInvoiceGenerated(Integer total);

    // CMR fields -------------------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="process-runs"
     *               role-name="runs-belongs_to-process"
     *               cascade-delete="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="process_id"            
     */
    public abstract BillingProcessEntityLocal getProcess();
    public abstract void setProcess(BillingProcessEntityLocal process);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="run-totals"
     *               role-name="run-has-totals"
     */
    public abstract Collection getTotals();
    public abstract void setTotals(Collection totals);

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
