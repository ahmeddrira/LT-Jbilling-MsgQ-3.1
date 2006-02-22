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

package com.sapienter.jbilling.client.order;

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

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.client.util.GenericMaintainAction;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.NewOrderSession;
import com.sapienter.jbilling.interfaces.NewOrderSessionHome;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.entity.OrderDTO;
import com.sapienter.jbilling.server.order.NewOrderDTO;

public class MaintainAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(MaintainAction.class);
        NewOrderDTO summary;
        NewOrderSession remoteSession;
        HttpSession session = request.getSession(false);
        Integer languageId = (Integer) session.getAttribute(
                Constants.SESSION_LANGUAGE);
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            
            if (request.getParameter("action").equals("view")) {
                OrderSessionHome orderHome =
                        (OrderSessionHome) EJBFactory.lookUpHome(
                        OrderSessionHome.class,
                        OrderSessionHome.JNDI_NAME);
                
                OrderSession remoteOrder = orderHome.create();
                Integer orderId = (request.getParameter("id") == null) 
                        ? (Integer) session.getAttribute(
                            Constants.SESSION_LIST_ID_SELECTED)
                        : Integer.valueOf(request.getParameter("id"));

                session.setAttribute(Constants.SESSION_ORDER_DTO, 
                        remoteOrder.getOrderEx(orderId, languageId));
                return mapping.findForward("view");
            } else if (request.getParameter("action").equals("status")) {
                String statusStr = request.getParameter("statusId");
                if (statusStr != null) {
                    OrderSessionHome orderHome =
                            (OrderSessionHome) EJBFactory.lookUpHome(
                            OrderSessionHome.class,
                            OrderSessionHome.JNDI_NAME);
                    
                    OrderSession remoteOrder = orderHome.create();
                    Integer orderId = ((OrderDTO) session.getAttribute(
                            Constants.SESSION_ORDER_DTO)).getId();
                    if (statusStr.equals("delete")) {
                        remoteOrder.delete(orderId);
                        session.removeAttribute(Constants.SESSION_LIST_KEY + 
                                Constants.LIST_TYPE_ORDER);
                        return mapping.findForward("list");
                    } else {
                        // change the status and update the dto in the session
                        // in one call
                        session.setAttribute(Constants.SESSION_ORDER_DTO, 
                                remoteOrder.setStatus(orderId, 
                                Integer.valueOf(statusStr), 
                                (Integer) session.getAttribute(
                                    Constants.SESSION_LOGGED_USER_ID), 
                                languageId));
                    }
                }  
                return mapping.findForward("view");
                              
            } else if (request.getParameter("action").equals("isParent")) {
                // New order: the list of customers has been show
                // now we have to see if a parent has been selected, to
                // show the children, or not and just go to the order edit
                Integer userId = (Integer) session.getAttribute(
                        Constants.SESSION_USER_ID);
                UserSessionHome orderHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            
                UserSession remoteUser = orderHome.create();
                if (remoteUser.isParentCustomer(userId).booleanValue()) {
                    return mapping.findForward("sub_accounts");
                } else {
                    return mapping.findForward("order_edit");
                }
            } else if (request.getParameter("action").equals("useParent")) {
                // New order: it doesn't want to create an order for a sub-account
                // it want to create one for the parent account
                return mapping.findForward("order_edit");
            }
            
            NewOrderSessionHome newOrderHome =
                    (NewOrderSessionHome) EJBFactory.lookUpHome(
                    NewOrderSessionHome.class,
                    NewOrderSessionHome.JNDI_NAME);

            summary = (NewOrderDTO) session.getAttribute(
                    Constants.SESSION_ORDER_SUMMARY);   
                        
            summary.setUserId((Integer) session.getAttribute( //TODO this is known to have thrown a Null pointer exception
                    Constants.SESSION_USER_ID));
            remoteSession = newOrderHome.create(summary,
                   (Integer) session.getAttribute(Constants.SESSION_LANGUAGE));

            // this is the way the server session is linked to the
            // customer session
            session.setAttribute(
                    Constants.SESSION_ORDER_SESSION_KEY,
                    remoteSession);
            GenericMaintainAction gma = new GenericMaintainAction(mapping,
                    form, request, response, servlet, remoteSession, 
                    "order");
                    
            return gma.process();            
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        return mapping.findForward("error");
    }
  
}
