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
 * Created on Sep 20, 2004
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

import com.sapienter.jbilling.interfaces.PermissionEntityLocal;
import com.sapienter.jbilling.interfaces.UserEntityLocal;

/**
 * @ejb:bean name="PermissionUserEntity" 
 *          display-name="Object representation of the table PERMISSION_USER"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PermissionUserEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PermissionUserEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="permission_user"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "permission_user"
 * @jboss.read-only read-only="true"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PermissionUserEntityBean implements EntityBean {

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
      * @jboss:column-name name="is_grant"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Integer getIsGrant();
     public abstract void setIsGrant(Integer flag);
     
     // CMR ----------------------------------------------------
     /**
      * @ejb:interface-method view-type="local"
      * @ejb.relation name="permission-user"
      *               role-name="permission-for-user"
      * @jboss.relation related-pk-field="userId"  
      *                 fk-column="user_id"            
      */
     public abstract UserEntityLocal getUser();
     public abstract void setUser(UserEntityLocal user);
     
     /**
      * @ejb:interface-method view-type="local"
      * @ejb.relation name="permission_user-permission"
      *               role-name="permission-definition"
      * @jboss.relation related-pk-field="id"  
      *                 fk-column="permission_id"            
      */
     public abstract PermissionEntityLocal getPermission();
     public abstract void setPermission(PermissionEntityLocal user);

    // EJB Callbacks -------------------------------------------------
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
