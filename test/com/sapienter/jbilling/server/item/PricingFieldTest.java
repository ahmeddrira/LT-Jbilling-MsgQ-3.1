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

/**
 *
 * @author Brian Cowdery
 * @since 10-11-2009
 */
package com.sapienter.jbilling.server.item;

import junit.framework.TestCase;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class PricingFieldTest extends TestCase {

    public PricingFieldTest() {
        super();
    }

    public PricingFieldTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetValue() {
        PricingField string = new PricingField("str field", "Some String");
        assertEquals("Some String", string.getValue());

        Date day = new Date(2009 - 1900, 11, 10);
        PricingField date = new PricingField("date field", day);
        assertEquals(day, date.getValue());

        PricingField integer = new PricingField("int field", 2009);
        assertEquals(2009, ((Integer) integer.getValue()).intValue());

        PricingField decimal = new PricingField("decimal field", new BigDecimal("20.63"));
        assertEquals(new BigDecimal("20.63").toString(), ((BigDecimal) decimal.getValue()).toString());
    }

    public void testGetStrValue() {
        PricingField string = new PricingField("str field", "Some String");
        assertEquals("Some String", string.getStrValue());

        Date day = new Date(2009 - 1900, 11, 10);
        PricingField date = new PricingField("date field", day);
        assertEquals("1260421200000", date.getStrValue());

        PricingField integer = new PricingField("int field", 2009);
        assertEquals("2009", integer.getStrValue());

        PricingField decimal = new PricingField("decimal field", new BigDecimal("20.63"));
        assertEquals("20.63", decimal.getStrValue());
    }

    public void testGetDateValue() {
        Date day = new Date(2009 - 1900, 11, 10);
        PricingField date = new PricingField("date field", day);

        assertEquals(PricingField.Type.DATE, date.getType());
        assertEquals(day, date.getDateValue());
    }

    public void testGetCalendarValue() {
        Date day = new Date(2009 - 1900, 11, 10);
        PricingField date = new PricingField("date field", day);

        assertEquals(PricingField.Type.DATE, date.getType());

        Calendar calendar = date.getCalendarValue();
        assertEquals(2009, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(10, calendar.get(Calendar.DAY_OF_MONTH));  
    }

    public void testGetIntegerValue() {
        PricingField integer = new PricingField("int field", 2009);

        assertEquals(PricingField.Type.INTEGER, integer.getType());
        assertEquals(2009, integer.getIntValue().intValue());
    }

    public void testGetDecimalValue() {
        PricingField decimal = new PricingField("decimal field", new BigDecimal("20.63"));

        assertEquals(PricingField.Type.DECIMAL, decimal.getType());
        assertEquals(new BigDecimal("20.63"), decimal.getDecimalValue());
    }
        
    public void testEncode() {
        PricingField string = new PricingField("str field", "Some String");
        assertEquals("str field:1:string:Some String", PricingField.encode(string));

        Date day = new Date(2009 - 1900, 11, 10);
        PricingField date = new PricingField("date field", day);
        assertEquals("date field:1:date:1260421200000", PricingField.encode(date));

        PricingField integer = new PricingField("int field", 2009);
        assertEquals("int field:1:integer:2009", PricingField.encode(integer));

        PricingField decimal = new PricingField("decimal field", new BigDecimal("20.63"));
        assertEquals("decimal field:1:float:20.63", PricingField.encode(decimal));
    }

    public void testDecode() {
        PricingField string = new PricingField("str field:1:string:Some String");
        assertEquals(PricingField.Type.STRING, string.getType());
        assertEquals("Some String", string.getStrValue());      

        PricingField date = new PricingField("date field:1:date:1260421200000");
        Date day = new Date(2009 - 1900, 11, 10);
        assertEquals(day.getTime(), date.getDateValue().getTime());        

        PricingField integer = new PricingField("int field:1:integer:2009");
        assertEquals(PricingField.Type.INTEGER, integer.getType());
        assertEquals(2009, integer.getIntValue().intValue());

        PricingField decimal = new PricingField("decimal field:1:float:20.63");
        assertEquals(PricingField.Type.DECIMAL, decimal.getType());
        assertEquals(new BigDecimal("20.63"), decimal.getDecimalValue());
    }
}
 