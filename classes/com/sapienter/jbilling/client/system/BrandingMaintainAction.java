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

package com.sapienter.jbilling.client.system;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.CrudAction;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;

public class BrandingMaintainAction extends CrudAction {
	private static final String FORM_BRANDING = "branding";
	private static final String FIELD_CSS = "css";
	private static final String FIELD_LOGO = "logo";
	private static final String MESSAGE_SUCCESS = "system.branding.updated";
	private static final String FORWARD_SUCCESS = "branding_edit";

	private final UserSession myUserSession;

	public BrandingMaintainAction() {
		setFormName(FORM_BRANDING);
		try {
			JNDILookup EJBFactory = JNDILookup.getFactory(false);
			UserSessionHome userHome = (UserSessionHome) EJBFactory.lookUpHome(
					UserSessionHome.class, UserSessionHome.JNDI_NAME);
			myUserSession = userHome.create();
		} catch (Exception e) {
			throw new SessionInternalError(
					"Initializing branding CRUD action: " + e.getMessage());
		}
	}

	@Override
	public void setup() {
		// set up which preferences do we need
		Integer[] preferenceIds = new Integer[] { //
				Constants.PREFERENCE_CSS_LOCATION, //
				Constants.PREFERENCE_LOGO_LOCATION, //
		};
		// get'em
		Map<?, ?> result;
		try {
			result = myUserSession.getEntityParameters(entityId, preferenceIds);
		} catch (RemoteException e) {
			throw new SessionInternalError("Setup branding error:"
					+ e.getMessage());
		}

		myForm.set(FIELD_CSS, stringNotNull(result
				.get(Constants.PREFERENCE_CSS_LOCATION)));
		myForm.set(FIELD_LOGO, stringNotNull(result
				.get(Constants.PREFERENCE_LOGO_LOCATION)));
		
		setForward(FORWARD_SUCCESS);
	}

	@Override
	public String delete() {
		throw new UnsupportedOperationException(
				"Can't delete branding. Delete mode is not supported");
	}

	@Override
	public void create(Object dtoHolder) {
		throw new UnsupportedOperationException(
				"Can't create branding. Create mode is not supported");
	}
	
	@Override
	public void reset() {
		//do nothing, reset is not supported
	}
	
	@Override
	public boolean otherAction(String action) {
		return false;
	}

	@Override
	public String update(Object dtoHolder) {
		CssAndLogo cssAndLogo = (CssAndLogo) dtoHolder;
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(Constants.PREFERENCE_CSS_LOCATION, cssAndLogo.getCss());
		map.put(Constants.PREFERENCE_LOGO_LOCATION, cssAndLogo.getLogo());
		try {
			myUserSession.setEntityParameters(entityId, map);
		} catch (RemoteException e) {
			throw new SessionInternalError("Branding update failed: "
					+ e.getMessage());
		}
		setForward(FORWARD_SUCCESS);
		return MESSAGE_SUCCESS;
	}

	@Override
	public Object editFormToDTO() {
		return new CssAndLogo((String) myForm.get(FIELD_CSS), (String) myForm
				.get(FIELD_LOGO));
	}

	protected static String stringNotNull(Object object) {
		return object == null ? "" : String.valueOf(object);
	}

	protected static String safeTrim(String text) {
		return stringNotNull(text).trim();
	}

	private static class CssAndLogo {
		private final String myLogo;
		private final String myCss;

		public CssAndLogo(String css, String logo) {
			myCss = safeTrim(css);
			myLogo = safeTrim(logo);
		}

		public String getCss() {
			return myCss;
		}

		public String getLogo() {
			return myLogo;
		}
	}

}
