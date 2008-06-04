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
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.CustomerEntityLocal;
import com.sapienter.jbilling.interfaces.PartnerEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="CustomerEntity" 
 *          display-name="Object representation of the table CUSTOMER" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/CustomerEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/CustomerEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="customer"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb.value-object name="Customer"
 *
 * @jboss:table-name "customer"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class CustomerEntityBean implements EntityBean {
    private Logger log = null;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate() 
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
                    Constants.TABLE_CUSTOMER));

        } catch (Exception e) {
            log.error("Exception creating customer", e);
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the customer table");
        }

        setId(newId);
        setInvoiceDeliveryMethodId(Constants.D_METHOD_EMAIL);
        setExcludeAging(new Integer(0)); // defaults to included in aging

        return newId;
    }

    public void ejbPostCreate() {
        
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
     * @jboss:column-name name="referral_fee_paid"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getReferralFeePaid();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setReferralFeePaid(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="notes"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getNotes();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setNotes(String notes);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="invoice_delivery_method_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getInvoiceDeliveryMethodId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setInvoiceDeliveryMethodId(Integer methodId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="auto_payment_type"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getAutoPaymentType();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAutoPaymentType(Integer type);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="due_date_unit_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDueDateUnitId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDueDateUnitId(Integer unit);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="due_date_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDueDateValue();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDueDateValue(Integer value);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="df_fm"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDfFm();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDfFm(Integer flag);
 
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="is_parent"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIsParent();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setIsParent(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="exclude_aging"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getExcludeAging();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setExcludeAging(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="invoice_child"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getInvoiceChild();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setInvoiceChild(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="current_order_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getCurrentOrderId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCurrentOrderId(Integer orderId);
    // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="customer-user"
     *               role-name="customer-is-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     */
    public abstract UserEntityLocal getUser();
    public abstract void setUser(UserEntityLocal user);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="customer-partner"
     *               role-name="customer-belongs_to-partner"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="partner_id"            
     */
    public abstract PartnerEntityLocal getPartner();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPartner(PartnerEntityLocal partner);

    /**
     * This is a circular relationship
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="children-parent"
     *               role-name="child-belongs_to-parent"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="parent_id"            
     */
    public abstract CustomerEntityLocal getParent();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setParent(CustomerEntityLocal customer);

    /**
     * This is a circular relationship
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="children-parent"
     *               role-name="parent-has-children"
     */
    public abstract Collection getChildren();
    public abstract void setChildren(Collection children);

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
        log = Logger.getLogger(CustomerEntityBean.class);
    }

    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
