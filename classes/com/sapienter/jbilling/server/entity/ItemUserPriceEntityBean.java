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
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ItemEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="ItemUserPriceEntity" 
 *          display-name="Object representation of the table ITEM_USER_PRICE"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ItemUserPriceEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ItemUserPriceEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="item_user_price"
 *
 * @ejb.value-object name="ItemUserPrice"
 *
 * @ejb:finder signature="Collection findByUserItem(java.lang.Integer userId, java.lang.Integer itemId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM item_user_price a 
 *                     WHERE a.user.userId = ?1
 *                       AND a.item.id = ?2 "
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="ItemUserPriceEntityLocal find(java.lang.Integer userId, java.lang.Integer itemId, java.lang.Integer currencyId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM item_user_price a 
 *                     WHERE a.user.userId = ?1
 *                       AND a.currencyId = ?3
 *                       AND a.item.id = ?2 "
 *             result-type-mapping="Local"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "item_user_price"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ItemUserPriceEntityBean implements EntityBean {
    private Logger log = null;

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer userId, Integer itemId, Float price,
            Integer currencyId) 
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
                    Constants.TABLE_ITEM_USER_PRICE));

        } catch (Exception e) {
            log.error("Exception creating price", e);
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the item_user_price table");
        }
        setId(newId);
        setPrice(price);
        setCurrencyId(currencyId);

        return newId;
    }
    
    public void ejbPostCreate(Integer userId, Integer itemId, Float price,
            Integer currencyId) {
        try {
            ItemBL itemBl = new ItemBL(itemId);
            setItem(itemBl.getEntity());
            
            UserBL userBl = new UserBL(userId);
            setUser(userBl.getEntity());
        } catch (Exception e) {
            log.error("Exceptoin", e);
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
    public abstract Integer getId();
    public abstract void setId(Integer itemId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="price"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getPrice();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setPrice(Float price);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="currency_id"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getCurrencyId();
    public abstract void setCurrencyId(Integer currencyId);

    // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-item_prices"
     *               role-name="prices-belong_to-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     */
    public abstract UserEntityLocal getUser();
    public abstract void setUser(UserEntityLocal user);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="item-user_prices"
     *               role-name="prices-belong_to-item"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="item_id"            
     */
    public abstract ItemEntityLocal getItem();
    public abstract void setItem(ItemEntityLocal item);

    // EJB callback -----------------------------------------------------
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
        log = Logger.getLogger(ItemUserPriceEntityBean.class);
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {

    }

}
