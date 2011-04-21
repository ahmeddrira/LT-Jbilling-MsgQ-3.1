includeTargets << grailsScript("Init")

includeTargets << new File("${basedir}/scripts/PackageRelease.groovy")

imageDir = "${basedir}/image"

sourcePackageName = "${targetDir}/${releaseName}-src.zip"

target(cleanPackages: "Remove old packages from the target directory.") {
    delete(dir: targetDir, includes: "${grailsAppName}-*.zip")
}

target(packageSource: "Packages the source code.") {
    zip(filesonly: false, update: false, destfile: sourcePackageName) {
        zipfileset(dir: basedir, prefix: grailsAppName) {
            exclude(name: "activemq-data/")
            exclude(name: "logs/")
            exclude(name: "tmp/")
            exclude(name: "image/")

            exclude(name: "resources/")
            exclude(name: "classes/")
            exclude(name: "target/")
            exclude(name: "out/")

            exclude(name: ".ant-targets*.xml")
            exclude(name: "**/TEST*.xml")
            exclude(name: "**/*.log*")
            exclude(name: "**/*.swp")
            exclude(name: "*~")
            exclude(name: "**/*.iml")
            exclude(name: ".idea/")
            exclude(name: ".settings/")
            exclude(name: "**/jbilling.properties")

            exclude(name: "**/.git/")
            exclude(name: ".gitignore")
            exclude(name: ".gitattributes")
        }

        zipfileset(file: "${javaDir}/jbilling.properties.sample", fullpath: "${releaseName}/src/java/jbilling.properties")
    }
}

target(checkImage: "Checks that a previous release of jBilling exists to use as an image for the new release.") {
    if (!new File(imageDir).exists()) {
        println "\nBuild failed:"
        println "${imageDir} does not exist."
        println "Cannot build a release package without an ./image directory containing the jbilling release image."
        exit(1)
    }
}

target(updateImage: "Updates the jbilling image with the current release artifacts.") {
    checkImage()
    copyResources()
    compileDesigns()
    compileReports()
    generateChangelog()
    war()

    mkdir(dir: "${imageDir}/resources")

    // copy reports
    delete(dir: "${imageDir}/resources/reports", includes: "**/*")
    copy(todir: "${imageDir}/resources/reports") {
        fileset(dir: "${resourcesDir}/reports")
    }

    // copy invoice designs
    delete(dir: "${imageDir}/resources/designs", includes: "**/*")
    copy(todir: "${imageDir}/resources/designs") {
        fileset(dir: "${resourcesDir}/designs")
    }

    // copy logos
    delete(dir: "${imageDir}/resources/logos", includes: "**/*")
    copy(todir: "${imageDir}/resources/logos") {
        fileset(dir: "${resourcesDir}/logos")
    }

    // copy mediation descriptors and sample asterisk files
    copy(todir: "${imageDir}/resources/mediation", overwrite: true) {
        fileset(dir: "${resourcesDir}/mediation", includes: "asterisk.xml")
        fileset(dir: "${resourcesDir}/mediation", includes: "asterisk-sample*.csv")
        fileset(dir: "${resourcesDir}/mediation", includes: "jbilling_cdr.*")
        fileset(dir: "${resourcesDir}/mediation", includes: "mediation.dtd")
    }

    // copy configuration files
    // don't copy DataSource, the reference tomcat install uses HSQLDB
    copy(file: "${javaDir}/jbilling.properties.sample", tofile: "${imageDir}/conf/jbilling.properties", overwrite: true)
    copy(file: "${configDir}/Config.groovy", tofile: "${imageDir}/conf/${grailsAppName}-Config.groovy", overwrite: true)

    // copy log4j configuration
    mkdir(dir: "${imageDir}/bin/grails-app/conf")
    copy(file: "${configDir}/log4j.xml", todir: "${imageDir}/bin/grails-app/conf", overwrite: true)

    // copy jbilling.war
    copy(file: "${targetDir}/${grailsAppName}.war", todir: "${imageDir}/webapps", overwrite: true)
}


target(packageTomcat: "Builds and packages the binary jbilling tomcat release.") {
    updateImage()

    zip(filesonly: false, update: false, destfile: packageName) {
        zipfileset(dir: imageDir, prefix: grailsAppName) {
            exclude(name: "webapps/jbilling/")

            exclude(name: "work/")
            exclude(name: "temp/")

            exclude(name: "**/logs/")
            exclude(name: "**/activemq-data/")
            exclude(name: "**/*.log")
        }
    }
}

target(packagePublicRelease: "Builds the public binary jbilling tomcat release, and the jbilling source release packages. ") {
    cleanPackages()
    packageSource()
    packageTomcat()
}

setDefaultTarget(packagePublicRelease)
