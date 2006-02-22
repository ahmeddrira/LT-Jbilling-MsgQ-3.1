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
 * Created on Dec 10, 2004
 *
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.util.GregorianCalendar;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.BillingProcessEntityLocal;
import com.sapienter.jbilling.interfaces.OrderEntityLocal;
import com.sapienter.jbilling.server.process.BillingProcessBL;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;
import com.sapienter.jbilling.server.util.PreferenceBL;

/**
 * @author Emil
 *
 */
public class OrderFilterAnticipatedTask extends BasicOrderFilterTask {
    
    public boolean isApplicable(OrderEntityLocal order, 
            BillingProcessEntityLocal process) throws TaskException {
        // by default, keep it in null 
        billingUntil = null;
        log = Logger.getLogger(OrderFilterAnticipatedTask.class);
        try {
            int pref;
            PreferenceBL preference = new PreferenceBL();
            try {
                preference.set(process.getEntityId(), 
                        Constants.PREFERENCE_USE_ORDER_ANTICIPATION);
            } catch (FinderException e) {
                // I like the default
            }
            if (preference.getInt() == 0 ) {
                log.warn("OrderAnticipated task is called, but this " +
                        "entity has the preference off");
            } else if (order.getAnticipatePeriods() != null && 
                    order.getAnticipatePeriods().intValue() > 0) {
                log.debug("Using anticipated order. Org billingUntil = " +
                        billingUntil + " ant periods " + 
                        order.getAnticipatePeriods());
                // calculate an extended end of billing process
                billingUntil = BillingProcessBL.getEndOfProcessPeriod(process);
                
                // move it forward by the number of ant months
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(billingUntil);
                cal.add(GregorianCalendar.MONTH,
                        order.getAnticipatePeriods().intValue());
                billingUntil = cal.getTime();
            }
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new TaskException(e);
        }
        
        return super.isApplicable(order, process);
    }
}
