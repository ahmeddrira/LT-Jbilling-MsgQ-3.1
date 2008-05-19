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

package com.sapienter.jbilling.server.order;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;

/**
 *
 * This is the session facade for the new order bean. It is a separate
 * bean becasue it has to be stateful and it's not good to make all the
 * interaction with orders stateful.
 *
 * @author emilc
 * @ejb:bean name="com/sapienter/jbilling/server/order/NewOrderSession"
 *           display-name="A stateful bean for new orders"
 *           type="Stateful"
 *           transaction-type="Container"
 *           view-type="remote"
 *           jndi-name="com/sapienter/jbilling/server/order/NewOrderSession"
 * 
 * @ejb.transaction type="Required" 
 **/

public class NewOrderSessionBean implements SessionBean {

    private static final Logger LOG = Logger.getLogger(NewOrderSessionBean.class);

    // -------------------------------------------------------------------------
    // Static
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Members 
    // -------------------------------------------------------------------------

    private SessionContext mContext;
    private Integer language = null;
    private OrderDTO order;

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------  

    public void setLanguage(Integer language) {
        this.language = language;
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public OrderDTO addItem(Integer itemID, Integer quantity, 
            Integer userId, Integer entityId) 
            throws SessionInternalError {

            LOG.debug("Adding item " + itemID + " q:" + quantity);

            OrderBL bl = new OrderBL(order);
            bl.addItem(itemID, quantity, language, userId, entityId, 
                    order.getCurrencyId());
            return order;

    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public OrderDTO deleteItem(Integer itemID) throws SessionInternalError {
        Logger log = Logger.getLogger(NewOrderSessionBean.class);
        log.debug("Deleting item " + itemID);

        new OrderBL(order).deleteItem(itemID);

        return order;

    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public OrderDTO recalculate(OrderDTO modifiedOrder, Integer entityId) 
            throws NamingException {
        
        OrderBL bl = new OrderBL();
        bl.set(modifiedOrder);
        bl.recalculate(entityId);
        return bl.getDTO();
    }
    
    /**
    * @ejb:interface-method view-type="remote"
    */
    public Integer createUpdate(Integer entityId, Integer executorId, 
            OrderDTO order) throws SessionInternalError {
        Integer retValue = null;
        if (language == null) {
            throw new SessionInternalError("The language has to be set.");
        }    
        try {
        	OrderBL bl = new OrderBL();
            if (order.getId() == null) {
                retValue = bl.create(entityId, executorId, new OrderDTO(order));
            } else {
                bl.set(order.getId());
                bl.update(executorId, order);
            }
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
        
        return retValue;
        
    }


    /**
    * Create the Session Bean
    *
    * @throws CreateException 
    *
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate(OrderDTO bean, Integer languageId) 
            throws CreateException {
        
        try {
        	order = bean;
            setLanguage(languageId);
            UserBL user = new UserBL(order.getBaseUserByUserId().getId());
            order.setCurrency(new CurrencyDAS().find(user.getCurrencyId()));
        } catch (Exception e) {
            LOG.debug("creation of bean", e);
            throw new CreateException(e.toString());
        }
    }

    /**
    * Describes the instance and its content for debugging purpose
    *
    * @return Debugging information about the instance and its content
    */
    public String toString() {
        return "NewOrderSessionBean [ " + " ]";
    }

    // -------------------------------------------------------------------------
    // Framework Callbacks
    // -------------------------------------------------------------------------  

    public void setSessionContext(SessionContext aContext)
        throws EJBException {
        mContext = aContext;
    }

    public void ejbActivate() throws EJBException {
        //try { newOrder = new OrderBL(newOrderDto); } catch (Exception e) {}
//        log = Logger.getLogger(NewOrderSessionBean.class);
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbRemove() throws EJBException {
    }
}
