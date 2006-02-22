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
 * Created on Nov 18, 2004
 *
 */
package com.sapienter.jbilling.client.util;

import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.sapienter.jbilling.server.user.UserDTOEx;

/**
 * @author Emil
 * 
 * @jsp:tag name="help"
 *      body-content="JSP"
 */
public class HelpTag extends TagSupport {

    private String page = null;
    private String anchor = null;
    private Boolean isMenu = null;
    
    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        UserDTOEx dto = (UserDTOEx) session.getAttribute(
                Constants.SESSION_USER_DTO); 
        ResourceBundle bundle = ResourceBundle.getBundle("help", 
                dto.getLocale());
        
        StringBuffer link = new StringBuffer();
        link.append(bundle.getString("url"));
        
        // menus need special parsing
        if (isMenu != null && isMenu.booleanValue()) {
            String fields[] = page.split("\\|");
            page = fields[1].substring(fields[1].indexOf('=') + 1);
            anchor = fields[2].substring(fields[2].indexOf('=') + 1);
        }
        
        link.append(bundle.getString(page));
        
        if (anchor != null && anchor.length() > 0) {
            link.append('#');
            link.append(bundle.getString(page + '.' + anchor));
        }
        
        // render the link 
        JspWriter out = pageContext.getOut();
        try {
            out.print("<a href=\"" + link.toString() + "\" target=\"" +
                    bundle.getString("target") + "\">");
        } catch (Exception e) {
            throw new JspException(e);
        }
        
        return EVAL_BODY_INCLUDE;
    }
    
    public int doAfterBody() throws JspException {
        // close the link 
        JspWriter out = pageContext.getOut();
        try {
            out.print("</a>");
        } catch (Exception e) {
            throw new JspException(e);
        }
        
        return SKIP_BODY;
    }
    
    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="true"
     *                type="java.lang.String"
     */
    public String getAnchor() {
        return anchor;
    }
    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    /**
     * @jsp:attribute required="false"
     *                rtexprvalue="false"
     *                type="java.lang.Boolean"
     */
    public Boolean getIsMenu() {
        return isMenu;
    }
    public void setIsMenu(Boolean isMenu) {
        this.isMenu = isMenu;
    }

    /**
     * @jsp:attribute required="true"
     *                rtexprvalue="true"
     *                type="java.lang.String"
     */
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
}
