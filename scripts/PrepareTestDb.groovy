includeTargets << grailsScript("Init")

target(prepareTestDb: "Import the test postgresql database.") {
    println "Importing the 'jbilling_test' database."

    exec(executable: "psql", failonerror: false) {
        arg(line: "-U jbilling -f ${basedir}/sql/jbilling_test.sql jbilling_test")
    }
}

setDefaultTarget(prepareTestDb)
