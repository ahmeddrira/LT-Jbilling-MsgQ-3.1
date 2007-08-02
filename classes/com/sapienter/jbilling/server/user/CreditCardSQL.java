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
 * Created on Jan 15, 2005
 *
 */
package com.sapienter.jbilling.server.user;

/**
 * @author Emil
 *
 */
public interface CreditCardSQL {
    static final String expiring =
        "select bu.id, cc.id " +
        " from base_user bu, credit_card cc, user_credit_card_map uccm " +
        "where bu.deleted = 0 " +
        "  and bu.status_id < " + UserDTOEx.STATUS_SUSPENDED +
        "  and cc.deleted = 0 " +
        "  and bu.id = uccm.user_id " +
        "  and cc.id = uccm.credit_card_id " +
        "  and cc.cc_expiry <= ?";
}
