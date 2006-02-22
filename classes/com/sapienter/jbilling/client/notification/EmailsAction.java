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

package com.sapienter.jbilling.client.notification;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.NotificationSession;
import com.sapienter.jbilling.interfaces.NotificationSessionHome;

public class EmailsAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(MaintainAction.class);
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            NotificationSessionHome notificationHome =
                    (NotificationSessionHome) EJBFactory.lookUpHome(
                    NotificationSessionHome.class,
                    NotificationSessionHome.JNDI_NAME);
            NotificationSession notificationSession = notificationHome.create();
        
            DynaActionForm myForm = (DynaActionForm) form;
            String separator = (String) myForm.get("separator");
            HttpSession session = request.getSession(false);
            String content = notificationSession.getEmails((Integer) 
                    session.getAttribute(Constants.SESSION_ENTITY_ID_KEY), 
                        separator);
            
            myForm.set("content", content);
            session.setAttribute("notificationEmails", myForm);
                    
            return mapping.findForward("view");            
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        return mapping.findForward("error");
    }
    
    protected boolean isCancelled(HttpServletRequest request) {
        return !request.getParameter("mode").equals("setup");
    }     
}
