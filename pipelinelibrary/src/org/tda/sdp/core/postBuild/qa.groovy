#!/bin/groovy
package org.tda.sdp.core.postBuild

def execute(){

   //build job: 'qaa/qa_sdp/master', parameters: [string(name: 'SYSTEM', value: ''+env.system+''), string(name: 'SUBSYSTEM', value: ''+env.subsystem+'')]
	build job: 'qaa/qa_sdp/master', parameters: [string(name: 'SYSTEM', value: ''+env.system+''), string(name: 'SUBSYSTEM', value: ''+env.subsystem+''), string(name: 'ENVIRONMENT', value: 'DEV'), string(name: 'VERSION', value: '5.0.0')]

}
