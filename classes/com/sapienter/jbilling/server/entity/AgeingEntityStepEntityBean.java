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
 * Created on Mar 24, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocal;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.EntityEntityLocalHome;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.UserStatusEntityLocal;
import com.sapienter.jbilling.interfaces.UserStatusEntityLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="AgeingEntityStepEntity" 
 *          display-name="Object representation of the table AGEING_ENTITY_STEP"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/AgeingEntityStepEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/AgeingEntityStepEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="ageing_entity_step"
 *
 * @ejb:finder signature="AgeingEntityStepEntityLocal findStep(java.lang.Integer entityId, java.lang.Integer stepId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM ageing_entity_step a 
 *                     WHERE a.entity.id = ?1
 *                       AND a.status.id = ?2" 
 *             result-type-mapping="Local"
 *
 * @ejb.value-object name="AgeingEntityStep"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "ageing_entity_step"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class AgeingEntityStepEntityBean implements EntityBean {
    private Logger log = null;
    
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, Integer statusId, 
            String welcomeMessage, String loginFailMessage, Integer languageId,
            Integer days) 
            throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false); 
            
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber(
                    Constants.TABLE_AGEING_ENTITY_STEP));

        } catch (Exception e) {
            log.error("Exception creating aging entity step row", e);
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the ageing_entity_step table");
        }
        setId(newId);
        setDays(days);
        try {
            setWelcomeMessage(languageId, welcomeMessage);
            setFailedLoginMessage(languageId, loginFailMessage);
        } catch (Exception e) {
            throw new CreateException("Exception setting the int fields " + e);
        }

        return newId;
    }

    public void ejbPostCreate(Integer entityId, Integer statusId, 
            String welcomeMessage, String loginFailMessage, Integer languageId,
            Integer days) {
        try {
            JNDILookup EJBFactory = null;
            EJBFactory = JNDILookup.getFactory(false);
            EntityEntityLocalHome entityHome =
                    (EntityEntityLocalHome) EJBFactory.lookUpLocalHome(
                    EntityEntityLocalHome.class,
                    EntityEntityLocalHome.JNDI_NAME);
            setEntity(entityHome.findByPrimaryKey(entityId));
 
            UserStatusEntityLocalHome userStatusHome =
                    (UserStatusEntityLocalHome) EJBFactory.lookUpLocalHome(
                    UserStatusEntityLocalHome.class,
                    UserStatusEntityLocalHome.JNDI_NAME);
            log.debug("setting crm with statusId = "  + statusId);
            setStatus(userStatusHome.findByPrimaryKey(statusId));
        } catch (Exception e) {
            log.error("Exception in post create ",e);
        }
        
    }

    private DescriptionEntityLocal getDescriptionObject(Integer language, String column) 
            throws FinderException, NamingException {
        JNDILookup EJBFactory = null;
        EJBFactory = JNDILookup.getFactory(false);
        DescriptionEntityLocalHome descriptionHome =
            (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                DescriptionEntityLocalHome.class,
                DescriptionEntityLocalHome.JNDI_NAME);

        return descriptionHome.findIt(Constants.TABLE_AGEING_ENTITY_STEP,
                getId(), column, language);
    }

    private void createDescription(Integer language, String column, 
            String message) throws CreateException, NamingException {
        JNDILookup EJBFactory = null;
        EJBFactory = JNDILookup.getFactory(false);
        DescriptionEntityLocalHome descriptionHome =
                (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                DescriptionEntityLocalHome.class,
                DescriptionEntityLocalHome.JNDI_NAME);

        descriptionHome.create(Constants.TABLE_AGEING_ENTITY_STEP,
                getId(), column, language, message);
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
      * @jboss:column-name name="days"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getDays();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setDays(Integer days);
    
    // Custom feilds -----------------------------------------------
    /**
      * @ejb:interface-method view-type="local"
      */
    public String getWelcomeMessage(Integer languageId) 
            throws NamingException {
        try {
            return getDescriptionObject(languageId, "welcome_message").getContent();
        } catch (FinderException e) {
            return null;
        } 
    }
    
    /**
      * @ejb:interface-method view-type="local"
      */
    public void setWelcomeMessage(Integer languageId, String message) 
            throws NamingException, CreateException {
        try {
            if (message == null) {
                message = "";
            }
            DescriptionEntityLocal row = getDescriptionObject(languageId, 
                    "welcome_message");
            row.setContent(message);
        } catch (FinderException e) {
            createDescription(languageId, "welcome_message", message);
        } 
    }
    
    /**
      * @ejb:interface-method view-type="local"
      */
    public String getFailedLoginMessage(Integer languageId) 
            throws NamingException {
        try {
            return getDescriptionObject(languageId, "failed_login_message").getContent();
        } catch (FinderException e) {
            return null;
        } 
    }
    
    /**
      * @ejb:interface-method view-type="local"
      */
    public void setFailedLoginMessage(Integer languageId, String message) 
            throws NamingException, CreateException {
        try {
            if (message == null) {
                message = "";
            }
            DescriptionEntityLocal row = getDescriptionObject(languageId, 
                    "failed_login_message");
            row.setContent(message);
        } catch (FinderException e) {
            createDescription(languageId, "failed_login_message", message);
        } 
    }
    
    // CMR fields --------------------------------------------------
    /**
     * many-to-one unidirectional
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="ageing_steps-status"
     *               role-name="steps-has-status"
     *               target-ejb="UserStatusEntity"
     *               target-role-name="status-provieds_for-step"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="status_id"            
     */
    public abstract UserStatusEntityLocal getStatus();
    public abstract void setStatus(UserStatusEntityLocal status);

    /**
     * one-to-many bidirectional
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="entity-ageing_steps"
     *               role-name="step-belongs_to-entity"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="entity_id"            
     */
    public abstract EntityEntityLocal getEntity();
    public abstract void setEntity(EntityEntityLocal entity);
    
    // EJB Callbacks ------------------------------------------------
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
    public void setEntityContext(EntityContext context) {
        log = Logger.getLogger(AgeingEntityStepEntityBean.class);
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
