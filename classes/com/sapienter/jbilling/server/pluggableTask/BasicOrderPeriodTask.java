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

package com.sapienter.jbilling.server.pluggableTask;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.server.process.ConfigurationBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;

public class BasicOrderPeriodTask
    extends PluggableTask
    implements OrderPeriodTask {
    
    protected Date viewLimit = null;
    protected Logger log = null;

    
    public BasicOrderPeriodTask() {
        viewLimit = null;
    }

    /**
     * Calculates the date that the invoice about to be generated is
     * going to start cover from this order. This IS NOT the invoice
     * date, since an invoice is composed by (potentially) several orders and
     * other invoices
     * @param order
     * @return
     */
     public Date calculateStart(OrderEntityLocal order) throws TaskException {
        Date retValue = null;

        if (order.getPeriod().getId().compareTo(
                Constants.ORDER_PERIOD_ONCE) == 0) {
            // this should be irrelevant, and could be either the order date
            // or this process date ...
            return null;
        } 
        
        if (order.getNextBillableDay() == null) {
            // never been processed
            // If it is open started (with no start date), we assume that
            // it started when it was created
            retValue = order.getActiveSince() == null ? 
                            order.getCreateDate() :
                            order.getActiveSince();
            
        } else {
            // the process date means always which day has not been paid
            // for yet.
            retValue = order.getNextBillableDay();
        }
        
        if (retValue == null) {
            throw new TaskException("Missing some date fields on " +
                "order " + order.getId());
        }
        
        // it's important to trucate this date
        
        return Util.truncateDate(retValue);
    }
 
    /**
     * This methods takes and order and calculates the end date that is 
     * going to be covered cosidering the starting date and the dates
     * of this process. 
     * @param order
     * @param process
     * @param startOfBillingPeriod
     * @return
     * @throws SessionInternalError
     */
    public Date calculateEnd(OrderEntityLocal order, 
            Date processDate, int maxPeriods) throws TaskException {

        if (order.getPeriod().getId().compareTo(
                Constants.ORDER_PERIOD_ONCE) == 0) {
            return null;
        }                    

        int periodsIn = 0;
        Date endOfPeriod = null;
        Date startOfBillingPeriod = calculateStart(order);
        GregorianCalendar cal = new GregorianCalendar();
        if (log == null) {
            log = Logger.getLogger(BasicOrderPeriodTask.class);
        }
        try {
            // calculate the how far we can see in the future
            // get the period of time from the process configuration
            if (viewLimit == null) {
                viewLimit = getViewLimit(order.getUser().getEntity().getId(),
                        processDate);
            }
            
            cal.setTime(startOfBillingPeriod);
        
            log.debug("Calculating ebp for order " + order.getId() + " sbp:" +
                    startOfBillingPeriod + " process date: " + processDate +
                    " viewLimit:" + viewLimit);
            
            if (!order.getStatusId().equals(Constants.ORDER_STATUS_ACTIVE)) {
                throw new TaskException("Only active orders should be " +
                        "generating invoice. This " + order.getStatusId());
            }

            if (order.getBillingTypeId().compareTo(
                    Constants.ORDER_BILLING_POST_PAID) == 0 ) {
                // this will move on time from the start of the billing period
                // to the closest muliple period that doesn't go beyond the 
                // visibility date
                
                while (cal.getTime().compareTo(viewLimit) < 0 &&
                        (order.getActiveUntil() == null ||
                         cal.getTime().compareTo(order.getActiveUntil()) <= 0) &&
                         periodsIn <= maxPeriods) {
                    // I assign the end period before adding another period
                    endOfPeriod = cal.getTime();
                    cal.add(MapPeriodToCalendar.map(order.getPeriod().getUnitId()),
                            order.getPeriod().getValue().intValue());
                    periodsIn++;
                    log.debug("post paid, now testing:" + cal.getTime() +
                            "(eop) = " + endOfPeriod + " compare " + 
                            cal.getTime().compareTo(viewLimit));
                }
            } else if (order.getBillingTypeId().compareTo(
                    Constants.ORDER_BILLING_PRE_PAID) == 0) {
                /* here the end of the period will be after the start of the billing
                 * process. This means that is NOT taking ALL the periods that are
                 * visible to this process, just the first one after the start of the
                 * process
                 */
                 
                // bring the date until it goes over the view limit
                // (or it reaches the expiration).
                // This then takes all previos periods that should've been billed
                // by previous processes
                while (cal.getTime().compareTo(viewLimit) < 0 &&
                        (order.getActiveUntil() == null ||
                         cal.getTime().compareTo(order.getActiveUntil()) < 0) &&
                         periodsIn < maxPeriods) {
                    cal.add(MapPeriodToCalendar.map(order.getPeriod().getUnitId()),
                            order.getPeriod().getValue().intValue());
                    periodsIn++;
                    log.debug("pre paid, now testing:" + cal.getTime() +
                            "(eop) = " + endOfPeriod + " compare " + 
                            cal.getTime().compareTo(viewLimit));          
                }
                
                endOfPeriod = cal.getTime();
                        
            } else {
                throw new TaskException("Order billing type "
                        + order.getBillingTypeId() + " is not supported");
            }
        } catch (Exception e) {
            throw new TaskException(e);
        }
        
        if (endOfPeriod == null) {
            throw new TaskException("Error calculating for order " +
                    order.getId());
        } else if (order.getActiveUntil() != null &&
                endOfPeriod.after(order.getActiveUntil())) {
            // make sure this date is not beyond the expiration date
            endOfPeriod = order.getActiveUntil();
        } else if (startOfBillingPeriod.compareTo(endOfPeriod) == 0) {
            // may be this won't be a throw, and will be used to check if
            // this order is fractional or not.
            throw new TaskException("Calculating the end period for" +
                " order " + order.getId() + " ends up being the same as the" +
                " start period. Shouldn't this order be excluded?");
        } 
        
        log.debug("ebp:" + endOfPeriod);
        
        return endOfPeriod;
    }

    protected Date getViewLimit(Integer entityId, Date processDate) 
            throws NamingException, FinderException, SessionInternalError {
        ConfigurationBL config = new ConfigurationBL(entityId);
        Integer periodUnitId = config.getEntity().getPeriodUnitId();
        Integer periodValue = config.getEntity().getPeriodValue();
        Calendar cal = Calendar.getInstance();
    
        cal.setTime(processDate);
        cal.add(MapPeriodToCalendar.map(periodUnitId),
                periodValue.intValue());
        return cal.getTime();

    }
}
