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
