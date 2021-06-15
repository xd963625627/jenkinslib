package org.devops
// 封装http请求
def HttpReq(sonarServer,reqType,reqUrl,reqBody){
    //def gitServer = "http://192.168.3.134:17000/api/v4"
    withCredentials([string(credentialsId: 'gitlab-token', variable: 'gitlabToken')]) {
      result = httpRequest authentication: 'c8cfe653-c5fa-4b30-b071-4c47c2a3e8d6',
                httpMode: reqType, 
                contentType: "APPLICATION_JSON",
                consoleLogResponseBody: true,
                ignoreSslErrors: true, 
                requestBody: reqBody,
                url: "${sonarServer}/${reqUrl}"
                //quiet: true
    }
    return result
}

// 获取Sonar质量阈状态
def GetProjectStatus(sonarServer, projectName) {
  apiUrl = "project_branches/list?project=${projectName}"
  response = HttpReq(sonarServer,'GET',apiUrl,'')
  response = readJSON text: """${response.content}"""
  println(response)
  return response
}
