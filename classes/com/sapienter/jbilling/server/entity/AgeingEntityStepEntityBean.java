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
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.process.db.AgeingEntityStepDAS;
import com.sapienter.jbilling.server.user.db.CompanyDAS;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.db.InternationalDescriptionDAS;
import com.sapienter.jbilling.server.util.db.InternationalDescriptionDTO;

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
    private static final Logger LOG = Logger.getLogger(AgeingEntityStepEntityBean.class);
    
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
            LOG.error("Exception creating aging entity step row", e);
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
            setEntity(new CompanyDAS().find(entityId));
 
            LOG.debug("setting crm with statusId = "  + statusId);
            setStatusId(statusId);
        } catch (Exception e) {
            LOG.error("Exception in post create ",e);
        }
        
    }

    private InternationalDescriptionDTO getDescriptionObject(Integer language, String column) {
    	InternationalDescriptionDTO inter = new InternationalDescriptionDAS().findIt(Constants.TABLE_AGEING_ENTITY_STEP,
                getId(), column, language);
        
        if (inter == null) {
            LOG.warn("Dont found the description");
            return null;
        }
        
        return inter;
    }

    private void createDescription(Integer language, String column, 
            String message) {
    	new InternationalDescriptionDAS().create(Constants.TABLE_AGEING_ENTITY_STEP,
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
    public String getWelcomeMessage(Integer languageId) {
            return getDescriptionObject(languageId, "welcome_message").getContent();
    }
    
    /**
      * @ejb:interface-method view-type="local"
      */
    public void setWelcomeMessage(Integer languageId, String message) 
            throws NamingException, CreateException {
        if (message == null) {
            message = "";
        }
        InternationalDescriptionDTO row = getDescriptionObject(languageId, 
                "welcome_message");
        if (row == null) {
        	createDescription(languageId, "welcome_message", message);
        } else {
        	row.setContent(message);
        }
    }
    
    /**
      * @ejb:interface-method view-type="local"
      */
    public String getFailedLoginMessage(Integer languageId) {
            return getDescriptionObject(languageId, "failed_login_message").getContent();
    }
    
    /**
      * @ejb:interface-method view-type="local"
      */
    public void setFailedLoginMessage(Integer languageId, String message) 
            throws NamingException, CreateException {
        if (message == null) {
            message = "";
        }
        InternationalDescriptionDTO row = getDescriptionObject(languageId, 
                "failed_login_message");
        if (row == null) {
        	createDescription(languageId, "failed_login_message", message);	
        } else {
            row.setContent(message);            	
        }
    }
    
    // CMR fields --------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="status_id"
     * @jboss.method-attributes read-only="true"
     */
   public abstract Integer getStatusId();
    public abstract void setStatusId(Integer status);

    /**
     * @ejb:interface-method view-type="local"
     */
    public CompanyDTO getEntity() {
        return new AgeingEntityStepDAS().find(getId()).getCompany();
    }
    /**
     * @ejb:interface-method view-type="local"
     */
    public void setEntity(CompanyDTO entity) {
        new AgeingEntityStepDAS().find(getId()).setCompany(entity);
    }

    
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
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
