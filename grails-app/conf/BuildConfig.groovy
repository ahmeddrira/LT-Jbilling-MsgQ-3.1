grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.war.file = "target/${appName}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
    }

    // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    log "warn"

    // repositories for dependency resolution
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
        mavenRepo "http://snapshots.repository.codehaus.org"
        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.org/nexus/content/groups/public-jboss/"
    }

    // dependencies under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes
    dependencies {

        compile('org.springmodules:spring-modules-cache:0.8') {
            transitive = false
        }

        compile 'org.apache.activemq:activemq-all:5.3.2'
        compile('org.apache.activemq:activemq-pool:5.3.2') {
            excludes 'junit', 'commons-logging', 'log4j'
        }

        compile 'org.drools:drools-core:5.0.1'
        compile 'org.drools:drools-compiler:5.0.1'

        compile 'quartz:quartz:1.4.5'

        compile 'joda-time:joda-time:1.6.2'

        compile 'commons-httpclient:commons-httpclient:3.0.1'
        compile 'commons-net:commons-net:2.0'

        compile 'org.hibernate:hibernate-validator:4.1.0.Final'
        compile 'javax.validation:validation-api:1.0.0.GA'

        compile('org.apache.xmlrpc:xmlrpc-client:3.1') {
            excludes 'xml-apis'
        }

        compile 'org.codehaus.jackson:jackson-core-asl:1.7.4'
        compile 'org.codehaus.jackson:jackson-mapper-asl:1.7.4'

        compile 'org.apache.velocity:velocity:1.6.2'
        compile('org.apache.velocity:velocity-tools:2.0') {
            excludes 'struts-core', 'struts-taglib', 'struts-tiles'
        }

        compile('net.sf.barcode4j:barcode4j:2.0') {
            excludes 'xerces', 'xalan', 'xml-apis'
        }

        compile('net.sf.jasperreports:jasperreports:4.0.0') {
            excludes 'jaxen', 'xalan', 'xml-apis'
        }
        compile 'net.sf.jasperreports:jasperreports-fonts:4.0.0'
        compile 'net.sourceforge.jexcelapi:jxl:2.6.10'

        compile 'c3p0:c3p0:0.9.1.2'

        runtime 'postgresql:postgresql:8.4-702.jdbc4'
    }
}
