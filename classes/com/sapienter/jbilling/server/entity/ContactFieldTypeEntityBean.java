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
 * Created on Sep 16, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.interfaces.EntityEntityLocal;

/**
 * @ejb:bean name="ContactFieldTypeEntity" 
 *          display-name="Object representation of the table CONTACT_FIELD_TYPE" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ContactFieldTypeEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ContactFieldTypeEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="contact_field_type"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb.value-object name="ContactFieldType"
 * 
 * @jboss:table-name "contact_field_type"
 * @jboss.read-only read-only="true"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ContactFieldTypeEntityBean implements EntityBean {

    //  CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer ruleId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="prompt_key"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getPromptKey();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setPromptKey(String key);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="data_type"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getDataType();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setDataType(String type);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="customer_readonly"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getReadOnly();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setReadOnly(Integer flag);

    // CMR ---------------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-contact_field_types"
     *               role-name="type-belongs_to-entity"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="entity_id"            
     */
    public abstract EntityEntityLocal getEntity();
    public abstract void setEntity(EntityEntityLocal entity);

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove() throws RemoveException, EJBException,
            RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext arg0) throws EJBException,
            RemoteException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
        // TODO Auto-generated method stub

    }

}
