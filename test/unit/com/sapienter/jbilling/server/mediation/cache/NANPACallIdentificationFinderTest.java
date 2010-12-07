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

package com.sapienter.jbilling.server.mediation.cache;

import junit.framework.TestCase;

/**
 * NANPACallIdentificationFinderTest
 *
 * @author Brian Cowdery
 * @since 07-12-2010
 */
public class NANPACallIdentificationFinderTest extends TestCase {

    public NANPACallIdentificationFinderTest() {
    }

    public NANPACallIdentificationFinderTest(String name) {
        super(name);
    }

    public void testGetDigits() throws Exception {
        NANPACallIdentificationFinder finder = new NANPACallIdentificationFinder(null, null);
        String number = "12345";

        assertEquals("12345", finder.getDigits(number, 5));
        assertEquals("1234", finder.getDigits(number, 4));
        assertEquals("123", finder.getDigits(number, 3));
        assertEquals("12", finder.getDigits(number, 2));
        assertEquals("1", finder.getDigits(number, 1));
        assertEquals("0", finder.getDigits(number, 0));
        assertEquals("0", finder.getDigits(number, -1));
    }
}
