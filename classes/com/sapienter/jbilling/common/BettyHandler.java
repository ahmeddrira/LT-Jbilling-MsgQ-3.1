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
