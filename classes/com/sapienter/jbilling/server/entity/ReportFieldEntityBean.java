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
 * @ejb:bean name="ReportFieldEntity" 
 *          display-name="Object representation of the table REPORT_FIELD" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ReportFieldEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ReportFieldEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="report_field"
 *
 * @ejb.value-object name="ReportField"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "report_field"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ReportFieldEntityBean implements EntityBean {
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer position, String table, String column,
            Integer isGrouped, Integer isShown, String type,
            Integer funFlag, Integer selFlag, Integer ordFlag, Integer opeFlag,
            Integer werFlag) throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                    (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer( generator.getNextSequenceNumber(
                    Constants.TABLE_REPORT_FIELD));
        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the report user table");
        }
        setId(newId);
        setPosition(position);
        setTable(table);
        setColumn(column);
        setIsGrouped(isGrouped);
        setIsShown(isShown);
        setDataType(type);
        setFunctionable(funFlag);
        setSelectable(selFlag);
        setOrdenable(ordFlag);
        setOperatorable(opeFlag);
        setWherable(werFlag);
        
        return newId;

    }
    
    public void ejbPostCreate(Integer position, String table, String column,
            Integer isGrouped, Integer isShown, String type,
            Integer funFlag, Integer selFlag, Integer ordFlag, Integer opeFlag,
            Integer werFlag) {
        
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
     * @jboss:column-name name="table_name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getTable();
    public abstract void setTable(String table);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="column_name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getColumn();
    public abstract void setColumn(String column);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="position_number"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getPosition();
    public abstract void setPosition(Integer order);


    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="order_position"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOrderPosition();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOrderPosition(Integer order);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="where_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getWhereValue();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setWhereValue(String where);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="title_key"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getTitleKey();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setTitleKey(String title);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="function_name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getFunction();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setFunction(String function);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="is_grouped"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIsGrouped();
    public abstract void setIsGrouped(Integer isGrouped);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="is_shown"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getIsShown();
    public abstract void setIsShown(Integer isShown);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="data_type"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getDataType();
    public abstract void setDataType(String type);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="operator_value"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getOperator();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setOperator(String operator);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="functionable"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getFunctionable();
    public abstract void setFunctionable(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="selectable"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getSelectable();
    public abstract void setSelectable(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="ordenable"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOrdenable();
    public abstract void setOrdenable(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="operatorable"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getOperatorable();
    public abstract void setOperatorable(Integer flag);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="whereable"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getWherable();
    public abstract void setWherable(Integer flag);

    //  EJB callbacks -----------------------------------------------------------
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
