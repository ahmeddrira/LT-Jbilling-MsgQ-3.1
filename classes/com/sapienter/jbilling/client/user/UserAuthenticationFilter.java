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

package com.sapienter.jbilling.client.user;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.Util;

/**
 * @author emilc
 *
 * This filter applies to all JSPs files. It verifies that a session with 
 * a username exists, which means that the user has logged in.
 * It takes as a parameter from the descriptor the name of the Login servlet,
 * because it's the only one that shouldn't be filtered and is the one 
 * where those not authenticated will be redirected.
 * mapping:
 * A string beginning with a ?/? character and ending with a ?/*? postfix is used
 *  for path mapping.
 * A string beginning with a ?*.? prefix is used as an extension mapping.
 * A string containing only the ?/? character indicates the "default" servlet of the
 *   application. In this case the servlet path is the request URI minus the context
 *   path and the path info is null.
 * All other strings are used for exact matches only.
 */
public final class UserAuthenticationFilter implements Filter {
    
    private String loginPage = null;
    private String loginAction = null;
    private String signupPrefix = null;
    private String forgetPasswordPage =  null;
    private String forgetPasswordAction = null;
    private String changePasswordAction = null;
    private Logger log = null;

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.loginPage = filterConfig.getInitParameter("login_page");
        this.loginAction = filterConfig.getInitParameter("login_action");
        this.signupPrefix = filterConfig.getInitParameter("signup_prefix");
        this.forgetPasswordPage = filterConfig.getInitParameter("forgetPassword_page");
        this.forgetPasswordAction = filterConfig.getInitParameter("forgetPassword_action");
        this.changePasswordAction = filterConfig.getInitParameter("changePassword_action");
        
        log = Logger.getLogger(UserAuthenticationFilter.class);
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(
        ServletRequest req,
        ServletResponse res,
        FilterChain fChain)
        throws IOException, ServletException {
            
        if(loginPage == null || loginAction == null) {
            log.fatal("Login page not configured. Add this parameter to the " +
                    "web.xml file.");
            return;
        }
        
        if (!(req instanceof HttpServletRequest)) {
            log.warn("Request not of a servlet.");
            return;
        }
        
        // check if this is the Login page
        HttpServletRequest httpReq = (HttpServletRequest)req;
        String thisPage = httpReq.getServletPath();
        
        /*
        log.debug("Filtering page " + thisPage + " Context:" + httpReq.getContextPath() +
                " path info:" + httpReq.getPathInfo() + " uri:" + httpReq.getRequestURI() +
                " protocol:" + httpReq.getProtocol() + " servlet path:" + httpReq.getServletPath()+
                " url:" + httpReq.getRequestURL());
        */

        // first verify that this is being done in a secure channel
        if (Boolean.valueOf(Util.getSysProp("force_https")).
                booleanValue()) {
            String url = httpReq.getRequestURL().toString();
            if (url.substring(0, 5).equals("http:")) {
                String newURL = url.replaceFirst("http", "https");
                log.debug("Redirecting from " + httpReq.getRequestURL() + 
                        " to " + newURL + "[" + Boolean.valueOf(Util.getSysProp("force_https")).
                        booleanValue() +"]");
                ((HttpServletResponse)res).sendRedirect(newURL);
                return;
            }
        }
        
        // check that the process is not running, if so, kill the session
        File lock = new File(Util.getSysProp("login_lock"));
        if (lock.exists()) {
            HttpSession session = httpReq.getSession(false);
            if (session != null) {
                log.debug("Kicking user out due to process lock");
                try {
                    session.invalidate();
                } catch (IllegalStateException e) {
                    // the session was already invalid ..  sure, no problem
                }
            }
        }

        
        //log.debug("Login = " + loginPage);
        if (thisPage.compareTo(loginPage) != 0 && 
                thisPage.compareTo(loginAction) != 0 &&
                !thisPage.startsWith(signupPrefix) &&
                thisPage.compareTo(forgetPasswordPage) != 0 &&
                thisPage.compareTo(forgetPasswordAction) != 0 &&
                thisPage.compareTo(changePasswordAction) != 0) {
            // then you need a session
            HttpSession session = httpReq.getSession(false);
            if (session == null) {
                log.info("Session not present accessing " + thisPage);
                // TODO add a 'you session might have timed out' message
                ((HttpServletResponse)res).sendRedirect(httpReq.getContextPath()+loginPage);
            } else {
                if (session.getAttribute(Constants.SESSION_LOGGED_USER_ID) == null) {
                    log.warn("Session exists but without user.");
                    ((HttpServletResponse)res).sendRedirect(
                            httpReq.getContextPath()+loginPage);
                } else {
                    //log.debug("Session is good:" + session.getAttribute(Constants.SESSION_USER_ID_KEY));
                    fChain.doFilter(req, res);
                }
            }
        } else {
            // it's the login page
            //log.debug("This is the login page/action.");
            fChain.doFilter(req, res);
        }

    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        loginPage = null;
    }

}
