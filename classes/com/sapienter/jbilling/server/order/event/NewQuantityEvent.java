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
package com.sapienter.jbilling.server.order.event;

import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.system.event.Event;

/**
 * This event is triggered when an order line's quantity is updated in an order.
 * 
 * @author Lucas Pickstone
 * 
 */
public class NewQuantityEvent implements Event {

    private final Integer entityId;
    private final Double oldQuantity;
    private final Double newQuantity;
    private final Integer orderId;
     // original (old) order line, unless line was newly added
    private final OrderLineDTO orderLine;
    
    public NewQuantityEvent(Integer entityId, Double oldQuantity, 
            Double newQuantity, Integer orderId, OrderLineDTO orderLine) {
        this.entityId = entityId;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
        this.orderId = orderId;
        this.orderLine = orderLine;
    }

    public Integer getEntityId() {
        return entityId;
    }
    
    public Double getOldQuantity() {
        return oldQuantity;
    }

    public Double getNewQuantity() {
        return newQuantity;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public OrderLineDTO getOrderLine() {
        return orderLine;
    }

    public String getName() {
        return "New Quantity Event - entity " + entityId;
    }
    
    public String toString() {
        return getName() + " - entity " + entityId;
    }

}
