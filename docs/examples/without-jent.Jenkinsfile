properties([
    parameters([
        choice(name: 'RUN_STAGE', choices: ['build', 'deploy'], description: 'Stage selector'),
        booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
        string(name: 'CHAOS_POINTS', defaultValue: '')
    ])
])

def chaosEnabled = params.CHAOS_ENABLED?.toString()?.toBoolean()
def chaosPoints = (params.CHAOS_POINTS ?: '')
        .toString()
        .split(/[,\s]+/)
        .findAll { it }
        .collect { it.trim().toLowerCase() }
        .toSet()

node {
    stage('Build') {
        if (params.RUN_STAGE == 'build') {
            try {
                retry(2) {
                    echo 'building...'
                }
            } catch (Exception e) {
                currentBuild.description = "Failed at stage: Build (${e.message ?: e.class.simpleName})"
                // notify slack logic
                throw e
            }
        } else {
            catchError(buildResult: 'SUCCESS', stageResult: 'NOT_BUILT') {
                error('Skipped by RUN_STAGE')
            }
        }
    }

    stage('Deploy') {
        if (params.RUN_STAGE == 'deploy') {
            try {
                retry(1) {
                    if (chaosEnabled && chaosPoints.contains('deploy.before')) {
                        error('Injected failure at chaos point: deploy.before')
                    }
                    echo 'deploying...'
                }
            } catch (Exception e) {
                currentBuild.description = "Failed at stage: Deploy (${e.message ?: e.class.simpleName})"
                // notify slack logic
                throw e
            }
        } else {
            catchError(buildResult: 'SUCCESS', stageResult: 'NOT_BUILT') {
                error('Skipped by RUN_STAGE')
            }
        }
    }
}
