#!/usr/bin/groovy
package org.tda.sdp.core.build

def execute(def artifactoryPublishFlag=true) {
  def server = Artifactory.server "CT-ART01" //Declare Gradle Configuration
  def rtGradle = Artifactory.newGradleBuild() 
  rtGradle.tool = "Docker_Gradle" // Tool name from Jenkins configuration
  rtGradle.deployer repo:''+env.gradleDeployRepo+'', server: server
  rtGradle.resolver repo:'libs-release', server: server
  
  if ( artifactoryPublishFlag == "false" ) {  // Disable publishing artifacts if declared
    rtGradle.deployer.deployArtifacts = false
  }
  
  def buildInfo = rtGradle.run rootDir: "", buildFile: ''+env.gradleFile+'', tasks: ''+env.gradleBuildGoals+''
  server.publishBuildInfo buildInfo
}
