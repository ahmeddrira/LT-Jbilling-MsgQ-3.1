/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2008 Enterprise jBilling Software Ltd. and Emiliano Conde

    This file is part of jbilling.

    jbilling is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    jbilling is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sapienter.jbilling.server.mediation.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.Constants;
import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Record;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.util.PreferenceBL;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * JDBCReader allows reading event records from a database for 
 * mediation. By default it reads all field from a table 'cdr'that 
 * are greater than the last id that was read during a previous run.
 * (stored in preference MEDIATION_JDBC_READER_LAST_ID).
 */
public class JDBCReader extends PluggableTask implements IMediationReader {

    private static final Logger LOG = Logger.getLogger(JDBCReader.class);

    // plug-in parameters
    protected static final String PARAM_DATABASE_NAME = "database_name";
    protected static final String PARAM_TABLE_NAME = "table_name";
    protected static final String PARAM_KEY_COLUMN_NAME = "key_column_name";
    protected static final String PARAM_WHERE_APPEND = "where_append";
    protected static final String PARAM_DRIVER = "driver";
    protected static final String PARAM_URL = "url";
    protected static final String PARAM_USERNAME = "username";
    protected static final String PARAM_PASSWORD = "password";

    // defaults
    protected static final String DATABASE_NAME_DEFAULT = "jbilling_cdr";
    protected static final String TABLE_NAME_DEFAULT = "cdr";
    protected static final String KEY_COLUMN_NAME_DEFAULT = "id";
    protected static final String DRIVER_DEFAULT = "org.hsqldb.jdbcDriver";
    protected static final String USERNAME_DEFAULT = "SA";
    protected static final String PASSWORD_DEFAULT = "";

    protected Connection connection = null;

    private int maxId = 0; // last record that was processed

    public JDBCReader() {

    }

    @Override
    public boolean validate(Vector<String> messages) {
        try {
            connection = getConnection();
        } catch (SQLException sqle) {
            throw new SessionInternalError(sqle);
        } catch (ClassNotFoundException cnfe) {
            throw new SessionInternalError(cnfe);
        }

        return true;
    }

    @Override
    public Iterator<Record> iterator() {
        try {
            return new Reader(getRecords());
        } catch (Exception e) {
            throw new SessionInternalError(e);
        }
    }

    /**
     * Returns a connection to the database
     */
    protected Connection getConnection() throws SQLException, ClassNotFoundException{
        String driver = getDriver();
        String url = getUrl();
        String username = getParameter(PARAM_USERNAME, USERNAME_DEFAULT);
        String password = getParameter(PARAM_PASSWORD, PASSWORD_DEFAULT);

        // create connection
        Class.forName(driver); // load driver
        Connection conn = DriverManager.getConnection(url, username, password);

        return conn;
    }

    /**
     * Returns the database name, either from a plug-in parameter or
     * the default.
     */
    protected String getDatabaseName() {
        return getParameter(PARAM_DATABASE_NAME, DATABASE_NAME_DEFAULT);
    }

    /**
     * Returns the table name, either from a plug-in parameter 
     * or the default.
     */
    protected String getTableName() {
        return getParameter(PARAM_TABLE_NAME, TABLE_NAME_DEFAULT);
    }

    /**
     * Returns the driver string, either from a plug-in parameter or
     * the default.
     */
    protected String getDriver() {
        return getParameter(PARAM_DRIVER, DRIVER_DEFAULT);
    }

    /**
     * Returns the url string, either from a plug-in parameter or
     * a default.
     */
    protected String getUrl() {
        String url = (String) parameters.get(PARAM_TABLE_NAME);
        if (url != null) {
            return url;
        }

        // construct a default hsqldb url for a database in the 
        // 'jbilling/resources/mediation' directory.
        return "jdbc:hsqldb:" + Util.getSysProp("base_dir") + "mediation/" +
                getDatabaseName() + ";shutdown=true";
    }

    /**
     * Returns a result set of all the records to be processed. This
     * can be overrided when, for example, a stored procedure is used
     * to retrieve the data.
     */
    protected ResultSet getRecords() throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(getQueryString());
    }

    /**
     * Returns the default query string that selects all fields from
     * a table with a primary key greater than a value stored in the
     * MEDIATION_JDBC_READER_LAST_ID preference. Also allows the 
     * WHERE clause to be appended with a value from a plug-in 
     * parameter. This can be overrided to provide a specific query
     * to be executed that returns records to be processed.
     */
    protected String getQueryString() throws SQLException {
        String tableName = getTableName();
        String keyColumnName = getKeyColumnName();

        // get the maximum id last processed
        PreferenceBL preferenceBL = new PreferenceBL();
        try {
            preferenceBL.set(getEntityId(), 
                    Constants.PREFERENCE_MEDIATION_JDBC_READER_LAST_ID);
        } catch (EmptyResultDataAccessException fe) { 
            // use default
        }
        Integer maxId = preferenceBL.getInt(); 


        String query = "SELECT * FROM " + tableName + " WHERE " + 
            keyColumnName + " > " + maxId;

        String whereAppend = (String) parameters.get(PARAM_WHERE_APPEND);
        if (whereAppend != null) {
            query += " " + whereAppend + " ";
        }

        query += "ORDER BY " + keyColumnName;

        return query;
    }

    /**
     * Returns the key column name. This is either from a plug-in 
     * parameter, otherwise from the database metadata. Failing that,
     * it defaults to 'ID'.
     */ 
    protected String getKeyColumnName() throws SQLException {
        // try getting primary key column name from plug-in parameter
        String keyColumnName = (String) parameters.get(PARAM_KEY_COLUMN_NAME);
        if (keyColumnName != null) {
            return keyColumnName;
        }

        // none, so try from metadata

        // first, try to get the table name in the correct case (needed for
        // hsqldb driver, which is case-sensitive for metadata info)
        String tableName = getTableName();
        ResultSet tables = connection.getMetaData().getTables(null, null, null,
                null);
        while (tables.next()) {
            String table = tables.getString(3);
            if (table.equalsIgnoreCase(tableName)) {
                tableName = table;
                break;
            }
        }

        // now get the primary key from metadata
        ResultSet keys = connection.getMetaData().getPrimaryKeys(null, null, 
                tableName);
        if (keys.next()) {
            keyColumnName = keys.getString("COLUMN_NAME");
            if (keys.next()) {
                throw new SessionInternalError("Primary key is expected to be " +
                        "a single column, not a composite key.");
            }
        } else {
            // could not determine from metadata, default to 'id'
            LOG.warn("No primary key found, default to '" + 
                    KEY_COLUMN_NAME_DEFAULT + "'");
            keyColumnName = KEY_COLUMN_NAME_DEFAULT;
        }

        return keyColumnName;
    }

    /**
     * Gets called with the primary key id of the record just 
     * processed. Can be overrided to provide a means of marking 
     * processed records. By default, just saves the last id 
     * processed.
     */
    protected void recordProcessed(int id) {
        maxId = id;
    }

    /**
     * Gets called after the last record is processed. Can be 
     * overrided to be used for clean-up. By default, just saves the
     * last record id processed in the MEDIATION_JDBC_READER_LAST_ID 
     * preference. 
     */
    protected void processingComplete() {
        // set the maximum id last processed
        PreferenceBL preferenceBL = new PreferenceBL();
        try {
            preferenceBL.set(getEntityId(), 
                    Constants.PREFERENCE_MEDIATION_JDBC_READER_LAST_ID);
        } catch (EmptyResultDataAccessException fe) { 
            LOG.warn("No max id preference set, skipping update");
            return;
        }

        preferenceBL.createUpdateForEntity(getEntityId(),
                Constants.PREFERENCE_MEDIATION_JDBC_READER_LAST_ID, maxId,
                null, null);
    }

    /**
     * The Record iterator class.
     */ 
    public class Reader implements Iterator<Record> {
        private ResultSet records;
        private PricingField.Type[] columnTypes;
        private String[] columnNames;
        private int keyColumnIndex;

        private Record currentRecord;
        private boolean isRecordUsed; // whether record has been returned

        protected Reader(ResultSet records) { 
            this.records = records;
            currentRecord = null;
            isRecordUsed = true;

            try {
                setColumnInfo();
            } catch (SQLException sqle) {
                throw new SessionInternalError(sqle);
            }
        }

        public boolean hasNext() {
            try {
                return updateCurrent();
            } catch (SQLException sqle) {
                throw new SessionInternalError(sqle);
            }
        }

        public Record next() {
            try {
                if (updateCurrent()) {
                    isRecordUsed = true;
                    return currentRecord;
                }
                throw new NoSuchElementException("No more records.");
            } catch (SQLException sqle) {
                throw new SessionInternalError(sqle);
            }
        }

        /**
         * Returns whether a new records is available to be returned.
         * Gets the next record from the result set when needed. 
         * Closes the DB connection when there are none left.
         */
        private boolean updateCurrent() throws SQLException {
            if (!isRecordUsed) {
                // a record is available
                return true;
            }

            // try to get a new record
            if (records.next()) {
                currentRecord = new Record();
                for (int i = 0; i < columnTypes.length; i++) {
                    switch (columnTypes[i]) {
                        case STRING:
                            currentRecord.addField(new PricingField(columnNames[i], 
                                    records.getString(i + 1)), i == keyColumnIndex);
                            break;

                        case INTEGER:
                            currentRecord.addField(new PricingField(columnNames[i], 
                                    records.getInt(i + 1)), i == keyColumnIndex);
                            break;

                        case FLOAT:
                            currentRecord.addField(new PricingField(columnNames[i], 
                                    records.getDouble(i + 1)), i == keyColumnIndex);
                            break;

                        case DATE:
                            currentRecord.addField(new PricingField(columnNames[i], 
                                    records.getTimestamp(i + 1)), i == keyColumnIndex);
                            break;
                    }
                }

                // primary key can't be grouped?
                currentRecord.setPosition(1);

                // there is now a new record available
                isRecordUsed = false;
                return true;
            }

            // no more records
            processingComplete();
            // close db connection
            connection.close();

            return false;
        }

        /**
         * Sets the column info (names, types, key) from the database
         * metadata. 
         */
        private void setColumnInfo() throws SQLException {
            ResultSetMetaData metaData = records.getMetaData();
            columnTypes = new PricingField.Type[metaData.getColumnCount()];
            columnNames = new String[metaData.getColumnCount()];
            keyColumnIndex = -1;
            String keyColumnName = getKeyColumnName();

            for (int i = 0; i < columnTypes.length; i++) {
                // set column types of the result set
                switch (metaData.getColumnType(i + 1)) {
                    case Types.CHAR:
                    case Types.LONGNVARCHAR:
                    case Types.LONGVARCHAR:
                    case Types.NCHAR:
                    case Types.NVARCHAR:
                    case Types.VARCHAR:
                        columnTypes[i] = PricingField.Type.STRING;
                        break;

                    case Types.BIGINT:
                    case Types.BIT:
                    case Types.BOOLEAN:
                    case Types.INTEGER:
                    case Types.SMALLINT:
                    case Types.TINYINT:
                        columnTypes[i] = PricingField.Type.INTEGER;
                        break;

                    case Types.DECIMAL:
                    case Types.DOUBLE:
                    case Types.FLOAT:
                    case Types.NUMERIC:
                    case Types.REAL:
                        columnTypes[i] = PricingField.Type.FLOAT;
                        break;

                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        columnTypes[i] = PricingField.Type.DATE;
                        break;

                    default:
                        throw new SessionInternalError("In column '" + 
                            metaData.getColumnName(i + 1) + 
                            "', unsupported java.sql.Type: " + 
                            metaData.getColumnTypeName(i + 1));
                }

                // set column names (lower case for rules)
                columnNames[i] = metaData.getColumnName(i + 1).toLowerCase();

                // check if primary key
                if (columnNames[i].equalsIgnoreCase(keyColumnName)) {
                    keyColumnIndex = i;
                }
            }

            if (keyColumnIndex == -1) {
                LOG.warn("No primary key column found in result set");
            }
        }

        public void remove() {
            // needed to comply with Iterator only
            throw new UnsupportedOperationException("remove not supported");
        }
    }

    /**
     * Convenience method for getting String plug-in parameters
     */ 
    private String getParameter(String name, String defaultValue) {
        String value = (String) parameters.get(name);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }
}
