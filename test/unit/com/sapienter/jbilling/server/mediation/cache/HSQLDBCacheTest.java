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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import com.sapienter.jbilling.server.BigDecimalTestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sapienter.jbilling.server.mediation.cache.IFinder;
import com.sapienter.jbilling.server.mediation.cache.ILoader;
import com.sapienter.jbilling.server.mediation.cache.PricingFinder;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class HSQLDBCacheTest extends BigDecimalTestCase {

    private static final String PRICING_LOADER_BEAN = "pricingLoader";
    private static final String PRICING_FINDER_BEAN = "pricingFinder";
    private static final String MEMCACHE_DATASOURCE_BEAN = "memcacheDataSource";

    private static final ApplicationContext spring
            = new ClassPathXmlApplicationContext(new String[] { "/jbilling-caching.xml" });

    // classes under test
    private ILoader loader = null;
    private IFinder finder = null;

    public HSQLDBCacheTest() {
    }

    public HSQLDBCacheTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        loader = (ILoader) spring.getBean(PRICING_LOADER_BEAN);
        finder = (IFinder) spring.getBean(PRICING_FINDER_BEAN);
    }

    private DataSource getDataSource() {
        return (DataSource) spring.getBean(MEMCACHE_DATASOURCE_BEAN);
    }

    /*
        Tests
     */

    public void testLoader() throws Exception {
        assertEquals("rules_table", loader.getTableName());

        // count the number of records loaded
        DataSource dataSource = getDataSource();
        Connection connection = DataSourceUtils.getConnection(dataSource);

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select count(*) as REC_COUNT from " + loader.getTableName());
        result.next();

        long recordsCount = result.getLong("REC_COUNT");
       
        assertTrue("Loader successfully populated records in the database",recordsCount > 0);
        assertEquals("Loaded correct number of records", 1769, recordsCount);

        // cleanup
        result.close();
        statement.close();
        DataSourceUtils.releaseConnection(connection, dataSource);
    }

    public void testPricingFinder() {
        PricingFinder finder = (PricingFinder) this.finder;

        BigDecimal val = finder.getPriceForDestination("5215585888");
        assertTrue("Finder returned a value greater than zero", val.compareTo(new BigDecimal("0")) > 0);
        assertEquals("Found the right value", new BigDecimal("0.175"), val);

        val = finder.getPriceForDestination("9699999888");
        assertTrue("Finder returned a value greater than zero", val.compareTo(new BigDecimal("0")) > 0);
        assertEquals("Found the right value", new BigDecimal("0.990"), val);

        val = finder.getPriceForDestination("7400000000");
        assertTrue("Finder returned a value greater than zero", val.compareTo(new BigDecimal("0")) > 0);
        assertEquals("Found the right value", new BigDecimal("0.093"), val);
    }
}
