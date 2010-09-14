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

package com.sapienter.jbilling.server.item.db;

import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.util.db.AbstractDAS;
import org.hibernate.Query;

import java.util.List;

/**
 * @author Brian Cowdery
 * @since 30-08-2010
 */
public class PlanDAS extends AbstractDAS<PlanDTO> {

    /**
     * Fetch a list of all customers that have subscribed to the given plan
     * by adding the "plan subscription" item to a recurring order.
     *
     * @param planId id of plan
     * @return list of customers subscribed to the plan, empty if none
     */
    @SuppressWarnings("unchecked")
    public List<CustomerDTO> findCustomersByPlan(Integer planId) {
        Query query = getSession().getNamedQuery("CustomerDTO.findCustomersByPlan");
        query.setParameter("plan_id", planId);

        return query.list();
    }

    /**
     * Returns true if the customer is subscribed to to the given plan id.
     *
     * @param userId user id of the customer
     * @param planId plan id
     * @return true if customer is subscribed to the plan, false if not.
     */
    public boolean isSubscribed(Integer userId, Integer planId) {
        Query query = getSession().getNamedQuery("PlanDTO.isSubscribed");
        query.setParameter("user_id", userId);
        query.setParameter("plan_id", planId);
        
        return query.iterate().hasNext();
    }

    /**
     * Fetch all plans for the given plan subscription item id.
     *
     * @param planItemId plan subscription item id
     * @return list of plans, empty if none found
     */
    @SuppressWarnings("unchecked")
    public List<PlanDTO> findByPlanSubscriptionItem(Integer planItemId) {
        Query query = getSession().getNamedQuery("PlanDTO.findByPlanItem");
        query.setParameter("plan_item_id", planItemId);

        return query.list();
    }

    /**
     * Fetch all plans that affect the pricing of the given item id, or include
     * the item in a bundle.
     *
     * @param affectedItemId affected item id
     * @return list of plans, empty if none found
     */
    @SuppressWarnings("unchecked")
    public List<PlanDTO> findByAffectedItem(Integer affectedItemId) {
        Query query = getSession().getNamedQuery("PlanDTO.findByAffectedItem");
        query.setParameter("affected_item_id", affectedItemId);

        return query.list();
    }

}
