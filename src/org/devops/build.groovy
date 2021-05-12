package org.devops
// 构建类型
def Build(buildType, buildShell) {
  def buildTools = ["maven":"maven3.6.3", "ant":"ANT", "gradle": "GRADLE", "npm": "node"]
  buildHome = tool buildTools[buildType]
  println('buildHome is',buildHome)
  sh "${buildHome}/bin/${buildType} ${buildShell}"
}
