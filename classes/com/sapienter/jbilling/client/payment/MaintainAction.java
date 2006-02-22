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

package com.sapienter.jbilling.client.payment;

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
import com.sapienter.jbilling.client.util.GenericMaintainAction;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.CustomerSession;
import com.sapienter.jbilling.interfaces.CustomerSessionHome;
import com.sapienter.jbilling.interfaces.InvoiceSession;
import com.sapienter.jbilling.interfaces.InvoiceSessionHome;
import com.sapienter.jbilling.interfaces.NotificationSession;
import com.sapienter.jbilling.interfaces.NotificationSessionHome;
import com.sapienter.jbilling.interfaces.PaymentSession;
import com.sapienter.jbilling.interfaces.PaymentSessionHome;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.user.PartnerDTOEx;
import com.sapienter.jbilling.server.user.PartnerPayoutDTOEx;
import com.sapienter.jbilling.server.user.UserDTOEx;

public class MaintainAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(MaintainAction.class);
        String forward = null;
        ActionForward retValue = null;
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            PaymentSessionHome paymentHome =
                    (PaymentSessionHome) EJBFactory.lookUpHome(
                    PaymentSessionHome.class,
                    PaymentSessionHome.JNDI_NAME);

            PaymentSession myRemoteSession = paymentHome.create();
            
            /*
             * Because of the review step, the payment has some actions that
             * so far makes no sense to dump them in the generic action
             */
            String action = request.getParameter("action");
            HttpSession session = request.getSession(false);
            ActionMessages messages = new ActionMessages();

            String key;    
            key = session.getAttribute("jsp_is_refund") == null
                    ? "payment."
                    : "refund.";    
            // get the payment/refund information
            String sessionKey;
            if (key.equals("payment.")) {
                sessionKey = Constants.SESSION_PAYMENT_DTO;
            } else {
                sessionKey = Constants.SESSION_PAYMENT_DTO_REFUND;
            }
            
            if (action.equals("send")) {
                
                PaymentDTOEx paymentDto  = (PaymentDTOEx) session.getAttribute(
                        sessionKey);

                if (paymentDto == null) {
                    log.error("dto can't be null when sending");
                    throw new ServletException("paymentDTO is null");
                }
                // get the invoice, it might not be there
                Integer invoiceId = null;
                InvoiceDTO invoice = (InvoiceDTO) session.getAttribute(
                        Constants.SESSION_INVOICE_DTO);
                if (invoice != null) {
                    invoiceId = invoice.getId();
                }
                
                boolean isPayout = false;
                PartnerPayoutDTOEx payout = null;
                PartnerDTOEx partner = null;
                if (request.getParameter("payout") != null && 
                        request.getParameter("payout").equals("yes")) {
                    isPayout = true;
                    payout = (PartnerPayoutDTOEx) session.getAttribute(
                            Constants.SESSION_PAYOUT_DTO);
                    partner = (PartnerDTOEx) session.
                            getAttribute(Constants.SESSION_PARTNER_DTO);                
                }
                
                if (((Boolean) session.getAttribute("tmp_process_now")).
                        booleanValue()) {
                    Integer result; 
                    if (!isPayout) {
                        result = myRemoteSession.processAndUpdateInvoice(
                                paymentDto, invoiceId, (Integer) session.
                                    getAttribute(Constants.SESSION_ENTITY_ID_KEY));
                    } else {
                        
                        result = myRemoteSession.processPayout(paymentDto, 
                                payout.getStartingDate(), 
                                payout.getEndingDate(), partner.getId(),
                                new Boolean(true));
                        payout.setPayment(paymentDto);
                    }
                    
                    if (result == null) {
                        key = key + "no_result";
                    } else if (result.equals(Constants.RESULT_OK)) {
                        key = key + "result.approved";
                    } else if (result.equals(Constants.RESULT_FAIL)) {
                        key = key + "result.rejected";
                    } else if (result.equals(Constants.RESULT_UNAVAILABLE)) {
                        key = key + "result.unavailable";
                    } else {
                        key = "all.internal";
                        log.error("Unsupported result from server:" + result);
                    }
                        
                } else {
                    if (!isPayout) {
                        myRemoteSession.applyPayment(paymentDto, invoiceId);
                    } else {
                        myRemoteSession.processPayout(paymentDto, 
                                payout.getStartingDate(), 
                                payout.getEndingDate(), partner.getId(),
                                new Boolean(false));
                    }
                    key = key + "enter.success";
                }
                
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage(key));
                if (!isPayout) {
                    forward = "payment_view";
                } else {
                    forward = "payout_view";
                }
            } else if (action.equals("last_invoice")) {
                forward = "no_invoice";
                // make sure the logged user shows up as the user for the payment
                session.setAttribute(Constants.SESSION_USER_ID, 
                        session.getAttribute(
                                Constants.SESSION_LOGGED_USER_ID));
                // now find out which is the latest invoice and make it available
                UserDTOEx user = (UserDTOEx) session.getAttribute(
                        Constants.SESSION_USER_DTO);
                Integer invoiceId = user.getLastInvoiceId();
                if (invoiceId != null) {
                    InvoiceSessionHome invoiceHome =
                        (InvoiceSessionHome) EJBFactory.lookUpHome(
                        InvoiceSessionHome.class,
                        InvoiceSessionHome.JNDI_NAME);
        
                    InvoiceSession invoiceSession = invoiceHome.create();
                    InvoiceDTO invoice = invoiceSession.getInvoice(
                            invoiceId);
                    if (invoice.getToProcess().intValue() == 1) {
                        session.setAttribute(Constants.SESSION_INVOICE_DTO,
                                invoice);
                        //this will chain the action, but it's effective
                        forward = "last_invoice";
                    }
                }
                
                if (forward.equals("no_invoice")) {
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("payment.error.noInvoice"));
                }
            } else if (action.equals("current_invoice")) {
                session.setAttribute(Constants.SESSION_USER_ID, 
                        session.getAttribute(
                                Constants.SESSION_LOGGED_USER_ID));
                // the invoice dto is already in the sesssion .. piece of cake
                forward = "last_invoice";
            } else if (action.equals("cancel")) {
                session.removeAttribute(sessionKey);
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage("payment.enter.cancel"));
                forward = "payment_list";
            } else if (action.equals("view")) {
                // this is called when a payment is selected from the list
                // for a read-only view
                
                Integer paymentId;
                if (request.getParameter("id") != null) {
                    // this is being called from anywhere, to check out a payment
                    paymentId = Integer.valueOf(request.getParameter("id"));
                } else {
                    // this is called from the list of payments
                    paymentId = (Integer) session.getAttribute(
                            Constants.SESSION_LIST_ID_SELECTED);
                }
                Integer languageId = (Integer) session.getAttribute(
                        Constants.SESSION_LANGUAGE);
                PaymentDTOEx dto = ((PaymentSession) myRemoteSession).getPayment(
                        paymentId, languageId);
                if (dto.getIsRefund().intValue() == 1) {
                    session.setAttribute(Constants.SESSION_PAYMENT_DTO_REFUND, 
                            dto);
                    if (dto.getPayment() != null) {
                        session.setAttribute(Constants.SESSION_PAYMENT_DTO,
                                ((PaymentSession) myRemoteSession).getPayment(
                                    dto.getPayment().getId(), languageId));                            
                    } else {
                        session.removeAttribute(Constants.SESSION_PAYMENT_DTO);
                    }
                   
                } else {
                    session.setAttribute(Constants.SESSION_PAYMENT_DTO, dto);
                }
            
                // now include the invoice and customer dto of this payment
                try {
                    // payments get the linked invoices
                    if (dto.getIsRefund().intValue() == 0 ) {
                        InvoiceSessionHome invoiceHome =
                                (InvoiceSessionHome) EJBFactory.lookUpHome(
                                InvoiceSessionHome.class,
                                InvoiceSessionHome.JNDI_NAME);
        
                        InvoiceSession invoiceSession = invoiceHome.create();
                        // we'll display only the first invoice now, since from the
                        // gui only one invoice per payment is now being supported.
                        if (dto.getInvoiceIds().size() > 0) {
                            InvoiceDTO invoices[] = new InvoiceDTO[
                                    dto.getInvoiceIds().size()];
                            for (int f = 0; f < invoices.length; f++) {
                                invoices[f] = invoiceSession.getInvoice((Integer)
                                        dto.getInvoiceIds().get(f));
                            }
                            session.setAttribute("jsp_linked_invoices", invoices);
                        } else {
                            // otherwise one from a prvious selection will show up
                            session.removeAttribute("jsp_linked_invoices");
                        }
                    } else { // refunds get another payment
                    }
                                    
                    // now the user
                    session.setAttribute(Constants.SESSION_USER_ID, 
                            dto.getUserId());
                    CustomerSessionHome userHome =
                            (CustomerSessionHome) EJBFactory.lookUpHome(
                            CustomerSessionHome.class,
                            CustomerSessionHome.JNDI_NAME);
    
                    CustomerSession userSession = userHome.create();
                    session.setAttribute(Constants.SESSION_CUSTOMER_CONTACT_DTO,
                            userSession.getPrimaryContactDTO(dto.getUserId()));
                
                                
                } catch (Exception e) {
                    throw new SessionInternalError(e);
                } 

                forward = "payment_view";
            } else if (action.equals("notify")) {
                NotificationSessionHome NotificationHome =
                    (NotificationSessionHome) EJBFactory.lookUpHome(
                    NotificationSessionHome.class,
                    NotificationSessionHome.JNDI_NAME);
        
                NotificationSession notificationSession = 
                        NotificationHome.create();
                Integer paymentId = Integer.valueOf(
                        request.getParameter("id"));
                Boolean result = notificationSession.emailPayment(paymentId);
                String field;
                if (result.booleanValue()) {
                    field = "email.notify.ok";
                } else {
                    field = "email.notify.error"; 
                }
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage(field));
                forward = "payment_view";
            } else {
                GenericMaintainAction gma = new GenericMaintainAction(mapping,
                        form, request, response, servlet, myRemoteSession, 
                        "payment");
                        
                retValue = gma.process();
            }      
            
            saveMessages(request, messages);      
        } catch (Exception e) {
            log.error("Exception ", e);
            retValue = mapping.findForward("error");
            forward = null;
        }
        
        if (forward != null) {
            retValue = mapping.findForward(forward);
        }
        return retValue; 
    }
  
}
