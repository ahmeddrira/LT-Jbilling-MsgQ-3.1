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

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO;

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
    //private SessionContext context = null;

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
        	OrderDAS das = new OrderDAS();
        	OrderDTO order = das.find(orderId);
        	order.touch();
        	return order;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public OrderDTO getOrderEx(Integer orderId, Integer languageId) 
            throws SessionInternalError {
        try {
        	OrderDAS das = new OrderDAS();
        	OrderDTO order = das.find(orderId);
        	order.addExtraFields(languageId);
        	order.touch();
        	Collections.sort(order.getLines(), new OrderLineComparator());
        	//LOG.debug("returning order " + order);
        	return order;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
    * @ejb:interface-method view-type="remote"
    */
    public OrderDTO setStatus(Integer orderId, Integer statusId, 
            Integer executorId, Integer languageId) 
            throws SessionInternalError {
        try {
            OrderBL order = new OrderBL(orderId);
            order.setStatus(executorId, statusId);
            OrderDTO dto = order.getDTO();
            dto.addExtraFields(languageId);
            dto.touch();
            return dto;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * This is a version used by the http api, should be
     * the same as the web service but without the 
     * security check
     * @ejb:interface-method view-type="remote"
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
     */


    /**
     * @ejb:interface-method view-type="remote"
     */
    public void delete(Integer id, Integer executorId) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL(id);
            bl.delete(executorId);
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }

    }
 
    /**
     * @ejb:interface-method view-type="remote"
     */
    public OrderPeriodDTO[] getPeriods(Integer entityId, Integer languageId) 
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
    public OrderPeriodDTO getPeriod(Integer languageId, Integer id) 
            throws SessionInternalError {
        try {
            // now get the order
            OrderBL bl = new OrderBL();
            OrderPeriodDTO dto =  bl.getPeriod(languageId, id);
            dto.touch();
            
            return dto;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * @ejb:interface-method view-type="remote"
     */
    public void setPeriods(Integer languageId, OrderPeriodDTO[] periods) 
            throws SessionInternalError {
        try {
			OrderBL bl = new OrderBL();
			bl.updatePeriods(languageId, periods);
		} catch (NamingException e) {
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
        //context = aContext;
    }

}
