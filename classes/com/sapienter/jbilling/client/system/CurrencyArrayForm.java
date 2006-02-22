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
 * Created on Mar 20, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.system;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.sapienter.jbilling.server.item.CurrencyDTOEx;

/**
 * @author Emil
 */
public class CurrencyArrayForm extends ActionForm {

    private CurrencyDTOEx[] lines = null;
    private Integer defaultCurrencyId = null;
    /**
     * 
     */
    public CurrencyArrayForm() {
        super();
    }

    /**
     * @return
     */
    public CurrencyDTOEx[] getLines() {
        return lines;
    }

    /**
     * @param line
     */
    public void setLines(CurrencyDTOEx[] lines) {
        this.lines = lines;
    }

    // with the booldy checkboxes, I've got to reset them all
    public void reset(ActionMapping mapping,
            javax.servlet.http.HttpServletRequest request) {
        if (lines != null) {
            for (int f = 0; f < lines.length; f++) {
                lines[f].setInUse(new Boolean(false));
            }
        }    
    }  
    /**
     * @return
     */
    public Integer getDefaultCurrencyId() {
        return defaultCurrencyId;
    }

    /**
     * @param defaultCurrencyId
     */
    public void setDefaultCurrencyId(Integer defaultCurrencyId) {
        this.defaultCurrencyId = defaultCurrencyId;
    }

}
