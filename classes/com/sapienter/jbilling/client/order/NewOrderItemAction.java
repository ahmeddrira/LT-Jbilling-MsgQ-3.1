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
 * Created on 10-Mar-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.order;

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

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.NewOrderSession;
import com.sapienter.jbilling.interfaces.NewOrderSessionHome;
import com.sapienter.jbilling.server.order.NewOrderDTO;

/**
 * @author Emil
 */
public class NewOrderItemAction extends Action {
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException {

        Logger log = Logger.getLogger(NewOrderItemAction.class);
        ActionErrors errors = new ActionErrors();
        NewOrderDTO summary = null;

        // get the item id and quantity from the form
        Integer itemID, quantity;

        String action = request.getParameter("action");

        itemID = ((OrderAddItemForm) form).getItemID();
        quantity = ((OrderAddItemForm) form).getQuantity();

        log.debug("Adding item " + itemID + " quant " + quantity);

        // check if the ejb session is already there
        HttpSession session = request.getSession(false);
        NewOrderSession remoteSession =
            (NewOrderSession) session.getAttribute(
                Constants.SESSION_ORDER_SESSION_KEY);
        // if not create new one
        try {
            if (remoteSession == null) {
                JNDILookup EJBFactory = JNDILookup.getFactory(false);
                NewOrderSessionHome newOrderHome =
                        (NewOrderSessionHome) EJBFactory.lookUpHome(
                        NewOrderSessionHome.class,
                        NewOrderSessionHome.JNDI_NAME);

                summary = (NewOrderDTO) session.getAttribute(
                        Constants.SESSION_ORDER_SUMMARY);   
                        
                summary.setUserId((Integer) session.getAttribute(
                        Constants.SESSION_USER_ID));
                remoteSession = newOrderHome.create(summary,
                	   (Integer) session.getAttribute(Constants.SESSION_LANGUAGE));

                // this is the way the server session is linked to the
                // customer session
                session.setAttribute(
                        Constants.SESSION_ORDER_SESSION_KEY,
                        remoteSession);
            }

            // call the ejb session with the item to add
            // and get the order DTO for the summary
            if (action.compareTo("delete") == 0) {
                summary = remoteSession.deleteItem(itemID);
            } else {
                summary = remoteSession.addItem(itemID, quantity, 
                        (Integer) session.getAttribute(
                            Constants.SESSION_USER_ID),
                        (Integer) session.getAttribute(
                            Constants.SESSION_ENTITY_ID_KEY));
            }

        } catch (SessionInternalError e) {
            // this has been already logged ...
            log.error("Exception: ", e);
            errors.add(
                ActionErrors.GLOBAL_ERROR,
                new ActionError("all.internal"));
        } catch (Exception e) {
            log.error("Error at the AddItemAction", e);
            if (e.getCause().getMessage().equals("Item Manager task error")) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("order.error.task"));
            } else {
                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("all.internal"));
            }
        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            summary = new NewOrderDTO();
        } 
        // add the order DTO to the http session for the summary
        session.setAttribute(Constants.SESSION_ORDER_SUMMARY, summary);
        log.debug("The bean " + Constants.SESSION_ORDER_SUMMARY
                + " is now in the session [" + 
                session.getAttribute(Constants.SESSION_ORDER_SUMMARY) + "]");

        // go back to the new order page, so the user can keep adding items 
        return (mapping.findForward("showOrderLIst"));
    }
}
