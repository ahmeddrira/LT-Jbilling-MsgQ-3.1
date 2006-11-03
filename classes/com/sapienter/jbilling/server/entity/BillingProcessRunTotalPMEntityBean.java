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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PaymentMethodEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="BillingProcessRunTotalPMEntity" 
 *          display-name="Object representation of the table PROCESS_RUN_TOTAL_PM" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/BillingProcessRunTotalPMEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/BillingProcessRunTotalPMEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="process_run_total_pm"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb.value-object name="BillingProcessRunTotalPM"
 * 
 * @jboss:table-name "process_run_total_pm"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class BillingProcessRunTotalPMEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Float total) 
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
                    Constants.TABLE_BILLING_PROCESS_RUN_TOTAL_PM));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the process_run_total_pm table");
        }

        setId(newId);
        setTotal(total);
        
        return newId;
    }
    public void ejbPostCreate(Float total) {}


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
     * @jboss:column-name name="total"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getTotal();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setTotal(Float total);

    // CMR fields -------------------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="run-payment_methods"
     *               role-name="payment_method-belongs_to-total"
     *               cascade-delete="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="process_run_total_id"            
     */
    public abstract BillingProcessRunTotalEntityBean getRunTotal();
    public abstract void setRunTotal(BillingProcessRunTotalEntityBean runTotal);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="total-payment_method"
     *               role-name="total-has-payment_method"
     *               target-ejb="PaymentMethodEntity"
     *               target-role-name="payment_method-gives-total"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="payment_method_id"            
     */
    public abstract PaymentMethodEntityLocal getPaymentMethod();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPaymentMethod(PaymentMethodEntityLocal method);

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
