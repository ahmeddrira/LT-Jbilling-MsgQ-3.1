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

/*
 * Created on Apr 28, 2003
 *
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.BillingProcessEntityLocal;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.server.process.BillingProcessBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.EventLogger;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;

/**
 * Verifies if the order should be included in a porcess considering its
 * active range dates. It takes the billing period type in consideration
 * as well. This probably would've been easier to do if EJB/QL had support
 * for Date types
 */
public class BasicOrderFilterTask 
        extends PluggableTask implements OrderFilterTask {

    protected Logger log = null;
    protected Date billingUntil = null;
    
    public BasicOrderFilterTask() {
        billingUntil = null;
    }
    
    /* (non-Javadoc)
     * @see com.sapienter.jbilling.server.pluggableTask.OrderFilterTask#isApplicable(com.sapienter.betty.interfaces.OrderEntityLocal)
     */
    public boolean isApplicable(OrderEntityLocal order, 
            BillingProcessEntityLocal process) throws TaskException {

        boolean retValue = true;
        log = Logger.getLogger(BasicOrderFilterTask.class);
        
        GregorianCalendar cal = new GregorianCalendar();

        log.debug("running isApplicable for order " + order.getId() + 
                " billingUntil = " + billingUntil);
        // some set up to simplify the code
        Date activeUntil = null;
        if (order.getActiveUntil() != null) {
            activeUntil = Util.truncateDate(order.getActiveUntil());
        }
        Date activeSince = null;
        if (order.getActiveSince() != null) {
            activeSince = Util.truncateDate(order.getActiveSince());
        } else {
            // in fact, an open starting point doesn't make sense (an order reaching
            // inifinitly backwards). So we default to the creation date
            activeSince = Util.truncateDate(order.getCreateDate());
        }
        
        try {
            // calculate how far in time this process applies
            if (billingUntil == null) {
                // this could have been set by a class extending this one
                // If not, we use this as a default
                billingUntil = BillingProcessBL.getEndOfProcessPeriod(process);
            }

            EventLogger eLog = EventLogger.getInstance();
            
            if (order.getBillingTypeId().compareTo(
                    Constants.ORDER_BILLING_POST_PAID) == 0) {
                
                // check if it is too early        
                if(activeSince.
                        after(process.getBillingDate())) {
                    // didn't start yet
                    eLog.info(process.getEntityId(), order.getId(), 
                            EventLogger.MODULE_BILLING_PROCESS,
                            EventLogger.BILLING_PROCESS_NOT_ACTIVE_YET,
                            Constants.TABLE_PUCHASE_ORDER);
                    retValue = false;
                // One time only orders don't need to check for periods                     
                } else if (!order.getPeriod().getId().equals(
                        Constants.ORDER_PERIOD_ONCE)) {
                    // check that there's at least one period since this order
                    // started, otherwise it's too early to bill
                    cal.setTime(activeSince);
                    cal.add(MapPeriodToCalendar.map(order.getPeriod().getUnitId()),
                             order.getPeriod().getValue().intValue());
                    Date firstBillingDate = cal.getTime();
                    if (!firstBillingDate.before(billingUntil)) {
                        eLog.info(process.getEntityId(), order.getId(), 
                                EventLogger.MODULE_BILLING_PROCESS,
                                EventLogger.BILLING_PROCESS_ONE_PERIOD_NEEDED,
                                Constants.TABLE_PUCHASE_ORDER);
                        
                        retValue = false; // gotta wait for the first bill
                    }
                }
                
                // there must be at least one period after the last paid day
                if (retValue && order.getNextBillableDay() != null) {
                    cal.setTime(order.getNextBillableDay());
                    cal.add(MapPeriodToCalendar.map(order.getPeriod().getUnitId()),
                             order.getPeriod().getValue().intValue());
                    Date endOfNextPeriod = cal.getTime();                
                    if (endOfNextPeriod.after(process.getBillingDate())) {
                        eLog.info(process.getEntityId(), order.getId(), 
                                EventLogger.MODULE_BILLING_PROCESS,
                                EventLogger.BILLING_PROCESS_RECENTLY_BILLED,
                                Constants.TABLE_PUCHASE_ORDER);
                        
                        retValue = false;
                        
                        // may be it's actually billed to the end of its life span
                        if (activeUntil != null && //may be it's immortal ;)
                                order.getNextBillableDay().compareTo(activeUntil) >= 0) {
                            // this situation shouldn't have happened
                            log.warn("Order " + order.getId() + " should've been" +
                                " flagged out in the previous process");
                            eLog.warning(process.getEntityId(), order.getId(), 
                                    EventLogger.MODULE_BILLING_PROCESS,
                                    EventLogger.BILLING_PROCESS_WRONG_FLAG_ON,
                                    Constants.TABLE_PUCHASE_ORDER);
                                
                            order.setStatusId(Constants.ORDER_STATUS_FINISHED);   
                            order.setNextBillableDay(null);         
                        }
                    }
                }
                
                // post paid orders can't be too late to process 
                      
            } else if (order.getBillingTypeId().compareTo(
                    Constants.ORDER_BILLING_PRE_PAID) == 0) {
            
                //  if it has a billable day
                if (order.getNextBillableDay() != null) {
                    // now check if there's any more time to bill as far as this
                    // process goes
                    log.debug("order " + order.getId() + " nbd = " + 
                            order.getNextBillableDay() + " bu = " + billingUntil);
                    if (order.getNextBillableDay().compareTo(billingUntil) >= 0) {
                        retValue = false;
                        eLog.info(process.getEntityId(), order.getId(), 
                                EventLogger.MODULE_BILLING_PROCESS,
                                EventLogger.BILLING_PROCESS_RECENTLY_BILLED,
                                Constants.TABLE_PUCHASE_ORDER);
                        
                    }
                    
                    // check if it is all billed already
                    if (activeUntil != null && order.getNextBillableDay().
                                compareTo(activeUntil) >= 0) { 
                        retValue = false;
                        log.warn("Order " + order.getId() + " was set to be" +                                " processed but the next billable date is " +                                "after the active unitl");
                        eLog.warning(process.getEntityId(), order.getId(), 
                                EventLogger.MODULE_BILLING_PROCESS,
                                EventLogger.BILLING_PROCESS_EXPIRED,
                                Constants.TABLE_PUCHASE_ORDER);
                        order.setStatusId(Constants.ORDER_STATUS_FINISHED);
                        order.setNextBillableDay(null);                                   
                    }
                } else if (activeUntil != null && process.getBillingDate().
                        after(activeUntil)) {
                    retValue = false;
                    order.setStatusId(Constants.ORDER_STATUS_FINISHED);
                    eLog.warning(process.getEntityId(), order.getId(), 
                            EventLogger.MODULE_BILLING_PROCESS,
                            EventLogger.BILLING_PROCESS_WRONG_FLAG_ON,
                            Constants.TABLE_PUCHASE_ORDER);
                    log.warn("Found expired order " + order.getId() + 
                            " without nbp but with to_process=1");
                }
            
                
                // see if it is too early
                if (retValue && activeSince != null) {
                    
                    if (!activeSince.before(billingUntil)) {
                        // This process is not including the time this order
                        // starts
                        retValue = false;
                        eLog.info(process.getEntityId(), order.getId(), 
                                EventLogger.MODULE_BILLING_PROCESS,
                                EventLogger.BILLING_PROCESS_NOT_ACTIVE_YET,
                                Constants.TABLE_PUCHASE_ORDER);
                        
                    }
                }
                
                // finally if it is too late (would mean a warning)
                if (retValue && activeUntil != null) {
                    if (process.getBillingDate().after(activeUntil)) {
                        // how come this order has some period yet to be billed, but
                        // the active is already history ? It should've been billed
                        // in a previous process
                        eLog.warning(process.getEntityId(), order.getId(), 
                                 EventLogger.MODULE_BILLING_PROCESS,
                                 EventLogger.BILLING_PROCESS_EXPIRED,
                                Constants.TABLE_PUCHASE_ORDER);
            
                        log.warn("Order with time yet to be billed not included!");
                    }
                }
                        
            } else {
                throw new TaskException("Billing type of this order " +
                    "is not supported:" + order.getBillingTypeId());
            }
        } catch (NumberFormatException e) {
            log.fatal("Exception converting types", e);
            throw new TaskException("Exception with type conversions: " +
                    e.getMessage());
        } catch (SessionInternalError e) {
            log.fatal("Internal exception ", e);
            throw new TaskException(e);
        } catch (NamingException e) {
            log.fatal("Problems with the event logger", e);
            throw new TaskException(e);
        } 
        
        log.debug("Order " + order.getId() + " filter:" + retValue); 
        
        return retValue;

    }

}
