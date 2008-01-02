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

/*
 * Created on Dec 29, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.order;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.sapienter.jbilling.server.entity.OrderDTO;

/**
 * @author Emil
 * @jboss-net.xml-schema urn="sapienter:OrderWS"
 */
public class OrderWS extends OrderDTO implements Serializable {

    private OrderLineWS orderLines[] = null;
    private Integer period = null;
    private Integer userId = null; // who is buying ?
    // these are necessary for the view page, to show the description
    // instead of the ids
    private String periodStr = null;
    private String billingTypeStr = null;

    /**
     * 
     */
    public OrderWS() {
        super();
    }

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
    public OrderWS(Integer id, Integer billingTypeId, Integer notify,
    		Date activeSince,
            Date activeUntil, Date createDate, Date nextBillableDay,
            Integer createdBy, Integer statusId, Integer deleted,
            Integer currencyId, Date lastNotified, Integer notifStep,
            Integer dueDateUnitId, Integer dueDateValue) {
        super(id, billingTypeId, notify, activeSince, activeUntil, createDate,
                nextBillableDay, createdBy, statusId, deleted, currencyId,
				lastNotified, notifStep, dueDateUnitId, dueDateValue, null, 
                null, null, null, null, null);
    }

    /**
     * @param otherValue
     */
    public OrderWS(OrderDTO otherValue) {
        super(otherValue);
    }
    
    /*
     * User OrderBL.getWS instead !!! god damn it!!!! 

    public OrderWS(OrderEntityLocal record, Integer languageId) 
            throws NamingException, FinderException {
        setId(record.getId()); 
        setBillingTypeId(record.getBillingTypeId());
        setNotify(record.getNotify()); 
        setActiveSince(record.getActiveSince());
        setActiveUntil(record.getActiveUntil()); 
        setCreateDate(record.getCreateDate());
        setNextBillableDay(record.getNextBillableDay()); 
        setCreatedBy(record.getCreatedBy());
        setStatusId(record.getStatusId()); 
        setDeleted(record.getDeleted()); 
        setCurrencyId(record.getCurrencyId()); 
        setLastNotified(record.getLastNotified());
        setNotificationStep(record.getNotificationStep()); 
        setDueDateUnitId(record.getDueDateUnitId()); 
        setDueDateValue(record.getDueDateValue());
        setPeriod(record.getPeriod().getId());
        setPeriodStr(record.getPeriod().getDescription(languageId));
        setUserId(record.getUser().getUserId());
        
        // get the billing type str :p
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        OrderBillingTypeEntityLocalHome orderBillingTypeHome =
            (OrderBillingTypeEntityLocalHome) EJBFactory.lookUpLocalHome(
            OrderBillingTypeEntityLocalHome.class,
            OrderBillingTypeEntityLocalHome.JNDI_NAME);

        setBillingTypeStr(orderBillingTypeHome.findByPrimaryKey(
                record.getBillingTypeId()).getDescription(languageId));
        
        // the lines
        int f = 0;
        orderLines = new OrderLineWS[record.getOrderLines().size()];
        for (Iterator it = record.getOrderLines().iterator(); 
                it.hasNext(); f++) {
            OrderLineEntityLocal line = (OrderLineEntityLocal) it.next();
            orderLines[f] = new OrderLineWS(line.getId(), line.getItemId(),
                    line.getDescription(), line.getAmount(), line.getQuantity(),
                    line.getPrice(), line.getItemPrice(), line.getCreateDate(),
                    line.getDeleted());
            orderLines[f].setTypeId(line.getType().getId());
        }
    }
    */
    
    public OrderWS(NewOrderDTO dto) {
        super(dto);
        period = dto.getPeriod();
        periodStr = dto.getPeriodStr();
        userId = dto.getUserId();
        billingTypeStr = dto.getBillingTypeStr();
        Collection lines = dto.getOrderLinesMap().values();
        orderLines = new OrderLineWS[lines.size()];
        int f = 0;
        for (Iterator it = lines.iterator(); it.hasNext(); f++) {
            OrderLineDTOEx lineDto = (OrderLineDTOEx) it.next();
            orderLines[f] = new OrderLineWS(lineDto);
        }
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
    public OrderLineWS[] getOrderLines() {
        return orderLines;
    }

    /**
     * @param orderLines
     */
    public void setOrderLines(OrderLineWS orderLines[]) {
        this.orderLines = orderLines;
    }

    /**
     * @return
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * @param period
     */
    public void setPeriod(Integer period) {
        this.period = period;
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
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
