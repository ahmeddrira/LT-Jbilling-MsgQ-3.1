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

package com.sapienter.jbilling.server.pluggableTask;


public interface PluggableTaskSQL {
        
    // Yet another sad example of excesive locking with entity beans
    // This query was then taken out for direct SQL
    static final String findByEntity = 
        "SELECT t.id, t.entity_id, t.type_id, t.processing_order " +
        "  FROM pluggable_task t, pluggable_task_type ty " +
        " WHERE t.entity_id = ? " +
        "   AND ty.category_id = ? " +
        "   AND t.type_id = ty.id " +
        "ORDER BY t.processing_order";

    
}




