@Library('jent@main') _

node {
    Stage('test') {
        sh 'make test'
    }

    Stage('build') {
        sh 'make build'
    }
}
