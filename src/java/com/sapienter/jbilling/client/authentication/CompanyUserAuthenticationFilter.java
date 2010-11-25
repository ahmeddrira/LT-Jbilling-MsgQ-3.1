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

package com.sapienter.jbilling.client.authentication;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * An extension of the base spring security {@link UsernamePasswordAuthenticationFilter} that appends
 * the user entered company ID to the username for authentication.
 *
 * Similar to the {@link UsernamePasswordAuthenticationFilter}, the web form parameter names can be
 * configured via spring bean properties. 
 *
 * Default configuration:
 *      passwordParameter = "j_password"
 *      usernameParameter = "j_username"
 *      clientIdParameter = "j_client_id"
 *
 * 
 *
 * @author Brian Cowdery
 * @since 04-10-2010
 */
public class CompanyUserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOG = Logger.getLogger(CompanyUserAuthenticationFilter.class);

    public static final String FORM_CLIENT_ID_KEY = "j_client_id";

    public static final String SESSION_USER_ID = "user_id";
    public static final String SESSION_USER_LANGUAGE_ID = "language_id";
    public static final String SESSION_USER_COMPANY_ID = "company_id";
    public static final String SESSION_USER_LOCALE = "locale";

    private String clientIdParameter;

    /**
     * Returns the form submitted user name as colon delimited string containing
     * the user name and client id of the user to authenticate, e.g., "bob:1"
     * 
     * @param request HTTP servlet request
     * @return username string
     */
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username = request.getParameter(getUsernameParameter());
        String companyId = request.getParameter(getClientIdParameter());

        return UsernameHelper.buildUsernameToken(username, companyId);
    }

    public final String getClientIdParameter() {
        return clientIdParameter == null ? FORM_CLIENT_ID_KEY : clientIdParameter;
    }

    public void setClientIdParameter(String clientIdParameter) {
        this.clientIdParameter = clientIdParameter;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            Authentication result) throws IOException, ServletException {

        CompanyUserDetails principal = (CompanyUserDetails) result.getPrincipal();

        // add common user attributes to the session for easy access
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_USER_ID, principal.getUserId());                                                                         
        session.setAttribute(SESSION_USER_LANGUAGE_ID, principal.getLanguageId());
        session.setAttribute(SESSION_USER_COMPANY_ID, principal.getCompanyId());        
        session.setAttribute(SESSION_USER_LOCALE, principal.getLocale());

        super.successfulAuthentication(request, response, result);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        String principal = (String) failed.getAuthentication().getPrincipal();
        LOG.debug("User " + principal + " authentication failed!");

        // clear user session variables just in-case. the user could have re-authenticated
        // with bad credentials, which would invalidate the current session.
        HttpSession session = request.getSession();
        session.removeAttribute(SESSION_USER_ID);
        session.removeAttribute(SESSION_USER_LANGUAGE_ID);
        session.removeAttribute(SESSION_USER_COMPANY_ID);
        session.removeAttribute(SESSION_USER_LOCALE);

        super.unsuccessfulAuthentication(request, response, failed);
    }
}
