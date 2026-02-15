# Jent

Type-oriented abstractions for Jenkins Scripted Pipelines.

Jenkins pipelines are powerful, but raw scripted logic quickly becomes noisy.
Jent adds a higher-level layer on top of Jenkins with composable primitives:
`Stage`, `When`, `Retry`, `Failure`, and `Chaos`.

The goal is simple: make pipeline code easier to read, safer to evolve, and more consistent across teams.

## Why Jent

- Reduce repetitive Jenkinsfile boilerplate (`if`, `retry`, `try/catch`, ad-hoc handlers)
- Express failure handling and fault injection as explicit policies/actions
- Keep build-scoped state inside internal registries instead of leaking state logic into Jenkinsfiles

## What You Get

- `Stage(id, opts) { ... }`
- `When` policy execution control via `opts.when`
- `Retry` control via `opts.retry`
- `OnFailure(...)` for stage-level and global failure handlers
- `RegisterChaos(...)` + `Chaos(...)` for controlled fault injection points
- Build-scoped internal registry state
- Optional verbose logging via `VERBOSE=true`

## Why not plain Declarative only?

Declarative Jenkinsfiles are great for baseline structure, but they are still Jenkins-syntax centric.
Jent adds a reusable, type-oriented layer on top of Jenkins syntax:
- policy/action interfaces (`WhenPolicy`, `FailureAction`, `ChaosPolicy`)
- composable execution model (`Stage` + `When` + `Retry` + `Failure` + `Chaos`)
- shared behavior through internal registries instead of per-pipeline wiring

## Quick Start (Scripted Pipeline)

More complete side-by-side examples are in `docs/examples`:
- `docs/examples/with-jent.Jenkinsfile`
- `docs/examples/without-jent.Jenkinsfile`

### Without Jent (raw scripted pipeline)

```groovy
node {
    properties([
        parameters([
            booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
            string(name: 'CHAOS_POINTS', defaultValue: ''),
            string(name: 'DEPLOY_ENV', defaultValue: 'dev')
        ])
    ])

    def chaosEnabled = params.CHAOS_ENABLED?.toString()?.toBoolean()
    def chaosPoints = (params.CHAOS_POINTS ?: '')
            .toString()
            .split(/[,\s]+/)
            .findAll { it }
            .collect { it.trim().toLowerCase() }
            .toSet()

    stage('Build') {
        if (env.BRANCH_NAME == 'main') {
            try {
                retry(2) {
                    sh 'make build'
                }
            } catch (Exception e) {
                // notify slack logic
                throw e
            }
        } else {
            catchError(buildResult: 'SUCCESS', stageResult: 'NOT_BUILT') {
                error("Skipped by condition: Build")
            }
        }
    }

    stage('Deploy') {
        try {
            retry(1) {
                if (chaosEnabled && chaosPoints.contains('deploy.before')) {
                    catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                        error("Injected failure at chaos point: deploy.before")
                    }
                }

                sh "make deploy ENV=${params.DEPLOY_ENV}"

                if (params.DEPLOY_ENV == 'prod' && chaosEnabled && chaosPoints.contains('deploy.after')) {
                    catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                        error("Injected failure at chaos point: deploy.after")
                    }
                }
            }
        } catch (Exception e) {
            currentBuild.description = "Failed at stage: Deploy"
            // notify slack logic
            throw e
        }
    }
}
```

### With Jent

```groovy
@Library('jent@main') _

import org.jent.when.BranchPatternPolicy
import org.jent.failure.FailureLogAction
import org.jent.chaos.ParameterChaosPolicy

properties([
    parameters([
        booleanParam(name: 'CHAOS_ENABLED', defaultValue: false),
        string(name: 'CHAOS_POINTS', defaultValue: '')
    ])
])

OnFailure('Deploy', new FailureLogAction())

RegisterChaos(new ParameterChaosPolicy())

node {
    env.VERBOSE = 'false' // true|1|yes|on enables verbose logs

    Stage('Build', [retry: 2, when: BranchPatternPolicy]) {
        echo 'build...'
    }

    Stage('Deploy', [retry: 1]) {
        Chaos('deploy.before') {
            echo 'deploy...'
        }
    }
}
```

### Why this is better

- Less boilerplate in Jenkinsfile
- Failure and chaos behavior are declared, not hand-wired per stage
- Policy and action logic become reusable typed-like components
- Build-scoped registry state is managed internally instead of ad-hoc maps

## Core APIs

### Stage

```groovy
Stage(String id, Map opts = [:], Closure body)
```

Options:
- `retry` (default: `1`)
- `when` (`WhenPolicy` class or instance)

Behavior:
- If `when` does not allow execution, stage is marked `NOT_BUILT`
- Stage body is retried according to `retry`
- On exception, registered `FailureAction`s are executed, then the exception is re-thrown

When composition example:

```groovy
import org.jent.when.BranchPatternPolicy
import org.jent.when.ParamFlagPolicy

Stage('Deploy', [
    when: When().and([
        new BranchPatternPolicy(patterns: ['main', 'release/*']),
        When().not(new ParamFlagPolicy(paramName: 'SKIP_DEPLOY', expectedValue: true)),
        When().or([
            new ParamFlagPolicy(paramName: 'RUN_DEPLOY', expectedValue: true),
            new BranchPatternPolicy(patterns: ['hotfix/*'])
        ])
    ])
]) {
    echo 'deploy...'
}

Stage('Smoke', [
    when: When().of(new BranchPatternPolicy(patterns: ['main']))
]) {
    echo 'smoke...'
}
```

### OnFailure

```groovy
OnFailure(String stageId, FailureAction action) // stage-specific
OnFailure(FailureAction action)                 // global
```

### Chaos

```groovy
RegisterChaos(ChaosPolicy policy)
Chaos(String pointId) { ... }
```

- Register one or more chaos policies
- `Chaos("point")` evaluates registered policies and injects failures on match
- `ParameterChaosPolicy` uses `CHAOS_ENABLED` and `CHAOS_POINTS`

## Built-in Policies and Actions

When policies:
- `org.jent.when.BranchPatternPolicy`
- `org.jent.when.ParamFlagPolicy`
- `org.jent.core.when.AndPolicy`
- `org.jent.core.when.OrPolicy`
- `org.jent.core.when.NotPolicy`

Failure actions:
- `org.jent.failure.FailureLogAction`
- `org.jent.failure.SetBuildDescriptionAction`

Chaos policies:
- `org.jent.chaos.ParameterChaosPolicy`
- `org.jent.chaos.EnvChaosPolicy`

## Registry Design

Jent keeps runtime state in internal registries:

- `FailureRegistryState` -> `FailureRegistryData`
- `ChaosRegistryState` -> `ChaosRegistryData` / `ChaosRegistry`

Design points:
- Build-scoped isolation using `currentBuild.rawBuild`
- `WeakHashMap` lifecycle-friendly storage
- Jenkinsfiles do not manage registry internals directly

## Verbose Logging

Chaos-related debug logs are centralized through a shared logger.

- Enable: `env.VERBOSE = 'true'`
- Disable: unset or `false`
- Accepted truthy values: `true`, `1`, `yes`, `on`

## Type-oriented, not fully static-typed

Jent uses interface-driven abstractions (`WhenPolicy`, `FailureAction`, `ChaosPolicy`) and typed domain objects (`FailureContext`, registry data models).

Because Jenkins Scripted Pipeline runs on Groovy with dynamic runtime behavior, this is best described as **type-oriented** or **typed-like abstractions**, not strict compile-time type safety.

## Philosophy

Treat Jenkins pipelines as a composable execution model:

- `When` = execution filter
- `Retry` = execution wrapper
- `Stage` = execution unit
- `Failure` = failure side-effect router
- `Chaos` = controlled fault injection

Jent exists to raise the abstraction level of Jenkins Scripted Pipelines while staying practical for real CI/CD operations.
