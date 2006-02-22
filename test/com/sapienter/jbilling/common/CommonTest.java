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

import junit.framework.TestCase;

import com.sapienter.jbilling.interfaces.UserSession;
import com.sapienter.jbilling.interfaces.UserSessionHome;

/**
 * EJBFactory
 * 
 * @author Emil
 */

public class CommonTest extends TestCase {

    /**
     * Constructor for GeneralTest.
     * @param arg0
     */
    public CommonTest(String arg0) {
        super(arg0);
    }

	public void testEJBFactory () {
	    
        try {
            // just a simple test for the factory
            UserSessionHome UserHome = (UserSessionHome)
            		JNDILookup.getFactory(true).lookUpHome(
            		UserSessionHome.class, UserSessionHome.JNDI_NAME);
            UserSession lSession = UserHome.create();
            lSession.toString();            		
        } catch (Exception e) {
            e.printStackTrace();
            fail("Got an exception " + e );
        }
	}
}
