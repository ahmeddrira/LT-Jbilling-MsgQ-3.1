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
package com.sapienter.jbilling.server.util.api;

/**
 * This is a checked exception because the client should always catch it and decide
 * what to do. A failure on a call is not necessary an unrecoverable error.
 * 
 * @author Emiliano Conde
 *
 */
public class JbillingAPIException extends Exception {
    public JbillingAPIException() {
        super();
    }

    public JbillingAPIException(String s) {
        super(s);
    }
}