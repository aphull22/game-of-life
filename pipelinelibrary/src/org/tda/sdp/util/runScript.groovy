#!/bin/groovy
package org.tda.sdp.util

def execute(String task){ //support pre/post commands
  if ( task != null ) {
      taskExec = "$task"
      sh "${taskExec}"
  }
}
