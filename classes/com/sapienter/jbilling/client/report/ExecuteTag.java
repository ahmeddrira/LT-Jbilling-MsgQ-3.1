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

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.client.list.ListTagBase;
import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ReportSession;
import com.sapienter.jbilling.interfaces.ReportSessionHome;
import com.sapienter.jbilling.server.report.ReportDTOEx;

/**
 * Prepares the result set so the InsertDataRowTag can then render
 * the data in a table
 * 
 * @author emilc
 *
 * @jsp:tag name="reportExecute"
 *          body-content="JSP"
 */
public class ExecuteTag extends ListTagBase {
    public int doStartTag() throws JspException {
        int retValue = SKIP_BODY;

        log = Logger.getLogger(ExecuteTag.class);
        ActionErrors errors = new ActionErrors();

        try {
            // I'll get the entity id first from the session
            HttpSession session = pageContext.getSession();
            // the session is never null, even when it times out what it's deleted
            // is its contents
            ReportDTOEx report = (ReportDTOEx) session.getAttribute(
                    Constants.SESSION_REPORT_DTO);
            if (report == null) { //means that the session has timed out
                log.fatal(
                    "The session timed out, but the filted didn't catch it");
                throw new JspException("CustomerListTag: session timed out.");
            }
            
            queryResults = (CachedRowSet) session.getAttribute(
                    Constants.SESSION_REPORT_RESULT);

            if (queryResults == null) {
                // Now I'll call the session bean to get the CachedRowSet with
                // the results of the query
                JNDILookup EJBFactory = JNDILookup.getFactory(false);
                ReportSessionHome reportHome =
                       (ReportSessionHome) EJBFactory.lookUpHome(
                        ReportSessionHome.class,
                        ReportSessionHome.JNDI_NAME);
    
                ReportSession myRemoteSession = reportHome.create();
    
                queryResults = myRemoteSession.execute(report);
                
                session.setAttribute(Constants.SESSION_REPORT_RESULT, 
                        queryResults);
            } else {
                // let's rewind, this will change when the web-paging is done
                queryResults.beforeFirst();
            }
            // we leave the cursor ready to render data
            if (queryResults.next()) {
                retValue = EVAL_BODY_INCLUDE;
            }

        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR, 
                    new ActionError("all.internal"));
            pageContext.getRequest().setAttribute(Globals.ERROR_KEY, 
                    errors);

            log.error("Exception at execute report tag", e);
        }

        return retValue;

    }

}
