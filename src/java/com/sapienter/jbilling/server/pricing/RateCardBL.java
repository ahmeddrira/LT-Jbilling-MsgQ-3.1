/*
 JBILLING CONFIDENTIAL
 _____________________

 [2003] - [2012] Enterprise jBilling Software Ltd.
 All Rights Reserved.

 NOTICE:  All information contained herein is, and remains
 the property of Enterprise jBilling Software.
 The intellectual and technical concepts contained
 herein are proprietary to Enterprise jBilling Software
 and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material
 is strictly forbidden.
 */

package com.sapienter.jbilling.server.pricing;

import au.com.bytecode.opencsv.CSVReader;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.mediation.cache.IFinder;
import com.sapienter.jbilling.server.mediation.cache.ILoader;
import com.sapienter.jbilling.server.mediation.task.IMediationReader;
import com.sapienter.jbilling.server.pricing.db.RateCardDAS;
import com.sapienter.jbilling.server.pricing.db.RateCardDTO;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.util.sql.TableGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Business Logic for RateCardDTO crud, and for creating and updating the rating tables
 * associated with the card.
 *
 * @author Brian Cowdery
 * @since 16-Feb-2012
 */
public class RateCardBL {
    private static final Logger LOG = Logger.getLogger(RateCardBL.class);

    public static final int BATCH_SIZE = 10;
    public static final String DEFAULT_DATA_TYPE = "varchar(255)";

    private RateCardDAS rateCardDas;
    private JdbcTemplate jdbcTemplate;
    private TableGenerator tableGenerator;

    private RateCardDTO rateCard;

    public RateCardBL() {
        _init();
    }

    public RateCardBL(Integer rateCardId) {
        _init();
        set(rateCardId);
    }

    public RateCardBL(RateCardDTO rateCard) {
        _init();
        this.rateCard = rateCard;
        this.tableGenerator = new TableGenerator(rateCard.getTableName(), RateCardDTO.TABLE_COLUMNS);
    }

    public void set(Integer rateCardId) {
        this.rateCard = rateCardDas.find(rateCardId);
        this.tableGenerator = new TableGenerator(rateCard.getTableName(), RateCardDTO.TABLE_COLUMNS);
    }

    private void _init() {
        this.rateCardDas = new RateCardDAS();
        this.jdbcTemplate = Context.getBean(Context.Name.JDBC_TEMPLATE);
    }

    /**
     * Returns the RateCardDTO object being managed by this BL class.
     * @return rate card object
     */
    public RateCardDTO getEntity() {
        return rateCard;
    }

    /**
     * Create a new rate card with the specified rates.
     *
     * @param rateCard rate card to create
     * @param rates file handle of the CSV on disk containing the rates.
     * @return id of the saved rate card
     */
    public Integer create(RateCardDTO rateCard, File rates) {
        if (rateCard != null) {
            LOG.debug("Saving new rate card " + rateCard);
            this.rateCard = rateCardDas.save(rateCard);
            this.tableGenerator = new TableGenerator(this.rateCard.getTableName(), RateCardDTO.TABLE_COLUMNS);

            LOG.debug("Creating a new rate table & saving rating data");
            if (rates != null) {
                try {
                    saveRates(rates);
                } catch (IOException e) {
                    throw new SessionInternalError("Could not load rating table", e, new String[] { "RateCarDTO,rates,cannot.read.file" });
                } catch (SessionInternalError e) {
                    dropRates();
                    throw e;
                }

                registerSpringBeans();
            }

            return this.rateCard.getId();
        }

        LOG.error("Cannot save a null RateCardDTO!");
        return null;
    }

    /**
     * Updates an existing rate card and rates.
     *
     * @param rateCard rate card to create
     * @param rates file handle of the CSV on disk containing the rates.
     */
    public void update(RateCardDTO rateCard, File rates) {
        if (this.rateCard != null) {
            // re-create the rating table
            LOG.debug("Re-creating the rate table & saving updated rating data");
            if (rates != null) {
                dropRates();

                try {
                    saveRates(rates);
                } catch (IOException e) {
                    throw new SessionInternalError("Could not load rating table", e, new String[] { "RateCarDTO,rates,cannot.read.file" });
                }
            }

            // prepare SQL to rename the table if the table name has changed
            String originalTableName = this.rateCard.getTableName();
            String alterTableSql = null;

            if (!originalTableName.equals(rateCard.getTableName())) {
                alterTableSql = this.tableGenerator.buildRenameTableSQL(rateCard.getTableName());
            }

            // do update
            this.rateCard.setName(rateCard.getName());
            this.rateCard.setTableName(rateCard.getTableName());

            LOG.debug("Saving updates to rate card " + rateCard.getId());
            this.rateCard = rateCardDas.save(rateCard);
            this.tableGenerator = new TableGenerator(this.rateCard.getTableName(), RateCardDTO.TABLE_COLUMNS);

            // do rename after saving the new table name
            if (alterTableSql != null) {
                LOG.debug("Renaming the rate table");
                jdbcTemplate.execute(alterTableSql);
            }

            // re-register spring beans if rates were updated
            if (rates != null) {
                removeSpringBeans();
                registerSpringBeans();
            }

        } else {
            LOG.error("Cannot update, RateCardDTO not found or not set!");
        }
    }

    /**
     * Deletes the current rate card managed by this class.
     */
    public void delete() {
        if (rateCard != null) {
            rateCardDas.delete(rateCard);
            dropRates();

        } else {
            LOG.error("Cannot delete, RateCardDTO not found or not set!");
        }
    }



    /*
            Rate Table Database Stuff
     */

    /**
     * Drop the rate table of a rate card.
     */
    public void dropRates() {
        String dropSql = tableGenerator.buildDropTableSQL();
        jdbcTemplate.execute(dropSql);
    }

    /**
     * Updates the rate table of a rate card with the rating information in
     * the given CSF file of rates.
     *
     * @param rates file handle of the CSV on disk containing the rates.
     * @throws IOException if file does not exist or is not readable
     */
    public void saveRates(File rates) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(rates));
        String[] line = reader.readNext();
        validateCsvHeader(line);

        // parse the header and read out the extra columns.
        // ignore the default rate card table columns as they should ALWAYS exist
        int start = RateCardDTO.TABLE_COLUMNS.size();
        for (int i = start; i <line.length; i++) {
            tableGenerator.addColumn(new TableGenerator.Column(line[i], DEFAULT_DATA_TYPE, true));
        }

        // create rate table
        String createSql = tableGenerator.buildCreateTableSQL();
        jdbcTemplate.execute(createSql);

        // load rating data in batches
        String insertSql = tableGenerator.buildInsertPreparedStatementSQL();

        int id = 1;
        List<List<String>> rows = new ArrayList<List<String>>();
        for (int i = 1; i <= BATCH_SIZE; i++) {
            // add row to insert batch
            line = reader.readNext();
            if (line != null) rows.add(Arrays.asList(line));

            // end of file
            if (line == null) {
                id += executeBatchInsert(insertSql, rows, id);
                break; // done
            }

            // reached batch limit
            if (i == BATCH_SIZE) {
                id += executeBatchInsert(insertSql, rows, id);
                i = 1; rows.clear(); // next batch
            }
        }
    }

    /**
     * Validates that the uploaded CSV file starts with the expected columns from {@link RateCardDTO#TABLE_COLUMNS}.
     * If the column names don't match or are in an incorrect order a SessionInternalError will be throw.
     *
     * @param header header line to validate
     * @throws SessionInternalError thrown if errors found in header data
     */
    private void validateCsvHeader(String[] header) throws SessionInternalError {
        List<String> errors = new ArrayList<String>();

        List<TableGenerator.Column> columns = RateCardDTO.TABLE_COLUMNS;
        for (int i = 1; i < columns.size(); i++) {
            String columnName = header[i - 1].trim();    // id column will be missing from the header
            String expected = columns.get(i).getName();  // because the id value is generated

            if (!expected.equalsIgnoreCase(columnName)) {
                errors.add("RateCardDTO,rates,rate.card.unexpected.header.value," + expected + "," + columnName);
            }
        }

        if (!errors.isEmpty()) {
            throw new SessionInternalError("Rate card CSV has errors in the order of columns, or is missing required columns",
                    errors.toArray(new String[errors.size()]));
        }
    }

    /**
     * Inserts a batch of records into the database.
     *
     * @param insertSql prepared statement SQL
     * @param rows list of rows to insert
     */
    private int executeBatchInsert(String insertSql, final List<List<String>> rows, final int lastId) {
        LOG.debug("Inserting " + rows.size() + " records:");
        LOG.debug(rows);

        jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement, int batch) throws SQLException {
                List<String> values = rows.get(batch);

                // incremented id
                preparedStatement.setInt(1, lastId + batch);

                // values read from the csv file
                for (int i = 0; i < values.size(); i++) {
                    String value = values.get(i);
                    switch (i) {
                        case 2:  // rate card rate
                            preparedStatement.setBigDecimal(i + 2, StringUtils.isNotBlank(value) ? new BigDecimal(value) : BigDecimal.ZERO);
                            break;

                        default: // everything else
                            preparedStatement.setObject(i + 2, value);
                    }
                }
            }

            public int getBatchSize() {
                return rows.size();
            }
        });

        return lastId + rows.size();
    }



    /*
            Spring Beans stuff
     */

    /**
     * Registers spring beans with the application context so support caching and look-up
     * of pricing from the rating tables.
     */
    public void registerSpringBeans() {
        RateCardBeanFactory factory = getBeanFactory();

        String readerBeanName = factory.getReaderBeanName();
        BeanDefinition readerBeanDef = factory.getReaderBeanDefinition(rateCard.getCompany().getId());

        String loaderBeanName = factory.getLoaderBeanName();
        BeanDefinition loaderBeanDef = factory.getLoaderBeanDefinition(readerBeanName);

        String finderBeanName = factory.getFinderBeanName();
        BeanDefinition finderBeanDef = factory.getFinderBeanDefinition(loaderBeanName);

        LOG.debug("Registering beans: " + readerBeanName + ", " + loaderBeanName + ", " + finderBeanName);

        // register spring beans!
        GenericApplicationContext ctx = (GenericApplicationContext) Context.getApplicationContext();
        ctx.registerBeanDefinition(readerBeanName, readerBeanDef);
        ctx.registerBeanDefinition(loaderBeanName, loaderBeanDef);
        ctx.registerBeanDefinition(finderBeanName, finderBeanDef);
    }

    /**
     * Removes registered spring beans from the application context.
     */
    public void removeSpringBeans() {
        RateCardBeanFactory factory = getBeanFactory();

        String readerBeanName = factory.getReaderBeanName();
        String loaderBeanName = factory.getLoaderBeanName();
        String finderBeanName = factory.getFinderBeanName();

        LOG.debug("Removing beans: " + readerBeanName + ", " + loaderBeanName + ", " + finderBeanName);

        GenericApplicationContext ctx = (GenericApplicationContext) Context.getApplicationContext();
        ctx.removeBeanDefinition(readerBeanName);
        ctx.removeBeanDefinition(loaderBeanName);
        ctx.removeBeanDefinition(finderBeanName);
    }

    /**
     * Returns an instance of the {@link RateCardBeanFactory} for producing rate card beans
     * used for pricing.
     *
     * @return rate card bean factory
     */
    public RateCardBeanFactory getBeanFactory() {
        return new RateCardBeanFactory(rateCard);
    }
}
