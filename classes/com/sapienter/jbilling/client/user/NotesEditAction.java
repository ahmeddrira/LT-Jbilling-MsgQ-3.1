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
 * Created on Jul 16, 2004
 *
 */
package com.sapienter.jbilling.client.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.user.UserDTOEx;

/**
 * @author Emil
 * This will simply update the notes filed for the customer record.
 * If the trimmed field is empty, it will send a null,
 */
public class NotesEditAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Logger log = Logger.getLogger(NotesEditAction.class);
        HttpSession session = request.getSession(false);
        String forward = "error";
        ActionErrors errors = new ActionErrors();
        String action = request.getParameter("action");
    	try {
    		UserDTOEx user = (UserDTOEx) session.getAttribute(
	        		Constants.SESSION_CUSTOMER_DTO);
    		
    		if (action.equals("edit")) {
	            JNDILookup EJBFactory = JNDILookup.getFactory(false);
	            UserSessionHome userHome =
	                    (UserSessionHome) EJBFactory.lookUpHome(
	                    UserSessionHome.class,
	                    UserSessionHome.JNDI_NAME);
	            UserSession userSession = userHome.create();
	            DynaActionForm myForm = (DynaActionForm) form;
	            
	            String notes = (String) myForm.get("notes");
	            notes = notes.trim();
	            if (notes.length() > 1000) {
	            	notes.substring(0,1000);
	            } else if (notes.length() == 0) {
	            	notes = null;
	            }

	            // parse new lines
	            if (notes != null) {
	            	notes = notes.replaceAll("\r\n", "<br/>");
	            }
	            Integer userId = (Integer) session.getAttribute(
	            		Constants.SESSION_USER_ID);
	            userSession.setCustomerNotes(userId, notes);

	            // refresh the object in the session
	            user.getCustomerDto().setNotes(notes);

	            forward = "customer_view";
    		} else if (action.equals("setup")) {
    	        ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request,
    	                servlet.getServletContext());
    	        DynaActionForm myForm = (DynaActionForm) RequestUtils.createActionForm(
    	                request, mapping, moduleConfig, servlet);
    	        
    	        String notes = user.getCustomerDto().getNotes();
    	        if (notes != null) {
    	        	notes = notes.replaceAll("<br/>", "\r\n");
    	        }
        		myForm.set("notes", notes);
        		session.setAttribute("notes", myForm);
        		forward = "notes_edit";
    		}
        } catch (Exception e) {
        	log.error("Exception in action", e);
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("all.internal"));
        }
        
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        
        return mapping.findForward(forward);
    }
}
