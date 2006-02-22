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
 * Created on Mar 9, 2004
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
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;
import com.sapienter.jbilling.server.util.Constants;

/**
 * @ejb:bean name="ItemPriceEntity" 
 *          display-name="Object representation of the table ITEM_PRICE"
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/ItemPriceEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/ItemPriceEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="item_price"
 *
 * @ejb:finder signature="ItemPriceEntityLocal find(java.lang.Integer itemId, java.lang.Integer currencyId) "
 *             query="SELECT OBJECT(p)
 *                      FROM item AS i, IN (i.prices) p  
 *                     WHERE p.currencyId = ?2
 *                       AND i.id = ?1"
 *
 * @ejb.value-object name="ItemPrice"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "item_price"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */
public abstract class ItemPriceEntityBean implements EntityBean {

    private Logger log = null;
    
    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(Integer currencyId, Float price) 
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
                    Constants.TABLE_ITEM_PRICE));

        } catch (Exception e) {
            log.error("Exception creating item price", e);
            throw new CreateException(
                    "Problems generating the primary key "
                    + "for the item_price table");
        }
        setId(newId);
        setPrice(price);
        setCurrencyId(currencyId);

        return newId;
    }
    
    public void ejbPostCreate(Integer currencyId, Float price) {
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
      * @jboss:column-name name="price"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Float getPrice();
    /**
      * @ejb:interface-method view-type="local"
      */
    public abstract void setPrice(Float price);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="currency_id"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Integer getCurrencyId();
    public abstract void setCurrencyId(Integer currencyId);

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
    public void setEntityContext(EntityContext context) {
        log = Logger.getLogger(CurrencyEntityBean.class);
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() throws EJBException, RemoteException {
    }

}
