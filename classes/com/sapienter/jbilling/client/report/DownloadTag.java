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

import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.Resources;

import sun.jdbc.rowset.CachedRowSet;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.server.report.Field;
import com.sapienter.jbilling.server.report.ReportDTOEx;

/**
 * Calls the report session bean to get the specified report DTO
 * 
 * @author emilc
 *
 * @jsp:tag name="reportDownload"
 *          body-content="empty"
 */
public class DownloadTag extends TagSupport {

    public int doStartTag() throws JspException {
        int retValue = SKIP_BODY;
        
        Logger log = Logger.getLogger(DownloadTag.class);
        ActionErrors errors = new ActionErrors();
        
        log.debug("Running download:");
        
        HttpSession session = pageContext.getSession();
        CachedRowSet list = (CachedRowSet) session.getAttribute(
                Constants.SESSION_REPORT_RESULT);
        ReportDTOEx report = (ReportDTOEx) session.getAttribute(
                Constants.SESSION_REPORT_DTO);
        MessageResources mess = (MessageResources) session.getAttribute(
                Constants.SESSION_MESSAGES);
        try {
            HttpServletResponse response = (HttpServletResponse) 
                    pageContext.getResponse();

            response.setContentType("application/download");
            log.debug("Getting title key:" + report.getTitleKey());
            // the InsertDataRowTag is doing this with no problems ...
            String title = Resources.getMessage(mess, Locale.getDefault(), 
                    report.getTitleKey());
            response.setHeader("Content-Disposition", 
                    "attachment;filename=" + title + ".csv");
            PrintWriter out = response.getWriter();
            String fieldSeparator = "";
            
            // print the column titles
            Vector fields = report.getFields();
            int f = (report.getIdColumn().intValue() == 1) ? 1 : 0;
            for ( ; f < fields.size(); f++) {
                Field field = (Field) fields.get(f);
                
                if (field.getIsShown().intValue() == 1) {
                    String columnTitle = Resources.getMessage(mess, 
                            Locale.getDefault(), field.getTitleKey());
                    out.write(fieldSeparator + columnTitle);
                    fieldSeparator = ",";
                }
            }
            out.println();
            
            // now the data itself
            ResultSetMetaData metaData = list.getMetaData();
            list.beforeFirst();
            while (list.next()) {
                fieldSeparator = "";
                
                for (f = 2; f <= metaData.getColumnCount(); f++) {
                    String content = list.getString(f);
                    out.write(fieldSeparator + 
                            (content == null ? "" : content));
                    fieldSeparator = ",";
                }
                out.println();
                            
            }
            
            out.close();
        } catch (Exception e) {
            log.error("Exception", e);
            errors.add(ActionErrors.GLOBAL_ERROR, 
                    new ActionError("all.internal"));
            pageContext.getRequest().setAttribute(Globals.ERROR_KEY, 
                    errors);
        }
        
        // clean up the session from the messages
        session.removeAttribute(Constants.SESSION_MESSAGES);        
        
        return retValue;
    }    
    
}
