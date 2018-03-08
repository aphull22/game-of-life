# TDA SDP Jenkins Global Pipeline Library

As more and more projects are being adopted to our SDP Jenkins, common patterns are emerging. 
SDP Engineering team is maintaining a pipeline library, defining these common steps as groovy methods. 
This library is automatically available to be called in your Jenkinsfile.

Release Notes and detailed definitions can be found in Confluence:  [SDPJenkins-Confluence](https://confluence.associatesys.local/display/SDP/Global+Pipeline+Library "SDPJenkins-Confluence")
Details for Jenkinsfile can be found in Confluence: [Jenkinsfile](https://confluence.associatesys.local/display/SDP/Jenkinsfile "Jenkinsfile")
List of Docker Images available to utilize for your builds: [SDP Build Images](https://confluence.associatesys.local/display/SDP/SDP+Jenkins+Build+Images "SDP Build Images")
Features or Issues/Concerns can be requested via JIRA: [SDP-JIRA](https://jira-ct.associatesys.local/secure/RapidBoard.jspa?rapidView=2509&projectKey=SDPB&view=planning.nodetail "SDP-JIRA")

### TDA Generic Jenkinsfile
```
env.system = $SYSTEM
env.subsystem = $SUBSYSTEM1 $SUBSYSTEM2
env.type = "$BUILD_TYPE" (ex. maven/npm/gradle/docker)

<< MANDATORY BUILD PARAMATERS >>

node('$DOCKER_IMAGE') {
  
  preBuild{
    addEmail = "$email Addresses" // Optional - Jenkins will include email address defined here to your job notifications
    dailySchedule = "$MILITARY_HOUR" //Optional - schedules a run at the hour defined
  }
  

  runBuild{
    preScript = "$PATH_TO_SCRIPT or $COMMANDS" //Optional - provide command or path to script in your repo to be executed before build
    postScript = "$PATH_TO_SCRIPT" or "$COMMANDS"  //Optional - provide command or path to script in your repo to be executed after build
    artifactoryPublish = "false" or "disabled" //Optional - disables publishing your artifact to artifactory (enabled by default)
    $SUBSYTEM1_binaries = "$PATH_TO_BINARY" //Optional - BINARIES FOR one of your SUBSYSTEMS (subsystem defined on top of Jenkinsfile)
    $SUBSYTEM1_configs = "$PATH_TO_CONFIGS" //Optional - CONFIGS FOR one of your SUBSYSTEMS (subsystem defined on top of Jenkinsfile)
    $SUBSYTEM2_binaries = "$PATH_TO_BINARY"  //Optional - BINARIES FOR one of your SUBSYSTEMS (second subsystem defined on top of Jenkinsfile)
    $SUBSYTEM2_configs = "$PATH_TO_CONFIGS" //Optional - CONFIGS FOR one of your SUBSYSTEMS (second subsystem defined on top of Jenkinsfile)
  }
  
  deploy{
    enable = "true" //Optional - disabled by default
    branchPattern = "$BRANCH_PATTERN" //Optional - creates a condition to only deploy if branch being built matches branchPattern
    env = $DEV_ENVIRONMENT //Required if deployment is enabled - Target Development Environment
  }
  
  postBuild{
    javaDocs = "$PATH_TO_JAVA_DOCS" //Optional - enables JavaDocs publish stage ( publishes your javaDocs to Confluence )
  }
}
```

### MANDATORY BUILD PARAMETERS
```
#### MAVEN Mandatory Paramaters
env.mavenBuildFile = "pom.xml"
env.mavenBuildGoals = "clean install javadoc:javadoc -DskipITs"
env.mavenReleaseGoals = "release:prepare -B release:perform"
env.mavenSnapshotRepo = "data-snapshots-local"
env.mavenReleaseRepo = "data-releases-local"
env.buildVersion = "4.1.0"

#### GRADLE Mandatory Paramaters
env.gradleFile = "build.gradle"
env.gradleBuildGoals = "clean artifactoryPublish --info"
env.gradleReleaseGoals = "clean artifactoryPublish --info"
env.gradleDeployRepo = "data-snapshots-local"
env.buildVersion = "1.0.0"

#### NPM Mandatory Paramaters
env.npmFile = "package.json"
env.buildVersion = "1.0.0"
env.npmBuildGoals = "cd sampleappwithcfg && npm install"
env.npmReleaseGoals = "cd sampleappwithcfg && npm install --release-it"

#### DOCKER Mandatory Paramaters
env.dockerFile = "Dockerfile"
env.dockerImageName = "sdp_mvn3.3.9_jdk1.8"
```

### ARTIFACT PARAMETERS FOR BUILD METHOD PER SUBSYSTEMS DEFINED
```
$SUBSYSTEM1_binaries = "target/sampleAppWithCfg.war"
$SUBSYSTEM1_configs = "env-config"
$SUBSYSTEM2_binaries = "web-target/sampleAppWEB-content/"
$SUBSYSTEM2_configs = "web_env-config"
```

#### The definitions in your BUILD PARAMETERS navigates the methods defined in your Jenkinsfile accordingly.
