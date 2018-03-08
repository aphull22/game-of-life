#!/bin/groovy
package org.tda.sdp.util

def execute(String testResults="**/target/surefire-reports/*.xml") {
  testReports=testResults
  junit allowEmptyResults: true, testResults: ''+testReports+''
}
