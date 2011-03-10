/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.item.tasks;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.item.event.ItemDeletedEvent;
import com.sapienter.jbilling.server.item.event.ItemUpdatedEvent;
import com.sapienter.jbilling.server.item.event.NewItemEvent;
import com.sapienter.jbilling.server.item.tasks.itemChanges.FileItemChangesCommunication;
import com.sapienter.jbilling.server.item.tasks.itemChanges.IItemChangesCommunication;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.system.event.task.IInternalEventsTask;

/**
 * Responds to changes to items. Used to communicate the changes to 
 * external systems, such as SugarCRM.
 */
public class ItemChangesExternalTask extends PluggableTask 
        implements IInternalEventsTask {

    private static final Logger LOG = 
            Logger.getLogger(ItemChangesExternalTask.class);

    private static final Class<Event> events[] = new Class[] { 
            NewItemEvent.class, ItemUpdatedEvent.class, 
            ItemDeletedEvent.class };

    public Class<Event>[] getSubscribedEvents() {
        return events;
    }

    public void process(Event event) throws PluggableTaskException {

        IItemChangesCommunication task = new FileItemChangesCommunication();

        if (event instanceof NewItemEvent) {
            task.process((NewItemEvent) event);
        } else if (event instanceof ItemUpdatedEvent) {
            task.process((ItemUpdatedEvent) event);
        } else if (event instanceof ItemDeletedEvent) {
            task.process((ItemDeletedEvent) event);
        } else {
            throw new PluggableTaskException("Unknown event: " + 
                    event.getClass());
        }
    }
}
