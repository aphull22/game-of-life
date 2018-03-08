#!/usr/bin/groovy
package org.tda.sdp.core.build

def execute(def artifactoryPublishFlag=true) { 
  sh '$npmBuildGoals'
}
