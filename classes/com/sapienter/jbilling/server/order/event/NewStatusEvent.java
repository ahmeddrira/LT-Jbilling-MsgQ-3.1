package com.sapienter.jbilling.server.order.event;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.system.event.Event;

public class NewStatusEvent implements Event {

    private static final Logger LOG = Logger.getLogger(NewStatusEvent.class); 
    private Integer entityId;
    private Integer userId;
    private Integer orderId;
    private Integer orderType;
    private Integer oldStatusId;
    private Integer newStatusId;
    
    public NewStatusEvent(Integer orderId, Integer oldStatusId, Integer newStatusId) {
        try {
            OrderBL order = new OrderBL(orderId);
            
            this.entityId = order.getEntity().getUser().getEntity().getId();
            this.userId = order.getEntity().getUser().getUserId();
            this.orderType = order.getEntity().getOrderPeriod().getId();
            this.oldStatusId = oldStatusId;
            this.newStatusId = newStatusId;
        } catch (Exception e) {
            LOG.error("Handling order in event", e);
        } 
        this.orderId = orderId;
    }
    
    public Integer getEntityId() {
        return entityId;
    }

    public String getName() {
        return "New status";
    }

    public String toString() {
        return getName();
    }
    public Integer getOrderId() {
        return orderId;
    }
    public Integer getUserId() {
        return userId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public Integer getNewStatusId() {
        return newStatusId;
    }

    public Integer getOldStatusId() {
        return oldStatusId;
    }

    
}
