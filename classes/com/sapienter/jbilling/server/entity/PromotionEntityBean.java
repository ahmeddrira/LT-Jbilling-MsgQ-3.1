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
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ItemEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.item.ItemBL;
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
 * @ejb:finder signature="PromotionEntityLocal findByEntityCode(java.lang.Integer entityId, java.lang.String code)"
 *             query="SELECT OBJECT(a) 
 *                      FROM promotion a 
 *                     WHERE a.code = ?2
 *                       AND a.item.entity.id = ?1
 *                       AND a.item.deleted = 0"
 *             result-type-mapping="Local"
 *
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
            ItemBL bl = new ItemBL(itemId);
            setItem(bl.getEntity());
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
     * @ejb.relation name="item-promotion"
     *               role-name="promotion-needs-item"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="item_id"            
     */
    public abstract ItemEntityLocal getItem();
    public abstract void setItem(ItemEntityLocal item);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="promotion-user"
     *               role-name="promotion-used_by-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     * @jboss.relation-table table-name="promotion_user_map"
     *                       create-table="false"
     */
    public abstract Collection getUsers();
    public abstract void setUsers(Collection users);
    
    // Select methods -------------
    /**
     * @ejb.select query="SELECT u.userId
     *                      FROM promotion AS pr, IN (pr.users) u  
     *                     WHERE pr.code = ?2
     *                       AND u.userId = ?1"
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
