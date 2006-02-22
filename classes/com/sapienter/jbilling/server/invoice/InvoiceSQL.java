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

package com.sapienter.jbilling.server.invoice;


public interface InvoiceSQL {

    static final String payableByUser = 
       "select i.id, i.public_number, i.id, i.create_datetime, i.due_date, " +
        "       c.symbol, i.total, i.balance " +
        "  from invoice i, currency c " +
        " where i.user_id = ? " +
		"   and (i.balance >= 0.01 or i.balance <= -0.01) " +
        "   and i.is_review = 0 " +
        "   and i.currency_id = c.id " +
        "   and i.deleted = 0 " +
        " order by 1 desc";

    // Internal gets all the invoices ever
    static final String internalList = 
        "select i.id, i.public_number, bu.user_name, i.id, i.create_datetime, i.due_date, " +
        "       c.symbol, i.total, i.balance, i.to_process " +
        "  from invoice i, base_user bu, currency c " +
        " where i.user_id = bu.id " +
        "   and i.currency_id = c.id " +
        "   and i.is_review = 0 " +
        "   and i.deleted = 0 " +
        " order by 1 desc";
        
    // Root-Clerk gets all the entity's invoices
    static final String rootClerkList = 
        "select i.id, i.public_number, bu.user_name, co.organization_name,i.id, i.create_datetime, " + 
        "       c.symbol, i.total, i.balance " +
        "  from invoice i, base_user bu, currency c , contact co " +
        " where i.user_id = bu.id " +
        "   and i.currency_id = c.id " +
        "   and bu.entity_id = ? " +
        "   and i.is_review = 0 " +
        "   and i.deleted = 0 " +
        "   and co.user_id = bu.id ";

    // The partner get's only its users
    static final String partnerList = 
        "select i.id, i.public_number, bu.user_name, co.organization_name,i.id, i.create_datetime, " + 
        "       c.symbol, i.total, i.balance " +
        "  from invoice i, base_user bu, partner pa, contact co, " +
        "       customer cu, currency c " +
        " where i.user_id = bu.id " +
        "   and i.currency_id = c.id " +
        "   and bu.entity_id = ? " +
        "   and cu.partner_id = pa.id " +
        "   and pa.user_id = ? " +
        "   and i.is_review = 0 " +
        "   and cu.user_id = bu.id " +        
        "   and i.deleted = 0 " +
        "   and co.user_id = bu.id ";


    // A customer only sees its own
    static final String customerList = 
        "select i.id, i.public_number, bu.user_name, co.organization_name,i.id, i.create_datetime, " + 
        "       c.symbol, i.total, i.balance " +
        "  from invoice i, base_user bu, currency c, contact co " +
        " where i.user_id = bu.id " +
        "   and i.currency_id = c.id " +
        "   and bu.id = ? " +
        "   and i.is_review = 0 " +
        "   and i.deleted = 0 " +
        "   and co.user_id = bu.id ";
        

    // Invoices generated in a billing process
    static final String processList = 
        "select i.id, i.public_number, i.id, bu.user_name, co.organization_name, i.due_date, c.symbol, i.total, i.to_process " +
        "  from invoice i, base_user bu, currency c, contact co " +
        " where i.billing_process_id = ? " +
        "   and bu.id = i.user_id " +
        "   and i.currency_id = c.id " +
        "   and i.deleted = 0 " +
        "   and co.user_id = bu.id " +
        " order by 1 desc";

    // Last invoice id for a user
    static final String lastIdbyUser =
    "select max(i.id) " +
    "  from invoice i, base_user bu " +
    " where bu.user_name = ? " +
    "   and bu.entity_id = ? " +
    "   and i.user_id = bu.id " +
    "   and i.deleted = 0 " +
    "   and i.is_review = 0";
    
    static final String previous = 
    "select max(i.id) " +
    "  from invoice i " +
    " where i.user_id = ? " +
    "   and i.deleted = 0 " +
    "   and i.is_review = 0" +
    "   and i.id < ?";
        
    
    // All the invoices to send reminders
    static final String toRemind  = 
    "select i.id " +
    "  from invoice i, base_user b " +
    " where i.user_id = b.id " +
    "   and b.deleted = 0 " +
    "   and i.deleted = 0 " +
    "   and i.is_review = 0 " +
    "   and i.to_process = 1 " +
    "   and i.due_date > ? " +
    "   and i.create_datetime <= ? " +
    "   and (i.last_reminder is null or " +
    "        i.last_reminder <= ?)" +
    "   and b.entity_id = ?";
    
    // For the overdue interest calculation only
    // All the invoices that are going overdue
    // Overdue step, 0 means no further process required
    static final String overdue =
    "select i.id " +
    "  from invoice i, base_user b" +
    " where i.user_id = b.id " +
    "   and b.entity_id = ? " +
    "   and i.is_review = 0 " +
    "   and i.due_date < ? " +
    "   and i.deleted = 0 " +
    "   and ( (i.to_process = 1 and i.delegated_invoice_id is null) or" +
    "         (i.to_process = 0 and i.delegated_invoice_id is not null) )" +
    "   and (i.overdue_step != 0 or i.overdue_step is null)";

    // Invoice in ageing: any invoices that make this user applicable 
    // to the ageing process (then what happends depends on the ageing config)
    static final String getOverdueForAgeing =
    "select i.id " +
    "  from invoice i " +
    "  where i.is_review = 0 " +
    "  and i.due_date < ? " +
    "  and i.deleted = 0 " +
    "  and i.user_id = ? " +
    "  and i.to_process = 1 " +
    "  and i.id != ?";
 

    // All the invoices created for a period of time
    static final String getByDate =
    "select i.id " +
    "  from invoice i, base_user b " +
    " where b.entity_id = ? " +
    "   and i.user_id = b.id " +
    "   and i.is_review = 0 " +
    "   and i.deleted = 0 " +
    "   and i.create_timestamp >= ? " +
    "   and i.create_timestamp < ? ";
}


