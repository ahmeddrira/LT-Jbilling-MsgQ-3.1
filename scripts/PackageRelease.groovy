includeTargets << grailsScript("War")

includeTargets << new File("${basedir}/scripts/CopyResources.groovy")
includeTargets << new File("${basedir}/scripts/CompileDesigns.groovy")
includeTargets << new File("${basedir}/scripts/CompileReports.groovy")
includeTargets << new File("${basedir}/scripts/CompileRules.groovy")

resourcesDir = "${basedir}/resources"
configDir = "${basedir}/grails-app/conf"
sqlDir = "${basedir}/sql"
javaDir = "${basedir}/src/java"
targetDir = "${basedir}/target"

releaseName = "${grailsAppName}-${grailsAppVersion}"
packageName = "${targetDir}/${releaseName}.zip"

target(prepareRelease: "Builds the war and all necessary resources.") {
    copyResources()
    compileDesigns()
    compileReports()
    compileRules()
    war()
}

target(packageRelease: "Builds the war and packages all the necessary config files and resources in a release zip file.") {
    depends(prepareRelease)

    // zip up resources into a release package
    delete(dir: targetDir, includes: "${grailsAppName}-*.zip")

    zip(filesonly: false, update: false, destfile: packageName) {
        zipfileset(dir: resourcesDir, prefix: "resources")
        zipfileset(dir: javaDir, includes: "jbilling.properties.sample", fullpath: "conf/jbilling.properties")
        zipfileset(dir: configDir, includes: "Config.groovy", fullpath: "conf/${grailsAppName}-Config.groovy")
        zipfileset(dir: configDir, includes: "DataSource.groovy", fullpath: "conf/${grailsAppName}-DataSource.groovy")
        zipfileset(dir: targetDir, includes: "${grailsAppName}.war")
        zipfileset(dir: sqlDir, includes: "jbilling_test.sql")
    }

    println "Packaged release to ${packageName}"
}

setDefaultTarget(packageRelease)
