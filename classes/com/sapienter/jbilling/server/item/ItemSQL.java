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
 * Created on 27-Feb-2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.item;

/**
 * @author Emil
 */
public interface ItemSQL {
    
    // the general list of items, shows always the description of
    // the entity. This then prevents items not showing up because
    // the logged user has a differenct language
	static final String list = 
		"select a.id, a.id, a.internal_number, b.content " +
		"  from item a, international_description b, jbilling_table c," +
        "       entity e " +
		" where a.entity_id = e.id " +
        "   and e.id = ? " +
		"   and a.deleted = 0 " +
        "   and b.table_id = c.id " +
        "   and c.name = 'item' " +
        "   and b.foreign_id = a.id " +
        "   and b.language_id = e.language_id " +
        "   and b.psudo_column = 'description' " +
        " order by a.internal_number";

    static final String listType = 
        "select a.id, a.id, a.description " +
        "  from item_type a " +
        " where a.entity_id = ? ";

    static final String listUserPrice = 
        "select d.id, a.id, a.internal_number, b.content, d.price " +
        "  from item a, international_description b, jbilling_table c, " + 
        "       item_user_price d " +
        " where a.entity_id = ? " +
        "   and d.user_id = ? " +
        "   and a.id = d.item_id " +
        "   and a.deleted = 0 " +
        "   and b.table_id = c.id " +
        "   and c.name = 'item' " +
        "   and b.foreign_id = a.id " +
        "   and b.language_id = ? " +
        "   and b.psudo_column = 'description' " +
        " order by 1";

    static final String listPromotion = 
        "select b.id, b.code, b.since, b.until, b.once, c.content" +
        "  from item a, promotion b, international_description c, jbilling_table d  " +
        " where a.entity_id = ? " +
        "   and a.deleted = 0 " +
        "   and c.table_id = d.id " +
        "   and d.name = 'item' " +
        "   and c.foreign_id = a.id " +
        "   and c.language_id = ? " +
        "   and c.psudo_column = 'description' " +
        "   and a.id = b.item_id " +
        " order by 1";

}
