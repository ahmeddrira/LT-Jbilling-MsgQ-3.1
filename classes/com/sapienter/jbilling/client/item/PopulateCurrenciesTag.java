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
 * Created on Mar 13, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.item;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.validator.DynaValidatorForm;

import com.sapienter.jbilling.client.util.Constants;
import com.sapienter.jbilling.server.item.ItemPriceDTOEx;
import com.sapienter.jbilling.server.util.OptionDTO;

/**
 * Takes the object form and adds prices from the options page object
 * 
 * @author emilc
 *
 * @jsp:tag name="populateCurrencies"
 *          body-content="empty"
 */
public class PopulateCurrenciesTag extends TagSupport {

    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        // put the price in the form
        DynaValidatorForm form = (DynaValidatorForm) session.getAttribute(
                "item");

        Vector prices = (Vector) form.get("prices");
        if (prices.size() > 0) {
            // it's been done already
            return SKIP_BODY;
        } 
        
        for (Iterator it = ((Collection) pageContext.getAttribute(
                Constants.PAGE_CURRENCIES)).iterator(); it.hasNext(); ) {
            OptionDTO option = (OptionDTO) it.next();
            ItemPriceDTOEx price = new ItemPriceDTOEx(null,null, 
                    Integer.valueOf(option.getCode()));
            price.setName(option.getDescription());
            
            prices.add(price);
        }
        
        return SKIP_BODY;
    }
}
