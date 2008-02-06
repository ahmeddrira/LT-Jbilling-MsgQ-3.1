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

/*
 * Created on 15-Mar-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.order;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.EntityEntityLocal;
import com.sapienter.jbilling.interfaces.NotificationSessionLocal;
import com.sapienter.jbilling.interfaces.NotificationSessionLocalHome;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.interfaces.OrderEntityLocalHome;
import com.sapienter.jbilling.interfaces.OrderLineEntityLocal;
import com.sapienter.jbilling.interfaces.OrderLineEntityLocalHome;
import com.sapienter.jbilling.interfaces.OrderLineTypeEntityLocal;
import com.sapienter.jbilling.interfaces.OrderLineTypeEntityLocalHome;
import com.sapienter.jbilling.interfaces.OrderPeriodEntityLocal;
import com.sapienter.jbilling.interfaces.OrderPeriodEntityLocalHome;
import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.PromotionBL;
import com.sapienter.jbilling.server.item.tasks.IItemPurchaseManager;
import com.sapienter.jbilling.server.list.ResultList;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.notification.MessageDTO;
import com.sapienter.jbilling.server.notification.NotificationBL;
import com.sapienter.jbilling.server.notification.NotificationNotFoundException;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderProcessDTO;
import com.sapienter.jbilling.server.order.event.NewActiveUntilEvent;
import com.sapienter.jbilling.server.order.event.NewStatusEvent;
import com.sapienter.jbilling.server.pluggableTask.OrderProcessingTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.process.ConfigurationBL;
import com.sapienter.jbilling.server.system.event.EventManager;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.EntityBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.DTOFactory;
import com.sapienter.jbilling.server.util.PreferenceBL;
import com.sapienter.jbilling.server.util.audit.EventLogger;

/**
 * @author Emil
 */
public class OrderBL extends ResultList 
        implements OrderSQL {
    private NewOrderDTO newOrder = null;
    private JNDILookup EJBFactory = null;
    private OrderEntityLocalHome orderHome = null;
    private OrderEntityLocal order = null;
    private OrderLineEntityLocalHome orderLineHome = null;
    private OrderLineTypeEntityLocalHome orderLineTypeHome = null;
    private OrderPeriodEntityLocalHome orderPeriodHome = null;
    private OrderDAS orderDas = null;
    
    private static final Logger LOG = Logger.getLogger(OrderBL.class);
    private EventLogger eLogger = null;

    public OrderBL(Integer orderId) 
            throws NamingException, FinderException {
        init();
        set(orderId);
    }

    public OrderBL() throws NamingException {
        init();
    }
    
    public OrderBL (OrderEntityLocal order) {
        try {
            init();
        } catch (NamingException e) {
            throw new SessionInternalError(e);
        }
        this.order = order;
    }
    
    
    public OrderBL(NewOrderDTO order) throws NamingException {
        newOrder = order;
        init();
    }


    private void init() throws NamingException {
        eLogger = EventLogger.getInstance();        
        EJBFactory = JNDILookup.getFactory(false);
        orderHome = (OrderEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                OrderEntityLocalHome.class,
                OrderEntityLocalHome.JNDI_NAME);
        orderLineHome = (OrderLineEntityLocalHome) EJBFactory.lookUpLocalHome(
                OrderLineEntityLocalHome.class,
                OrderLineEntityLocalHome.JNDI_NAME);

        orderLineTypeHome = (OrderLineTypeEntityLocalHome) 
                EJBFactory.lookUpLocalHome(
                OrderLineTypeEntityLocalHome.class,
                OrderLineTypeEntityLocalHome.JNDI_NAME);
        orderPeriodHome =
                (OrderPeriodEntityLocalHome) EJBFactory.lookUpLocalHome(
                OrderPeriodEntityLocalHome.class,
                OrderPeriodEntityLocalHome.JNDI_NAME);
    
        orderDas = new OrderDAS();
    }

    public OrderEntityLocal getEntity() {
        return order;
    }
    
    public OrderEntityLocalHome getHome() {
        return orderHome;
    }
    
    public OrderPeriodDTOEx getPeriod(Integer language, Integer id) 
            throws FinderException {
        OrderPeriodEntityLocal period = orderPeriodHome.findByPrimaryKey(id);
        OrderPeriodDTOEx dto = new OrderPeriodDTOEx();
        dto.setDescription(period.getDescription(language));
        dto.setEntityId(period.getEntityId());
        dto.setId(period.getId());
        dto.setUnitId(period.getUnitId());
        dto.setValue(period.getValue());
        
        return dto;
    }

    public void set(Integer id) throws FinderException {
        order = orderHome.findByPrimaryKey(id);
    }
    
    public void set(OrderEntityLocal newOrder) {
        order = newOrder;
    }

    public void setUserCurrent(Integer userId) {
        try {
            UserBL user = new UserBL(userId);
            Integer orderId = user.getEntity().getCustomer().getCurrentOrderId();
            if (orderId != null) {
                set(orderId);
                // deleted does not count
                if (order.getDeleted().equals(1)) {
                    order = null;
                }
            } else {
                order = null;
            }
        } catch (FinderException e) {
            throw new SessionInternalError("Can not find for current order", OrderBL.class, e);
        }
    }

    public NewOrderDTO getNewOrderDTO() {
        return newOrder;
    }
    
    public OrderWS getWS(Integer languageId) 
            throws FinderException, NamingException {
        OrderWS retValue = new OrderWS(getDTO());
        
        retValue.setPeriod(order.getPeriod().getId());
        retValue.setPeriodStr(order.getPeriod().getDescription(languageId));
        retValue.setBillingTypeStr(DTOFactory.getBillingTypeString(
                order.getBillingTypeId(), languageId));
        retValue.setUserId(order.getUser().getUserId());
        
        Vector<OrderLineWS> lines = new Vector<OrderLineWS>();
        for (Iterator it = order.getOrderLines().iterator(); it.hasNext();) {
            OrderLineEntityLocal line = (OrderLineEntityLocal) it.next();
            if (line.getDeleted().intValue() == 0) {
                OrderLineWS lineWS = new OrderLineWS(DTOFactory.getOrderLineDTOEx(line));
                lineWS.setTypeId(line.getType().getId());
                lines.add(lineWS);
            }
        }
        retValue.setOrderLines(new OrderLineWS[lines.size()]);
        lines.toArray(retValue.getOrderLines());
        
        return retValue;
    }
    
    public OrderDTO getDTO() {
        return new OrderDTO(order.getId(), order.getBillingTypeId(), 
        	order.getNotify(),
            order.getActiveSince(), order.getActiveUntil(), 
            order.getCreateDate(), order.getNextBillableDay(),
            order.getCreatedBy(), order.getStatusId(), order.getDeleted(),
            order.getCurrencyId(), order.getLastNotified(),
            order.getNotificationStep(), order.getDueDateUnitId(),
            order.getDueDateValue(), order.getDfFm(),
            order.getAnticipatePeriods(), order.getOwnInvoice(),
            order.getNotesInInvoice(), order.getNotes(), order.getIsCurrent());
    }

    public void setDTO(NewOrderDTO mOrder) {
        newOrder = mOrder;
    }

    public void addItem(Integer itemID, Integer quantity, Integer language, 
            Integer userId, Integer entityId, Integer currencyId, Vector<Record> records) {

            try {
                PluggableTaskManager<IItemPurchaseManager> taskManager =
                    new PluggableTaskManager<IItemPurchaseManager>(entityId,
                    Constants.PLUGGABLE_TASK_ITEM_MANAGER);
                IItemPurchaseManager myTask = taskManager.getNextClass();
                
                while(myTask != null) {
                    myTask.addItem(itemID, quantity, language, userId, entityId, currencyId, newOrder, records);
                    myTask = taskManager.getNextClass();
                }
            } catch (Exception e) {
                // do not change this error text, it is used to identify the error
                throw new SessionInternalError("Item Manager task error", OrderBL.class, e);
            }

    }

    public void addItem(Integer itemID, Integer quantity, Integer language, 
            Integer userId, Integer entityId, Integer currencyId) {
        addItem(itemID, quantity, language, userId, entityId, currencyId, null);
    }
    
    public void deleteItem(Integer itemID) {
        newOrder.removeOrderLine(itemID);
    }
    
    public void delete() {
        for (Iterator it = order.getOrderLines().iterator(); it.hasNext();) {
            OrderLineEntityLocal line = (OrderLineEntityLocal) it.next();
            line.setDeleted(new Integer(1));
        }
        
        order.setDeleted(new Integer(1));
    }

    /**
     * Method recalculate.
     * Goes over the processing tasks configured in the database for this
     * entity. The NewOrderDTO of this session is then modified.
     */
    public void recalculate(Integer entityId) throws SessionInternalError {
        LOG.debug("Processing and order for reviewing." + newOrder.getOrderLinesMap().size());

        try {
            PluggableTaskManager taskManager = new PluggableTaskManager(
                    entityId, Constants.PLUGGABLE_TASK_PROCESSING_ORDERS);
            OrderProcessingTask task = 
                    (OrderProcessingTask) taskManager.getNextClass();
            while (task != null) {
                task.doProcessing(newOrder);
                task = (OrderProcessingTask) taskManager.getNextClass();
            }

        } catch (PluggableTaskException e) {
            LOG.fatal("Problems handling order processing task.", e);
            throw new SessionInternalError("Problems handling order " +
                    "processing task.");
        } catch (TaskException e) {
			LOG.fatal("Problems excecuting order processing task.", e);
			throw new SessionInternalError("Problems executing order processing task.");
        }
    }
    
    public Integer create(Integer entityId, Integer userAgentId,
            NewOrderDTO orderDto) throws SessionInternalError {
        Integer newOrderId = null;

        try {
            // if the order is a one-timer, force pre-paid to avoid any
            // confusion
            if (orderDto.getPeriod().equals(Constants.ORDER_PERIOD_ONCE)) {
                orderDto.setBillingTypeId(Constants.ORDER_BILLING_PRE_PAID);
                // one time orders can not be the main subscription
                orderDto.setIsCurrent(new Integer(0));
            }
            /*
             * First create the order record
             */
            // create the record 
            order = orderHome.create(entityId, orderDto.getPeriod(),
                    userAgentId, orderDto.getUserId(),
                    orderDto.getBillingTypeId(), orderDto.getCurrencyId());
            order.setActiveUntil(orderDto.getActiveUntil());
            order.setActiveSince(orderDto.getActiveSince());
            order.setNotify(orderDto.getNotify());
            order.setDueDateUnitId(orderDto.getDueDateUnitId());
            order.setDueDateValue(orderDto.getDueDateValue());
            order.setDfFm(orderDto.getDfFm());
            order.setAnticipatePeriods(orderDto.getAnticipatePeriods());
            order.setOwnInvoice(orderDto.getOwnInvoice());
            order.setNotes(orderDto.getNotes());
            order.setNotesInInvoice(orderDto.getNotesInInvoice());
            if (orderDto.getIsCurrent() != null && orderDto.getIsCurrent().intValue() == 1) {
                setMainSubscription(userAgentId);
            }

            // get the collection of lines to update the fk of the table
            newOrderId = order.getId();
            createLines(orderDto);
            
            // update any promotion involved
            updatePromotion(entityId, orderDto.getPromoCode());
        } catch (Exception e) {
            LOG.fatal("Create exception creating order entity bean", e);
            throw new SessionInternalError(e);
        } 
        
        return newOrderId;
    }

    public void update(Integer executorId, NewOrderDTO dto) 
            throws FinderException, CreateException {
        // update first the order own fields
        if (!Util.equal(order.getActiveUntil(), dto.getActiveUntil())) {
            audit(executorId, order.getActiveUntil());
            // this needs an event
            NewActiveUntilEvent event = new NewActiveUntilEvent(
                    order.getId(), dto.getActiveUntil(), 
                    order.getActiveUntil());
            EventManager.process(event);
            // update the period of the latest invoice as well
            OrderProcessDTO process = null;
            if (order.getActiveUntil() != null) {
            	process = orderDas.findProcessByEndDate(order.getId(), 
            		order.getActiveUntil());
            }
            if (process != null) {
            	LOG.debug("Updating process id " + process.getId());
            	process.setPeriodEnd(dto.getActiveUntil());
            	
            } else {
            	LOG.debug("Did not find any process for order " + order.getId() +  
            			" and date " + order.getActiveUntil());
            }
            // update it
            order.setActiveUntil(dto.getActiveUntil());
        }
        if (!Util.equal(order.getActiveSince(), dto.getActiveSince())) {
            audit(executorId, order.getActiveSince());
            order.setActiveSince(dto.getActiveSince());
        }
        setStatus(executorId, dto.getStatusId());
        
        OrderPeriodEntityLocal period = orderPeriodHome.findByPrimaryKey(
                dto.getPeriod());
        if (order.getPeriod().getId().compareTo(period.getId()) != 0) {
            audit(executorId, order.getPeriod().getId());
            order.setPeriod(period);
        }
        order.setBillingTypeId(dto.getBillingTypeId());
        order.setNotify(dto.getNotify());
        order.setDueDateUnitId(dto.getDueDateUnitId());
        order.setDueDateValue(dto.getDueDateValue());
        order.setDfFm(dto.getDfFm());
        order.setAnticipatePeriods(dto.getAnticipatePeriods());
        order.setOwnInvoice(dto.getOwnInvoice());
        order.setNotes(dto.getNotes());
        order.setNotesInInvoice(dto.getNotesInInvoice());
        if (dto.getIsCurrent() != null && dto.getIsCurrent().intValue() == 1) {
            setMainSubscription(executorId);
        }
        if (dto.getIsCurrent() != null && dto.getIsCurrent().intValue() == 0) {
            unsetMainSubscription(executorId);
        }
        // this one needs more to get updated
        updateNextBillableDay(executorId, dto.getNextBillableDay());
        OrderLineEntityLocal oldLine = null;
    	int nonDeletedLines = 0;
        // Determine if the item of the order changes and, if it is,
        // LOG a subscription change event.
        LOG.info("Order lines: " + order.getOrderLines().size() + "  --> new Order: "+
        		dto.getNumberOfLines().intValue());
        if (dto.getNumberOfLines().intValue() == 1 &&
        	order.getOrderLines().size() >= 1) {
        	// This event needs to LOG the old item id and description, so
        	// it can only happen when updating orders with only one line.
        	
        	for (Iterator i = order.getOrderLines().iterator(); i.hasNext();) {
        		// Check which order is not deleted.
        		OrderLineEntityLocal temp = (OrderLineEntityLocal)i.next();
        		if (temp.getDeleted() == 0) {
        			oldLine = temp;
        			nonDeletedLines++;
        		}
        	}
        }
        
        // now update this order's lines
        // first, mark all the lines as deleted
        for (Iterator it = order.getOrderLines().iterator(); it.hasNext();) {
            OrderLineEntityLocal line = (OrderLineEntityLocal) it.next();
            line.setDeleted(new Integer(1));
        }  
        createLines(dto);
 
        if (oldLine != null && nonDeletedLines == 1) {
    		OrderLineEntityLocal newLine = null;
    		for (Iterator i = order.getOrderLines().iterator(); i.hasNext();) {
    			OrderLineEntityLocal temp = (OrderLineEntityLocal)i.next();
    			if (temp.getDeleted() == 0) {
    				newLine = temp;
    			}
    		}
    	   	if (newLine != null && !oldLine.getItemId().equals(newLine.getItemId())) {
                if (executorId != null) {
        	   		eLogger.audit(executorId, 
        	   				Constants.TABLE_ORDER_LINE,
        	   				newLine.getId(), EventLogger.MODULE_ORDER_MAINTENANCE,
        	   				EventLogger.ORDER_LINE_UPDATED, oldLine.getId(), 
        	   				oldLine.getDescription(),
        	   				null);
                } else {
                    // it is the mediation process
                    eLogger.auditBySystem(order.getUser().getEntity().getId(), 
                            Constants.TABLE_ORDER_LINE,
                            newLine.getId(), EventLogger.MODULE_ORDER_MAINTENANCE,
                            EventLogger.ORDER_LINE_UPDATED, oldLine.getId(), 
                            oldLine.getDescription(),
                            null);
                }
    	   	}
    	}

        if (executorId != null) {
            eLogger.audit(executorId, Constants.TABLE_PUCHASE_ORDER, 
                    order.getId(),
                    EventLogger.MODULE_ORDER_MAINTENANCE, 
                    EventLogger.ROW_UPDATED, null,  
                    null, null);
        } else {
            eLogger.auditBySystem(order.getUser().getEntity().getId(), 
                    Constants.TABLE_PUCHASE_ORDER, 
                    order.getId(),
                    EventLogger.MODULE_ORDER_MAINTENANCE, 
                    EventLogger.ROW_UPDATED, null,  
                    null, null);
        }
        
    }
    
    private void updateNextBillableDay(Integer executorId, Date newDate) {
        if (newDate == null) return;
        // only if the new date is in the future
        if (order.getNextBillableDay() == null ||
                newDate.after(order.getNextBillableDay())) {
            // this audit can be added to the order details screen
            // otherwise the user can't account for the lost time
            eLogger.audit(executorId, Constants.TABLE_PUCHASE_ORDER, 
                    order.getId(),
                    EventLogger.MODULE_ORDER_MAINTENANCE, 
                    EventLogger.ORDER_NEXT_BILL_DATE_UPDATED, null,  
                    null, order.getNextBillableDay());
            // update the period of the latest invoice as well
            OrderProcessDTO process = null;
            if (order.getNextBillableDay() != null) {
            	process = orderDas.findProcessByEndDate(order.getId(), 
            		order.getNextBillableDay());
            }
            if (process != null) {
            	LOG.debug("Updating process id " + process.getId());
            	process.setPeriodEnd(newDate);
            	
            } else {
            	LOG.debug("Did not find any process for order " + order.getId() +  
            			" and date " + order.getNextBillableDay());
            }

            // do the actual update
            order.setNextBillableDay(newDate);
        } else {
            LOG.info("order " + order.getId() + 
                    " next billable day not updated from " + 
                    order.getNextBillableDay() + " to " + newDate);
        }
    }
    
    private void createLines(NewOrderDTO orderDto) 
            throws FinderException, CreateException {
        Collection orderLines = order.getOrderLines();

        /*
         * now go over the order lines
         */
        for (OrderLineDTOEx line : orderDto.getRawOrderLines()) {
            // get the type id bean for the relationship
            OrderLineTypeEntityLocal lineType =
                orderLineTypeHome.findByPrimaryKey(line.getTypeId());

            // first, create the line record
            OrderLineEntityLocal newOrderLine =
                orderLineHome.create(
                    line.getItemId(),
                    lineType,
                    line.getDescription(),
                    line.getAmount(),
                    line.getQuantity(),
                    line.getPrice(),
                    line.getItemPrice(),
                    new Integer(0));
            // then update the order fk column
            orderLines.add(newOrderLine);
        }
    }       
    
    private void updatePromotion(Integer entityId, String code) 
            throws NamingException {
        if (code != null && code.length() > 0) {
            PromotionBL promotion = new PromotionBL();
            if (promotion.isPresent(entityId, code)) {
                promotion.getEntity().getUsers().add(order.getUser());
            } else {
                LOG.error("Can't find promotion entity = " + entityId +
                        " code " + code);
            }
        }
    } 
    
    /**
     * Method lookUpEditable.
     * Gets the row from order_line_type for the type specifed
     * @param type 
     * The order line type to look.
     * @return Boolean
     * If it is editable or not
     * @throws SessionInternalError
     * If there was a problem accessing the entity bean
     */
    static public Boolean lookUpEditable(Integer type)
        throws SessionInternalError {
        Boolean editable = null;
        Logger LOG = Logger.getLogger(OrderBL.class);

        try {

            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            OrderLineTypeEntityLocalHome orderLineTypeHome =
                (OrderLineTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
                    OrderLineTypeEntityLocalHome.class,
                    OrderLineTypeEntityLocalHome.JNDI_NAME);

            OrderLineTypeEntityLocal typeBean =
                orderLineTypeHome.findByPrimaryKey(type);
            editable = new Boolean(typeBean.getEditable().intValue() == 1);
        } catch (Exception e) {
            LOG.fatal(
                "Exception looking up the editable flag of an order "
                    + "line type. Type = "
                    + type,
                e);
            throw new SessionInternalError("Looking up editable flag");
        }

        return editable;
    }

    public CachedRowSet getList(Integer entityID, Integer userRole, 
            Integer userId) 
            throws SQLException, Exception{
                
        if(userRole.equals(Constants.TYPE_INTERNAL) ||
                userRole.equals(Constants.TYPE_ROOT) ||
                userRole.equals(Constants.TYPE_CLERK)) {
            prepareStatement(OrderSQL.listInternal);
            cachedResults.setInt(1,entityID.intValue());
        } else if(userRole.equals(Constants.TYPE_PARTNER)) {
            prepareStatement(OrderSQL.listPartner);
            cachedResults.setInt(1, entityID.intValue());
            cachedResults.setInt(2, userId.intValue());
        } else if(userRole.equals(Constants.TYPE_CUSTOMER)) {
            prepareStatement(OrderSQL.listCustomer);
            cachedResults.setInt(1, userId.intValue());
        } else {
            throw new Exception("The orders list for the type " + userRole + 
                    " is not supported");
        }
        
        execute();
        conn.close();
        return cachedResults;
    }
    
    public Integer getLatest(Integer userId) 
            throws SessionInternalError {
        Integer retValue = null;
        try {
            prepareStatement(OrderSQL.getLatest);
            cachedResults.setInt(1, userId.intValue());
            execute();
            if (cachedResults.next()) {
                int value = cachedResults.getInt(1);
                if (!cachedResults.wasNull()) {
                    retValue = new Integer(value);
                }
            } 
            cachedResults.close();
            conn.close();
        } catch (Exception e) {
            throw new SessionInternalError(e);
        } 
        
        return retValue;
    }


    public CachedRowSet getOrdersByProcessId(Integer processId)
            throws SQLException, Exception{

        prepareStatement(OrderSQL.listByProcess);
        cachedResults.setInt(1,processId.intValue());
        execute();
        conn.close();
        return cachedResults;
    }
    
    public void setStatus(Integer executorId, Integer statusId) {
        if (statusId == null || order.getStatusId().equals(statusId)) {
            return;
        }
        if (executorId != null) {
            eLogger.audit(executorId, Constants.TABLE_PUCHASE_ORDER, 
                    order.getId(), 
                    EventLogger.MODULE_ORDER_MAINTENANCE, 
                    EventLogger.ORDER_STATUS_CHANGE, 
                    order.getStatusId(), null, null);
        } else {
            eLogger.auditBySystem(order.getUser().getEntity().getId(), 
                    Constants.TABLE_PUCHASE_ORDER, 
                    order.getId(), 
                    EventLogger.MODULE_ORDER_MAINTENANCE, 
                    EventLogger.ORDER_STATUS_CHANGE, 
                    order.getStatusId(), null, null);

        }
        NewStatusEvent event = new NewStatusEvent(
                order.getId(), order.getStatusId(), statusId);
        EventManager.process(event);
        order.setStatusId(statusId);

    }
    
    /**
     * To be called from the http api, this simply looks for lines
     * in the order that lack some fields, it finds that info based
     * in the item. 
     * @param dto
     */
    public void fillInLines(NewOrderDTO dto, Integer entityId) 
            throws NamingException, FinderException, SessionInternalError {
        /*
         * now go over the order lines
         */
        Hashtable lines = dto.getOrderLinesMap();
        Collection values = lines.values();
        ItemBL itemBl = new ItemBL();
        for (Iterator i = values.iterator(); i.hasNext();) {
            OrderLineDTOEx line = (OrderLineDTOEx) i.next();
            itemBl.set(line.getItemId());
            Integer languageId = itemBl.getEntity().getEntity().
                    getLanguageId();
            // this is needed for the basic pluggable task to work
            line.setItem(itemBl.getDTO(languageId, dto.getUserId(), 
                    entityId, dto.getCurrencyId()));
            if (line.getPrice() == null) {
                line.setPrice(itemBl.getPrice(dto.getUserId(), 
                        dto.getCurrencyId(), entityId));
            }
            if (line.getDescription() == null) {
                line.setDescription(itemBl.getEntity().getDescription(
                        languageId));
            }
        }
     }
    
    private void audit(Integer executorId, Date date) {
        eLogger.audit(executorId, Constants.TABLE_PUCHASE_ORDER, 
                order.getId(),
                EventLogger.MODULE_ORDER_MAINTENANCE, 
                EventLogger.ROW_UPDATED, null,  
                null, date);
    }        
    private void audit(Integer executorId, Integer in) {
        eLogger.audit(executorId, Constants.TABLE_PUCHASE_ORDER, 
                order.getId(),
                EventLogger.MODULE_ORDER_MAINTENANCE, 
                EventLogger.ROW_UPDATED, in,  
                null, null);
    }        

    public static boolean validate(OrderWS dto) {
        boolean retValue = true;
        
        if (dto.getUserId() == null || dto.getPeriod() == null ||
                dto.getBillingTypeId() == null || 
                dto.getOrderLines() == null) {
            retValue = false;
        } else {
            for (int f = 0 ; f < dto.getOrderLines().length; f++) {
                if (!validate(dto.getOrderLines()[f])) {
                    retValue = false;
                    break;
                }
            }
        }
        return retValue;
    }
    
    public static boolean validate(OrderLineWS dto) {
        boolean retValue = true;
        
        if (dto.getTypeId() == null || dto.getAmount() == null || 
                dto.getDescription() == null || dto.getQuantity() == null) {
            retValue = false;
        }
        
        return retValue;
    }
    
    public void reviewNotifications(Date today) 
    		throws NamingException, FinderException, SQLException, Exception  {
        NotificationSessionLocalHome notificationHome =
            (NotificationSessionLocalHome) EJBFactory.lookUpLocalHome(
            NotificationSessionLocalHome.class,
            NotificationSessionLocalHome.JNDI_NAME);

        NotificationSessionLocal notificationSess = 
            	notificationHome.create();

    	EntityBL entity = new EntityBL();
    	Collection entities = entity.getHome().findEntities();
    	for (Iterator it = entities.iterator(); it.hasNext(); ) {
    		EntityEntityLocal ent = (EntityEntityLocal) it.next();
    		// find the orders for this entity
    	    prepareStatement(OrderSQL.getAboutToExpire);
    	    
    	    cachedResults.setDate(1, new java.sql.Date(today.getTime()));
    	    // calculate the until date
    	    
    	    // get the this entity preferences for each of the steps
    	    PreferenceBL pref = new PreferenceBL();
            int totalSteps = 3;
            int stepDays[] = new int[totalSteps];
            boolean config = false;
            int minStep = -1;
            for (int f = 0; f < totalSteps; f++) {
        	    try {
        	    	pref.set(ent.getId(), new Integer(
                            Constants.PREFERENCE_DAYS_ORDER_NOTIFICATION_S1.intValue() +
                            f));
                    if (pref.isNull()) {
                        stepDays[f] = -1;
                    } else {
                    	stepDays[f] = pref.getInt();
                        config = true;
                        if (minStep == -1) {
                        	minStep = f;
                        }
                    }
        	    } catch (FinderException e) {
                    stepDays[f] = -1;
        	    }
            }
            
            if (!config) {
                LOG.warn("Preference missing to send a notification for " +
                        "entity " + ent.getId());
                continue;
            }

    	    Calendar cal = Calendar.getInstance();
    	    cal.clear();
    	    cal.setTime(today);
    	    cal.add(Calendar.DAY_OF_MONTH, stepDays[minStep]);
    	    cachedResults.setDate(2, new java.sql.Date(
    	    		cal.getTime().getTime()));
    	    
    	    // the entity
    	    cachedResults.setInt(3, ent.getId().intValue());
            // the total number of steps
            cachedResults.setInt(4, totalSteps);
    	            
    	    execute();
    	    while (cachedResults.next()) {
		    	int orderId = cachedResults.getInt(1);
                Date activeUntil = cachedResults.getDate(2);
                int currentStep = cachedResults.getInt(3);
                int days = -1;

                // find out how many days apply for this order step
                for (int f = currentStep; f < totalSteps; f++) {
                    if (stepDays[f] >= 0) {
                        days = stepDays[f];
                        currentStep = f + 1;
                        break;
                    }
                }
                
                if (days == -1) {
                	throw new SessionInternalError("There are no more steps " +
                            "configured, but the order was selected. Order " +
                            " id = " + orderId);
                }
                
                // check that this order requires a notification
                cal.setTime(today);
                cal.add(Calendar.DAY_OF_MONTH, days);
                if (activeUntil.compareTo(today) >= 0 &&
                        activeUntil.compareTo(cal.getTime()) <= 0) {
                	/*/ ok
                    LOG.debug("Selecting order " + orderId + " today = " + 
                            today + " active unitl = " + activeUntil + 
                            " days = " + days);
                    */
                } else {
                    /*
                    LOG.debug("Skipping order " + orderId + " today = " + 
                            today + " active unitl = " + activeUntil + 
                            " days = " + days);
                            */
                	continue;
                }

		    	set(new Integer(orderId));
		    	UserBL user = new UserBL(order.getUser());
		    	try {
		    		NotificationBL notification = new NotificationBL();
                    ContactBL contact = new ContactBL();
                    contact.set(user.getEntity().getUserId());
                    OrderDTOEx dto = DTOFactory.getOrderDTOEx(
                            new Integer(orderId), new Integer(1));
		    		MessageDTO message = notification.getOrderNotification(
		    				ent.getId(), 
                            new Integer(currentStep), 
		    				user.getEntity().getLanguageIdField(), 
							order.getActiveSince(),
							order.getActiveUntil(),
                            user.getEntity().getUserId(),
                            dto.getTotal(), order.getCurrencyId());
                    // update the order record only if the message is sent 
                    if (notificationSess.notify(user.getEntity(), message).
                            booleanValue()) {
    		            // if in the last step, turn the notification off, so
                        // it is skiped in the next process
                        if (currentStep >= totalSteps) {
                        	order.setNotify(new Integer(0));
                        }
                        order.setNotificationStep(new Integer(currentStep));
    		            order.setLastNotified(Calendar.getInstance().getTime());
                    }

		    	} catch (NotificationNotFoundException e) {
		    		LOG.warn("Without a message to send, this entity can't" +
		    				" notify about orders. Skipping");
		    		break;
		    	}
		    	
		    } 
            cachedResults.close();
    	}
        // The connection was found null when testing on Oracle
        if (conn != null) {
            conn.close();
        }
    }
    
    public TimePeriod getDueDate() 
            throws NamingException, FinderException {
        TimePeriod retValue = new TimePeriod();
        if (order.getDueDateValue() == null) {
            // let's go see the customer
            if (order.getUser().getCustomer().getDueDateValue() == null) {
                // still unset, let's go to the entity
                ConfigurationBL config = new ConfigurationBL(
                        order.getUser().getEntity().getId());
                retValue.setUnitId(config.getEntity().getDueDateUnitId());
                retValue.setValue(config.getEntity().getDueDateValue());
            } else {
                retValue.setUnitId(order.getUser().getCustomer().
                        getDueDateUnitId());
                retValue.setValue(order.getUser().getCustomer().getDueDateValue());
            }
        } else {
            retValue.setUnitId(order.getDueDateUnitId());
            retValue.setValue(order.getDueDateValue());
        }
        
        // df fm only applies if the entity uses it
        PreferenceBL preference = new PreferenceBL();
        try {
            preference.set(order.getUser().getEntity().getId(), 
                    Constants.PREFERENCE_USE_DF_FM);
        } catch (FinderException e) {
            // no problem go ahead use the defualts
        }
        if (preference.getInt() == 1) {
            // now all over again for the Df Fm
            if (order.getDfFm() == null) {
                // let's go see the customer
                if (order.getUser().getCustomer().getDfFm() == null) {
                    // still unset, let's go to the entity
                    ConfigurationBL config = new ConfigurationBL(
                            order.getUser().getEntity().getId());
                    retValue.setDf_fm(config.getEntity().getDfFm());
                } else {
                    retValue.setDf_fm(order.getUser().getCustomer().getDfFm());
                }
            } else {
                retValue.setDf_fm(order.getDfFm());
            }
        } else {
            retValue.setDf_fm((Boolean) null);
        }
        
        retValue.setOwn_invoice(order.getOwnInvoice());
        
        return retValue;
    }
    
    public Date getInvoicingDate() {
        Date retValue;;
        if (order.getNextBillableDay() != null) {
            retValue = order.getNextBillableDay();
        } else {
            if (order.getActiveSince() != null) {
                retValue = order.getActiveSince();
            } else {
                retValue = order.getCreateDate();
            }
        }
            
        return retValue;
    }
    
    public Integer[] getListIds(Integer userId, Integer number, 
            Integer entityId) 
            throws Exception {
        // use the list of orders as if this was a customer asking
        CachedRowSet result = getList(entityId, Constants.TYPE_CUSTOMER, userId);
        Vector<Integer> allRows = new Vector<Integer>();
        while (result.next()) {
            allRows.add(new Integer(result.getInt(1)));
        }
        result.close();
        
        // now convert to an array that is no bigger than the expected
        Integer retValue[] = new Integer[allRows.size() > 
                                         number.intValue() ? number.intValue() :
                                             allRows.size()];
        for (int f = 0; f < allRows.size() && f < number.intValue(); 
                f++) {
            retValue[f] = allRows.get(f);
        }
        
        return retValue;
    }
    
    public Integer[] getByUserAndPeriod(Integer userId, Integer statusId) 
            throws SessionInternalError {
        // find the order records first
        try {
            Vector result = new Vector();
            prepareStatement(OrderSQL.getByUserAndPeriod);
            cachedResults.setInt(1, userId.intValue());
            cachedResults.setInt(2, statusId.intValue());
            execute();
            while (cachedResults.next()) {
                result.add(new Integer(cachedResults.getInt(1)));
            } 
            cachedResults.close();
            conn.close();
            // now convert the vector to an int array
            Integer retValue[] = new Integer[result.size()];
            result.toArray(retValue);

            return retValue;
        } catch (Exception e) {
            throw new SessionInternalError(e);
        } 
    }
    
    public Collection<OrderEntityLocal> getActiveRecurringByUser(Integer userId) 
            throws FinderException {
        return orderHome.findByUserSubscriptions(userId);
    }

    public OrderPeriodDTOEx[] getPeriods(Integer entityId, Integer languageId) {
        OrderPeriodDTOEx retValue[] = null;
        try {
            Collection periods = orderPeriodHome.findByEntity(entityId);
            retValue = new OrderPeriodDTOEx[periods.size()];
            int f = 0;
            for (Iterator it = periods.iterator(); it.hasNext(); f++) {
                OrderPeriodEntityLocal period = 
                    (OrderPeriodEntityLocal) it.next();
                retValue[f] = new OrderPeriodDTOEx();
                retValue[f].setId(period.getId());
                retValue[f].setEntityId(period.getEntityId());
                retValue[f].setUnitId(period.getUnitId());
                retValue[f].setValue(period.getValue());
                retValue[f].setDescription(period.getDescription(languageId));
            }
        } catch (FinderException e) {
            retValue = new OrderPeriodDTOEx[0];
        }
        
        return retValue;
    }
    
    public void updatePeriods(Integer languageId, OrderPeriodDTOEx periods[]) 
            throws FinderException {
        for (int f = 0; f < periods.length; f++) {
            OrderPeriodEntityLocal period = orderPeriodHome.findByPrimaryKey(
                    periods[f].getId());
            period.setUnitId(periods[f].getUnitId());
            period.setValue(periods[f].getValue());
            period.setDescription(periods[f].getDescription(), languageId);
        }
    }
    
    public void addPeriod(Integer entitytId, Integer languageId) 
            throws CreateException {
        orderPeriodHome.create(entitytId, new Integer(1), new Integer(1), " ",
                languageId);
    }
    
    public boolean deletePeriod(Integer periodId) 
            throws FinderException, RemoveException{
        OrderPeriodEntityLocal period = orderPeriodHome.findByPrimaryKey(
                periodId);
        if (period.getOrders().size() > 0) {
            return false;
        } else {
            period.remove();
            return true;
        }
    }
    
    public OrderLineWS getOrderLineWS(Integer id) 
            throws FinderException {
        OrderLineEntityLocal line = orderLineHome.findByPrimaryKey(id);
        OrderLineWS retValue = new OrderLineWS(line.getId(),line.getItemId(), 
                line.getDescription(), line.getAmount(), line.getQuantity(), 
                line.getPrice(), line.getItemPrice(), line.getCreateDate(), 
                line.getDeleted(), line.getType().getId(), 
                new Boolean(line.getEditable()));
        return retValue;
    }
    
    public OrderLineEntityLocal getOrderLine(Integer id)
            throws FinderException {
        return orderLineHome.findByPrimaryKey(id);
    }
    
    public void updateOrderLine(OrderLineWS dto) 
            throws FinderException, RemoveException {
        OrderLineEntityLocal line = orderLineHome.findByPrimaryKey(dto.getId());
        if (dto.getQuantity() != null && dto.getQuantity().intValue() == 0) {
            // deletes the order line if the quantity is 0
            line.remove();
            
        } else {
            line.setAmount(dto.getAmount());
            line.setDeleted(dto.getDeleted());
            line.setDescription(dto.getDescription());
            line.setItemId(dto.getItemId());
            line.setItemPrice(dto.getItemPrice());
            line.setPrice(dto.getPrice());
            line.setQuantity(dto.getQuantity());
        }
    }
    
    public void updateCurrent(Integer entityId, Integer executorId, Integer userId, Integer currencyId, 
            Vector<OrderLineDTOEx> lines, Vector<Record> records, Date eventDate) {
        
        try {
            NewOrderDTO currentOrder;
            setUserCurrent(userId);
            UserBL user = new UserBL(userId);
            Integer language = user.getEntity().getLanguageIdField();
            CurrentOrder co = new CurrentOrder(userId, eventDate);
            
            Integer currentOrderId = co.getCurrent();
            if (currentOrderId == null) {
                // this is almost an error, put them in a new order?
                currentOrderId = co.create(eventDate, currencyId, entityId);
                LOG.warn("Created current one-time order without a suitable main " +
                        "subscription order:" + currentOrderId);
            }
            // update an existing order
            set(currentOrderId);
            currentOrder = new NewOrderDTO(getDTO(), order);
            setDTO(currentOrder);
            for (OrderLineDTOEx line: lines) {
                addItem(line.getItemId(), line.getQuantity(), language, userId, 
                        entityId, currencyId, records);
            }
            update(executorId, currentOrder);
        } catch (FinderException e) {
            throw new SessionInternalError("Updating current order", OrderBL.class, e);
        } catch (CreateException e) {
            throw new SessionInternalError("Updating current order", OrderBL.class, e);
        }
    }
    
    /**
     * @param userId
     * @param eventDate
     * @return
     */
    private Integer getCurrent(Integer userId, Date eventDate) {
        return null;
    }
    
    /**
     * The order has to be set and made persitent with an ID
     * @param isNew
     */
    private void setMainSubscription(Integer executorId) {
        // set the field
        order.setIsCurrent(1);
        // there can only be one main subscription order
        Integer oldCurrent = order.getUser().getCustomer().getCurrentOrderId();
        if (oldCurrent == null || !oldCurrent.equals(order.getId())) {
            eLogger.audit(executorId, Constants.TABLE_PUCHASE_ORDER, 
                    order.getId(),
                    EventLogger.MODULE_ORDER_MAINTENANCE, 
                    EventLogger.ORDER_MAIN_SUBSCRIPTION_UPDATED, oldCurrent,  
                    null, null);
            // this is the new subscription order
            order.getUser().getCustomer().setCurrentOrderId(order.getId());
            // if there was an old one
            if (oldCurrent != null) {
                // update so it does not have the 'is subscription' flag on
                try {
                    OrderEntityLocal old = orderHome.findByPrimaryKey(oldCurrent);
                    old.setIsCurrent(new Integer(0));
                } catch (FinderException e) {
                    LOG.error("Can not find order to set 'is_current' off " + oldCurrent);
                }
            }
        }
    }
    
    private void unsetMainSubscription(Integer executorId) {
        if (order.getIsCurrent() == null) {
            order.setIsCurrent(0);
        }
        // it is removing the main subscription
        if (order.getIsCurrent().intValue() == 1) {
            eLogger.audit(executorId, Constants.TABLE_PUCHASE_ORDER, 
                    order.getId(),
                    EventLogger.MODULE_ORDER_MAINTENANCE, 
                    EventLogger.ORDER_MAIN_SUBSCRIPTION_UPDATED, null,  
                    null, null);
            // this is the new subscription order
            order.getUser().getCustomer().setCurrentOrderId(null);
            order.setIsCurrent(0);
        }
    }
    
    public CachedRowSet getOneTimersByDate(Integer userId, Date activeSince) 
            throws SQLException, Exception{
                
        prepareStatement(OrderSQL.getCurrent);
        cachedResults.setInt(1,userId.intValue());
        cachedResults.setDate(2,new java.sql.Date(activeSince.getTime()));
        
        execute();
        conn.close();
        return cachedResults;
    }

}
