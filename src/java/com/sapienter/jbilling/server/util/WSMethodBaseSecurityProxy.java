/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * Created on Dec 24, 2003
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.util;

import javax.naming.NamingException;

import com.sapienter.jbilling.client.authentication.CompanyUserDetails;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.user.UserBL;
import grails.plugins.springsecurity.SpringSecurityService;

/**
 * @author Emil
 */
public abstract class WSMethodBaseSecurityProxy extends MethodBaseSecurityProxy {

    protected SpringSecurityService getSpringSecurityService() {
        return Context.getBean(Context.Name.SPRING_SECURITY_SERVICE);
    }

    protected Integer getCallerId() {
        CompanyUserDetails details = (CompanyUserDetails) getSpringSecurityService().getPrincipal();
        return details.getUserId();
    }

    protected Integer getCallerCompanyId() {
        CompanyUserDetails details = (CompanyUserDetails) getSpringSecurityService().getPrincipal();
        return details.getCompanyId();
    }

    protected String getCallerUserName() {
        CompanyUserDetails details = (CompanyUserDetails) getSpringSecurityService().getPrincipal();
        return details.getUsername();
    }
    
    protected void validate(Integer callerId) 
            throws SecurityException, SessionInternalError, NamingException {
        // get the user. Since this is called only by root users, the username
        // is unique and the entity is that of the user
        String user = getCallerUserName();
        UserBL bl = new UserBL();
        if (callerId == null || !bl.validateUserBelongs(user, callerId)) {
            throw new SecurityException("Unauthorize access by" + user + " to user " + 
                    callerId);
        }
    }
}
