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

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PluggableTaskTypeEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;


/**
 * @ejb:bean name="PluggableTaskEntity" 
 *          display-name="Object representation of the table PLUGGABLE_TASK" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/PluggableTaskEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/PluggableTaskEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="pluggable_task"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @ejb:finder signature="Collection findAllByEntity(java.lang.Integer entityId)"
 *             query="SELECT OBJECT(b) 
 *                      FROM pluggable_task b 
 *                     WHERE b.entityId = ?1"
 *             result-type-mapping="Local"
 *
 * @ejb:finder signature="PluggableTaskEntityLocal findByEntityType(java.lang.Integer entityId, java.lang.Integer typeId)"
 *             query="SELECT OBJECT(b) 
 *                      FROM pluggable_task b 
 *                     WHERE b.entityId = ?1
 *                       AND b.type.id = ?2"
 *             result-type-mapping="Local"
 *
 * @ejb.value-object name="PluggableTask"
 * 
 * @jboss:table-name "pluggable_task"
 * 
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */

public abstract class PluggableTaskEntityBean implements EntityBean {

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer entityId, 
            PluggableTaskTypeEntityLocal type, Integer processingOrder) 
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
                    Constants.TABLE_PLUGGABLE_TASK));

        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the pluggable task table");
        }

        setId(newId);
        setEntityId(entityId);
        setProcessingOrder(processingOrder);
        
        return newId;
    }
    
    public void ejbPostCreate(Integer entityId, 
            PluggableTaskTypeEntityLocal type, Integer processingOrder) {
        setType(type);
    }
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
     * @jboss:column-name name="entity_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getEntityId();
    public abstract void setEntityId(Integer entityId);

    /**
     * @ejb:interface-method view-type="local
     * @ejb:persistent-field
     * @jboss:column-name name="processing_order"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getProcessingOrder();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setProcessingOrder(Integer pOrder);

    // CMR fields --------------------------------------------------
    /**
     * 
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="pluggable_task-type"
     *               role-name="task-has-type"
     *               target-ejb="PluggableTaskTypeEntity"
     *               target-role-name="task_type-provides"
     *               target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="type_id"            
     */
    public abstract PluggableTaskTypeEntityLocal getType();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setType(PluggableTaskTypeEntityLocal pType);

    /**
     * 
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="pluggable_task-parameters"
     *               role-name="task-has-parameters"
     */
    public abstract Collection getParameters();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setParameters(Collection parameters);

    //  EJB callbacks -----------------------------------------------------------

    /**
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove()
        throws RemoveException, EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext arg0)
        throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
