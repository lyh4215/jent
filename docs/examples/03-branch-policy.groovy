@Library('jent@main') _

import org.jent.when.BranchPatternPolicy

node {
    Stage('Checkout') {
        checkout scm
    }

    Stage('test') {
        sh 'make test'
    }

    Stage('deploy', [when: new BranchPatternPolicy(patterns: ['main'])]) {
        sh 'make deploy'
    }
}
