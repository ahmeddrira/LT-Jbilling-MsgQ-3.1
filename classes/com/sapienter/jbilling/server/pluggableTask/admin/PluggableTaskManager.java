/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * Created on Apr 15, 2003
 *
 */
package com.sapienter.jbilling.server.pluggableTask.admin;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.server.pluggableTask.PluggableTask;

public class PluggableTaskManager<T> {

    private static final Logger LOG = Logger.getLogger(PluggableTaskManager.class);
    private Vector<PluggableTaskDTO> classes = null;
    private Iterator it = null;
    private int lastProcessingOrder;
	
    public PluggableTaskManager(Integer entityId, Integer taskCategory)
        throws PluggableTaskException {

        try {
			lastProcessingOrder = 0;
            
            PluggableTaskDAS das = new PluggableTaskDAS();
            classes = new Vector<PluggableTaskDTO>();
            classes.addAll(das.findByEntityCategory(entityId, taskCategory));
            
            it = classes.iterator();
            LOG.debug("total classes = " + classes.size());
            
        } catch (Exception e) {
            throw new PluggableTaskException(e);
        } 
    }

    public T getNextClass() throws PluggableTaskException {
        if (it != null && it.hasNext()) {
            PluggableTaskDTO aRule = (PluggableTaskDTO) it.next();

            // check if the order by is in place
            int processingOrder = aRule.getProcessingOrder().intValue();
            // this is helpful also to indetify bad data in the table
            if (processingOrder <= lastProcessingOrder) {
                // means that the results are not ordered !
                LOG.fatal("Results of processing tasks are not orderd");
                throw new PluggableTaskException("Processing tasks not orderd");
            }
            lastProcessingOrder = processingOrder;

            String className = aRule.getType().getClassName();
            String interfaceName =
                aRule.getType().getCategory().getInterfaceName();

            LOG.debug("Applying task " + className);
            try {
                Class task = Class.forName(className);
                Class taskInterface = Class.forName(interfaceName);

                if (taskInterface.isAssignableFrom(task)) {
                    T thisTask = (T) task.newInstance();
                    ((PluggableTask) thisTask).initializeParamters(aRule);
                    return thisTask;

                } 
                throw new PluggableTaskException(
                    "The task "
                        + className
                        + " is not implementing "
                        + interfaceName);
                
            } catch (ClassNotFoundException e) {
                throw new PluggableTaskException(
                    "Can't find the classes for this"
                        + " task. Class: "
                        + className
                        + " Interface: "
                        + interfaceName,
                    e);
            } catch (Exception e) {
                throw new PluggableTaskException(e);
            } 
        } 
        
        return null;
        
    }

}
