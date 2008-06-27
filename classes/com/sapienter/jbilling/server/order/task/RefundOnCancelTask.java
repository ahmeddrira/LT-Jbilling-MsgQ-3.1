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
package com.sapienter.jbilling.server.order.task;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDecimalsException;
import com.sapienter.jbilling.server.item.db.Item;
import com.sapienter.jbilling.server.item.db.ItemDAS;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.order.db.OrderLineTypeDAS;
import com.sapienter.jbilling.server.order.db.OrderStatusDAS;
import com.sapienter.jbilling.server.order.event.PeriodCancelledEvent;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.system.event.task.IInternalEventsTask;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;

public class RefundOnCancelTask extends PluggableTask implements IInternalEventsTask {

    private static final Class<Event> events[] = new Class[] { PeriodCancelledEvent.class };

    private static final Logger LOG = Logger.getLogger(RefundOnCancelTask.class);

    public Class<Event>[] getSubscribedEvents() {
        return events;
    }

    public void process(Event event) {
        PeriodCancelledEvent myEvent = null;

        // validate the type of the event
        if (event instanceof PeriodCancelledEvent) {
            myEvent = (PeriodCancelledEvent) event;
        } else {
            throw new SessionInternalError("Can't process anything but a period cancel event");
        }

        OrderDTO order = myEvent.getOrder();
        LOG.debug("Plug in processing cancellation event for order " + order.getId());

        // local variables
        Integer userId = order.getBaseUserByUserId().getUserId();
        Integer entityId = order.getBaseUserByUserId().getCompany().getId();
        UserBL userBL;
        ResourceBundle bundle;
        try {
            userBL = new UserBL(userId);
            bundle = ResourceBundle.getBundle("entityNotifications", userBL.getLocale());
        } catch (Exception e) {
            throw new SessionInternalError("Error when doing credit", RefundOnCancelTask.class, e);
        }

        // create a new order that is the same as the original one, but all
        // negative prices
        OrderDTO newOrder = new OrderDTO(order);
        // reset the ids, so it is a new order
        newOrder.setId(null);
        newOrder.setVersionNum(null);
        newOrder.getOrderProcesses().clear(); // no invoices created for a new order
        newOrder.getLines().clear();
        // starts where the cancellation starts
        newOrder.setActiveSince(order.getActiveUntil());
        // ends where the original would invoice next
        newOrder.setActiveUntil(order.getNextBillableDay());
        newOrder.setNextBillableDay(null);
        newOrder.setIsCurrent(0);
        // add some clarification notes
        newOrder.setNotes(bundle.getString("order.credit.notes") + " " + order.getId());
        newOrder.setNotesInInvoice(0);
        // 
        // order lines:
        //
        for (OrderLineDTO line : order.getLines()) {
            OrderLineDTO newLine = new OrderLineDTO(line);
            // reset so they get inserted
            newLine.setId(0);
            newLine.setVersionNum(null);
            newLine.setPurchaseOrder(newOrder);
            newOrder.getLines().add(newLine);
            // make the order negative (refund/credit)
            newLine.setPrice(line.getPrice() * -1);
        }

        // add extra lines with items from the parameters
        for (String name : parameters.keySet()) {
            if (!name.startsWith("item")) {
                LOG.warn("parameter is not an item:" + name);
                continue; // not an item parameter
            }
            int itemId = Integer.parseInt((String) parameters.get(name));
            LOG.debug("adding item " + itemId + " to new order");
            Item item = new ItemDAS().findNow(itemId);
            if (item == null || item.getEntity().getId() != event.getEntityId()) {
                LOG.error("Item " + itemId + " not found");
                continue;
            }
            OrderLineDTO newLine = new OrderLineDTO();
            newLine.setDeleted(0);

            newLine.setDescription(item.getDescription(userBL.getEntity().getLanguageIdField()));
            newLine.setItem(item);
            newLine.setOrderLineType(new OrderLineTypeDAS().find(Constants.ORDER_LINE_TYPE_ITEM));
            try {
                newLine.setPrice(new ItemBL(itemId).getPrice(userId, entityId));
            } catch (Exception e) {
                throw new SessionInternalError("Error when doing credit", RefundOnCancelTask.class, e);
            }
            newLine.setQuantity(1);
            newLine.setPurchaseOrder(newOrder);
            newOrder.getLines().add(newLine);
        }

        // do the maths
        OrderBL orderBL = new OrderBL(newOrder);
        try {
            orderBL.recalculate(entityId);
        } catch (ItemDecimalsException e) {
            throw new SessionInternalError("Error when doing credit", RefundOnCancelTask.class, e);
        }

        // save
        Integer newOrderId = orderBL.create(entityId, null, newOrder);

        // audit so we know why all these changes happened
        new EventLogger().auditBySystem(entityId, Constants.TABLE_PUCHASE_ORDER, order.getId(),
                EventLogger.MODULE_ORDER_MAINTENANCE, EventLogger.ORDER_CANCEL_AND_CREDIT,
                newOrderId, null, null);

        //
        // Update original order
        //
        order.setOrderStatus(new OrderStatusDAS().find(Constants.ORDER_STATUS_FINISHED));
        order.setNotes(order.getNotes() + " - " + bundle.getString("order.cancelled.notes") + " "
                + newOrderId);
        
        LOG.debug("Credit done with new order " + newOrderId);

    }

    public String toString() {
        return "RefundOnCancelTask for events " + events;
    }

}
