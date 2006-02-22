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

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocal;
import com.sapienter.jbilling.interfaces.DescriptionEntityLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="OrderStatusEntity" 
 *          display-name="Object representation of the table ORDER_STATUS" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/OrderStatusEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/OrderStatusEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="order_status"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "order_status"
 * @jboss.read-only read-only="true"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class OrderStatusEntityBean implements EntityBean {
    
    private Logger log = null;
    
    private DescriptionEntityLocal getDescriptionObject(Integer language) {
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            DescriptionEntityLocalHome descriptionHome =
                    (DescriptionEntityLocalHome) EJBFactory.lookUpLocalHome(
                    DescriptionEntityLocalHome.class,
                    DescriptionEntityLocalHome.JNDI_NAME);

            return descriptionHome.findIt(Constants.TABLE_ORDER_STATUS,
                    getId(), "description", language);
        } catch (Exception e) {
            log.warn("Exception while looking for a order billing description", e);
            return null;
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
    public abstract void setId(Integer orderLineId);

    
	//  Custom fields ----------------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     */
    public String getDescription(Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language);
        if (desc == null) {
            return "Description not set for this order status";
        } else {
            return desc.getContent();
        }
    }
    public void setDescription(String description, Integer language) {
        DescriptionEntityLocal desc = getDescriptionObject(language);
        if (desc == null) {
            log.error("Can't update a non existing record");
        } else {
            desc.setContent(description);
        }
    }
	

    // EJB callbacks -----------------------------------------------------------
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
        log = Logger.getLogger(OrderStatusEntityBean.class);            
    }

    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
