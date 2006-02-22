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

package com.sapienter.jbilling.client.report;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.ReportSession;
import com.sapienter.jbilling.interfaces.ReportSessionHome;

public class ListAction extends Action {

    Logger log = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        log = Logger.getLogger(ListAction.class);
        ActionErrors errors = new ActionErrors();        
        HttpSession session = request.getSession();
        
        /*
         * Use this same action to get the list of report for a type
         */
        
        String typeId = request.getParameter("type");
        if (typeId != null) {
            try {
                session.setAttribute(Constants.SESSION_REPORT_LIST, 
                        getListByType(Integer.valueOf(typeId)));
                return mapping.findForward("listType");
            } catch (Exception e) {
                log.error("Exception:", e);
                return mapping.findForward("error");
            }
        } else {
            throw new ServletException("typeId missing for list of reports");
        }
        
    }
    
    private Collection getListByType(Integer type) 
            throws NamingException, CreateException, SessionInternalError,
                RemoteException {
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        ReportSessionHome reportHome =
               (ReportSessionHome) EJBFactory.lookUpHome(
                ReportSessionHome.class,
                ReportSessionHome.JNDI_NAME);

        ReportSession myRemoteSession = reportHome.create();

        return myRemoteSession.getListByType(type);

    }
}
