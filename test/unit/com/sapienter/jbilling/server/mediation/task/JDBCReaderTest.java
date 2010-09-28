package com.sapienter.jbilling.server.mediation.task;

import com.sapienter.jbilling.server.mediation.Record;
import junit.framework.TestCase;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.StopWatch;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Cowdery
 * @since 28-09-2010
 */
public class JDBCReaderTest extends TestCase {

    // local in-memory database for testing
    private static final String DATABASE_NAME = "jdbc_reader_test";
    private static final String TABLE_NAME = "records";
    private static final String URL = "jdbc:hsqldb:mem:" + DATABASE_NAME;
    private static final String DRIVER = "org.hsqldb.jdbcDriver";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private JdbcTemplate jdbcTemplate;

    // plug-in parameters
    private static Map<String, Object> parameters;
    static {
        parameters = new HashMap<String, Object>();
        parameters.put("database_name", DATABASE_NAME);
        parameters.put("table_name", TABLE_NAME);
        parameters.put("url", URL);
        parameters.put("username", USERNAME);
        parameters.put("password", PASSWORD);
        parameters.put("batch_size", "100");
    }

    // class under test
    private AbstractJDBCReader reader = new JDBCReader();

    public JDBCReaderTest() {
    }

    public JDBCReaderTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        jdbcTemplate = new JdbcTemplate(dataSource);
        createTestSchema();

        reader.setParameters(parameters);
        reader.validate(new ArrayList<String>());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
        Database setup
     */

    private void createTestSchema() {
        String sql =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";"
                + " CREATE TABLE " + TABLE_NAME + "("
                + "    id integer NOT NULL, "
                + "    content varchar(255) NOT NULL, "
                + "    jbilling_timestamp timestamp NULL, "
                + "    PRIMARY KEY (id) "
                + " );";

        jdbcTemplate.execute(sql);
    }

    private void fillTestDatabase(int rows) {
        for (int i = 0; i < rows; i++) {
            jdbcTemplate.update("INSERT INTO " + TABLE_NAME + " (id, content) values (?, ?);",
                                new Object[] { i, "row number " + i});
        }
    }

    /*
        Tests
     */

    public void testReaderConfig() throws Exception {                
        assertEquals(DATABASE_NAME, reader.getDatabaseName());
        assertEquals(DRIVER, reader.getDriverClassName());
        assertEquals(URL, reader.getUrl());
        assertEquals(USERNAME, reader.getUsername());
        assertEquals(PASSWORD, reader.getPassword());

        // case corrected table/column names
        assertEquals(TABLE_NAME.toUpperCase(), reader.getTableName());
        assertEquals("ID", reader.getKeyColumns().get(0));
        assertEquals("JBILLING_TIMESTAMP", reader.getTimestampColumnName());

        // mark method set to TIMESTAMP as "jbilling_timestamp" column exsits
        assertEquals(AbstractJDBCReader.MarkMethod.TIMESTAMP, reader.getMarkMethod());

        // generated query string
        assertEquals("SELECT * FROM RECORDS WHERE JBILLING_TIMESTAMP IS NULL ORDER BY ID", reader.getSqlQueryString());
    }

    public void testBatchRead() throws Exception {
        fillTestDatabase(200); // 2 batches (100 per batch)

        int rowcount = 0;
        for (List<Record> records : reader) {
            rowcount = rowcount + records.size();
        }
        
        assertEquals("200 rows read", 200, rowcount);
    }

    public void testLargeRead() throws Exception {
        fillTestDatabase(150000); // 1500 batches (100 per batch)

        StopWatch read = new StopWatch("read 150000 rows from HSQLDB");
        read.start();
        
        int rowcount = 0;
        for (List<Record> records : reader) {
            rowcount = rowcount + records.size();
        }

        read.stop();
        System.out.println(read.shortSummary());
        
        assertEquals("150000 rows read", 150000, rowcount);
    }

}
