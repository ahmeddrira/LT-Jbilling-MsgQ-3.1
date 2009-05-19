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

import java.util.Date;

import com.sapienter.jbilling.common.Util;

public class PeriodOfTime {
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

	public int getDaysInPeriod() {
		if (end.getTime() <= start.getTime()) return 0;
		// convert an amount of milliseconds to days
		return (int) ((end.getTime() - start.getTime()) / 1000 / 60 / 60 / 24);
	}

	public String toString() {
		return "period starts: " + start + " ends " + end + " position "
				+ position + " days in cycle " + getDaysInCycle();
	}
}
