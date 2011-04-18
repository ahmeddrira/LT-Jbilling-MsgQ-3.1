includeTargets << grailsScript("Init")

target(reformatText: "Cleans up text files and sets the appropriate eol character.") {
    // replace tab characters with spaces
    // replace UNIX eol characters with DOS characters
    fixcrlf(srcdir: "${basedir}/src", includes: "**/*.java",
            tab: "remove", tablength: "4", javafiles: "yes", eol: "crlf")
}

setDefaultTarget(reformatText)
