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
import com.sapienter.jbilling.server.pricing.db.PriceModelStrategy;
import com.sapienter.jbilling.server.pricing.strategy.PricingStrategy;
import com.sapienter.jbilling.server.pricing.util.AttributeUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 06-08-2010
 */
public class PriceModelBL {

    private static final Logger LOG = Logger.getLogger(PriceModelBL.class);
    
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
            PriceModelDTO root = null;
            PriceModelDTO model = null;

            for (PriceModelWS next = ws; next != null; next = next.getNext()) {
                if (model == null) {
                    model = root = new PriceModelDTO(next, new CurrencyBL(ws.getCurrencyId()).getEntity());
                } else {
                    model.setNext(new PriceModelDTO(next, new CurrencyBL(ws.getCurrencyId()).getEntity()));
                    model = model.getNext();
                }
            }

            return root;
        }
        return null;
    }

    /**
     * Validates that the given pricing model has all the required attributes and that
     * the given attributes are of the correct type.
     *
     * @param model pricing model to validate
     * @throws SessionInternalError if attributes are missing or of an incorrect type
     */
    public static void validateAttributes(PriceModelDTO model) throws SessionInternalError {
        List<String> errors = new ArrayList<String>();

        for (PriceModelDTO next = model; next != null; next = next.getNext()) {
            try {
                AttributeUtils.validateAttributes(next.getAttributes(), next.getStrategy());
            } catch (SessionInternalError e) {
                errors.addAll(Arrays.asList(e.getErrorMessages()));
            }
        }

        if (!errors.isEmpty()) {
            throw new SessionInternalError("Price model attributes failed validation.",
                                           errors.toArray(new String[errors.size()]));
        }
    }


    /**
     * Validates that the given pricing model WS object has all the required attributes and that
     * the given attributes are of the correct type.
     *
     * @param model pricing model WS object to validate
     * @throws SessionInternalError if attributes are missing or of an incorrect type
     */
    public static void validateAttributes(PriceModelWS model) throws SessionInternalError {
        List<String> errors = new ArrayList<String>();

        for (PriceModelWS next = model; next != null; next = next.getNext()) {
            try {
                PriceModelStrategy type = PriceModelStrategy.valueOf(next.getType());
                AttributeUtils.validateAttributes(next.getAttributes(), type.getStrategy());
            } catch (SessionInternalError e) {
                errors.addAll(Arrays.asList(e.getErrorMessages()));
            }
        }

        if (!errors.isEmpty()) {
            throw new SessionInternalError("Price model attributes failed validation.",
                                           errors.toArray(new String[errors.size()]));
        }
    }
}
