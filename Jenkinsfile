#!groovy
@Library('jenkinslib') _
def tools = new org.devops.tools

String workspace = "/opt/jenkins/workspace"

pipeline {
    agent {
        node {
            label "master"
            customWorkspace "${workspace}"
        }
    }
    parameters {
        string(name: "PERSON", defaultValue: "JXD", description: "Who are you?")
    }
    stages{
        stage('SayHello') {
            input {
                message "Should we continue"
                ok "Yes, we should"
                submitter "xd1998"
            }
            steps {
                echo "Hello ${PERSON}"
            }
        }
        stage("PullCode") { // 阶段名称
            steps { // 步骤
                timeout(time:5, unit:'MINUTES') { // 指定步骤的超时时间
                    script { // 指定运行的脚本
                        println("获取代码")
                    }
                }
            }
        }
        stage("Build") { // 阶段名称
            steps { // 步骤
                timeout(time:20, unit:'MINUTES') { // 指定步骤的超时时间
                    script { // 指定运行的脚本
                        println("应用打包")
                    }
                }
            }
        }
        stage("CodeScan") { // 阶段名称
            when {
                environment name: "PERSON",
                value: 'JXD'
            }
            steps { // 步骤
                timeout(time:30, unit:'MINUTES') { // 指定步骤的超时时间
                    script { // 指定运行的脚本
                        println("代码扫描")
                        tools.PrintMes('this is mylib')
                    }
                }
            }
        }
    }
}
