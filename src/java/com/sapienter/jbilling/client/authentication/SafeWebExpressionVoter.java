package com.sapienter.jbilling.client.authentication;

import org.codehaus.groovy.grails.plugins.springsecurity.WebExpressionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * An extension of the base grails security core WebExpressionVoter that abstains when an
 * expression cannot be evaluated.
 *
 * This is only necessary because all secured path attributes are wrapped as a WebExpressionConfigAttribute
 * that will be parsed by the voter, even if the attribute is simply a plain-text permission string and
 * not an expression.
 *
 * @author Brian Cowdery
 * @since 05-10-2010
 */
public class SafeWebExpressionVoter extends WebExpressionVoter {

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        try {
            return super.vote(authentication, object, attributes);
        } catch (IllegalArgumentException e) {
            return ACCESS_ABSTAIN;
        }
    }
}
