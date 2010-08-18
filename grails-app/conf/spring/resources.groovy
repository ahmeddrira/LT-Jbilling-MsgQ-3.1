import com.mchange.v2.c3p0.ComboPooledDataSource
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

// Place your Spring DSL code here
beans = {
    dataSource(ComboPooledDataSource) { bean ->
    bean.destroyMethod = 'close'
    // use grails' datasource configuration for connection user, password, driver and JDBC url
    user = CH.config.dataSource.username
    password = CH.config.dataSource.password
    driverClass = CH.config.dataSource.driverClassName
    jdbcUrl = CH.config.dataSource.url
    // Pool settings
    acquireIncrement = 2
    initialPoolSize = 5
    maxPoolSize = 20
    maxIdleTime = 300
    minPoolSize = 2
    checkoutTimeout = 10000
    // The next three properties deal with potential time outs by the DB server of idle connections. If this
    // happens regardless you can either reduce the seconds of testing (30 is the default)
    // or activate the property testConnectionOnCheckout
    preferredTestQuery = "select id from jbilling_table where id = 1"
    testConnectionOnCheckin = true
    idleConnectionTestPeriod = 30
    // for debug of leaks only: after ten seconds, throw an exception that tells who is holding
    //    a connection
    // unreturnedConnectionTimeout" 10"/>
    // debugUnreturnedConnectionStackTraces" true"/>
   }

}
