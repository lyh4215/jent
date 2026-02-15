@Library('jent@main') _

import org.jent.when.ParamFlagPolicy
import org.jent.failure.SetBuildDescriptionAction
import org.jent.chaos.ParameterChaosPolicy

properties([
    parameters([
        choice(name: 'RUN_STAGE', choices: ['build', 'deploy'], description: 'Stage selector'),
        booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
        string(name: 'CHAOS_POINTS', defaultValue: '')
    ])
])

// global failure behavior
OnFailure(new SetBuildDescriptionAction(includeException: true, maxMessageLength: 80))
OnFailure({ script, ctx ->
    // notify slack logic
} as org.jent.core.failure.FailureAction)

// chaos policy registration
RegisterChaos(new ParameterChaosPolicy())

node {
    env.VERBOSE = 'false'

    Stage('Build', [retry: 2, when: new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'build')]) {
        echo 'building...'
    }

    Stage('Deploy', [retry: 1, when: new ParamFlagPolicy(paramName: 'RUN_STAGE', expectedValue: 'deploy')]) {
        Chaos('deploy.before') {
            echo 'deploying...'
        }
    }
}
