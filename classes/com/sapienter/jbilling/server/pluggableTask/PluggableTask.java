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
 * Created on Apr 15, 2003
 *
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.sapienter.jbilling.interfaces.PluggableTaskEntityLocal;
import com.sapienter.jbilling.interfaces.PluggableTaskParameterEntityLocal;


public abstract class PluggableTask {
    protected HashMap parameters = null;
    Integer entityId = null;

    public void initializeParamters(PluggableTaskEntityLocal task) 
            throws PluggableTaskException {
        Collection DBparameters = task.getParameters();
        parameters = new HashMap();
        entityId = task.getEntityId();
        if (DBparameters.size() < 
                task.getType().getMinParameters().intValue()) {
            throw new PluggableTaskException("Type [" + task.getType().getTitle(
                    new Integer(1)) + "] requires at least " + 
                    task.getType().getMinParameters() + " parameters." +
                    DBparameters.size() + " found.");
        }
        
        if (DBparameters.isEmpty()) {
            return;
        }
        
        for(Iterator it = DBparameters.iterator(); it.hasNext();) {
            PluggableTaskParameterEntityLocal parameter = 
                (PluggableTaskParameterEntityLocal) it.next();
            
            Object value = parameter.getIntValue();
            if (value == null) {
                value = parameter.getStrValue();
                if (value == null) {
                    value = parameter.getFloatValue();
                }
            }
            
            parameters.put(parameter.getName(), value);
        }
    }
}
