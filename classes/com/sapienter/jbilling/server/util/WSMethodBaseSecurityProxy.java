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
 * Created on Dec 24, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.util;

import javax.naming.NamingException;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.user.UserBL;

/**
 * @author Emil
 */
public abstract class WSMethodBaseSecurityProxy extends MethodBaseSecurityProxy {

    protected void validate(Integer callerId) 
            throws SecurityException, SessionInternalError, NamingException {
        // get the user. Since this is called only by root users, the username
        // is unique and the entity is that of the user
        String user = context.getCallerPrincipal().getName();
        UserBL bl = new UserBL();
        if (callerId == null || !bl.validateUserBelongs(user, callerId)) {
            throw new SecurityException("Unauthorize access to user " + 
                    callerId);
        }
    }
}
