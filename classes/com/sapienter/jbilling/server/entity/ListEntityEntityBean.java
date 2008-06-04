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

/*
 * Created on Nov 26, 2004
 *
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

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ListEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="ListEntityEntity" 
 *          display-name="Object representation of the table LIST_ENTITY" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ListEntityEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ListEntityEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="list_entity"
 * 
 * @ejb:finder signature="ListEntityEntityLocal findByEntity(java.lang.Integer id, java.lang.Integer entityId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM list_entity a 
 *                     WHERE a.list.id = ?1
 *                       AND a.entityId = ?2" 
 *             result-type-mapping="Local"
 *
 * @ejb.value-object name="ListEntity"
 * 
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "list_entity"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ListEntityEntityBean implements EntityBean {
    
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(ListEntityLocal list, Integer entityId, 
            Integer count) 
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
                    Constants.TABLE_LIST_ENTITY));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the contact table");
        }
        setId(newId);
        setLastUpdate(Calendar.getInstance().getTime());
        setEntityId(entityId);
        setTotalRecords(count);

        return newId;
    }

    public void ejbPostCreate(ListEntityLocal list, Integer entityId, 
            Integer count) {
        setList(list);
    }
 
    // CMP field accessors ----------------------------------------------------
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
     * @jboss:column-name name="total_records"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getTotalRecords();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setTotalRecords(Integer total);

   /**
    * @ejb:interface-method view-type="local"
    * @ejb:persistent-field
    * @jboss:column-name name="last_update"
     * @jboss.method-attributes read-only="true"
    */
    public abstract Date getLastUpdate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setLastUpdate(Date date);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="entity_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getEntityId();
    public abstract void setEntityId(Integer entity);
    
    // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="list-list_entity"
     *               role-name="list_entity-belongs_to-list"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="list_id"            
     */
    public abstract ListEntityLocal getList();
    public abstract void setList(ListEntityLocal list);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="list_entity-list_field_entity"
     *               role-name="list_entity-has-list_field_entity"
     */
    public abstract Collection getFields();
    public abstract void setFields (Collection fields);

    // EJB Callbacks

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
    public void ejbRemove() throws RemoveException, EJBException,
            RemoteException {

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() throws EJBException, RemoteException {

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext arg0) throws EJBException,
            RemoteException {

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {

    }

}
