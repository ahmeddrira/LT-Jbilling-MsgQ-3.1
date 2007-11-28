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

/*
 * Created on Apr 2, 2004
 *
 * Copyright Sapienter Enterprise Software
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
import javax.naming.NamingException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PartnerEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="PartnerPayoutEntity" 
 *          display-name="Object representation of the table PARTNER_PAYOUT" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PartnerPayoutEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PartnerPayoutEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="partner_payout"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @ejb.value-object name="PartnerPayout"
 * 
 * @jboss:table-name "partner_payout"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PartnerPayoutEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Date startDate, Date endDate, Float payments, 
            Float refuds, Float balance) 
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
                    Constants.TABLE_PARTNER_PAYOUT));

        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the partner_payout table");
        }

        setId(newId);
        setStartingDate(startDate);
        setEndingDate(endDate);
        setPaymentsAmount(payments);
        setRefundsAmount(refuds);
        setBalanceLeft(balance);
        
        return newId;
    }
    
    public void ejbPostCreate(Date startDate, Date endDate, Float payments, 
            Float refuds, Float balance) {
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
    public abstract void setId(Integer id);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="starting_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getStartingDate();
    public abstract void setStartingDate(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="ending_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getEndingDate();
    public abstract void setEndingDate(Date date);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="payments_amount"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getPaymentsAmount();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPaymentsAmount(Float payments);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="refunds_amount"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getRefundsAmount();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setRefundsAmount(Float refunds);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="balance_left"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getBalanceLeft();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setBalanceLeft(Float balance);

    // CMR fileds --------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="partner-payouts"
     *               role-name="payout-belongs_to-partner"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="partner_id"            
     */
    public abstract PartnerEntityLocal getPartner();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPartner(PartnerEntityLocal partner);

    /**
     * A payout is really a payment. This relatinship links both rows.
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payout-payment"
     *               role-name="payout-is-payment"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="payment_id"            
     */
    public abstract PaymentEntityLocal getPayment();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPayment(PaymentEntityLocal payment);
 
    /**
     * A payment/refund from a customer that belongs to a partner would
     * have to be eventually included in a payout
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payments-payout"
     *               role-name="payout-is_made_of-payments"
     */
    public abstract Collection getIncludedPayments();
    public abstract void setIncludedPayments(Collection payments);
    
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
