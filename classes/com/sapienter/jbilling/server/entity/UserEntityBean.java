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

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.AchEntityLocal;
import com.sapienter.jbilling.interfaces.CustomerEntityLocal;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.EntityEntityLocalHome;
import com.sapienter.jbilling.interfaces.PartnerEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserStatusEntityLocal;
import com.sapienter.jbilling.interfaces.UserStatusEntityLocalHome;
import com.sapienter.jbilling.interfaces.SubscriptionStatusEntityLocal;
import com.sapienter.jbilling.interfaces.SubscriptionStatusEntityLocalHome;
import com.sapienter.jbilling.server.system.event.EventManager;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="UserEntity" 
 *          display-name="representation of the table BASE_USER" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/UserEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/UserEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="userId"
 *          schema="base_user"
 *
 * @ejb.value-object name="User"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 *
 * @ejb:finder signature="UserEntityLocal findByUserName(java.lang.String userName, java.lang.Integer entityId)"
 *             query="SELECT OBJECT(b) 
 *                      FROM base_user b 
 *                     WHERE b.userName = ?1
 *                       AND b.entity.id = ?2
 *                       AND b.deleted = 0"
 *             result-type-mapping="Local"
 *
 * @ejb.finder signature="UserEntityLocal findRoot(java.lang.String userName)"
 *             query=" select OBJECT(bu)
 *                       from base_user AS bu, IN( bu.roles ) AS r
 *                      where bu.userName = ?1 
 *                        and bu.deleted = 0
 *                        and r.id = 2"
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="Collection findAgeing()"
 *             query="SELECT OBJECT(a) 
 *                      FROM base_user a 
 *                     WHERE a.status.id > 1
 *                       AND a.status.id <> 8
 *                       AND a.customer.excludeAging = 0
 *                       AND a.deleted = 0"
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="Collection findInStatus(java.lang.Integer entityId, java.lang.Integer statusId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM base_user a 
 *                     WHERE a.status.id = ?2
 *                       AND a.entity.id = ?1
 *                       AND a.deleted = 0"
 *             result-type-mapping="Local"
 * 
 * @ejb:finder signature="Collection findNotInStatus(java.lang.Integer entityId, java.lang.Integer statusId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM base_user a 
 *                     WHERE a.status.id <> ?2
 *                       AND a.entity.id = ?1
 *                       AND a.deleted = 0"
 *             result-type-mapping="Local"
 * 
 * @ejb:finder signature="Collection findByCustomField(java.lang.Integer entityId, java.lang.Integer typeId, java.lang.String value)"
 *             query="SELECT OBJECT(a) 
 *                      FROM base_user a, contact_map cm, IN (cm.contact.fields) f
 *                     WHERE a.entity.id = ?1
 *                       AND a.userId = cm.foreignId
 *                       AND cm.tableId = 10
 *                       AND cm.type.isPrimary = 1
 *                       AND f.typeId = ?2
 *                       AND f.content = ?3
 *                       AND a.deleted = 0"
 *             result-type-mapping="Local"
 *
 * @jboss:table-name "base_user"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 * 
 */

public abstract class UserEntityBean implements EntityBean {
    private static final Logger LOG = Logger.getLogger(UserEntityBean.class); 
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, String userName,
            String password, Integer languageId, Integer currencyId,
			Integer statusId, Integer subscriptionStatusId) 
            throws CreateException {
        Integer newId;
        try {
        	JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer( generator.getNextSequenceNumber(
                    Constants.TABLE_BASE_USER));

        } catch (Exception e) {
            LOG.debug("Can't create primary key for base_user", e);
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the base_user table:" + e);
        }
        setUserId(newId);
        setUserName(userName);
        setPassword(password);
        setLanguageId(languageId);
        setDeleted(new Integer(0));
        setCurrencyId(currencyId);
        setCreateDateTime(Calendar.getInstance().getTime());       

        return newId;
    }

    public void ejbPostCreate(Integer entityId, String userName, 
            String password, Integer languageId, Integer currencyId,
			Integer statusId, Integer subscriptionStatusId) {
        try {
        	JNDILookup EJBFactory = JNDILookup.getFactory(false);
            // set the entity
            EntityEntityLocalHome entityHome =
                    (EntityEntityLocalHome) EJBFactory.lookUpLocalHome(
                    EntityEntityLocalHome.class,
                    EntityEntityLocalHome.JNDI_NAME);
            EntityEntityLocal entityRow = entityHome.findByPrimaryKey(entityId);
            setEntity(entityRow);
            // set the status 
            UserStatusEntityLocalHome statusHome =
                    (UserStatusEntityLocalHome) EJBFactory.lookUpLocalHome(
                    UserStatusEntityLocalHome.class,
                    UserStatusEntityLocalHome.JNDI_NAME);
            UserStatusEntityLocal statusRow = statusHome.findByPrimaryKey(
                    statusId);
            setStatus(statusRow);

            SubscriptionStatusEntityLocalHome subStatusHome =
                (SubscriptionStatusEntityLocalHome) EJBFactory.lookUpLocalHome(
                SubscriptionStatusEntityLocalHome.class,
                SubscriptionStatusEntityLocalHome.JNDI_NAME);
            SubscriptionStatusEntityLocal status = subStatusHome.findByPrimaryKey(
                    subscriptionStatusId);
            setSubscriptionStatus(status);
            
            LOG.debug("created user with subscirber = " + subscriptionStatusId);

        } catch (Exception e) {
            LOG.fatal("exception in post create",e);
        }
    }
    
    // Custom field accessors --------------------------------------------------
    /**
     * Returns the language id of the user, or the entity one if the user has
     * none specified.
     * @ejb:interface-method view-type="local"
     */
    public Integer getLanguageIdField() {
        Integer retValue = getLanguageId();
        if (retValue == null) {
            retValue = getEntity().getLanguageId();
        }
        
        return retValue;
    }

    /**    
     * @ejb:interface-method view-type="local"
     */
    public void setStatusId(Integer statusId) {
        try {
        	JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserStatusEntityLocalHome statusHome =
                    (UserStatusEntityLocalHome) EJBFactory.lookUpLocalHome(
                    UserStatusEntityLocalHome.class,
                    UserStatusEntityLocalHome.JNDI_NAME);
            UserStatusEntityLocal statusRow = statusHome.findByPrimaryKey(statusId);
            setStatus(statusRow);
        } catch (Exception e) {
            LOG.error("exception setting the status id:" + statusId, e);
        }
        
    }         

    // CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getUserId();
    public abstract void setUserId(Integer userId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="user_name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getUserName();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setUserName(String userName);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="password"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getPassword();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPassword(String password);

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
      * This doesn't go to the local interface, use getLanguageField instead
      * @ejb:persistent-field
      * @jboss:column-name name="language_id"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Integer getLanguageId();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setLanguageId(Integer languageId);
     
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
      * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Date getCreateDateTime();
     public abstract void setCreateDateTime(Date date);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="last_status_change"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Date getLastStatusChange();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setLastStatusChange(Date date);

     /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="last_login"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Date getLastLogin();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setLastLogin(Date date);
     
    // CMR field accessors -----------------------------------------------------
    /**
      * @ejb:interface-method view-type="local"
      * @ejb.relation name="user-type"
      *               role-name="user-belongs-to-type"
      *               target-ejb="UserTypeEntity"
      *               target-role-name="type-has-users"
      *               target-multiple="yes"
      * @jboss.relation related-pk-field="id"  
      *                 fk-column="type_id"            
      
     public abstract UserTypeEntityLocal getType();
    
     * @ejb:interface-method view-type="local"
     public abstract void setType(UserTypeEntityLocal type);
     */

    /**
      * @ejb:interface-method view-type="local"
      * @ejb.relation name="user-status"
      *               role-name="user-belongs-to-status"
      *               target-ejb="UserStatusEntity"
      *               target-role-name="status-marks-users"
      *               target-multiple="yes"
      * @jboss.relation related-pk-field="id"  
      *                 fk-column="status_id"            
      */
     public abstract UserStatusEntityLocal getStatus();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setStatus(UserStatusEntityLocal status);

     /**
      * @ejb:interface-method view-type="local"
      * @ejb.relation name="user-subscriber-status"
      *               role-name="user-belongs-to-s_status"
      *               target-ejb="SubscriptionStatusEntity"
      *               target-role-name="status-marks-users"
      *               target-multiple="yes"
      * @jboss.relation related-pk-field="id"  
      *                 fk-column="subscriber_status"            
      */
     public abstract SubscriptionStatusEntityLocal getSubscriptionStatus();
    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setSubscriptionStatus(SubscriptionStatusEntityLocal status);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-orders"
     *               role-name="user-has-orders"
     */
    public abstract Collection getOrders();
    public abstract void setOrders(Collection orders);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-invoices"
     *               role-name="user-has-invoices"
     */
    public abstract Collection getInvoices();
    public abstract void setInvoices(Collection invoices);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-users"
     *               role-name="user-belongs-to-entity"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="entity_id"            
     */
    public abstract EntityEntityLocal getEntity();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setEntity(EntityEntityLocal entity);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-payments"
     *               role-name="user-has-payments"
     */
    public abstract Collection getPayments();
    public abstract void setPayments(Collection payments);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-archived_messages"
     *               role-name="user-has-archived_messages"
     *               target-ejb="NotificationMessageArchiveEntity"
     *               target-role-name="archived_message-belongs_to-user"
     * @jboss.target-relation related-pk-field="userId"  
     *                        fk-column="user_id"            
     */
    public abstract Collection getArchivedMessages();
    public abstract void setArchivedMessages(Collection messages);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-reports"
     *               role-name="user-has-reports"
     */
    public abstract Collection getReports();
    public abstract void setReports(Collection reports);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-events"
     *               role-name="user-has-events"
     */
    public abstract Collection getEvents();
    public abstract void setEvents(Collection events);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="role-user"
     *               role-name="user-has-roles"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="role_id"            
     * @jboss.relation-table table-name="user_role_map"
     *                       create-table="false"
     */
    public abstract Collection getRoles();
    public abstract void setRoles(Collection roles);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="permission-user"
     *               role-name="user-has-permissions"
     */
    public abstract Collection getPermissions();
    public abstract void setPermissions(Collection permissions);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="partner-user"
     *               role-name="user-is-partner"
     */
    public abstract PartnerEntityLocal getPartner();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPartner(PartnerEntityLocal partner);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="customer-user"
     *               role-name="user-is-customer"
     */
    public abstract CustomerEntityLocal getCustomer();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCustomer(CustomerEntityLocal customer);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-item_prices"
     *               role-name="user-has-prices"
     */
    public abstract Collection getItemPrices();
    public abstract void setItemPrices(Collection itemPrices);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="promotion-user"
     *               role-name="user-used-promotions"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="promotion_id"            
     * @jboss.relation-table table-name="promotion_user_map"
     *                       create-table="false"
     */
    public abstract Collection getPromotions();
    public abstract void setPromotions(Collection promotions);

    /**
     * This is not really a many-to-many relationship, but since the
     * other side is blind it doesn't make any difference.
     * 
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-cc_info"
     *               role-name="user-has-cc_info"
     *               target-ejb="CreditCardEntity"
     *               target-role-name="cc_info-belongs_to-user"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="credit_card_id"            
     * @jboss.target-relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     * @jboss.relation-table table-name="user_credit_card_map"
     *                       create-table="false"
     */
    public abstract Collection getCreditCard();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCreditCard(Collection cc);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-ach"
     *               role-name="user-has-ach"
     *               target-ejb="AchEntity"
     *               target-role-name="ach-belongs_to-user"
     * @jboss.target-relation related-pk-field="userId"  
     *                        fk-column="user_id"            
     */
    public abstract AchEntityLocal getAch();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAch(AchEntityLocal ach);

    // EJB Callbacks -----------------------------------------------------
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
