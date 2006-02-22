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

package com.sapienter.jbilling.server.notification;

import com.sapienter.jbilling.server.util.Constants;

/**
 * @author Emil
 */
public interface NotificationSQL {

    static final String listTypes = 
        "select nmt.id, i.content " +
        "  from notification_message_type nmt, international_description i, " +
        "       jbilling_table bt " + 
        " where i.table_id = bt.id " +
        "   and bt.name = 'notification_message_type' " + 
        "   and i.foreign_id = nmt.id " + 
        "   and i.language_id = ? " +
        "   and i.psudo_column = 'description'";

    static final String allEmails = 
        "select c.email " +
        "  from base_user a, contact_map b, contact c, jbilling_table d, " +
        "       contact_type ct, user_role_map urm " +
        " where a.id = b.foreign_id " +
        "   and b.type_id = ct.id " +
        "   and a.id = urm.user_id " +
        "   and urm.role_id = " + Constants.TYPE_CUSTOMER +
        "   and ct.is_primary = 1 " +
        "   and b.table_id = d.id " +
        "   and b.contact_id = c.id " +
        "   and d.name = 'base_user' " +
        "   and c.email is not null " +
        "   and a.entity_id = ?";

}
