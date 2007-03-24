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
package com.sapienter.jbilling.server.system.event;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskException;
import com.sapienter.jbilling.server.pluggableTask.PluggableTaskManager;

/**
 * Implementation of this interface take an event, open it to extracts its data.
 * Then calls a specific pluggable task using this data as parameters
 * Usually, there is a one-to-one relationship between:
 *          event - processor - pluggable task
 * Yet, a processor can take care of many events, and deal with one pluggable task.
 * It can also deal with more than one pluggable task, but I don't see a reason 
 * for this.
 * @author ece
 */
public abstract class EventProcessor<TaskType> {
    public abstract void process(Event event);

    protected TaskType getPluggableTask(Integer entityId, Integer taskCategoryId) {
        try {
            PluggableTaskManager taskManager =
                new PluggableTaskManager(entityId,
                taskCategoryId);
            return  (TaskType) taskManager.getNextClass();
        } catch (PluggableTaskException e) {
            throw new SessionInternalError(e);
        }
    }

    public String toString() {
        return this.getClass().getName();
    }
}
