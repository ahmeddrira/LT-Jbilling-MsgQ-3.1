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
