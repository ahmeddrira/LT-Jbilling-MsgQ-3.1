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
package com.sapienter.jbilling.server.report;

/**
 * @author Emil
 */
public interface ReportSQL {
	static final String list = 
		"select a.id, a.titleKey " +
		"  from report a, report_entity_map b, report_type rt, report_type_map rtm " +
		" where b.entity_id = ? " +
        "   and a.id = rtm.report_id " +
        "   and rt.id = rtm.type_id " +
        "   and rt.showable = 1 " +
        "   and a.id = b.report_id";
}
