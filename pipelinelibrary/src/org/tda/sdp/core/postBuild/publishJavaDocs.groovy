#!/bin/groovy
package org.tda.sdp.core.postBuild

def execute(String javaDocs) {
  env.javaDocs = "$javaDocs"
  
  withCredentials([usernamePassword(credentialsId: '2b02f382-8b24-41f5-804a-6fb203999491', passwordVariable: 'JENKINS_PASSWORD', usernameVariable: 'JENKINS_USER')]) { 
    sh '''DOC_NAME=$PROJECT-$REPO-$BRANCH_NAME_NEW
    DOCS=`curl -k -u $JENKINS_USER:$JENKINS_PASSWORD -X GET https://confluence-npe.iteclientsys.local/rest/docs/2.0/repository/ | jq -r .categories[].docs[].name`
    jar -cvf "$DOC_NAME"_javadocs.jar -C $build_javaDocs .

	if [[ $DOCS == *$DOC_NAME* ]]; then
  		echo "$DOC_NAME already exists"
  		KEY=`curl -k -u $JENKINS_USER:$JENKINS_PASSWORD -X GET https://confluence-npe.iteclientsys.local/rest/docs/2.0/repository/ | jq -r .categories[].docs | jq -rM "map(select(.name == \\"$DOC_NAME\\")) | .[].id"`
  		curl -k -u $JENKINS_USER:$JENKINS_PASSWORD -X POST --upload-file "$DOC_NAME"_javadocs.jar https://confluence-npe.iteclientsys.local/rest/docs/2.0/repository/c1001-"$KEY" -H "X-Atlassian-Token: no-check"
  	else
  		echo "$DOC_NAME does not exist"
  		curl -k -u $JENKINS_USER:$JENKINS_PASSWORD -X PUT --upload-file "$DOC_NAME"_javadocs.jar https://confluence-npe.iteclientsys.local/rest/docs/2.0/repository/c1001/"$DOC_NAME"
  		KEY=`curl -k -u $JENKINS_USER:$JENKINS_PASSWORD -X GET https://confluence-npe.iteclientsys.local/rest/docs/2.0/repository/ | jq -r .categories[].docs | jq -rM "map(select(.name == \\"$DOC_NAME\\")) | .[].id"`
	fi

	echo "https://confluence-npe.iteclientsys.local/docs/overview.action?path=c1001-$KEY/index.html" > .javaDoc
    '''
  }
}
