#!/bin/groovy
package org.tda.sdp.util

def execute(String subsystem, String binaries, String configs) { 
  appSubsystem = subsystem
  appBinaries = binaries
  appConfigs = configs
  
  if( appBinaries != null) {
      env."${appSubsystem}_binaries" = appBinaries
  }
    
  if( appConfigs != null) {
      env."${appSubsystem}_configs" = appConfigs
  }
}
