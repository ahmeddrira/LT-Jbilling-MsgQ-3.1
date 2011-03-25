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

package com.sapienter.jbilling.server.item;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.db.PlanItemBundleDTO;
import com.sapienter.jbilling.server.order.db.OrderPeriodDAS;
import com.sapienter.jbilling.server.order.db.OrderPeriodDTO;

/**
 * PlanItemBundleBL
 *
 * @author Brian Cowdery
 * @since 25/03/11
 */
public class PlanItemBundleBL {

    /**
     * Convert a given PlanItemBundleWS web-service object into a PlanItemBundleDTO.
     *
     * The PlanItemWS web-service object must have a valid period id or an exception
     * will be thrown.
     *
     * @param ws ws object to convert
     * @return converted DTO object
     * @throws SessionInternalError if required field is missing
     */
    public static PlanItemBundleDTO getDTO(PlanItemBundleWS ws) {
        if (ws != null) {
            if (ws.getPeriodId() == null)
                throw new SessionInternalError("PlanItemBundleWS must have a period.");

            OrderPeriodDTO period = ws.getPeriodId() != null ? new OrderPeriodDAS().find(ws.getPeriodId()) : null;

            return new PlanItemBundleDTO(ws, period);
        }
        return null;
    }

    /**
     * Convert a given PlanItemBundleDTO into a PlanItemBundleWS web-service object.
     *
     * @param dto dto to convert
     * @return converted web-service object
     */
    public static PlanItemBundleWS getWS(PlanItemBundleDTO dto) {
        return dto != null ? new PlanItemBundleWS(dto) : null;
    }
}
