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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.agent.RuleAgent;

import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskDTO;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskParameterDTO;


public abstract class PluggableTask {
    protected HashMap<String, Object> parameters = null;
    Integer entityId = null;
    private static final Logger LOG = Logger.getLogger(PluggableTask.class);

    public void initializeParamters(PluggableTaskDTO task) 
            throws PluggableTaskException {
        Collection <PluggableTaskParameterDTO>DBparameters = task.getParameters();
        parameters = new HashMap<String, Object>();
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
        
        for(PluggableTaskParameterDTO parameter: DBparameters) {
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
    
    /**
     * Any pluggable task can get a rule base that takes the task's 
     * parameters as the configuration for the rule agent.
     * @return
     * @throws IOException
     * @throws Exception
     */
    protected RuleBase readRule() throws IOException, Exception {
        Properties rulesProperties = new Properties();
        
        for (String key: parameters.keySet()) {
            rulesProperties.setProperty(key, (String) parameters.get(key));
            LOG.debug("adding parameter " + key + " value " + 
                    rulesProperties.getProperty(key));
        }
        if (parameters.size() == 0) {
            String defaultDir = Util.getSysProp("base_dir") + "rules";
            rulesProperties.setProperty("dir", defaultDir);
            LOG.debug("No task parameters, using directory default:" + defaultDir);
        }
        RuleAgent agent = RuleAgent.newRuleAgent(rulesProperties);     
        return agent.getRuleBase(); 
    }
    
}
