/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

package com.sapienter.jbilling.server.pricing;

import com.sapienter.jbilling.server.mediation.cache.BasicLoaderImpl;
import com.sapienter.jbilling.server.mediation.cache.IFinder;
import com.sapienter.jbilling.server.mediation.cache.ILoader;
import com.sapienter.jbilling.server.mediation.cache.RateCardFinder;
import com.sapienter.jbilling.server.mediation.task.IMediationReader;
import com.sapienter.jbilling.server.mediation.task.StatelessJDBCReader;
import com.sapienter.jbilling.server.pricing.db.RateCardDTO;
import com.sapienter.jbilling.server.util.Context;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * RateCardBeanFactory
 *
 * @author Brian Cowdery
 * @since 18-02-2012
 */
public class RateCardBeanFactory {

    private final RateCardDTO rateCard;

    public RateCardBeanFactory(RateCardDTO rateCard) {
        this.rateCard = rateCard;
    }

    public IMediationReader getReaderInstance(Integer entityId) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("table_name", rateCard.getTableName());
        parameters.put("batch_size", String.valueOf(RateCardBL.BATCH_SIZE));

        DataSource dataSource = Context.getBean(Context.Name.DATA_SOURCE);
        JdbcTemplate jdbcTemplate = Context.getBean(Context.Name.JDBC_TEMPLATE);

        StatelessJDBCReader reader = new StatelessJDBCReader();
        reader.setParameters(parameters);
        reader.setDataSource(dataSource);
        reader.setJdbcTemplate(jdbcTemplate);
        reader.setEntityId(entityId);

        return reader;
    }

    public ILoader getCacheLoaderInstance(IMediationReader reader) {
        JdbcTemplate memcacheJdbcTemplate = Context.getBean(Context.Name.MEMCACHE_JDBC_TEMPLATE);
        TransactionTemplate memcacheTxTemplate = Context.getBean(Context.Name.MEMCACHE_TX_TEMPLATE);

        BasicLoaderImpl loader = new BasicLoaderImpl();
        loader.setJdbcTemplate(memcacheJdbcTemplate);
        loader.setTransactionTemplate(memcacheTxTemplate);
        loader.setReader(reader);
        loader.setTableName(rateCard.getTableName());
        loader.setIndexName(rateCard.getTableName() + "_idx");
        loader.setIndexColumnNames("match");

        loader.init();
        return loader;
    }

    public IFinder getCacheFinderInstance(ILoader loader) {
        JdbcTemplate memcacheJdbcTemplate = Context.getBean(Context.Name.MEMCACHE_JDBC_TEMPLATE);

        RateCardFinder finder = new RateCardFinder(memcacheJdbcTemplate, loader);
        finder.init();

        return finder;
    }
}
