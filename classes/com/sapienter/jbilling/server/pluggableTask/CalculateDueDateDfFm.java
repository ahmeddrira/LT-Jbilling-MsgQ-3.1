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
 * Created on Oct 11, 2004
 *
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.sapienter.jbilling.server.invoice.NewInvoiceDTO;

/**
 * @author Emil
 *
 */
public class CalculateDueDateDfFm extends CalculateDueDate {
    public void apply(NewInvoiceDTO invoice, Integer userId) throws TaskException {
        // make the normal calculations first
        super.apply(invoice, null);
        // then get into the Df Fm: last day of the month
        if (invoice.getDueDatePeriod().getDf_fm() != null && 
                invoice.getDueDatePeriod().getDf_fm().booleanValue()) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(invoice.getDueDate());
            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, lastDay);
            invoice.setDueDate(cal.getTime());
        }
    }
        
}
