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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.Resources;

public class AlphaNumValidator {

	private static String NUMBERS = ".*[0-9].*";
	private static String LETTERS = ".*[A-Za-z].*";

	
	public static boolean basicValidation(String password) {
		boolean result = true;
		if (password == null || password.equals("")) {
			return true;
		}
		try {
			if (!password.matches(LETTERS) || !password.matches(NUMBERS)) {
				result = false;
			}
		} catch (Throwable e) {
			result = false;
		}
		return result;
	}


	public static boolean validateAlphaNum(
			Object bean,
			ValidatorAction va, 
			Field field,
			ActionErrors errors,
			HttpServletRequest request, 
			ServletContext application) {

		String value = ValidatorUtils.getValueAsString(
				bean, field.getProperty());

		if (!GenericValidator.isBlankOrNull(value)) {
			try {
				if (!basicValidation(value)) {
					errors.add(field.getKey(),
							Resources.getActionError(
									request,
									va,
									field));

					return false;
				}
			} catch (Exception e) {
				errors.add(field.getKey(),
						Resources.getActionError(
								request,
								va,
								field));
				return false;
			}
		}
		return true;
	}
	
}
