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
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.item.db.PromotionDAS;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="PromotionEntity" 
 *          display-name="Object representation of the table PROMOTION"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PromotionEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PromotionEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="promotion"
 *
 * @ejb.value-object name="Promotion"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "promotion"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PromotionEntityBean implements EntityBean {
    private Logger log = null;

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer itemId, String code, Integer once) 
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
                    Constants.TABLE_PROMOTION));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the promotion table");
        }
        setId(newId);
        setCode(code);
        setOnce(once);

        return newId;
    }
    
    public void ejbPostCreate(Integer itemId, String code, Integer once) {
        try {
            new PromotionDAS().find(getId()).setItem(new ItemDAS().find(itemId));
        } catch (Exception e) {
            log.error("Exception in post create:", e);
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
     * @jboss:column-name name="code"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getCode();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCode(String code);

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
     * @jboss:column-name name="once"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOnce();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOnce(Integer flag);

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

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="since"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getSince();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setSince(Date since);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="until"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getUntil();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setUntil(Date until);

    // CMR  fields -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     */
    public com.sapienter.jbilling.server.item.db.ItemDTO getItem() {
        return new PromotionDAS().find(getId()).getItem();
    }

    //public abstract void setItem(ItemEntityLocal item);

    /**
     * @ejb:interface-method view-type="local"
     */
    public Collection getUsers() {
        return new PromotionDAS().find(getId()).getBaseUsers();
    }
//    public abstract void setUsers(Collection users);
    
    // Select methods -------------
    /**
     * @ejb.select query="SELECT pr.userId
     *                      FROM promotion AS pr  
     *                     WHERE pr.code = ?2
     *                       AND pr.userId = ?1"
     */
    public abstract int ejbSelectUserUsedPromotion(Integer userId, 
            String code) throws FinderException;
    
    /**
     * @ejb:interface-method view-type="local"
     */
    public boolean userUsedPromotion(Integer entityId, String code) {
        try {
            ejbSelectUserUsedPromotion(entityId, code);
        } catch (FinderException e) {
            log.debug("not found");
            return false;
        }
        
        return true;
    }      
    
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
        log = Logger.getLogger(PromotionEntityBean.class);
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {

    }

}
