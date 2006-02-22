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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.NotificationMessageTypeEntityLocal;
import com.sapienter.jbilling.interfaces.NotificationMessageTypeEntityLocalHome;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="NotificationMessageEntity" 
 *          display-name="Object representation of the table NOTIFICATION_MESSAGE"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/NotificationMessageEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/NotificationMessageEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="notification_message"
 *
 * @ejb:finder signature="NotificationMessageEntityLocal findIt(java.lang.Integer typeId, java.lang.Integer entityId, java.lang.Integer languageId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM notification_message a 
 *                     WHERE a.type.id = ?1
 *                       AND a.entityId = ?2
 *                       AND a.languageId = ?3"
 *             result-type-mapping="Local"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "notification_message"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class NotificationMessageEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer typeId, Integer entityId, 
            Integer languageId, Boolean useFlag) throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber(
                    Constants.TABLE_NOTIFICATION_MESSAGE));
        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the notification_message");
        }
        setId(newId);
        setLanguageId(languageId);
        setEntityId(entityId);
        setUseFlag(useFlag.booleanValue() ? new Integer(1) : new Integer(0));
        
        return newId;
    }
    
    public void ejbPostCreate(Integer typeId, Integer entityId, 
            Integer languageId, Boolean useFlag) {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            
            NotificationMessageTypeEntityLocalHome entityHome =
                    (NotificationMessageTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
                    NotificationMessageTypeEntityLocalHome.class,
                    NotificationMessageTypeEntityLocalHome.JNDI_NAME);
            NotificationMessageTypeEntityLocal type = entityHome.findByPrimaryKey(typeId);
            
            setType(type);
        } catch (Exception e) {
            Logger.getLogger(NotificationMessageEntityBean.class).error(
                    "Exception in postCreate", e);
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
     * @jboss:column-name name="entity_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getEntityId();
    public abstract void setEntityId(Integer entityId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="language_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getLanguageId();
    public abstract void setLanguageId(Integer languageId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="use_flag"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getUseFlag();
    public abstract void setUseFlag(Integer flag);

    //  CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="message-sections"
     *               role-name="message-has-sections"
     *               target-ejb="NotificationMessageSectionEntity"
     *               target-role-name="section-belong_to-message"
     *               target-cascade-delete="yes"
     * @jboss.target-relation related-pk-field="id"  
     *                        fk-column="message_id"            
     */
    public abstract Collection getSections();
    public abstract void setSections(Collection sections);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="messages-type"
     *               role-name="message-belongs_to_type"
     *               target-ejb="NotificationMessageTypeEntity"
     *               target-role-name="type-has-messages"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="type_id"            
     */
    public abstract NotificationMessageTypeEntityLocal getType();
    public abstract void setType(NotificationMessageTypeEntityLocal type);

    // EJB callbacks       -----------------------------------------------------
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
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
