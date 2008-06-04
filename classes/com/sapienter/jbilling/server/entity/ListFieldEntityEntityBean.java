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
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ListEntityEntityLocal;
import com.sapienter.jbilling.interfaces.ListFieldEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="ListFieldEntityEntity" 
 *          display-name="Object representation of the table LIST" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ListFieldEntityEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ListFieldEntityEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="list_field_entity"
 *
 * @ejb:finder signature="ListFieldEntityEntityLocal findByFieldEntity(java.lang.Integer fieldId, java.lang.Integer listEntityId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM list_field_entity a 
 *                     WHERE a.field.id = ?1
 *                       AND a.entity.id = ?2" 
 *             result-type-mapping="Local"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "list_field_entity"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ListFieldEntityEntityBean implements EntityBean {
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(ListEntityEntityLocal entity,  
            ListFieldEntityLocal field) 
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
                    Constants.TABLE_LIST_FIELD_ENTITY));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the list_field_entity table");
        }
        setId(newId);

        return newId;
    }

    public void ejbPostCreate(ListEntityEntityLocal entity,  
            ListFieldEntityLocal field) {
        setEntity(entity);
        setField(field);
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
     * @jboss:column-name name="min_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getMin();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMin(Integer min);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="max_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getMax();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMax(Integer max);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="min_str_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getMinStr();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMinStr(String min);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="max_str_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getMaxStr();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMaxStr(String max);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="min_date_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getMinDate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMinDate(Date min);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="max_date_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getMaxDate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setMaxDate(Date max);
    
    
    // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="list_field-list_field_entity"
     *               role-name="list_field_entity-belongs_to-list_field"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="list_field_id"            
     */
    public abstract ListFieldEntityLocal getField();
    public abstract void setField(ListFieldEntityLocal field);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="list_entity-list_field_entity"
     *               role-name="list_field_entity-belongs_to-list_entity"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="list_entity_id"            
     */
    public abstract ListEntityEntityLocal getEntity();
    public abstract void setEntity(ListEntityEntityLocal field);

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
