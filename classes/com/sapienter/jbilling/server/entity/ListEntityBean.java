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
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

/**
 * @ejb:bean name="ListEntity" 
 *          display-name="Object representation of the table LIST" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ListEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ListEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="list"
 * 
 * @ejb:finder signature="Collection findAll()"
 *             query="SELECT OBJECT(a) 
 *                      FROM list a" 
 *             result-type-mapping="Local"
 * 
 * @ejb:finder signature="ListEntityLocal findByName(java.lang.String legacyName)"
 *             query="SELECT OBJECT(a) 
 *                      FROM list a 
 *                     WHERE a.legacyName = ?1" 
 *             result-type-mapping="Local"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 *
 * @jboss:table-name "list"
 * @jboss.read-only read-only="true"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ListEntityBean implements EntityBean {
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
     * @jboss:column-name name="title_key"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getTitleKey();
    public abstract void setTitleKey(String key);

   /**
    * @ejb:interface-method view-type="local"
    * @ejb:persistent-field
    * @jboss:column-name name="instr_key"
     * @jboss.method-attributes read-only="true"
    */
    public abstract String getInstrKey();
    public abstract void setInstrKey(String key);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="legacy_name"
     * @jboss.method-attributes read-only="true"
     */
     public abstract String getLegacyName();
     public abstract void setLegacyName(String name);

    // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="list-list_entity"
     *               role-name="list-has-list_entities"
     */
    public abstract Collection getListEntities();
    public abstract void setListEntities(Collection listEntities);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="list-list_field"
     *               role-name="list-has-list_fields"
     */
    public abstract Collection getListFields();
    public abstract void setListFields(Collection listFields);

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
