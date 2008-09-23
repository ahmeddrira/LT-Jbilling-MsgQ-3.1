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
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.AchEntityLocal;
import com.sapienter.jbilling.interfaces.CreditCardEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentEntityLocal;
import com.sapienter.jbilling.interfaces.PaymentInfoChequeEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.payment.db.PaymentDAS;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.user.partner.db.PartnerPayout;
import com.sapienter.jbilling.server.user.partner.db.PartnerPayoutDAS;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="PaymentEntity" 
 *          display-name="Object representation of the table PAYMENT" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PaymentEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PaymentEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="payment"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @ejb:finder signature="Collection findWithBalance(java.lang.Integer userId)"
 *             query="SELECT OBJECT(p) 
 *                      FROM payment p
 *                     WHERE p.userId = ?1
 *                       AND p.balance >= 0.01
 *                       AND p.isRefund = 0
 *                       AND p.isPreauth = 0
 *                       AND p.deleted = 0"
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="Collection findPreauth(java.lang.Integer userId)"
 *             query="SELECT OBJECT(p) 
 *                      FROM payment p
 *                     WHERE p.userId = ?1
 *                       AND p.balance >= 0.01
 *                       AND p.isRefund = 0
 *                       AND p.isPreauth = 1
 *                       AND p.deleted = 0"
 *             result-type-mapping="Local"
 * 
 * @ejb.value-object name="Payment"
 * 
 * @jboss:table-name "payment"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PaymentEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Float amount, Integer methodId, Integer userId,
            Integer attempt, Integer result, Integer currencyId) 
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
                    Constants.TABLE_PAYMENT));

        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the purchase order table");
        }

        setId(newId);
        setAmount(amount);
        setMethodId(methodId);
        setCreateDateTime(Calendar.getInstance().getTime());
        setDeleted(new Integer(0));
        setAttempt(attempt);
        setResultId(result);
        setCurrencyId(currencyId);
        // by default, this is a normal payment, not a refund
        setIsRefund(new Integer(0));
        setIsPreauth(new Integer(0));
        setUserId(userId);
        
        return newId;
    }
    
    public void ejbPostCreate(Float amount, Integer methodId, Integer userId,
            Integer attempt, Integer result, Integer currencyId) {

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
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAmount(Float amount);

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
     * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDateTime();
    public abstract void setCreateDateTime(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="update_datetime"
     */
    public abstract Date getUpdateDateTime();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setUpdateDateTime(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="payment_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getPaymentDate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPaymentDate(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="attempt"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getAttempt();
    public abstract void setAttempt(Integer attempt);
    
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
    public abstract void setDeleted(Integer del);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="method_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getMethodId();
    
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMethodId(Integer method);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="result_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getResultId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setResultId(Integer result);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="is_refund"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIsRefund();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setIsRefund(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="is_preauth"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIsPreauth();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setIsPreauth(Integer flag);

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
      * @jboss:column-name name="user_id"
      * @jboss.method-attributes read-only="true"
      */
     public abstract Integer getUserId();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setUserId(Integer id);

    // CMR fields -------------------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="invoices-payments"
     *               role-name="payment-pays-invoice"
     */
    public abstract Collection getInvoicesMap();
    public abstract void setInvoicesMap(Collection invoices);

    /**
     * @ejb:interface-method view-type="local"
     */
    public UserDTO getUser() {
        return new UserDAS().find(getUserId());
    }
    public void setUser(UserDTO user) {
        setUserId(user.getUserId());
    }

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payment-cc_info"
     *               role-name="payment-has-cc_info"
     *               target-ejb="CreditCardEntity"
     *               target-role-name="cc_info-belongs_to-payment"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="credit_card_id"            
     */
    public abstract CreditCardEntityLocal getCreditCardInfo();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCreditCardInfo(CreditCardEntityLocal info);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payment-ach_info"
     *               role-name="payment-has-ach_info"
     *               target-ejb="AchEntity"
     *               target-role-name="ach_info-belongs_to-payment"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="ach_id"            
     */
    public abstract AchEntityLocal getAchInfo();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAchInfo(AchEntityLocal info);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payment-cheque_info"
     *               role-name="payment-has-cheque_info"
     *               target-ejb="PaymentInfoChequeEntity"
     *               target-role-name="cheque_info-belongs_to-payment"
     * @jboss.target-relation related-pk-field="id"  
     *                        fk-column="payment_id"            
     */
    public abstract PaymentInfoChequeEntityLocal getChequeInfo();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setChequeInfo(PaymentInfoChequeEntityLocal info);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="payment-authorization"
     *               role-name="payment-has-authorization"
     */
    public abstract Collection getAuthorizations();
    public abstract void setAuthorizations(Collection authorizations);

    /**
     * This is a circular relationship
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="refund-payment"
     *               role-name="refund-refunds-payment"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="payment_id"            
     */
    public abstract PaymentEntityLocal getPayment();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPayment(PaymentEntityLocal payment);

    /**
     * This is a circular relationship
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="refund-payment"
     *               role-name="payment-refunded_by-refund"
     */
    public abstract PaymentEntityLocal getRefund();
    public abstract void setRefund(PaymentEntityLocal refund);

    /**
     * A payout is really a payment. This relatinship links both rows.
     * @ejb:interface-method view-type="local"
     */
    public Set<PartnerPayout> getPayout() {
        return new PaymentDAS().find(getId()).getPartnerPayouts();
    }
    
    //public abstract void setPayout(PartnerPayoutEntityLocal payout);

    /**
     * A payment/refund from a customer that belongs to a partner would
     * have to be eventually included in a payout
     * @ejb:interface-method view-type="local"
     */
    public PartnerPayout getPayoutIncludedIn() {
        return new PartnerPayoutDAS().find(new PaymentDAS().find(getId()).getPayoutId());
    }
    /**
     * @ejb:interface-method view-type="local"
     */
    public void setPayoutIncludedIn(PartnerPayout payout) {
        new PaymentDAS().find(getId()).setPayoutId(payout.getId());
    }

    // EJB Callbacks ------------------------------------------------------------
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
