#!/usr/bin/groovy
def call(body) {
  // evaluate the body block, and collect configuration into the object
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
    
  def build = new org.tda.sdp.core.build.parent()
  def checkBinaryConfig = new org.tda.sdp.util.checkBinaryConfig()
  
  if (env.cancelBuild == "enable"){ //SDP-431 : exit 0 if scheduler is enabled and no changes are found 
    return
  }
  
  //check for binaries and configs defined for each subsystem as runBuild parameters and declare as env variables
  for (i in env.subsystem.split(",| ")) { 
    checkBinaryConfig.execute(i, config."${i}_binaries", config."${i}_configs")
  }
  
  //execute the build
  stage(''+env.type+''){
   build.execute(config.artifactoryPublish, config.preScript, config.postScript) //builds source and runs preShell/postShell if defined
  }
}
