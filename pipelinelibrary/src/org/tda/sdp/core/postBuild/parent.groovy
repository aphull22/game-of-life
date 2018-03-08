#!/usr/bin/groovy
package org.tda.sdp.core.postBuild

def execute(String javaDocsPath, String qaFlag, String fortifyScanFlag, String sonarScanFlag) {
  javaDocs = javaDocsPath
  qaTestFlag = qaFlag
  fortifyFlag = fortifyScanFlag
  sonarFlag = sonarScanFlag
  
  publishJavaDocs = new publishJavaDocs()
  qa = new qa()
  sonar = new sonar()
  fortifySched = new fortifySched()
  sdpEmail = new org.tda.sdp.util.sdpEmail() 
  
  if( javaDocs != null ){
   // publishJavaDocs.execute(javaDocs) //publish JavaDocs if path is defined in build.yml
  }
  
  if (sonarFlag != "disabled" || sonarFlag != "false" ){
    stage('SonarQube'){
      sonar.execute() //run sonar scan towaards source
    }
  }
  
  if (fortifyFlag != "disabled" || fortifyFlag != "false" ){ 
    stage('Schedule Fortify'){
      // fortifySched.execute() //trigger the jenkins job that schedules a fortify scan for the repo/branch being built
    }
  }
  
  if ( qaFlag == "enable" ) {
    qa.execute()
  }
  
  sdpEmail.execute() //send email
}
