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

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.CurrencyBL;
import com.sapienter.jbilling.server.pricing.db.AttributeDefinition;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.strategy.PricingStrategy;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 06-08-2010
 */
public class PriceModelBL {
    
    /**
     * Returns the given PriceModelDTO entity as a WS object
     *
     * @param dto PriceModelDTO to convert
     * @return plan price as a WS object, null if dto is null
     */
    public static PriceModelWS getWS(PriceModelDTO dto) {
        return dto != null ? new PriceModelWS(dto) : null;
    }

    /**
     * Returns the given list of PriceModelDTO entities as WS objects.
     *
     * @param dtos list of PriceModelDTO to convert
     * @return plan prices as WS objects, or an empty list if source list is empty.
     */
    public static List<PriceModelWS> getWS(List<PriceModelDTO> dtos) {
        if (dtos == null)
            return Collections.emptyList();

        List<PriceModelWS> ws = new ArrayList<PriceModelWS>(dtos.size());
        for (PriceModelDTO planPrice : dtos)
            ws.add(getWS(planPrice));
        return ws;
    }

    /**
     * Returns the given WS object as a PriceModelDTO entity. This method
     * does not perform any saves or updates, it only converts between the
     * two data structures.
     *
     * @param ws web service object to convert
     * @return PriceModelDTO entity, null if ws is null
     */
    public static PriceModelDTO getDTO(PriceModelWS ws) {
        if (ws != null) {
            if (ws.getCurrencyId() == null)
                throw new SessionInternalError("PriceModelWS must have a currency.");

            return new PriceModelDTO(ws,  new CurrencyBL(ws.getCurrencyId()).getEntity());
        }
        return null;
    }

    /**
     * Validates that the given pricing model has all the required attributes and that
     * the given attributes are of the correct type.
     *
     * @param model pricing model to validate
     */
    public static void validateAttributes(PriceModelDTO model) {
        AttributeUtils.validateAttributes(model.getAttributes(), model.getStrategy());
    }
}
