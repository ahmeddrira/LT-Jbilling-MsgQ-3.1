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
package com.sapienter.jbilling.server.mediation.task;

import java.util.Vector;

import org.apache.log4j.Logger;


import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.pluggableTask.TaskException;
import com.sapienter.jbilling.server.rule.RulesBaseTask;
import com.sapienter.jbilling.server.user.db.CompanyDAS;

public class RulesMediationTask extends RulesBaseTask implements
        IMediationProcess {

    private Vector<MediationResult> results = new Vector();

    protected Logger setLog() {
        return Logger.getLogger(RulesMediationTask.class);
    }
        
    public Vector<MediationResult> process(Vector<Record> records, String configurationName)
            throws TaskException {
 
	results.clear(); // delete this line for endless pain debugging
        for (Record record: records) {
            // one result per record
            MediationResult result = new MediationResult(configurationName);
            rulesMemoryContext.add(result);
            results.add(result); // for easy retrival later

            for (PricingField field: record.getFields()) {
                field.setResultId(result.getId());
                rulesMemoryContext.add(field);
            }
        }

        // add the company
        rulesMemoryContext.add(new CompanyDAS().find(getEntityId()));
        
        // then execute the rules
        executeRules();
        
        return results;
    }
}
