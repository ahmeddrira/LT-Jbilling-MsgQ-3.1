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
 * Created on 4-Jun-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.interfaces.ReportSession;
import com.sapienter.jbilling.interfaces.ReportSessionHome;
import com.sapienter.jbilling.server.report.Field;
import com.sapienter.jbilling.server.report.ReportDTOEx;

/**
 * Calls the report session bean to get the specified report DTO
 * 
 * @author emilc
 *
 * @jsp:tag name="reportSetUp"
 *          body-content="empty"
 */
public class ReportSetUpTag extends TagSupport {

    
    public int doStartTag() throws JspException {
        int retValue = SKIP_BODY;
        
        Logger log = Logger.getLogger(ReportSetUpTag.class);
        ActionErrors errors = new ActionErrors();
        
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) 
                pageContext.getRequest();
        ReportDTOEx report = (ReportDTOEx) session.getAttribute(
                Constants.SESSION_REPORT_DTO);
        // get the either the report id or the user report id
        String param = request.getParameter(Constants.REQUEST_REPORT_ID);
        Integer reportId = null;
        Integer userReportId = null;
        
        if (param != null ) {
            reportId = Integer.valueOf(param);
            session.setAttribute(Constants.SESSION_REPORT_LINK, 
                Constants.REQUEST_REPORT_ID + "=" + reportId);
        }
        
        param = request.getParameter(
                Constants.REQUEST_USER_REPORT_ID);
        
        if (param != null) {
            userReportId = Integer.valueOf(param);
            session.setAttribute(Constants.SESSION_REPORT_LINK, 
                Constants.REQUEST_USER_REPORT_ID + "=" + userReportId);
        }

        log.debug("Running report setup rid:" + reportId + ":" + 
                (report != null ? report.getId() : null));

        String back = request.getParameter("back");
        
        // verify that this is not just a recall
        if ((reportId != null && report != null && 
                report.getId().equals(reportId) && 
                report.getUserReportId() == null) ||
                back != null) {
            // this is doing some caching
            log.debug("Using a cached report");
            return retValue;
        }
        
        try {
            // get a report remote session
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ReportSessionHome reportHome =
                    (ReportSessionHome) EJBFactory.lookUpHome(
                    ReportSessionHome.class,
                    ReportSessionHome.JNDI_NAME);
    
            ReportSession myRemoteSession = reportHome.create();
            // get the object
            if (reportId != null) {
                Integer entityId = (Integer) session.getAttribute(
                        Constants.SESSION_ENTITY_ID_KEY);
                log.debug("Fetching report " + reportId);
                report = myRemoteSession.getReportDTO(
                        reportId, entityId);
            } else if (userReportId != null) {
                log.debug("Fetching user report " + userReportId);
                report = myRemoteSession.getReportDTO(
                        userReportId);
            } 
            
            if (report == null) {
                throw new SessionInternalError("Report is not present. " +                        "Both report id and user report id are missing");
            }

            // make it available in the session
            session.setAttribute(Constants.SESSION_REPORT_DTO, report);
            log.debug("reportDto = " + report);
            
            // create the dynamic form with the necessary fields
            Form form = new Form(report.getFields().size());
            for (int f = 0; f < report.getFields().size(); f++) {
                Field field = (Field) report.getFields().get(f);
                // set the flag of selected or not
                form.setSelect(f, field.getIsShown().intValue() == 1);
                // set the where fields if this field is wherable
                if (field.getWherable().intValue() == 1) {
                    if (field.getWhereValue() != null) {
                        if (field.getDataType().equals(Field.TYPE_DATE)) {
                            // TODO: this will fail to put back an invalid
                            // value. Example: an empty month will throw
                            // so the year will be skipped.
                            try {
                                form.setDay(f, String.valueOf(Util.getDay(
                                        field.getWhereValue())));
                                form.setMonth(f, String.valueOf(Util.getMonth(
                                        field.getWhereValue())));
                                form.setYear(f, String.valueOf(Util.getYear(
                                        field.getWhereValue())));
                            } catch (SessionInternalError e) {
                            }
                        } else {
                            form.setWhere(f, field.getWhereValue());
                        }
                    } else {
                        // let's see what happens when a null value
                        // is used to populate a html field.
                        // Hopefully is going to show up empty
                    }
                    
                    // since it's wherable, the operator will be required
                    form.setOperator(f, field.getOperator());
                }
                
                // set the functionable values
                if (field.getIsShown().intValue() == 1 && 
                        field.getFunctionable().intValue() == 1) {
                    if (field.getFunction() != null) {
                        form.setFunction(f, field.getFunction());            
                    } else if (field.getIsGrouped().intValue() == 1) {
                        form.setFunction(f, "grouped"); 
                    }
                    else {
                        form.setFunction(f, "none");
                    }
                }
                
                // set the order by values
                if (field.getOrdenable().intValue() == 1) {
                    form.setOrderBy(f, String.valueOf(
                            field.getOrderPosition()));
                }
            }

            log.debug("Now form is " + form);
            session.setAttribute(Constants.SESSION_REPORT_FORM, form);
        } catch (Exception e) {
            log.error("Exception: ", e);
            errors.add(ActionErrors.GLOBAL_ERROR, 
                    new ActionError("all.internal"));
        }        

        if (!errors.isEmpty()) {
            pageContext.getRequest().setAttribute(Globals.ERROR_KEY, 
                    errors);
        }
        
        return retValue;
    }    
}
