#!/usr/bin/groovy
package org.tda.sdp.core.preBuild

def buildPrep() { //Declare project properties
  env.PROJECT = sh(script:"cat .git/config | grep url | awk -F '/' '{print \$5}'", returnStdout: true).replaceAll("\r", "").replaceAll("\n", "").toUpperCase()
  env.REPO = sh(script:"cat .git/config | grep url | awk -F '/' '{print \$6}' | awk -F '.' '{print \$1}'", returnStdout:true).replaceAll("\r", "").replaceAll("\n", "")
  env.BRANCH_NAME_CLEAN = sh (script: "echo $BRANCH_NAME | sed -e 's%/%_%g'", returnStdout:true).replaceAll("\r", "").replaceAll("\n", "")
  if(env.subsystem) {
    env.subsystem = env.subsystem.replaceAll("\\s+", " ").replaceAll(", ", ",").trim()
  }
}

def deployPrep() {
  env.subsystem_uppercase = env.app_subsystem.toUpperCase()
  env.XL_ORCHESTRATOR = "sequential-by-container"
  env.ART_NAMESPACE = "sdp-snapshots-local/$PROJECT/$REPO/$BRANCH_NAME_CLEAN/$system/$subsystem/$BUILD_NUMBER"
}
