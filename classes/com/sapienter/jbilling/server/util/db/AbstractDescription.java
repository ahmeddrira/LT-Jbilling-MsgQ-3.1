/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.util.db;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.util.db.generated.JbillingTableDAS;

public abstract class AbstractDescription implements Serializable {
	
    private static final Logger LOG = Logger.getLogger(AbstractDescription.class);
	private String description = null;
	
	abstract public int getId();
	abstract protected String getTable();
	
    /**
     * Returns the description.
     * @return String
     */
    public String getDescription(Integer languageId) {
        return getDescription(languageId, "description");
    }
    
    public String getDescription(Integer languageId, String label) {
        if (label == null || languageId == null) {
            throw new SessionInternalError("Null parameters " + label + " " + languageId);
        }
        JbillingTableDAS jt = new JbillingTableDAS();
        DescriptionDAS de = new DescriptionDAS();
        InternationalDescriptionId iid = new InternationalDescriptionId(jt.findByName(
                getTable()).getId(), getId(), label, languageId);
        InternationalDescription desc = de.findNow(iid);
        
        if (desc != null) {
            return desc.getContent();
        } else {
            LOG.debug("Description not set for " + iid);
            return null;
        }
        
    }
    /**
     * Sets the description.
     * @param description The description to set
     */
    public void setDescription(String labelProperty, Integer languageId) {
        JbillingTableDAS jt = new JbillingTableDAS();
        
        InternationalDescriptionId iid = new InternationalDescriptionId(jt.findByName(
        		getTable()).getId(), getId(), "description", languageId);
        InternationalDescription desc = new InternationalDescription(iid, labelProperty);
        
        DescriptionDAS de = new DescriptionDAS();
        de.save(desc);
    }
    
    public String getDescription() {
    	if (description == null) {
    		return getDescription(1);
    	} else {
    		return description;
    	}
    }
    public void setDescription(String text) {
    	description = text;
    }
}
