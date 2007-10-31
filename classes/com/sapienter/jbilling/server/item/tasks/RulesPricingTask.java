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
            rulesMemoryContext.add(fields);
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
