includeTargets << grailsScript("Init")

final resourcesDir = "${basedir}/resources"
final descriptorsDir = "${basedir}/descriptors"

target(cleanResources: "Removes the existing jbilling resources directory.") {
    delete(dir: "${resourcesDir}")
}

target(createStructure: "Creates the jbilling resources directory structure.") {
    ant.sequential {
        mkdir(dir: "${resourcesDir}")
        mkdir(dir: "${resourcesDir}/designs")
        mkdir(dir: "${resourcesDir}/invoices")
        mkdir(dir: "${resourcesDir}/logos")
        mkdir(dir: "${resourcesDir}/mediation")
        mkdir(dir: "${resourcesDir}/mediation/errors")
        mkdir(dir: "${resourcesDir}/reports")
        mkdir(dir: "${resourcesDir}/rules")
    }
}

target(copyResources: "Creates the jbilling 'resources/' directories and copies necessary files.") {
    depends(cleanResources, createStructure)

    // copy default company logos
    copy(todir: "${resourcesDir}/logos") {
        fileset(dir: "${descriptorsDir}/logos")
    }

    // copy default mediation files
    copy(todir: "${resourcesDir}/mediation") {
        fileset(dir: "${descriptorsDir}/mediation", includes: "mediation.dtd")
        fileset(dir: "${descriptorsDir}/mediation", includes: "asterisk.xml")
    }

    // preserve empty directories when zipping
    touch(file: "${resourcesDir}/mediation/errors/emptyfile.txt")
    touch(file: "${resourcesDir}/rules/emptyfile.txt")
}

setDefaultTarget(copyResources)
