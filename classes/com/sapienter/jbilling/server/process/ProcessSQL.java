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

package com.sapienter.jbilling.server.process;

public interface ProcessSQL {
    // Internal gets all the invoices ever
    static final String generalList =         "select id, id, billing_date " +
        "  from billing_process " +
        " where entity_id = ? " +
        "   and is_review = 0 " +
        " order by 1";

    static final String lastId =
        "select max(id) " +        "  from billing_process" +        " where entity_id = ?" +
        "   and is_review = 0 ";
    
    static final String findTodays =
        "select id " +
        "  from billing_process" +
        " where entity_id = ? " +
        "   and is_review = ? " +
        "   and billing_date = ?";

    // needed to avoid getting into a trasaction in the billingProcess.trigger
    // since Collections have to be in transactions
    static String findToRetry =
        "select id " +
        " from billing_process " + 
        "where entity_id = ? " +
        "  and is_review = 0 " +
        "  and retries_to_do > 0";

}


