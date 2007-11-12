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
 * Created on 9-Apr-2003
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

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.DescriptionEntityPK;

/**
 * @ejb:bean name="DescriptionEntity" 
 *          display-name="Object representation of the table INTERNATIONAL_DESCRIPTION" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/DescriptionEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/DescriptionEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          schema="description"
 *
 * @ejb:finder signature="DescriptionEntityLocal findIt(java.lang.String tableName, 
 *                                                      java.lang.Integer rowId,
 *                                                      java.lang.String psudoColumn,
 *                                                      java.lang.Integer language)"
 *             query="SELECT OBJECT(a) 
 *                      FROM description a, jbilling_table b
 *                     WHERE a.tableId = b.id
 *                       AND b.name = ?1
 *                       AND a.languageId = ?4
 *                       AND a.foreignId = ?2
 *                       AND a.psudoColumn = ?3 " 
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="Collection findByTable_Row(java.lang.String tableName, 
 *                                                   java.lang.Integer rowId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM description a, jbilling_table b
 *                     WHERE a.tableId = b.id
 *                       AND b.name = ?1
 *                       AND a.foreignId = ?2 "
 *             result-type-mapping="Local"
 * 
 * @jboss:table-name "international_description"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class DescriptionEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public DescriptionEntityPK ejbCreate(String tableName, Integer foreignId, 
            String pColumn, Integer languageId, String content) 
            throws CreateException {
        setTableId(Util.getTableId(tableName));
        setForeignId(foreignId);
        setPsudoColumn(pColumn);
        setLanguageId(languageId);
        setContent(content);

        return new DescriptionEntityPK(Util.getTableId(tableName), foreignId, pColumn, 
                languageId);
    }
    
    public void ejbPostCreate(String tableName, Integer foreignId, 
            String pColumn, Integer languageId, String content) {
    }

    
    // CMP fields
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="table_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getTableId();
    public abstract void setTableId(Integer tableId);
    
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="foreign_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getForeignId();
    public abstract void setForeignId(Integer forId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="psudo_column"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getPsudoColumn();
    public abstract void setPsudoColumn(String col);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="language_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getLanguageId();
    public abstract void setLanguageId(Integer lanId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="content"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getContent();
    /**
     * @ejb:interface-method view-type="local"
	 */
    public abstract void setContent(String content);
    
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
