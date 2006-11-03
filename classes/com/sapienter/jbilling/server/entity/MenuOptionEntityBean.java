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
 * Created on Jul 5, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocal;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.interfaces.MenuOptionEntityLocal;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="MenuOptionEntity" 
 *          display-name="Object representation of the table MENU_OPTION"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/MenuOptionEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/MenuOptionEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="menu_option"
 *
 *
 * @ejb.value-object name="MenuOption"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "menu_option"
 * @jboss.read-only read-only="true"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class MenuOptionEntityBean implements EntityBean {
    private Logger log = null;

    private DescriptionEntityLocal getDescriptionObject(Integer language) {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            DescriptionEntityLocalHome descriptionHome =
                    (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);

            return descriptionHome.findIt(Constants.TABLE_MENU_OPTION,
                    getId(),"display", language);
        } catch (FinderException e) {
            log.warn("Description for menu " + getId() + 
                    " not found for language " + language);
            return null;
        } catch (Exception e1) {
            log.warn("Exception while looking for an menu option description", 
                    e1);
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

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="link"
     * @jboss.method-attributes read-only="true"
      */
     public abstract String getLink();
     public abstract void setLink(String link);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="level_field"
     * @jboss.method-attributes read-only="true"
      */
     public abstract Integer getLevel();
     public abstract void setLevel(Integer level);

    // CMR field accessors -----------------------------------------------------
    /**
     * This is a circular relationship
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="menu_option-menu-option"
     *               role-name="option-has-parent"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="parent_id"            
     */
    public abstract MenuOptionEntityLocal getParent();
    public abstract void setParent(MenuOptionEntityLocal parent);

    /**
     * This is a circular relationship
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="menu_option-menu-option"
     *               role-name="parent-has-options"
     */
    public abstract Collection getChildren();
    public abstract void setChildren(Collection options);

    // Custom field accessors --------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     */
    public String getDisplay(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language);
        
        if (desc == null) {
            // default to english
            desc = getDescriptionObject(new Integer(1));
        }
        if (desc == null) {
            return "Display not set for this permission";
        } 

        return desc.getContent();
    }

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
        log = Logger.getLogger(MenuOptionEntityBean.class);

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
