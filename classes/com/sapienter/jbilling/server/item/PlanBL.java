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

package com.sapienter.jbilling.server.item;

import com.sapienter.jbilling.server.item.db.PlanDAS;
import com.sapienter.jbilling.server.item.db.PlanDTO;
import com.sapienter.jbilling.server.item.db.PlanItemDTO;
import com.sapienter.jbilling.server.user.CustomerPriceBL;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author Brian Cowdery
 * @since 30-08-2010
 */
public class PlanBL {
    private static final Logger LOG = Logger.getLogger(PlanBL.class);

    private PlanDAS planDas;
    private CustomerPriceBL customerPriceBl;

    private PlanDTO plan;

    public PlanBL() {
        _init();
    }

    public PlanBL(Integer planId) {
        _init();
        set(planId);
    }

    public void set(Integer planId) {
        this.plan = planDas.find(planId); 
    }

    private void _init() {
        this.planDas = new PlanDAS();
        this.customerPriceBl = new CustomerPriceBL();
    }

    public PlanDTO getEntity() {
        return plan;
    }

    // todo: add event logging for plans

    public Integer create(PlanDTO plan) {
        this.plan = planDas.save(plan);
        return this.plan.getId();        
    }

    public void update(PlanDTO dto) {
        if (plan != null) {
            plan.setDescription(dto.getDescription());
            plan.setItem(dto.getItem());
            plan.setPlanItems(dto.getPlanItems());

            planDas.flush();
            refreshCustomerPrices();

        } else {
            LOG.error("Cannot update, PlanDTO not found or not set!");
        }
    }

    public void addPrice(PlanItemDTO planItem) {
        if (plan != null) {
            plan.addPlanItem(planItem);

            planDas.flush();
            refreshCustomerPrices();
            
        } else {
            LOG.error("Cannot add price, PlanDTO not found or not set!");
        }
    }

    public void delete() {
        if (plan != null) {
            planDas.delete(plan);
        } else {
            LOG.error("Cannot delete, PlanDTO not found or not set!");
        }
    }


    /**
     * Refreshes the customer plan item price mappings for all customers that have
     * subscribed to this plan. This method will remove all existing prices for the plan
     * and insert the current list of plan items. 
     */
    public void refreshCustomerPrices() {
        if (plan != null) {
            for (CustomerDTO customer : customerPriceBl.getCustomersByPlan(plan.getId())) {
                CustomerPriceBL bl = new CustomerPriceBL(customer);
                bl.removePrices(plan.getId());
                bl.addPrices(plan.getPlanItems());
            }
        } else {
            LOG.error("Cannot update customer prices, PlanDTO not found or not set!");
        }
    }

    /**
     * Returns all plans that use the given item as the "plan subscription" item.
     *
     * @param planItemId item id
     * @return list of plans, empty list if none found
     */
    public List<PlanDTO> getPlansByPlanItem(Integer planItemId) {
        return planDas.findByPlanSubscriptionItem(planItemId);
    }

    /**
     * Returns all plans that affect the pricing of the given item, or that include
     * the item in a bundle.
     *
     * @param itemId item id
     * @return list of plans, empty list if none found
     */
    public List<PlanDTO> getPlansByAffectedItem(Integer itemId) {
        return planDas.findByAffectedItem(itemId);
    }
}
