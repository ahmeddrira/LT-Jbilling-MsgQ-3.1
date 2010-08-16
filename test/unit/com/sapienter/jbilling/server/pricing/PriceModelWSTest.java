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

package com.sapienter.jbilling.server.pricing;

import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 09-08-2010
 */
public class PriceModelWSTest extends TestCase {

    public PriceModelWSTest() {
    }

    public PriceModelWSTest(String name) {
        super(name);
    }

    public void testFromDTO() {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("null_attr", null);
        attributes.put("attr", "some value");

        PriceModelDTO dto = new PriceModelDTO();
        dto.setId(1);
        dto.setType(PriceModelStrategy.METERED);
        dto.setAttributes(attributes);
        dto.setPrecedence(3);
        dto.setRate(new BigDecimal("0.7"));
        dto.setIncludedQuantity(BigDecimal.ZERO);
        dto.setDefaultPricing(false);

        // convert to PriceModelWS
        PriceModelWS ws = new PriceModelWS(dto, null);

        assertEquals(dto.getId(), ws.getId());
        assertEquals(PriceModelWS.PLAN_TYPE_METERED, ws.getType());
        assertEquals(dto.getPrecedence(), ws.getPrecedence());
        assertEquals(dto.getRate(), ws.getRate());
        assertEquals(dto.getIncludedQuantity(), ws.getIncludedQuantity());
        assertEquals(dto.isDefaultPricing(), ws.isDefaultPricing());

        assertNotSame(dto.getAttributes(), ws.getAttributes());
        assertEquals(PriceModelDTO.ATTRIBUTE_WILDCARD, ws.getAttributes().get("null_attr"));
        assertEquals("some value", ws.getAttributes().get("attr"));
    }
}
