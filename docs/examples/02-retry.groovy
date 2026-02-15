@Library('jent@main') _

node {
    Stage('Checkout') {
        checkout scm
    }

    Stage('build', [retry: 3]) {
        sh 'make build'
    }

    Stage('package', [retry: 2]) {
        sh 'make package'
    }
}
