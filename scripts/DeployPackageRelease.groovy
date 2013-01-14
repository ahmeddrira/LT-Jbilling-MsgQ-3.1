/*
 * JBILLING CONFIDENTIAL
 * _____________________
 *
 * [2003] - [2012] Enterprise jBilling Software Ltd.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Enterprise jBilling Software.
 * The intellectual and technical concepts contained
 * herein are proprietary to Enterprise jBilling Software
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden.
 */

includeTargets << grailsScript("War")

includeTargets << new File("${basedir}/scripts/CopyResources.groovy")
includeTargets << new File("${basedir}/scripts/CompileDesigns.groovy")
includeTargets << new File("${basedir}/scripts/CompileReports.groovy")
includeTargets << new File("${basedir}/scripts/CompileRules.groovy")
includeTargets << new File("${basedir}/scripts/Jar.groovy")

resourcesDir = "${basedir}/resources"
descriptorsDir = "${basedir}/descriptors"
configDir = "${basedir}/grails-app/conf"
sqlDir = "${basedir}/sql"
javaDir = "${basedir}/src/java"
targetDir = "${basedir}/target"
// The maven artifact version needs to be updated here
def nexusDeploymentVersion = "3.1.0.RC9"

def repositoryHost = "217.74.234.9"
def repositoryPort = "8081"
def repositoryId = "thirdparty"
def repositoryURL = "http://${repositoryHost}:${repositoryPort}/nexus/content/repositories/${repositoryId}"

releaseName = "${grailsAppName}-enterprise-${nexusDeploymentVersion}"
packageName = "${targetDir}/${releaseName}.zip"
jarName = "${targetDir}/${grailsAppName}.jar"

def deployArgs = "deploy:deploy-file -DgroupId=com.sapienter \
  -Dversion=${nexusDeploymentVersion} \
  -DrepositoryId=${repositoryId} \
  -Durl=http://${repositoryHost}:${repositoryPort}/nexus/content/repositories/${repositoryId}"

def deployJarArgs = "${deployArgs} -DartifactId=${grailsAppName} -Dpackaging=jar -Dfile=${jarName}"
def deployPackageReleaseArgs = "${deployArgs} -DartifactId=${grailsAppName}-package-release -Dpackaging=zip -Dfile=${packageName}"
  

target(prepareRelease: "Builds the war and all necessary resources.") {
    copyResources()
    compileDesigns()
    compileReports()
    compileRules()
    jar()
    war()
}

target(packageRelease: "Builds the war and packages all the necessary config files and resources in a release zip file.") {
    //depends(prepareRelease)

    // ship the data.sql file if it exists, otherwise use jbilling_test.sql
    def testDb = new File("${basedir}/sql/jbilling_test.sql")
    def referenceDb = new File("${basedir}/data.sql")
    File sqlFile = referenceDb.exists() ? referenceDb : testDb

    // zip up resources into a release package
    delete(dir: targetDir, includes: "${grailsAppName}-*.zip")

	// To aid deployment of a 'gui' jbilling and 'msg queue' jbilling Copy the jbilling file to a msg-queue war file
	copy(tofile: "${grailsAppName}-msg-queue.war") {
		fileset(dir: targetDir, includes: "${grailsAppName}.war")
	}
	
    // zip into a timestamped archive for delivery to customers
    zip(filesonly: false, update: false, destfile: packageName) {
        zipfileset(dir: resourcesDir, prefix: "jbilling/resources")
        zipfileset(dir: targetDir, includes: "${grailsAppName}.jar", prefix: "jbilling/resources/api")
        zipfileset(dir: javaDir, includes: "jbilling.properties", fullpath: "jbilling/jbilling.properties")
        zipfileset(dir: configDir, includes: "Config.groovy", fullpath: "jbilling/${grailsAppName}-Config.groovy")
        zipfileset(dir: configDir, includes: "DataSource.groovy", fullpath: "jbilling/${grailsAppName}-DataSource.groovy")
        zipfileset(dir: targetDir, includes: "${grailsAppName}.war")		
        //zipfileset(dir: targetDir, includes: "${grailsAppName}-msg-queue.war")
        zipfileset(file: sqlFile.absolutePath, includes: sqlFile.name)
        zipfileset(dir: sqlDir, includes: "upgrade.sql")
        zipfileset(file: "UPGRADE-NOTES")
    }

    println "Packaged release to ${packageName}"
}

target(deployRelease: "Deploys the package release war to nexus") {
	depends(packageRelease)
	
	System.out.println(" ");
	System.out.println("###############################################################################");
	System.out.println("Deploying Files:");
	System.out.println("   ${jarName}");
	System.out.println("   ${packageName}");
	System.out.println("Version:");
	System.out.println("   ${nexusDeploymentVersion}");
	System.out.println("To mvn Repository:");
	System.out.println("   ${repositoryId} [${repositoryHost}:${repositoryPort}]");
	System.out.println(" ");
	System.out.println("**** Make sure ${repositoryId}'s username and password");
	System.out.println("**** is added to your .m2/settings.xml file");
	System.out.println("###############################################################################");
	System.out.println(" ");

	def isWindows = System.properties.get("os.name").toLowerCase().startsWith("win")

	if (isWindows) {
		exec(executable: "cmd.exe", failOnError:true) {
			arg(line: "/c mvn ${deployJarArgs}")
		}
		exec(executable: "cmd.exe", failOnError:true) {
			arg(line: "/c mvn ${deployPackageReleaseArgs}")
		}
	} else {
		exec(executable: "mvn", outputproperty: "result", failOnError:true) {
			arg(line: "${deployJarArgs}")
		}
		exec(executable: "mvn", outputproperty: "result", failOnError:true) {
			arg(line: "${deployPackageReleaseArgs}")
		}
	}

}

setDefaultTarget(deployRelease)
