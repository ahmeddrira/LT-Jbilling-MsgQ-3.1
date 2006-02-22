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
