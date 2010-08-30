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

import com.sapienter.jbilling.server.item.ItemBL;
import com.sapienter.jbilling.server.item.ItemDTOEx;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.pricing.db.PriceModelDAS;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 06-08-2010
 */
public class PriceModelBL {
    private static final Logger LOG = Logger.getLogger(PriceModelBL.class);

    private PriceModelDAS priceModelDas;
    private PriceModelDTO model;

    public PriceModelBL() {
        _init();
    }

    public PriceModelBL(Integer id) {
        _init();
        set(id);
    }

    private void _init() {
        priceModelDas = new PriceModelDAS();
    }

    public void set(Integer id) {
        model = priceModelDas.find(id);
    }

    public PriceModelDTO getEntity() {
        return model;
    }

    /**
     * Returns the loaded PriceModelDTO entity as a WS object.
     *
     * @return plan price as a WS object
     */
    public PriceModelWS getWS() {
        return getWS(model);
    }

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
        return ws != null ? new PriceModelDTO(ws) : null;
    }

    // todo: Add event logger for pricing models.

    public Integer create(PriceModelDTO planPrice) {
        planPrice = priceModelDas.save(planPrice);
        return planPrice.getId();
    }

    public void update(PriceModelDTO dto) {
        if (model != null) {
            model.setType(dto.getType());
            model.setAttributes(dto.getAttributes());
            model.setRate(dto.getRate());
            model.setIncludedQuantity(dto.getIncludedQuantity());
        } else {
            LOG.error("Cannot update, PriceModelDTO not found or not set!");
        }
    }

    public void delete() {
        if (model != null) {
            priceModelDas.delete(model);
        } else {
            LOG.error("Cannot delete, PriceModelDTO not found or not set!");
        }
    }
}
