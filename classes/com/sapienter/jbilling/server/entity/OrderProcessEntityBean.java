/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.BillingProcessEntityLocal;
import com.sapienter.jbilling.interfaces.InvoiceEntityLocal;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="OrderProcessEntity" 
 *          display-name="Object representation of the table ORDER_PROCESS" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/OrderProcessEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/OrderProcessEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="order_process"
 *
 * @ejb.value-object name="OrderProcess"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "order_process"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class OrderProcessEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Date start, Date end, Integer isReview)
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
                        Constants.TABLE_ORDER_PROCESS));
        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the order_invoice_period table");
        }
        setId(newId);
        setPeriodStart(start);
        setPeriodEnd(end);
        setIsReview(isReview);
        
        return newId;
    }
    
    public void ejbPostCreate(Date start, Date end, Integer isReview) {}
 
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
      * @jboss:column-name name="period_start"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Date getPeriodStart();
    public abstract void setPeriodStart(Date start);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="period_end"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Date getPeriodEnd();
    public abstract void setPeriodEnd(Date end);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="periods_included"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getPeriodsIncluded();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setPeriodsIncluded(Integer periods);

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
     * @jboss:column-name name="origin"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOrigin();
    /**
     * @ejb:interface-method view-type="local"
     * @param flag
     */
    public abstract void setOrigin(Integer code);

    //  CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="orders-period"
     *               role-name="period-belongs_to-order"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="order_id"
     */
    public abstract OrderEntityLocal getOrder();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setOrder(OrderEntityLocal order);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoices-period"
     *               role-name="period-belongs_to-invoice"
     *               cascade-delete="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="invoice_id"
     */
    public abstract InvoiceEntityLocal getInvoice();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setInvoice(InvoiceEntityLocal invoice);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="process-orders"
     *               role-name="process-belongs_to-order"
     *               cascade-delete="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="billing_process_id"
     */
    public abstract BillingProcessEntityLocal getProcess();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setProcess(BillingProcessEntityLocal process);

    //  EJB callbacks -----------------------------------------------------
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
