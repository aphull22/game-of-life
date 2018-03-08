#!/bin/groovy
package org.tda.sdp.util

def execute(String taskName){ //Runs task for each subsystem
  
  xl = new org.tda.sdp.core.deploy.xl()
  projProperties = new org.tda.sdp.core.preBuild.projProperties()
  envProperties = new org.tda.sdp.core.preBuild.envProperties()
  createTar = new createTar()
  
  task = "$taskName"
  
  if (env.tokenReplaceEXTs) { //when token_replacement_scanning is specified in build.yml
    env.token_replacement_scanning_file_extensions = env.tokenReplaceEXTs.replaceAll("\\s+", " ").replaceAll(", ", ",").replaceAll(" ", "|").replaceAll(",", "|").trim() + "|"
  }
  
  for (i in env.subsystem.split(",| ")) {
    //reinitialize
    env.app_subsystem = "$i"
    env.deploy_configs = ""
    env.build_artifacts = ""
    
    if (env."${i}_binaries" != null ) {
      env.build_artifacts = env."${i}_binaries"
    }
    if (env."${i}_configs" != null ) {
      env.deploy_configs = env."${i}_configs"
    }
    
    env.subsystem_uppercase = "$i".toUpperCase() // add to env properties?
    env.app_system = env.system.toUpperCase() // add to env properties?
    
    projProperties.deployPrep() // need to redeclare env Properties , to inject single subsystem to ART_NAMESPACE 
    
    if ( task == "createTar" ) { 
      createTar.execute()
    }
    else {
      xl.execute("$task")
    }
  }
}
