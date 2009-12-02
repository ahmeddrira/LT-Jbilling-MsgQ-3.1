/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

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
package com.sapienter.jbilling.server.process;

import com.sapienter.jbilling.common.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PeriodOfTime {
    private static final long MILLISEC_PER_DAY = 24 * 60 * 60 * 1000;
    
	private final Date start;
	private final Date end;
	private final int position;
	private final int daysInCycle;

	public PeriodOfTime(Date start, Date end, int dayInCycle, int position) {
		this.start = Util.truncateDate(start);
		this.end = Util.truncateDate(end);
		this.position = position;
		this.daysInCycle = dayInCycle;
	}

	public Date getEnd() {
		return end;
	}

	public int getPosition() {
		return position;
	}

	public Date getStart() {
		return start;
	}

	public int getDaysInCycle() {
		return daysInCycle;
	}

    /**
     * Find the number of days between the period start date to the period end date inclusively. This means
     * that the start and end dates are counted as a days within the period. For example, January 01 to
     * January 10th includes 10 days total.
     *
     * This method takes into account daylight savings time to ensure that days are counted
     * correctly across DST boundaries.
     *
     * @return number of days between start and end dates
     */
	public int getDaysInPeriod() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(end);
        long endMillis = calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis());

        calendar.setTime(start);
        long startMillis = calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis());

        if (endMillis <= startMillis)
            return 0;

        Long delta = (endMillis - startMillis) / MILLISEC_PER_DAY;
        return delta.intValue() + 1; // convert delta to an inclusive number of days 
	}

    @Override
	public String toString() {
		return "period starts: " + start + " ends " + end + " position "
				+ position + " days in cycle " + getDaysInCycle();
	}
}
