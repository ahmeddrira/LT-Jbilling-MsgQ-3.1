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
 * Created on Sep 9, 2004
 *
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.interfaces.EntityEntityLocal;

/**
 * @ejb:bean name="ContactTypeEntity" 
 *          display-name="Object representation of the table Contact Type" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ContactTypeEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ContactTypeEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="contact_type"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @ejb:finder signature="ContactTypeEntityLocal findPrimary(java.lang.Integer entityId) "
 *             query="SELECT OBJECT(a)
 *                      FROM contact_type a
 *                     WHERE a.isPrimary = 1
 *                       AND a.entity.id = ?1"
 *
 * @jboss:table-name "contact_type"
 * @jboss.read-only read-only="true"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ContactTypeEntityBean implements EntityBean {

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
     * @ejb:persistent-field
     * @jboss:column-name name="is_primary"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIsPrimary();
    public abstract void setIsPrimary(Integer flag);
    

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-contact_types"
     *               role-name="type-belongs_to-entity"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="entity_id"           
     */
    public abstract EntityEntityLocal getEntity();
    public abstract void setEntity(EntityEntityLocal entity);

   
    /*
     * The description (international) could be included here, but
     * since this bean is not been actively called from the code
     * (only from the framework for finders), it is not included.
     */
    // EJB Callbacks
    
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
