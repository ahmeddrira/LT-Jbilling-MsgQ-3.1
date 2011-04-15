includeTargets << grailsScript("Init")

final tempDir = "${basedir}/tmp"
final resourcesDir = "${basedir}/resources"
final descriptorsDir = "${basedir}/descriptors"

target(compileReports: "Compiles jasper report designs.") {
    ant.taskdef(name: "jrc", classname: "net.sf.jasperreports.ant.JRAntCompileTask" )

    delete(dir: "${resourcesDir}/reports")
    mkdir(dir: "${resourcesDir}/reports")

    mkdir(dir: tempDir)
    jrc(destdir: "${resourcesDir}/reports", tempdir: tempDir, keepjava: "true", xmlvalidation: "true") {
        src {
            fileset(dir: "${descriptorsDir}/reports", includes: "**/*.jrxml")
        }
        classpath {
            path(refid: compileClasspath)
        }
    }
    delete(dir: tempDir)
}

setDefaultTarget(compileReports)
