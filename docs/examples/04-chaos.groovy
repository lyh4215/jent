@Library('jent@main') _

import org.jent.chaos.ParameterChaosPolicy

properties([
    parameters([
        booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
        string(name: 'CHAOS_POINTS', defaultValue: '')
    ])
])

RegisterChaos(new ParameterChaosPolicy())

node {
    Stage('Checkout') {
        checkout scm
    }

    Stage('build') {
        Chaos('build.command') {
            sh 'make build'
        }
    }
}
