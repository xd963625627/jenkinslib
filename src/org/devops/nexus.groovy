package org.devops
//获取POM中的坐标
def GetGav(){
    //上传制品
    def jarName = sh returnStdout: true, script: "cd target;ls *.jar"
    env.jarName = jarName - "\n"
    
    def pom = readMavenPom file: 'pom.xml'
    env.pomVersion = "${pom.version}"
    env.pomArtifact = "${pom.artifactId}"
    env.pomPackaging = "${pom.packaging}"
    env.pomGroupId = "${pom.groupId}"
    
    println("${pomGroupId}-${pomArtifact}-${pomVersion}-${pomPackaging}")

    return ["${pomGroupId}","${pomArtifact}","${pomVersion}","${pomPackaging}"]
}
// 使用Jenkins中的Nexus插件上传
def NexusUpload() {
     //use nexus plugin
    nexusArtifactUploader artifacts: [[artifactId: "${pomArtifact}", 
                                        classifier: '', 
                                        file: "${filePath}", 
                                        type: "${pomPackaging}"]], 
                            credentialsId: 'nexus', 
                            groupId: "${pomGroupId}", 
                            nexusUrl: '192.168.1.151:8081', 
                            nexusVersion: 'nexus3', 
                            protocol: 'http', 
                            repository: "${repoName}", 
                            version: "${pomVersion}"
}
// 使用Maven命令上传
def MavenUpload() {
    def mvnHome = tool "M2"
    sh  """ 
        cd target/
        ${mvnHome}/bin/mvn deploy:deploy-file -Dmaven.test.skip=true  \
                                -Dfile=${jarName} -DgroupId=${pomGroupId} \
                                -DartifactId=${pomArtifact} -Dversion=${pomVersion}  \
                                -Dpackaging=${pomPackaging} -DrepositoryId=maven-hostd \
                                -Durl=http://192.168.1.151:8081/repository/maven-hostd 
        """
}
// 定义main方法
def main(uploadType){
    GetGav()
    if ("${uploadType}" == "maven"){
        MavenUpload()
    } else if ("${uploadType}" == "nexus") {
        env.repoName = "maven-hostd"
        env.filePath = "target/${jarName}"
        NexusUpload()
    }
}
