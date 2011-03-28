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

package com.sapienter.jbilling.server.pricing;

import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskWS;
import com.sapienter.jbilling.server.util.api.JbillingAPI;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * PricingTestCase
 *
 * @author Brian Cowdery
 * @since 28/03/11
 */
public class PricingTestCase extends TestCase {

    // plug-in configuration
    private static final Integer PRICING_PLUGIN_ID = 410;
    private static final Integer RULES_PRICING_PLUGIN_TYPE_ID = 61; // RulesPricingTask2
    private static final Integer MODEL_PRICING_PLUGIN_TYPE_ID = 79; // PriceModelPricingTask


    public PricingTestCase() {
    }

    public PricingTestCase(String name) {
        super(name);
    }


    /*
        Enable/disable the PricingModelPricingTask plug-in.
     */

    public void enablePricingPlugin(JbillingAPI api) {
        PluggableTaskWS plugin = api.getPluginWS(PRICING_PLUGIN_ID);
        plugin.setTypeId(MODEL_PRICING_PLUGIN_TYPE_ID);

        api.updatePlugin(plugin);
    }

    public void disablePricingPlugin(JbillingAPI api) {
        PluggableTaskWS plugin = api.getPluginWS(PRICING_PLUGIN_ID);
        plugin.setTypeId(RULES_PRICING_PLUGIN_TYPE_ID);

        api.updatePlugin(plugin);
    }


    /*
        Convenience assertions for BigDecimal comparisons.
     */

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, BigDecimal expected, BigDecimal actual) {
        assertEquals(message,
                     (Object) (expected == null ? null : expected.setScale(2, RoundingMode.HALF_UP)),
                     (Object) (actual == null ? null : actual.setScale(2, RoundingMode.HALF_UP)));
    }

}
