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

import com.sapienter.jbilling.interfaces.UserEntityLocal;
import com.sapienter.jbilling.server.notification.MessageDTO;

/*
 * Each task is resposaible of verifying if it should run or not,
 * for example, an custom email task for an entity can extend the
 * basic email task, and then perform a verification if that user
 * has subscribed or not to that particular type of message.
 * Eventually, a method like getPreferredDeliveryType could be
 * provided.   
 */
public interface NotificationTask {
    public void deliver(UserEntityLocal user, MessageDTO sections)
            throws TaskException;
}
