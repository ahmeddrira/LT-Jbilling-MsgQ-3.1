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
package com.sapienter.jbilling.server.user.validator;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.Resources;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.user.ContactDTOEx;


public class NoUserInfoInPasswordValidator {
	
	/**
	 * This method verifies that the password passed as parameter does not
	 * contain any user information as retrieved from the user contact
	 * record.
	 * @param userId User ID of the user whose password is being verified.
	 * @param password the new password that is being validated.
	 * @return <code>true</code> if the password passes the verification,
	 * otherwise returns <code>false</code>.
	 */
	public static boolean basicValidation(Object dto, String password) {
		boolean retVal = true;
		try {
			if (dto == null) {
				retVal = false;
			}
			else {
				// Check all the fields against the password by using reflection.
				Class cl = dto.getClass();
				Method m[] = cl.getMethods();
				for (int i = 0; i < m.length && retVal == true; i++) {
					// We're interested only in the getter methods
					// that return a String value.
					if ( m[i].getReturnType() != String.class ||
						!m[i].getName().startsWith("get")) {
						continue;
					}
					
					// We can now invoke the method via reflection to retrieve
					// the value.
					String temp = (String)m[i].invoke(dto);
					if (temp == null) {
						continue;
					}
					
					// Now check the value against the provided password.
					if (temp.equalsIgnoreCase(password)) {
						retVal = false;
						break;
					}
					/*
					 * Now, break up the returned values into words and check
					 * those. This intercepts a case where, for example, the
					 * contact's name is "John Michael Doe" and the password
					 * is set to be only "michael".
					 */
					String te[] = temp.split(" ");
					for (int j = 0; j < te.length; j++) {
						if (te[j].equalsIgnoreCase(password)) {
							retVal = false;
							break;
						}
					}
				}
			}
		}
		catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}


	/**
	 * Struts validator. This method retrieves the parameters necessary for
	 * validating the password passed and calls basicValidation() to verify
	 * the value. As such, it only represents a struts wrapper to
	 * the core validation routine.
	 * @return
	 */
	public static boolean validateNoUserInfo(
			Object bean,
			ValidatorAction va, 
			Field field,
			ActionErrors errors,
			HttpServletRequest request, 
			ServletContext application) {
		
		boolean retVal = true;

		try {

			String value = ValidatorUtils.getValueAsString(
						bean, field.getProperty());

			if (!GenericValidator.isBlankOrNull(value)) {
				JNDILookup EJBFactory = JNDILookup.getFactory(false);
				UserSessionHome userHome = (UserSessionHome)EJBFactory.lookUpHome(
						UserSessionHome.class,
						UserSessionHome.JNDI_NAME);
				UserSession user = userHome.create();
				ContactDTOEx dto = user.getPrimaryContactDTO(
						(Integer)request.getSession().getAttribute("user_id"));
				if (dto != null) {
					retVal = basicValidation(dto, value);
				}
			}

		} catch (Exception e) {
			retVal = false;
		}
		if (retVal == false) {
			errors.add(field.getKey(),
					Resources.getActionError(
							request,
							va,
							field));
		}
		return retVal;
	}
}