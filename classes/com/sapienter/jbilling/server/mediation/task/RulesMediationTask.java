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
package com.sapienter.jbilling.server.mediation.task;

import java.util.Vector;

import org.drools.RuleBase;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.order.OrderLineDTOEx;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.user.UserBL;

public class RulesMediationTask extends PluggableTask implements
        IMediationProcess {

    private Vector<Record> records = null;
    ProcessManager manager = null;
    
    
    public Integer getUserId() {
        return manager.getUserId();
    }
    
    public Integer getCurrencyId() {
        return manager.getCurrencyId();
    }
    
    public Vector<OrderLineDTOEx> process(Vector<Record> records, String configurationName) 
            throws TaskException {
        RuleBase ruleBase;
        try {
            ruleBase = readRule();
        } catch (Exception e) {
            throw new TaskException(e);
        }
        session = ruleBase.newStatefulSession();
        Vector<Object> rulesMemoryContext = new Vector<Object>();
        for (Record record: records) {
            for (PricingField field: record.getFields()) {
                rulesMemoryContext.add(field);
            }
        }
        
        this.records = records;
        
        manager = new ProcessManager(configurationName);
        session.setGlobal("mediationManager", manager);
        // the manager needs to be also an object in the working memory
        rulesMemoryContext.add(manager);
        // then execute the rules
        executeStatefulRules(session, rulesMemoryContext);
        
        return manager.getLines();
    }
    
    public class ProcessManager {
        private Vector <OrderLineDTOEx> lines = null;
        private Integer userId = null;
        private Integer currencyId = null;
        private final String configurationName;
        
        public ProcessManager(String configurationName) {
            this.configurationName = configurationName;
            lines = new Vector<OrderLineDTOEx>();
        }
        
        public String getConfigurationName() {
            return configurationName;
        }
        
        public int getTotalRecords() {
            return records.size();
        }
        
        public Vector<OrderLineDTOEx> getLines() {
            return lines;
        }
        
        public void addLine(Integer itemId, Integer quantity) {
            OrderLineDTOEx line =  new OrderLineDTOEx();
            line.setItemId(itemId);
            line.setQuantity(quantity);
            lines.add(line);
        }
        
        public Integer getUserId() {
            return userId;
        }
        
        public Integer getCurrencyId() {
            return currencyId;
        }
        
        public void setUserFromUsername(String username) 
                throws TaskException {
            try {
                UserBL user = new UserBL(username, getEntityId());
                userId = user.getEntity().getUserId();
            } catch (Exception e) {
                throw new TaskException(e);
            } 
        }

        public void setUserFromCustomField(Integer typeId, String value) throws TaskException {
            try {
                UserBL user = new UserBL();
                CachedRowSet set = user.getByCustomField(getEntityId(), typeId, value);
                if (set.next()) {
                    userId = set.getInt(1);
                    if (set.next()) {
                        throw new TaskException("Too many users found for type " + typeId + " value " + value);
                    }
                    set.close();
                } else {
                    throw new TaskException("User not found for type " + typeId + " value " + value);
                }
            } catch (Exception e) {
                throw new TaskException(e);
            }
        }
        
        public void setUserFromId(Integer userId) throws TaskException {
            try {
                UserBL user = new UserBL(userId);
                this.userId = userId;
            } catch (Exception e) {
                throw new TaskException(e);
            } 
        }
        
        public void setCurrencyId(Integer currencyId) {
            this.currencyId = currencyId;
        }

        public void updateField(PricingField toUpdate) throws TaskException {
            updateObject(toUpdate, toUpdate);
        }
    }
}
