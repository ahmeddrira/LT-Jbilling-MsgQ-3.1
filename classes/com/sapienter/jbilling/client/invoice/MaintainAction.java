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

package com.sapienter.jbilling.client.invoice;

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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.InvoiceSession;
import com.sapienter.jbilling.interfaces.InvoiceSessionHome;
import com.sapienter.jbilling.interfaces.NotificationSession;
import com.sapienter.jbilling.interfaces.NotificationSessionHome;
import com.sapienter.jbilling.server.invoice.InvoiceDTOEx;
import com.sapienter.jbilling.server.user.UserDTOEx;

public class MaintainAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(MaintainAction.class);
        HttpSession session = request.getSession(false);
        ActionMessages messages = new ActionMessages();
        String forward = "invoice_view";

        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);

            InvoiceSessionHome invoiceHome =
                (InvoiceSessionHome) EJBFactory.lookUpHome(
                InvoiceSessionHome.class,
                InvoiceSessionHome.JNDI_NAME);
    
            InvoiceSession invoiceSession = invoiceHome.create();

            // I could call the GenericMaintainAction here, but since
            // there's no create/edit/update with invoices, it'd make 
            // things more complicated
            String action = request.getParameter("action");
            
            Integer invoiceId;
            if (request.getParameter("id") != null) {
                // this is being called from anywhere, to check out a invoice
                invoiceId = Integer.valueOf(request.getParameter("id"));
                session.setAttribute(Constants.SESSION_LIST_ID_SELECTED,
                        invoiceId);
            } else if (request.getParameter("latest") != null) {
                UserDTOEx user = (UserDTOEx) session.getAttribute(
                        Constants.SESSION_USER_DTO);
                invoiceId = user.getLastInvoiceId();
                if (invoiceId == null) {
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("invoice.error.noInvoice"));
                    forward = "no_invoice";
                }
            } else {
                // this is called from the list of invoices
                invoiceId = (Integer) session.getAttribute(
                        Constants.SESSION_LIST_ID_SELECTED);
            }
            
            if (action != null && action.equals("notify")) {
                NotificationSessionHome NotificationHome =
                    (NotificationSessionHome) EJBFactory.lookUpHome(
                    NotificationSessionHome.class,
                    NotificationSessionHome.JNDI_NAME);
        
                NotificationSession notificationSession = 
                        NotificationHome.create();
                Boolean result = notificationSession.emailInvoice(invoiceId);
                String field;
                if (result.booleanValue()) {
                    field = "email.notify.ok";
                } else {
                    field = "email.notify.error"; 
                }
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage(field));
            } if (action != null && action.equals("delete")) {
                Integer executorId = (Integer) session.getAttribute(
                        Constants.SESSION_LOGGED_USER_ID);
                invoiceSession.delete(invoiceId, executorId);
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage("invoice.delete.done"));
                // remove the cached list so the deleted invoice doesn't 
                // show up
                session.removeAttribute(Constants.SESSION_LIST_KEY +
                        Constants.LIST_TYPE_INVOICE_GRAL);
                forward = "invoice_list";
            } else if (invoiceId != null ) {
            
                InvoiceDTOEx dto = invoiceSession.getInvoiceEx(invoiceId,
                        (Integer) session.getAttribute(
                            Constants.SESSION_LANGUAGE));
                // just put the dto in the session for viewing
                session.setAttribute(Constants.SESSION_INVOICE_DTO, dto);
            }
            saveMessages(request, messages);
            log.debug("Forward to " + forward);
            return mapping.findForward(forward);            
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        return mapping.findForward("error");
    }
  
}
