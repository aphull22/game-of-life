#!/usr/bin/groovy
package org.tda.sdp.core.deploy

def runDeploy(String sdpDeployFlag,String branchPattern,String targetEnv,String dictionaryPath,String sdpEnv) {
  deployFlag = false //set deploy to false by default
  userDeployFlag = sdpDeployFlag
  env.deploy_env = targetEnv
  env.dictionary = dictionaryPath
  env.sdpENV = sdpEnv
  
  sdpEmail = new org.tda.sdp.util.sdpEmail()
  parseSubsystems = new org.tda.sdp.util.parseSubsystems()
  
  if ( userDeployFlag == "true" ) { //enable deploy if flag is enabled
    deployFlag = true
  }
  
  if ( branchPattern != null) { 
    deployBranchPattern = branchPattern
    if (BRANCH_NAME.startsWith(''+deployBranchPattern+'')) { 
      deployFlag = true } else { //enable deploy if BRANCH_NAME matches branch pattern deploy condition 
      print "BRANCH NAME: $BRANCH_NAME does not match your deploy branch pattern defined , skipping deploy stage."
      deployFlag = false //disable deploy if BRANCH_NAME does not match branch pattern deploy condition
    }
  }
  
  if ( env.type == "docker" ) {
    print "This appears to be a docker build , deployment can not be enabled. Skipping"
    return
  }
  
  if (deployFlag == true) {
    try {
      if (env.pipeline) {
        echo "pipeline is enabled"
        parseSubsystems.execute("pipelineExecute")
      } else if (deploy_env != null ) {
        echo "pipeline is not enabled, proceeding with AUTO deploy to DEV"
        parseSubsystems.execute("deployExecute")
      }
    } catch (err){
      currentBuild.result = "FAILURE"
      sdpEmail.execute()
      throw err
    }
  }
}
