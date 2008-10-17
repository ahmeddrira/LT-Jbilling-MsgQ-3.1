/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.client.util;

import java.rmi.RemoteException;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;

public abstract class PreferencesCrudActionBase<DTO> extends UpdateOnlyCrudActionBase<DTO> {
	private final UserSession myUserSession;
	
	public PreferencesCrudActionBase(String formName, String logFriendlyActionType, String forwardEdit) {
		super(formName, logFriendlyActionType, forwardEdit);
		try {
			JNDILookup EJBFactory = JNDILookup.getFactory(false);
			UserSessionHome userHome = (UserSessionHome) EJBFactory.lookUpHome(
					UserSessionHome.class, UserSessionHome.JNDI_NAME);

			myUserSession = userHome.create();
		} catch (Exception e) {
			throw new SessionInternalError(
					"Initializing " + logFriendlyActionType + " CRUD action: "
							+ e.getMessage());
		}
	}

	protected final UserSession getUserSession(){
		return myUserSession;
	}
	
	@SuppressWarnings("unchecked")
	protected final PreferencesMap getEntityPreferences(Integer[] ids) throws RemoteException {
		return new PreferencesMap(myUserSession.getEntityParameters(entityId, ids));
	}
	
	protected final boolean getCheckBoxBooleanValue(String fieldName) {
		return (Boolean) myForm.get(fieldName);
	}

}
