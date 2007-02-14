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

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.File;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.common.Util;

/**
 * Sets variable named by tag attribute 'id' to true if an invoice 
 * logo for the user's entity (company) has been uploaded.
 * 
 * @author Lucas Pickstone
 *
 * @jsp:tag name="invoiceLogoExists"
 *          body-content="empty"
 *          tei-class="com.sapienter.jbilling.client.invoice.LogoExistsTagTEI"
 */
public class LogoExistsTag extends TagSupport {
     public int doStartTag() {
         return SKIP_BODY;
     }

     public int doEndTag() throws JspException {
        Logger log = Logger.getLogger(LogoExistsTag.class);
        ActionErrors errors = new ActionErrors();
        
        log.debug("Checking whether invoice logo file exists:");
        
        HttpSession session = pageContext.getSession();

        try {

            Integer entityId = (Integer) session.getAttribute(
                    Constants.SESSION_ENTITY_ID_KEY);

            File imageFile = new File(Util.getSysProp("base_dir") + "logos"
                    + File.separator + "entity-" + entityId + ".jpg");

            pageContext.setAttribute(getId(), imageFile.exists());

        } catch (Exception e) {
            log.error("Exception", e);
            errors.add(ActionErrors.GLOBAL_ERROR, 
                    new ActionError("all.internal"));
            pageContext.getRequest().setAttribute(Globals.ERROR_KEY, 
                    errors);
        }

        return EVAL_PAGE;
    }

    /**
     * @jsp:attribute required="true"
     *                rtexprvalue="true"
     *                type="java.lang.String"
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * @param String
     */
    public void setId(String string) {
        id = string;
    }
}
