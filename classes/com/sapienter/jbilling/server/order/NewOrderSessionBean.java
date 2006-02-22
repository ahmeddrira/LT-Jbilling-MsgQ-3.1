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

package com.sapienter.jbilling.server.order;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.user.UserBL;

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

    // -------------------------------------------------------------------------
    // Static
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // Members 
    // -------------------------------------------------------------------------

    private SessionContext mContext;
    private transient OrderBL newOrder = null;
    private Integer language = null;
    private NewOrderDTO newOrderDto = null;

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------  

    public void setLanguage(Integer language) {
        this.language = language;
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public NewOrderDTO addItem(Integer itemID, Integer quantity, 
            Integer userId, Integer entityId) 
            throws SessionInternalError, FinderException {

        try {
            Logger log = Logger.getLogger(NewOrderSessionBean.class);
            log.debug("Adding item " + itemID + " q:" + quantity);

            ItemBL itemBL = new ItemBL(itemID);
            ItemDTOEx newItem = itemBL.getDTO(language, userId, entityId, 
                    newOrderDto.getCurrencyId());
            newOrder.addItem(newItem, quantity);

            return newOrder.getNewOrderDTO();
        } catch (NamingException e) {
            throw new SessionInternalError(e);
        } 

    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public NewOrderDTO deleteItem(Integer itemID) throws SessionInternalError {
        Logger log = Logger.getLogger(NewOrderSessionBean.class);
        log.debug("Deleting item " + itemID);

        newOrder.deleteItem(itemID);

        return newOrder.getNewOrderDTO();

    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public NewOrderDTO recalculate(NewOrderDTO modifiedOrder, Integer entityId) 
            throws SessionInternalError {
        
        newOrder.setDTO(modifiedOrder);
        newOrder.recalculate(entityId);
        return newOrder.getNewOrderDTO();
    }
    
    /**
    * @ejb:interface-method view-type="remote"
    */
    public Integer createUpdate(Integer entityId, Integer executorId, 
            NewOrderDTO order) throws SessionInternalError {
        Integer retValue = null;
        if (language == null) {
            throw new SessionInternalError("The language has to be set.");
        }    
        try {
            if (order.getId() == null) {
                retValue = newOrder.create(entityId, executorId, order);
            } else {
                newOrder.set(order.getId());
                newOrder.update(executorId, order);
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
    public void ejbCreate(NewOrderDTO bean, Integer languageId) 
            throws CreateException {
        
        try {
            newOrderDto = bean;
            newOrder = new OrderBL(bean);
            setLanguage(languageId);
            // initialize the currency
            UserBL user = new UserBL(newOrderDto.getUserId());
            newOrderDto.setCurrencyId(user.getCurrencyId());
            CurrencyBL currency = new CurrencyBL(newOrderDto.getCurrencyId());
            newOrderDto.setCurrencySymbol(currency.getEntity().getSymbol());
            
        } catch (Exception e) {
            Logger.getLogger(NewOrderSessionBean.class).debug(
                    "creation of bean", e);
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
        try { newOrder = new OrderBL(newOrderDto); } catch (Exception e) {}
//        log = Logger.getLogger(NewOrderSessionBean.class);
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbRemove() throws EJBException {
    }
}
