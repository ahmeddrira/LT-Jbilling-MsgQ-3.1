
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

/* 
 * Script deploys the jbilling jar to a maven repository as a maven artifactId
 */
includeTargets << grailsScript("Jar")

final targetDir = "${basedir}/target"

def repositoryHost = "buildserver.tpspay.com"
//def repositoryPort = "8081"
def repositoryId = "thirdparty"
def repositoryURL = "http://${repositoryHost}/nexus/content/repositories/${repositoryId}"

// The maven artifact version needs to be updated here
def mvnJarVersion = "3.1.0.RC10"

def deployArgs = "deploy:deploy-file -DgroupId=com.sapienter \
  -DartifactId=${grailsAppName} \
  -Dversion=${mvnJarVersion} \
  -Dpackaging=jar \
  -Dfile=${targetDir}/${grailsAppName}.jar \
  -DrepositoryId=${repositoryId} \
  -Durl=${repositoryURL}"

target(install: "Deploys the jbilling jar to a maven repo") {
	depends(jar)

	System.out.println(" ");
	System.out.println("###############################################################################");
	System.out.println("Deploying:");
	System.out.println("   ${targetDir}/${grailsAppName}.jar");
	System.out.println("   Version ${mvnJarVersion}");
	System.out.println("To mvn Repository:");
	System.out.println("   ${repositoryId} [${repositoryHost}]");
	System.out.println(" ");
	System.out.println("**** Make sure ${repositoryId}'s username and password");
	System.out.println("**** is added to your .m2/settings.xml file");
	System.out.println("###############################################################################");
	System.out.println(" ");

	def isWindows = System.properties.get("os.name").toLowerCase().startsWith("win")

	System.out.println("mvn ${deployArgs}")
	if (isWindows) {
		exec(executable: "cmd.exe", failOnError:true) {
			arg(line: "/c mvn ${deployArgs}")
		}
	} else {
		exec(executable: "mvn", outputproperty: "result", failOnError:true) {
			arg(line: "${deployArgs}")
		}
	}

	//println result;
}

setDefaultTarget(install)
