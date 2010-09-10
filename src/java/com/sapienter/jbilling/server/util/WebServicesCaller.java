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

package com.sapienter.jbilling.server.util;


/**
 * Provides session storage of web services caller info. The authentication
 * mechanism must make available the caller info here, which is then used by the
 * WebServicesSessionBean and the security proxy.
 */
public class WebServicesCaller implements IWebServicesCaller {
	private int callerId;
	private int callerCompanyId;
	private String callerUserName;

	/* (non-Javadoc)
	 * @see com.sapienter.jbilling.server.util.IWebServicesCaller#setCallerId(int)
	 */
	public void setCallerId(int callerId) {
		this.callerId = callerId;
	}

	/* (non-Javadoc)
	 * @see com.sapienter.jbilling.server.util.IWebServicesCaller#setCallerCompanyId(int)
	 */
	public void setCallerCompanyId(int callerCompanyId) {
		this.callerCompanyId = callerCompanyId;
	}

	/* (non-Javadoc)
	 * @see com.sapienter.jbilling.server.util.IWebServicesCaller#setCallerUserName(java.lang.String)
	 */
	public void setCallerUserName(String callerUserName) {
		this.callerUserName = callerUserName;
	}

	/* (non-Javadoc)
	 * @see com.sapienter.jbilling.server.util.IWebServicesCaller#getCallerUserName()
	 */
	public String getCallerUserName() {
		return callerUserName;
	}

	/* (non-Javadoc)
	 * @see com.sapienter.jbilling.server.util.IWebServicesCaller#getCallerId()
	 */
	public int getCallerId() {
		return callerId;
	}

	/* (non-Javadoc)
	 * @see com.sapienter.jbilling.server.util.IWebServicesCaller#getCallerCompanyId()
	 */
	public int getCallerCompanyId() {
		return callerCompanyId;
	}
	
	public void reset() {
		setCallerCompanyId(0);
		setCallerId(0);
		setCallerUserName(null);
	}
}
