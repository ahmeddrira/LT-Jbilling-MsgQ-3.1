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
 * Created on 14-Apr-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;


/**
 * @ejb:bean name="PluggableTaskParameterEntity" 
 *          display-name="Object representation of the table PLUGGABLE_TASK_PARAMETER" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PluggableTaskParameterEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PluggableTaskParameterEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="pluggable_task_parameter"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 * 
 * @ejb.value-object name="PluggableTaskParameter"
 * 
 * @jboss:table-name "pluggable_task_parameter"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class PluggableTaskParameterEntityBean implements EntityBean {
    //  CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getId();
    public abstract void setId(Integer ruleId);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="name"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getName();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setName(String name);

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
    public abstract void setStrValue(String str);

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
    public abstract void setFloatValue(Float flo);


	// EJB Callbacks -------------------------------------------------------------
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
