/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

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

import com.sapienter.jbilling.server.user.contact.db.ContactFieldDTO;

import java.io.Serializable;

/**
 * ContactFieldWS
 *
 * @author Brian Cowdery
 * @since 1-Aug-2011
 */
public class ContactFieldWS implements Serializable {

    private Integer typeId;
    private String content;

    public ContactFieldWS() {
    }

    public ContactFieldWS(Integer typeId, String content) {
        this.typeId = typeId;
        this.content = content;
    }

    public ContactFieldWS(ContactFieldDTO dto) {
        this.typeId = dto.getType() != null ? dto.getType().getId() : null;
        this.content = dto.getContent();
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
