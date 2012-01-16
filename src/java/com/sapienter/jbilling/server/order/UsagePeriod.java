/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

package com.sapienter.jbilling.server.order;

import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.process.PeriodOfTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * UsagePeriod represents a period of time for a customer's usage calculations. This
 * class holds the customer's main subscription order, the cycle start and end dates,
 * and a list of periods for the {@link UsageBL} usage period.
 *
 * @author Brian Cowdery
 * @since 03-09-2010
 */
public class UsagePeriod implements Serializable {

    // extracted from UsageBL fields to provide an object that could be cached

    private OrderDTO mainOrder;
    private Date cycleStartDate;
    private Date cycleEndDate;
    private List<PeriodOfTime> billingPeriods;

    public UsagePeriod() { }

    public OrderDTO getMainOrder() {
        return mainOrder;
    }

    public void setMainOrder(OrderDTO mainOrder) {
        this.mainOrder = mainOrder;
    }

    public Date getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(Date cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public Date getCycleEndDate() {
        return cycleEndDate;
    }

    public void setCycleEndDate(Date cycleEndDate) {
        this.cycleEndDate = cycleEndDate;
    }

    public List<PeriodOfTime> getBillingPeriods() {
        return billingPeriods;
    }

    public void setBillingPeriods(List<PeriodOfTime> billingPeriods) {
        this.billingPeriods = billingPeriods;
    }

    @Override public String toString() {
        return "UsagePeriod{"
               + "mainOrder=" + (mainOrder != null ? mainOrder.getId() : null)
               + ", cycleStartDate=" + cycleStartDate
               + ", cycleEndDate=" + cycleEndDate
               + ", billingPeriods=" + (billingPeriods != null ? billingPeriods.size() : null)
               + '}';
    }
}
