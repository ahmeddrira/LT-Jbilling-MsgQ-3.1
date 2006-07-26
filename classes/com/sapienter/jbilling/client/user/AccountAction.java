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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sapienter.jbilling.client.util.Constants;

/*
 * This is to avoid a landing page for the account.
 * It only puts in the session some values so the user edit and contact
 * edit pick up the current user. 
 */
public class AccountAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        // set up the variable to allow for user edit (change password)
        session.setAttribute(Constants.SESSION_USER_ID, session.getAttribute(
                Constants.SESSION_LOGGED_USER_ID));

        // do the setup for the contact edit
        session.setAttribute(Constants.SESSION_CONTACT_USER_ID, session.
                getAttribute(Constants.SESSION_LOGGED_USER_ID));
        
        // it will go to the default 'account' page
        // this could be editContact or passwordChange
        // this is chaining actions, from this one it goes to the maintain action
        return mapping.findForward("passwordChange");        

    }
}
