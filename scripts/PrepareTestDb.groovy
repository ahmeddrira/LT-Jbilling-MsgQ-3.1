/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
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

    println "Importing file '${file.name}' into the ${database} database (user: ${username})"
    // call postgresl to load the database
    exec(executable: "psql", failonerror: false) {
        arg(line: "-U ${username} -f ${file.path} ${database}")
    }

    println "Done."
}

setDefaultTarget(prepareTestDb)
