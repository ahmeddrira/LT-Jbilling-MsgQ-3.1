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

package com.sapienter.jbilling.server.mediation.cache;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;

import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.item.tasks.PricingResult;
import com.sapienter.jbilling.server.util.Context;

public class PricingFinder extends AbstractFinder {

    private static final Logger LOG = Logger.getLogger(PricingFinder.class);

    private static final String SPACE = " ";
    private static final String COMMA = ", ";

    public static final PricingFinder getInstance() {
        Object bean = Context.getBean(Context.Name.PRICING_FINDER);
        LOG.debug("Method: getInstance() found: " + bean);
        return (PricingFinder) bean;
    }

    PricingFinder(JdbcTemplate template, ILoader loader) {
        super(template, loader);
    }

    /**
     * 
     */
    private void init() {
        StopWatch watch = new StopWatch();
        watch.start();
        LOG.debug("Finder Initialized successfully.");
        watch.stop();
        LOG.debug("Watch: " + watch.toString());
    }

    public BigDecimal getPriceForDestination(String digits) {
        BigDecimal retVal = null;
        try {
            String query = "Select TOP 1 price from " + loader.getTableName()
                    + " Where '" + digits
                    + "' like CONCAT(dgts, '%') order by dgts desc;";
            LOG.debug("Method: getPriceForDestination - Select query:\n"
                    + query);
            retVal = (BigDecimal) this.jdbcTemplate.queryForObject(query,
                    BigDecimal.class);
            LOG.debug("Method: getPriceForDestination - Best Match: " + retVal);
        } catch (Exception e) {
            LOG
                    .error("ERROR occurred in PricingFinder.getPriceForDestination. "
                            + digits + " output " + retVal);
        }
        return retVal;
    }

    public BigDecimal getPriceForItemAndNumber(PricingField pricingField) {
        return getPricingResultForItemNumber(pricingField).getPrice();
    }

    public PricingResult getPricingResultForItemNumber(PricingField pricingField) {
        PricingResult result = null;
        String strSql = null;
        if ("dst".equalsIgnoreCase(pricingField.getName())) {
            String query = "Select TOP 1 price from " + loader.getTableName()
                    + " Where " + pricingField.getStrValue()
                    + " like CONCAT(dgts, '%') order by dgts desc;";
        }

        // this.jdbcTemplate.query("", new PricingResultMapper());

        return result;
    }

}
