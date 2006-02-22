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
 * Created on Mar 29, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.user;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.user.UserDTOEx;

/**
 * This tag simply goes to the server to look for the message that
 * goes with this user's status.
 * @author emilc
 *
 * @jsp:tag name="welcome"
 *          body-content="JSP"
 */
public class WelcomeTag extends TagSupport {

    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        // gather some information about the logged user
        Integer entityId = (Integer) session.getAttribute(
                Constants.SESSION_ENTITY_ID_KEY);
        Integer languageId = (Integer) session.getAttribute(
                Constants.SESSION_LANGUAGE);
        Integer statusId = ((UserDTOEx) session.getAttribute(
                Constants.SESSION_USER_DTO)).getStatusId();
        JspWriter out = pageContext.getOut();
        try {
            // get the order session bean
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserSessionHome orderHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            UserSession remoteUser = orderHome.create();
            out.print(remoteUser.getWelcomeMessage(
                    entityId, languageId, statusId));
        } catch (Exception e) {
            throw new JspException(e);
        }        
        return SKIP_BODY;
    }
}
