/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.pricing.strategy;

import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import junit.framework.TestCase;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * TimeOfDayPricingStrategyTest
 *
 * @author Brian Cowdery
 * @since 03/02/11
 */
public class TimeOfDayPricingStrategyTest extends TestCase {

    // class under test
    private PricingStrategy strategy = new TimeOfDayPricingStrategy();

    public TimeOfDayPricingStrategyTest() {
        super();
    }

    public TimeOfDayPricingStrategyTest(String name) {
        super(name);
    }


    private Date getTime(int hours, int minutes) {
        return new LocalTime(hours, minutes).toDateTimeToday().toDate();
    }

    /**
     * Test that the current time is used if no pricing fields are given
     */
    public void testCurrentTime() {
        PriceModelDTO model = new PriceModelDTO();
        model.addAttribute("00:00", "10.00");
        model.addAttribute("23:59", "99.99");

        // can't really test this without duplicating a bunch of time calculations
        // simply check to make sure that no exception occurs if the time isn't part of the pricing fields

        PricingResult result = new PricingResult(1, 2, 3);
        strategy.applyTo(result, null, model, null, null);
    }

    /**
     * Test that pricing field dates can be used for pricing
     */
    public void testPricingFieldTime() {
        PriceModelDTO model = new PriceModelDTO();
        model.addAttribute("date_field", "event_date");
        model.addAttribute("00:00", "10.00");
        model.addAttribute("12:00", "20.00");

        // test price at 1:00 (first time range)
        List<PricingField> fields = Arrays.asList(new PricingField("event_date", getTime(1, 0)));
        PricingResult result = new PricingResult(1, 2, 3);
        strategy.applyTo(result, fields, model, null, null);

        assertEquals(new BigDecimal("10.00"), result.getPrice());

        // test price at 12:00 (second time range)
        List<PricingField> fields2 = Arrays.asList(new PricingField("event_date", getTime(13, 0)));
        PricingResult result2 = new PricingResult(1, 2, 3);
        strategy.applyTo(result2, fields2, model, null, null);

        assertEquals(new BigDecimal("20.00"), result2.getPrice());
    }

    /**
     * Test upper and lower boundaries of time ranges
     */
    public void testPricing() {
        PriceModelDTO model = new PriceModelDTO();
        model.addAttribute("date_field", "event_date");
        model.addAttribute("00:00", "10.00");
        model.addAttribute("12:00", "20.00");
        model.addAttribute("18:00", "25.00");

        PricingResult result = new PricingResult(1, 2, 3);

        // test price at exactly 00:00 (equal to boundary)
        List<PricingField> fields = Arrays.asList(new PricingField("event_date", getTime(0, 0)));
        strategy.applyTo(result, fields, model, null, null);

        assertEquals(new BigDecimal("10.00"), result.getPrice());

        // greater than lower boundary
        fields = Arrays.asList(new PricingField("event_date", getTime(0, 1)));
        strategy.applyTo(result, fields, model, null, null);

        assertEquals(new BigDecimal("10.00"), result.getPrice());

        // 1 second before next time range (12:00)
        fields = Arrays.asList(new PricingField("event_date", getTime(11, 59)));
        strategy.applyTo(result, fields, model, null, null);

        assertEquals(new BigDecimal("10.00"), result.getPrice());

        // equal to lower boundary of 12:00 range
        fields = Arrays.asList(new PricingField("event_date", getTime(12, 0)));
        strategy.applyTo(result, fields, model, null, null);

        assertEquals(new BigDecimal("20.00"), result.getPrice());

        // greater than lower boundary of 12:00 range
        fields = Arrays.asList(new PricingField("event_date", getTime(12, 1)));
        strategy.applyTo(result, fields, model, null, null);

        assertEquals(new BigDecimal("20.00"), result.getPrice());
    }

}
