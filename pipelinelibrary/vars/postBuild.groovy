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
  
  def postBuild = new org.tda.sdp.core.postBuild.parent()
  postBuild.execute(config.javaDocs, config.qa, config.fortifyFlag, config.sonarFlag)

}
