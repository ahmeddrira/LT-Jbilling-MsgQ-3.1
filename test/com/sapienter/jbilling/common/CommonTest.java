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
