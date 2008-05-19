package com.sapienter.jbilling.server.util.db;

import java.io.Serializable;

import javax.persistence.Transient;

import com.sapienter.jbilling.server.util.db.generated.JbillingTableDAS;

public abstract class AbstractDescription implements Serializable {
	
	private String description = null;
	
	abstract public int getId();
	abstract protected String getTable();
	
    /**
     * Returns the description.
     * @return String
     */
    @Transient
    public String getDescription(Integer languageId) {
        JbillingTableDAS jt = new JbillingTableDAS();
        
        DescriptionDAS de = new DescriptionDAS();
        InternationalDescriptionId iid = new InternationalDescriptionId(jt.findByName(
        		getTable()).getId(), getId(), "description", languageId);
        InternationalDescription desc = de.find(iid);
        
        return desc.getContent();
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
