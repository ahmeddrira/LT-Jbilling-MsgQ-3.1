includeTargets << grailsScript("Init")

target(main: "Compiles jasper report designs.") {

    ant.taskdef( name: "jrc", classname: "net.sf.jasperreports.ant.JRAntCompileTask" )

    delete( dir: "./resources/reports" )
    mkdir( dir: "./resources/reports" )

    mkdir( dir: "./tmp" )
    jrc( destdir: "./resources/reports", tempdir: "./tmp", keepjava: "true", xmlvalidation: "true" ) {
        src {
            fileset( dir: "./descriptors/reports", includes: "**/*.jrxml" )
        }
        classpath {
            path( refid: compileClasspath )
        }
    }
    delete( dir: "./tmp")
}

setDefaultTarget(main)
