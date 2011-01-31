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
package com.sapienter.jbilling.server.user.contact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sapienter.jbilling.server.user.contact.db.ContactFieldDTO;
import com.sapienter.jbilling.server.user.contact.db.ContactFieldTypeDTO;
import com.sapienter.jbilling.server.util.InternationalDescriptionWS;
import com.sapienter.jbilling.server.util.db.LanguageDTO;

public class ContactFieldTypeWS  implements java.io.Serializable {


	private int id;
	private Integer companyId;
	private String promptKey;
	private String dataType;
	private Integer readOnly;
	private Set<ContactFieldDTO> contactFields = new HashSet<ContactFieldDTO>(0);
	private List<InternationalDescriptionWS> descriptions = new ArrayList<InternationalDescriptionWS>();

    public ContactFieldTypeWS() {
    	
    }

    public ContactFieldTypeWS(ContactFieldTypeDTO contactFieldType, List<LanguageDTO> languages) {
    	this.id = contactFieldType.getId();
        this.dataType=contactFieldType.getDataType();
        this.readOnly= contactFieldType.getReadOnly();

        if (contactFieldType.getEntity() != null) {
        	this.companyId = contactFieldType.getEntity().getId();
        }

        for(ContactFieldDTO dto: contactFieldType.getContactFields()) {
        	contactFields.add(dto);
        }

        for (LanguageDTO language : languages) {
            descriptions.add(new InternationalDescriptionWS(contactFieldType.getDescriptionDTO(language.getId())));
        }
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getPromptKey() {
		return promptKey;
	}

	public void setPromptKey(String promptKey) {
		this.promptKey = promptKey;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Integer readOnly) {
		this.readOnly = readOnly;
	}

	public Set<ContactFieldDTO> getContactFields() {
		return contactFields;
	}

	public void setContactFields(Set<ContactFieldDTO> contactFields) {
		this.contactFields = contactFields;
	}

	public List<InternationalDescriptionWS> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<InternationalDescriptionWS> descriptions) {
		this.descriptions = descriptions;
	}
    
}


