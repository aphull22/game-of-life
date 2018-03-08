#!/usr/bin/groovy
package org.tda.sdp.core.postBuild

def execute() {
//  build job: 'Developer-Services/Check_Fortify', parameters: [string(name: 'REPO_URL', value: 'test'), string(name: 'BRANCH', value: 'test2'), string(name: 'EMAIL', value: 'test3')], wait: false
  withCredentials([usernamePassword(credentialsId: '2b02f382-8b24-41f5-804a-6fb203999491', passwordVariable: 'JENKINS_PASSWORD', usernameVariable: 'JENKINS_USER')]) {
      sh '''
      		echo "Scheduling a Fortify Scan for your branch tonight at around 11PM EST"
        	curl -u $JENKINS_USER:$JENKINS_PASSWORD -X POST http://jenkins.associatesys.local/job/Developer-Services/job/Check_Fortify/build?delay=0sec --data-urlencode json=\'{"parameter": [{"name":"REPO_URL", "value":"https://bitbucket.associatesys.local/scm/'$PROJECT_LOWERCASE'/'$REPO'.git"},{"name":"BRANCH", "value":"'$BRANCH_NAME_NEW'"}]}\'
  			echo "Your Fortify Job link will be http://jenkins.associatesys.local/job/Fortify/job/$PROJECT_LOWERCASE/job/$REPO/job/$BRANCH_NAME_NEW" >> .tar
         '''
  }
}
