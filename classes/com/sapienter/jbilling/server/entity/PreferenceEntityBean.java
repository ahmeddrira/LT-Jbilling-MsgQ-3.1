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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.PreferenceTypeEntityLocal;
import com.sapienter.jbilling.interfaces.PreferenceTypeEntityLocalHome;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="PreferenceEntity" 
 *          display-name="Object representation of the table PREFERENCE" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PreferenceEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PreferenceEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="preference"
 *
 * @ejb.value-object name="Preference"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @ejb:finder signature="PreferenceEntityLocal findByType_Row(java.lang.Integer typeId,java.lang.Integer foreignId,java.lang.String tableId)"
 *             query="SELECT OBJECT(a) 
 *                      FROM preference a, jbilling_table b 
 *                     WHERE a.type.id = ?1
 *                       AND a.foreignId = ?2
 *                       AND a.tableId = b.id
 *                       AND b.name = ?3 "
 *             result-type-mapping="Local"
 *
 * @jboss:table-name "preference"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PreferenceEntityBean implements EntityBean {

    private JNDILookup EJBFactory = null;
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer typeId, String table,Integer foreignId,
    		Integer intValue, String strValue, Float fValue) 
    		throws CreateException {

        Integer newId, tableId;

        try {
            EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber(
                    Constants.TABLE_PREFERENCE));

            tableId = Util.getTableId(table);
        } catch (Exception e) {
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the preference table");
        }

        setId(newId);
        setForeignId(foreignId);
        setTableId(tableId);
        setIntValue(intValue);
        setStrValue(strValue);
        setFloatValue(fValue);
        
        return newId;
    }
    
    public void ejbPostCreate(Integer typeId, String table,Integer foreignId,
    		Integer intValue, String strValue, Float fValue) {
        try {
            PreferenceTypeEntityLocalHome typeHome = 
                (PreferenceTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
                PreferenceTypeEntityLocalHome.class, 
                PreferenceTypeEntityLocalHome.JNDI_NAME);
            setType(typeHome.findByPrimaryKey(typeId));
        } catch (Exception e) {};
    }

    // CMP field accessors ----------------------------------------------------
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
      * @jboss:column-name name="table_id"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getTableId();
    public abstract void setTableId(Integer tableId);

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
      * @jboss:column-name name="int_value"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getIntValue();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setIntValue(Integer intValue);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="str_value"
     * @jboss.method-attributes read-only="true"
      */
    public abstract String getStrValue();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setStrValue(String strValue);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="float_value"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Float getFloatValue();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setFloatValue(Float floatValue);

    // CMR methods ------------------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="preference_type"
     *               role-name="preference_has_type"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="type_id"            
     */
    public abstract PreferenceTypeEntityLocal getType();
    public abstract void setType(PreferenceTypeEntityLocal type);

    // EJB callback -----------------------------------------------------------
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
