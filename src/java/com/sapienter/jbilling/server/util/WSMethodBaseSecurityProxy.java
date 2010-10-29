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
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import grails.plugins.springsecurity.SpringSecurityService;
import org.apache.log4j.Logger;

/**
 * @author Emil
 */
public abstract class WSMethodBaseSecurityProxy extends MethodBaseSecurityProxy {

    private static final Logger LOG = Logger.getLogger(WSMethodBaseSecurityProxy.class);


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

    /**
     * Validates that the given owningUserId resides under the same entity as authenticated
     * user account making the web-service call.
     *
     * This check ensures that only data belonging to the logged in entity is accessed.
     *
     * @param owningUserId user id owning the data being returned
     * @throws SecurityException throw if user is accessing data that does not belonging to them
     */
    protected void validate(Integer owningUserId) throws SecurityException {
        if (!getSpringSecurityService().isLoggedIn())
                throw new SecurityException("Web-service call has not been authenticated.");

        UserDAS userDas = new UserDAS();

        // validate that owning user ID has been persisted (not a transient user)
        if (owningUserId != null && userDas.isIdPersisted(owningUserId)) {
            UserDTO user = userDas.find(owningUserId);
            Integer entityId = user.getCompany().getId();

            // validate that the owning user ID resides under the same entity as the web-service caller
            if (!getCallerCompanyId().equals(entityId))
                throw new SecurityException("Unauthorized access to entity " + entityId
                                            + " by caller '" + getCallerUserName() + "' (id " + getCallerId() + ")");

        } else {
            LOG.warn("Data accessed does not belong to a specific user, or belongs to a transient user.");
        }
    }
}
