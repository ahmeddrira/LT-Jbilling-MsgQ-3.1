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

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.order.db.OrderDAS;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO;
import com.sapienter.jbilling.server.order.db.UsageDAS;
import com.sapienter.jbilling.server.pluggableTask.OrderPeriodTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskManager;
import com.sapienter.jbilling.server.util.Constants;
import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Provides easy access to usage information over the customers natural billing period.
 *
 * @author Brian Cowdery
 * @since 16-08-2010
 */
public class UsageBL {
    private static final Logger LOG = Logger.getLogger(UsageBL.class);

    private UsageDAS usageDas;

    private Integer userId;
    private OrderDTO mainOrder;

    public UsageBL(Integer userId) {
        _init();
        set(userId);
    }

    private void _init() {
        usageDas = new UsageDAS();
    }

    public void set(Integer userId) {
        this.userId = userId;

        // get main subscription order
        Integer orderId = new OrderBL().getMainOrderId(userId);
        if (orderId != null)
            mainOrder = new OrderBL(orderId).getEntity();

        if (mainOrder == null)
            throw new SessionInternalError("Customer " + userId + " does not have main subscription order!");
    }

    public Integer getUserId() {
        return userId;
    }

    /**
     * Returns the main subscription order for this customer. The users main subscription
     * order defines the billing cycle dates.
     * 
     * @return customers main subscription order.
     */
    public OrderDTO getMainOrder() {
        return mainOrder;
    }

    /**
     * Returns the cycle start date of the customers main subscription order. If the order
     * does not have a set cycle start date, the active since date will be returned. If the
     * order does not have an active since date, the creation date will be returned.
     *
     * @return order cycle start date
     */
    public Date getCycleStartDate() {
        return mainOrder.getCycleStarts() != null ? mainOrder.getCycleStarts()
                                                  : (mainOrder.getActiveSince() != null ? mainOrder.getActiveSince()
                                                                                        : mainOrder.getCreateDate());
    }

    /**
     * Returns the period start date for N periods in the past, where 1 would
     * be the previous period (effectively the current period start date minus 1 cycle)
     * and 0 would be the current period only.
     *
     * @param periods number of periods in the past
     * @return calculated past period start date
     */
    public Date getPeriodStart(Integer periods) {        
        DateMidnight start;
        try {
            Integer entityId = mainOrder.getBaseUserByUserId().getCompany().getId(); 
            PluggableTaskManager manager = new PluggableTaskManager(entityId, Constants.PLUGGABLE_TASK_ORDER_PERIODS);
            OrderPeriodTask periodTask = (OrderPeriodTask) manager.getNextClass();

            if (periodTask == null)
                throw new SessionInternalError("OrderPeriodTask not configured!");

            start = new DateMidnight(periodTask.calculateStart(mainOrder).getTime());
            
        } catch (PluggableTaskException e) {
            throw new SessionInternalError("Exception occurred retrieving the configured OrderPeriodTask.", e);
        } catch (TaskException e) {
            throw new SessionInternalError("Exception occurred calculating the customers current period start date.", e);
        }

        LOG.debug("Main order period start date: " + start);
        
        OrderPeriodDTO period = mainOrder.getOrderPeriod();
        periods = periods * period.getValue();

        if (period.getUnitId().equals(Constants.PERIOD_UNIT_DAY)) {
            return start.minusDays(periods).toDate();
        } else if (period.getUnitId().equals(Constants.PERIOD_UNIT_WEEK)) {
            return start.minusWeeks(periods).toDate();
        } else if (period.getUnitId().equals(Constants.PERIOD_UNIT_MONTH)) {
            return start.minusMonths(periods).toDate();
        } else if (period.getUnitId().equals(Constants.PERIOD_UNIT_YEAR)) {
            return start.minusYears(periods).toDate();
        } else {
            throw new IllegalStateException("Period unit " + period.getUnitId() + " is not supported!");
        }
    }

    /**
     * Returns the current period end date for this customer, always the end of day today.
     *
     * @return current period end date
     */
    public Date getPeriodEnd() {
        // todo: should calculate using the OrderPeriodTask, using EOD today excludes charges created with future dates
        return new DateMidnight().plusDays(1).toDate();
    }

    /**
     * Returns the total usage of the given itemId for the past N periods. Zero periods
     * in the past will include only usage for the current billing period.
     *
     * @param itemId item id
     * @param periods number of past periods to include
     * @return usage
     */
    public Usage getItemUsage(Integer itemId, Integer periods) {
        Date startDate = getPeriodStart(periods);
        Date endDate = getPeriodEnd();
        LOG.debug("Fetching usage of item for " + periods + " period(s), start: " + startDate + ", end: " + endDate);
        return usageDas.findUsageByItem(itemId, startDate, endDate);
    }

    /**
     * Returns the total usage of the given item type for the past N periods. Zero periods
     * in the past will include only usage for the current billing period.
     * 
     * @param itemTypeId item type id
     * @param periods number of past periods to include
     * @return usage
     */
    public Usage getItemTypeUsage(Integer itemTypeId, Integer periods) {
        Date startDate = getPeriodStart(periods);
        Date endDate = getPeriodEnd();
        LOG.debug("Fetching usage of item type for " + periods + " period(s), start: " + startDate + ", end: " + endDate);
        return usageDas.findUsageByItemType(itemTypeId, startDate, endDate);
    }
}
