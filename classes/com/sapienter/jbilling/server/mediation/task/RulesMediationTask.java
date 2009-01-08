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
package com.sapienter.jbilling.server.mediation.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.drools.RuleBase;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.user.UserBL;
import com.sapienter.jbilling.server.user.db.UserDAS;

public class RulesMediationTask extends PluggableTask implements
        IMediationProcess {

    private Vector<Record> records = null;
    private ProcessManager manager = null;
    private static final Logger LOG = Logger.getLogger(RulesMediationTask.class);
    
    public Integer getUserId() {
        return manager.getUserId();
    }
    
    public Integer getCurrencyId() {
        return manager.getCurrencyId();
    }
    
    public Date getEventDate() {
        return manager.getEventDate();
    }
    
    public String getDescription() {
        return manager.getDescription();
    }
    
    public Vector<OrderLineDTO> process(Vector<Record> records, String configurationName) 
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
        private Vector <OrderLineDTO> lines = null;
        private Integer userId = null;
        private Integer currencyId = null;
        private final String configurationName;
        private Date eventDate = null;
        private String description = null;

        
        public ProcessManager(String configurationName) {
            this.configurationName = configurationName;
            lines = new Vector<OrderLineDTO>();
        }
        
        public String getConfigurationName() {
            return configurationName;
        }
        
        public int getTotalRecords() {
            return records.size();
        }
        
        public Vector<OrderLineDTO> getLines() {
            return lines;
        }
        
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        
        public void addLine(Integer itemId, Integer quantity) {
        	addLine(itemId, new Double(quantity));
        }
        
        public void addLine(Integer itemId, Double quantity) {
            OrderLineDTO line =  new OrderLineDTO();
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
        	UserDAS das = new UserDAS();
        	if (das.findNow(userId) != null) {
        		this.userId = userId;
        	} else {
        		throw new TaskException("User id " + userId + " does not exist");
        	}
        }
        
        public void setCurrencyId(Integer currencyId) {
            this.currencyId = currencyId;
        }

        public void setEventDate(Date date) {
            eventDate = date;
        }

        public void setEventDate(String date, String format) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            try {
                eventDate = dateFormat.parse(date);
            } catch (ParseException e) {
                eventDate = null;
                LOG.warn("Exception parsing a string date to set the event date", e);
            }
        }
        
        public Date getEventDate() {
            return eventDate;
        }
    }
}
