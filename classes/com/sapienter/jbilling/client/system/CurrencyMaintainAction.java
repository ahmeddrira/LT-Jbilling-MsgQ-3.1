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

package com.sapienter.jbilling.client.system;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
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
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ItemSession;
import com.sapienter.jbilling.interfaces.ItemSessionHome;
import com.sapienter.jbilling.server.item.CurrencyDTOEx;
import com.sapienter.jbilling.server.user.UserDTOEx;

public class CurrencyMaintainAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(CurrencyMaintainAction.class);
        ActionMessages messages = new ActionMessages();
        ActionErrors errors = new ActionErrors();
        
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ItemSessionHome itemHome =
                    (ItemSessionHome) EJBFactory.lookUpHome(
                    ItemSessionHome.class,
                    ItemSessionHome.JNDI_NAME);
        
            ItemSession itemSession = itemHome.create();
            
            String action = request.getParameter("action");
            HttpSession session = request.getSession(false);
            Integer entityId = (Integer) session.getAttribute(
                    Constants.SESSION_ENTITY_ID_KEY);
            Integer languageId = (Integer) session.getAttribute(
                    Constants.SESSION_LANGUAGE);
            CurrencyArrayForm myForm = (CurrencyArrayForm) form;
            if (action.equals("setup")) {
                myForm.setLines(itemSession.getCurrencies(languageId, 
                        entityId));
                myForm.setDefaultCurrencyId(itemSession.getEntityCurrency(
                        entityId)); 
            } else if (action.equals("edit")) {
                for (int f = 0; f < myForm.getLines().length; f++) {
                    CurrencyDTOEx line = myForm.getLines()[f];
                    if (line.getRate() != null && line.getRate().trim().
                            length() > 0) {
                        try {
                            UserDTOEx user = (UserDTOEx) session.getAttribute(
                                    Constants.SESSION_USER_DTO);
                            NumberFormat nf = NumberFormat.getInstance(
                                    user.getLocale());

                            nf.parse(line.getRate().trim());
                        } catch(ParseException e) {
                            String field = Resources.getMessage(request, 
                                    "system.currency.prompt.rate"); 
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                    new ActionError("errors.float", field));
                            
                        }
                    } else {
                        myForm.getLines()[f].setRate(null);
                    }
                }
                
                if (errors.isEmpty()) {
                    itemSession.setCurrencies(entityId, myForm.getLines(),
                            myForm.getDefaultCurrencyId());
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("system.currency.updated"));
                } else {
                    // Save the error messages we need
                    request.setAttribute(Globals.ERROR_KEY, errors);
                }
            }
            
            saveMessages(request, messages);
            return mapping.findForward("edit");
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        return mapping.findForward("error");
    }

}
