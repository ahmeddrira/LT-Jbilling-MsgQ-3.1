/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.order;

import com.sapienter.jbilling.server.order.db.OrderLineDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Usage represents a single customers usage of an item, or an item type over a
 * set date range (usually aligned with the customer's billing period).
 * 
 * @author Brian Cowdery
 * @since 16-08-2010
 */
public class Usage {

    private Integer itemId;
    private Integer itemTypeId;
    private BigDecimal quantity;
    private BigDecimal amount;

    private Date startDate;
    private Date endDate;

    public Usage() {
    }

    public Usage(Integer itemId, Integer itemTypeId, BigDecimal quantity, BigDecimal amount, Date startDate, Date endDate) {
        this.itemId = itemId;
        this.itemTypeId = itemTypeId;
        this.quantity = quantity;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Usage(List<OrderLineDTO> lines, Integer itemId, Integer itemTypeId, Date startDate, Date endDate) {
        this.itemId = itemId;
        this.itemTypeId = itemTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        
        calculateUsage(lines);
    }

    public void calculateUsage(List<OrderLineDTO> lines) {
        quantity = BigDecimal.ZERO;
        amount = BigDecimal.ZERO;
        for (OrderLineDTO line : lines) {
            quantity = quantity.add(line.getQuantity());
            amount = amount.add(line.getAmount());
        }
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    /**
     * The total quantity, or "number of units" purchased
     * over the period.
     *
     * @return number of units purchased
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * The total dollar amount of usage purchased over the period.
     *
     * @return total amount of usage in dollars
     */
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Usage{"
                + "itemId=" + itemId
                + ", itemTypeId=" + itemTypeId
                + ", quantity=" + quantity
                + ", amount=" + amount
                + ", startDate=" + startDate
                + ", endDate=" + endDate
                + '}';
    }
}
