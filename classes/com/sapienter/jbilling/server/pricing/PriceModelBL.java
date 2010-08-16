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
        if (dto != null) {
            ItemDTOEx planItem = dto.isDefaultPricing() || dto.getPlanItem() == null
                                 ? PriceModelWS.DEFAULT_PLAN_ITEM
                                 : new ItemBL().getWS(dto.getPlanItem());

            return new PriceModelWS(dto, planItem);
        }
        return null;
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
            ItemDTO planItem = ws.isDefaultPricing() || ws.getPlanItem() == null
                               ? PriceModelDTO.DEFAULT_PLAN_ITEM :
                               new ItemBL(ws.getPlanItem().getId()).getEntity();

            return new PriceModelDTO(ws, planItem);
        }
        return null;
    }

    // todo: Add event logger for pricing models.

    public Integer create(PriceModelDTO planPrice) {
        planPrice = priceModelDas.save(planPrice);
        return planPrice.getId();
    }

    public void update(PriceModelDTO dto) {
        if (model != null) {

            if (model.getPlanItem().getId() != dto.getPlanItem().getId())
                model.setPlanItem(dto.getPlanItem());

            model.setType(dto.getType());
            model.setAttributes(dto.getAttributes());
            model.setPrecedence(dto.getPrecedence());
            model.setRate(dto.getRate());
            model.setIncludedQuantity(dto.getIncludedQuantity());
            model.setDefaultPricing(dto.isDefaultPricing());
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


    /**
     * Returns a list of all default plan prices.
     *
     * @return list of found default plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getDefaultPriceModels() {
        return priceModelDas.findDefaultPriceModels();
    }

    /**
     * Returns a list of all plan prices by the pricing strategy type.
     *
     * @see com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy
     *
     * @param strategy pricing strategy type
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPriceModelsByType(PriceModelStrategy strategy) {
        return priceModelDas.findByType(strategy);
    }

    /**
     * Returns a list of all plan prices for the given plan item id.
     *
     * @param planItemId plan item id
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPriceModels(Integer planItemId) {
        return priceModelDas.findByPlanItem(planItemId);
    }

    /**
     * Return all plan prices for the given list of plan items and attributes.
     *
     * @see com.sapienter.jbilling.server.pricing.db.PriceModelDAS#findByPlanItemAndWildcardAttributes(Integer[], java.util.Map, Integer)
     *
     * @param planItemIds plan item ids
     * @param attributes attributes of plan pricing to match
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPriceModelsByItemAndAttributes(Integer[] planItemIds, Map<String, String> attributes) {
        return priceModelDas.findByPlanItemAndAttributes(planItemIds, attributes, null);
    }

    /**
     * Return all plan prices for the given list of plan items and attributes, limiting the number
     * of results returned (queried from database).
     *
     * @see com.sapienter.jbilling.server.pricing.db.PriceModelDAS#findByPlanItemAndAttributes(Integer[], java.util.Map, Integer)
     *
     * @param planItemIds plan item ids
     * @param attributes attributes of plan pricing to match
     * @param maxResults limit database query return results
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPriceModelsByItemAndAttributes(Integer[] planItemIds, Map<String, String> attributes,
                                                                 Integer maxResults) {

        return priceModelDas.findByPlanItemAndAttributes(planItemIds, attributes, maxResults);
    }

    /**
     * Return all plan prices for the given list of items and attributes, allowing for wildcard
     * matches of plan attributes.
     *
     * @see com.sapienter.jbilling.server.pricing.db.PriceModelDAS#findByPlanItemAndWildcardAttributes(Integer[], java.util.Map, Integer)
     *
     * @param planItemIds plan item ids
     * @param attributes attributes of plan pricing to match
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPriceModelsByItemAndWildcardAttributes(Integer[] planItemIds,
                                                                         Map<String, String> attributes) {

        return priceModelDas.findByPlanItemAndWildcardAttributes(planItemIds, attributes, null);
    }

    /**
     * Return all plan prices for the given list of items and attributes, allowing for wildcard
     * matches of plan attributes and limiting the number of results returned (queried from database).
     *
     * @see com.sapienter.jbilling.server.pricing.db.PriceModelDAS#findByPlanItemAndWildcardAttributes(Integer[], java.util.Map, Integer)
     *
     * @param planItemIds plan item ids
     * @param attributes attributes of plan pricing to match
     * @param maxResults limit database query return results
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPriceModelsByItemAndWildcardAttributes(Integer[] planItemIds,
                                                                         Map<String, String> attributes,
                                                                         Integer maxResults) {

        return priceModelDas.findByPlanItemAndWildcardAttributes(planItemIds, attributes, maxResults);
    }
}
