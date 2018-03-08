#!/usr/bin/groovy
package org.tda.sdp.core.build

def execute(def artifactoryPublishFlag=true) { 
  
  def server = Artifactory.server "CT-ART01" //Declare Maven Configuration
  def rtMaven = Artifactory.newMavenBuild()
  rtMaven.tool = "Maven3" // Tool name from Jenkins configuration
  rtMaven.deployer releaseRepo:''+env.mavenReleaseRepo+'', snapshotRepo:''+env.mavenSnapshotRepo+'', server: server
  rtMaven.resolver releaseRepo:'libs-release', snapshotRepo:'libs-snapshot', server: server 
  
  if ( artifactoryPublishFlag == "false" ) {  // Disable publishing artifacts if declared
    rtMaven.deployer.deployArtifacts = false
  }
  
  if ( env.MAVEN_RELEASE == "true" ) { //IF RELEASE, RUN RELEASE GOALS AND APPEND "MAVEN_RELEASED" TO DISPLAY NAME
    def buildInfo =  rtMaven.run pom: ''+env.mavenBuildFile+'', goals: ''+env.mavenReleaseGoals+''
    server.publishBuildInfo buildInfo
    currentBuild.displayName = "$BUILD_NUMBER-MAVEN_RELEASED"
  }
  else {
    def buildInfo =  rtMaven.run pom: ''+env.mavenBuildFile+'', goals: ''+env.mavenBuildGoals+''
    server.publishBuildInfo buildInfo
    currentBuild.displayName = "$BUILD_NUMBER"
  }
}
