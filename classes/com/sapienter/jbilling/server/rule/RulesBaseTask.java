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
package com.sapienter.jbilling.server.rule;

import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.StatelessSession;

/**
 *
 * @author emilc
 */
public abstract class RulesBaseTask extends PluggableTask {
    
    protected Logger LOG = setLog(); // to be set by the real plug-in

    private StatelessSession statelessSession = null;
    protected Vector<Object> rulesMemoryContext = new Vector<Object>();

    protected void executeRules() throws TaskException {
        // show what's in first
        for (Object o: rulesMemoryContext) {
        	LOG.debug("in memory context=" + o);
        }



        RuleBase ruleBase;
        try {
            ruleBase = readRule();
            statelessSession = ruleBase.newStatelessSession();
        } catch (Exception e) {
            throw new TaskException(e);
        }

        // add the log object for the rules to use
        statelessSession.setGlobal("LOG", LOG);
        statelessSession.executeWithResults(rulesMemoryContext);
    }

    protected abstract Logger setLog();
}
