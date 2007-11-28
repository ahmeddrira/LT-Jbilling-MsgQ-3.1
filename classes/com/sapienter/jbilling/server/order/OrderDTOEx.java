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

package com.sapienter.jbilling.server.order;

import java.util.Date;
import java.util.Vector;

import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.entity.UserDTO;

public class OrderDTOEx extends OrderDTO {
    Vector orderLines = null;
    Vector invoices = null;
    // these are billing periods where this order got processed
    Vector periods = null;
    Vector processes = null;
    UserDTO user = null;
    // this is the period (monthly, weekly, etc) that this order
    // currently covers
    Integer periodId = null;
    // Some fields to show readable info
    String periodStr = null;
    String billingTypeStr = null;
    String statusStr = null;
    String timeUnitStr = null;
    // if later there's suppport for more than one promotion, this
    // would be an array
    String promoCode = null;
    // this is pre-calculated for dispaly
    Float total = null;
    // human readable currency info
    String currencySymbol = null;
    String currencyName = null;
    

    /**
     * @param id
     * @param billingTypeId
     * @param activeSince
     * @param activeUntil
     * @param createDate
     * @param nextBillableDay
     * @param createdBy
     * @param toProcess
     * @param deleted
     */
    public OrderDTOEx(Integer id, Integer billingTypeId, Integer notify,
    		Date activeSince,
            Date activeUntil, Date createDate, Date nextBillableDay,
            Integer createdBy, Integer statusId, Integer deleted,
            Integer currencyId, Date lastNotified, Integer notifStep,
            Integer dueDateUnitId, Integer dueDateValue) {
        
        super(id, billingTypeId, notify, activeSince, activeUntil, createDate,
                nextBillableDay, createdBy, statusId, deleted, 
                currencyId, lastNotified, notifStep, dueDateUnitId, dueDateValue,
                null, null, null, null, null);

        orderLines = new Vector();
        invoices = new Vector();
        periods = new Vector();
        processes = new Vector();
    }

    /**
     * @param otherValue
     */
    public OrderDTOEx(OrderDTO otherValue) {
        super(otherValue);

    }

    /**
     * @return
     */
    public Vector getInvoices() {
        return invoices;
    }

    /**
     * @return
     */
    public Vector getOrderLines() {
        return orderLines;
    }

    /**
     * @return
     */
    public Vector getPeriods() {
        return periods;
    }

    /**
     * @return
     */
    public Vector getProcesses() {
        return processes;
    }

    /**
     * @param vector
     */
    public void setInvoices(Vector vector) {
        invoices = vector;
    }

    /**
     * @param vector
     */
    public void setOrderLines(Vector vector) {
        orderLines = vector;
    }

    /**
     * @param vector
     */
    public void setPeriods(Vector vector) {
        periods = vector;
    }

    /**
     * @param vector
     */
    public void setProcesses(Vector vector) {
        processes = vector;
    }

    /**
     * @return
     */
    public Integer getPeriodId() {
        return periodId;
    }

    /**
     * @param integer
     */
    public void setPeriodId(Integer integer) {
        periodId = integer;
    }

    /**
     * @return
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * @param string
     */
    public void setPromoCode(String string) {
        promoCode = string;
    }

    /**
     * @return
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * @param user
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * @return
     */
    public String getBillingTypeStr() {
        return billingTypeStr;
    }

    /**
     * @param billingTypeStr
     */
    public void setBillingTypeStr(String billingTypeStr) {
        this.billingTypeStr = billingTypeStr;
    }

    /**
     * @return
     */
    public String getPeriodStr() {
        return periodStr;
    }

    /**
     * @param periodStr
     */
    public void setPeriodStr(String periodStr) {
        this.periodStr = periodStr;
    }

    /**
     * @return
     */
    public Float getTotal() {
        return total;
    }

    /**
     * @param total
     */
    public void setTotal(Float total) {
        this.total = total;
    }



    /**
     * @return
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * @param currencySymbol
     */
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    /**
     * @return
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * @param currencyName
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * @return
     */
    public String getStatusStr() {
        return statusStr;
    }

    /**
     * @param statusStr
     */
    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getTimeUnitStr() {
        return timeUnitStr;
    }
    public void setTimeUnitStr(String timeUnitStr) {
        this.timeUnitStr = timeUnitStr;
    }
}
