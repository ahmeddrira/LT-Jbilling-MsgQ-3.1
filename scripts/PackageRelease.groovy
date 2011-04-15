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

final packageFileName = "${grailsAppName}-${grailsAppVersion}.zip"

target(packageRelease: "Packages a war file with the necessary config files and resources for release.") {
    depends(war, copyResources, compileDesigns, compileReports, compileRules)

    delete(dir: targetDir, includes: packageFileName)

    zip(filesonly: false, update: false, destfile: "${targetDir}/${packageFileName}") {
        zipfileset(dir: resourcesDir, prefix: "resources")
        zipfileset(dir: javaDir, includes: "jbilling.properties")
        zipfileset(dir: configDir, includes: "Config.groovy", fullpath: "${grailsAppName}-Config.groovy")
        zipfileset(dir: configDir, includes: "DataSource.groovy", fullpath: "${grailsAppName}-DataSource.groovy")
        zipfileset(dir: targetDir, includes: "${grailsAppName}.war")
        zipfileset(dir: sqlDir, includes: "jbilling_test.sql")
    }
}

setDefaultTarget(packageRelease)
