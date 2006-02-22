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
 * Created on 20-Mar-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.order;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.Resources;

import com.sapienter.jbilling.server.order.OrderLineDTOEx;

/**
 * A great pain to make this work. Finally, the solution was the scope
 * of this bean. Since it goes back a forth to the server a few times,
 * it has to be at the session level. A request level is all trouble.
 * @author Emil
 */
public class NewOrderDTOForm extends ActionForm {
    private Hashtable orderLines = null;
    
    public NewOrderDTOForm() {
        orderLines = new Hashtable();
    }

    // called by the logic:iterate tag, to get the keys of
    // each line
    public Hashtable getOrderLines() {
        return orderLines;
    }

    // called by the action form, to set the lines from the
    // original DTO
    public void setOrderLines(Hashtable newOL) {
        orderLines = newOL;
    }

    // called by the html:text tag, to show (an apparently also set ??)
    // the values of each line
    public Object getOrderLine(String itemId) {
        return (OrderLineDTOEx) orderLines.get(Integer.valueOf(itemId));
    }

    // apparently never called
    public void setOrderLine(String itemId, Object line) {
        orderLines.put(Integer.valueOf(itemId), line);
    }

    // just for debugging
    public String toString() {
        return (orderLines == null ? "Empty" : orderLines.toString());
    }

    /**
     * Manual validation for the order review page.
     * This is necessary because is a multi-lined form made of X
     * number of beans in a Hashmap. (don't know how to acomplish this
     * by the normal xml declarations).
     * @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
     */
    public ActionErrors validate(
        ActionMapping mapping,
        HttpServletRequest request) {

        ServletContext application = getServlet().getServletContext();
        ActionErrors errors = new ActionErrors();
        Validator validator = null;
        Logger log = Logger.getLogger(NewOrderDTOForm.class);

        // validate is called even before the page is shown ..
        if (orderLines == null) {
            return null;
        }

        Collection lines = orderLines.values();
        try {
            int lineNumber = 1;
            for (Iterator i = lines.iterator(); i.hasNext(); lineNumber++) {
                OrderLineDTOEx line = (OrderLineDTOEx) i.next();
                // only editable lines get validated
                if (line.getEditable().booleanValue()) { 
                    validator = Resources.initValidator("orderReviewForm",
                            line, application, request, errors, 0);

                    validator.validate();
                    if (!errors.isEmpty()) {
                        log.debug("line = " + line);
                        errors.add(
                            ActionErrors.GLOBAL_ERROR,
                            new ActionError(
                                "order.review.bad_order_line",
                                new Integer(lineNumber)));
                        break;
                    }
                }

            }

        } catch (ValidatorException e) {
            log.error("Error calling the validator", e);
        }

        return errors;
    }

}