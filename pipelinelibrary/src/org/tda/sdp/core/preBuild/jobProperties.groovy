#!/usr/bin/groovy
package org.tda.sdp.core.preBuild

def declare() {
  properties([ //job property declaration
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '15', numToKeepStr: '10')),
    disableConcurrentBuilds(),
    addParameters(),
    addSchedule(),
  ])
}

public void addParameters(){
  def buildTypeParameters = [] //create array for paramaters
  def addParameters = disableConcurrentBuilds()
  def addTimerParameters = disableConcurrentBuilds()
 
  if(env.addTimer){
    buildTypeParameters.add([
    $class: 'hudson.model.BooleanParameterDefinition',
    name: "FORCE_BUILD",
    default: false,
    description:"Set to true, if you want to force this build despite no changes made."
    ])
    addParameters = parameters(buildTypeParameters)
  }
  
  if( env.type == "npm" || "maven" ) {
    buildTypeParameters.add([
    $class: 'hudson.model.BooleanParameterDefinition',
    name: ""+env.type.toUpperCase()+"_RELEASE",
    default: false,
    description:"Set to true, if this is a "+env.type.toUpperCase()+" release build."
  ])
    addParameters = parameters(buildTypeParameters)
  }
  return addParameters
}

public void addSchedule() {
  def addSchedule =  pipelineTriggers([]) //add daily cronJob if hour is defined in Jenkinsfile
  if(env.addTimer){
    addSchedule = pipelineTriggers([cron('H '+addTimer+' * * * ')])
  }
  return addSchedule
}
