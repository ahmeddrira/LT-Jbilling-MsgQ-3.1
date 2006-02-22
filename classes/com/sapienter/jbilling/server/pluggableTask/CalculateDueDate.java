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
 * Created on 23-Apr-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.invoice.NewInvoiceDTO;
import com.sapienter.jbilling.server.util.MapPeriodToCalendar;

/**
 * This simple task gets the days to add to the invoice date from the 
 * billing process configuration. It doesn't get into any other consideration,
 * like business days, etc ...
 * @author Emil
 */
public class CalculateDueDate
    extends PluggableTask
    implements InvoiceCompositionTask {

    /* (non-Javadoc)
     * @see com.sapienter.jbilling.server.pluggableTask.InvoiceCompositionTask#apply(com.sapienter.betty.server.invoice.NewInvoiceDTO)
     */
    public void apply(NewInvoiceDTO invoice, Integer userId) throws TaskException {
        // set up
        Date generated = invoice.getBillingDate();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(generated);
        Logger.getLogger(CalculateDueDate.class).debug(
                "Calculating due date from " + cal.getTime());
        
        // get the days configures
        try {
            // add the period of time
            cal.add(MapPeriodToCalendar.map(invoice.getDueDatePeriod().getUnitId()), 
                    invoice.getDueDatePeriod().getValue().intValue());
            // set the due date
            invoice.setDueDate(cal.getTime());
        } catch (Exception e) {
            Logger.getLogger(CalculateDueDate.class).error("Exception:", e);
            throw new TaskException(e);
        }
       
    }

}
