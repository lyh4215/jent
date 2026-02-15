@Library('jent@main') _

import org.jent.when.BranchPatternPolicy
import org.jent.when.ParamFlagPolicy

properties([
    parameters([
        booleanParam(name: 'RUN_DEPLOY', defaultValue: false),
        booleanParam(name: 'SKIP_DEPLOY', defaultValue: false)
    ])
])

def deployWhen = When().and([
        new BranchPatternPolicy(patterns: ['main', 'release/*']),
        When().not(new ParamFlagPolicy(paramName: 'SKIP_DEPLOY', expectedValue: true)),
        When().or([
                new ParamFlagPolicy(paramName: 'RUN_DEPLOY', expectedValue: true),
                new BranchPatternPolicy(patterns: ['hotfix/*'])
        ])
])

node {
    Stage('build') {
        sh 'make build'
    }

    Stage('deploy', [when: deployWhen]) {
        sh 'make deploy'
    }
}
