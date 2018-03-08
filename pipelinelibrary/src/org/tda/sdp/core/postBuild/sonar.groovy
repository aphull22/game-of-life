#!/usr/bin/groovy
package org.tda.sdp.core.postBuild

def execute() {
  try {
    withSonarQubeEnv('PROD-SONAR') {
      if ( env.type == "maven" ) {
        sh 'mvn -Dsonar.branch=$BRANCH_NAME_CLEAN sonar:sonar -f $mavenBuildFile'
      }    
      if ( env.type == "gradle" ) {
        //sh 'gradle sonarqube -Dsonar.branch=$BRANCH_NAME_NEW -Dsonar.host.url=https://sonar.associatesys.local -Dsonar.verbose=true -x test -b $build_buildFile'
        sh 'chmod -R 755 services/*'
        sh 'services/gradlew --info sonarqube'
      }
    }
  } catch (err){
    print err 
    print "SonarQube Stage failed but will not fail your job"
  }
}
