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
 * Created on Nov 19, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.pluggableTask;

import java.util.Vector;

import com.sapienter.jbilling.server.entity.PluggableTaskDTO;

/**
 * @author Emil
 */
public class PluggableTaskDTOEx extends PluggableTaskDTO {
    
    // this is in synch with the DB (pluggable task type)
    public static final Integer TYPE_EMAIL = new Integer(9);

    private Vector parameters = null;
    private Integer typeId = null;
    /**
     * 
     */
    public PluggableTaskDTOEx() {
        super();
        parameters = new Vector();
    }

    /**
     * @param id
     * @param entityId
     * @param processingOrder
     */
    public PluggableTaskDTOEx(
        Integer id,
        Integer entityId,
        Integer processingOrder) {
        super(id, entityId, processingOrder);
    }

    /**
     * @param otherValue
     */
    public PluggableTaskDTOEx(PluggableTaskDTO otherValue) {
        super(otherValue);
    }

    /**
     * @return
     */
    public Vector getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     */
    public void setParameters(Vector parameters) {
        this.parameters = parameters;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String toString() {
        return "Type = " + typeId + " parameters (total) = " +
            parameters.size() + " " + super.toString();
    }
}
