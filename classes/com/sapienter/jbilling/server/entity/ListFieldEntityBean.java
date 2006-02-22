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

import com.sapienter.jbilling.interfaces.ListEntityLocal;

/**
 * @ejb:bean name="ListFieldEntity" 
 *          display-name="Object representation of the table LIST_FIELD" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ListFieldEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ListFieldEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="list_field"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * @ejb.value-object name="ListField"
 *
 * @jboss:table-name "list_field"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ListFieldEntityBean implements EntityBean {


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
     * @jboss:column-name name="column_name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getColumnName();
    public abstract void setColumnName(String name);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="ordenable"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOrdenable();
    public abstract void setOrdenable(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="searchable"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getSearchable();
    public abstract void setSearchable(Integer flag);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="data_type"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getDataType();
    public abstract void setDataType(String type);
    
    // CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="list-list_field"
     *               role-name="list_field-belongs_to-list"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="list_id"            
     */
    public abstract ListEntityLocal getList();
    public abstract void setList(ListEntityLocal list);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="list_field-list_field_entity"
     *               role-name="list_field-has-list_field_entities"
     */
    public abstract Collection getFieldEntities();
    public abstract void setFieldEntities(Collection entities);

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
