/*
    jbilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2007 Sapienter Billing Software Corp. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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
