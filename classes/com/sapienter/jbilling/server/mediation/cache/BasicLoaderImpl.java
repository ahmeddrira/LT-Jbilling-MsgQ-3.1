package com.sapienter.jbilling.server.mediation.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;

import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.mediation.task.IMediationReader;

public class BasicLoaderImpl implements ILoader {

    private static final Logger LOG = Logger.getLogger(BasicLoaderImpl.class);

    private JdbcTemplate jdbcTemplate = null;
    private String indexColumnNames = null;
    private Integer readerId = null;
    private IMediationReader reader = null;

    private static final String SPACE = " ";
    private static final String COMMA = ", ";

    private String tableName = "rules_table";
    private String indexName = "rules_index";
    private boolean tableCreated = false;

    @Override
    public String getTableName() {
        LOG.debug("BasicLoaderImpl.getTableName called.");
        return tableName;
    }

    BasicLoaderImpl() {
        LOG.debug("BasicLoaderImpl() Constructor Called");
    }

    public void init() {
        initDB();
        if (tableCreated) {
            createIndexes();
        }
        LOG.debug("Loader initialized successfully.");
    }

    /**
     * 
     */
    private void initDB() {
        LOG.debug("BasicLoaderImpl: init() called - readerId="
                + readerId);
        LOG.debug("Reader ID=" + readerId + ", jdbcTemplate="
                + jdbcTemplate + ", indexColumnNames=" + indexColumnNames
                + ", Reader=" + reader);
        StopWatch watch = new StopWatch();
        watch.start();

        // try {
        // PluggableTaskBL<IMediationReader> readerTask = new
        // PluggableTaskBL<IMediationReader>();
        // LOG.debug("Reader ID=" + readerId);
        // readerTask.set(readerId);
        // reader = readerTask.instantiateTask();
        // } catch (PluggableTaskException e) {
        // throw new SessionInternalError(
        // "Could not instantiate reader plug-in.", e);
        // }
        List<String> errorMessages = new ArrayList<String>();
        // read records and normalize using the MediationProcess plug-in
        if (reader.validate(errorMessages)) {
            /*
             * Catch exceptions and log errors instead of re-throwing as
             * SessionInternalError
             */
            try {
                // /check if transactions can be begun here
                for (List<Record> thisGroup : reader) {
                    LOG.debug("Now processing " + thisGroup.size()
                            + " records.");

                    if (thisGroup.size() < 1) {
                        return;
                    }

                    if (!tableCreated) {
                        tableCreated = this.createTable(thisGroup.get(0));
                    }
                    String[] batchSqls = new String[thisGroup.size()];
                    for (int i = 0; i < batchSqls.length; i++) {
                        batchSqls[i] = computeInsertSql(thisGroup.get(i));
                    }
                    System.out
                            .println("BasicLoaderImpl: BaNow inserting records in mem db");
                    // insert record using jdbcTemplate
                    this.jdbcTemplate.batchUpdate(batchSqls);
                }
            } catch (Throwable t) {
                LOG.error("Unhandled exception occurred during loading.", t);
            }
        }

        // mark end
        watch.stop();
        LOG.debug("BasicLoaderImpl: Finished loader. Watch (secs): "
                + watch.getTotalTimeSeconds());

    }

    private boolean createIndexes() {
        LOG.debug("BasicLoaderInpl.createIndexes()");
        String[] cols = indexColumnNames.split(",");
        StringBuffer indexSql = new StringBuffer("CREATE INDEX ").append(
                indexName).append(" ON ");
        indexSql.append(tableName).append("(");
        for (String col : cols) {
            indexSql.append(col).append(COMMA);
        }
        int lastIdxOfComma = indexSql.lastIndexOf(COMMA);
        indexSql
                .replace(lastIdxOfComma, (indexSql.lastIndexOf(COMMA) + 2), ")");
        LOG.debug("BasicLoaderInpl: indexQuery= " + indexSql);
        this.jdbcTemplate.execute(indexSql.toString());
        return true;
    }

    /**
     * 
     * @param record
     * @return
     */
    private String computeInsertSql(Record record) {
        LOG.debug("BasicLoaderInpl.computeInsertSql()");
        StringBuffer values = new StringBuffer("");
        StringBuffer retVal = new StringBuffer("INSERT INTO ").append(
                getTableName()).append(SPACE).append("(");
        List<PricingField> fields = record.getFields();
        for (PricingField field : fields) {
            retVal.append(field.getName()).append(COMMA);
            values.append(field.getValue()).append(COMMA);
        }
        int lastIdxOfComma = retVal.lastIndexOf(COMMA);
        retVal.replace(lastIdxOfComma, (retVal.lastIndexOf(COMMA) + 2),
                ") VALUES (");

        lastIdxOfComma = values.lastIndexOf(COMMA);
        values.replace(lastIdxOfComma, (values.lastIndexOf(COMMA) + 2), ")");
        retVal.append(values);
        LOG.debug("BasicLoaderImpl: Insert SQL=" + retVal);
        return retVal.toString();
    }

    /**
     * 
     * @param record
     * @return
     */
    private boolean createTable(Record record) {
        LOG.debug("BasicLoaderImpl: createTable()");
        StringBuffer createTable = new StringBuffer("CREATE CACHED TABLE ")
                .append(getTableName()).append(SPACE);
        List<PricingField> fields = record.getFields();
        // String key = record.getKey();
        createTable.append("(");
        for (PricingField field : fields) {
            createTable.append(field.getName()).append(SPACE).append(
                    mapDBType(field));
            createTable.append(COMMA);
        }
        int lastIdxOfComma = createTable.lastIndexOf(COMMA);
        createTable.replace(lastIdxOfComma,
                (createTable.lastIndexOf(COMMA) + 2), ")");
        LOG.debug("BasicLoaderImpl: Create Table Query= "
                + createTable);
        this.jdbcTemplate.execute(createTable.toString());
        return true;
    }

    /**
     * 
     * @param field
     * @return
     */
    private static final String mapDBType(PricingField field) {
        String retVal = null;
        switch (field.getType()) {
        case STRING:
            retVal = " VARCHAR(100) ";
            break;
        case INTEGER:
            retVal = " INTEGER ";
            break;
        case DECIMAL:
            retVal = " NUMERIC(22,10) ";
            break;
        case DATE:
            retVal = " TIMESTAMP ";
            break;
        case BOOLEAN:
            retVal = " BOOLEAN ";
            break;
        }
        return retVal;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getIndexColumnNames() {
        return indexColumnNames;
    }

    public void setIndexColumnNames(String indexColumnNames) {
        this.indexColumnNames = indexColumnNames;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public IMediationReader getReader() {
        return reader;
    }

    public void setReader(IMediationReader reader) {
        this.reader = reader;
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        // jdbcTemplate.execute("drop table " + getTableName());
    }

}
