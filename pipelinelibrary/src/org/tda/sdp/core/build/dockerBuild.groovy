#!/usr/bin/groovy
package org.tda.sdp.core.build

def execute(def artifactoryPublishFlag=true) { 
  sh ''' 
  REF_IMAGE=`cat Dockerfile | grep FROM | awk -F \' \' \'{print $2}\'`
  docker pull $REF_IMAGE 
  '''
  buildImage = docker.build(''+env.dockerImageName+'')
  
  if(artifactoryPublishFlag == "true"){
    docker.withRegistry("https://artifactory.associatesys.local:6555") {
      buildImage.push('latest')
    }
  }  
}
