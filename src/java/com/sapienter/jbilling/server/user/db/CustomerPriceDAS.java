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

package com.sapienter.jbilling.server.user.db;

import com.sapienter.jbilling.server.item.db.PlanItemDTO;
import com.sapienter.jbilling.server.util.db.AbstractDAS;
import org.apache.log4j.Logger;
import org.hibernate.Query;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 30-08-2010
 */
public class CustomerPriceDAS extends AbstractDAS<CustomerPriceDTO> {

    private static final Logger LOG = Logger.getLogger(CustomerPriceDAS.class);

    public CustomerPriceDTO find(Integer userId, Integer planItemId) {
        Query query = getSession().getNamedQuery("PlanItemDTO.find");
        query.setParameter("user_id", userId);
        query.setParameter("plan_item_id", planItemId);

        return (CustomerPriceDTO) query.uniqueResult();
    }

    /**
     * Fetch the customer price for the given customer and item.
     *
     * @param userId user id of the customer
     * @param itemId item id of the item being priced
     * @return customer price for the given item, or null if none found
     */
    public PlanItemDTO findPriceByItem(Integer userId, Integer itemId) {
        Query query = getSession().getNamedQuery("PlanItemDTO.findCustomerPriceByItem");
        query.setParameter("user_id", userId);
        query.setParameter("item_id", itemId);

        // use an iterator to scroll the cursor instead of returning all customer
        // prices in a list. This way we only fetch the price with the highest precedence
        // and not the entire result set.
        Iterator it = query.iterate();
        return it.hasNext() ? (PlanItemDTO) it.next() : null;
    }

    /**
     * Fetch a list of all customer specific prices.
     *
     * @param userId user id of the customer
     * @return list of customer specific prices, empty list if none found
     */
    @SuppressWarnings("unchecked")
    public List<PlanItemDTO> findAllCustomerSpecificPrices(Integer userId) {
        Query query = getSession().getNamedQuery("PlanItemDTO.findAllCustomerSpecificPrices");
        query.setParameter("user_id", userId);

        return query.list();
    }

    /**
     * Fetch a list of all customer prices.
     *
     * @param userId user id of the customer
     * @return list of customer prices, empty list if none found
     */
    @SuppressWarnings("unchecked")
    public List<PlanItemDTO> findAllCustomerPrices(Integer userId) {
        Query query = getSession().getNamedQuery("PlanItemDTO.findAllCustomerPrices");
        query.setParameter("user_id", userId);

        return query.list();
    }

    /**
     * Fetch a list of all customer prices for a specific item.
     * @param userId user id of the customer
     * @param itemId item id
     * @return list of customer prices, empty list if none found
     */
    @SuppressWarnings("unchecked")
    public List<PlanItemDTO> findAllCustomerPricesByItem(Integer userId, Integer itemId) {
        Query query = getSession().getNamedQuery("PlanItemDTO.findCustomerPriceByItem");
        query.setParameter("user_id", userId);
        query.setParameter("item_id", itemId);

        return query.list();
    }

    /**
     * Deletes all customer prices for the given plan id for a customer.
     *
     * @param userId user id of the customer
     * @param planId id of the plan
     * @return number of rows deleted
     */
    public int deletePrices(Integer userId, Integer planId) {
        Query query = getSession().getNamedQuery("CustomerPriceDTO.deletePriceByPlan");        
        query.setParameter("plan_id", planId);
        query.setParameter("user_id", userId);

        return query.executeUpdate();
    }

    /**
     * Deletes all the given customer prices for the given list of plan items.
     *
     * @param userId user id of the customer
     * @param planItems list of plan items to delete
     * @return number of rows deleted
     */
    public int deletePrices(Integer userId, List<PlanItemDTO> planItems) {
        int deleted = 0;

        for (PlanItemDTO planItem : planItems)
            deleted += deletePrice(userId, planItem.getId());

        return deleted;
    }

    /**
     * Deletes the customer price for the given plan item id (plan item price).
     *
     * @param userId user id of the customer
     * @param planItemId plan item price id
     * @return number of rows deleted
     */
    public int deletePrice(Integer userId, Integer planItemId) {
        Query query = getSession().getNamedQuery("CustomerPriceDTO.deletePrice");
        query.setParameter("user_id", userId);
        query.setParameter("plan_item_id", planItemId);

        return query.executeUpdate();
    }

    // it would be nice to do this with hibernate criteria, but unfortunately criteria
    // queries do not support collections of values (attributes['key_name'] = ?), so we
    // need to manually construct the HQL query by hand.

    private static final String PRICE_ATTRIBUTE_QUERY_HQL =
            "select price.id.planItem "
            + " from CustomerPriceDTO price "
            + "  join price.id.planItem.model as model "
            + " where price.id.planItem.item.id = :item_id "
            + "  and price.id.baseUser.id = :user_id " ;
    
    private static final String PRICE_ATTRIBUTE_ORDER_HQL =
            " order by price.id.planItem.precedence, price.createDatetime desc";

    /**
     * Fetch all customer pricing in order of precedence (highest first), where
     * all plan attributes <strong>must</strong> match the given map of attributes.
     *
     * @param userId user id of the customer
     * @param itemId item id of the item being priced
     * @param attributes attributes of pricing to match
     * @param maxResults maximum limit of returned query results
     * @return list of found plan prices, empty list if none found.
     */
    @SuppressWarnings("unchecked")
    public List<PlanItemDTO> findPriceByAttributes(Integer userId, Integer itemId, Map<String, String> attributes,
                                                     Integer maxResults) {

        StringBuffer hql = new StringBuffer();
        hql.append(PRICE_ATTRIBUTE_QUERY_HQL);

        // build collection of values query from attributes
        // clause - "and price.attributes['key'] = :key"
        for (String key : attributes.keySet())
            hql.append(" and ")
                    .append(getAttributeClause(key))
                    .append(" = ")
                    .append(getAttributeNamedParameter(key));

        hql.append(PRICE_ATTRIBUTE_ORDER_HQL);
        LOG.debug("Constructed HQL query with attributes: \n " + hql.toString());

        Query query = getSession().createQuery(hql.toString());
        query.setParameter("user_id", userId);
        query.setParameter("item_id", itemId);

        // bind attribute named parameters
        for (Map.Entry<String, String> entry : attributes.entrySet())
            query.setParameter(entry.getKey(), entry.getValue());

        if (maxResults != null)
            query.setMaxResults(maxResults);

        return query.list();
    }

    /**
     * Fetch all customer pricing in order of precedence (highest first), where
     * attributes matched are equal or saved in the database as a wildcard ('*'). Allows partial
     * matches of attributes to find the "best fit" pricing.
     *
     * Attributes may be persisted as a wildcard ('*') which will match any attribute value
     * passed into this method. This is useful for defining pricing that only need to match
     * on a single attribute out of many possible attributes.
     *
     * Eg.
     *
     * Item price with saved attributes:
     * <code>
     *      lata = '*'
     *      rateCenter = '*'
     *      stateProvince = 'NC'
     * </code>
     *
     * Matches:
     * <code>
     *      lata = '0772'
     *      rateCenter = 'CHARLOTTE'
     *      stateProvince = 'NC'
     * </code>
     *
     * @param userId user id of the customer
     * @param itemId item id of the item being priced
     * @param attributes attributes of pricing to match
     * @param maxResults maximum limit of returned query results
     * @return list of found prices, empty list if none found.
     */
    @SuppressWarnings("unchecked")
    public List<PlanItemDTO> findPriceByWildcardAttributes(Integer userId, Integer itemId,
                                                             Map<String, String> attributes,
                                                             Integer maxResults) {

        StringBuffer hql = new StringBuffer();
        hql.append(PRICE_ATTRIBUTE_QUERY_HQL);

        // build collection of values query from attributes
        // clause - "and (price.attributes['key'] = :key or price.attributes['key'] = '*')"
        for (String key : attributes.keySet())
            hql.append(" and (")
                    .append(getAttributeClause(key))
                    .append(" = ")
                    .append(getAttributeNamedParameter(key))
                    .append(" or ")
                    .append(getAttributeClause(key))
                    .append(" = '*')");

        hql.append(PRICE_ATTRIBUTE_ORDER_HQL);
        LOG.debug("Constructed HQL query with wildcard attributes: \n " + hql.toString());

        Query query = getSession().createQuery(hql.toString());
        query.setParameter("user_id", userId);
        query.setParameter("item_id", itemId);

        // bind attribute named parameters
        for (Map.Entry<String, String> entry : attributes.entrySet())
            query.setParameter(entry.getKey(), entry.getValue());

        if (maxResults != null)
            query.setMaxResults(maxResults);

        return query.list();
    }

    private String getAttributeClause(String key) {
        return "model.attributes['" + key + "']";
    }

    private String getAttributeNamedParameter(String key) {
        return ":" + key;
    }
}
