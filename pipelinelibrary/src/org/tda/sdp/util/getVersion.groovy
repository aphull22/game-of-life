#!/bin/groovy
package org.tda.sdp.util

def execute() {
  env.VERSION = ""+env.buildVersion+"-"+env.BUILD_NUMBER+"_snapshot".replaceAll("\r", "").replaceAll("\n", "")
}
