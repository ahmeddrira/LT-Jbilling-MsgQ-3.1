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

package com.sapienter.jbilling.server.customer;

import com.sapienter.jbilling.server.util.Constants;

/**
 * @author Emil
 */
public interface CustomerSQL {

    // ROOT: all the STAFF users within its entity
    static final String listRoot = 
        "select c.id, c.id, a.organization_name, a.last_name, a.first_name, " +
		"       c.user_name " +
    	"from contact a, contact_map b, base_user c, jbilling_table d, " +
        "     contact_type ct, user_role_map urm " +    	"where a.id = b.contact_id" +    	"  and b.foreign_id = c.id" +    	"  and b.table_id = d.id" +
        "  and b.type_id = ct.id " +
        "  and ct.is_primary = 1 " +    	"  and d.name = 'base_user'" +
    	"  and c.deleted = 0 " +
    	"  and a.deleted = 0 " +
        "  and c.id = urm.user_id " +
        "  and urm.role_id in (2,3,4) " + // no customers or internals    	"  and c.entity_id = ? " +
        " order by 3,4,5";


    // CLERK: same as root, but restricted to customers and partners
    static final String listClerk = 
        "select c.id, c.id, a.organization_name, a.last_name, a.first_name, " +
		"       c.user_name " +
        "from contact a, contact_map b, base_user c, jbilling_table d, " +
        "     contact_type ct, user_role_map urm  " +
        "where a.id = b.contact_id" +
        "  and b.foreign_id = c.id" +
        "  and b.type_id = ct.id " +
        "  and ct.is_primary = 1 " +
        "  and b.table_id = d.id" +
        "  and d.name = 'base_user'" +
        "  and c.id = urm.user_id " +
        "  and urm.role_id in (3,4) " + // no customers or internals or admins
        "  and c.deleted = 0 " +
        "  and a.deleted = 0 " +
        "  and c.entity_id = ? " +
        " order by 3,4,5";

    // PARTNER: will show only customers that belong to this partner
    static final String listPartner = 
        "select c.id, c.id, a.organization_name, a.last_name, a.first_name, " +
		"       c.user_name " +
        "from contact a, contact_map b, base_user c, jbilling_table d, " +
        "     customer cu, partner pa, " +
        "     contact_type ct " +
        "where a.id = b.contact_id" +
        "  and b.foreign_id = c.id" +
        "  and b.table_id = d.id" +
        "  and d.name = 'base_user'" +
        "  and c.deleted = 0 " +
        "  and b.type_id = ct.id " +
        "  and ct.is_primary = 1 " +
        "  and a.deleted = 0 " +
        "  and c.entity_id = ? " +
        "  and cu.partner_id = pa.id " +
        "  and pa.user_id = ? " +
        "  and cu.user_id = c.id ";
    
    // customer list, takes only customers.
    // it excluded sub-accounts (child customers)
    static final String listCustomers = 
        "select c.id, c.id, a.organization_name, a.last_name, a.first_name, " +
		"       c.user_name " +
        "from contact a, contact_map b, base_user c, jbilling_table d, " +
        "     user_role_map urm, customer cu, " +
        "     contact_type ct " +
        "where a.id = b.contact_id" +
        "  and b.foreign_id = c.id" +
        "  and b.table_id = d.id" +
        "  and b.type_id = ct.id " +
        "  and ct.is_primary = 1 " +
        "  and d.name = 'base_user'" +
        "  and c.entity_id = ? " +
        "  and c.deleted = 0 " +
        "  and a.deleted = 0 " +
        "  and c.status_id != 8 " +
        "  and c.id = urm.user_id " +        "  and urm.role_id = " + Constants.TYPE_CUSTOMER +
        "  and cu.user_id = c.id " +
        "  and ( cu.parent_id is null or cu.invoice_child = 1)";
    
    static final String listCustomersCCFiler = 
        "and c.id in ( " +
        "   select ma.user_id " +
        "     from user_credit_card_map ma, credit_card cc " +
        "    where cc.id = ma.credit_card_id " +
        "      and cc.cc_number_plain like ? " +
        ")";

    // sub-accounts: all the customers belonigng to another customer
    static final String listSubaccounts = 
        "select c.id, c.id, a.organization_name, a.last_name, a.first_name, " +
        "       c.user_name " +
        "from contact a, contact_map b, base_user c, jbilling_table d, " +
        "     user_role_map urm, customer cu, " +
        "     contact_type ct " +
        "where a.id = b.contact_id" +
        "  and b.foreign_id = c.id" +
        "  and b.table_id = d.id" +
        "  and b.type_id = ct.id " +
        "  and ct.is_primary = 1 " +
        "  and d.name = 'base_user'" +
        "  and cu.parent_id = ? " +
        "  and c.deleted = 0 " +
        "  and a.deleted = 0 " +
        "  and c.status_id != 8 " +
        "  and c.id = urm.user_id " +
        "  and urm.role_id = " + Constants.TYPE_CUSTOMER +
        "  and cu.user_id = c.id " +
        " order by 3,4,5";

}
