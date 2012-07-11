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

target(upgradeDb: "Upgrades database to the latest version") {


    // optionally accept user defined arguments:
    // user - jbilling database user : default: jbilling
    // password - jbilling database user password : default: ""
    // db - jbilling database table only for postgres Db, for other databases use dbUrl parameter : default : jbilling_test
    // dbUrl - jbilling database url : default : jdbc:postgresql://localhost:5432/${database}
    // dbDriver - jbilling database driver : default: org.postgresql.Driver
    // dbClasspath - jbilling database driver classpath default : lib/postgresql-8.4-702.jdbc4.jar

    // version - jbilling branch version: default: 3.2
    // testDb - if set upgrades test database : default : false
    // changeLogFile - path to the upgrade change log file : default : "descriptors/database/jbilling-upgrade-${version}.xml"

    parseArguments();

    def username = argsMap.user ? argsMap.user : "jbilling"
    def database = argsMap.db ? argsMap.db : "jbilling_test"
    def dbUrl = argsMap.dbUrl ? argsMap.dbUrl : "jdbc:postgresql://localhost:5432/${database}"
    def driver = argsMap.dbDriver ? argsMap.dbDriver : "org.postgresql.Driver"
    def classpath = argsMap.dbClasspath ? argsMap.dbClasspath : "lib/postgresql-8.4-702.jdbc4.jar"

    def version = argsMap.version ? argsMap.version : "3.1"
    def testDb = argsMap.testDb ? argsMap.testDb : false
    def changeLogFile = argsMap.changeLogFile ? argsMap.changeLogFile : "descriptors/database/jbilling-upgrade-${version}.xml"

    condition(property: "liquibaseExecutable", value: "liquidbase-2.0.5/liquibase.bat")
    {
        os("family": "windows")
    }

    condition(property: "liquibaseExecutable", value: "liquidbase-2.0.5/liquibase")
    {
        os("family": "unix")
    }

    def jbillingUpgrade="--driver=${driver} --classpath=${classpath} --changeLogFile=${changeLogFile} --url=${dbUrl} --username=${username} --password= "

    println "Upgrading jbilling database schema to the latest version ${version}"
    // call liquibase to upgrade DB to the latest version
    exec(executable: "${liquibaseExecutable}", failonerror: false) {
        arg(line: "${jbillingUpgrade} --contexts=base update")
    }
    println "Done"

    if (testDb) {
        println "Upgrading test data to the latest version: ${version}"
        // call liquibase to upgrade test data
        exec(executable: "${liquibaseExecutable}", failonerror: false) {
            arg(line: "${jbillingUpgrade} --contexts=test update")
        }
        println "Done"
    }
}

setDefaultTarget(upgradeDb)
