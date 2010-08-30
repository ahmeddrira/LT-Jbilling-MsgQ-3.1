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

package com.sapienter.jbilling.server.user;

import com.sapienter.jbilling.server.item.db.PlanItemDTO;
import com.sapienter.jbilling.server.pricing.db.PriceModelDTO;
import com.sapienter.jbilling.server.user.db.CustomerDTO;
import com.sapienter.jbilling.server.user.db.CustomerPriceDAS;
import com.sapienter.jbilling.server.user.db.CustomerPriceDTO;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Business logic for customer to plan item (plan item pricing) mappings.
 *
 * This class handles the application of plan pricing to a customer. Plan item prices can
 * be added and removed from a customer to either grant or revoke access to the plans
 * special pricing for an item.
 *
 * Customer specific pricing can be added by saving a {@link PlanItemDTO} that has no
 * association to a plan. 
 *
 * @author Brian Cowdery
 * @since 30-08-2010
 */
public class CustomerPriceBL {
    private static final Logger LOG = Logger.getLogger(CustomerPriceBL.class);

    private CustomerPriceDAS customerPriceDas;
    private UserBL userBl;
    private CustomerDTO customer;
    private Integer userId;


    public CustomerPriceBL() {
        _init();
    }
    
    public CustomerPriceBL(Integer userId) {
        _init();
        set(userId);
    }

    public CustomerPriceBL(CustomerDTO customer) {
        _init();
        this.customer = customer;
        this.userId = customer.getBaseUser().getId();
    }

    private void _init() {
        customerPriceDas = new CustomerPriceDAS();
        userBl = new UserBL();
    }

    public void set(Integer userId) {        
        userBl.set(userId);
        this.customer = userBl.getEntity().getCustomer();
        this.userId = userId;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public Integer getUserId() {
        return userId;
    }

    /**
     * Returns the customer's price for the given item. This method returns null
     * If the customer does not have any special pricing for the given item (customer
     * is not subscribed to a plan affecting the items price, or no customer-specific
     * price found).
     *
     * @param itemId item to price
     * @return customer price, null if no special price found
     */
    public PlanItemDTO getPrice(Integer itemId) {
        return customerPriceDas.findPrice(userId, itemId);
    }

    /**
     * Returns a list of all customer-specific prices that apply only to this customer.
     * @return list of prices, empty list if none
     */
    public List<PlanItemDTO> getCustomerSpecificPrices() {
        return customerPriceDas.findAllCustomerSpecificPrices(userId);
    }

    /**
     * Returns the customer's price for the given item and pricing attributes.
     *
     * @see CustomerPriceDAS#findPriceByAttributes(Integer, Integer, java.util.Map, Integer)
     *
     * @param itemId id of item being priced
     * @param attributes attributes of plan pricing to match
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPricesByAttributes(Integer itemId, Map<String, String> attributes) {
        return customerPriceDas.findPriceByAttributes(userId, itemId, attributes, null);
    }

    /**
     * Returns the customer's price for the given item and pricing attributes, limiting the number
     * of results returned (queried from database).
     *
     * @see CustomerPriceDAS#findPriceByAttributes(Integer, Integer, java.util.Map, Integer)
     *
     * @param itemId id of item being priced
     * @param attributes attributes of plan pricing to match
     * @param maxResults limit database query return results
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPricesByAttributes(Integer itemId, Map<String, String> attributes, Integer maxResults) {
        return customerPriceDas.findPriceByAttributes(userId, itemId, attributes, maxResults);
    }

    /**
     * Returns the customer's price for the given item and pricing attributes, allowing for wildcard
     * matches of pricing attributes.
     *
     * @see CustomerPriceDAS#findPriceByWildcardAttributes(Integer, Integer, java.util.Map, Integer)
     *
     * @param itemId id of item being priced
     * @param attributes attributes of plan pricing to match
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPricesByWildcardAttributes(Integer itemId, Map<String, String> attributes) {
        return customerPriceDas.findPriceByWildcardAttributes(userId, itemId, attributes, null);
    }

    /**
     * Returns the customer's price for the given item and pricing attributes, allowing for wildcard
     * matches of plan attributes and limiting the number of results returned (queried from database).
     *
     * @see CustomerPriceDAS#findPriceByWildcardAttributes(Integer, Integer, java.util.Map, Integer)
     *
     * @param itemId id of item being priced
     * @param attributes attributes of plan pricing to match
     * @param maxResults limit database query return results
     * @return list of found plan prices, empty list if none found.
     */
    public List<PriceModelDTO> getPricesByWildcardAttributes(Integer itemId, Map<String, String> attributes, Integer maxResults) {
        return customerPriceDas.findPriceByWildcardAttributes(userId, itemId, attributes, maxResults);
    }

    /**
     * Returns a list of all customers that have subscribed to the given plan. A customer
     * subscribes to a plan by adding the plan subscription item to a recurring order.
     *
     * @param planId id of plan
     * @return list of customers subscribed to the plan, empty if none found
     */
    public List<CustomerDTO> getCustomersByPlan(Integer planId) {
        return customerPriceDas.findCustomersByPlan(planId);
    }

    /**
     * Removes all plan item prices from this customer for the given plan id.
     *
     * @param planId id of plan
     */
    public void removePrices(Integer planId) {
        customerPriceDas.deletePrices(userId, planId);        
    }

    /**
     * Removes the given list of plan item prices from this customer, effectively
     * un-subscribing the customer from a plan and revoking the special item pricing.
     *
     * @param planItems plan items to remove
     */
    public void removePrices(List<PlanItemDTO> planItems) {
        int deleted = customerPriceDas.deletePrices(userId, planItems);
        LOG.debug("Deleted " + deleted + " customer price entries.");        
    }

    /**
     * Removes a plan item price from this customer, revoking the special price for an item.
     *
     * @param planItem plan item to remove
     */
    public void removePrice(PlanItemDTO planItem) {
        customerPriceDas.deletePrice(userId, planItem.getId());
    }

    /**
     * Adds the given list of plan item prices to this customer, effectively
     * subscribing the customer to a plan and applying special item pricing.
     *
     * @param planItems plan items to add
     * @return list of saved customer prices
     */
    public List<CustomerPriceDTO> addPrices(List<PlanItemDTO> planItems) {
        List<CustomerPriceDTO> saved = new ArrayList<CustomerPriceDTO>(planItems.size());
        for (PlanItemDTO planItem : planItems)
            saved.add(addPrice(planItem));

        LOG.debug("Saved " + saved.size() + " customer price entries.");
        return saved;
    }

    /**
     * Add a plan item price to this customer, applying a special price for an item.
     *
     * If the given PlanItemDTO is not linked to a PlanDTO, then this price
     * will be treated as a customer-specific price that applies only to
     * this customer.
     *
     * @param planItem plan item to add
     * @return saved customer price
     */
    public CustomerPriceDTO addPrice(PlanItemDTO planItem) {
        CustomerPriceDTO dto = new CustomerPriceDTO();
        dto.setCustomer(customer);
        dto.setPlanItem(planItem);

        return customerPriceDas.save(dto);
    }
}
