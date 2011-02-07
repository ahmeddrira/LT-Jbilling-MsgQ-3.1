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

import com.sapienter.jbilling.server.BigDecimalTestCase;
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
 * TimeOfDayPercentageStrategyTest
 *
 * @author Brian Cowdery
 * @since 07/02/11
 */
public class TimeOfDayPercentageStrategyTest extends BigDecimalTestCase {

    // class under test
    private PricingStrategy strategy = new TimeOfDayPercentageStrategy();

    public TimeOfDayPercentageStrategyTest() {
    }

    public TimeOfDayPercentageStrategyTest(String name) {
        super(name);
    }

    /** Convenience method to produce a time for the given hours and minutes */
    private Date getTime(int hours, int minutes) {
        return new LocalTime(hours, minutes).toDateTimeToday().toDate();
    }

    /**
     * Test that the current time is used if no pricing fields are given
     */
    public void testCurrentTime() {
        PriceModelDTO model = new PriceModelDTO();
        model.addAttribute("00:00", "0.80");
        model.addAttribute("23:59", "0.90");

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
        model.addAttribute("00:00", "0.80");
        model.addAttribute("12:00", "0.90");

        // test price at 1:00 (first time range)
        List<PricingField> fields = Arrays.asList(new PricingField("event_date", getTime(1, 0)));
        PricingResult result = new PricingResult(1, 2, 3);
        result.setPrice(new BigDecimal("10.00"));

        strategy.applyTo(result, fields, model, null, null);
        assertEquals(new BigDecimal("8.00"), result.getPrice());

        // test price at 12:00 (second time range)
        List<PricingField> fields2 = Arrays.asList(new PricingField("event_date", getTime(13, 0)));
        PricingResult result2 = new PricingResult(1, 2, 3);
        result2.setPrice(new BigDecimal("10.00"));

        strategy.applyTo(result2, fields2, model, null, null);

        assertEquals(new BigDecimal("9.00"), result2.getPrice());
    }
}
