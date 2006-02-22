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
 * Created on Sep 24, 2004
 *
 * Shows which keys are missing for a language in the ApplicationProperties
 */
package com.sapienter.jbilling.tools;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Emil
 *
 */
public class LanguageKeys {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: LanguageKeys language_code");
        }
        
        String language = args[0];
        
        try {
            // open the default properties page
            Properties globalProperties = new Properties();
            FileInputStream propFile = new FileInputStream(
                    "ApplicationResources.properties");
            globalProperties.load(propFile);

            // and the one for the specifed language
            Properties languageProperties = new Properties();
            propFile = new FileInputStream(
                    "ApplicationResources_" + language + ".properties");
            languageProperties.load(propFile);
            
            // no go through all the keys
            for (Iterator it = globalProperties.keySet().iterator(); 
                    it.hasNext(); ) {
                String key = (String) it.next();
                if (!languageProperties.containsKey(key)) {
                    System.out.println(key + "=" + 
                            globalProperties.getProperty(key));
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
