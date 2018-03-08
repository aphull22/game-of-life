#!/usr/bin/groovy
package org.tda.sdp.core.build

def execute(String buildPublishFlag, String preScript, String postScript) {
  env.artifactoryPublishFlag = true // artifactory Publish set to true by default
  publishFlag = buildPublishFlag
  preBuildScript = preScript
  postBuildScript = postScript
  
  sdpEmail = new org.tda.sdp.util.sdpEmail()
  publishJunit = new org.tda.sdp.util.publishJunit()
  parseSubsystems = new org.tda.sdp.util.parseSubsystems()
  runScript = new org.tda.sdp.util.runScript()
  loadBuildType() //declare buildType
  
  if ( publishFlag == "disabled" || publishFlag == "false" ) { // disable artifactory publishing if disabled via artifactoryPublish option
    env.artifactoryPublishFlag = false
  }
  
  try{
    runScript.execute(preBuildScript) //preScript execution
    try{
      buildType.execute(artifactoryPublishFlag)
    } finally {
      publishJunit.execute()
    }
    runScript.execute(postBuildScript) //postScript execution
    if (env.type != "docker") {
      parseSubsystems.execute("createTar")
    }
  } catch (err){
      currentBuild.result = "FAILURE"
      sdpEmail.execute()
      throw err
  }
}

def loadBuildType() { //declare buildType
  if ( env.type == "maven" ) {
    buildType = new mavenBuild()
  }
  if ( env.type == "gradle" ) {
    buildType = new gradleBuild()
  }
  if ( env.type == "npm" ) { 
    buildType  = new npmBuild()
  } 
  if ( env.type == "docker" ) {  
    buildType = new dockerBuild()
  }
}
