#!/usr/bin/groovy
package org.tda.sdp.core.preBuild

def versionControl() {
  checkout scm
}
