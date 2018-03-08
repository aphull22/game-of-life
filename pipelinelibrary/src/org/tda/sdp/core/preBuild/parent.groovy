#!/usr/bin/groovy
package org.tda.sdp.core.preBuild

def execute(String address, String scheduler, String sdpEnvironment) {
  if ( address != null ) { 
    env.addEmail = address 
  }
  
  if ( scheduler != null ) {
    env.addTimer = scheduler
  }
  
  if ( sdpEnvironment != null ) {
    env.sdpEnv = sdpEnvironment
  }
  
  clone = new clone()
  envProperties = new envProperties()
  jobProperties = new jobProperties()
  projProperties = new projProperties()
  preBuildCommitCheck = new preBuildCommitCheck()
  
  sdpEmail = new org.tda.sdp.util.sdpEmail()
  
  try{
    deleteDir() //clear workspace
    clone.versionControl() //clones Branch on commit
    projProperties.buildPrep() //Sets proj level properties (ex. BRANCH_NAME/PROJECT_NAME)
    jobProperties.declare() //Sets SDP Standard Properties (ex. disable concurrent builds)
    envProperties.declare() //Sets Integration Properties (ex. ART/XLD/XLR/Fortify)
  } catch (err){
    currentBuild.result = "FAILURE"
    sdpEmail.execute()
    throw err
  }
  
  if(env.addTimer){
  	stage ('Check for changes before building'){
	    preBuildCommitCheck.execute() //Checks if the commit has changed for scheduled jobs to prevent duplicate builds/deploys
  	}
  }
}
