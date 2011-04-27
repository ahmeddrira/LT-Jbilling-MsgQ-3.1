includeTargets << grailsScript("Init")

includeTargets << new File("${basedir}/scripts/PackageRelease.groovy")
includeTargets << new File("${basedir}/scripts/Jar.groovy")

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

        zipfileset(file: "${javaDir}/jbilling.properties.sample", fullpath: "${grailsAppName}/src/java/jbilling.properties")
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
    jar()
    war()

    def jbillingHome = "${imageDir}/jbilling/"

    mkdir(dir: "${jbillingHome}/resources")

    // copy reports
    delete(dir: "${jbillingHome}/resources/reports", includes: "**/*")
    copy(todir: "${jbillingHome}/resources/reports") {
        fileset(dir: "${resourcesDir}/reports")
    }

    // copy invoice designs
    delete(dir: "${jbillingHome}/resources/designs", includes: "**/*")
    copy(todir: "${jbillingHome}/resources/designs") {
        fileset(dir: "${resourcesDir}/designs")
    }

    // copy logos
    delete(dir: "${jbillingHome}/resources/logos", includes: "**/*")
    copy(todir: "${jbillingHome}/resources/logos") {
        fileset(dir: "${resourcesDir}/logos")
    }

    // copy mediation descriptors and sample asterisk files
    copy(todir: "${jbillingHome}/resources/mediation", overwrite: true) {
        fileset(dir: "${resourcesDir}/mediation", includes: "asterisk.xml")
        fileset(dir: "${resourcesDir}/mediation", includes: "asterisk-sample*.csv")
        fileset(dir: "${resourcesDir}/mediation", includes: "jbilling_cdr.*")
        fileset(dir: "${resourcesDir}/mediation", includes: "mediation.dtd")
    }

    // copy jbilling.jar
    delete(file: "${jbillingHome}/resources/api")
    mkdir(dir: "${jbillingHome}/resources/api")
    copy(file: "${targetDir}/${grailsAppName}.jar", todir: "${jbillingHome}/resources/api")

    // copy configuration files
    // don't copy DataSource, the reference tomcat install uses HSQLDB
    copy(file: "${javaDir}/jbilling.properties.sample", tofile: "${jbillingHome}/jbilling.properties", overwrite: true)
    copy(file: "${configDir}/Config.groovy", tofile: "${jbillingHome}/${grailsAppName}-Config.groovy", overwrite: true)

    // copy log4j configuration
    mkdir(dir: "${imageDir}/bin/grails-app/conf")
    copy(file: "${configDir}/log4j.xml", todir: "${imageDir}/bin/grails-app/conf", overwrite: true)

    // copy jbilling.war
    delete(file: "${imageDir}/webapps/${grailsAppName}.war")
    copy(file: "${targetDir}/${grailsAppName}.war", todir: "${imageDir}/webapps")
}


target(packageTomcat: "Builds and packages the binary jbilling tomcat release.") {
    updateImage()

    // clear tomcat temp and work directories
    delete(dir: "${imageDir}/temp")
    mkdir(dir: "${imageDir}/temp")

    delete(dir: "${imageDir}/work")
    mkdir(dir: "${imageDir}/work")

    // zip tomcat image
    zip(filesonly: false, update: false, destfile: packageName) {
        zipfileset(dir: imageDir, prefix: grailsAppName) {
            exclude(name: "webapps/jbilling/")
            exclude(name: "webapps/drools-guvnor/")

            exclude(name: "**/logs/")
            exclude(name: "**/activemq-data/")
            exclude(name: "**/*.log")
        }
    }
}

target(packagePublicRelease: "Builds the public binary jbilling tomcat release, and the jbilling source release packages. ") {
    switch(args) {
        case "-update":
            println "Updating image ..."
            updateImage()
            break

        default:
            println "Building release packages ..."
            cleanPackages()
            packageSource()
            packageTomcat()
    }
}

setDefaultTarget(packagePublicRelease)
