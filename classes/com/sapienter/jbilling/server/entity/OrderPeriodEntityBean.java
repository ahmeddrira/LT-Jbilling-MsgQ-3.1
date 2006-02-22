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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocal;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="OrderPeriodEntity" 
 *          display-name="Object representation of the table order_period"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/OrderPeriodEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/OrderPeriodEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="order_period"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb.value-object name="OrderPeriod"
 * 
 * @ejb:finder signature="Collection findByEntity(java.lang.Integer entityId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM order_period a 
 *                     WHERE a.entityId = ?1"
 *             result-type-mapping="Local"
 *
 * @jboss:table-name "order_period"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */

public abstract class OrderPeriodEntityBean implements EntityBean {

    private Logger log = null;

    private DescriptionEntityLocal getDescriptionObject(Integer language) {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            DescriptionEntityLocalHome descriptionHome =
                (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);

            return descriptionHome.findIt(
                Constants.TABLE_ORDER_PERIOD,
                getId(),
                "description",
                language);
        } catch (FinderException e1) {
            log.warn("The order period " + getId() + " doesn't have a " +
                    "description in language " + language);
            return null;
        } catch (Exception e) {
            log.error(
                "Exception while looking for an order_period description",
                e);
            return null;
        }
    }
    
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, Integer unitId, Integer value,
            String description, Integer languageId) 
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
                    Constants.TABLE_ORDER_PERIOD));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the order_period table");
        }

        setId(newId);
        setUnitId(unitId);
        setValue(value);
        setEntityId(entityId);
        
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            DescriptionEntityLocalHome descriptionHome =
                (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);
            
            descriptionHome.create(Constants.TABLE_ORDER_PERIOD, newId, "description", 
                    languageId, description);
        } catch (Exception e) {
            log.error(e);
        }
        
        return newId;
    }
    public void ejbPostCreate(Integer entityId, Integer unitId, Integer value,
            String description, Integer languageId) {}


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
    public abstract void setEntityId(Integer entity);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getValue();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setValue(Integer value);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="unit_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getUnitId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setUnitId(Integer unit);

    // Custom field accessors --------------------------------------------------

    /**
     * @ejb:interface-method view-type="local"
     */
    public String getDescription(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language);
        if (desc == null) {
            return "Description not set for this order period";
        } 
        return desc.getContent();
    }

    /**
     * @ejb:interface-method view-type="local"
     */
    public void setDescription(String description, Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language);
        if (desc == null) {
            log.error("Can't update a non existing record");
        } else {
            desc.setContent(description);
        }
    }

    // CMR
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="order-order_period"
     *               role-name="order_period-provides"
     */
    public abstract Collection getOrders();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOrders(Collection order);

    // callbacks
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
        log = Logger.getLogger(OrderPeriodEntityBean.class);            
    }

    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
