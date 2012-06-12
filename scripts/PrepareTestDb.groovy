/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

includeTargets << grailsScript("Init")

target(prepareTestDb: "Import the test postgresql database.") {

    // optionally accept user defined arguments:
    // user - jbilling database username : default: jbilling
    // db - jbilling database table only for postgres Db, for other databases use dbUrl parameter : default : jbilling_test
    // dbUrl - jbilling database url : default : jdbc:postgresql://localhost:5432/${database}
    // dbDriver - jbilling database driver : default: org.postgresql.Driver
    // dbClasspath - jbilling database driver classpath default : lib/postgresql-8.4-702.jdbc4.jar

    // version - jbilling branch version: default: 3.2
    // skipTest - if skipTest is true create init database; otherwise load test database : default : false

    parseArguments();
    def username = argsMap.user ? argsMap.user : "jbilling"
    def database = argsMap.db ? argsMap.db : "jbilling_test"
    def dbUrl = argsMap.dbUrl ? argsMap.dbUrl : "jdbc:postgresql://localhost:5432/${database}"
    def driver = argsMap.dbDriver ? argsMap.dbDriver : "org.postgresql.Driver"
    def classpath = argsMap.dbClasspath ? argsMap.dbClasspath : "lib/postgresql-8.4-702.jdbc4.jar"

    def version = argsMap.version ? argsMap.version : "3.1"
    def skipTest = argsMap.skipTest ? argsMap.skipTest : false

    condition(property: "liquibaseExecutable", value: "liquidbase-2.0.5/liquibase.bat")
    {
        os("family": "windows")
    }

    condition(property: "liquibaseExecutable", value: "liquidbase-2.0.5/liquibase")
    {
        os("family": "unix")
    }

    println "Using liquibase: ${liquibaseExecutable}"

    def jbillingSchema="--driver=${driver} --classpath=${classpath} --changeLogFile=descriptors/database/jbilling-schema.xml --url=${dbUrl} --username=${username} --password= "
    def jbillingInit="--driver=${driver} --classpath=${classpath} --changeLogFile=descriptors/database/jbilling-init_data.xml --url=${dbUrl} --username=${username} --password= "
    def jbillingTest="--driver=${driver} --classpath=${classpath} --changeLogFile=descriptors/database/jbilling-test_data.xml --url=${dbUrl} --username=${username} --password= "
    def jbillingUpgrade="--driver=${driver} --classpath=${classpath} --changeLogFile=descriptors/database/jbilling-upgrade-${version}.xml --url=${dbUrl} --username=${username} --password= "


    println "Dropping a database: ${database}..."
    // call postgresl to drop database
    exec(executable: "dropdb", failonerror: false) {
        arg(line: "-U ${username} -e ${database}")
    }
    println "Done."

    println "Creating a database: ${database}..."
    // call postgresl to create database
    exec(executable: "createdb", failonerror: true) {
        arg(line: "-U ${username} -O ${username} -E UTF-8 -e ${database}")
    }
    println "Done."

    println "Loading jbilling database schema"
    // call liquibase to load the database base schema
    exec(executable: "${liquibaseExecutable}", failonerror: false) {
        arg(line: "${jbillingSchema} --contexts=base update")
    }
    println "Done."

    if (skipTest) {

        println "Initializing jbilling database: ${database}"
        // skip test db load init db data
        exec(executable: "${liquibaseExecutable}", failonerror: false) {
            arg(line: "${jbillingInit}  update")
        }
        println "Done."

    } else {

        println "Loading test data into database: ${database}"
        // call liquibase to load test db data
        exec(executable: "${liquibaseExecutable}", failonerror: false) {
            arg(line: "${jbillingTest}  update")
        }
        println "Done."
    }

    println "Settings database foreign keys"
    // call liquibase to load the database foreign keys
    exec(executable: "${liquibaseExecutable}", failonerror: false) {
        arg(line: "${jbillingSchema} --contexts=FKs update")
    }
    println "Done"

    println "Upgrading jbilling database schema to the latest version ${version}"
    // call liquibase to upgrade DB to the latest version
    exec(executable: "${liquibaseExecutable}", failonerror: false) {
        arg(line: "${jbillingUpgrade} --contexts=base update")
    }
    println "Done"

    if (!skipTest) {
        println "Upgrading test data to the latest version: ${version}"
        // call liquibase to upgrade test data
        exec(executable: "${liquibaseExecutable}", failonerror: false) {
            arg(line: "${jbillingUpgrade} --contexts=test update")
        }
        println "Done"
    }

    println "Done."
}

setDefaultTarget(prepareTestDb)
