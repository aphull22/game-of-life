#!/bin/groovy
package org.tda.sdp.util

def execute() {
  getVersion = new getVersion()
  getVersion.execute()
  print VERSION
  sh '''

#!/bin/bash
mkdir -p $WORKSPACE/binaries-$app_subsystem

BODY="<?xml version=\\"1.0\\" encoding=\\"UTF-8\\"?><udm.DeploymentPackage version="\\"$VERSION\\"" application="\\"$app_system/$app_subsystem\\""><application /><orchestrator><value>$XL_ORCHESTRATOR</value></orchestrator><deployables><!--new_deployable--></deployables> </udm.DeploymentPackage>"
echo $BODY > $WORKSPACE/deployit-manifest.xml

function zipandmanifest {
        DEPLOYABLE=$1
        SCAN=$2
        BINARY=$(basename $DEPLOYABLE)
        PARENT_DIR=`dirname $DEPLOYABLE`
        echo "deployable is $DEPLOYABLE"
        echo "parent dir is $PARENT_DIR"

if   [ -d $PARENT_DIR ] && [ $PARENT_DIR != "." ]; then
        echo  "Parent Dir exists = $PARENT_DIR"
        BINARY_NAME=$BINARY
        if   [ -d "$WORKSPACE/${DEPLOYABLE}" ]; then
                echo "$WORKSPACE/${DEPLOYABLE} is a directory"
            cd $(dirname $DEPLOYABLE) && zip -r $WORKSPACE/binaries-$app_subsystem/$BINARY_NAME.zip $BINARY_NAME && cd -;
        elif [ -f "$WORKSPACE/${DEPLOYABLE}" ]; then
            echo "$WORKSPACE/${DEPLOYABLE} is a file"
            zip -jr $WORKSPACE/binaries-$app_subsystem/$BINARY_NAME.zip $DEPLOYABLE;
        else echo "${DEPLOYABLE} is not valid";
        fi

fi

if   [ ! -d $PARENT_DIR ] || [ $PARENT_DIR = "."  ]; then
        echo "Parent Dir does not exist"
        BINARY_NAME=$(cd $WORKSPACE && echo $BINARY)
        if   [ -d "$WORKSPACE/${BINARY_NAME}" ]; then
            echo "$WORKSPACE/${BINARY_NAME} is a directory"
            cd $WORKSPACE && zip -r $WORKSPACE/binaries-$app_subsystem/$BINARY_NAME.zip $BINARY_NAME && cd -;
        elif [ -f "$WORKSPACE/${BINARY_NAME}" ]; then
            echo "$WORKSPACE/${BINARY_NAME} is a file"
            cd $WORKSPACE &&  zip -jr $WORKSPACE/binaries-$app_subsystem/$BINARY_NAME.zip $BINARY_NAME && cd -;
        else echo "${BINARY} is not valid";
        fi
fi

curl -v -k -X PUT "$ART_URL/$ART_NAMESPACE/$BINARY_NAME.zip" -T $WORKSPACE/binaries-$app_subsystem/$BINARY_NAME.zip | jq -r .uri > $WORKSPACE/binaries-$app_subsystem/.$BINARY_NAME
ARTIFACT_URL=$(cat $WORKSPACE/binaries-$app_subsystem/.$BINARY_NAME)
if [ -n "$token_replacement_scanning_file_extensions" ] && [ "$SCAN" == "true" ]; then # Set textFileNamesRegex 
     sed -i -e \'s%<!--new_deployable-->%\'"<tda.generic.unix.Application name="\\""$BINARY_NAME"\\""><tags><value>"$subsystem_uppercase"</value></tags><scanPlaceholders>$SCAN</scanPlaceholders><textFileNamesRegex>.+\\.($token_replacement_scanning_file_extensions ameritrade | cfg | conf | config | ini | properties | props | txt | asp | aspx | htm | html | jsf | jsp | xht | xhtml | sql | xml | xsd | xsl | xslt)</textFileNamesRegex><fileUri>"$ARTIFACT_URL"</fileUri></tda.generic.unix.Application><!--new_deployable-->"\'%g\' $WORKSPACE/deployit-manifest.xml
else      
     sed -i -e \'s%<!--new_deployable-->%\'"<tda.generic.unix.Application name="\\""$BINARY_NAME"\\""><tags><value>"$subsystem_uppercase"</value></tags><scanPlaceholders>$SCAN</scanPlaceholders><fileUri>"$ARTIFACT_URL"</fileUri></tda.generic.unix.Application><!--new_deployable-->"\'%g\' $WORKSPACE/deployit-manifest.xml
fi  
}

if [[ -n $build_artifacts ]]; then
        for i in $build_artifacts; do
                echo $i
                zipandmanifest $i false
        done
fi

if [[ -n $deploy_configs ]]; then
        for i in $deploy_configs; do
                echo $i
                zipandmanifest $i true
        done
fi
'''
}
