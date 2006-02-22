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
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="NotificationMessageSectionEntity" 
 *          display-name="Object representation of the table NOTIFICATION_MESSAGE_SECTION"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/NotificationMessageSectionEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/NotificationMessageSectionEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="notification_message_section"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "notification_message_section"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class NotificationMessageSectionEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer section) throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber(
                    Constants.TABLE_NOTIFICATION_MESSAGE_SECTION));
        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the notification_message_section");
        }
        setId(newId);
        setSection(section);
        
        return newId;
    }
    
    public void ejbPostCreate(Integer section) {}
 
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
     * @jboss:column-name name="section"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getSection();
    public abstract void setSection(Integer section);

    //  CMR field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="section-lines"
     *               role-name="section-has-lines"
     *               target-ejb="NotificationMessageLineEntity"
     *               target-role-name="lines-belong_to-section"
     *               target-cascade-delete="yes"
     * @jboss.target-relation related-pk-field="id"  
     *                        fk-column="message_section_id"            
     */
    public abstract Collection getLines();
    public abstract void setLines(Collection lines);


    // EJB callbacks      -----------------------------------------------------
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
