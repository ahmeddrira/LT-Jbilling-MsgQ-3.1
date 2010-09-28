/*
    jBilling - The Enterprise Open Source Billing System
    Copyright (C) 2003-2009 Enterprise jBilling Software Ltd. and Emiliano Conde

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

package com.sapienter.jbilling.server.mediation.task;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.item.PricingField;
import com.sapienter.jbilling.server.mediation.Record;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Brian Cowdery
 * @since 27-09-2010
 */
public class JDBCReader extends AbstractJDBCReader {

    private String timestampUpdateSql = null;

    public JDBCReader() {
    }

    /**
     * Returns an SQL query to read records that have not previously been read.
     *
     * If MarkMethod.TIMESTAMP is used, then this will limit read records to those that have a
     * null time stamp column value.
     *
     * If MarkMethod.LAST_ID is used, then this will limit read records to those where the record
     * id is greater than the last read id (example: "WHERE id > 123").
     *
     * @return SQL query string
     */
    @Override
    protected String getSqlQueryString() {
        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ")
                .append(getTableName())
                .append(" WHERE ");

        // constrain query based on marking method
        if (getMarkMethod() == MarkMethod.LAST_ID) {
            if (getKeyColumns().size() > 1)
                throw new SessionInternalError("LAST_ID marking method only allows for one key column.");
            query.append(getKeyColumns().get(0)).append(" > ").append(getLastId());

        } else if (getMarkMethod() == MarkMethod.TIMESTAMP) {
            query.append(getTimestampColumnName()).append(" IS NULL ");

        } else {
            throw new SessionInternalError("Marking method not configured, 'id' or 'timestamp_column' not set.");
        }

        // append optional user-defined where clause
        String where = getParameter(PARAM_WHERE_APPEND, (String) null);
        if (where != null)
            query.append(where).append(" ");

        // append optional user-defined order, or build one by using defined key columns
        String order = getParameter(PARAM_ORDER_BY, (String) null);
        if (order != null) {
            query.append(order);

        } else {
            for (Iterator<String> it = getKeyColumns().iterator(); it.hasNext();) {
                query.append(it.next());
                if (it.hasNext())
                    query.append(", ");
            }
        }

        return query.toString();
    }

    /**
     * If MarkMethod.TIMESTAMP, this method will update the time stamp column of the
     * database row after it has been read.
     *
     * If MarkMethod.LAST_ID, this method will increment the "last read ID" (see {@link #getLastId()}
     * field value with the id of the record that was read. This does not write the property out to the
     * database, as to prevent pre-maturely marking a record as read should a subsequent read throw an
     * exception that prevents the batch from being processed (see {@link #batchRead(java.util.List, int[])}.
     *
     * @param record record that was read
     * @param keyColumnIndexes index of record PricingFields that represent key columns.
     */
    @Override
    protected void recordRead(final Record record, final int[] keyColumnIndexes) {
        if (getMarkMethod() == MarkMethod.TIMESTAMP) {
            if (timestampUpdateSql == null) {
                // build update query to mark timestamps
                StringBuilder query = new StringBuilder()
                        .append("UPDATE ")
                        .append(getTableName())
                        .append(" SET ")
                        .append(getTimestampColumnName())
                        .append(" = ? ")
                        .append(" WHERE ");

                // add primary key constraint using key columns
                for (int key = 0; key < keyColumnIndexes.length; key++) {
                    PricingField field = record.getFields().get(key);
                    query.append(field.getName()).append(" = ? ");
                    
                     if (key < keyColumnIndexes.length-1)
                         query.append(" AND ");
                }

                timestampUpdateSql = query.toString();
            }
        }

        if (getMarkMethod() == MarkMethod.LAST_ID) {
            setLastId(record.getFields().get(keyColumnIndexes[0]).getIntValue());
        }
    }

    /**
     * If MarkMethod.LAST_ID, this method will flush out the "last read ID" preference to the
     * jBilling database after the entire batch has been read.
     *
     * @param records list of records that were read
     * @param keyColumnIndexes index of record PricingFields that represent key columns.
     */
    @Override
    protected void batchRead(final List<Record> records, final int[] keyColumnIndexes) {
        if (getMarkMethod() == MarkMethod.TIMESTAMP) {
            // execute batch update for all read records
            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            getJdbcTemplate().batchUpdate(
                    timestampUpdateSql,
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setTimestamp(1, timestamp);

                            // query parameters for primary key SQL
                            int j = 1; // prepared statement parameter index
                            Record record = records.get(i);
                            for (int key : keyColumnIndexes) {
                                PricingField field = record.getFields().get(key);
                                switch (field.getType()) {
                                    case STRING:
                                        ps.setString(j++, field.getStrValue());
                                        break;
                                    case INTEGER:
                                        ps.setInt(j++, field.getIntValue());
                                        break;
                                    case DECIMAL:
                                        ps.setBigDecimal(j++, field.getDecimalValue());
                                        break;
                                    case DATE:
                                        ps.setTimestamp(j++, new Timestamp(field.getDateValue().getTime()));
                                        break;
                                    case BOOLEAN:
                                        ps.setBoolean(j++, field.getBooleanValue());
                                        break;
                                }
                            }
                        }

                        public int getBatchSize() {
                            return records.size();
                        }
                    });
        }

        if (getMarkMethod() == MarkMethod.LAST_ID) {
            flushLastId();
        }
    }
}
