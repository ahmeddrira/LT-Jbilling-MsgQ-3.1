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
 * Created on 6-Apr-2003
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

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ContactEntityLocal;
import com.sapienter.jbilling.interfaces.ContactTypeEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.interfaces.TableEntityLocal;
import com.sapienter.jbilling.interfaces.TableEntityLocalHome;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="ContactMapEntity" 
 *          display-name="Object representation of the table CONTACT_MAP" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ContactMapEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ContactMapEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="contact_map"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "contact_map"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ContactMapEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer typeId, String tableName, Integer foreignId) 
            throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer( generator.getNextSequenceNumber(
                    Constants.TABLE_CONTACT_MAP));

            TableEntityLocalHome tableHome = (TableEntityLocalHome)
                    EJBFactory.lookUpLocalHome(
                    TableEntityLocalHome.class,
                    TableEntityLocalHome.JNDI_NAME);
            TableEntityLocal table = tableHome.findByTableName(tableName);
            setTableId(table.getId());
        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the contact_map table");
        }
        setId(newId);
        setTypeId(typeId);
        setForeignId(foreignId);

        return newId;
    }

    public void ejbPostCreate(Integer typeId, String tableName, 
            Integer foreignId) {
    }

    // CMP fields
    /**
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer cmid);
   
    /**
     * @ejb:persistent-field
     * @jboss:column-name name="type_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getTypeId();
    public abstract void setTypeId(Integer x);

    /**
     * @ejb:persistent-field
     * @jboss:column-name name="table_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getTableId();
    public abstract void setTableId(Integer x);

    /**
     * @ejb:persistent-field
     * @jboss:column-name name="foreign_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getForeignId();
    public abstract void setForeignId(Integer x);

    //  CMR fields 
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="contact-contact_map"
     *               role-name="map-has-contact"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="contact_id"            
     */
    public abstract ContactEntityLocal getContact();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setContact(ContactEntityLocal contact);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="contact_map-type"
     *               role-name="map-has-type"
     *               target-ejb="ContactTypeEntity"
     *               target-role-name="type-for-contact_map"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="type_id"           
     */
    public abstract ContactTypeEntityLocal getType();
    /**
     * @ejb:interface-method view-type="local" 
     */
    public abstract void setType(ContactTypeEntityLocal type);
    
    // EJB Callbacks

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
