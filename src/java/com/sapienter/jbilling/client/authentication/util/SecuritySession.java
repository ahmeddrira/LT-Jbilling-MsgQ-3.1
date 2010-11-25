/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.client.authentication.util;

import com.sapienter.jbilling.client.authentication.CompanyUserDetails;

import javax.servlet.http.HttpSession;

/**
 * Helper class to handle the export of user attributes as session attributes.
 *
 * @author Brian Cowdery
 * @since 25-11-2010
 */
public class SecuritySession {

    public static final String USER_ID = "user_id";
    public static final String USER_LANGUAGE_ID = "language_id";
    public static final String USER_CURRENCY_ID = "currency_id";
    public static final String USER_COMPANY_ID = "company_id";
    public static final String USER_LOCALE = "locale";

    /**
     * Sets common attributes of the logged in user as session attributes.
     * 
     * @param session session
     * @param principal logged in user
     */
    public static void setSessionAttributes(HttpSession session, CompanyUserDetails principal) {
        session.setAttribute(USER_ID, principal.getUserId());
        session.setAttribute(USER_LANGUAGE_ID, principal.getLanguageId());
        session.setAttribute(USER_CURRENCY_ID, principal.getCurrencyId());
        session.setAttribute(USER_COMPANY_ID, principal.getCompanyId());
        session.setAttribute(USER_LOCALE, principal.getLocale());
    }

    /**
     * Clears the security session variables from the current HttpSession. This should be
     * done whenever an un-successful login attempt is made to ensure that no session attributes
     * leak over when switching users or performing complex authentication steps.
     *
     * @param session session to clear
     */
    public static void clearSessionAttributes(HttpSession session) {
        session.removeAttribute(USER_ID);
        session.removeAttribute(USER_LANGUAGE_ID);
        session.removeAttribute(USER_CURRENCY_ID);
        session.removeAttribute(USER_COMPANY_ID);
        session.removeAttribute(USER_LOCALE);
    }
}
