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
package com.sapienter.jbilling.client.invoice;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.InvoiceSession;
import com.sapienter.jbilling.interfaces.InvoiceSessionHome;
import com.sapienter.jbilling.server.entity.InvoiceDTO;

/**
 * Calls the report session bean to get the specified report DTO
 * 
 * @author emilc
 *
 * @jsp:tag name="invoiceDownload"
 *          body-content="empty"
 */
public class DownloadTag extends TagSupport {
    
    private String name = null;

    public int doStartTag() throws JspException {
        int retValue = SKIP_BODY;
        
        Logger log = Logger.getLogger(DownloadTag.class);
        ActionErrors errors = new ActionErrors();
        
        log.debug("Running download:");
        
        HttpSession session = pageContext.getSession();
        InvoiceDTO invoice = (InvoiceDTO) session.getAttribute(
                Constants.SESSION_INVOICE_DTO);
        try {
            JNDILookup EJBFactory = JNDILookup.getFactory(false);
            InvoiceSessionHome invoiceHome =
                    (InvoiceSessionHome) EJBFactory.lookUpHome(
                    InvoiceSessionHome.class,
                    InvoiceSessionHome.JNDI_NAME);
        
            InvoiceSession invoiceSession = invoiceHome.create();

            HttpServletResponse response = (HttpServletResponse) 
                    pageContext.getResponse();
            
            response.setContentType("application/download");
            response.setHeader("Content-Disposition",  
                    "attachment;filename=" + invoiceSession.getFileName(
                            invoice.getId()) + ".pdf");
            // a pdf is a binary file, therefore we use an output stream
            // that won't be encoded
            ServletOutputStream out = response.getOutputStream();

            byte[] document = invoiceSession.getPDFInvoice(invoice.getId());
            for(int f = 0; f < document.length; f++) {
            	out.write(document[f]);
            }
            
            out.close();
        } catch (Exception e) {
            log.error("Exception", e);
            errors.add(ActionErrors.GLOBAL_ERROR, 
                    new ActionError("all.internal"));
            pageContext.getRequest().setAttribute(Globals.ERROR_KEY, 
                    errors);
        }
        
        return retValue;
    }    
    
}
