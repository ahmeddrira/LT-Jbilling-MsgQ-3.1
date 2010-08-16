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

package com.sapienter.jbilling.server.pricing.db;

import com.sapienter.jbilling.server.pricing.strategy.PriceModelStrategy;
import com.sapienter.jbilling.server.util.db.AbstractDAS;
import org.apache.log4j.Logger;
import org.hibernate.Query;

import java.util.List;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 05-08-2010
 */
public class PriceModelDAS extends AbstractDAS<PriceModelDTO> {

    private static final Logger LOG = Logger.getLogger(PriceModelDAS.class);

    @SuppressWarnings("unchecked")
    public List<PriceModelDTO> findDefaultPriceModels() {
        Query query = getSession().getNamedQuery("PriceModelDTO.findDefaultPricing");
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<PriceModelDTO> findByType(PriceModelStrategy strategy) {
        Query query = getSession().getNamedQuery("PriceModelDTO.findByType");
        query.setParameter("strategy_type", strategy);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<PriceModelDTO> findByPlanItem(Integer planItemId) {
        Query query = getSession().getNamedQuery("PriceModelDTO.findByPlanItemId");
        query.setParameter("plan_item_id", planItemId);
        return query.list();
    }

    // it would be nice to do this with hibernate criteria, but unfortunately criteria
    // queries do not support collections of values (attributes['key_name'] = ?), so we
    // need to manually construct the HQL query by hand.

    private static final String PLAN_PRICE_INCL_DEFAULT_QUERY_HQL =
            "select price " +
                    " from PriceModelDTO price " +
                    " where (price.planItem.id in (:plan_item_ids) or price.defaultPricing = true) ";

    private static final String PLAN_PRICE_INCL_DEFAULT_ORDER_HQL =
            " order by price.precedence desc";

    /**
     * Returns all pricing and default pricing in order of precedence (highest first), where
     * all plan attributes <strong>must</strong> match the given map of attributes.
     *
     * @param planItemIds plan item ids
     * @param attributes attributes of pricing to match
     * @param maxResults limit database query return results
     * @return list of found plan prices, empty list if none found.
     */
    @SuppressWarnings("unchecked")
    public List<PriceModelDTO> findByPlanItemAndAttributes(Integer[] planItemIds, Map<String, String> attributes,
                                                          Integer maxResults) {

        StringBuffer hql = new StringBuffer();
        hql.append(PLAN_PRICE_INCL_DEFAULT_QUERY_HQL);

        // build collection of values query from attributes
        // clause - "and price.attributes['key'] = :key"
        for (String key : attributes.keySet())
            hql.append(" and ")
                    .append(getAttributeClause(key))
                    .append(" = ")
                    .append(getAttributeNamedParameter(key));

        hql.append(PLAN_PRICE_INCL_DEFAULT_ORDER_HQL);
        LOG.debug("Constructed HQL query with attributes: \n " + hql.toString());

        Query query = getSession().createQuery(hql.toString());
        query.setParameterList("plan_item_ids", planItemIds);

        // bind attribute named parameters
        for (Map.Entry<String, String> entry : attributes.entrySet())
            query.setParameter(entry.getKey(), entry.getValue());

        if (maxResults != null)
            query.setMaxResults(maxResults);

        return query.list();
    }


    // todo: update javadoc to reflect usage of wildcard characters.

    /**
     * Returns all pricing and default pricing in order of precedence (highest first), where
     * attributes matched are equal or saved in the database as a wildcard ('*'). Allows partial
     * matches of attributes to find the "best fit" pricing plan.
     *
     * Attributes may be persisted as a wildcard ('*') which will match any attribute value
     * passed into this method. This is useful for defining pricing that only need to match
     * on a single attribute out of many possible attributes.
     *
     * Eg.
     *
     * Plan with saved attributes:
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
     * @param planItemIds plan item ids
     * @param attributes attributes of pricing to match
     * @param maxResults limit database query return results
     * @return list of found prices, empty list if none found.
     */
    @SuppressWarnings("unchecked")
    public List<PriceModelDTO> findByPlanItemAndWildcardAttributes(Integer[] planItemIds, Map<String, String> attributes,
                                                                  Integer maxResults) {

        StringBuffer hql = new StringBuffer();
        hql.append(PLAN_PRICE_INCL_DEFAULT_QUERY_HQL);

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

        hql.append(PLAN_PRICE_INCL_DEFAULT_ORDER_HQL);
        LOG.debug("Constructed HQL query with wildcard attributes: \n " + hql.toString());

        Query query = getSession().createQuery(hql.toString());
        query.setParameterList("plan_item_ids", planItemIds);

        // bind attribute named parameters
        for (Map.Entry<String, String> entry : attributes.entrySet())
            query.setParameter(entry.getKey(), entry.getValue());

        if (maxResults != null)
            query.setMaxResults(maxResults);

        return query.list();
    }

    private String getAttributeClause(String key) {
        return "price.attributes['" + key + "']";
    }

    private String getAttributeNamedParameter(String key) {
        return ":" + key;
    }
}
