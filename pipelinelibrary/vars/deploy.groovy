#!/usr/bin/groovy
def call(body) {
  // evaluate the body block, and collect configuration into the object
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
  
  if (env.cancelBuild == "enable"){
    return
  }
  
  def sdpDeploy = new org.tda.sdp.core.deploy.parent()
  sdpDeploy.runDeploy(config.enable, config.branchPattern, config.env, config.dictionary, config.sdpEnv)
  
}
