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

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.ReportSession;
import com.sapienter.jbilling.interfaces.ReportSessionHome;

/**
 * Calls the report session bean to get the specified report DTO
 * 
 * @author emilc
 *
 * @jsp:tag name="reportDelete"
 *          body-content="empty"
 */
public class DeleteTag extends TagSupport {

    Integer reportId = null;

    public int doStartTag() throws JspException {
        int retValue = SKIP_BODY;
        
        Logger log = Logger.getLogger(DeleteTag.class);
        ActionErrors errors = new ActionErrors();
        HttpSession session = pageContext.getSession();
        
        log.debug("Deleting ..");
        
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            ReportSessionHome reportHome =
                    (ReportSessionHome) EJBFactory.lookUpHome(
                    ReportSessionHome.class,
                    ReportSessionHome.JNDI_NAME);

            ReportSession myRemoteSession = reportHome.create();
            myRemoteSession.delete(reportId);

            // this will force a reload of the list, otherwise the new 
            // entiry won't show up
            session.removeAttribute(Constants.SESSION_REPORT_LIST_USER);

        } catch (Exception e) {
            log.error(e);
            errors.add(ActionErrors.GLOBAL_ERROR, 
                    new ActionError("all.internal"));
            pageContext.getRequest().setAttribute(Globals.ERROR_KEY, 
                    errors);
        }        
        
        return retValue;
    }    
    
    /**
     * @jsp:attribute required="true"
     *                rtexprvalue="true"
     *                type="java.lang.Integer"
     * @return
     */
    public Integer getReportId() {
        return reportId;
    }

    /**
     * @param integer
     */
    public void setReportId(Integer integer) {
        reportId = integer;
    }

}
