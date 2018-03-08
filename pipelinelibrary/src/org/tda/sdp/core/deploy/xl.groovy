#!/usr/bin/groovy
package org.tda.sdp.core.deploy

def execute(String taskName){
  task = "$taskName"
  zipManifest = new org.tda.sdp.util.zipManifest()
  zipManifest.execute()
  if ( task == "deployExecute" ) {
    deployExecute()
  }
  if ( task == "pipelineExecute" ) {
    pipelineExecute()
  }
}

def deployExecute() {
  createPackage()
  publishPackage()
  deployPackage()
}

def pipelineExecute() {
  createPackage()
  publishPackage()
  pipeline()
}

def createPackage() {
  xldCreatePackage artifactsPath: '', darPath: '$PROJECT-$REPO-$BRANCH_NAME_NEW-$app_subsystem-$BUILD_NUMBER.dar', manifestPath: 'deployit-manifest.xml'
}

def publishPackage() {
  xldPublishPackage darPath: '$PROJECT-$REPO-$BRANCH_NAME_NEW-$app_subsystem-$BUILD_NUMBER.dar', serverCredentials: ''+xlEnv+''
  dictionariesLoad()
}

def deployPackage() {
  deployEnv = env.deploy_env + "/" + env.system + "-" + env.deploy_env
  xldDeploy environmentId: 'Environments/'+deployEnv+'', packageId: 'Applications/'+app_system+'/'+app_subsystem+'/'+VERSION+'', serverCredentials: ''+xlEnv+''
}

def pipeline() {
  xlrCreateRelease releaseTitle: '$app_subsystem-$build_version-${BUILD_NUMBER}_coordinator', serverCredentials: 'PROD_XLR', template: 'E2E Pipeline Coordinator V2', variables: [[propertyName: 'PROJECT', propertyValue: '$pipeline_project'], [propertyName: 'APPLICATION', propertyValue: '$app_system'], [propertyName: 'SUBSYS', propertyValue: '$app_subsystem'], [propertyName: 'VERSION_NUMBER', propertyValue: '$build_version-$BUILD_NUMBER'], [propertyName: 'NON_PROD_ENVS', propertyValue: '$pipeline_nonprod_env'], [propertyName: 'PROD_ENV_ORDER', propertyValue: '$pipeline_prod_env'], [propertyName: 'URL', propertyValue: '$TAR_URL']]	
}

def dictionariesLoad() {
  withCredentials([usernamePassword(credentialsId: '702fadf9-29f4-4adb-9380-4c7c6ba04208', passwordVariable: 'xld_password', usernameVariable: 'xld_user')]) {
    sh 'echo "Checking for dictionaries"'
    //sh 'pipeline/dictionaries.sh' 
  }
}
