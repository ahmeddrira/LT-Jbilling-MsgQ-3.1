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

    // load the client reference DB if it exists
    def testDb = new File("${basedir}/sql/jbilling_test.sql")
    def referenceDb = new File("${basedir}/data.sql")
    def file = referenceDb.exists() ? referenceDb : testDb

    // optionally accept database name and user name arguments
    parseArguments();
    def cleanDb = argsMap.cleanDb;
    def username = argsMap.user ? argsMap.user : "jbilling"
    def database = argsMap.db ? argsMap.db : "jbilling_test"

    if (cleanDb) {
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
    }

    println "Importing file test database into the ${database} database (user: ${username})"
    // call liquibase to load the database base schema
    exec(executable: "lb-schema.bat", failonerror: false) {
        arg(line: "--contexts=base update")
    }

    println "Importing data to database: ${database}."
    // call liquibase to load the database data
    exec(executable: "liquidbase-2.0.5/liquibase.bat", failonerror: false) {
        arg(line: "--driver=org.postgresql.Driver --classpath=lib/postgresql-8.4-702.jdbc4.jar --changeLogFile=descriptors/database/jbilling-test_data.xml --url=\"jdbc:postgresql://localhost:5432/jbilling_test\" --username=jbilling update")
    }

    println "Importing FK to database: ${database}."
    // call liquibase to load the database foreign keys
    exec(executable: "lb-fk.bat", failonerror: false) {
        arg(line: "--contexts=FKs update")
    }

    println "Done."
}

setDefaultTarget(prepareTestDb)
