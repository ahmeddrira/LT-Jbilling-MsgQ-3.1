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
import com.sapienter.jbilling.server.pricing.db.RateCardDAS;
import com.sapienter.jbilling.server.pricing.db.RateCardDTO;
import com.sapienter.jbilling.server.util.Context;
import com.sapienter.jbilling.server.util.sql.TableGenerator;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Business Logic for RateCardDTO crud, and for creating and updating the rating tables
 * associated with the card.
 *
 * @author Brian Cowdery
 * @since 16-Feb-2012
 */
public class RateCardBL {
    private static final Logger LOG = Logger.getLogger(RateCardBL.class);

    private static final int BATCH_SIZE = 10;


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

    public void set(Integer rateCardId) {
        this.rateCard = rateCardDas.find(rateCardId);
        this.tableGenerator = new TableGenerator(rateCard.getTableName(), RateCardDTO.TABLE_COLUMNS);
    }

    private void _init() {
        this.rateCardDas = new RateCardDAS();
        this.jdbcTemplate = Context.getBean(Context.Name.JDBC_TEMPLATE);
    }

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

            if (rates != null) {
                try {
                    setRates(rates);
                } catch (IOException e) {
                    throw new SessionInternalError("Could not load rating table", e, new String[] { "RateCarDTO,rates,cannot.read.file" });
                }

                registerPricingFinderBean();
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
            // do update
            this.rateCard.setName(rateCard.getName());
            this.rateCard.setTableName(rateCard.getTableName());

            LOG.debug("Saving updates to rate card " + rateCard.getId());
            this.rateCard = rateCardDas.save(rateCard);

            // recreate the rating table
            if (rates != null) {
                dropRates();

                try {
                    setRates(rates);
                } catch (IOException e) {
                    throw new SessionInternalError("Could not load rating table", e, new String[] { "RateCarDTO,rates,cannot.read.file" });
                }
            }

        } else {
            LOG.error("Cannot update, RateCardDTO not found or not set!");
        }
    }

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
     */
    public void setRates(File rates) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(rates));
        String[] line = reader.readNext();

        validateCsvHeader(line);

        // parse the header and read out the extra columns.
        // ignore the default rate card table columns as they should ALWAYS exist
        int start = RateCardDTO.TABLE_COLUMNS.size();
        TableGenerator.Column[] columns = new TableGenerator.Column[line.length - start];

        for (int i = start; i < line.length; i++) {
            columns[i] = new TableGenerator.Column(line[i], "varchar (255)", true);
        }

        tableGenerator.addColumns(columns);

        // create rate table
        String createSql = tableGenerator.buildCreateTableSQL();
        LOG.debug("Create rating table SQL: " + createSql);
        jdbcTemplate.execute(createSql);

        // load rating data in batches
        String insertSql = tableGenerator.buildInsertPreparedStatementSQL();

        LOG.debug("Insert SQL: " + insertSql);

//        while ((line = reader.readNext()) != null) {
//
//        }
    }

    private void validateCsvHeader(String[] header) {

    }

    private void executeInsert(String insertSql, final String[][] rows) {
        jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement preparedStatement, int batch) throws SQLException {
                String[] values = rows[batch];
                for (int i = 0; i < values.length; i++) {
                    preparedStatement.setObject(i, values[i]);
                }
            }

            public int getBatchSize() {
                return rows.length;
            }
        });
    }

    /**
     * Register a pricing finder bean that can be used by the {@link RateCardPricingStrategy} to
     * cache and look-up prices from the rating table
     */
    public void registerPricingFinderBean() {
        // todo: get application context, create finder beans and register.
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

}
