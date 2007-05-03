package com.sapienter.jbilling.client.util;

import java.rmi.RemoteException;
import java.util.Map;

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
	
	public static class PreferencesMap {
		private final Map<Integer, String> myMap;

		public PreferencesMap(Map<Integer, String> map){
			myMap = map;
		}
		
		public String getString(Integer key){
			String result = myMap.get(key);
			return result == null ? "" : result;
		}
		
		public Integer getInteger(Integer key){
			String result = myMap.get(key);
			return Integer.valueOf(result);
		}
		
		public boolean getBoolean(Integer key){
			String result = myMap.get(key);
			return "1".equals(result);
		}
		
	}

}
