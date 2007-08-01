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

import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.interfaces.PluggableTaskEntityLocal;
import com.sapienter.jbilling.interfaces.PluggableTaskEntityLocalHome;
import com.sapienter.jbilling.server.list.ResultList;

public class PluggableTaskManager extends ResultList implements PluggableTaskSQL {

    private Vector<Integer> classes = null;
    private Iterator it = null;
    private Logger log = null;
    private int lastProcessingOrder;
	private JNDILookup factory;
	private PluggableTaskEntityLocalHome pluggableTaskHome;
	
	private void init() 
			throws NamingException{
        factory = JNDILookup.getFactory(false);
		pluggableTaskHome = (PluggableTaskEntityLocalHome) factory.lookUpLocalHome(
		                    PluggableTaskEntityLocalHome.class,
		                    PluggableTaskEntityLocalHome.JNDI_NAME);
	}

    public PluggableTaskManager(Integer entityId, Integer taskCategory)
        throws PluggableTaskException {

        log = Logger.getLogger(PluggableTaskManager.class);

        try {
        	init();
			lastProcessingOrder = 0;
            prepareStatement(PluggableTaskSQL.findByEntity); 
            cachedResults.setInt(1, entityId.intValue()); 
            cachedResults.setInt(2, taskCategory.intValue());
            execute();
            conn.close();
            
            classes = new Vector<Integer>();
            while (cachedResults.next()) {
                classes.add(cachedResults.getInt(1));
            }
            it = classes.iterator();
            log.debug("total classes = " + classes.size());
            
        } catch (Exception e) {
            throw new PluggableTaskException(e);
        } 

    }

    public Object getNextClass() throws PluggableTaskException {
        if (it != null && it.hasNext()) {
            Integer ruleId = (Integer) it.next();
            PluggableTaskEntityLocal aRule;
            try {
                aRule = pluggableTaskHome.findByPrimaryKey(ruleId);
            } catch (FinderException e1) {
                throw new PluggableTaskException(e1);
            }

            // check if the order by is in place
            int processingOrder = aRule.getProcessingOrder().intValue();
            // this is helpful also to indetify bad data in the table
            if (processingOrder <= lastProcessingOrder) {
                // means that the results are not ordered !
                log.fatal("Results of processing tasks are not orderd");
                throw new PluggableTaskException("Processing tasks not orderd");
            }
            lastProcessingOrder = processingOrder;

            String className = aRule.getType().getClassName();
            String interfaceName =
                aRule.getType().getCategory().getInterfaceName();

            log.debug("Applying task " + className);
            try {
                Class task = Class.forName(className);
                Class taskInterface = Class.forName(interfaceName);

                if (taskInterface.isAssignableFrom(task)) {
                    Object thisTask = task.newInstance();
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
