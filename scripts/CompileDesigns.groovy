includeTargets << grailsScript("Init")

final tempDir = "${basedir}/tmp"
final resourcesDir = "${basedir}/resources"
final descriptorsDir = "${basedir}/descriptors"

target(compileDesigns: "Compiles jasper paper invoice designs.") {
    ant.taskdef(name: "jrc", classname: "net.sf.jasperreports.ant.JRAntCompileTask")

    delete(dir: "${resourcesDir}/designs")
    mkdir(dir: "${resourcesDir}/designs")

    mkdir(dir: tempDir)
    jrc(destdir: "${resourcesDir}/designs", tempdir: tempDir, keepjava: "true", xmlvalidation: "true") {
        src {
            fileset(dir: "${descriptorsDir}/designs", includes: "**/*.jrxml")
        }
        classpath {
            path(refid: runtimeClasspath)
        }
    }
    delete(dir: tempDir)
}

setDefaultTarget(compileDesigns)
