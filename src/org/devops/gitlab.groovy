package org.devops
// 封装http请求
def HttpReq(gitServer,reqType,reqUrl,reqBody){
    //def gitServer = "http://192.168.3.134:17000/api/v4"
    withCredentials([string(credentialsId: 'gitlab-token', variable: 'gitlabToken')]) {
      result = httpRequest customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: "${gitlabToken}"]], 
                httpMode: reqType, 
                contentType: "APPLICATION_JSON",
                consoleLogResponseBody: true,
                ignoreSslErrors: true, 
                requestBody: reqBody,
                url: "${gitServer}/${reqUrl}"
                //quiet: true
    }
    return result
}

// 更改提交状态
def ChangeCommitStatus(gitServer,projectId,commitSha,status){
    commitApi = "projects/${projectId}/statuses/${commitSha}?state=${status}"
    response = HttpReq(gitServer,'POST',commitApi,'')
    println(response)
    return response
}
