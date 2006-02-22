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
 * Created on Mar 1, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.server.user;

import java.rmi.RemoteException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.AbstractServerLoginModule;

import com.sapienter.jbilling.common.JNDILookup;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;

/**
 * @author Emil
 */
public class BettyLoginModule extends AbstractServerLoginModule {

    /** The login identity */
    private Principal identity;
    /** The proof of login identity */
    private char[] credential;
    /** the principal to use when a null username and password are seen */
    private Principal unauthenticatedIdentity;

    
    Logger log;

    private Group[] roles;
    /**
     * 
     */
    public BettyLoginModule() {
        super();
    }

    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map sharedState, Map options)   {
        super.initialize(subject, callbackHandler, sharedState, options);
        // Check for unauthenticatedIdentity option.
        String name = (String) options.get("unauthenticatedIdentity");
        if (name != null) {
            unauthenticatedIdentity = new SimplePrincipal(name);
        }
    }
    
    /* (non-Javadoc)
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getIdentity()
     */
    protected Principal getIdentity() {
        log = Logger.getLogger(BettyLoginModule.class);
        log.debug("now getting called in getIdentity");

        return identity;
    }

    /* (non-Javadoc)
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getRoleSets()
     */
    protected Group[] getRoleSets() throws LoginException {
        log = Logger.getLogger(BettyLoginModule.class);
        log.debug("now getting called in getRoleSets");

        return roles;
    }

    public boolean login() throws LoginException {
        
        log = Logger.getLogger(BettyLoginModule.class);
        log.debug("now getting called in login!");
        // See if shared credentials exist
        if (super.login() == true) {
            log.debug("now getting called in login 1");
            // Setup our view of the user
            Object username = sharedState.get("javax.security.auth.login.name");
            if (username instanceof Principal)
                identity = (Principal) username;
            else {
                String name = username.toString();
                identity = new SimplePrincipal(name);
            }
            Object password = sharedState.get("javax.security.auth.login.password");
            if (password instanceof char[])
                credential = (char[]) password;
            else if (password != null) {
                String tmp = password.toString();
                credential = tmp.toCharArray();
            }
            
            log.debug("Returning A in login. identity " + getIdentity());
            return true;
        }
        
        log.debug("now getting called in login 2");
    
        super.loginOk = false;
        
        // find the username, password, and entityId as sent by the client
        // prompt for a username and password
        if (callbackHandler == null) {
            log.debug("now getting called in login 3");
            throw new LoginException(
                "Error: no CallbackHandler available "
                    + "to collect authentication information");
        }

        log.debug("now getting called in login 4");
        roles = new Group[1];
        roles[0] =  new SimpleGroup("Roles");

        NameCallback nc = new NameCallback("User name: ");
        PasswordCallback pc = new PasswordCallback("Password: ", false);
        TextInputCallback tic = new TextInputCallback("Company ID: ");
        Callback[] callbacks = { nc, pc, tic };
        String username = null;
        String password = null;
        Integer entityID = null;
        log.debug("now getting called in login 5");
        try {
            callbackHandler.handle(callbacks);
            username = nc.getName();
            char[] tmpPassword = pc.getPassword();
            if (tmpPassword != null) {
                credential = new char[tmpPassword.length];
                System.arraycopy(tmpPassword, 0, credential, 0, 
                        tmpPassword.length);
                pc.clearPassword();
                password = new String(credential);
            }
            
            String entityIdStr = tic.getText();
            entityID = (entityIdStr == null) ? null  
                                             : Integer.valueOf(tic.getText());
        } catch (java.io.IOException ioe) {
            log.debug("now getting called in login 5a");
            throw new LoginException(ioe.toString());
        } catch (UnsupportedCallbackException uce) {
            log.debug("now getting called in login 5b");
            // this means that the login is not being called from our
            // excplicit call in the login servlet. So you are not 
            // passing a username and password anyways ...
        }
        
        log.debug("username:" + username + " password " + password);
        if (username == null && password  == null) {
            identity = unauthenticatedIdentity;
        } else { 
        
            identity = new SimplePrincipal(username);
    
            UserDTOEx user;
            try {
                // Validate the password supplied by the subclass
                JNDILookup EJBFactory = JNDILookup.getFactory(false);
                UserSessionHome userHome =
                        (UserSessionHome) EJBFactory.lookUpHome(
                        UserSessionHome.class,
                        UserSessionHome.JNDI_NAME);

                UserSession myRemoteSession = userHome.create();
                user = myRemoteSession.getUserDTOEx(username, entityID);
                
            } /*catch (FinderException e) {
                throw new FailedLoginException("Invalid username/companyID");        
            } */catch (NamingException e) {
                throw new FailedLoginException("Internal error");
            } catch (RemoteException e) {
                throw new FailedLoginException("Internal error");
            } catch (SessionInternalError e) {
                throw new FailedLoginException("Internal error");
            } catch (CreateException  e) {
                throw new FailedLoginException("Internal error");
            }            
            
            if (password == null || !user.getPassword().equals(
                    password) || user.getDeleted().intValue() == 1) {
                throw new FailedLoginException("Password Incorrect/Password Required");
            }
            
            // initialize the roles
            for (Iterator it =  user.getRoles().iterator();
                    it.hasNext(); ) {
                Integer role = (Integer) it.next();
                roles[0].addMember( new SimplePrincipal(role.toString()));
            }
            
        }
        
        if (getUseFirstPass() == true) {
            // Add the username and password to the shared state map
            sharedState.put("javax.security.auth.login.name", username);
            sharedState.put("javax.security.auth.login.password", credential);
        }
        super.loginOk = true;
        log.debug("Returning true in login. username " + username + " password " + password +
                " identity " + getIdentity());
        return true;
    }

}
