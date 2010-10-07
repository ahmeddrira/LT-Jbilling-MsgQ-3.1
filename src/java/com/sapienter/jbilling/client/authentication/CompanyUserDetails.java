/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.client.authentication;

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * A spring security UserDetails implementation that includes the users company id.
 *
 * @author Brian Cowdery
 * @since 04-10-2010
 */
public class CompanyUserDetails extends GrailsUser {

    private final Integer entityId;

    public CompanyUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                              boolean credentialsNonExpired, boolean accountNonLocked,
                              Collection<GrantedAuthority> authorities, Integer id, Integer entityId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id);
        this.entityId = entityId;
    }

    public Integer getEntityId() {
        return entityId;
    }
}
