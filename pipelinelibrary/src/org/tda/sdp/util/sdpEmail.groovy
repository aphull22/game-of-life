#!/bin/groovy
package org.tda.sdp.util

def execute() {
  def emailAddress 
  if (env.addEmail){
    emailAddress = env.addEmail // support additional email addresses
  }
  
  //construct email body
  def emailBody = "Please see console output: ${BUILD_URL}" 
  emailBody = emailBody + "\n" + "JOB ID: " + BUILD_TAG
  if (fileExists('.tar')) {
    env.tarURL = readFile '.tar'
    print tarURL
    emailBody = emailBody + "\n" + tarURL
  }
  
  if (fileExists('.javaDoc')) {
    env.javaDocURL = readFile '.javaDoc'
    emailBody = emailBody + "\n" + "JavaDocs have been published to " + javaDocURL
  }
  
  if ( currentBuild.currentResult == "FAILURE" ) { //construct subject based off build result
    env.emailSubject = "Your Build is Failing" 
  }
  else {
    env.emailSubject = "Your Build Completed Sucessfully" 
  }
  
  emailext body: ''+emailBody+'',  recipientProviders: [[$class: 'RequesterRecipientProvider'], [$class: 'DevelopersRecipientProvider']], subject: ''+emailSubject+'', to: ''+emailAddress+''
}
