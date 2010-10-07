import com.mchange.v2.c3p0.ComboPooledDataSource
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

beans = {
    /*
        Database configuration
     */
    dataSource(ComboPooledDataSource) { bean ->
        bean.destroyMethod = 'close'

        // database connection properties from DataSource.groovy
        user = CH.config.dataSource.username
        password = CH.config.dataSource.password
        driverClass = CH.config.dataSource.driverClassName
        jdbcUrl = CH.config.dataSource.url

        // Connection pooling using c3p0
        acquireIncrement = 2
        initialPoolSize = 5
        maxPoolSize = 20
        maxIdleTime = 300
        minPoolSize = 2
        checkoutTimeout = 10000

        /*
         Periodically test the state of idle connections and validate connections on checkout. Handles
         potential timeouts by the database server. Increase the connection idle test period if you
         have intermittent database connection issues.
        */
        preferredTestQuery = "select id from jbilling_table where id = 1"
        testConnectionOnCheckin = true
        idleConnectionTestPeriod = 30

        /*
         Destroy un-returned connections after a period of time (in seconds) and throw an exception
         that shows who is still holding the un-returned connection. Useful for debugging connection
         leaks.
        */
        // unreturnedConnectionTimeout = 10
        // debugUnreturnedConnectionStackTraces = true
    }

    /*
        Spring security
     */
    authenticationProcessingFilter(com.sapienter.jbilling.client.authentication.CompanyUserAuthenticationFilter) {
        authenticationManager = ref("authenticationManager")
    }

    userDetailsService(com.sapienter.jbilling.client.authentication.CompanyUserDetailsService) {
        springSecurityService = ref("springSecurityService")
    }    

    passwordEncoder(com.sapienter.jbilling.client.authentication.JBillingPasswordEncoder)

    permissionVoter(com.sapienter.jbilling.client.authentication.PermissionVoter)

    webExpressionVoter(com.sapienter.jbilling.client.authentication.SafeWebExpressionVoter) {
        expressionHandler = ref("webExpressionHandler")
    }

    /*
        Remoting
     */
    // HTTP request handler for Spring httpinvoker remote beans
    httpRequestAdapter org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter
}
