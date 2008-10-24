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

package com.sapienter.jbilling.server.item.tasks;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.drools.FactHandle;
import org.drools.RuleBase;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.order.OrderBL;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.ContactDTOEx;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.contact.db.ContactFieldDTO;
import com.sapienter.jbilling.server.util.DTOFactory;

public class RulesItemManager extends BasicItemManager {

    private static final Logger LOG = Logger.getLogger(RulesItemManager.class);

    protected OrderManager helperOrder = null;
    private Vector<Record> records;
    
    public void addItem(Integer itemID, Double quantity, Integer language,
            Integer userId, Integer entityId, Integer currencyId,
            OrderDTO order, Vector<Record> records) throws TaskException {
        super.addItem(itemID, quantity, language, userId, entityId, currencyId, 
                order, records);
        this.records = records;
        helperOrder = new OrderManager(order, language, userId, entityId, currencyId);
        processRules(order);
    }

    protected void processRules(OrderDTO newOrder) throws TaskException {
        // now we have the line with good defaults, the order and the item
        // These have to be visible to the rules
        RuleBase ruleBase;
        try {
            ruleBase = readRule();
        } catch (Exception e) {
            throw new TaskException(e);
        }
        session = ruleBase.newStatefulSession();
        Vector<Object> rulesMemoryContext = new Vector<Object>();
        rulesMemoryContext.add(helperOrder);
        for (OrderLineDTO line: newOrder.getLines()) {
            if (line.getItem() != null) {
            	ItemBL item = new ItemBL(line.getItemId());
            	rulesMemoryContext.add(item.getDTO(helperOrder.getLanguage(), helperOrder.getUserId(), 
            			helperOrder.getEntityId(), helperOrder.getCurrencyId()));
            }
            rulesMemoryContext.add(line);
        }

        if (newOrder.getPricingFields() != null && newOrder.getPricingFields().size() > 0) {
        	for (PricingField pf : newOrder.getPricingFields()) {
        		rulesMemoryContext.add(pf);
        	}
        }
        try {
            Integer userId = newOrder.getBaseUserByUserId().getId();
            UserDTOEx user = DTOFactory.getUserDTOEx(userId); 
            rulesMemoryContext.add(user);
            ContactBL contact = new ContactBL();
            contact.set(userId);
            ContactDTOEx contactDTO = contact.getDTO();
            rulesMemoryContext.add(contactDTO);
            for (ContactFieldDTO field: (Collection<ContactFieldDTO>) contactDTO.getFieldsTable().values()) {
                rulesMemoryContext.add(field);    
            }

            
            // Add the subscriptions
            OrderBL order = new OrderBL();
            for(OrderDTO myOrder: order.getActiveRecurringByUser(userId)) {
                for (OrderLineDTO myLine: myOrder.getLines()) {
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
   
    public class OrderManager {
        private OrderDTO order = null;
        private Integer language = null;
        private Integer userId = null;
        private Integer entityId = null;
        private Integer currencyId = null;
        
        public OrderManager(OrderDTO order, Integer language,
                Integer userId, Integer entityId, Integer currencyId) {
            this.order = order;
            this.language = language;
            this.userId = userId;
            this.entityId = entityId;
            this.currencyId = currencyId;
        }

        public OrderDTO getOrder() {
            return order;
        }

        public void setOrder(OrderDTO order) {
            this.order = order;
        }
        
        public OrderLineDTO addItem( Integer itemID, Integer quantity)
            	throws TaskException {
        	return addItem( itemID, new Double(quantity) );
        }
        
        public OrderLineDTO addItem(Integer itemID, Double quantity) 
                throws TaskException {
        	LOG.debug("Adding item " + itemID + " q: " + quantity);
        			
            BasicItemManager helper = new BasicItemManager();
            OrderLineDTO oldLine = order.getLine(itemID);
            FactHandle h = null;
            if (oldLine != null) { 
            	h = handlers.get(oldLine);
            }
            helper.addItem(itemID, quantity, language, userId, entityId, currencyId, order, records);
            OrderLineDTO retValue =  helper.getLatestLine();
            if (h != null) {
            	LOG.debug("updating");
            	session.update(h, retValue);
            } else {
            	LOG.debug("inserting");
            	handlers.put(retValue, session.insert(retValue));
            }
            
            LOG.debug("Now order line is " + retValue + " hash: " + retValue.hashCode());
	        return retValue;
        }
        
        public OrderLineDTO addItem(Integer itemId) throws TaskException {
            return addItem(itemId, 1.0);
        }
        
        public void removeItem(Integer itemId) {
            removeObject(order.getLine(itemId));
            order.removeLine(itemId);
        }
        
        /**
         * Adds or updates an order line. It will calculate the amount to add based on the
         * price of the itemBase, using the price as a percentage of the itemId
         * @param itemId
         * @param itemBase
         * @return
         * @throws TaskException
         */
        public OrderLineDTO percentageIncrease(Integer itemId, Integer itemBase) 
                throws TaskException {
            OrderLineDTO updateLine = order.getLine(itemId);
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

		public Integer getCurrencyId() {
			return currencyId;
		}

		public Integer getEntityId() {
			return entityId;
		}

		public Integer getLanguage() {
			return language;
		}

		public Integer getUserId() {
			return userId;
		}
    }
    
    public static class Subscription {
        private final Integer itemId;
        private final Integer periodId;
        private final Date activeSince;
        private final Date activeUntil;
        private final Double quantity;
        
        protected Subscription(OrderLineDTO line) {
            periodId = line.getPurchaseOrder().getOrderPeriod().getId();
            activeSince = line.getPurchaseOrder().getActiveSince();
            activeUntil = line.getPurchaseOrder().getActiveUntil();
            itemId = line.getItemId();
            quantity = line.getQuantity();
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

        public Double getQuantity() {
            return quantity;
        }
    }
}
