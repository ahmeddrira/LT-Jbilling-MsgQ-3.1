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
 * Created on Feb 28, 2005
 *
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.validator.DynaValidatorForm;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.OrderSession;
import com.sapienter.jbilling.interfaces.OrderSessionHome;
import com.sapienter.jbilling.server.order.OrderPeriodDTOEx;

/**
 * @author Emil
 *
 */
public class OrderPeriodAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(MaintainAction.class);
        HttpSession session = request.getSession(false);
        Integer languageId = (Integer) session.getAttribute(
                Constants.SESSION_LANGUAGE);
        Integer entityId = (Integer) session.getAttribute(
                Constants.SESSION_ENTITY_ID_KEY);
        DynaValidatorForm myForm;
        try {
            String ret = "view";
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ActionMessages messages = new ActionMessages();
            ActionErrors errors = new ActionErrors();
            
            String action = request.getParameter("action");
            OrderSessionHome orderHome =
                (OrderSessionHome) EJBFactory.lookUpHome(
                OrderSessionHome.class,
                OrderSessionHome.JNDI_NAME);
            OrderSession orderSession = orderHome.create();
            
            if (action.equals("setup")) {
                OrderPeriodDTOEx[] periods = orderSession.getPeriods(entityId,
                        languageId);
                
                Integer arr1[] = new Integer[periods.length];
                Integer arr2[] = new Integer[periods.length];
                String arr3[] = new String[periods.length];
                String arr4[] = new String[periods.length];
                for (int f = 0; f < periods.length; f++) {
                    arr1[f] = periods[f].getId();
                    arr2[f] = periods[f].getUnitId();
                    arr3[f] = periods[f].getValue().toString();
                    arr4[f] = periods[f].getDescription();
                }
                
                ModuleConfig moduleConfig = RequestUtils.getModuleConfig(request,
                        servlet.getServletContext());
                myForm = (DynaValidatorForm) RequestUtils.createActionForm(
                        request, mapping, moduleConfig, servlet);
                myForm.set("id", arr1);
                myForm.set("unit", arr2);
                myForm.set("value", arr3);
                myForm.set("description", arr4);
                
                session.setAttribute("orderPeriod", myForm);
            } else if (action.equals("edit")) {
                myForm = (DynaValidatorForm) form;
                Integer ids[] = (Integer[]) myForm.get("id");
                OrderPeriodDTOEx[] periods = new OrderPeriodDTOEx[ids.length];
                for (int f = 0; f < ids.length; f++) {
                    periods[f] = new OrderPeriodDTOEx();
                    periods[f].setId(ids[f]);
                    periods[f].setUnitId(((Integer[]) myForm.get("unit"))[f]);
                    try {
                        periods[f].setValue(Integer.valueOf(((String[]) myForm.get(
                        "value"))[f]));
                        if (periods[f].getValue().intValue() <= 0) {
                            throw new Exception();
                        }
                    } catch (Exception e) {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "order.period.error.value"));
                        break;
                    }
                    periods[f].setDescription(((String[]) myForm.get("description"))[f]);
                    if (periods[f].getDescription() == null || 
                            periods[f].getDescription().trim().length() == 0) {
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "order.period.error.description"));
                        break;
                    }
                }
                
                if (errors.isEmpty()) {
                    orderSession.setPeriods(languageId, periods);
                } else {
                    saveErrors(request, errors);
                }
            } else if (action.equals("add")) {
                orderSession.addPeriod(entityId, languageId);
                ret = "refresh";
            } else if (action.equals("delete")) {
                Integer id = Integer.valueOf(request.getParameter("id"));
                Boolean result = orderSession.deletePeriod(id);
                if (result.booleanValue()) {
                    messages.add(ActionMessages.GLOBAL_MESSAGE, 
                            new ActionMessage("order.period.deleted", id));
                    saveMessages(request, messages);
                } else {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
                            "order.period.error.delete"));
                    saveErrors(request, errors);
                }
                ret = "refresh";
            }
            
            return mapping.findForward(ret);
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        return mapping.findForward("error");
    }
}
