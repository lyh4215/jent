@Library('jent@main') _

import org.jent.failure.FailureLogAction
import org.jent.failure.SetBuildDescriptionAction

OnFailure(new SetBuildDescriptionAction(includeException: true, maxMessageLength: 80))
OnFailure('deploy', new FailureLogAction())

node {
    Stage('Checkout') {
        checkout scm
    }

    Stage('test') {
        sh 'make test'
    }

    Stage('build') {
        sh 'make build'
    }

    Stage('deploy') {
        sh 'make deploy'
    }
}
