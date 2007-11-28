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

 package com.sapienter.jbilling.server.entity;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.interfaces.OrderLineTypeEntityLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocal;
import com.sapienter.jbilling.interfaces.SequenceSessionLocalHome;

/**
 * @ejb:bean name="OrderLineEntity" 
 *          display-name="Object representation of the table ORDER_LINE" 
 *          view-type="local" 
 *          type="CMP" 
 *          local-jndi-name="com/sapienter/jbilling/server/entity/OrderLineEntityLocal"
 *          jndi-name="com/sapienter/jbilling/server/entity/OrderLineEntity"
 *          reentrant="false"
 *          cmp-version="2.x"
 *          primkey-field="id"
 *          schema="order_line"
 *
 * @ejb.value-object name="OrderLine"
 *
 * @ejb:pk class="java.lang.Integer"
 *         generate="false"
 *
 * @jboss:table-name "order_line"
 * @jboss:create-table create="false"
 * @jboss:remove-table remove="false"
 */

public abstract class OrderLineEntityBean implements EntityBean {
    private EntityContext ctx;
    private Logger log = null;

    /**
     * @ejb:create-method view-type="local"
     */
    public Integer ejbCreate(
        Integer itemId,
        OrderLineTypeEntityLocal type,
        String description,
        Float amount,
        Integer quantity,
        Float price,
        Integer itemPrice,
        Integer deleted)
        throws CreateException {
        Integer newId;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            SequenceSessionLocalHome generatorHome =
                (SequenceSessionLocalHome) EJBFactory.lookUpLocalHome(
                    SequenceSessionLocalHome.class,
                    SequenceSessionLocalHome.JNDI_NAME);

            SequenceSessionLocal generator = generatorHome.create();
            newId = new Integer(generator.getNextSequenceNumber("order_line"));
        } catch (Exception e) {
            throw new CreateException(
                "Problems generating the primary key "
                    + "for the purchase order table");
        }
        setId(newId);
        setItemId(itemId);
        if (description.length() > 1000) {
            description = description.substring(0, 1000);
            log.warn("Truncated an order line description to " + description);
        }

        setDescription(description);
        setAmount(amount);
        setQuantity(quantity);
        setPrice(price);
        setItemPrice(itemPrice);
        setDeleted(deleted);
        setCreateDate(Calendar.getInstance().getTime());

        return newId;
    }

    public void ejbPostCreate(
        Integer itemId,
        OrderLineTypeEntityLocal type,
        String description,
        Float amount,
        Integer quantity,
        Float price,
        Integer itemPrice,
        Integer deleted) {
            
        setType(type);
        
    }
    
    /**
     * @ejb:interface-method view-type="local"
     * @return boolean
     */
    public boolean getEditable() {
        return getType().getEditable().intValue() == 1;
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

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="item_id"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getItemId();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setItemId(Integer itemId);


    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="description"
     * @jboss.method-attributes read-only="true"
     */
    public abstract String getDescription();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDescription(String description);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
     * @jboss:column-name name="amount"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Float getAmount();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setAmount(Float amount);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="quantity"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getQuantity();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setQuantity(Integer quantity);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @ejb:pk-field
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
     * @jboss:column-name name="item_price"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getItemPrice();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setItemPrice(Integer itemPrice);

    /**
      * @ejb:interface-method view-type="local"
      * @ejb:persistent-field
      * @jboss:column-name name="create_datetime"
     * @jboss.method-attributes read-only="true"
      */
    public abstract Date getCreateDate();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setCreateDate(Date create);

    /**
     * @ejb:interface-method view-type="local"
     * @ejb:persistent-field
     * @jboss:column-name name="deleted"
     * @jboss.method-attributes read-only="true"
     */
    public abstract Integer getDeleted();
    /**
     * @ejb:interface-method view-type="local"
     */
    public abstract void setDeleted(Integer deleted);
    
    // CMR field accessors -----------------------------------------------------
    /**
     * 
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="order_lines-type"
     *               role-name="orderlines-has-type"
     *               target-ejb="OrderLineTypeEntity"
     *               target-role-name="type-provides-orderlines"
     * 				 target-multiple="yes"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="type_id"            
     */
    public abstract OrderLineTypeEntityLocal getType();
    /**
     * @ejb:interface-method view-type="local"
     * @param type
     */
    public abstract void setType(OrderLineTypeEntityLocal type);
     

    /**
     * @ejb:interface-method view-type="local"
     * @ejb.relation name="order-order_lines"
     *               role-name="oorderline-belongs_to-order"
     * @jboss.relation related-pk-field="id"  
     *                 fk-column="order_id"            
     */
    public abstract OrderEntityLocal getOrder();
    public abstract void setOrder(OrderEntityLocal order);
    
    // EJB callbacks -----------------------------------------------------------
    public void setEntityContext(EntityContext context) {
        ctx = context;
        log = Logger.getLogger(OrderLineEntityBean.class);
    }

    public void unsetEntityContext() {
        ctx = null;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbStore() {
    }

    public void ejbLoad() {
    }

    /* (non-Javadoc)
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove()
        throws RemoveException, EJBException, RemoteException {
    }

}
