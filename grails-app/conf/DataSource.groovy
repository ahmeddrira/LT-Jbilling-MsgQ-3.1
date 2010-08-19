import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration

dataSource {
    /*
     * Database connection settings *

     Choose your dialect:
        org.hibernate.dialect.HSQLDialect
        org.hibernate.dialect.MySQLDialect
        org.hibernate.dialect.Oracle9Dialect
    */
    dialect = "org.hibernate.dialect.PostgreSQLDialect"
    driverClassName = "org.postgresql.Driver"
    username = "jbilling"
    password = ""
    url = "jdbc:postgresql://localhost:5432/jbilling_test"

    // other database settings, these should not be changed.
    pooled = true
    configClass = GrailsAnnotationConfiguration.class
    dbCreate = "none"
    // the connection pool parameters of the data source configuration is in resources.groovy
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
/*
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop','update'
            url = "jdbc:hsqldb:mem:devDB"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:file:prodDb;shutdown=true"
        }
    }
}
*/