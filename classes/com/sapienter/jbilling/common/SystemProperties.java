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

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This is a Singleton call that provides the system properties from
 * the jbilling.properties file
 */
public class SystemProperties {
    private static SystemProperties ref;
    private Properties prop = null;
    private static final Logger LOG = Logger.getLogger(SystemProperties.class);


    private SystemProperties() throws IOException {
        prop = new Properties();
        prop.load(SystemProperties.class.getResourceAsStream("/jbilling.properties"));
        LOG.debug("System properties loaded");
    }

    public static SystemProperties getSystemProperties() 
            throws IOException{
        if (ref == null) {
            // it's ok, we can call this constructor
            ref = new SystemProperties();		
        }
        return ref;
    }

    public String get(String key) throws Exception {
        String retValue = prop.getProperty(key);
        // all the system properties have to be there
        if (retValue == null) {
            throw new Exception("Missing system property: " + key);
        }
        //log.debug("Sys prop " + key + " = " + retValue);

        return retValue;
    }
    
    public String get(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }
    

    public Object clone()
	    throws CloneNotSupportedException {
        throw new CloneNotSupportedException(); 
        // a singleton should never be cloned
    }
}
