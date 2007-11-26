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

import org.apache.log4j.Logger;

public class InvalidArgumentException extends RuntimeException {
    private final Integer code;
    private final Exception e;
    private static final Logger LOG = Logger.getLogger(InvalidArgumentException.class);
    
    public InvalidArgumentException(String message, Integer code, Exception e) {
        super(message);
        this.code = code;
        this.e = e;
        LOG.debug(message + ((e == null) ? "" : " - " + e.getMessage()));
    }

    public InvalidArgumentException(String message, Integer code) {
        this(message, code, null);
    }

    public InvalidArgumentException(InvalidArgumentException e) {
        this(e.getMessage(), e.getCode(), e.getException());
    }
    
    public Integer getCode() {
        return code;
    }
    public Exception getException() {
        return e;
    }
}
