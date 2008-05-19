/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

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
import org.apache.struts.validator.DynaValidatorForm;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.NewOrderSession;
import com.sapienter.jbilling.interfaces.NewOrderSessionHome;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.order.db.OrderDTO;
import com.sapienter.jbilling.server.user.db.BaseUser;

public class MaintainAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(MaintainAction.class);
        OrderDTO summary;
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
                    Integer userId = (Integer) session.getAttribute(
                            Constants.SESSION_LOGGED_USER_ID);
                    if (statusStr.equals("delete")) {
                        remoteOrder.delete(orderId, userId);
                        session.removeAttribute(Constants.SESSION_LIST_KEY + 
                                Constants.LIST_TYPE_ORDER);
                        return mapping.findForward("list");
                    } else {
                        // change the status and update the dto in the session
                        // in one call
                        session.setAttribute(Constants.SESSION_ORDER_DTO, 
                                remoteOrder.setStatus(orderId, 
                                Integer.valueOf(statusStr), userId, 
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

            summary = (OrderDTO) session.getAttribute(
                    Constants.SESSION_ORDER_SUMMARY);   
                        
            BaseUser user = new BaseUser();
            user.setId((Integer) session.getAttribute( //TODO this is known to have thrown a Null pointer exception
                    Constants.SESSION_USER_ID));
            summary.setBaseUserByUserId(user);
            //summary.setBillingTypeId((Integer) ((DynaValidatorForm)form).get("billingType"));

            remoteSession = newOrderHome.create(summary,
                   (Integer) session.getAttribute(Constants.SESSION_LANGUAGE));

            // this is the way the server session is linked to the
            // customer session
            session.setAttribute(
                    Constants.SESSION_ORDER_SESSION_KEY,
                    remoteSession);
            
            OrderCrudAction delegate = new OrderCrudAction(remoteSession);
            delegate.setServlet(getServlet());
            return delegate.execute(mapping, form, request, response);
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        return mapping.findForward("error");
    }
  
}
