package com.sapienter.jbilling.server.mediation.cache;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sapienter.jbilling.server.mediation.cache.IFinder;
import com.sapienter.jbilling.server.mediation.cache.ILoader;
import com.sapienter.jbilling.server.mediation.cache.PricingFinder;

public class MediationPerformanceTest extends TestCase {

    private static final Integer ENTITY_ID = 1;

    private static final ApplicationContext spring = new ClassPathXmlApplicationContext(
            new String[] { "/jbilling-caching.xml" });

    private ILoader loader = null;
    private IFinder ifinder = null;

    public MediationPerformanceTest() {
    }

    public MediationPerformanceTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        loader = (ILoader) spring.getBean("basicLoader");
        ifinder = (IFinder) spring.getBean("pricingFinder");
        System.out.println("JUnit setUp() complete.");
    }

    public void testLoader() {
        System.out.println("testLoader");
        assertEquals("rules_table", loader.getTableName());

        try {
            Connection connection = getConnection();
            String query = "select count(*) as REC_COUNT from "
                    + loader.getTableName();

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            result.next();
            Long recordsCount = result.getLong("REC_COUNT");
            System.out.println("Loader loaded " + recordsCount + " records.");
            assertTrue(
                    "Loader successfully populated records in the database..",
                    recordsCount > 0);
            assertEquals("Loaded correct number of records", new Long(1769),
                    new Long(recordsCount));
            result.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Ending loader tests.");
    }

    public void testFinder() {
        PricingFinder finder = (PricingFinder) ifinder;
        BigDecimal val = finder.getPriceForDestination("5215585888");
        System.out.println("Value returnd as best match= " + val);
        assertTrue("Finder returned a value greater than zero", val
                .compareTo(new BigDecimal("0")) > 0);
        assertEquals("Found the right value", new BigDecimal("0.175"), val);

        val = finder.getPriceForDestination("9699999888");
        System.out.println("Value returnd as best match= " + val);
        assertTrue("Finder returned a value greater than zero", val
                .compareTo(new BigDecimal("0")) > 0);
        assertEquals("Found the right value", new BigDecimal("0.990"), val);

        val = finder.getPriceForDestination("7400000000");
        System.out.println("Value returnd as best match= " + val);
        assertTrue("Finder returned a value greater than zero", val
                .compareTo(new BigDecimal("0")) > 0);
        assertEquals("Found the right value", new BigDecimal("0.093"), val);

        System.out.println("Ending finder tests.");
    }

    private Connection getConnection() throws SQLException,
            ClassNotFoundException {
        // String driver = "org.hsqldb.jdbcDriver";//
        // String url = "jdbc:hsqldb:mem:cacheDB";
        // String username = "sa";
        // String password = "";
        // create connection
        // Class.forName(driver); // load driver
        DataSource ds = (DataSource) spring.getBean("memoryDataSource");
        return ds.getConnection();
        // return DriverManager.getConnection(url, username, password);
    }

    public static void assertEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, BigDecimal expected,
            BigDecimal actual) {
        assertEquals(message, (Object) (expected == null ? null : expected
                .setScale(2, RoundingMode.HALF_UP)),
                (Object) (actual == null ? null : actual.setScale(2,
                        RoundingMode.HALF_UP)));
    }
}
