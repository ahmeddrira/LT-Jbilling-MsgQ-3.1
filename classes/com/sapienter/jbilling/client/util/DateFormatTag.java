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
 * Created on Aug 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sapienter.jbilling.client.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.validator.Resources;

/**
 *
 * @jsp:tag name="dateFormat"
 *          body-content="JSP"
 */
public class DateFormatTag extends TagSupport {
    public String format;
    
    public int doStartTag() throws JspException {
        int retValue = EVAL_BODY_INCLUDE;
        Logger log = Logger.getLogger(DateFormatTag.class);
        
        HttpServletRequest request = (HttpServletRequest)
                pageContext.getRequest();
        // get the message key with the format
        String field = Resources.getMessage(request, "format.form.date");
        if (field == null) {
            log.error("Missing key in ApplicationResources. format.form.date " +
                    "is needed to display date fields in the right order");
            retValue = SKIP_BODY;
        } else {
            if (!field.equalsIgnoreCase(getFormat())) {
                retValue = SKIP_BODY;
            }
        }
        return retValue;
    }
    
    /**
     * @jsp:attribute required="true"
     *                rtexprvalue="true"
     *                type="java.lang.String"
     * @return
     */
    public String getFormat() {
        return format;
    }
    /**
     * @param format The format to set.
     */
    public void setFormat(String format) {
        this.format = format;
    }
}
