/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.security;

import com.sapienter.jbilling.server.order.OrderWS;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

/**
 * WSSecurityAdviceTest
 *
 * @author Brian Cowdery
 * @since 02-11-2010
 */
public class WSSecurityAdviceTest extends TestCase {




    public void testInstanceOf() {
        Object order = new OrderWS();

        Object object = new Object();

        Object list = new ArrayList();

        System.out.println("is OrderWS instanceof " + (order instanceof WSSecured));
        System.out.println("is Object instanceof " + (object instanceof WSSecured));
        System.out.println("is ArrayList instanceof Collection " + (list instanceof Collection));


        System.out.println("class " + order.getClass());
        System.out.println("is assignable " + order.getClass().isAssignableFrom(WSSecured.class));


         assertTrue(true);

    }

}
