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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.db.BaseUser;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;

public class CurrentOrder {
    private final Date eventDate;
    private final Integer userId;
    private final UserBL user;
    private static final Logger LOG = Logger.getLogger(CurrentOrder.class);
    private OrderBL order = null;
    private final EventLogger eLogger = EventLogger.getInstance();

    
    protected CurrentOrder(Integer userId, Date eventDate) throws FinderException{
        LOG.debug("Current order constructed with user " + userId + " event date " +
                eventDate);
        if (userId == null || eventDate == null) {
            throw new IllegalArgumentException("user and date are mandatory for current " +
                    "orders [" + userId + '-' + eventDate + ']');
        }
        this.userId = userId;
        this.eventDate = eventDate;
        user = new UserBL(userId);
    }
    
    /**
     * Returns the ID of a one-time order, where to add an event.
     * Returns null if no applicable order
     * @return
     */
    public Integer getCurrent() {
        Integer subscriptionId = user.getEntity().getCustomer().getCurrentOrderId();
        Integer entityId = null;
        Integer currencyId = null;
        if (subscriptionId == null) {
            return null;
        }
        try {
            order = new OrderBL(subscriptionId);
            entityId = order.getEntity().getBaseUserByUserId().getCompany().getId();
            currencyId = order.getEntity().getCurrencyId();
        } catch (Exception e) {
            throw new SessionInternalError("Error looking for main subscription order", 
                    CurrentOrder.class, e);
        }
        
        int futurePeriods = 0;
        boolean orderFound = false;
        do {
            LOG.debug("Calculating one timer date. Future periods " + futurePeriods);
            final Date newOrderDate = calculateDate(futurePeriods);
            if (newOrderDate == null) {
                // this is an error, there isn't a good date give the event date and
                // the main subscription order
                return null;
            }
            // now that the date is set, let's see if there is a one-time order for that date
            boolean somePresent = false;
            try {
                CachedRowSet rows = order.getOneTimersByDate(userId,
                        newOrderDate);
                while (rows.next()) {
                    somePresent = true;
                    int orderId = rows.getInt(1);
                    order.set(orderId);
                    if (order.getEntity().getStatusId().equals(
                            Constants.ORDER_STATUS_FINISHED)) {
                        LOG.debug("Found one timer " + orderId
                                + " but status is finished");
                    } else {
                        orderFound = true;
                        LOG.debug("Found existing one-time order");
                        break;
                    }
                }
                rows.close();
            } catch (Exception e) {
                throw new SessionInternalError(
                        "Error looking for one time orders",
                        CurrentOrder.class, e);
            }
            if (somePresent && !orderFound) {
                eLogger.auditBySystem(entityId, Constants.TABLE_PUCHASE_ORDER, order.getEntity().getId(), 
                        EventLogger.MODULE_MEDIATION, EventLogger.CURRENT_ORDER_FINISHED, 
                        subscriptionId, null, null);
            } else if (!somePresent) {
                // there aren't any one-time orders for this date at all, create one
                create(newOrderDate, currencyId, entityId);
                
                orderFound = true;
                LOG.debug("Created new one-time order");
            }
            // non present -> create new one with correct date
            // some present & none found -> try next date
            // some present & found -> use the found one
            futurePeriods++;
        } while (!orderFound);  
        
        // the result is in 'order'
        Integer retValue = order.getEntity().getId();
        LOG.debug("Returning " + retValue);
        return retValue;
    }
    
    /**
     * Assumes that the order has been set with the main subscription order
     * @return
     */
    private Date calculateDate(int futurePeriods) {
        GregorianCalendar cal = new GregorianCalendar();
        // start from the active since if it is there, otherwise the create time
        final Date startingTime = order.getEntity().getActiveSince() == null ? order
                .getEntity().getCreateDate() : order.getEntity()
                .getActiveSince();
                
        // calculate the event date with the added future periods
        Date actualEventDate = eventDate;
        cal.setTime(actualEventDate);
        for(int f = 0; f < futurePeriods; f++) {
            cal.add(MapPeriodToCalendar.map(order.getEntity().getOrderPeriod().getPeriodUnit().getId()), 
                    order.getEntity().getOrderPeriod().getValue());
        }
        actualEventDate = cal.getTime();
                
        // is the starting date beyond the time frame of the main order?
        if (order.getEntity().getActiveSince() != null && 
                actualEventDate.before(order.getEntity().getActiveSince())) {
            LOG.error("The event for date " + actualEventDate + " can not be assigned for " +
                    "order " + order.getEntity().getId() + 
                    " active since " + order.getEntity().getActiveSince());
            return null;
        }
        
        Date newOrderDate = startingTime;
        cal.setTime(startingTime);
        while (cal.getTime().before(actualEventDate)) {
            newOrderDate = cal.getTime();
            cal.add(MapPeriodToCalendar.map(order.getEntity().getOrderPeriod().getPeriodUnit().getId()), 
                    order.getEntity().getOrderPeriod().getValue());
        }
        
        // is the found date beyond the time frame of the main order?
        if (order.getEntity().getActiveUntil() != null && 
                newOrderDate.after(order.getEntity().getActiveUntil())) {
            LOG.error("The event for date " + actualEventDate + " can not be assigned for " +
                    "order " + order.getEntity().getId() + 
                    " active until " + order.getEntity().getActiveUntil());
            return null;
        }

        return newOrderDate;
    }
    
    public Integer create(Date activeSince, Integer currencyId, Integer entityId) {
        OrderDTO currentOrder = new OrderDTO();
        currentOrder.setCurrency(new CurrencyDTO(currencyId));
        // notes
        try {
            EntityBL entity = new EntityBL(entityId);
            ResourceBundle bundle = ResourceBundle.getBundle("entityNotifications", 
                    entity.getLocale());
            currentOrder.setNotes(bundle.getString("order.current.notes"));
        } catch (Exception e) {
            throw new SessionInternalError("Error setting the new order notes", 
                    CurrentOrder.class, e);
        } 

        currentOrder.setActiveSince(activeSince);
        
        // create the order
        if (order == null) {
            try {
                order = new OrderBL();
            } catch (NamingException e) {
                throw new SessionInternalError("Error creating order", 
                        CurrentOrder.class, e);
            }
        }
        order.set(currentOrder);
        order.addRelationships(userId, Constants.ORDER_PERIOD_ONCE, currencyId);
        return order.create(entityId, null, currentOrder);
    }
}
