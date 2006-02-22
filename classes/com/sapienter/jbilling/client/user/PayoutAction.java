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
 * Created on Apr 13, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.user;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

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
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.validator.Resources;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;
import com.sapienter.jbilling.server.payment.PaymentDTOEx;
import com.sapienter.jbilling.server.user.PartnerDTOEx;
import com.sapienter.jbilling.server.user.PartnerPayoutDTOEx;

/**
 * @author Emil
 */
public class PayoutAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        Logger log = Logger.getLogger(PayoutAction.class);
        String forward = "error";
        ActionErrors errors = new ActionErrors();
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            UserSessionHome userHome =
                    (UserSessionHome) EJBFactory.lookUpHome(
                    UserSessionHome.class,
                    UserSessionHome.JNDI_NAME);
            UserSession userSession = userHome.create();
            String action = request.getParameter("action");
            HttpSession session = request.getSession(false);
            PartnerDTOEx partner = (PartnerDTOEx) session.getAttribute(
                    Constants.SESSION_PARTNER_DTO);
            
            DynaValidatorForm myForm = (DynaValidatorForm) form;
            if (action.equals("setup")) {
                // get the dates for a new payout
                Date dates[] = userSession.getPartnerPayoutDates(
                        partner.getId());
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dates[0]);
                myForm.set("start_month", String.valueOf(cal.get(
                        GregorianCalendar.MONTH) + 1));
                myForm.set("start_day", String.valueOf(cal.get(
                        GregorianCalendar.DAY_OF_MONTH)));
                myForm.set("start_year", String.valueOf(cal.get(
                        GregorianCalendar.YEAR)));            
                cal.setTime(dates[1]);
                myForm.set("end_month", String.valueOf(cal.get(
                        GregorianCalendar.MONTH) + 1));
                myForm.set("end_day", String.valueOf(cal.get(
                        GregorianCalendar.DAY_OF_MONTH)));
                myForm.set("end_year", String.valueOf(cal.get(
                        GregorianCalendar.YEAR)));            
                forward = "edit";
            } else if (action.equals("edit")) {
                // validates
                errors = myForm.validate(mapping, request);
                Date startDate = null, endDate = null;
                if (errors.isEmpty()) {
                    startDate = Util.getDate(Integer.valueOf(
                            (String) myForm.get("start_year")), 
                            Integer.valueOf((String) myForm.get("start_month")),
                            Integer.valueOf((String) myForm.get("start_day")));
                    if (startDate == null) {
                        String field = Resources.getMessage(request, 
                                "payout.prompt.startDate"); 
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("errors.date", field));
                    } 
                    endDate = Util.getDate(Integer.valueOf(
                            (String) myForm.get("end_year")), 
                            Integer.valueOf((String) myForm.get("end_month")),
                            Integer.valueOf((String) myForm.get("end_day")));
                    if (endDate == null) {
                        String field = Resources.getMessage(request, 
                                "payout.prompt.endDate"); 
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                new ActionError("errors.date", field));
                    } 
                }
                
                if (errors.isEmpty()) {
                    PartnerPayoutDTOEx payout = userSession.calculatePayout(
                            partner.getId(), startDate, endDate, null);
                    session.setAttribute(Constants.SESSION_PAYMENT_DTO, 
                            new PaymentDTOEx(payout.getPayment()));
                    session.setAttribute(Constants.SESSION_PAYOUT_DTO, payout);
                    session.setAttribute("jsp_payment_method", 
                            (String) myForm.get("method"));
                    forward = "payment";
                } else {
                    forward = "edit";
                }
            } else if (action.equals("view")) {
                Integer payoutId = Integer.valueOf(request.getParameter("id"));
                session.setAttribute(Constants.SESSION_PAYOUT_DTO,
                        userSession.getPartnerPayoutDTO(payoutId));
                forward = "view";
            }
        } catch (Exception e) {
            log.error("Exception ", e);
        }
        
        saveErrors(request, errors);
        
        return mapping.findForward(forward);
    }
 
}
