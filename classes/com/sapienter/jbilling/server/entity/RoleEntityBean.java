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

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocal;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="RoleEntity" 
 *          display-name="Object representation of the table ROLE"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/RoleEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/RoleEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="role"
 *
 * @ejb.value-object name="Role"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "role"
 * @jboss.read-only read-only="true"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class RoleEntityBean implements EntityBean {
    private Logger log = null;

    private DescriptionEntityLocal getDescriptionObject(Integer language) {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            DescriptionEntityLocalHome descriptionHome =
                    (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);

            return descriptionHome.findIt(Constants.TABLE_ROLE,
                    getId(),"description", language);
        } catch (Exception e) {
            log.warn("Exception while looking for a role description", 
                    e);
            return null;
        }
    }

    private DescriptionEntityLocal getNameObject(Integer language) {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            DescriptionEntityLocalHome descriptionHome =
                    (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);

            return descriptionHome.findIt(Constants.TABLE_ROLE,
                    getId(),"title", language);
        } catch (Exception e) {
            log.warn("Exception while looking for a role title", e);
            return null;
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

    //  CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="permission-role"
     *               role-name="role-has-permissions"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="permission_id"            
     * @jboss.relation-table table-name="permission_role_map"
     *                       create-table="false"
     */
    public abstract Collection getPermissions();
    public abstract void setPermissions(Collection permissions);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="role-user"
     *               role-name="role-for-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     * @jboss.relation-table table-name="user_role_map"
     *                       create-table="false"
     */
    public abstract Collection getUsers();
    public abstract void setUsers(Collection users);

    // Custom field accessors --------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     */
    public String getDescription(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language);
        if (desc == null) {
            return "Description not set for this role";
        } else {
            return desc.getContent();
        }
    }

    /**
     * @ejb:interface-method view-type="local"
     */
    public String getTitle(Integer language) {
        DescriptionEntityLocal desc = getNameObject(language);
        if (desc == null) {
            return "Title not set for this role";
        } else {
            return desc.getContent();
        }
    }

    // EJB callbacks -----------------------------------------------------------

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
        log = Logger.getLogger(RoleEntityBean.class);
        
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {

    }

}
