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

package com.sapienter.jbilling.server.item.tasks.itemChanges;

import java.io.File;
import java.io.FileWriter;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.event.AbstractItemEvent;
import com.sapienter.jbilling.server.item.event.ItemDeletedEvent;
import com.sapienter.jbilling.server.item.event.ItemUpdatedEvent;
import com.sapienter.jbilling.server.item.event.NewItemEvent;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;

/**
 * Test implementation of IItemChangesCommunication that simply writes
 * the item changes to jbilling/resources/item_chages.txt
 */
public class FileItemChangesCommunication implements IItemChangesCommunication {

    private static final String FILENAME = "item_changes.txt";

    public void process(NewItemEvent event) throws PluggableTaskException {
        writeToFile(event, "insert");
    }

    public void process(ItemUpdatedEvent event) throws PluggableTaskException {
        writeToFile(event, "update");
    }

    public void process(ItemDeletedEvent event) throws PluggableTaskException {
        writeToFile(event, "delete");
    }

    private void writeToFile(AbstractItemEvent event, String action) 
            throws PluggableTaskException {

        ItemDTO item = event.getItem();

        String output = action + " itemId: " + item.getId() + 
                ", description: " + item.getDescription(
                item.getEntity().getLanguageId()) + "\n";

        String directory = Util.getSysProp("base_dir") + File.separator;

        FileWriter writer = null;
        try {
            writer = new FileWriter(directory + FILENAME, true);
            writer.write(output);
        } catch (Exception e) {
            throw new PluggableTaskException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) { }
            }
        }
    }
}
