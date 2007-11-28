/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
