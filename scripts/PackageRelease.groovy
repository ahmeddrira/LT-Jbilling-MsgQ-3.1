includeTargets << grailsScript("War")

includeTargets << new File("${basedir}/scripts/CopyResources.groovy")
includeTargets << new File("${basedir}/scripts/CompileDesigns.groovy")
includeTargets << new File("${basedir}/scripts/CompileReports.groovy")
includeTargets << new File("${basedir}/scripts/CompileRules.groovy")

final resourcesDir = "${basedir}/resources"
final configDir = "${basedir}/grails-app/conf"
final sqlDir = "${basedir}/sql"
final javaDir = "${basedir}/src/java"
final targetDir = "${basedir}/target"

target(packageRelease: "Builds the war and packages all the necessary config files and resources in a release zip file.") {
    // build all resources
    copyResources()
    compileDesigns()
    compileReports()
    compileRules()
    war()

    // zip up resources into a release package
    delete(dir: targetDir, includes: "${grailsAppName}-*.jar")

    zip(filesonly: false, update: false, destfile: "${targetDir}/${grailsAppName}-${grailsAppVersion}.zip") {
        zipfileset(dir: resourcesDir, prefix: "resources")
        zipfileset(dir: javaDir, includes: "jbilling.properties.sample", fullpath: "jbilling.properties")
        zipfileset(dir: configDir, includes: "Config.groovy", fullpath: "${grailsAppName}-Config.groovy")
        zipfileset(dir: configDir, includes: "DataSource.groovy", fullpath: "${grailsAppName}-DataSource.groovy")
        zipfileset(dir: targetDir, includes: "${grailsAppName}.war")
        zipfileset(dir: sqlDir, includes: "jbilling_test.sql")
    }
}

setDefaultTarget(packageRelease)
