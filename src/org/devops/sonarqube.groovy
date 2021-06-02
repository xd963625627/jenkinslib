package org.devops

// scan
def SonarScan(projectName, projectDesc, projectPath) {
  def sonarHome = "/home/software/sonar-scanner"
  def sonarServer = "http://192.168.1.151:9000/"
  def sonarDate = sh returnStdout: true, script: 'date +%Y%m%d%H%M%S'
  sonarDate = sonarDate - "\n"
  sh """
    ${sonarHome}/bin/sonar-scanner -Dsonar.host.url="${sonarServer}" \
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
                  -Dsonar.java.surefire.report=target/surefire-reports
    """
}

// 使用jenkins插件
def SonarScanInJenkins(projectName, projectDesc, projectPath) {
  def qg = waitForQualityGate()
  // 使用jenkins指定的环境 可以不需要配置server、password、login
  withSonarQubeEnv("sonarqube-docker") {
    def sonarHome = "/home/software/sonar-scanner"
    // def sonarServer = "http://192.168.1.151:9000/"
    def sonarDate = sh returnStdout: true, script: 'date +%Y%m%d%H%M%S'
    sonarDate = sonarDate - "\n"
    sh """
      ${sonarHome}/bin/sonar-scanner -Dsonar.projectKey=${projectName} \
                    -Dsonar.projectName=${projectName} \
                    -Dsonar.projectVersion=${sonarDate} \
                    -Dsonar.ws.timeout=30 \
                    -Dsonar.projectDescription=${projectDesc} \
                    -Dsonar.sources=${projectPath} \
                    -Dsonar.sourceEncoding=UTF-8 \
                    -Dsonar.java.binaries=target/classes \
                    -Dsonar.java.test.binaries=target/test-classes \
                    -Dsonar.java.surefire.report=target/surefire-reports
      """
    if (qg.status != 'OK') {
        error "Pipeline aborted due to quality gate failure: ${{qg.status}"
    }
  }
}
