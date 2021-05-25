package org.devops

// scan
def SonarScan(projectName, projectDesc, projectPath) {
  def sonarServer = "http://192.168.1.151:9000/"
  def sonarDate = sh returnStdout: true, script: 'date +%Y%m%d%H%M%S'
  sonarDate = sonarDate - "\n"
  sh """
    sonar-scanner -Dsonar.host.url="${sonarServer}" \
                  -Dsonar.projectKey=${projectName} \
                  -Dsonar.login=admin \
                  -Dsonar.password=123456 \
                  -Dsonar.projectName=${projectName} \
                  -Dsonar.projectVersion=${sonarDate} \
                  -Dsonar.ws.timeout=30 \
                  -Dsonar.projectDescription=${projectDesc} \
                  -Dsonar.sources=${projectPath} \
                  -Dsonar.sourceEncoding=UTF-8 \
                  -Dsonar.java.binaries=target/classes \
                  -Dsonar.java.test.binaries=target/test-classes \
                  -Dsonar.java.surefire.report=target/surefire-reports  "
                  #-Dsonar.branch.name=${CI_COMMIT_REF_NAME} -X "
    """
}
