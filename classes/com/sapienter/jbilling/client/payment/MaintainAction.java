/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.client.payment;

import java.io.IOException;
import java.util.Vector;

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
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.CustomerSession;
import com.sapienter.jbilling.interfaces.CustomerSessionHome;
import com.sapienter.jbilling.interfaces.InvoiceSession;
import com.sapienter.jbilling.interfaces.InvoiceSessionHome;
import com.sapienter.jbilling.interfaces.PaymentSession;
import com.sapienter.jbilling.interfaces.PaymentSessionHome;
import com.sapienter.jbilling.server.entity.InvoiceDTO;
import com.sapienter.jbilling.server.entity.PaymentDTO;
import com.sapienter.jbilling.server.notification.NotificationSessionBean;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.user.UserDTOEx;
import com.sapienter.jbilling.server.user.partner.db.Partner;
import com.sapienter.jbilling.server.user.partner.db.PartnerPayout;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.util.db.generated.Payment;

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
                PartnerPayout payout = null;
                Partner partner = null;
                if (request.getParameter("payout") != null && 
                        request.getParameter("payout").equals("yes")) {
                    isPayout = true;
                    payout = (PartnerPayout) session.getAttribute(
                            Constants.SESSION_PAYOUT_DTO);
                    partner = (Partner) session.
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
                        payout.setPayment(new Payment(paymentDto));
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
                        log.debug("sending payment. Id = " + paymentDto.getId() +
                                " refund " + paymentDto.getIsRefund());
                        if (paymentDto.getId() != null && 
                                paymentDto.getIsRefund().intValue() == 0) {
                            // it is an update
                            myRemoteSession.update((Integer) session.getAttribute(
                                    Constants.SESSION_LOGGED_USER_ID), paymentDto);
                        } else {
                            // it is a new payment
                            paymentDto.setId(myRemoteSession.applyPayment(
                                    paymentDto, invoiceId));
                            // I need to update the DTO, so the left bar can
                            // make the right decitions
                            if (invoiceId != null) {
                                Vector<Integer> invoices = new Vector<Integer>();
                                invoices.add(invoiceId);
                                paymentDto.setInvoiceIds(invoices);
                            } 
                        }
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
                    // the dto has to be refreshed, otherwise the 
                    // map to the invoice won't be there
                    session.setAttribute(Constants.SESSION_PAYMENT_DTO, 
                            myRemoteSession.getPayment(paymentDto.getId(), 
                                    (Integer) session.getAttribute(
                                            Constants.SESSION_LANGUAGE)));
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
                log.debug("my dto is " + dto);
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
                NotificationSessionBean notificationSession = (NotificationSessionBean) Context.getBean(
                        Context.Name.NOTIFICATION_SESSION);
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
            } else if (action.equals("unlink")) {
                Integer mapId = Integer.valueOf(request.getParameter("mapId"));
                myRemoteSession.removeInvoiceLink(mapId);
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage("payment.link.removalDone"));
                // make sure the payment now shows up updated
                session.setAttribute(Constants.SESSION_LIST_ID_SELECTED,
                        ((PaymentDTO) session.getAttribute(
                            Constants.SESSION_PAYMENT_DTO)).getId());
                session.removeAttribute(Constants.SESSION_PAYMENT_DTO);
                forward = "payment_setupView";
            } else if (action.equals("apply")) {
                // call the server to apply tha payment
                myRemoteSession.applyPayment(((PaymentDTO) session.getAttribute(
                        Constants.SESSION_PAYMENT_DTO)).getId(), 
                        ((InvoiceDTO) session.getAttribute(
                                Constants.SESSION_INVOICE_DTO)).getId());
                // show a 'done' message
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage("payment.link.applyDone"));
                // redirect to payment view, with the refreshed payment record
                session.setAttribute(Constants.SESSION_LIST_ID_SELECTED,
                        ((PaymentDTO) session.getAttribute(
                            Constants.SESSION_PAYMENT_DTO)).getId());
                session.removeAttribute(Constants.SESSION_PAYMENT_DTO);
                forward = "payment_setupView";
            } else {
            	PaymentCrudAction delegate = new PaymentCrudAction(myRemoteSession);
            	delegate.setServlet(getServlet());
            	retValue = delegate.execute(mapping, form, request, response);
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
