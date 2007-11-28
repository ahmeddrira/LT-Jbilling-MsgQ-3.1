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
package com.sapienter.jbilling.server.item.tasks;

import java.math.BigDecimal;
import java.util.Vector;

import org.drools.RuleBase;
import org.drools.StatelessSession;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.util.DTOFactory;

public class RulesPricingTask extends PluggableTask implements IPricing {
    
   // private static final Logger LOG = Logger.getLogger(RulesPricingTask.class);

    public Float getPrice(Integer itemId, Integer userId, Integer currencyId,
            Vector<PricingField> fields, Float defaultPrice) 
            throws TaskException {
        // now we have the line with good defaults, the order and the item
        // These have to be visible to the rules
        RuleBase ruleBase;
        try {
            ruleBase = readRule();
        } catch (Exception e) {
            throw new TaskException(e);
        }
        StatelessSession session = ruleBase.newStatelessSession();
        Vector<Object> rulesMemoryContext = new Vector<Object>();
        
        PricingManager manager = new PricingManager(itemId, userId, currencyId, defaultPrice);
        session.setGlobal("manager", manager);
        
        if (fields != null && !fields.isEmpty()) {
            rulesMemoryContext.addAll(fields);
        }

        try {
            if (userId != null) {
                UserDTOEx user = DTOFactory.getUserDTOEx(userId); 
                rulesMemoryContext.add(user);
                ContactBL contact = new ContactBL();
                contact.set(userId);
                rulesMemoryContext.add(contact.getDTO());
            }
            rulesMemoryContext.add(manager);
        } catch (Exception e) {
            throw new TaskException(e);
        }
        // then execute the rules
        session.executeWithResults(rulesMemoryContext);

        return new Float(manager.getPrice());
    }

    public static class PricingManager {
        private final Integer itemId;
        private final Integer userId;
        private final Integer currencyId;
        private BigDecimal price; // it is all about setting the value of this field ...
        
        public PricingManager(Integer itemId, Integer userId, 
                Integer currencyId, Float price) {
            this.itemId = itemId;
            this.userId = userId;
            this.currencyId = currencyId;
            setPrice(price);
        }
        
        public double getPrice() {
            return price.doubleValue();
        }
        
        public void setPrice(double defaultPrice) {
            this.price = new BigDecimal(defaultPrice);
        }
        
        public void setPrice(int price) {
            setPrice((double) price);
        }
        
        public void setByPercentage(double percentage) {
            this.price = price.add(price.divide(new BigDecimal(100), Constants.BIGDECIMAL_SCALE, 
                    Constants.BIGDECIMAL_ROUND).multiply(new BigDecimal(percentage)));
        }
        
        public void setByPercentage(int percentage) {
            setByPercentage((double) percentage);
        }
        
        public Integer getCurrencyId() {
            return currencyId;
        }
        public Integer getItemId() {
            return itemId;
        }
        public Integer getUserId() {
            return userId;
        }
        
    }
}
