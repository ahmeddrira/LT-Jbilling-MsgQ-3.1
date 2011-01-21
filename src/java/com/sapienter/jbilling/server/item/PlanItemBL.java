package com.sapienter.jbilling.server.item;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.db.ItemDTO;
import com.sapienter.jbilling.server.item.db.PlanItemDTO;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.util.db.CurrencyDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * PlanItemDTO business logic. Currently only contains static factory methods for converting
 * PlanItemDTO objects into their web-service equivalents.
 *
 * @author Brian Cowdery
 * @since 20-09-2010
 */
public class PlanItemBL {

    /**
     * Convert a given PlanItemWS web-service object into a PlanItemDTO.
     *
     * The PlanItemWS web-service object must have an affected item id, price model
     * and price model currency id or an exception will be thrown.
     *
     * @param ws ws object to convert
     * @return converted DTO object
     * @throws SessionInternalError if required field is missing
     */
    public static PlanItemDTO getDTO(PlanItemWS ws) {
        if (ws != null) {
            if (ws.getItemId() == null)
                throw new SessionInternalError("PlanItemDTO must have an affected item.");

            if (ws.getModel() == null)
                throw new SessionInternalError("PlanItemDTO must have a price model.");

            if (ws.getModel().getCurrencyId() == null)
                throw new SessionInternalError("PlanItemDTO price model must have a currency.");

            // affected item
            ItemDTO item = new ItemBL(ws.getItemId()).getEntity();

            // price model
            CurrencyDTO currency = new CurrencyBL(ws.getModel().getCurrencyId()).getEntity();
            PriceModelDTO model = new PriceModelDTO(ws.getModel(), currency);

            return new PlanItemDTO(ws, item, model);
        }
        return null;
    }

    /**
     * Converts a list of PlanItemWS web-service objects into PlanItemDTO objects.
     *
     * @see #getDTO(PlanItemWS)
     *
     * @param objects ws objects to convert
     * @return a list of converted DTO objects, or an empty list if ws objects list was empty.
     */
    public static List<PlanItemDTO> getDTO(List<PlanItemWS> objects) {
        List<PlanItemDTO> planItems = new ArrayList<PlanItemDTO>(objects.size());
        for (PlanItemWS ws : objects)
            planItems.add(getDTO(ws));
        return planItems;
    }


    /**
     * Convert a given PlanItemDTO into a PlanItemWS web-service object.
     * @param dto dto to convert
     * @return converted web-service object
     */
    public static PlanItemWS getWS(PlanItemDTO dto) {
        return dto != null ? new PlanItemWS(dto) : null;
    }


    /**
     * Converts a list of PlanItemDTO objects into PlanItemWS web-service objects.
     *
     * @see #getWS(PlanItemDTO)
     *
     * @param objects dto objects to convert
     * @return a list of converted WS objects, or an empty list if dto objects list was empty.
     */
    public static List<PlanItemWS> getWS(List<PlanItemDTO> objects) {
        List<PlanItemWS> planItems = new ArrayList<PlanItemWS>(objects.size());
        for (PlanItemDTO dto : objects)
            planItems.add(new PlanItemWS(dto));
        return planItems;
    }
}
