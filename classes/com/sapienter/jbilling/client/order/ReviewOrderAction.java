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

/*
 * Created on 17-Mar-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.order;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.naming.NamingException;
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
import org.apache.struts.validator.Resources;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.FormHelper;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.CustomerSession;
import com.sapienter.jbilling.interfaces.CustomerSessionHome;
import com.sapienter.jbilling.interfaces.NewOrderSession;
import com.sapienter.jbilling.interfaces.NewOrderSessionHome;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.order.db.OrderLineDTO;

/**
 * @author Emil
 */
public class ReviewOrderAction extends Action {
    static Logger log = null;
    private HttpSession session;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        log = Logger.getLogger(ReviewOrderAction.class);
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        session = request.getSession(false);
        OrderDTO newOrder = (OrderDTO) session.getAttribute(
                Constants.SESSION_ORDER_SUMMARY);
        String forward = null;

        String action = request.getParameter("action");
        log.debug("Review. action = " + action);

        NewOrderSession remoteSession =
                (NewOrderSession) session.getAttribute(
                Constants.SESSION_ORDER_SESSION_KEY);
        if (remoteSession == null && !action.equals("read")) {
            try {
                JNDILookup EJBFactory = JNDILookup.getFactory(false);
                NewOrderSessionHome sHome =
                        (NewOrderSessionHome) EJBFactory.lookUpHome(
                        NewOrderSessionHome.class,
                        NewOrderSessionHome.JNDI_NAME);
                remoteSession = sHome.create(newOrder, (Integer) session.
                        getAttribute(Constants.SESSION_LANGUAGE));
            } catch (Exception e) {}
        }

        try {
            if (action.equals("setup")) {
                // get the processing of this order, so the taxes, etc, show up
                newOrder = remoteSession.recalculate(newOrder,
                        (Integer) session.getAttribute(
                            Constants.SESSION_ENTITY_ID_KEY));
                // initializing the wraping form to allow displaying and
                // updating the dto
                Hashtable hashlines = new Hashtable();
                ((NewOrderDTOForm) form).setOrderLines(hashlines);
                // the price has to be formated i18n
                for(OrderLineDTO line : newOrder.getLines()) {
                	if (line.getDeleted() == 0) {
	                    line.setPriceStr(FormHelper.float2string(
	                            line.getPrice(), session));
	                    hashlines.put(line.getItemId(), line);
                	}
                }
                
                log.debug("The form has been set");
                forward = "show";
            } else if (action.equals("cancel")) {
                // garbage collect the stuff from the session
                FormHelper.cleanUpSession(session);
                // cancel the remote session
                try {
                    remoteSession.remove();
                } catch (Exception e) {
                    log.error("Exception when removing the remote session", e);
                }
                forward ="list";
                messages.add(ActionMessages.GLOBAL_MESSAGE, 
                        new ActionMessage("order.canceled"));
                
            } else if (action.equals("process")) {
                // this means that the form has to be either recalculated
                // or the order is confirmed

                if (request.getParameter("recalc") != null) {
                    log.debug("recaclculate!");
                    // the price has to be formated i18n
                    for(OrderLineDTO line : newOrder.getLines()) {
                        if (!line.getEditable().booleanValue()) //probalby a tax
                            continue;
                        line.setPrice(FormHelper.string2float(
                                line.getPriceStr(), session));
                        log.debug("line = " + line);
                        if (line.getPrice() == null) {
                            String field = Resources.getMessage(request, 
                                    "order.line.prompt.price");
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("errors.float", field));
                            saveErrors(request, errors);
                            forward = "show";
                            break;
                        }
                    }

                    if (errors.isEmpty()) {
                        newOrder = remoteSession.recalculate(newOrder,
                                (Integer) session.getAttribute(
                                    Constants.SESSION_ENTITY_ID_KEY));
                        forward = "show";
                    }
                } else if (request.getParameter("commit") != null) {
                    log.debug("commit !");
                    // create or update the order record
                    Integer orderId = remoteSession.createUpdate(
                            (Integer) session.getAttribute(
                            Constants.SESSION_ENTITY_ID_KEY),
                            (Integer) session.getAttribute(
                            Constants.SESSION_LOGGED_USER_ID), newOrder);
                    // garbage collect the stuff from the session
                    FormHelper.cleanUpSession(session);
                    // cancel the remote session
                    try {
                        remoteSession.remove();
                    } catch (Exception e) {
                        log.error("Exception when removing the remote session",
                                e);
                    }
                    
                    if (orderId != null) {
                        // we are forwarding to the view page, so the order dto
                        // has to be present
                        putOrderInSession(orderId, request);
                        forward = "view";
                        messages.add(ActionMessages.GLOBAL_MESSAGE, 
                                new ActionMessage("order.completed.message"));
                        messages.add(ActionMessages.GLOBAL_MESSAGE, 
                                new ActionMessage("order.completed.id", orderId));
                    } else {
                        forward = "list";
                        messages.add(ActionMessages.GLOBAL_MESSAGE, 
                                new ActionMessage("order.updated.message"));
                    }
                }
            } else if (action.equals("read")) {
                // get the order information from the server and make it
                // available to in the session
                JNDILookup EJBFactory = JNDILookup.getFactory(false);
                Integer orderId = (request.getParameter("id") == null) 
                        ? (Integer) session.getAttribute(
                            Constants.SESSION_LIST_ID_SELECTED)
                        : Integer.valueOf(request.getParameter("id"));
                
                OrderDTO orderDto = putOrderInSession(orderId, request);
                        
                NewOrderDTOForm dto = new NewOrderDTOForm();
                for (Iterator it = orderDto.getLines().iterator();
                        it.hasNext();) {
                    OrderLineDTO line = (OrderLineDTO) it.next();
                    // gst and other 'automatic' lines dont have item id ...
                    if (line.getItemId() != null && line.getDeleted() == 0) {
                        dto.setOrderLine(line.getItemId().toString(), line); 
                    } 
                }
                
                session.setAttribute("orderDTOForm", dto);
                session.setAttribute(Constants.SESSION_ORDER_SUMMARY, orderDto);
                
                // the user has to be also in the session
                session.setAttribute(Constants.SESSION_USER_ID, 
                        orderDto.getUser().getUserId());
                CustomerSessionHome userHome =
                        (CustomerSessionHome) EJBFactory.lookUpHome(
                        CustomerSessionHome.class,
                        CustomerSessionHome.JNDI_NAME);
    
                CustomerSession userSession = userHome.create();
                // this is needed for the customer display
                session.setAttribute(Constants.SESSION_CUSTOMER_CONTACT_DTO,
                        userSession.getPrimaryContactDTO(orderDto.getUser().getUserId()));
                
                // and the order form bean with the period/active dates info
                // this has to be in a different action, so I have to chain :(
                forward = "setup";
            } else {
                log.error(
                    "ReviewAction called with unsupported action: " + action);
                errors.add(
                    ActionErrors.GLOBAL_ERROR,
                    new ActionError("all.internal"));
                saveErrors(request, errors);
                forward = "show";
            }
        } catch (Exception e) {
            log.error("Error processing a new order", e);
            errors.add(
                ActionErrors.GLOBAL_ERROR,
                new ActionError("all.internal"));
            
            forward = "show";
        }

        saveMessages(request, messages);   
        saveErrors(request, errors);
        return mapping.findForward(forward);

    }
    
    private OrderDTO putOrderInSession(Integer orderId,
            HttpServletRequest request) 
            throws SessionInternalError, NamingException, RemoteException,
                CreateException {
        JNDILookup EJBFactory = JNDILookup.getFactory(false);
        
        OrderSessionHome orderHome =
                (OrderSessionHome) EJBFactory.lookUpHome(
                OrderSessionHome.class,
                OrderSessionHome.JNDI_NAME);
                
        OrderSession order = orderHome.create();

        OrderDTO orderDto = order.getOrderEx(orderId,
                (Integer) session.getAttribute(
                    Constants.SESSION_LANGUAGE));
        // I'll need this information later
        session.setAttribute(Constants.SESSION_ORDER_DTO,
                orderDto);
                
        return orderDto;
    }

}
