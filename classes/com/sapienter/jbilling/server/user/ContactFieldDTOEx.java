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

/*
 * Created on Sep 16, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sapienter.jbilling.server.user;

import com.sapienter.jbilling.server.entity.ContactFieldDTO;
import com.sapienter.jbilling.server.entity.ContactFieldTypeDTO;

public class ContactFieldDTOEx extends ContactFieldDTO {

    private ContactFieldTypeDTO type = null;
    /**
     * 
     */
    public ContactFieldDTOEx() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param content
     * @param typeId
     */
    public ContactFieldDTOEx(Integer id, String content, Integer typeId) {
        super(id, content, typeId);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param otherValue
     */
    public ContactFieldDTOEx(ContactFieldDTO otherValue) {
        super(otherValue);
        // TODO Auto-generated constructor stub
    }

    public ContactFieldTypeDTO getType() {
        return type;
    }
    public void setType(ContactFieldTypeDTO type) {
        this.type = type;
    }
}
