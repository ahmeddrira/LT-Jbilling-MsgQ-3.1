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

import org.apache.log4j.Logger;


/**
 * @ejb:bean name="CreditCardUserEntity" 
 *          display-name="Object representation of the table user_credit_card_map"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/CreditCardUserEntity"
 *          jndi-name="com/sapienter/jbilling/server/entity/CreditCardUserEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="creditCardId"
 *          schema="user_credit_card_map"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "user_credit_card_map"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class CreditCardUserEntityBean implements EntityBean {
    private static final Logger log = Logger.getLogger(CreditCardUserEntityBean.class);

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer userId, Integer creditCardId) throws CreateException {
        setUserId(userId);
        setCreditCardId(creditCardId);

        return creditCardId;
    }
    
    public void ejbPostCreate(Integer userId, Integer creditCardId) {
    }

    // CMP field accessors -----------------------------------------------------
    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="credit_card_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getCreditCardId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCreditCardId(Integer id);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="user_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getUserId();
    public abstract void setUserId(Integer id);


    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
        try { 
            Logger.getLogger(this.getClass()).debug("Activating..."); 
        } catch (Exception e) {}
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
