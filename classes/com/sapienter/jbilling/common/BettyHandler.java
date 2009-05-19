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
 * Created on Mar 1, 2004
 *
 * Copyright Sapienter Enterprise Software
 */
package com.sapienter.jbilling.common;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.security.auth.callback.ObjectCallback;

/**
 * @author Emil
 */
public class BettyHandler implements CallbackHandler {

    private transient String username;
    private transient char[] password;
    private transient Object credential;
    private transient Integer entityId;

    /**
     * @param arg0
     * @param arg1
     */
    public BettyHandler(String username, char[] password, Integer entityId) {
        this.username = username;
        this.password = password;
        this.credential = password;
        this.entityId = entityId;
    }

    /**
     * We leave the handling of the username and password to the 
     * jboss handler. The only drawback for this is that there are
     * two loops over the callbacks
     */
    public void handle(Callback[] callbacks) throws
          UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            Callback c = callbacks[i];
            if (c instanceof NameCallback) {
                NameCallback nc = (NameCallback) c;
                nc.setName(username);
            } else if (c instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) c;
                if (password == null) {
                    // We were given an opaque Object credential but a char[] is requested?
                    if (credential != null) {
                        String tmp = credential.toString();
                        password = tmp.toCharArray();
                    }
                }
                pc.setPassword(password);
            } else if (c instanceof TextInputCallback) {
                // for the entity we used this text input callback, instead of
                // making our own ...
                TextInputCallback tic = (TextInputCallback) c;
                tic.setText(entityId.toString());
            } else if (c instanceof ObjectCallback) {
                ObjectCallback oc = (ObjectCallback) c;
                oc.setCredential(credential);
            } else {
                throw new UnsupportedCallbackException(
                    callbacks[i],
                    "Unrecognized Callback");
            }
        }
    }
}
