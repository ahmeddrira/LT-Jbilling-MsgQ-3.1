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

/*
 * Created on Jan 18, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.util;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;

/**
 * Finds a preference. If no beanName is specified, the value of the
 * preference is redered to the output. Otherwise, a new bean 
 * in the page context is created.
 * 
 * @author emilc
 *
 * @jsp:tag name="getPreference"
 *          body-content="empty"
 */
public class GetPreferenceTag extends TagSupport {
    
    private Integer preferenceId = null;
    private String beanName = null;
    
    public int doStartTag() throws JspException {
        Logger log = Logger.getLogger(GetPreferenceTag.class);

        // pull some data from the session before making the call
        HttpSession session = pageContext.getSession();
        Integer entityId = (Integer) session.getAttribute(
                Constants.SESSION_ENTITY_ID_KEY);
        JspWriter out = pageContext.getOut();

        // these preferences can be called for every page, so it is
        // importante to cache
        String result = null;
        String sessionKey = null;
        if (preferenceId.equals(Constants.PREFERENCE_CSS_LOCATION)) {
            sessionKey = Constants.SESSION_CSS_LOCATION;
        } else if (preferenceId.equals(Constants.PREFERENCE_LOGO_LOCATION)) {
            sessionKey = Constants.SESSION_LOGO_LOCATION;
        }
        
        // some might not need any caching
        if (sessionKey != null) {
            result = (String) session.getAttribute(sessionKey);
        }
        
        if (result == null) {
            
            try {
                // get the the jndi factory
                JNDILookup EJBFactory = JNDILookup.getFactory(false);
                UserSessionHome userHome =
                        (UserSessionHome) EJBFactory.lookUpHome(
                        UserSessionHome.class,
                        UserSessionHome.JNDI_NAME);
                UserSession remoteUser = userHome.create();
                result = remoteUser.getEntityPreference(entityId, preferenceId);
                
                // update the cache is applicable
                if (sessionKey != null) {
                    session.setAttribute(sessionKey, result);
                }
            } catch (Exception e) {
                 log.error("Exception on getting the css url", e);
                 throw new JspException(e);
            }
        }
        
        try {
            if (beanName != null && beanName.length() > 0) {
                pageContext.setAttribute(beanName, result);
            } else {
                out.print(result);
            }
        } catch (IOException e) {
            log.error("Can't write!", e);
            throw new JspException(e);
        }
        
        return SKIP_BODY;
    }
    
    /**
     * @jsp:attribute required="true"
     *                rtexprvalue="true"
     *                type="java.lang.Integer"
     */
    public Integer getPreferenceId() {
        return preferenceId;
    }

    /**
     * @param preferenceId
     */
    public void setPreferenceId(Integer parameterId) {
        this.preferenceId = parameterId;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.String"
     */
    public String getBeanName() {
        return beanName;
    }
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
