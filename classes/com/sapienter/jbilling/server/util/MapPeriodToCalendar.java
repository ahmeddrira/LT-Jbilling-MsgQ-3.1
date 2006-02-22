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
 * Created on Apr 25, 2003
 *
 */
package com.sapienter.jbilling.server.util;

import java.util.GregorianCalendar;

import com.sapienter.jbilling.common.SessionInternalError;

/**
 * @author emilc
 *
 */
public class MapPeriodToCalendar {
    public static int map(Integer period) 
            throws SessionInternalError {
        int retValue;
        
        if (period == null) {
            throw new SessionInternalError("Can't map a period that is null");
        }
        if (period.compareTo(Constants.PERIOD_UNIT_DAY) == 0) {
            retValue = GregorianCalendar.DAY_OF_YEAR;
        } else if (period.compareTo(Constants.PERIOD_UNIT_MONTH) == 0) {
            retValue = GregorianCalendar.MONTH;
        } else if (period.compareTo(Constants.PERIOD_UNIT_WEEK) == 0) {
            retValue = GregorianCalendar.WEEK_OF_YEAR;
        } else if (period.compareTo(Constants.PERIOD_UNIT_YEAR) == 0) {
            retValue = GregorianCalendar.YEAR;
        } else { // error !
            throw new SessionInternalError("Period not supported:" + period);
        }
        
        return retValue;
    }
}
