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
