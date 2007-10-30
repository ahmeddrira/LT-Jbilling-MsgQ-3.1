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

package com.sapienter.jbilling.server.item.tasks;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.ejb.FinderException;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.StatelessSession;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.interfaces.OrderLineEntityLocal;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.order.NewOrderDTO;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.OrderLineDTOEx;
import com.sapienter.jbilling.server.pluggableTask.BasicLineTotalTask;
import com.sapienter.jbilling.server.pluggableTask.OrderProcessingTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.util.DTOFactory;

public class RulesItemManager extends BasicItemManager implements OrderProcessingTask {

    private OrderManager helperOrder = null;
    private Hashtable<Object,FactHandle> handlers = null;
    
    public void addItem(Integer itemID, Integer quantity, Integer language,
            Integer userId, Integer entityId, Integer currencyId,
            NewOrderDTO order) throws TaskException {
        super.addItem(itemID, quantity, language, userId, entityId, currencyId, order);
        helperOrder = new OrderManager(order, language, userId, entityId, currencyId);
        processRules(order);
    }

    private void processRules(NewOrderDTO newOrder) throws TaskException {
        // now we have the line with good defaults, the order and the item
        // These have to be visible to the rules
        RuleBase ruleBase;
        try {
            ruleBase = readRule();
        } catch (Exception e) {
            throw new TaskException(e);
        }
        StatefulSession session = ruleBase.newStatefulSession();
        Vector<Object> rulesMemoryContext = new Vector<Object>();
        for (OrderLineDTOEx line: newOrder.getOrderLinesMap().values()) {
            rulesMemoryContext.add(line.getItem());
            rulesMemoryContext.add(line);
        }
        try {
            Integer userId = newOrder.getUserId();
            UserDTOEx user = DTOFactory.getUserDTOEx(userId); 
            rulesMemoryContext.add(user);
            ContactBL contact = new ContactBL();
            contact.set(userId);
            rulesMemoryContext.add(contact.getDTO());
            
            // Add the subscriptions
            OrderBL order = new OrderBL();
            for(OrderEntityLocal myOrder: order.getActiveRecurringByUser(userId)) {
                for (OrderLineEntityLocal myLine: (Collection<OrderLineEntityLocal>)myOrder.getOrderLines()) {
                    rulesMemoryContext.add(new Subscription(myLine));
                }
            }
        } catch (Exception e) {
            throw new TaskException(e);
        }
        session.setGlobal("order", helperOrder);
        // then execute the rules
        
        
        executeStatefulRules(session, rulesMemoryContext);
    }
    
    private void executeStatelessRules(StatelessSession session, Vector context) {
        session.executeWithResults(context);
    }
    
    private void executeStatefulRules(StatefulSession session, Vector context) {
        handlers = new Hashtable<Object,FactHandle>();
        for (Object o: context) {
            handlers.put(o, session.insert(o));
        }
        helperOrder.setHandles(handlers);
        helperOrder.setSession(session);

        session.fireAllRules();
    }
    
    public void doProcessing(NewOrderDTO order) throws TaskException {
        try {
            UserBL user = new UserBL(order.getUserId());
            helperOrder = new OrderManager(order, user.getEntity().getLanguageIdField(), 
                    user.getEntity().getUserId(), user.getEntity().getEntity().getId(), 
                    user.getCurrencyId());
        } catch (FinderException e) {
            throw new TaskException(e);
        }
        processRules(order);
        
        // cant extend two classes
        BasicLineTotalTask parent = new BasicLineTotalTask();
        parent.doProcessing(order);
    }
    
    public static class OrderManager {
        private NewOrderDTO order = null;
        private Integer language = null;
        private Integer userId = null;
        private Integer entityId = null;
        private Integer currencyId = null;
        private StatefulSession session = null;
        private Hashtable<Object,FactHandle>  handles = null;
        
        public OrderManager(NewOrderDTO order, Integer language,
                Integer userId, Integer entityId, Integer currencyId) {
            this.order = order;
            this.language = language;
            this.userId = userId;
            this.entityId = entityId;
            this.currencyId = currencyId;
        }

        public NewOrderDTO getOrder() {
            return order;
        }

        public void setOrder(NewOrderDTO order) {
            this.order = order;
        }
        
        public OrderLineDTOEx addItem(Integer itemID, Integer quantity) 
                throws TaskException {
            BasicItemManager helper = new BasicItemManager();
            if (order.getOrderLine(itemID) != null) {
                session.retract(handles.get(order.getOrderLine(itemID)));
            }
            helper.addItem(itemID, quantity, language, userId, entityId, currencyId, order);
            OrderLineDTOEx retValue =  helper.getLatestLine();
            handles.put(retValue, session.insert(retValue));
            session.fireAllRules(); // might lead to infinite recurring loop
            return retValue;
        }
        
        /*
        public OrderLineDTOEx addItem(OrderLineDTOEx line) throws TaskException {
            BasicItemManager helper = new BasicItemManager();
            if (line.getItemId() == null) {
                throw new TaskException("The item id is mandatory to add an item to an order");
            }
            helper.populateOrderLine(language, userId, entityId, currencyId, line);
            order.setOrderLine(line.getItemId(), line);
            return line;
        }
        */
        
        public OrderLineDTOEx addItem(Integer itemId) throws TaskException {
            return addItem(itemId, 1);
        }
        
        public void removeItem(Integer itemId) {
            FactHandle h = handles.get(order.getOrderLine(itemId));
            if (h != null) {
                session.retract(h);
            }
            order.removeOrderLine(itemId);
        }
        
        /**
         * Adds or updates an order line. It will calculate the amount to add based on the
         * price of the itemBase, using the price as a percentage of the itemId
         * @param itemId
         * @param itemBase
         * @return
         * @throws TaskException
         */
        public OrderLineDTOEx percentageIncrease(Integer itemId, Integer itemBase) 
                throws TaskException {
            OrderLineDTOEx updateLine = order.getOrderLine(itemId);
            if (updateLine == null) {
                // no luck, create a new one
                updateLine = addItem(itemId);
                updateLine.setAmount(0.0F); // starts from 0
                updateLine.setTotalReadOnly(true);
            }
            
            // now add the amount based on the itemBase
            ItemBL item = new ItemBL(itemBase);
            ItemDTOEx itemDto = item.getDTO(language, userId, entityId, currencyId);
            BigDecimal percentage = new BigDecimal(updateLine.getItem().getPercentage().toString());
            BigDecimal base = new BigDecimal(itemDto.getPrice().toString());
            BigDecimal result = base.divide(new BigDecimal("100"), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUND).multiply(percentage).add(
                    new BigDecimal(updateLine.getAmount().toString()));
            updateLine.setAmount(result.floatValue());
            
            return updateLine;
        }

        public void setHandles(Hashtable<Object, FactHandle> handles) {
            this.handles = handles;
        }

        public void setSession(StatefulSession session) {
            this.session = session;
        }
    }
    
    public static class Subscription {
        private final Integer itemId;
        private final Integer periodId;
        private final Date activeSince;
        private final Date activeUntil;
        private final Integer quantity;
        
        protected Subscription(OrderLineEntityLocal line) {
            periodId = line.getOrder().getPeriod().getId();
            activeSince = line.getOrder().getActiveSince();
            activeUntil = line.getOrder().getActiveUntil();
            itemId = line.getItemId();
            quantity = line.getQuantity();
        }
        
        public void setItemId() {
            
        }

        public Date getActiveSince() {
            return activeSince;
        }

        public Date getActiveUntil() {
            return activeUntil;
        }

        public Integer getItemId() {
            return itemId;
        }

        public Integer getPeriodId() {
            return periodId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}
