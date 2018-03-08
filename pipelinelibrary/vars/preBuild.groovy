#!/usr/bin/groovy
def call(body) {
  // evaluate the body block, and collect configuration into the object
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
  
  def preBuild = new org.tda.sdp.core.preBuild.parent()
  
  stage('preBuild'){
    preBuild.execute(config.addEmail, config.dailySchedule, config.sdpEnv)
  }

}
