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

Contributor(s): Lucas Pickstone_______________________.
*/

package com.sapienter.jbilling.client.invoice;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.Util;

/**
 * Retrieves the invoice logo for the user's entity (company).
 * 
 * @author Lucas Pickstone
 *
 * @jsp:tag name="invoiceLogo"
 *          body-content="empty"
 */
public class LogoTag extends TagSupport {
     public int doStartTag() throws JspException {
        Logger log = Logger.getLogger(LogoTag.class);
        ActionErrors errors = new ActionErrors();
        
        log.debug("Running invoice logo file download:");
        
        HttpSession session = pageContext.getSession();

        try {

            HttpServletResponse response = (HttpServletResponse) 
                    pageContext.getResponse();
            
            // image is a binary file, therefore we use an output stream
            // that won't be encoded
            ServletOutputStream out = response.getOutputStream();

            response.setContentType("image/jpeg");

            Integer entityId = (Integer) session.getAttribute(
                    Constants.SESSION_ENTITY_ID_KEY);

            File imageFile = new File(Util.getSysProp("base_dir") + "logos"
                    + File.separator + "entity-" + entityId + ".jpg");
            FileInputStream imageInput = new FileInputStream(imageFile);

            while (imageInput.available() != 0) {
                out.write(imageInput.read());
            }

            imageInput.close();
            out.close();
        } catch (Exception e) {
            log.error("Exception", e);
            errors.add(ActionErrors.GLOBAL_ERROR, 
                    new ActionError("all.internal"));
            pageContext.getRequest().setAttribute(Globals.ERROR_KEY, 
                    errors);
        }

        return SKIP_BODY;
    }
}
