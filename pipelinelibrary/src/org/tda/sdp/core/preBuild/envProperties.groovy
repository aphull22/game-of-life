#!/usr/bin/groovy
package org.tda.sdp.core.preBuild

def declare() {
  endpointVariables()
}

def endpointVariables() { //need to call in parseForSubsystem b/c subsystem is defined in $ART_NAMESPACE
  //endpoint variables
  env.ART_URL = "https://artifactory.associatesys.local/artifactory" //create_TAR() and zip() call this endpoint
  env.xlEnv = 'prod_sdp_xld' //xl calls this endpoint
  env.CONFLUENCE_URL = "https://confluence-npe.iteclientsys.local" // javaDocs calls this endpoints
  //SONAR - configured in MANAGE JENKINS
  //FORTIFY - configured in FORTIFY SCRIPT
  
  if(env.sdpEnv){ 
    if (env.sdpEnv == "DEV") { //internal use - reinitialize endpoints to point to DEV
      env.xlEnv = 'dev_sdp_xld'
      env.XLD_URL = "http://devctlvsdpxld01.pteassociatesys.local:4516/deployit/repository/ci"
      env.ART_URL = "https://artifactory-dev.associatesys.local/artifactory"
      //add variable to point dictionary scripts to dev XL
    } 
  }
}
