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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.FormHelper;
import com.sapienter.jbilling.server.user.Menu;
import com.sapienter.jbilling.server.user.UserDTOEx;

/**
 * @author emilc
 *
 * This filter applies to all JSPs files. It verifies that a session with 
 * a username exists, which means that the user has logged in.
 * It takes as a parameter from the descriptor the name of the Login servlet,
 * because it's the only one that shouldn't be filtered and is the one 
 * where those not authenticated will be redirected.
 */
public final class MenuSelectionFilter implements Filter {
    
    private Logger log = null;

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        log = Logger.getLogger(MenuSelectionFilter.class);
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain fChain) throws IOException, ServletException {
        
        if (!(req instanceof HttpServletRequest)) {
            log.warn("Request not of a servlet.");
            return;
        }
        
        HttpServletRequest httpReq = (HttpServletRequest)req;
        String optionStr = req.getParameter("menu_option");
        if (optionStr != null) {
            //log.debug("option selected " + optionStr);
            try {
                Integer option = Integer.valueOf(optionStr);
                HttpSession session = httpReq.getSession(false); // this is the right way to do it
                Menu menu = ((UserDTOEx) session.getAttribute(
                        Constants.SESSION_USER_DTO)).getMenu();
                
                if (menu.selectOption(option)) {
                    log.debug("Cleaning up the session");
                    FormHelper.cleanUpSession(session);
                    /*
                    java.util.Enumeration entries = session.getAttributeNames();
                    for (String entry = (String)entries.nextElement(); entries.hasMoreElements();
                        entry = (String)entries.nextElement()) {
                        log.debug("Session entry:[" + entry + "]");        
                    }
                    */
                }
            } catch (Exception e) {
                log.error("exception selection an option:" + optionStr, e);
                throw new ServletException(e);
            }
        }
        
        fChain.doFilter(req, res);

    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        log = null;
    }

}
