#!/usr/bin/groovy
package org.tda.sdp.core.preBuild

def execute() {
  previousBuild = sh (script : 'curl --silent $JOB_URL/lastBuild/buildNumber', returnStdout: true).trim()
  //echo "previousBuild was ${previousBuild}"
  previousBuildNumber = previousBuild as Integer
  previousBuildNumber -= 1
  //echo "previousBuildNumber was ${previousBuildNumber}"
  previousBuildTime=sh (script: "curl --silent $JOB_URL/${previousBuildNumber}/buildTimestamp?format=yyyyMMddHHmmss", returnStdout: true).trim()
  echo "code last built timestamp: ${previousBuildTime}"
  recentCommitTime = sh (script: 'git show -s --format=%ci  | sed \'s/-//g\' | sed \'s/://g\' | awk \'{print $1$2}\'', returnStdout: true).trim()
  echo "most recent commit timestamp: ${recentCommitTime}"
  
  if (recentCommitTime < previousBuildTime) { 
    echo "no new commit since last build, not re-building/deploying code that hasn't changed"
    if ( env.FORCE_BUILD != "true") {
      env.cancelBuild = "enable"
      currentBuild.displayName = "$BUILD_NUMBER-NOT_BUILT"
      //currentBuild.result = 'NOT_BUILT'
    }
  }
}
