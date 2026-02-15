@Library('jent@main') _

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
}
