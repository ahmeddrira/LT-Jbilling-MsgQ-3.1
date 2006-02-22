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

package com.sapienter.jbilling.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

public class SessionInternalError extends Exception {
    public SessionInternalError() {
    }

    public SessionInternalError(String s) {
        super(s);
    }
    
    
    /**
     * Method SessionInternalError.
     * Gets the original exception as a parameter an logs the message
     * and whole stack trece.
     * @param e
     */
    public SessionInternalError(Exception e) {
        super(e.getMessage());
        
        Logger log = Logger.getLogger("com.sapienter.jbilling");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();

        log.fatal("Internal error: " + e.getMessage() +
                "\n" + sw.toString());
    }
}
