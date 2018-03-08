#!/bin/groovy
package org.tda.sdp.util

def execute() {
  getVersion = new getVersion()
  getVersion.execute()
  
  if (env.artifactoryPublishFlag == "true") {
    if (env.type != "docker"){
      createTar()
    }
    env.TAR_URL = sh(script: "tail -1 .tar", returnStdout: true)
  }
}

def createTar() {
 sh '''
  #!/bin/bash
  COMMIT_ID=$(git rev-parse --short HEAD)
  TAR_FILE="$app_subsystem-$COMMIT_ID-$VERSION.tar"
  mkdir binaries-"$app_subsystem"
  
  cp build.yml binaries-"$app_subsystem"/.
  
  if [[ -n $deploy_configs ]]; then 
  	for i in $deploy_configs; do
  		cp -r $i binaries-"$app_subsystem"/.
	done
  fi
  
  if [[ -n $deploy_dictionary ]]; then
	for i in $deploy_dictionary; do
		cp -r $i binaries-"$app_subsystem"/.
	done  
  fi
  
  if [[ -n $build_artifacts ]]; then 
	for i in $build_artifacts; do
		cp -r $i binaries-"$app_subsystem"/.
	done
  fi
  
  tar -cvf $TAR_FILE -C binaries-"$app_subsystem" .
  #curl -u zjenkins:AKCp5Z2XwUgRfC3ByTqEkCzqYWNE3cwm2ncDCGTLq5chCaj6M6QnuVLGVPxdXe1cZTCvMVc3w -k -X PUT "https://artifactory-dev.associatesys.local/artifactory/sdp-snapshots-local/$PROJECT/$REPO/$BRANCH_NAME_NEW/$app_subsystem/$BUILD_NUMBER/$TAR_FILE" -T $TAR_FILE | jq -r .uri >> $WORKSPACE/.tar
  curl -u zsdpjenkins:AKCp5Z329XpybWQJ7HcnoUZJ8jdPiX3KbdtgUVvGdsdUBQEXTajAx2E8NYDx6whtHC3AJScum -k -X PUT "$ART_URL/$ART_NAMESPACE/$TAR_FILE" -T $TAR_FILE | jq -r .uri >> $WORKSPACE/.tar
  ''' 
}
