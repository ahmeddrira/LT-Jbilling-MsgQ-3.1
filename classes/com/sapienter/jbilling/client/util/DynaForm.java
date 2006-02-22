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
 * Created on Aug 15, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.client.util;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.FormPropertyConfig;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Emil
 */
public class DynaForm extends DynaValidatorForm {

    /**
     * 
     */
    public DynaForm() {
        super();
    }

    public void reset(ActionMapping mapping,
            javax.servlet.http.HttpServletRequest request) {
        
        String name = mapping.getName();
        if (name == null) {
            return;
        }
        FormBeanConfig config = mapping.getModuleConfig().
                findFormBeanConfig(name);
        if (config == null) {
            return;
        }
        
        FormPropertyConfig props[] = config.findFormPropertyConfigs();
        for (int i = 0; i < props.length; i++) {
            if (props[i].getName().startsWith("chbx_")) {
                set(props[i].getName(), new Boolean(false));
            }
        }
    }
}
