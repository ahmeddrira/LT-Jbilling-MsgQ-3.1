package com.sapienter.jbilling.client.authentication;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.common.JBCrypto;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * An extension of the base spring security UsernamePasswordAuthenticationFilter that appends
 * the user entered company ID to the username for authentication.
 *
 * Similar to the UsernamePasswordAuthenticationFilter, the web form parameter names can be
 * configured via spring bean properties. 
 *
 * Default configuration:
 *      passwordParameter = "j_password"
 *      usernameParameter = "j_username"
 *      clientIdParameter = "j_client_id"
 *
 * @author Brian Cowdery
 * @since 04-10-2010
 */
public class CompanyUserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    public static final String FORM_CLIENT_ID_KEY = "j_client_id";
    public static final String VALUE_SEPARATOR = ":";

    private String clientIdParameter;
    
    /**
     * Returns the form submitted password as an encrypted string using jBillings
     * own crypto services. This encrypted password will be compared to the users
     * stored encrypted password for account authentication.
     *
     * @param request HTTP servlet request
     * @return encrypted password
     */
    @Override
    protected String obtainPassword(HttpServletRequest request) {
        JBCrypto cipher = JBCrypto.getPasswordCrypto(Constants.TYPE_ROOT);
        return cipher.encrypt(super.obtainPassword(request));        
    }

    /**
     * Returns the form submitted user name as colon delimited string containing
     * the user name and client id of the user to authenticate, e.g., "bob:1"
     * 
     * @param request HTTP servlet request
     * @return username string
     */
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        StringBuilder username = new StringBuilder();
        username.append(request.getParameter(getUsernameParameter()));
        username.append(VALUE_SEPARATOR);
        username.append(request.getParameter(getClientIdParameter()));

        return username.toString();
    }

    public final String getClientIdParameter() {
        return clientIdParameter == null ? FORM_CLIENT_ID_KEY : clientIdParameter;
    }

    public void setClientIdParameter(String clientIdParameter) {
        this.clientIdParameter = clientIdParameter;
    }
}
