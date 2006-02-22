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

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="ItemTypeEntity" 
 *          display-name="Object representation of the table ITEM_TYPE"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ItemTypeEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ItemTypeEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="item_type"
 *
 * @ejb.value-object name="ItemType"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "item_type"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ItemTypeEntityBean implements EntityBean {
    private Logger log = null;

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, Integer orderLineTypeId, 
            String description) throws CreateException {
        Integer newId;
        log = Logger.getLogger(ItemTypeEntityBean.class);
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer( generator.getNextSequenceNumber(
                    Constants.TABLE_ITEM_TYPE));

        } catch (Exception e) {
            log.error("Exception generating pk for " + Constants.TABLE_ITEM_TYPE, e);
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the item_type table");
        }
        setId(newId);
        setEntityId(entityId);
        setDescription(description);
        setOrderLineTypeId(orderLineTypeId);

        return newId;
    }
    
    public void ejbPostCreate(Integer entityId, Integer orderLineTypeId, 
            String description) {
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
     * @jboss:column-name name="entity_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getEntityId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setEntityId(Integer entityId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="description"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getDescription();

    /**
     * @ejb:interface-method view-type="local"
     */
     public abstract void setDescription(String description); 

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="order_line_type_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOrderLineTypeId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOrderLineTypeId(Integer typeId);

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
        try { 
            Logger.getLogger(this.getClass()).debug("Activating..."); 
        } catch (Exception e) {}
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
        try { 
            log = Logger.getLogger(ItemTypeEntityBean.class);
        } catch (Exception e) {}
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {

    }

}
