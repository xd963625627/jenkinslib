package org.devops

def AnsibleDeploy(hosts, func) {
  sh "ansible ${func} ${hosts}"
}
