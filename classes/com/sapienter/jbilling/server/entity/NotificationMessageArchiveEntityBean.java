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

package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.NotificationMessageArchiveLineEntityLocal;
import com.sapienter.jbilling.interfaces.NotificationMessageArchiveLineEntityLocalHome;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.notification.MessageSection;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="NotificationMessageArchiveEntity" 
 *          display-name="Object representation of the table NOTIFICATION_MESSAGE_ARCH"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/NotificationMessageArchiveEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/NotificationMessageArchiveEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="notification_message_arch"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "notification_message_arch"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class NotificationMessageArchiveEntityBean implements EntityBean {
    Logger log = null;
    private static int LINE_LENGTH = 500;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer typeId, MessageSection sections[])
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
                    Constants.TABLE_NOTIFICATION_MESSAGE_ARCHIVE));
        } catch (Exception e) {
            Logger.getLogger(NotificationMessageArchiveEntityBean.class).
                    debug("Exception ", e);
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the notification message archive table");
        }
        setId(newId);
        setTypeId(typeId);
        setCreateDatetime(Calendar.getInstance().getTime());
        return newId;
    }
    
    public void ejbPostCreate(Integer typeId, MessageSection sections[]) {
        Collection newLines = getLines();
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            NotificationMessageArchiveLineEntityLocalHome lineHome =
                    (NotificationMessageArchiveLineEntityLocalHome) EJBFactory.lookUpLocalHome(
                    NotificationMessageArchiveLineEntityLocalHome.class,
                    NotificationMessageArchiveLineEntityLocalHome.JNDI_NAME);

            for (int f = 0; f < sections.length; f++) {
                
                String content = sections[f].getContent();
                for (int index = 0; index < content.length(); 
                        index += LINE_LENGTH) {
                    int end = (content.length() < index + 
                            LINE_LENGTH)
                            ? content.length() 
                            : index + LINE_LENGTH;    
                    NotificationMessageArchiveLineEntityLocal line = 
                            lineHome.create(content.substring(index, end),
                                sections[f].getSection());
                    newLines.add(line);
                }
            } 
        } catch (Exception e) {
            log.error("Exception creating the message archive lines", e);
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
     * @jboss:column-name name="type_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getTypeId();
    public abstract void setTypeId(Integer typeId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDatetime();
    public abstract void setCreateDatetime(Date create);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="result_message"
     * @jboss.method-attributes read-only="true"
      */
     public abstract String getResultMessage();
    /**
      * @ejb:interface-method view-type="local"
      */
     public abstract void setResultMessage(String message);

    //  CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="message-lines"
     *               role-name="message-has-lines"
     *               target-ejb="NotificationMessageArchiveLineEntity"
     *               target-role-name="line-belong_to-message"
     * @jboss.target-relation related-pk-field="id"  
     *                        fk-column="message_archive_id"            
     */
    public abstract Collection getLines();
    public abstract void setLines(Collection sections);

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
        log = Logger.getLogger(NotificationMessageArchiveEntityBean.class);
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
