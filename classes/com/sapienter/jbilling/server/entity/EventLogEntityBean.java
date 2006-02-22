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
import java.util.Calendar;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.TableEntityLocal;
import com.sapienter.jbilling.interfaces.TableEntityLocalHome;
import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="EventLogEntity" 
 *          display-name="Object representation of the table EVENT_LOG"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/EventLogEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/EventLogEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="event_log"
 * 
 * @ejb.transaction type="RequiresNew"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "event_log"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class EventLogEntityBean implements EntityBean {

    Logger log = null;
    /**
      * @ejb:create-method view-type="local"
      * 
      */
    public Integer ejbCreate(Integer entity, Integer foreignId, 
           Integer level, Integer module, Integer message, String table) 
           throws CreateException {
               
        Integer newId;
        log.debug("creating event log table = " + table);
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber(
                    Constants.TABLE_EVENT_LOG));

        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the event log table");
        }

        setId(newId);
        setEntityId(entity);
        setForeignId(foreignId);
        setLevel(level);
        setModuleId(module);
        setMessageId(message);
        setCreateDateTime(Calendar.getInstance().getTime());

        return newId;                 
    }

    public void ejbPostCreate(Integer entity, Integer foreignId, 
            Integer level, Integer module, Integer message, String table) {
        log.debug("post creating event log table = " + table);

        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            TableEntityLocalHome tableHome =
                    (TableEntityLocalHome) EJBFactory.lookUpLocalHome(
                    TableEntityLocalHome.class,
                    TableEntityLocalHome.JNDI_NAME);

            TableEntityLocal tableRow = tableHome.findByTableName(table);
            setTable(tableRow);
            log.debug("post creating 2 event log table = " + table);
            
        } catch (Exception e) {
            log.error("Cant update table relationship:" + table, e);
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
     * This could be a CMR, but would make the creation much more complicated
     * since for sure the entity id will be available, while the entity might 
     * not
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
     * @jboss:column-name name="foreign_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getForeignId();
    public abstract void setForeignId(Integer foreignId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getCreateDateTime();
    public abstract void setCreateDateTime(Date create);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="level"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getLevel();
    public abstract void setLevel(Integer levelId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="module_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getModuleId();
    public abstract void setModuleId(Integer moduleId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="message_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getMessageId();
    public abstract void setMessageId(Integer messageId);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="old_num"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOldNum();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOldNum(Integer oldNum);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="old_str"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getOldStr();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOldStr(String oldStr);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="old_date"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Date getOldDate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOldDate(Date oldDate);

    // CMR fields ----------------------------------------------------
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="user-events"
     *               role-name="events-belong_to-user"
     * @jboss.relation related-pk-field="userId"  
     *                 fk-column="user_id"            
     */
    public abstract UserEntityLocal getUser();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setUser(UserEntityLocal user);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb.relation name="event-table"
      *               role-name="event-belongs_to-table"
      *               target-ejb="TableEntity"
      *               target-role-name="table-has-events"
      *               target-multiple="yes"
      * @jboss.relation related-pk-field="id"  
      *                 fk-column="table_id"            
      */
     public abstract TableEntityLocal getTable();
     public abstract void setTable(TableEntityLocal table);
    
    // EJB callbacks ----------------------------------------------------
    
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
        log = Logger.getLogger(EventLogEntityBean.class);

    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
