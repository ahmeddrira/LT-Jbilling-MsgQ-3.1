includeTargets << new File("${basedir}/scripts/CopyResources.groovy")
includeTargets << new File("${basedir}/scripts/CompileDesigns.groovy")
includeTargets << new File("${basedir}/scripts/CompileReports.groovy")
includeTargets << new File("${basedir}/scripts/CompileRules.groovy")
includeTargets << new File("${basedir}/scripts/PrepareTestDb.groovy")

target(prepareTest: "Prepares the testing environment, compiling all necessary resources and loading the test database.") {
    copyResources()
    compileDesigns()
    compileReports()
    compileRules()
    prepareTestDb()

    println "Environment prepared for test run. Start grails with './run-app' and run 'ant test'."
}

setDefaultTarget(prepareTest)

