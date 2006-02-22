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

package com.sapienter.jbilling.client.process;

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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.BillingProcessSession;
import com.sapienter.jbilling.interfaces.BillingProcessSessionHome;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.BillingProcessConfigurationDTO;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.invoice.InvoiceDTOEx;
import com.sapienter.jbilling.server.order.OrderDTOEx;
import com.sapienter.jbilling.server.process.BillingProcessDTOEx;

public class MaintainAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(MaintainAction.class);
        HttpSession session = request.getSession(false);
        String forward = "error";
        ActionMessages messages = new ActionMessages();
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            BillingProcessSessionHome processHome =
                    (BillingProcessSessionHome) EJBFactory.lookUpHome(
                    BillingProcessSessionHome.class,
                    BillingProcessSessionHome.JNDI_NAME);
        
            BillingProcessSession processSession = processHome.create();
            
            String action = request.getParameter("action");
            Integer entityId = (Integer) session.
                    getAttribute(Constants.SESSION_ENTITY_ID_KEY);
            
            if (action.equals("newInvoice") || 
                    action.equals("applyToInvoice")) {
                OrderDTOEx order = (OrderDTOEx) session.getAttribute(
                        Constants.SESSION_ORDER_DTO);
                if (!order.getStatusId().equals(
                        Constants.ORDER_STATUS_ACTIVE)) {
                    ActionErrors errors = new ActionErrors();
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "order.error.status"));
                    saveErrors(request, errors);
                    return mapping.findForward("order_view");       
                }
            }

            if (action.equals("view")) {
                Integer processId = (Integer) session.getAttribute(
                        Constants.SESSION_LIST_ID_SELECTED);

                // by default, we show the last for this entity
                if (processId == null || request.getParameter("latest") != null) {
                    processId = processSession.getLast(entityId);
                }
                // it could be that an entity just signed up and has no processes
                if (processId != null) {
                    BillingProcessDTOEx dto = processSession.getDto(processId,
                            (Integer) session.getAttribute(
                                Constants.SESSION_LANGUAGE));
                    session.setAttribute(Constants.SESSION_PROCESS_DTO, dto);
                    forward = "process_view";
                } else {
                    forward = "process_list";
                }
            } else if (action.equals("review")) {
                BillingProcessDTOEx dto = processSession.getReviewDto(entityId,
                        (Integer) session.getAttribute(
                            Constants.SESSION_LANGUAGE));
                BillingProcessConfigurationDTO configDto = processSession.
                        getConfigurationDto(entityId);
                if (dto != null) {
                    session.setAttribute(Constants.SESSION_PROCESS_DTO, dto);
                } else {
                    
                    session.removeAttribute(Constants.SESSION_PROCESS_DTO);
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("process.noReview"));
                }
                session.setAttribute(
                        Constants.SESSION_PROCESS_CONFIGURATION_DTO, 
                        configDto);
                forward = "process_review";
            } else if (action.equals("approval")) {
                boolean flag;
                if (request.getParameter("yes") != null) {
                    flag = true;
                } else {
                    flag = false;
                }
                BillingProcessConfigurationDTO configDto = 
                        processSession.setReviewApproval((Integer) 
                        session.getAttribute(Constants.SESSION_LOGGED_USER_ID), 
                            entityId, new Boolean(flag));
                session.setAttribute(
                        Constants.SESSION_PROCESS_CONFIGURATION_DTO, 
                        configDto);
                forward = "process_review";
            } else if (action.equals("newInvoice")) {
                // an invoice is being generated from the gui, directly from
                // an order.
                // I need an order first
                OrderDTOEx order = (OrderDTOEx) session.getAttribute(
                        Constants.SESSION_ORDER_DTO);
                if (order == null) {
                    throw new SessionInternalError("an order dto has to be " +
                            "present to generate an invoice");
                }
                Integer languageId = (Integer) session.getAttribute(
                        Constants.SESSION_LANGUAGE);
                InvoiceDTOEx invoice = processSession.generateInvoice(
                        order.getId(), null, languageId);                
                if (invoice != null) {
                    // get the user in the session
                    UserSessionHome userHome =
                            (UserSessionHome) EJBFactory.lookUpHome(
                            UserSessionHome.class,
                            UserSessionHome.JNDI_NAME);
        
                    UserSession myRemoteSession = userHome.create();
                    session.setAttribute(Constants.SESSION_CUSTOMER_CONTACT_DTO,
                            myRemoteSession.getPrimaryContactDTO(
                                invoice.getUserId()));
                    // the user id too
                    session.setAttribute(Constants.SESSION_USER_ID,
                            invoice.getUserId());
                    // and this invoice
                    session.setAttribute(Constants.SESSION_INVOICE_DTO,
                            invoice);
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("process.invoiceGenerated", 
                                    invoice.getId()));
                    forward = "payment_create";
                } else {
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("process.invoiceNotGenerated"));
                    forward = "order_view";
                }
            } else if (action.equals("applyToInvoice")) {
                OrderDTOEx order = (OrderDTOEx) session.getAttribute(
                        Constants.SESSION_ORDER_DTO);
                InvoiceDTO invoice = (InvoiceDTO) session.getAttribute(
                        Constants.SESSION_INVOICE_DTO);
                
                if (order == null || invoice == null) {
                    throw new SessionInternalError("an order dto and " +
                            " invoice dto have to be " +
                            "present to apply to an invoice");
                }
                
                Integer languageId = (Integer) session.getAttribute(
                        Constants.SESSION_LANGUAGE);
                InvoiceDTOEx invoiceEx  = processSession.generateInvoice(
                        order.getId(), invoice.getId(), languageId);
                
                if (invoiceEx == null) {
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("process.orderNotApplied",
                                    order.getId()));
                    
                    forward = "order_view";
                } else {
                    // the new invoice has to be in the session for display
                    session.setAttribute(Constants.SESSION_INVOICE_DTO, 
                            invoiceEx);
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("process.orderApplied", 
                                    order.getId(), invoiceEx.getId()));
                    forward = "invoice_view";
                }
            }
            
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        saveMessages(request, messages);
        
        return mapping.findForward(forward);            
    }
  
}
