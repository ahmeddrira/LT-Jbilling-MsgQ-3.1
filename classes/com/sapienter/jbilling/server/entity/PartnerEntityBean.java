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

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="PartnerEntity" 
 *          display-name="Object representation of the table PARTNER" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PartnerEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PartnerEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="partner"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * @ejb.value-object name="Partner"
 * 
 * @jboss:table-name "partner"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PartnerEntityBean implements EntityBean {
    
    private Logger log = null;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Float balance, Float totalPayments, 
            Float totalRefunds, Float totalPayouts, Integer oneTime,
            Integer periodUnit, Integer periodValue, Date nextPayout,
            Integer automaticProcess) 
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
                    Constants.TABLE_PARTNER));

        } catch (Exception e) {
            log.error("Exception creating partner", e);
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the item_price table");
        }

        setId(newId);
        setBalance(balance);
        setTotalPayments(totalPayments);
        setTotalRefunds(totalRefunds);
        setTotalPayouts(totalPayouts);
        setOneTime(oneTime);
        setPeriodUnitId(periodUnit);
        setPeriodValue(periodValue);
        setNextPayoutDate(nextPayout);
        setAutomaticProcess(automaticProcess);

        return newId;
    }

    public void ejbPostCreate(Float balance, Float totalPayments, 
            Float totalRefunds, Float totalPayouts, Integer oneTime,
            Integer periodUnit, Integer periodValue, Date nextPayout,
            Integer automaticProcess) {
        
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
     * @jboss:column-name name="total_payments"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getTotalPayments();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setTotalPayments(Float total);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="total_refunds"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getTotalRefunds();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setTotalRefunds(Float total);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="total_payouts"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getTotalPayouts();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setTotalPayouts(Float total);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="percentage_rate"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getPercentageRate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPercentageRate(Float rate);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="referral_fee"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getReferralFee();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setReferralFee(Float fee);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="fee_currency_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getFeeCurrencyId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setFeeCurrencyId(Integer currencyId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="one_time"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOneTime();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOneTime(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="period_unit_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPeriodUnitId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPeriodUnitId(Integer unitId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="period_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPeriodValue();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPeriodValue(Integer value);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="next_payout_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getNextPayoutDate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setNextPayoutDate(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="due_payout"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getDuePayout();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDuePayout(Float amount);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="automatic_process"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getAutomaticProcess();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAutomaticProcess(Integer flag);

    // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="partner-user"
     *               role-name="partner-is-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     */
    public abstract UserEntityLocal getUser();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setUser(UserEntityLocal user);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="customer-partner"
     *               role-name="partner-owns-customers"
     */
    public abstract Collection getCustomer();
    public abstract void setCustomer(Collection customer);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="partner-ranges"
     *               role-name="partner-has-ranges"
     */
    public abstract Collection getRanges();
    public abstract void setRanges(Collection ranges);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="partner-clerk"
     *               role-name="partner-has-clerk"
     *               target-ejb="UserEntity"
     *               target-role-name="clerk-takes_care_of-partner"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="related_clerk"            
     */
    public abstract UserEntityLocal getRelatedClerk();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setRelatedClerk(UserEntityLocal user);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="partner-payouts"
     *               role-name="partner-has-payouts"
     */
    public abstract Collection getPayouts();
    public abstract void setPayouts(Collection payouts);
 
    //  EJB callbacks -----------------------------------------------------------
    /**
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove()
        throws RemoveException, EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext arg0)
            throws EJBException, RemoteException {
        log = Logger.getLogger(PartnerEntityBean.class);
    }

    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
