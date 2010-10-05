package com.sapienter.jbilling.client.authentication;

import org.springframework.security.core.GrantedAuthority;

/**
 * Interface for jBilling authoritative classes. These authorities require initialization to
 * ensure that the authority value is available to Spring Security regardless of their
 * dependency on hibernate look-ups, lazy-initialization and other external factors.
 *
 * @author Brian Cowdery
 * @since 05-10-2010
 */
public interface InitializingGrantedAuthority extends GrantedAuthority {
    public void initializeAuthority();
}
