/*
 * jBilling - The Enterprise Open Source Billing System
 * Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

 * This file is part of jbilling.

 * jbilling is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * jbilling is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sapienter.jbilling.server.mediation.task;

import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;
import com.sapienter.jbilling.server.rule.Result;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author emilc
 */
public class MediationResult extends Result {

    private static final Logger LOG = Logger.getLogger(MediationResult.class);

    // the lines that where 'created' by the mediation process
    private List<OrderLineDTO> lines = null;
    // the difference lines of the current orders, comparing the original lines
    private List<OrderLineDTO> diffLines = null;
    private OrderDTO currentOrder = null;
    private Integer userId = null;
    private Integer currencyId = null;
    private final String configurationName;
    private Date eventDate = null;
    private String description = null;

    public MediationResult(String configurationName) {
        this.configurationName = configurationName;
        lines = new ArrayList<OrderLineDTO>();
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public List<OrderLineDTO> getLines() {
        return lines;
    }

    public String getDescription() {
        return description;
    }

    public OrderDTO getCurrentOrder() {
        return currentOrder;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addLine(Integer itemId, Integer quantity) {
        addLine(itemId, new Double(quantity));
    }

    public void addLine(Integer itemId, Double quantity) {
        OrderLineDTO line = new OrderLineDTO();
        line.setItemId(itemId);
        line.setQuantity(quantity);
        line.setDefaults();
        lines.add(line);
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getCurrencyId() {
        return currencyId;
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

    public void setCurrentOrder(OrderDTO currentOrder) {
        this.currentOrder = currentOrder;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<OrderLineDTO> getDiffLines() {
        return diffLines;
    }

    public void setDiffLines(List<OrderLineDTO> diffLines) {
        this.diffLines = diffLines;
    }


    
}

