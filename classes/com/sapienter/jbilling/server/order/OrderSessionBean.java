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

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.DTOFactory;

/**
 *
 * This is the session facade for the orders in general. It is a statless
 * bean that provides services not directly linked to a particular operation
 *
 * @author emilc
 * @ejb:bean name="OrderSession"
 *           display-name="A stateless bean for orders"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type="both"
 *           jndi-name="com/sapienter/jbilling/server/order/OrderSession"
 * 
 * 
 **/
public class OrderSessionBean implements SessionBean {
    
    private static final Logger LOG = Logger.getLogger(OrderSessionBean.class);
    private SessionContext context = null;

    /**
    * Create the Session Bean
    * @throws CreateException
    * @ejb:create-method view-type="remote"
    */
    public void ejbCreate() throws CreateException {
    }

    /**
     * @ejb:interface-method view-type="remote"
     * @ejb.transaction type="RequiresNew"
     */
    public void reviewNotifications(Date today) 
    		throws SessionInternalError {
    	
    	try {
    		OrderBL order = new OrderBL();
    		order.reviewNotifications(today);
    	} catch (Exception e) {
    		throw new SessionInternalError(e);
    	}
    }
    /**
    * @ejb:interface-method view-type="remote"
    */
    public OrderDTO getOrder(Integer orderId) throws SessionInternalError {
        try {
            OrderBL order = new OrderBL(orderId);
            return order.getDTO();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public OrderDTOEx getOrderEx(Integer orderId, Integer languageId) 
            throws SessionInternalError {
        try {
            return DTOFactory.getOrderDTOEx(orderId, languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public OrderDTOEx setStatus(Integer orderId, Integer statusId, 
            Integer executorId, Integer languageId) 
            throws SessionInternalError {
        try {
            OrderBL order = new OrderBL(orderId);
            order.setStatus(executorId, statusId);
            return DTOFactory.getOrderDTOEx(orderId, languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * This is a version used by the http api, should be
     * the same as the web service but without the 
     * security check
     * @ejb:interface-method view-type="remote"
     */
    public Integer create(OrderWS order, Integer entityId,
            String rootUser, boolean process) 
            throws SessionInternalError {
        try {
            LOG.debug("bkp 1");
            // get the info from the caller
            UserBL bl = new UserBL();
            bl.setRoot(rootUser);
            Integer executorId = bl.getEntity().getUserId();
            LOG.debug("bkp 2");
            // make a dto out of the ws
            NewOrderDTO dto = new NewOrderDTO(order);
            
            // call the creation
            OrderBL orderBL = new OrderBL(dto);
            if (process) {
                orderBL.fillInLines(dto, entityId);
                orderBL.recalculate(entityId);
            }
            LOG.debug("bkp 4");
            return orderBL.create(entityId, executorId, dto);
            
        } catch(Exception e) {
            LOG.debug("Exception:", e);
            throw new SessionInternalError(e);
        }

    }


    /**
     * @ejb:interface-method view-type="remote"
     */
    public void delete(Integer id) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL(id);
            bl.delete();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }

    }
 
    /**
     * @ejb:interface-method view-type="remote"
     */
    public OrderPeriodDTOEx[] getPeriods(Integer entityId, Integer languageId) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL();
            return bl.getPeriods(entityId, languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public OrderPeriodDTOEx getPeriod(Integer languageId, Integer id) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL();
            return bl.getPeriod(languageId, id);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void setPeriods(Integer languageId, OrderPeriodDTOEx[] periods) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL();
            bl.updatePeriods(languageId, periods);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void addPeriod(Integer entityId, Integer languageId) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL();
            bl.addPeriod(entityId, languageId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public Boolean deletePeriod(Integer periodId) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL();
            return new Boolean(bl.deletePeriod(periodId));
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    // EJB Callbacks -------------------------------------------------

    /**
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException, RemoteException {
    }

    /**
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext aContext)
            throws EJBException {
        context = aContext;
    }

}
