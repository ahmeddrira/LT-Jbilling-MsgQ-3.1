package com.sapienter.jbilling.server.mediation.task;

import com.sapienter.jbilling.server.mediation.Record;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Brian Cowdery
 * @since 27-09-2010
 */
public class StatelessJDBCReader extends AbstractJDBCReader {

    public StatelessJDBCReader() {
    }

    /**
     * Returns a SQL query that reads all records present regardless of previous reads.
     *
     * @return SQL query string
     */
    @Override
    protected String getSqlQueryString() {
        StringBuilder query = new StringBuilder()
                .append("SELECT * FROM ")
                .append(getTableName())
                .append(" WHERE ");

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
     * Not implemented. Stateless JDBC reader does not record reads.
     *
     * @param record record that was read
     * @param keyColumnIndexes index of record PricingFields that represent key columns.
     */
    @Override
    protected void recordRead(final Record record, final int[] keyColumnIndexes) {
    }

    /**
     * Not implemented. Stateless JDBC reader does not record reads.
     * 
     * @param records list of records that were read
     * @param keyColumnIndexes index of record PricingFields that represent key columns.
     */
    @Override
    protected void batchRead(final List<Record> records, final int[] keyColumnIndexes) {
    }
}
