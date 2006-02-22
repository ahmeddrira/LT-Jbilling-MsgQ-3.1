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

package com.sapienter.jbilling.client.report;

import java.util.Collection;

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
import com.sapienter.jbilling.interfaces.ReportSession;
import com.sapienter.jbilling.interfaces.ReportSessionHome;
import com.sapienter.jbilling.server.report.ReportDTOEx;

/**
 * Prepares the result set so the InsertDataRowTag can then render
 * the data in a table
 * 
 * @author emilc
 *
 * @jsp:tag name="reportList"
 * 			body-content="JSP"
 */

public class ListTag extends TagSupport {
    
    // this can be:
    //     entity = all the reports for the entity (in session)
    //     user   = all the saved reports for the give user/report type
    String mode = null;

    public int doStartTag() throws JspException {
        int retValue = SKIP_BODY;

        Logger log = Logger.getLogger(ListTag.class);
        ActionErrors errors = new ActionErrors();
        
        try {
            // I'll get the entity id first from the session
            HttpSession session = pageContext.getSession();

            // this might be a recall, so lets see if the list is already cached
            // recalls have to be addressed with this cache, because for each click
            // to add an item there's a recall ...
            Collection queryResults = null;
            
            if (mode.equals("entity")) {
                queryResults = (Collection) session.getAttribute(
                    Constants.SESSION_REPORT_LIST);
            } else if (mode.equals("user")) {
                // do not cache the list of user's reports
                queryResults = null;
            }
            
            if (queryResults == null) {

                // Now I'll call the session bean to get the CachedRowSet with
                // the results of the query
                JNDILookup EJBFactory = JNDILookup.getFactory(false);
                ReportSessionHome reportHome =
                        (ReportSessionHome) EJBFactory.lookUpHome(
                        ReportSessionHome.class,
                        ReportSessionHome.JNDI_NAME);

                ReportSession myRemoteSession = reportHome.create();

                if (mode.equals("entity")) {
                    Integer entityID = (Integer) session.getAttribute(
                            Constants.SESSION_ENTITY_ID_KEY);
                    
                    queryResults = myRemoteSession.getList(entityID);
                    session.setAttribute(Constants.SESSION_REPORT_LIST, 
                            queryResults);
                } else if (mode.equals("user")) {
                    Integer userId =  (Integer) session.getAttribute(
                            Constants.SESSION_LOGGED_USER_ID);
                    Object ob = session.getAttribute(
                            Constants.SESSION_REPORT_DTO);
                    HttpServletRequest request = (HttpServletRequest) 
                            pageContext.getRequest();
                    Integer reportId = null;
                    
                    String reqReportId = request.getParameter(
                            Constants.REQUEST_REPORT_ID);
                    if (reqReportId != null && reqReportId.length() > 0) {
                        reportId = Integer.valueOf(reqReportId);
                    }
                    if (reportId == null && ob != null ) { // take it from the session
                        reportId = ((ReportDTOEx) ob).getId();
                    } 
                    if (reportId == null) {
                        throw new SessionInternalError("can't get the report id");
                    }
                    queryResults = myRemoteSession.getUserList(reportId,
                            userId);
                    session.setAttribute(Constants.SESSION_REPORT_LIST_USER, 
                            queryResults);
                }
                log.debug("Added the reports list to the session");
            } 
        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR, 
                    new ActionError("all.internal"));
            pageContext.getRequest().setAttribute(Globals.ERROR_KEY, 
                    errors);

            log.error("Exception at list report tag", e);
        }

        return retValue;

    }

    /**
     * @jsp:attribute required="true"
     *                rtexprvalue="true"
     *                type="java.lang.String"
     * @return
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param string
     */
    public void setMode(String string) {
        mode = string;
    }

}
