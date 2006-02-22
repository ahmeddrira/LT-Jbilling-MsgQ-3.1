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
 * Created on 14-Apr-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocal;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.interfaces.PluggableTaskTypeCategoryEntityLocal;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="PluggableTaskTypeEntity" 
 *          display-name="Object representation of the table PLUGGABLE_TASK_TYPE" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PluggableTaskTypeEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PluggableTaskTypeEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="pluggable_task_type"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @jboss:table-name "pluggable_task_type"
 * @jboss.read-only read-only="true"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PluggableTaskTypeEntityBean implements EntityBean {
    private Logger log = null;
    
    private DescriptionEntityLocal getDescriptionObject(
        Integer language,
        String column) {
        try {

            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            DescriptionEntityLocalHome descriptionHome =
                (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);

            return descriptionHome.findIt(
                Constants.TABLE_PLUGGABLE_TASK_TYPE,
                getId(),
                column,
                language);
        } catch (Exception e) {
            log.warn("Exception while looking for pluggable_task_type inter. field", e);
            return null;
        }

    }

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
     * @jboss:column-name name="class_name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getClassName();
    public abstract void setClassName(String className);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="min_parameters"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getMinParameters();
    public abstract void setMinParameters(Integer minParameters);

    //  CMR field accessors -----------------------------------------------------
    /**
     * 
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="pluggable_type-category"
     *               role-name="typ-belongs_to-category"
     *               target-ejb="PluggableTaskTypeCategoryEntity"
     *               target-role-name="category-provides"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="category_id"            
     */
    public abstract PluggableTaskTypeCategoryEntityLocal getCategory();
    public abstract void setCategory(PluggableTaskTypeCategoryEntityLocal category);

    // Custom field accessors --------------------------------------------------

    /**
     * @ejb:interface-method view-type="local"
     */
    public String getTitle(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language, "title");
        if (desc == null) {
            return "Title not set for this rule";
        } else {
            return desc.getContent();
        }
    }
    public void setTitle(String title, Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language, "title");
        if (desc == null) {
            log.error("Can't update a non existing record");
        } else {
            desc.setContent(title);
        }
    }


    /**
     * @ejb:interface-method view-type="local"
     */
    public String getDescription(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language, "description");
        if (desc == null) {
            return "Description not set for this rule";
        } else {
            return desc.getContent();
        }
    }
    public void setDescription(String description, Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language, "description");
        if (desc == null) {
            log.error("Can't update a non existing record");
        } else {
            desc.setContent(description);
        }
    }
    


    // EJB Callbacks -------------------------------------------------------------

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
        log = Logger.getLogger(PluggableTaskTypeEntityBean.class);            
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
