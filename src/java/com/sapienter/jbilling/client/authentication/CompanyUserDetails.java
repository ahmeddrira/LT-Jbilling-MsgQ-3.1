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

    private final Integer companyId;

    public CompanyUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                              boolean credentialsNonExpired, boolean accountNonLocked,
                              Collection<GrantedAuthority> authorities, Integer id, Integer companyId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id);
        this.companyId = companyId;
    }

    /**
     * Returns the user ID as an Integer. This is the same as calling {@link #getId()}.
     * 
     * @return user ID
     */
    public Integer getUserId() {
        return (Integer) getId();
    }

    /**
     * Returns the users company ID.
     *
     * @return user company ID
     */
    public Integer getCompanyId() {
        return companyId;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CompanyUserDetails");
        sb.append("{id=").append(getId());
        sb.append(", username=").append("'").append(getUsername()).append("'");
        sb.append(", companyId=").append(getCompanyId());
        sb.append(", enabled=").append(isEnabled());
        sb.append(", accountExpired=").append(!isAccountNonExpired());  
        sb.append(", credentialsExpired=").append(!isCredentialsNonExpired());
        sb.append(", accountLocked=").append(!isAccountNonLocked());
        sb.append('}');
        return sb.toString();
    }
}
