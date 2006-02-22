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

package com.sapienter.jbilling.server.order;

/**
 * @author Emil
 */
public interface OrderSQL {

    // This one is used for root and clerks
    static final String listInternal = 
        "select po.id, po.id, bu.user_name, c.organization_name , po.create_datetime " + 
        "  from purchase_order po, base_user bu, contact c " +
        "  where po.deleted = 0  " +
        "  and bu.entity_id = ? " +
        "  and po.user_id = bu.id " +
        "  and c.user_id = bu.id ";

    // PARTNER: will show only customers that belong to this partner
    static final String listPartner = 
        "select po.id, po.id, bu.user_name, c.organization_name, po.create_datetime " +
        "  from purchase_order po, base_user bu, customer cu, partner pa, contact c " +
        " where po.deleted = 0 " +
        "   and bu.entity_id = ? " +
        "   and po.user_id = bu.id" +
        "   and cu.partner_id = pa.id " +
        "   and pa.user_id = ? " +
        "   and cu.user_id = bu.id " +
        "   and c.user_id = bu.id ";

    static final String listCustomer = 
        "select po.id, po.id, bu.user_name, c.organization_name, po.create_datetime " +
        "  from purchase_order po, base_user bu, contact c " +
        " where po.deleted = 0 " +
        "   and po.user_id = ? " +
        "   and po.user_id = bu.id " +
        "   and c.user_id = bu.id ";

    static final String listByProcess = 
        "select po.id, po.id, bu.user_name, po.create_datetime " +
        "  from purchase_order po, base_user bu, billing_process bp, order_process op "+
        " where bp.id = ? " +
        "   and po.user_id = bu.id " +
        "  and op.billing_process_id = bp.id " + 
        "  and op.order_id = po.id " +
        "  order by 1 desc";
    
    static final String getAboutToExpire =
    	"select purchase_order.id, purchase_order.active_until, " +
        "       purchase_order.notification_step " +
    	" from purchase_order, base_user " +
    	"where active_until >= ? " +
    	"  and active_until <= ? " +
    	"  and notify = 1 " +
    	"  and purchase_order.status_id = 1 " +
    	"  and user_id = base_user.id " +
    	"  and entity_id = ? " +
        "  and (notification_step is null or notification_step < ?)";
    
    static final String getLatest =
        "select max(id) " +
        "  from purchase_order " +
        " where user_id = ?" +
        "   and deleted = 0";
    
    static final String getByUserAndPeriod =
        "select id " +
        "  from purchase_order " +
        " where user_id = ? " +
        "   and period_id = ? " +
        "   and deleted = 0";

}
